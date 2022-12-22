package com.example.Crawling;

import com.example.Crawling.controller.CrawlController;
import com.example.Crawling.dao.KeywordDao;
import com.example.Crawling.dao.KeywordTagDao;
import com.example.Crawling.entity.KeywordTag;
import com.example.Crawling.service.IlbeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class JpaTest {
    private MockMvc mvc;
    @Autowired
    private KeywordTagDao keywordTagDao;

    @MockBean
    private IlbeService ilbeService;

    @Autowired
    private KeywordDao keywordDao;

    @BeforeEach
    public void setUp() {
        mvc =
                MockMvcBuilders.standaloneSetup(new CrawlController(ilbeService))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true)) // utf-8 필터 추가
                        .build();
    }

    @Test
    public void listTest(){
        List<String> keywordTagList1 = new java.util.ArrayList<>(keywordTagDao.findAllByKeywordId(23)
                .stream()
                .map(KeywordTag::getTagName)
                .map(keywordTag -> "%" + keywordTag + "%")
                .toList());
        List<String> keywordTagList2 = keywordTagDao.findAllByKeywordId(23)
                .stream()
                .map(KeywordTag::getTagName)
                .toList();
        String keyword = keywordDao.findById(23)
                .orElseThrow(RuntimeException::new)
                .getKeyword();
        System.out.println( keywordTagList2.toString());
        System.out.println( keyword);
        System.out.println( keywordTagList1.toString());
        keywordTagList1.add("%" + keyword + "%");
    }

    @Test
    public void listTest2() throws Exception {
        mvc.perform(
                get("/keyword/23")
        );

    }

    @Test
    public void listTest3() throws Exception {
        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        ilbeService.crawlIlbe(a);


    }

}
