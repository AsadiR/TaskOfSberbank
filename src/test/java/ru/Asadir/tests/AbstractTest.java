package ru.Asadir.tests;

import com.opencsv.CSVReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import static org.junit.Assert.fail;

public abstract class AbstractTest {
    protected static WebDriver driver;
    protected SberbankPersonPage page;

    @BeforeClass
    public static void openBrowser() {
        driver = new FirefoxDriver();
    }

    public AbstractTest() {
        page = new SberbankPersonPage(driver);
    }


    public static Collection read_csv(String name) {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(name));
            return reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Проверьте наличие соответствующего csv файла в директории resources");
            return null;
        }
    }

    @AfterClass
    public static void closeBrowser() throws InterruptedException{
        driver.quit();
    }
}
