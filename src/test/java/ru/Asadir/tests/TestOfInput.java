package ru.Asadir.tests;


import com.opencsv.CSVReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestOfInput {
    private static final String csv_file_name = "src/test/resources/ResForTestOfInput.csv";
    private static final String sberbank_url = "http://www.sberbank.ru/ru/person";
    private static WebDriver driver;
    private static WebDriverWait waiter;
    private String input;
    private String expected_value;



    @BeforeClass
    public static void openBrowser() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        waiter = new WebDriverWait(driver, 5);
    }

    public TestOfInput(String input, String expected_value) {
        this.input = input;
        this.expected_value = expected_value;
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



    @Title("Проверка фильтра ввода")
    @Description("Вводит текст в поле, считывает текст из поля, сравнивает значение с ожидаемым")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void checkTextFields() {
        driver.get(sberbank_url);
        check_field("to", input);
        check_field("from", input);
    }

    @Step("Проверяем поле {0} на корректность отображения ввода {1}")
    private void check_field(String tag, String input) {
        try {
            WebElement field = waiter.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.id(tag)
                    )
            );
            field.clear();
            field.sendKeys(input);
            String real_value = field.getAttribute("value");
            assertEquals(real_value, expected_value);
        } catch (TimeoutException e) {
            fail("Поле для ввода не найдено");
        }
    }

    @AfterClass
    public static void closeBrowser() throws InterruptedException{
        driver.quit();
    }
}
