package com.example.Crawling;

import com.example.Crawling.selenium.SeleniumRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SeleniumTest {

    SeleniumRequest seleniumRequest = new SeleniumRequest();

    @Test
    public void test() {
        List<String> urlList = new ArrayList<>();
        urlList.add("/view/11425896961?page=816&listStyle=list");
        urlList.forEach(
                url -> {
                    Assertions.assertThatCode(() -> seleniumRequest.request(url))
                            .doesNotThrowAnyException();
                }
        );
    }
}