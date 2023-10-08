package nl.eric.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.List;

public class SeleniumUtil {

    private WebDriver driver;

    public SeleniumUtil(String url) {

        int count = 0;

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        try {
            initDriver(url, options);
        } catch (WebDriverException e) {
            count += 1;

            if (count < 2) {

                System.out.println("Retrying Webdriver init");

                initDriver(url, options);
            } else {

                throw e;
            }
        }
    }

    private void initDriver (String url, ChromeOptions options) {
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.get(url);
    }

    public String getTitle () {

        return driver.getTitle();
    }

    public WebElement getElement (By by) {

        return driver.findElement(by);
    }

    public List<WebElement> getElements (By by) {

        return driver.findElements(by);
    }

    public void clickElement (By by) {

        driver.findElement(by).click();
    }

    public boolean elementExists (By by) {

        try {

            driver.findElement(by);

            return true;
        } catch (NoSuchElementException e) {

            return false;
        }
    }

    public void closeWebdriver () {

        driver.close();
    }
}
