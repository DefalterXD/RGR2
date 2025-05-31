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

public class ProfilePage {
    private static final Logger logger = LogManager.getLogger(ProfilePage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "firstname")
    private WebElement firstNameField;

    @FindBy(id = "old_passwd")
    private WebElement currentPasswordField;

    @FindBy(id = "passwd")
    private WebElement newPasswordField;

    @FindBy(id = "confirmation")
    private WebElement confirmPasswordField;

    @FindBy(css = "button[name='submitIdentity']")
    private WebElement saveButton;

    @FindBy(css = "a.logout")
    private WebElement logoutLink;

    @FindBy(css = ".alert-success")
    private WebElement successMessage;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("http://automationpractice.pl/index.php?controller=identity");
        wait.until(ExpectedConditions.visibilityOf(firstNameField));
        logger.info("Перейти на страницу редактирования профиля");
    }

    public void updateProfile(String newUsername, String currentPassword, String newPassword) {
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(firstNameField));
            firstNameField.clear();
            firstNameField.sendKeys(newUsername);
            logger.info("Обновленный никнейм: " + newUsername);
        }
        if (newPassword != null && !newPassword.trim().isEmpty() && currentPassword != null && !currentPassword.trim().isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(currentPasswordField));
            currentPasswordField.sendKeys(currentPassword);
            newPasswordField.sendKeys(newPassword);
            confirmPasswordField.sendKeys(newPassword);
            logger.info("Обновлён пароль");
        }
    }

    public void saveChanges() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveButton.click();
        try {
            wait.until(ExpectedConditions.visibilityOf(successMessage));
            logger.info("Изменения профился сохранились успешно");
        } catch (Exception e) {
            logger.warn("Сообщение о успешном сохранении не вывелось, изменения не сохранены");
        }
    }

    public String getDisplayedUsername() {
        wait.until(ExpectedConditions.visibilityOf(firstNameField));
        String username = firstNameField.getAttribute("value");
        logger.info("Получить выводимый никнейм: " + username);
        return username;
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        logoutLink.click();
        logger.info("Выход");
    }

    public boolean isUpdateSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successMessage));
            return successMessage.getText().contains("successfully updated");
        } catch (Exception e) {
            return false;
        }
    }
}