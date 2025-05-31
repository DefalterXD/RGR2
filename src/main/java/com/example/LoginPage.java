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

public class LoginPage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "passwd")
    private WebElement passwordField;

    @FindBy(id = "SubmitLogin")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        int retries = 3;
        while (retries > 0) {
            try {
                driver.get("http://automationpractice.pl/index.php?controller=authentication&back=my-account");
                wait.until(ExpectedConditions.visibilityOf(emailField));
                logger.info("Перейти на страницу логина");
                return;
            } catch (Exception e) {
                logger.warn("Ошибка загрузки страницы логина, попыток осталось: " + retries);
                retries--;
                if (retries == 0) {
                    logger.error("Не мог перейти на страницу логина: " + e.getMessage());
                    throw e;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void enterEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(emailField));
            emailField.clear();
            emailField.sendKeys(email);
            logger.info("Введенный email: " + email);
        } else {
            logger.error("Неправильно дан email ");
            throw new IllegalArgumentException("Email не может быть пустым");
        }
    }

    public void enterPassword(String password) {
        if (password != null && !password.trim().isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            passwordField.clear();
            passwordField.sendKeys(password);
            logger.info("Введеный пароль");
        } else {
            logger.error("Неправильно предоставленный пароль");
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
    }

    public void submitLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
        logger.info("Отправлена форма для логина");
    }
}