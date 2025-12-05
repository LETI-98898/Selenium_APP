package iscteiul.ista.selenium_app;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    // --- TOP HEADER ---

    // Botão Developer Tools (abre o submenu)
    public SelenideElement toolsMenu =
            $("button[data-test='main-menu-item-action'][aria-label*='Developer Tools']");

    // --- SUBMENU (menu que aparece após clicar Developer Tools) ---

    // "See All Developer Tools"
    public SelenideElement seeDeveloperToolsButton =
            $("a[data-test='see-developer-tools']");

    // "Find your tools"
    public SelenideElement findYourToolsButton =
            $("a[data-test='products-find-your-tools']");

    // --- SEARCH ---

    // Botão do ícone da lupa
    public SelenideElement searchIcon =
            $("[data-test='site-header-search-action']");

    // Campo de input do popup de pesquisa
    public SelenideElement searchInput =
            $("[data-test-id='search-input']");
}
