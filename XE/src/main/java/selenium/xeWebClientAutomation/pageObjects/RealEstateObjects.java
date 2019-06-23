package selenium.xeWebClientAutomation.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/*
Object that will be used for all actions that can take place in the Real Estate page
 */

@Component
public class RealEstateObjects {

    public enum RealEstateUIElements { //Dictionary for all the locators that are going to be used
        /**
         *
         */

        BTN_LET("#transaction_let"),
        IPF_SEARCH_LOCATIONS("input#imf_locations_input"),
        BTN_SEARCH("#search_submit"),
        LI_SEARCH_RESULTS("#imf_autocomplete li"),
        CB_ADD_RESULT_TO_SEARCH("[class=add_to_search]"),
        IPF_PRICE_FROM("#imf_price_from"),
        IPF_PRICE_TO("#imf_price_to"),
        IPF_SQM_FROM("#imf_sqm_from"),
        IPF_SQM_TO("#imf_sqm_to"),
        DIV_SEARCH_RESULTS_PAGE_TITLE("#breadcrumb li strong"),
        BTN_COOKIES_OK(".disc_ok"),
        ;

        private final String myLocator;

        RealEstateUIElements(String locator) {
            myLocator = locator;
        }

        public String get(){
            return myLocator;
        }

    }

    private static final String searchResultsPageTitle = "Αποτελέσματα αναζήτησης";

    /*
     Method to fill in a search in specific area
    */

    public RealEstateObjects searchToRentInArea(WebDriver driver, WebDriverWait wait, String area){

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(RealEstateUIElements.BTN_LET.get())));
        driver.findElement(By.cssSelector(RealEstateUIElements.BTN_LET.get())).click();
        driver.findElement(By.cssSelector(RealEstateUIElements.IPF_SEARCH_LOCATIONS.get())).sendKeys(area);
        //wait until results appear in dropdown
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(RealEstateUIElements.CB_ADD_RESULT_TO_SEARCH.get())));

        return this;
    }

    /*
     Method to verify that all search results contain the search term
    */

    public RealEstateObjects verifySearchResultsContainTerm(WebDriver driver, WebDriverWait wait, String area){

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(RealEstateUIElements.CB_ADD_RESULT_TO_SEARCH.get())));
        //create a list with all search results that appear in dropdown
        List<WebElement> searchResults = driver.findElements(By.cssSelector(RealEstateUIElements.LI_SEARCH_RESULTS.get()));
        //for every element check that its text contains the search term
        for(WebElement searchResult : searchResults){
            String searchResultText = searchResult.getText();
            Assert.isTrue(searchResultText.contains(area),"The search result"+searchResultText+" contains the search term.");
        }

        return this;
    }

    /*
     Method to select all the search results
    */

    public RealEstateObjects selectAllSearchResults(WebDriver driver, WebDriverWait wait){

        Actions action = new Actions(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(RealEstateUIElements.CB_ADD_RESULT_TO_SEARCH.get())));
        //create a list of all search results
        List<WebElement> searchResults = driver.findElements(By.cssSelector(RealEstateUIElements.CB_ADD_RESULT_TO_SEARCH.get()));
        //click on each
        for(WebElement searchResult : searchResults){
            action.moveToElement(searchResult).build().perform();
            searchResult.click();
        }

        return this;
    }

    /*
     Method to verify all selected results remain selected
    */

    public RealEstateObjects verifyAllResultsRemainSelected(WebDriver driver){

        int unselectedResults;
        unselectedResults = driver.findElements(By.cssSelector(RealEstateUIElements.CB_ADD_RESULT_TO_SEARCH.get())).size();
        Assert.isTrue(unselectedResults==0,"All search results are selected.");

        return this;
    }

    /*
     Method to close the search results pop-up
    */

    public RealEstateObjects closeSearchResults(WebDriver driver){

        driver.findElement(By.cssSelector(RealEstateUIElements.IPF_SEARCH_LOCATIONS.get())).click(); //click on input field to close the results

        return this;
    }

    /*
     Method to fill in price
    */

    public RealEstateObjects fillInPrice(WebDriver driver, WebDriverWait wait, String from, String to){

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(RealEstateUIElements.IPF_PRICE_FROM.get())));
        //Type price values on the appropriate fields
        driver.findElement(By.cssSelector(RealEstateUIElements.IPF_PRICE_FROM.get())).sendKeys(from);
        driver.findElement(By.cssSelector(RealEstateUIElements.IPF_PRICE_TO.get())).sendKeys(to);

        return this;
    }

        /*
     Method to fill in square meters
    */

    public RealEstateObjects fillInSQM(WebDriver driver, WebDriverWait wait, String from, String to){

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(RealEstateUIElements.IPF_SQM_FROM.get())));
        //Type sqm values on the appropriate fields
        driver.findElement(By.cssSelector(RealEstateUIElements.IPF_SQM_FROM.get())).sendKeys(from);
        driver.findElement(By.cssSelector(RealEstateUIElements.IPF_SQM_TO.get())).sendKeys(to);

        return this;
    }

    /*
     Method to press the search button
    */

    public RealEstateObjects pressSearchButton(WebDriver driver, WebDriverWait wait){

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(RealEstateUIElements.BTN_SEARCH.get())));
        //Press the search button
        driver.findElement(By.cssSelector(RealEstateUIElements.BTN_SEARCH.get())).click();

        return this;
    }

    /*
     Method to accept cookies
    */

    public RealEstateObjects pressCookiesButton(WebDriver driver){

        //If the button to accept cookies is present, press it
        if(driver.findElements(By.cssSelector(RealEstateUIElements.BTN_COOKIES_OK.get())).size()>0)
            driver.findElement(By.cssSelector(RealEstateUIElements.BTN_COOKIES_OK.get())).click();

        return this;
    }

}
