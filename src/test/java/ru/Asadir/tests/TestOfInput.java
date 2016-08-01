package ru.Asadir.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestOfInput extends AbstractTest{
    private String input;
    private String expected_value;
    private static final String csv_file_name = "src/test/resources/ResForTestOfInput.csv";

    public TestOfInput(String input, String expected_value) {
        this.input = input;
        this.expected_value = expected_value;
    }

    @Parameterized.Parameters
    public static Collection readParams()  {
        return read_csv(csv_file_name);
    }

    @Title("Проверка фильтра ввода")
    @Description("Вводит текст в поле, считывает текст из поля, сравнивает значение с ожидаемым")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void checkTextFields() {
        check_field(page.element_text_field_from, input);
        check_field(page.element_text_field_to, input);
    }

    @Step("Проверяем поле {0} на корректность отображения ввода {1}")
    private void check_field(By by, String input) {
        try {
            page.clear_field(by);
            page.type_value_in_field(by, input);
            String real_value = page.get_attr_value_from_field(by);
            assertEquals(real_value, expected_value);
        } catch (TimeoutException e) {
            fail("Поле для ввода не найдено");
        }
    }
}
