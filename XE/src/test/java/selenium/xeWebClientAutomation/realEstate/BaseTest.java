package selenium.xeWebClientAutomation.realEstate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import java.util.concurrent.TimeUnit;

/*
Since all our tests will log in to the Real Estate page, a base test can be used to avoid duplicate code. In a more "functional"
suite the log in could start from the main XE page and navigate from there.
 */

@Configuration
@ContextConfiguration(locations = { "file:.\\src\\test\\resources\\spring-config.xml" }) //Configuration needed for spring
public class BaseTest extends AbstractTestNGSpringContextTests {

    public WebDriver driver; //Declare the webdriver that will be used in the class
    public WebDriverWait wait;

    public void setup(){

        System.setProperty("webdriver.chrome.driver",".\\src\\test\\resources\\chromedriver.exe");//the chromedriver.exe that is needed has been included in the suite for convenience
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.xe.gr/property");
    }

    public void tearDown(){
        driver.quit();
    }

}
