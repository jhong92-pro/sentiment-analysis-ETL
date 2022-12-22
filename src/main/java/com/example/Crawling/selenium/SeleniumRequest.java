package com.example.Crawling.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class SeleniumRequest {

    private static long prevRequestTime = 0L;

    public List<String> request(String url) throws InterruptedException {
        Thread.sleep(1300);
        WebDriver driver = SeleniumChromeDriver.getDriver();
        System.out.println("url : " + url);
        driver.get("https://www.ilbe.com" +url);
        try{
            WebElement bodyElement = driver.findElement(By.xpath("//*[@id='content']"));
            String body = bodyElement.getAttribute("value")
                    .replaceAll("http://[^\\s]+", "")
                    .replaceAll("< ?img(.*?)>", "") // 이미지 제거
                    .replaceAll("<[^>]*>", "") // 태그 제거
                    .replaceAll("\\xa0","");

            List<String> sentenceList = new ArrayList<>(driver.findElements(By.className("cmt"))
                    .stream()
                    .map(webElement -> webElement.getText()
                            .replaceAll("http[s|]://[^\\s]+", "")
                            .replaceAll("<[^>]*>", "")
                            .replaceAll("\\xa0","")
                    )
                    .toList());
            sentenceList.add(body);

            return sentenceList;
        }catch (NoSuchElementException e){
            return new ArrayList<>();
        }
    }
}
