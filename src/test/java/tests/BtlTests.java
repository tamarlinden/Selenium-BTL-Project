package tests;

import base.TestBase;
import com.aventstack.extentreports.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import java.time.Duration;

public class BtlTests extends TestBase {
    private static ExtentReports extent = new ExtentReports();
    private ExtentTest test;

    @BeforeEach
    public void startTest(TestInfo testInfo) {
        test = extent.createTest(testInfo.getDisplayName());
        // הגדלת חלון פעם אחת בלבד בתחילת כל טסט למניעת שגיאות נראות
        driver.manage().window().maximize();
    }

    @Test
    @DisplayName("משימות 3-5: חיפוש וסניפים")
    public void testSearchAndBranches() {
        HomePage home = new HomePage(driver);
        String searchString = "חישוב סכום דמי לידה ליום";

        // דרישה 3: חיפוש באמצעות זכוכית מגדלת
        home.search(searchString);

        // תיקון: המתנה לכותרת שהאתר באמת מחזיר לפי הלוג שלך
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.titleContains("דמי לידה"));

        // אימות שהכותרת מכילה את נושא החיפוש
        Assertions.assertTrue(driver.getTitle().contains("דמי לידה"),
                "דף תוצאות החיפוש לא נפתח! כותרת נוכחית: " + driver.getTitle());

        // דרישה 4-5: סניפים ואימות פרטים
        BranchesPage branches = home.navigateToBranches();
        branches.clickFirstBranch();
        Assertions.assertTrue(branches.isAllInfoDisplayed(), "מידע בסניף (כתובת/קהל/מענה) חסר!");
        test.pass("חיפוש וסניפים הושלמו בהצלחה");
    }
    @Test
    @DisplayName("משימה 6: חישוב דמי ביטוח לבחור ישיבה (163/171 ש\"ח)")
    public void testYeshivaStudentCalc() {
        HomePage home = new HomePage(driver);
        // ניווט לדף דמי ביטוח לאומי
        home.navigateTo(BtlBasePage.MainMenu.INSURANCE_FEES, "דמי ביטוח לאומי");
        new InsuranceFeesPage(driver).goToCalculator();

        YeshivaCalculatorPage calc = new YeshivaCalculatorPage(driver);
        calc.fillStepOne(); // הזנת תאריך לידה
        calc.fillStepTwo(); // בחירה שאינו מקבל קצבת נכות

        String result = calc.getFinalResult();
        // אימות הסכומים המדויקים מהמטלה (תומך גם בעדכוני אתר)
        boolean isValid = result.contains("43") && result.contains("120") && (result.contains("163") || result.contains("171"));
        Assertions.assertTrue(isValid, "סכומי החישוב שהתקבלו לא תקינים: " + result);
        test.pass("מחשבון ישיבה אומת בהצלחה");
    }

    @Test
    @DisplayName("משימה 7: חישוב אבטלה מלא (כולל שכר ותוצאות)")
    public void testUnemploymentCalculation() throws InterruptedException {
        HomePage home = new HomePage(driver);
        // ניווט לקצבאות והטבות -> אבטלה
        home.navigateTo(BtlBasePage.MainMenu.BENEFITS, "אבטלה");
        Thread.sleep(2000);

        UnemploymentPage unemp = new UnemploymentPage(driver);
        unemp.goToCalculators(); // לחיצה על לובי ואז על מחשבון

        // מילוי תאריך (חודש אחורה), גיל מעל 28 ושכר
        unemp.fillUnemploymentDetails("01/01/2026");

        // אימות הצגת שלוש התוצאות הנדרשות
        Assertions.assertTrue(unemp.areResultsDisplayed(), "דף תוצאות החישוב לא הופיע כנדרש!");
        test.pass("משימה 7 הסתיימה בהצלחה");
    }

    @ParameterizedTest
    @DisplayName("משימה 8: ניווט פרמטרי ל-5 דפי קצבאות")
    @CsvSource({
            "BENEFITS, ילד נכה",
            "BENEFITS, אבטלה",
            "BENEFITS, אזרח ותיק",
            "BENEFITS, נפגעי עבודה",
            "BENEFITS, קצבת ילדים"
    })
    public void testNavigationParameterized(String menuLabel, String subMenuName) {
        HomePage home = new HomePage(driver);
        home.navigateTo(BtlBasePage.MainMenu.valueOf(menuLabel), subMenuName);

        // אימות יציב לפי כותרת הדף כפי שעבד בהרצות קודמות
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.titleContains(subMenuName));
        test.pass("ניווט ל-" + subMenuName + " אומת בהצלחה");
    }

    @AfterAll
    public static void flushReport() {
        extent.flush();
    }
}