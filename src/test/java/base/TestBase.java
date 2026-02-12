package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class TestBase {
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.btl.gov.il/");
    }

    // ניהול חיי הדרייבר דרך ה-Watcher כדי לאפשר צילום מסך לפני סגירה
    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {

        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            if (driver != null) {
                try {
                    File screenshotDir = new File("./screenshots/");
                    if (!screenshotDir.exists()) screenshotDir.mkdirs();

                    File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    String testName = context.getDisplayName().replaceAll("[^a-zA-Z0-9]", "_");
                    File destFile = new File("./screenshots/" + testName + ".png");
                    FileUtils.copyFile(srcFile, destFile);
                    System.out.println("✅ Screenshot saved to: " + destFile.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("❌ Failed to save screenshot: " + e.getMessage());
                } finally {
                    driver.quit(); // סוגרים את הדפדפן רק פה בכישלון
                }
            }
        }

        @Override
        public void testSuccessful(ExtensionContext context) {
            if (driver != null) {
                driver.quit(); // סוגרים את הדפדפן בהצלחה
            }
        }

        @Override
        public void testAborted(ExtensionContext context, Throwable cause) {
            if (driver != null) driver.quit();
        }
    };

    // הסרנו את ה-@AfterEach הרגיל כדי למנוע סגירה מוקדמת מדי של הדרייבר
}