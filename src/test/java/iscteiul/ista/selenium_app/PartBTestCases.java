package iscteiul.ista.selenium_app;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class PartBTestCases {

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
        Selenide.open("https://the-internet.herokuapp.com/");

    }

    @Test
    public void dropdown() throws InterruptedException {
        $x("//a[text()='Dropdown']").click();
        $("#dropdown").shouldBe(visible);


        $("#dropdown").selectOptionByValue("1");
        $("#dropdown").shouldHave(value("1"));
        $("#dropdown").selectOptionByValue("2");
        $("#dropdown").shouldHave(value("2"));


    }


    @Test
    public void horizontal_slider() throws InterruptedException {
        $x("//a[text()='Horizontal Slider']").click();
        $("#range").shouldBe(visible);

        String sliderInput = "input[type='range']";

        // 1. Click the slider to set focus
        $(sliderInput).click();
        $(sliderInput).shouldBe(focused);
        $("#range").shouldHave(text("2.5"));

        for (int i = 0; i < 5; i++) {
            actions().sendKeys(Keys.ARROW_RIGHT).perform();
        }
        $("#range").shouldHave(text("5"));
        for (int i = 0; i < 10; i++) {
            actions().sendKeys(Keys.ARROW_LEFT).perform();
        }
        $("#range").shouldHave(text("0"));
    }

}
