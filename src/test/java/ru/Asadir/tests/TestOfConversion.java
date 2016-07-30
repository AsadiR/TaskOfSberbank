package ru.Asadir.tests;


import com.opencsv.CSVReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class TestOfConversion {
    private static final String csv_file_name = "src/test/resources/ResForTestOfConversion.csv";
    private static final String sberbank_url = "http://www.sberbank.ru/ru/person";
    private static WebDriver driver;
    private static WebDriverWait waiter;
    private String currency_from;
    private String currency_to;
    private double amount_to;



    @BeforeClass
    public static void openBrowser() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        waiter = new WebDriverWait(driver, 5);
    }


    public TestOfConversion(String currency_from, String currency_to, String amount_to) {
        this.currency_from = currency_from;
        this.currency_to = currency_to;
        this.amount_to = Double.parseDouble(amount_to);
    }

    @Parameterized.Parameters
    public static Collection readParams()  {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(csv_file_name));
            return reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Проверьте наличие соответствующего csv файла в директории resources");
            return null;
        }
    }

    @Title("Проверка правильности конвертации")
    @Description("Валюта конвертируется по курсу, указанному в нижней части виджета")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void checkConversion() {
        driver.get(sberbank_url);
        //погрешность res1 +- 0.005
        double res1 = convert(currency_from, currency_to, amount_to);
        double price_of_one = 0;
        try {
            String price_of_one_str = driver
                    .findElement(By.xpath("//div[@class='currency-converter-result']/span[5]"))
                    .getText();
            //погрешность price_of_one +- 0.00005
            price_of_one = Double.parseDouble(price_of_one_str);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            fail("Не найдена подсказка 'о стоимости одной единицы валюты'");
        }

        //погрешность price_of_one +- (0.00005 + погрешность от price_of_one)
        double res2 = convert_analytically(amount_to, price_of_one);

        //хотелось бы сравнить значения, но тот факт, что price_of_one
        //кругляется до 4 цифр после запятой мешает это сделать.
        //assertEquals(res1, res2);
        //по этой причине  будем определять дельта окрестность исходя из количества потерянных
        //цифр при округлении до 4 знака после запятой
        double delta = 2*0.00005/price_of_one;
        assertTrue(res1 + res1 * delta > res2);
        assertTrue(res1 - res1 * delta < res2);
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String saveRealRes(String res, String message) {
        return message;
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String saveAnRes(String res, String message) {
        return message;
    }

    @Step("Оцениваем стоимость {2} {1} в {0} с помощью конвертера")
    private double convert(String currency_from, String currency_to, double amount_to) {
        try {
            waiter.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//div[label='Поменять']/div[@class='input-group col-xs-12']/div[@class='input-group-addon']/div/a")
                    )
            ).click();

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
            ).sendKeys(String.valueOf(amount_to));

            String amount_from = driver.findElement(By.id("from")).getAttribute("value").replace(" ", "");
            saveRealRes(amount_from, "Результат полученный с помощью конвертора");

            return Double.parseDouble(amount_from);
        } catch (TimeoutException e) {
            e.printStackTrace();
            fail("Не найдено поле для ввода или dropdown_menu");
            return 0;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            fail("Не найдено поле для ввода from");
            return 0;
        }
    }

    @Step("Оцениваем стоимость аналитически")
    private double convert_analytically(double amount_to, double price_of_one) {
        double amount_from = amount_to/price_of_one;
        String str = String.format(Locale.ENGLISH, "%.2f", amount_from);
        saveAnRes(str, "Результат полученный аналитически");
        return Double.parseDouble(str);
    }


    @AfterClass
    public static void closeBrowser() throws InterruptedException{
        driver.quit();
    }
}
