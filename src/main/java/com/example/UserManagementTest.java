package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class UserManagementTest {
    private static final Logger logger = LogManager.getLogger(UserManagementTest.class);
    private WebDriver driver;
    private Properties config;
    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private ProfilePage profilePage;
    private WelcomePage welcomePage;

    @BeforeClass
    public void setUp() {
        try {
            config = new Properties();
            try (InputStream configFile = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (configFile == null) {
                    throw new RuntimeException("config.properties не найдено");
                }
                config.load(configFile);
            }

            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS); // Increased to 10 seconds
            driver.get(config.getProperty("base.url"));

            loginPage = new LoginPage(driver);
            registrationPage = new RegistrationPage(driver);
            profilePage = new ProfilePage(driver);
            welcomePage = new WelcomePage(driver);

            logger.info("Сборка теста прошла успешно");
        } catch (Exception e) {
            logger.error("Ошибка в сборке: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 1)
    public void testUserRegistration() {
        logger.info("Начало теста регистрации");

        registrationPage.navigateTo();
        registrationPage.enterEmail(config.getProperty("test.email"));
        registrationPage.enterUsername(config.getProperty("test.username"), "Test");
        registrationPage.enterPassword(config.getProperty("test.password"));
        registrationPage.submitRegistration();

        String successMessage = welcomePage.getWelcomeMessage();
        Assert.assertTrue(successMessage.contains("Welcome"),
                "Сообщение, что регистрация прошла успешно не вывелось");
        logger.info("Тест регистрации прошел успешно");
    }

    @Test(priority = 2)
    public void testProfileUpdate() {
        logger.info("Начало теста по редактированию профилю");

        profilePage.navigateTo();
        String newUsername = config.getProperty("test.newUsername");
        String currentPassword = config.getProperty("test.password");
        String newPassword = config.getProperty("test.newPassword");

        profilePage.updateProfile(newUsername, currentPassword, newPassword);
        profilePage.saveChanges();

        Assert.assertTrue(profilePage.isUpdateSuccessful(),
                "Сообщение, что редактирование профиля прошла успешно не вывелось");
        logger.info("Тест обновления профиля прошла успешно");
    }

    @Test(priority = 3)
    public void testLogoutAndReLogin() {
        logger.info("Тест выхода и релогина");

        profilePage.logout();
        loginPage.navigateTo();
        loginPage.enterEmail(config.getProperty("test.email"));
        loginPage.enterPassword(config.getProperty("test.newPassword"));
        loginPage.submitLogin();

        Assert.assertTrue(driver.getCurrentUrl().contains("my-account"),
                "Не перевел на страницу профиля");
        logger.info("Тест выхода и релогина прошла успешно");
    }

    @AfterClass
    public void tearDown() {
        logger.info("Освобождение ресурсов...");
        if (driver != null) {
            driver.quit();
        }
    }
}