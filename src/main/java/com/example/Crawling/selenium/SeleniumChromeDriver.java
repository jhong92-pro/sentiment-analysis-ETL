package com.example.Crawling.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class SeleniumChromeDriver {

    private static final WebDriver driver;
    static{
        ChromeOptions options = new ChromeOptions();
        String WEB_DRIVER_ID = "webdriver.chrome.driver";
        //    @Value("${web.driver.path}")
        String WEB_DRIVER_PATH = "/home/jun/server/crawl-ETL/driver/chromedriver";
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        options.addArguments("headless"); // 크롬 창 띄우지 않음
        options.addArguments("no-sandbox");
        options.addArguments("disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }
    private SeleniumChromeDriver() {
    }

    public static WebDriver getDriver() {
        return driver;
    }
}