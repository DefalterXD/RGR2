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

public class RegistrationPage {
    private static final Logger logger = LogManager.getLogger(RegistrationPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "email_create")
    private WebElement emailField;

    @FindBy(id = "SubmitCreate")
    private WebElement createAccountButton;

    @FindBy(id = "customer_firstname")
    private WebElement firstNameField;

    @FindBy(id = "customer_lastname")
    private WebElement lastNameField;

    @FindBy(id = "passwd")
    private WebElement passwordField;

    @FindBy(id = "submitAccount")
    private WebElement registerButton;

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("http://automationpractice.pl/index.php?controller=authentication&back=my-account");
        wait.until(ExpectedConditions.visibilityOf(emailField));
        logger.info("Переход на страницу регистрации");
    }

    public void enterEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(emailField));
            emailField.clear();
            emailField.sendKeys(email);
            createAccountButton.click();
            wait.until(ExpectedConditions.visibilityOf(firstNameField));
            logger.info("Введеный email: " + email);
        } else {
            logger.error("Неправльно дан email");
            throw new IllegalArgumentException("Email не может быть пустым");
        }
    }

    public void enterUsername(String firstName, String lastName) {
        wait.until(ExpectedConditions.visibilityOf(firstNameField));
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        logger.info("Введено имя: " + firstName + " и фамилия: " + lastName);
    }

    public void enterPassword(String password) {
        if (password != null && !password.trim().isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            passwordField.sendKeys(password);
            logger.info("Введеный пароль");
        } else {
            logger.error("Неправльно предоставленный пароль");
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
    }

    public void submitRegistration() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        registerButton.click();
        logger.info("Форма регистрации отправлена");
    }
}