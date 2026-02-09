package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BtlBasePage extends BasePage {

    public enum MainMenu {
        INSURANCE_FEES("דמי ביטוח"),
        BENEFITS("קצבאות והטבות"),
        BRANCHES("סניפים וערוצי שירות");
        private String text;
        MainMenu(String text) { this.text = text; }
        public String getText() { return text; }
    }

    @FindBy(css = ".search-btn") // יש לוודא לוקייטור תקין באתר
    private WebElement searchIcon;

    @FindBy(id = "search_input")
    private WebElement searchInput;

    @FindBy(linkText = "סניפים וערוצי שירות")
    private WebElement branchesBtn;

    public BtlBasePage(WebDriver driver) {
        super(driver);
    }

    public void clickOnMenu(MainMenu menu) {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(menu.getText()))).click();
    }

    public void clickOnSubMenu(String subMenuText) {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(subMenuText))).click();
    }

    // ניווט משולב לפי דרישה: Enum ו-String
    public void navigateTo(MainMenu menu, String subMenu) {
        clickOnMenu(menu);
        clickOnSubMenu(subMenu);
    }

    public void search(String term) {
        searchInput.clear();
        searchInput.sendKeys(term);
        searchIcon.click();
    }
}