import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Locale;


public class Simple {
    private static final String sberbank_url = "http://www.sberbank.ru/ru/person";
    public static void main (String[] args) {
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        WebDriverWait waiter = new WebDriverWait(driver, 5);
        driver.get(sberbank_url);

        waiter.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[label='Поменять']/div[@class='input-group col-xs-12']/div[@class='input-group-addon']/div/a")
                )
        ).click();

        String currency_from = "EUR";
        String xpath_from = String.format("//div[@class='select2-result-label'][@role='option'][contains(text(),'%s')]", currency_from);
        waiter.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath(xpath_from)
                )
        ).click();

        waiter.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[label='На']/div[@class='input-group col-xs-12']/div[@class='input-group-addon']/div/a")
                )
        ).click();

        String currency_to = "USD";
        String xpath_to = String.format("//div[@class='select2-result-label'][@role='option'][contains(text(),'%s')]", currency_to);
        waiter.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath(xpath_to)
                )
        ).click();

        waiter.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("to")
                )
        ).sendKeys("100");


        String amount_from = driver.findElement(By.id("from")).getAttribute("value");

        System.out.println(amount_from);

        String price_of_one = driver.findElement(By.xpath("//div[@class='currency-converter-result']/span[5]")).getText();

        String result = String.format(Locale.ENGLISH, "%.2f", 100/Double.parseDouble(price_of_one));

        System.out.println(result);

    }
}
