package com.example.Crawling;

import com.example.Crawling.config.JasyptConfig;
import com.example.Crawling.dao.SentimentPreStageDao;
import com.example.Crawling.entity.SentimentPreStage;
import com.example.Crawling.model.SentimentCode;
import com.example.Crawling.model.SentimentResponse;
import com.example.Crawling.selenium.SeleniumRequest;
import com.example.Crawling.service.IlbeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
@ExtendWith(MockitoExtension.class)
@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
public class ConsumerLogicTest {

    @SpyBean //@Mock은 stub된 함수만 사용 가능
    private IlbeService ilbeService;

    @Autowired
    private SentimentPreStageDao sentimentPreStageDao;

    @Test
    public void crawlIlbeTest() throws Exception {
        //given
        SentimentResponse sentimentResponse = new SentimentResponse();
        sentimentResponse.setNegative(1);
        sentimentResponse.setNeutral(1);
        sentimentResponse.setPositive(1);

        doReturn(sentimentResponse).when(ilbeService).requestSentimentById(anyList());

        //when   test때문에 dao를 건드리지는 않음
        List<Integer> sentimentPreStageList = getUnProcessedItems();
        ilbeService.crawlIlbe(sentimentPreStageList);

        //then
        List<Integer> sentimentPreStageNextList = getUnProcessedItems();
        Set<Integer> result = sentimentPreStageList.stream()
                .distinct()
                .filter(sentimentPreStageNextList::contains)
                .collect(Collectors.toSet());
        // 다시 가져왔을 때 중복 없어야 함
        Assertions.assertThat(result.size()).isZero();
    }

    private List<Integer> getUnProcessedItems(){
        List<SentimentPreStage> sentimentPreStageList = sentimentPreStageDao.findAll();
        return sentimentPreStageList.stream()
                .filter(sentimentPreStage->{
                    boolean condition1 = sentimentPreStage.getSentimentCode()==SentimentCode.NEUTRAL;
                    boolean condition2 = !sentimentPreStage.isFinished();
                    return condition1&&condition2;
                })
                .map(SentimentPreStage::getSentimentPreStageId)
                .sorted()
                .limit(5)
                .toList();
    }
}