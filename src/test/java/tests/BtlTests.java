package tests;

import base.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.HomePage;

public class BtlTests extends TestBase {

    @Test
    @DisplayName("בדיקת חיפוש דמי לידה")
    public void testSearchFlow() {
        HomePage homePage = new HomePage(driver);
        String searchTerm = "חישוב סכום דמי לידה ליום";

        homePage.search(searchTerm);

        // תוצאה צפויה: בדיקת כותרת הדף
        String expectedTitle = "תוצאות חיפוש עבור " + searchTerm;
        Assertions.assertTrue(driver.getTitle().contains(expectedTitle), "הכותרת לא תואמת לחיפוש!");
    }

    @Test
    @DisplayName("בדיקת כניסה לדף סניפים")
    public void testBranchesNavigation() {
        HomePage homePage = new HomePage(driver);
        // כאן תקראי לפונקציה של כפתור הסניפים שהגדרת ב-BtlBasePage
        // וודאי שהדף שעלה הוא "סניפים וערוצי שירות"
        Assertions.assertEquals("סניפים וערוצי שירות", driver.getTitle());
    }
}