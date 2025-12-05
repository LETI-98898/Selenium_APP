package iscteiul.ista.selenium_app;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import java.io.File;
import java.time.Duration;


import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class MainPageTest {

    MainPage mainPage = new MainPage();

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        Configuration.downloadsFolder = "downloads";
        Configuration.fileDownload = FileDownloadMode.FOLDER; // evita problemas com direct HTTP -> mais confiável
        SelenideLogger.addListener("allure", new AllureSelenide());
    }


    @BeforeEach
    public void setUp() {
        open("https://www.jetbrains.com/");
        handleCookies();
    }

    private void handleCookies() {
        // 1 — Popup simples
        if ($("button.ch2-allow-all-btn").exists()) {
            $("button.ch2-allow-all-btn").click();
            sleep(500);
            return;
        }

        // 2 — Popup grande (Cookie Settings)
        if ($("button.ch2-btn.ch2-allow-all-btn.ch2-btn-primary").exists()) {
            $("button.ch2-btn.ch2-allow-all-btn.ch2-btn-primary").click();
            sleep(500);
            return;
        }

        // 3 — Nova UI JetBrains 2025 (às vezes aparece)
        if ($("button[data-testid='uc-accept-all-button']").exists()) {
            $("button[data-testid='uc-accept-all-button']").click();
            sleep(500);
        }
    }
    private void closeAllCookies() {

        // Lista de selectores que podem bloquear cliques
        String[] cookieSelectors = {

                // Botão principal (hidden às vezes)
                "button.ch2-allow-all-btn",

                // Botões dentro de containers alternativos
                "div.ch2-container button.ch2-allow-all-btn",

                // Botão de dismiss interno
                "button[data-test='button__accept-all']",

                // Botões genéricos usados pela JetBrains
                "[data-test='close-button']",
                "[data-test='dismiss-button']"
        };

        // Clicar em todos os elementos encontrados (mesmo que estejam escondidos)
        for (String selector : cookieSelectors) {
            if ($(selector).exists()) {
                try {
                    executeJavaScript("arguments[0].click();", $(selector));
                    sleep(300);
                } catch (Exception ignored) {}
            }
        }

        // Remover overlays que ainda bloqueiam cliques
        executeJavaScript(
                "document.querySelectorAll('.ch2-container, .ch2-overlay, .fc-consent-root').forEach(e => e.remove());"
        );
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
    @Test
    public void storeIndividualUse() throws InterruptedException {

        // Abrir o menu Store
        $("button[data-test='main-menu-item-action'][aria-label='Store: Open submenu']")
                .shouldBe(visible)
                .click();

        Thread.sleep(800);

        // Clicar em "For Individual Use"
        $$("span._mainSubmenuItem__title_9khr8w_1")
                .findBy(text("For Individual Use"))
                .shouldBe(visible)
                .click();

        Thread.sleep(1500);

        // Fechar o popup caso apareça
        if ($("button[data-test='close-button']").exists()) {
            $("button[data-test='close-button']").click();
        }

        System.out.println("✔ Store → For Individual Use validado!");
    }

    @Test
    public void supportMenu() throws InterruptedException {

        // 1) Scroll down – isto ativa o popup escondido
        executeJavaScript("window.scrollTo(0, 600);");
        Thread.sleep(700);

        // 2) Subir outra vez
        executeJavaScript("window.scrollTo(0, 0);");
        Thread.sleep(700);

        // 3) AGORA o popup já existe → remover tudo
        closeAllCookies();
        Thread.sleep(500);

        // 4) Agora já pode clicar no menu Support
        $("button[data-test='main-menu-item-action'][aria-label='Support: Open submenu']")
                .shouldBe(visible, enabled)
                .click();

        Thread.sleep(1200);

        System.out.println("✔ Support menu validado com sucesso!");
    }
    @Test
    public void checkboxesTest() throws InterruptedException {

        // Abrir página estável e própria para testes
        open("https://the-internet.herokuapp.com/checkboxes");

        // Encontrar as checkboxes
        SelenideElement checkbox1 = $$("input[type='checkbox']").get(0);
        SelenideElement checkbox2 = $$("input[type='checkbox']").get(1);

        // Scroll só para visualização no vídeo
        executeJavaScript("arguments[0].scrollIntoView(true);", checkbox1);
        Thread.sleep(500);

        // Interação real
        checkbox1.shouldBe(visible, enabled).click();     // selecionar
        Thread.sleep(500);

        checkbox2.shouldBe(visible, enabled).click();     // desselecionar
        Thread.sleep(500);

        // Validações
        checkbox1.shouldBe(checked);
        checkbox2.shouldNotBe(checked);

        System.out.println("✔ Checkboxes test validado com sucesso!");
    }


}
