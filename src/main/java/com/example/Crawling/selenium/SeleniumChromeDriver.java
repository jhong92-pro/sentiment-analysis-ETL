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
        String WEB_DRIVER_PATH = "C:\\Users\\Jun Seung Hong\\OneDrive\\바탕 화면\\Coding\\project\\Crawling\\src\\main\\resources\\chromedriver.exe";
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        options.addArguments("headless"); // 크롬 창 띄우지 않음
        driver = new ChromeDriver(options);
    }
    private SeleniumChromeDriver() {
    }

    public static WebDriver getDriver() {
        return driver;
    }
}