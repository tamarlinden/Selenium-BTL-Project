package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import java.util.Set;

public class UnemploymentPage extends BtlBasePage {

    @FindBy(xpath = "//a[contains(@href, 'AvtCalcIndex')]")
    private WebElement lobbyCalculatorsLink;

    @FindBy(xpath = "//a[contains(@href, 'AvtalaCalcNew')]")
    private WebElement calculatorLink;

    @FindBy(css = "input[id$='PiturimDate_Date']")
    private WebElement terminationDateInput;

    @FindBy(css = "input[id$='rdb_age_1']")
    private WebElement ageOver28Radio;

    @FindBy(css = "input[id$='StartNextButton']")
    private WebElement nextBtn;

    @FindBy(css = "input[id*='Sallary']")
    private List<WebElement> salaryFields;

    @FindBy(css = "input[id$='StepNextButton']")
    private WebElement finishBtn;

    @FindBy(xpath = "//*[contains(text(),'שכר יומי ממוצע')]")
    private WebElement resDailyWage;

    public UnemploymentPage(WebDriver driver) { super(driver); }

    public void goToCalculators() {
        String mainWindow = driver.getWindowHandle();

        // לחיצה על לובי המחשבונים
        WebElement lobby = wait.until(ExpectedConditions.elementToBeClickable(lobbyCalculatorsLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", lobby);

        // לחיצה על המחשבון הספציפי
        WebElement calc = wait.until(ExpectedConditions.elementToBeClickable(calculatorLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", calc);

        // פתרון יציב: מעבר ללשונית חדשה רק אם היא קיימת
        Set<String> handles = driver.getWindowHandles();
        if (handles.size() > 1) {
            for (String h : handles) {
                if (!h.equals(mainWindow)) {
                    driver.switchTo().window(h);
                    break;
                }
            }
        }
    }

    public void fillUnemploymentDetails(String date) {
        // סריקת Iframes למציאת הטופס
        driver.switchTo().defaultContent();
        List<WebElement> frames = driver.findElements(By.tagName("iframe"));
        for (WebElement f : frames) {
            driver.switchTo().frame(f);
            if (driver.findElements(By.cssSelector("input[id$='PiturimDate_Date']")).size() > 0) break;
            driver.switchTo().defaultContent();
        }

        // מילוי שלב 1: תאריך וגיל
        WebElement dateInp = wait.until(ExpectedConditions.visibilityOf(terminationDateInput));
        dateInp.clear();
        dateInp.sendKeys(date);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ageOver28Radio);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextBtn);

        // שלב 2: מילוי שכר
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[id*='Sallary']")));
        for (WebElement field : salaryFields) {
            field.clear();
            field.sendKeys("10000");
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", finishBtn);
    }

    public boolean areResultsDisplayed() {
        try {
            // אימות הופעת התוצאות
            return wait.until(ExpectedConditions.visibilityOf(resDailyWage)).isDisplayed();
        } catch (Exception e) { return false; }
    }
}