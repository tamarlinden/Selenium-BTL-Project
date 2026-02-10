package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class InsuranceFeesPage extends BtlBasePage {
    // זיהוי הלינק לפי הטקסט המדויק שמופיע בתמונה (221155)
    @FindBy(partialLinkText = "מחשבון לחישוב דמי הביטוח")
    private WebElement calculatorLink;

    public InsuranceFeesPage(WebDriver driver) {
        super(driver);
    }

    public void goToCalculator() {
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(calculatorLink)).click();
    }
}