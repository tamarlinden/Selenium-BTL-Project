package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver; // Instance של הדרייבר [cite: 149]
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // איתחול הדרייבר והפעלת initElements [cite: 140, 149]
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // המתנה דינמית [cite: 555]
    }
}