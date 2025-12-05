package iscteiul.ista.selenium_app;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import java.time.Duration;
import static com.codeborne.selenide.Condition.exist;
public class MainPageTest {
    MainPage mainPage = new MainPage();
    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    public boolean isElementPresent(String cssSelector, Duration timeout) {
        try {
            // Wait for the element to exist within the specified timeout
            $(cssSelector).should(exist, timeout);
            return true;
        } catch (Throwable e) {
            // If the element does not exist after the timeout, catch the error and return false
            return false;
        }
    }

    @BeforeEach
    public void setUp() {
        Selenide.open("https://www.jetbrains.com/");
        String denyButtonSelector = "button.ch2-btn.ch2-deny-all-btn.ch2-btn-primary";

        if (isElementPresent(denyButtonSelector, Duration.ofSeconds(2))) {
            $(denyButtonSelector).click();
        } else {
        }
    }

    @Test
    public void search() throws InterruptedException {
        mainPage.searchButton.click();

        $("input[data-test$='inner']").sendKeys("Selenium");
        $("button[data-test='full-search-button']").click();
        $("input[data-test$='inner']").shouldHave(attribute("value", "Selenium"));
    }
    @Test
    public void toolsMenu() {
        mainPage.toolsMenu.click();

        $("div[data-test='main-menu-item']").shouldBe(visible);
    }

    @Test
    public void navigationToAllTools() {
        mainPage.seeDeveloperToolsButton.click();
        mainPage.findYourToolsButton.click();

        $("#products-page").shouldBe(visible);

        assertEquals("All Developer Tools and Products by JetBrains", Selenide.title());
    }
}
