package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class OrderFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createOrderPage_isAccessible(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/create");
        String pageTitle = driver.getTitle();
        assertEquals("Create New Order", pageTitle);
    }

    @Test
    void orderHistoryForm_isAccessible(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/history");
        String pageTitle = driver.getTitle();
        assertEquals("Order History Form", pageTitle);
    }

    @Test
    void simulateCreateAndCheckHistory(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/order/create");

        WebElement authorInput = driver.findElement(By.id("authorInput"));
        authorInput.sendKeys("test");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        driver.get(baseUrl + "/order/history");

        WebElement historyAuthorInput = driver.findElement(By.id("authorInput"));
        historyAuthorInput.sendKeys("test");

        WebElement historySubmitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        historySubmitButton.click();

        String pageTitle = driver.getTitle();
        assertEquals("Order History", pageTitle);
    }
}