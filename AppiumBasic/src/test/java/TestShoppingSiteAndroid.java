import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class TestShoppingSiteAndroid {

    AppiumDriver driver;

    @BeforeClass
    public void setup() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setUdid("emulator-5554")
                .setNewCommandTimeout(Duration.ofSeconds(30))
                .withBrowserName("Chrome");


        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void test() {
        driver.get("https://magento.softwaretestingboard.com/push-it-messenger-bag.html");
        WebElement productTitle = driver.findElement(By.cssSelector("h1.page-title"));
        driver.executeScript("arguments[0].scrollIntoView(true);", productTitle);

        WebElement addToCartButton = driver.findElement(By.id("product-addtocart-button"));
        Assert.assertTrue(addToCartButton.isDisplayed(), "Add to cart button not found");

        addToCartButton.click();

        String expectedItem = driver.findElement(By.xpath("//span[@class='base']")).getText();
        WebElement shoppingCart = driver.findElement(By.xpath("//a[text() = 'shopping cart']"));
        shoppingCart.click();

        String actualItem = driver.findElement(By.xpath("//strong[@class='product-item-name']//a[not(@data-bind)]")).getText();
        Assert.assertEquals(actualItem, expectedItem, "The items do not match!");

        //click proceedToCheckout to checkout
        WebElement proceedToCheckout = driver.findElement(By.xpath("//button[@data-role='proceed-to-checkout']"));
        proceedToCheckout.click();

       driver.findElement(By.id("customer-email")).sendKeys("thang@gmail.com");  // Entering "test" as the email
        driver.findElement(By.xpath("//input[@name='lastname']")).sendKeys("nguyen");
        driver.findElement(By.xpath("//input[@name='firstname']")).sendKeys("thang");
        driver.findElement(By.xpath("//input[@name='street[0]']")).sendKeys("Tan Ph 1");
        driver.findElement(By.xpath("//input[@name='city']")).sendKeys("Da Nang");
        driver.findElement(By.xpath("//input[@name='postcode']")).sendKeys("12345");

        driver.findElement(By.xpath("//select[@name='country_id']")).click();
        driver.findElement(By.xpath("//option[@data-title='Albania']")).click();

        driver.findElement(By.xpath("//input[@name='telephone']")).sendKeys("0915810456");
        driver.findElement(By.xpath("//input[@value='flatrate_flatrate']")).click();

        driver.findElement(By.xpath("//span[text()='Next']/parent::button")).click();
        driver.findElement(By.xpath("//span[text()='Place Order']//parent::button")).click();

        String actualMessage = driver.findElement(By.xpath("//span[@class='base']")).getText();
        String ExpectedMessage = "Thank you for your purchase!";
        Assert.assertEquals(actualMessage, ExpectedMessage, "Cannot order item");

    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}