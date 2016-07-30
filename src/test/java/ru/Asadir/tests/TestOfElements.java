package ru.Asadir.tests;



import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;
import static org.junit.Assert.*;


import java.util.Calendar;


public class TestOfElements {
    private final String[] monthNames = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    private static final String csv_file_name = "src/test/resources/ResForTestOfElements.csv";
    private static final String sberbank_url = "http://www.sberbank.ru/ru/person";
    private static WebDriver driver;
    private static WebDriverWait waiter;



    @BeforeClass
    public static void openBrowser() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        waiter = new WebDriverWait(driver, 5);
    }

    public TestOfElements() {

    }


    @Title("Проверка даты в виджете")
    @Description("Сравниваем дату из виджета с текущей датой")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void checkDate() {
        try {
            driver.get(sberbank_url);
            String date_from_widget = waiter.until(
                    ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='currency-converter-date']"))
            ).getText();
            Calendar now = Calendar.getInstance();
            String cur_date = String.format("%s %s %s",
                    now.get(Calendar.DATE), monthNames[now.get(Calendar.MONTH)], now.get(Calendar.YEAR));
            assertEquals(date_from_widget, cur_date);
        } catch (TimeoutException e) {
            fail("Поле с датой не найдено");
        }
    }

    @Title("Проверка содержимого меток (label)")
    @Description("Метки должны содержать правильный текст")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void checkTextFields() {
        try {
            driver.get(sberbank_url);
            String text_from_for = waiter.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//label[@class='col-xs-12 control-label'][@for='from']")
                    )
            ).getText();
            assertEquals(text_from_for, "Поменять");

            String text_from_to = waiter.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//label[@class='col-xs-12 control-label'][@for='to']")
                    )
            ).getText();
            assertEquals(text_from_to, "На");
        } catch (TimeoutException e) {
            e.printStackTrace();
            fail("Метка 'На' или метка'Поменять' не найдена");
        }
    }

    @AfterClass
    public static void closeBrowser() throws InterruptedException{
        driver.quit();
    }
}
