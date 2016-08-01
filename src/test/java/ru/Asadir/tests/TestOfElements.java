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

public class TestOfElements extends AbstractTest {

    private final String[] monthNames = {
        "января", "февраля", "марта",
        "апреля", "мая", "июня",
        "июля", "августа", "сентября",
        "октября", "ноября", "декабря"
    };

    @Title("Проверка даты в виджете")
    @Description("Сравниваем дату из виджета с текущей датой")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void checkDate() {
        try {
            String date_from_widget = page.get_text(page.element_date);
            Calendar now = Calendar.getInstance();

            int date = now.get(Calendar.DATE);
            String str_date = String.valueOf(date);
            if (date < 10) str_date = "0" + date;

            String cur_date = String.format(
                    "%s %s %s",
                    str_date,
                    monthNames[now.get(Calendar.MONTH)],
                    now.get(Calendar.YEAR)
            );
            assertEquals(date_from_widget, cur_date);
        } catch (TimeoutException e) {
            fail("Поле с датой не найдено");
        }
    }

    @Title("Проверка содержимого меток (label)")
    @Description("Метки должны содержать правильный текст")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void checkLabels() {
        try {
            String text_from_label_from = page.get_text(page.element_label_from);
            assertEquals(text_from_label_from, "Поменять");

            String text_from_label_to = page.get_text(page.element_label_to);
            assertEquals(text_from_label_to, "На");
        } catch (TimeoutException e) {
            e.printStackTrace();
            fail("Метка 'На' или метка'Поменять' не найдена");
        }
    }
}
