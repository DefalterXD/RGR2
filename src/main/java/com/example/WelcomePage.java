package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WelcomePage {
    private static final Logger logger = LogManager.getLogger(WelcomePage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = ".info-account")
    private WebElement welcomeMessage;

    public WelcomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }

    public String getWelcomeMessage() {
        wait.until(ExpectedConditions.visibilityOf(welcomeMessage));
        String message = welcomeMessage.getText();
        logger.info("Получить сообщение о успешном переходе на личный профиль: " + message);
        return message;
    }
}