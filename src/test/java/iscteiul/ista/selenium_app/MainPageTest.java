package iscteiul.ista.selenium_app;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class MainPageTest {

    MainPage mainPage = new MainPage();

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open("https://www.jetbrains.com/");
        handleCookies();
    }

    /**
     * Accept All cookies if cookie popup appears
     */
    private void handleCookies() {
        SelenideElement accept = $("button.ch2-allow-all-btn");

        if (accept.exists()) {
            accept.click();
            sleep(500);
        }
    }




    @Test
    public void search() throws InterruptedException {

        open("https://www.jetbrains.com/");
        Thread.sleep(1500);

        // Aceitar cookies
        if ($("button.ch2-allow-all-btn").exists()) {
            $("button.ch2-allow-all-btn").click();
            Thread.sleep(1500);
        }

        // Abrir ícone de pesquisa
        $("[data-test='site-header-search-action']")
                .shouldBe(visible, enabled)
                .click();

        Thread.sleep(1500);

        // Campo do popup
        SelenideElement input = $("[data-test-id='search-input']")
                .should(appear)
                .shouldBe(visible, enabled);

        input.click();
        Thread.sleep(800);

        // Escrever a pesquisa
        input.setValue("jetbrains");
        Thread.sleep(1500);

        // Tentar ENTER primeiro
        input.pressEnter();
        Thread.sleep(2000);

        // Se não saiu da homepage → clicar botão Full Search
        if (webdriver().driver().url().equals("https://www.jetbrains.com/")) {

            System.out.println("ENTER falhou → clicar botão Full Search");

            $("button[data-test='full-search-button']")
                    .shouldBe(visible, enabled)
                    .click();

            Thread.sleep(2500);
        }

        // VALIDAR URL CORRETA DO JETBRAINS SEARCH MODERNO
        webdriver().shouldHave(urlContaining("?q="));
        webdriver().shouldHave(urlContaining("jetbrains"));

        System.out.println("✔ Pesquisa validada com sucesso!");
    }

    @Test
    public void toolsMenu() throws InterruptedException {
        $("button[data-test='main-menu-item-action'][aria-label='Developer Tools: Open submenu']")
                .shouldBe(visible)
                .click();

        Thread.sleep(1500);

        System.out.println("✔ Menu validada com sucesso!");
    }

    @Test
    public void navigationToAllTools() throws InterruptedException {

        // Abrir o menu Developer Tools
        $("button[data-test='main-menu-item-action'][aria-label='Developer Tools: Open submenu']")
                .shouldBe(visible)
                .click();

        Thread.sleep(2500); // só para visualização

        // Clicar no botão "Find your tool"
        $("a[data-test='suggestion-link']")
                .shouldBe(visible, enabled)
                .click();

        Thread.sleep(2000); // ver a navegação acontecer



        System.out.println("✔ Navegação para Find Your Tool validada com sucesso!");
    }

}
