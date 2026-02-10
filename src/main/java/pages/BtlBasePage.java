package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BtlBasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public enum MainMenu {
        BENEFITS("קצבאות והטבות"),
        INSURANCE_FEES("דמי ביטוח"),
        RIGHTS_EXHAUSTION("מיצוי זכויות");
        private final String label;
        MainMenu(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    @FindBy(id = "TopQuestions")
    private WebElement searchInput;

    // לוקייטור חסין לאייקון זכוכית המגדלת לפי ה-Inspect ששלחת
    @FindBy(xpath = "//input[@title='חפש'] | //button[contains(@class,'search')] | //*[@id='ct100_SiteHeader_reserve_btnSearchBtnSearch']")
    private WebElement searchIcon;

    @FindBy(linkText = "סניפים")
    private WebElement branchesBtn;

    public BtlBasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    public void clickMainMenu(MainMenu menu) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(menu.getLabel())));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void clickSubMenu(String subMenuName) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(subMenuName)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void navigateTo(MainMenu menu, String subMenuName) {
        clickMainMenu(menu);
        clickSubMenu(subMenuName);
    }

    public void search(String text) {
        WebElement input = wait.until(ExpectedConditions.visibilityOf(searchInput));
        input.clear();
        input.sendKeys(text);
        // דרישה: לחיצה פיזית על האייקון
        try {
            wait.until(ExpectedConditions.elementToBeClickable(searchIcon)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchIcon);
        }
    }

    public BranchesPage navigateToBranches() {
        wait.until(ExpectedConditions.elementToBeClickable(branchesBtn)).click();
        return new BranchesPage(driver);
    }

    // הוספת פונקציה להחזרת כותרת ה-Title לאימות
    public String getPageTitle() {
        return driver.getTitle();
    }
}