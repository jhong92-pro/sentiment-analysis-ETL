package com.example.Crawling.service;

import com.example.Crawling.dao.CrawlResultDao;
import com.example.Crawling.dao.KeywordDao;
import com.example.Crawling.dao.KeywordTagDao;
import com.example.Crawling.entity.CrawlResult;
import com.example.Crawling.entity.KeywordTag;
import com.example.Crawling.model.KeywordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FMKoreaService {
    public final KeywordDao keywordDao;
    public final KeywordTagDao keywordTagDao;
    public final CrawlResultDao crawlResultDao;

    public void sentimentAnalysisIteration(KeywordRequest keywordRequest) throws ParseException {
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

        List<String> keywordTagList = keywordTagDao.findAllByKeywordId(keywordId)
                .stream()
                .map(KeywordTag::getTagName)
                .toList();
        keywordTagList.add(keyword);

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyymmdd");
            String currDate = dateFormat.format(date);
            sentimentAnalysis(currDate, keywordId, keywordTagList, keyword);
        }
    }

    @Transactional
    public void sentimentAnalysis(String date, int keywordId, List<String> keywordTagList, String keyword){
        List<CrawlResult> crawlResultList = crawlResultDao.findAllByDomainAndYmd(DomainCode.FMKOREA, date);

    }
}
