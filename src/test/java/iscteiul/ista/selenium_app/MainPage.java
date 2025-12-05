package iscteiul.ista.selenium_app;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    // --- TOP HEADER ---

    public SelenideElement toolsMenu =
            $("button[data-test='main-menu-item-action'][aria-label*='Developer Tools']");

    // --- SUBMENU ---

    public SelenideElement seeDeveloperToolsButton =
            $("a[data-test='see-developer-tools']");

    public SelenideElement findYourToolsButton =
            $("a[data-test='products-find-your-tools']");

    // --- SEARCH ---

    public SelenideElement searchIcon =
            $("[data-test='site-header-search-action']");

    public SelenideElement searchInput =
            $("[data-test-id='search-input']");
}
