package com.example.Crawling;

import com.example.Crawling.controller.CrawlController;
import com.example.Crawling.dao.KeywordDao;
import com.example.Crawling.dao.KeywordTagDao;
import com.example.Crawling.entity.KeywordTag;
import com.example.Crawling.service.IlbeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(CrawlController.class)
public class WebTest {
    private MockMvc mvc;


    @MockBean
    private IlbeService ilbeService;


    @BeforeEach
    public void setUp() {
        mvc =
                MockMvcBuilders.standaloneSetup(new CrawlController(ilbeService))
                        .addFilters(new CharacterEncodingFilter("UTF-8", true)) // utf-8 필터 추가
                        .build();
    }

    @Test
    public void listTest2() throws Exception {
        ResultActions result = mvc.perform(
                get("/keyword/23")
        );

    }

}
