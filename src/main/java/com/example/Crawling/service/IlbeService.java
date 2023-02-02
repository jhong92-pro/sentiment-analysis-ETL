package com.example.Crawling.service;

import com.example.Crawling.dao.*;
import com.example.Crawling.entity.CrawlResult;
import com.example.Crawling.entity.Keyword;
import com.example.Crawling.entity.KeywordTag;
import com.example.Crawling.entity.SentimentPreStage;
import com.example.Crawling.model.*;
import com.example.Crawling.producer.CrawlProducer;
import com.example.Crawling.producer.SentimentProducer;
import com.example.Crawling.selenium.SeleniumRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class IlbeService {
    private final SentimentPreStageDao sentimentPreStageDao;
    private final KeywordDao keywordDao;
    private final KeywordTagDao keywordTagDao;
    private final CrawlResultDao crawlResultDao;

    private final String baseURL = "http://localhost:8000";
    private final CrawlProducer crawlProducer;
    private final SentimentProducer sentimentProducer;

    private final SeleniumRequest seleniumRequest = new SeleniumRequest();

    @Transactional
    public void sendSentimentAnalysisIteration(KeywordRequest keywordRequest) throws RuntimeException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date startDate = formatter.parse(keywordRequest.getStartDate());
        Date endDate = formatter.parse(keywordRequest.getEndDate());

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int keywordId = keywordRequest.getKeywordId();

        String keyword = keywordDao.findById(keywordId)
                .orElseThrow(RuntimeException::new)
                .getKeyword();
        log.info(keywordTagDao.findAllByKeywordId(keywordId).toString());
        List<String> keywordTagList = new java.util.ArrayList<>(keywordTagDao.findAllByKeywordId(keywordId)
                .stream()
                .map(KeywordTag::getTagName)
                .toList());
        keywordTagList.add(keyword);

        List<CrawlResult> crawlResultList = getCrawlResultData(keywordTagList, DomainCode.ILBE);
        List<Integer> sentimentPreStageIdListKafka = new ArrayList<>();
        int rowCnt = 0;
        for(SentimentCode sentimentCode: SentimentCode.values()) {
            List<SentimentPreStage> sentimentPreStageList = crawlResultList.stream()
                    .map(crawlResult -> SentimentPreStage.builder()
                            .crawlResult(crawlResult)
                            .sentimentCode(sentimentCode)
                            .keywordId(keywordId)
                            .build())
                    .toList();
            List<Integer> sentimentPreStageIdList = sentimentPreStageDao.saveAll(sentimentPreStageList)
                    .stream().map(SentimentPreStage::getSentimentPreStageId)
                    .toList();
            rowCnt+=sentimentPreStageIdList.size();
            if (sentimentCode.equals(SentimentCode.NEUTRAL)) {
                sentimentPreStageIdListKafka.addAll(sentimentPreStageIdList);
                // 감정 하나만 Producer에 넣으면 됨 (sentimentprestageId 중복해서 안 들어가게)
            }
        }
        if (rowCnt==0){ // 검색결과없음
            SentimentPreStage sentimentPreStage = SentimentPreStage.builder()
                    .sentimentCode(SentimentCode.NEUTRAL)
                    .total(0)
                    .isFinished(true)
                    .keywordId(keywordId)
                    .build();
            sentimentPreStageDao.save(sentimentPreStage);
            return;
        }

        produceSentimentPreStageIdList(sentimentPreStageIdListKafka);
        }

    private void produceSentimentPreStageIdList(List<Integer> sentimentPreStageIdListKafka) throws RuntimeException {
        //100개씩 잘라서 producer 생성
        int listSize = sentimentPreStageIdListKafka.size();
        log.info("listSize : " + listSize);
        int divSize = 1; // TODO: 설정값으로 뺄 수 있을 지
        int repeat =  listSize % divSize != 0 ? listSize  / divSize + 1 : listSize  / divSize;
        // Producer chunk로 한번에 보낼수없음... batch.size를 dynamic하게 수정 불가
        for(int i=0;i<repeat;i++) {
            if (i != (repeat - 1)) {
                List<Integer> message = new ArrayList<>(sentimentPreStageIdListKafka.subList(i * divSize, (i + 1) * divSize));
                log.info("message : "+Arrays.toString(message.toArray()));
                crawlProducer.async("crawl", CrawlRequest.builder().sentimentPreStageIdList(message).build());
//                crawlProducer.async("crawl", message);
            } else {
                List<Integer> message = new ArrayList<>(sentimentPreStageIdListKafka.subList(i * divSize, listSize));
                crawlProducer.async("crawl",CrawlRequest.builder().sentimentPreStageIdList(message).build());
//                crawlProducer.async("crawl", message);
            }
        }

    }

    @Transactional
    public void crawlIlbe (List<Integer> sentimentPreStageIdList) throws Exception {
        // TODO List <-> CrawlRequest 형 변환 제거 (kafka에서 바로 List<Integer> 전송방법 있을듯함)
        List<SentimentPreStage> sentimentPreStageList = sentimentPreStageDao.findBySentimentPreStageIdIn(sentimentPreStageIdList);
        log.info(Arrays.toString(sentimentPreStageList.toArray()));
        HashMap<Integer, List<String>> sentenceListById = new HashMap<>();
        Integer keywordId = sentimentPreStageList.get(0).getKeywordId();
        List<String> wordList = new ArrayList<>(keywordTagDao.findAllByKeywordId(keywordId).
                stream().map(KeywordTag::getTagName).toList());
        String keyword
                = keywordDao.findById(keywordId)
                .orElseThrow(Exception::new)
                .getKeyword();
        wordList.add(keyword);

        for(SentimentPreStage sentimentPreStage:sentimentPreStageList){

            CrawlResult crawlResult = sentimentPreStage.getCrawlResult();
            Integer crawlResultId = crawlResult.getCrawlResultId();
            if (!sentenceListById.containsKey(crawlResultId)){
                sentenceListById.put(crawlResultId,new ArrayList<>());
            }

            List<String> sentenceList = seleniumRequest.request(crawlResult.getUrl());

            sentenceList = filterCrawlResponse(wordList, crawlResult, sentenceList);

            sentenceListById.put(crawlResultId,sentenceList); //TODO : sentenceListById 삭제
            SentimentResponse sentimentResponse = requestSentimentById(sentenceList);
            saveSentimentResponse(keywordId, crawlResult, sentimentResponse);
        }
    }

    private static List<String> filterCrawlResponse(List<String> wordList, CrawlResult crawlResult, List<String> sentenceList) {
        String body;
        if (sentenceList.size()!=0 && sentenceList.get(sentenceList.size()-1).length()<10){
            sentenceList.set(sentenceList.size()-1, crawlResult.getTitle());
        }
        //TODO : body 로직 개선 sentenceList의 가장 뒤가 body, body는 문장에서 word 존재하는지 check에 걸리지 않음
        if (sentenceList.size()==0){
            return sentenceList;
        }
        body = sentenceList.remove(sentenceList.size() - 1);
        sentenceList =new ArrayList<>(sentenceList.stream()
                .filter(sentence->{
                    for(String word: wordList){
                        if(sentence.contains(word)){
                            return true;
                        }
                    }
                    return false;
                })
                .toList());
        if (!"".equals(body))
            sentenceList.add(body);
        return sentenceList;
    }

    private void saveSentimentResponse(Integer keywordId, CrawlResult crawlResult, SentimentResponse sentimentResponse) throws Exception {
        SentimentPreStage positivePrestage = sentimentPreStageDao.findByKeywordIdAndSentimentCodeAndCrawlResult(keywordId, SentimentCode.POSITIVE, crawlResult)
                .orElseThrow(Exception::new);
        positivePrestage.setTotal(sentimentResponse.getPositive());
        positivePrestage.setFinished(true);
        SentimentPreStage neutralPrestage = sentimentPreStageDao.findByKeywordIdAndSentimentCodeAndCrawlResult(keywordId, SentimentCode.NEUTRAL, crawlResult)
                .orElseThrow(Exception::new);
        neutralPrestage.setTotal(sentimentResponse.getNeutral());
        neutralPrestage.setFinished(true);
        SentimentPreStage negativePrestage = sentimentPreStageDao.findByKeywordIdAndSentimentCodeAndCrawlResult(keywordId, SentimentCode.NEGATIVE, crawlResult)
                .orElseThrow(Exception::new);
        negativePrestage.setTotal(sentimentResponse.getNegative());
        negativePrestage.setFinished(true);
    }


    public List<CrawlResult> getCrawlResultData(List<String> keywordTagList, DomainCode domainCode){
        if (keywordTagList.size() ==0){
            return  null;
        }

        Specification<CrawlResult> spec = CrawlResultSpecification.likeTitleAndEqualDomain(keywordTagList, domainCode);
        return crawlResultDao.findAll(spec);
    }

    // not used 감정분석 kafka topic 따로 있으면 쓸 수도 있음
    public SentimentResponse requestSentiment(HashMap<Integer, List<String>> sentenceListById) throws JsonProcessingException {
        RestTemplate sentimentRestTemplate = SentimentRestTemplate.getRestTemplate();
        SentimentResponse sentimentResponse = sentimentRestTemplate.postForObject(baseURL+"/sentiment/ko", sentenceListById, SentimentResponse.class);

        return sentimentResponse;
    }

    public SentimentResponse requestSentimentById(List<String> sentenceList) throws JsonProcessingException {


        RestTemplate sentimentRestTemplate = SentimentRestTemplate.getRestTemplate();
        HashMap<String, List<String>>bodyParamMap = new HashMap<>();
        bodyParamMap.put("sentences", sentenceList);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        String reqBodyData = new ObjectMapper().writeValueAsString(bodyParamMap);
        HttpEntity<String> request = new HttpEntity<>(reqBodyData, headers);
        log.debug("request : "+request.getBody());
        SentimentResponse sentimentResponse = sentimentRestTemplate.postForObject(baseURL+"/sentiment/ko", request, SentimentResponse.class);

        log.debug(sentimentResponse.toString());
        return sentimentResponse;
    }

}
