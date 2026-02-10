package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class YeshivaCalculatorPage extends BtlBasePage {

    // שדה תאריך לידה (צילום 221403)
    @FindBy(css = "input[id$='BirthDate_Date']")
    private WebElement birthDateInput;

    // רדיו באטן "תלמיד ישיבה" (צילום 221252)
    @FindBy(css = "input[id$='employeType_2']")
    private WebElement yeshivaRadio;

    // חדש: רדיו באטן "זכר" (לפי מבנה ה-ID באתר)
    @FindBy(css = "input[id$='Gender_0']")
    private WebElement maleRadio;

    // כפתור "המשך" - שימוש ב-XPath לפי ערך הטקסט "המשך" ליציבות מקסימלית [cite: 1418]
    @FindBy(xpath = "//input[@value='המשך']")
    private WebElement nextBtn;

    // רדיו באטן קצבת נכות "לא" (צילום 221531)
    @FindBy(css = "input[id$='rdb_GetNechut_1']")
    private WebElement disabilityNoRadio;

    // שדה סכום סופי (צילום 221725)
    @FindBy(xpath = "//li[contains(text(),'סך הכל')]//strong")
    private WebElement totalAmountLabel;

    public YeshivaCalculatorPage(WebDriver driver) {
        super(driver);
    }

    public void fillStepOne() {
        // 1. הגרלת תאריך לידה (לפחות 18 שנה אחורה) [cite: 173]
        int yearsBack = 18 + new Random().nextInt(50);
        String date = LocalDate.now().minusYears(yearsBack).minusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        wait.until(ExpectedConditions.visibilityOf(birthDateInput)).sendKeys(date);

        // 2. בחירת מין "זכר" (חובה כדי שהטופס יתקדם!)
        wait.until(ExpectedConditions.elementToBeClickable(maleRadio)).click();

        // 3. בחירת "תלמיד ישיבה"
        yeshivaRadio.click();

        // 4. לחיצה על המשך (עם גלילה למטה כדי לוודא שהכפתור נראה) [cite: 1510]
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextBtn);
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn)).click();
    }

    public void fillStepTwo() {
        // בחירה ב"לא" לקצבת נכות [cite: 175]
        wait.until(ExpectedConditions.elementToBeClickable(disabilityNoRadio)).click();
        nextBtn.click();
    }

    public String getFinalResult() {
        // שליפת הטקסט של הסכום הסופי [cite: 1441]
        return wait.until(ExpectedConditions.visibilityOf(totalAmountLabel)).getText();
    }
}