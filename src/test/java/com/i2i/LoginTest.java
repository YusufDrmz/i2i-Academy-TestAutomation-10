package com.i2i;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {

    private static WebDriver driver;

    @BeforeAll
    static void setup() {
        ChromeOptions options = new ChromeOptions();
        // Tarayıcıyı görünür şekilde aç (istersan headless için "--headless" ekle)
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    @Order(1)
    @DisplayName("Successful Login Test")
    void testSuccessfulLogin() {
        // 1. Siteye git
        driver.get("https://www.saucedemo.com");

        // 2. Username ve password alanlarını bul, doldur
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");

        // 3. Login butonuna tıkla
        driver.findElement(By.id("login-button")).click();

        // 4. Assertion — login başarılıysa URL değişir
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(
                currentUrl.contains("inventory"),
                "Login failed! URL does not contain 'inventory'."
        );

        System.out.println("SUCCESS: Logged in. Current URL: " + currentUrl);
    }

    @Test
    @Order(2)
    @DisplayName("Failed Login Test - Wrong Password")
    void testFailedLogin() {
        // 1. Siteye git
        driver.get("https://www.saucedemo.com");

        // 2. Yanlış credentials gir
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");

        // 3. Login butonuna tıkla
        driver.findElement(By.id("login-button")).click();

        // 4. Assertion — hata mesajı görünmeli
        WebElement errorMessage = driver.findElement(
                By.cssSelector("[data-test='error']")
        );
        Assertions.assertTrue(
                errorMessage.isDisplayed(),
                "Error message not shown for invalid credentials."
        );

        System.out.println("SUCCESS: Error message displayed: " + errorMessage.getText());
    }

    @AfterAll
    static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}