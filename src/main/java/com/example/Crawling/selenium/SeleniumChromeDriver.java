package com.example.Crawling.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumChromeDriver {

    private static WebDriver driver;
    private static int crawlCnt;
    private static final ChromeOptions options;
    static {
        options = new ChromeOptions();
        String WEB_DRIVER_ID = "webdriver.chrome.driver";
        //    @Value("${web.driver.path}")
        String WEB_DRIVER_PATH = "/home/jun/server/crawl-ETL/driver/chromedriver";
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        options.addArguments("headless"); // 크롬 창 띄우지 않음
        options.addArguments("no-sandbox");
        options.addArguments("disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        crawlCnt = 0;
    }
    private SeleniumChromeDriver() {
    }

    public static WebDriver getDriver() {
        if(crawlCnt++>=70){
            driver.quit();
            driver = new ChromeDriver(options);
        }
        return driver; // Fix invalid Session id
    }
}