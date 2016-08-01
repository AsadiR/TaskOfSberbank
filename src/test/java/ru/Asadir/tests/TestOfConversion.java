package ru.Asadir.tests;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.Collection;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class TestOfConversion extends AbstractTest {

    private static final String csv_file_name = "src/test/resources/ResForTestOfConversion.csv";

    private String currency_from;
    private String currency_to;
    private double amount_to;

    public TestOfConversion(String currency_from, String currency_to, String amount_to) {
        super();
        this.currency_from = currency_from;
        this.currency_to = currency_to;
        this.amount_to = Double.parseDouble(amount_to);
    }

    @Parameterized.Parameters
    public static Collection readParams()  {
        return read_csv(csv_file_name);
    }

    @Title("Проверка правильности конвертации")
    @Description("Валюта конвертируется по курсу, указанному в нижней части виджета")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void checkConversion() {
        //погрешность converter_res +- 0.005
        double converter_res = convert(currency_from, currency_to, amount_to);
        double price_of_one = 0;
        try {
            String price_of_one_str = page.get_text(page.element_price_of_one);
            //погрешность price_of_one +- 0.00005
            price_of_one = Double.parseDouble(price_of_one_str);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            fail("Не найдена подсказка 'о стоимости одной единицы валюты'");
        }
        //погрешность analitic_res +- (0.005 + погрешность от price_of_one)
        double analitic_res = convert_analytically(amount_to, price_of_one);

        //хотелось бы сравнить значения, но тот факт, что price_of_one
        //кругляется до 4 цифр после запятой мешает это сделать.
        //assertEquals(res1, res2);
        //по этой причине  будем определять дельта окрестность исходя из количества потерянных
        //цифр при округлении до 4 знака после запятой
        double delta = 2 * 0.00005 / price_of_one;
        assertTrue(converter_res + converter_res * delta > analitic_res);
        assertTrue(converter_res - converter_res * delta < analitic_res);
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
            page.click_on_elem(page.element_dropdown_list_from);
            page.select_element_in_dropdown_list(currency_from);

            page.click_on_elem(page.element_dropdown_list_to);
            page.select_element_in_dropdown_list(currency_to);

            check_rate_string(currency_from, currency_to);

            page.type_value_in_field(page.element_text_field_to, String.valueOf(amount_to));

            String amount_from = page
                    .get_attr_value_from_field(page.element_text_field_from)
                    .replace(" ", "");
            saveRealRes(amount_from, "Результат полученный с помощью конвертора");

            return Double.parseDouble(amount_from);
        } catch (TimeoutException e) {
            e.printStackTrace();
            fail("Не найдено поле для ввода или dropdown_menu");
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

    @Step("Проверяем строку курса {0} -> {1} в нижней часте виджета")
    private void check_rate_string(String currency_from, String currency_to) {
        try {
            String one = page.get_text(page.element_one);
            assertEquals("1", one);

            String widget_currency_from = page.get_text(page.element_currency_from);
            assertEquals(currency_from, widget_currency_from);

            String eq_sign = page.get_text(page.element_eq_sign);
            assertEquals("=", eq_sign);

            String widget_currency_to = page.get_text(page.element_currency_to);
            assertEquals(currency_to, widget_currency_to);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            fail("Строка курса не корректна");
        }
    }
}
