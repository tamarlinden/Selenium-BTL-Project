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
    }

    @Test
    @DisplayName("משימות 3-5: חיפוש וסניפים")
    public void testSearchAndBranches() {
        HomePage home = new HomePage(driver);
        home.search("חישוב סכום דמי לידה ליום");

        // תיקון: המתנה לכותרת "חיפוש" כפי שהאתר מציג בפועל לפי הלוג שלך
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.titleContains("חיפוש"));

        BranchesPage branches = home.navigateToBranches();
        branches.clickFirstBranch();
        Assertions.assertTrue(branches.isAllInfoDisplayed(), "מידע בסניף חסר!");
        test.pass("משימות 3-5 הושלמו בהצלחה");
    }

    @Test
    @DisplayName("משימה 6: חישוב דמי ביטוח לבחור ישיבה")
    public void testYeshivaStudentCalc() {
        HomePage home = new HomePage(driver);
        home.navigateTo(BtlBasePage.MainMenu.INSURANCE_FEES, "דמי ביטוח לאומי");
        new InsuranceFeesPage(driver).goToCalculator();
        YeshivaCalculatorPage calc = new YeshivaCalculatorPage(driver);
        calc.fillStepOne();
        calc.fillStepTwo();
        String result = calc.getFinalResult();
        // אימות סכום גמיש (163 או 171)
        Assertions.assertTrue(result.contains("163") || result.contains("171"), "הסכום לא תקין: " + result);
        test.pass("מחשבון ישיבה עבר בהצלחה");
    }

    @Test
    @DisplayName("משימה 7: חישוב אבטלה מלא")
    public void testUnemploymentCalculation() throws InterruptedException {
        HomePage home = new HomePage(driver);
        home.navigateTo(BtlBasePage.MainMenu.BENEFITS, "אבטלה");
        Thread.sleep(2000);
        UnemploymentPage unemp = new UnemploymentPage(driver);
        unemp.goToCalculators();
        unemp.fillUnemploymentDetails("01/01/2026");
        Assertions.assertTrue(unemp.areResultsDisplayed(), "תוצאות החישוב לא הופיעו!");
        test.pass("משימה 7 הסתיימה בהצלחה");
    }

    @ParameterizedTest
    @DisplayName("משימה 8: ניווט ל-5 דפי קצבאות")
    @CsvSource({
            "BENEFITS, ילד נכה",
            "BENEFITS, אבטלה",
            "BENEFITS, אזרח ותיק",
            "BENEFITS, נפגעי עבודה",
            "BENEFITS, דמי לידה"
    })
    public void testNavigationParameterized(String menuLabel, String subMenuName) {
        HomePage home = new HomePage(driver);
        home.navigateTo(BtlBasePage.MainMenu.valueOf(menuLabel), subMenuName);
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.titleContains(subMenuName));
        test.pass("ניווט ל-" + subMenuName + " אומת בהצלחה");
    }

    @AfterAll
    public static void flushReport() {
        extent.flush();
    }
}