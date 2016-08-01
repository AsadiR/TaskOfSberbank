package ru.Asadir.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SberbankPersonPage {
    private final WebDriver driver;
    private final WebDriverWait waiter;
    private static final String sberbank_url = "http://www.sberbank.ru/ru/person";

    public final By element_one =
            By.xpath("//div[@class='currency-converter-result']/span[1]");
    public final By element_currency_from =
            By.xpath("//div[@class='currency-converter-result']/span[3]");
    public final By element_eq_sign =
            By.xpath("//div[@class='currency-converter-result']/span[4]");
    public final By element_price_of_one =
            By.xpath("//div[@class='currency-converter-result']/span[5]");
    public final By element_currency_to =
            By.xpath("//div[@class='currency-converter-result']/span[7]");

    public final By element_date =
            By.xpath("//span[@class='currency-converter-date']");
    public final By element_label_from  =
            By.xpath("//label[@class='col-xs-12 control-label'][@for='from']");
    public final By element_label_to  =
            By.xpath("//label[@class='col-xs-12 control-label'][@for='to']");
    public final By element_text_field_from = By.id("from");
    public final By element_text_field_to = By.id("to");

    public final By element_dropdown_list_from =
            By.xpath("//div[label='Поменять']/div[@class='input-group col-xs-12']/div[@class='input-group-addon']/div/a");
    public final By element_dropdown_list_to =
            By.xpath("//div[label='На']/div[@class='input-group col-xs-12']/div[@class='input-group-addon']/div/a");

    public void select_element_in_dropdown_list(String currency) throws NoSuchElementException {
        String xpath =
                String.format("//div[@class='select2-result-label'][@role='option'][contains(text(),'%s')]", currency);
        waiter.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath(xpath)
                )
        ).click();
    }

    public void type_value_in_field(By by, String amount) throws NoSuchElementException {
        waiter.until(
                ExpectedConditions.presenceOfElementLocated(by)
        ).sendKeys(amount);
    }

    public void clear_field(By by) throws NoSuchElementException {
        waiter.until(
                ExpectedConditions.presenceOfElementLocated(by)
        ).clear();
    }

    public String get_attr_value_from_field(By by) throws NoSuchElementException {
        return waiter.until(
                ExpectedConditions.presenceOfElementLocated(by)
        ).getAttribute("value");
    }

    public String get_text(By by) throws NoSuchElementException {
        return waiter.until(
                ExpectedConditions.presenceOfElementLocated(by)
        ).getText();
    }

    public void click_on_elem(By by) {
        waiter.until(
                ExpectedConditions.presenceOfElementLocated(by)
        ).click();
    }

    public SberbankPersonPage(WebDriver driver) {
        this.driver = driver;
        waiter = new WebDriverWait(driver, 5);
        driver.manage().window().maximize();
        driver.get(sberbank_url);
    }
}
