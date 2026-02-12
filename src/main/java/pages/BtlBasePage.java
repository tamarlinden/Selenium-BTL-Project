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

    @FindBy(xpath = "//input[@title='חפש'] | //*[@id='ct100_SiteHeader_reserve_btnSearchBtnSearch']")
    private WebElement searchIcon;

    @FindBy(linkText = "סניפים")
    private WebElement branchesBtn;

    @FindBy(xpath = "//*[contains(@class,'breadcrumb')]")
    private WebElement breadcrumbArea;

    public BtlBasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    public void navigateTo(MainMenu menu, String subMenuName) {
        WebElement main = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(menu.getLabel())));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", main);

        WebElement sub = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(subMenuName)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sub);
    }

    public void search(String text) {
        WebElement input = wait.until(ExpectedConditions.visibilityOf(searchInput));
        input.clear();
        input.sendKeys(text);
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

    public String getBreadcrumbsText() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        return wait.until(ExpectedConditions.visibilityOf(breadcrumbArea)).getText();
    }
}