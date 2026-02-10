package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BranchesPage extends BtlBasePage {

    // לוקייטור חזק יותר לסניף הראשון (אשדוד) לפי הטקסט שמופיע בתמונה שלך
    @FindBy(xpath = "(//a[contains(text(),'אשדוד') or contains(@href, 'BranchDetails')])[1]")
    private WebElement firstBranchLink;

    // לוקייטורים למידע בתוך דף הסניף - זיהוי לפי טקסט כדי למנוע תלות ב-ID משתנה
    @FindBy(xpath = "//*[contains(text(), 'כתובת')]")
    private WebElement addressLabel;

    @FindBy(xpath = "//*[contains(text(), 'קבלת קהל')]")
    private WebElement receptionLabel;

    @FindBy(xpath = "//*[contains(text(), 'מענה טלפוני')]")
    private WebElement phoneLabel;

    public BranchesPage(WebDriver driver) {
        super(driver);
    }

    public void clickFirstBranch() {
        // המתנה שהקישור יהיה לחיץ וגלילה אליו במידת הצורך
        wait.until(ExpectedConditions.elementToBeClickable(firstBranchLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstBranchLink);
        firstBranchLink.click();
    }

    public boolean isAllInfoDisplayed() {
        // בדיקה שהאלמנטים גלויים בדף הסניף שנפתח
        try {
            return wait.until(ExpectedConditions.visibilityOf(addressLabel)).isDisplayed() &&
                    wait.until(ExpectedConditions.visibilityOf(receptionLabel)).isDisplayed() &&
                    wait.until(ExpectedConditions.visibilityOf(phoneLabel)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}