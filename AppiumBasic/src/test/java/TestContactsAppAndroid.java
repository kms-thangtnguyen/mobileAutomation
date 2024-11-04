import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class TestContactsAppAndroid {

    AppiumDriver driver;

    @BeforeClass
    public void setup() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setUdid("emulator-5554")
                .setNewCommandTimeout(Duration.ofSeconds(30))
                .setAppPackage("com.google.android.contacts")
                .setAppActivity("com.android.contacts.activities.PeopleActivity");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void test() {
        String firstName = "Thang";
        String lastName = "Nguyen";
        String phone = "1234567890";
        driver.findElement(AppiumBy.accessibilityId("Create contact")).click();
        driver.findElement(AppiumBy.xpath("//android.widget.EditText[@text='First name']")).sendKeys(firstName);
        driver.findElement(AppiumBy.xpath("//android.widget.EditText[@text='Last name']")).sendKeys(lastName);
        driver.findElement(AppiumBy.xpath("//android.widget.EditText[@text='Phone']")).sendKeys(phone);
        driver.findElement(AppiumBy.id("com.google.android.contacts:id/menu_save")).click();
        driver.findElement(AppiumBy.xpath("//android.widget.ImageButton[@content-desc='Navigate up']")).click();
        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@content-desc='" + firstName + " " + lastName + "" +
                "']")).click();

        String fullName = driver.findElement(AppiumBy.id("com.google.android.contacts:id/large_title")).getAttribute("text");
        String displayedPhone = driver.findElement(AppiumBy.id("com.google.android.contacts:id/header")).getAttribute("text");

        String normalizedInputPhone = phone.replaceAll("[^0-9]", "");
        String normalizedDisplayedPhone = displayedPhone.replaceAll("[^0-9]", "");

        Assert.assertEquals(fullName, firstName + " " + lastName, "Full name does not match the expected value.");
        Assert.assertEquals(normalizedDisplayedPhone, normalizedInputPhone, "Phone numbers do not match.");
        driver.findElement(AppiumBy.accessibilityId("More options")).click();

        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@resource-id='com.google.android.contacts:id/title' and @text='Delete']")).click();
        driver.findElement(AppiumBy.xpath("//android.widget.Button[@resource-id='android:id/button1']")).click();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
