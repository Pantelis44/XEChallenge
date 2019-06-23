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
import java.util.concurrent.TimeUnit;

/*
Object that will be used for all actions that can take place in the Search Results page
 */

@Component
public class SearchResultsPageObjects {

    public enum SearchResultsUIElements { //Dictionary for all the locators that are going to be used
        /**
         *
         */

        DIV_SEARCH_RESULTS_PAGE_TITLE("#breadcrumb li strong"),
        DIV_AD_ENTRY(".lazy.r"),
        DIV_AD_ENTRY_PRICE(".r_price"),
        DIV_AD_ENTRY_PLUS(".gold_plus_label"),
        DIV_AD_ENTRY_TITLE(".lazy.r h2 a"),
        BTN_BACK_TO_SEARCH(".last_search"),
        DIV_AD_SQM("//ul[@class='r_stats']/li[2]"), //xpath
        DIV_AD_NUMBER_OF_PHOTOS(".r_photo"),
        DIV_AD_WAITING_PHOTOS("[src='/property/images/thumbs/no_image_re_residence_128x96.png']"),
        DIV_AD_RESULTS_PAGE(".page a"),
        DIV_AD_RESULTS_FIRST_PAGE(".white_button.firstpage"),
        BTN_SORT_RESULTS("#r_sorting_link"),
        DIV_SORT_OPTION("#r_sorting_selection li a"),
        BTN_SHOW_PHONE_NUMBER(".column_300 .details_phone_lnk"),
        ;

        private final String myLocator;

        SearchResultsUIElements(String locator) {
            myLocator = locator;
        }

        public String get(){
            return myLocator;
        }

    }

    private static final String searchResultsPageTitle = "Αποτελέσματα αναζήτησης";
    private static final int numberOfPhotos = 10; //The maximum number of photos an ad can have

    /*
     Method to verify successful transition to search results page - here more verifications could be added
    */

    public SearchResultsPageObjects verifyTransitionToSearchResults(WebDriver driver, WebDriverWait wait){

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SearchResultsUIElements.DIV_SEARCH_RESULTS_PAGE_TITLE.get())));
        String searchResultsTitle;
        //Search for the results page title
        searchResultsTitle = driver.findElement(By.cssSelector(SearchResultsUIElements.DIV_SEARCH_RESULTS_PAGE_TITLE.get())).getText();
        //Assert its value is correct
        Assert.isTrue(searchResultsTitle.equals(searchResultsPageTitle),"The results page title value is correct.");

        return this;
    }

    /*
     Method to verify the price of an ad is within given boundaries
    */

    public SearchResultsPageObjects verifyAdPrice(WebElement ad, String priceFrom, String priceTo){

        //Find the element containing the price within the ad element
        String price = ad.findElement(By.cssSelector(SearchResultsUIElements.DIV_AD_ENTRY_PRICE.get())).getText().replaceAll("[^0-9]", "");
        //Check if its value is within given boundaries
        Assert.isTrue(Integer.parseInt(price)<=Integer.parseInt(priceTo)&&Integer.parseInt(price)>=Integer.parseInt(priceFrom),"Price "+price+" is within given boundaries.");

        return this;
    }

    /*
     Method to verify the square footage of an ad is within given boundaries
    */

    public SearchResultsPageObjects verifySQM( WebElement ad, String sqmFrom, String sqmTo){

        //Find the element containing the squre footage within the ad element
        String sqm = ad.findElement(By.xpath(SearchResultsUIElements.DIV_AD_SQM.get())).getText().replaceAll("[^0-9]", "");
        //Check if its value is within given boundaries
        Assert.isTrue(Integer.parseInt(sqm)<=Integer.parseInt(sqmTo)&&Integer.parseInt(sqm)>=Integer.parseInt(sqmFrom),"Square footage "+sqm+" is within given boundaries.");

        return this;
    }

    /*
     Method to verify the number of pictures of an ad is no bigger than the given number
    */

    public SearchResultsPageObjects verifyAdPhotos(WebDriver driver, WebElement ad){

        //Set implicit wait to 1 second because we are going to check for every element if the waiting for photos picture is present
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        if(ad.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_WAITING_PHOTOS.get())).size()==0) { //if the add has photos
            String adPhotos = ad.findElement(By.cssSelector(SearchResultsUIElements.DIV_AD_NUMBER_OF_PHOTOS.get())).getText().replaceAll("[^0-9]", "");
            Assert.isTrue(Integer.parseInt(adPhotos) <= numberOfPhotos,"Number of photos is correct.");
        }
        //Reset wait time
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return this;
    }

    /*
     Method to click next results pages
    */

    public SearchResultsPageObjects clickNextResultsPage(WebDriver driver, WebDriverWait wait) throws InterruptedException {

        int count = 0;//counter for number of pages
        Actions action = new Actions(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get())));
        //list of all results page pointers(bottom of screen)
        List<WebElement> resultsPages = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get()));
        action.moveToElement( resultsPages.get(count)).build().perform();
        Thread.sleep(1000); //give some time to the elements to settle down

        for(WebElement resultsPage : resultsPages){
            if(resultsPage.getAttribute("class")!=null && resultsPage.getAttribute("class").equals("here")){//if we found current page
                if(count+1<resultsPages.size())//if there is a next page
                    resultsPages.get(count+1).click();//click on it
                break;
            }
            else
                count++;
        }

        return this;
    }

    /*
     Method to check all ads square footage is between given boundaries
    */

    public SearchResultsPageObjects verifyAdsSquareFootage(WebDriver driver, WebDriverWait wait, String sqmFrom, String sqmTo) throws InterruptedException {

        Actions action = new Actions(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get())));
        //get all result page pointers
        List<WebElement> resultsPages = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get()));
        for(int i=0;i<resultsPages.size();i++){//for each results page
            List<WebElement> pageAds = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_ENTRY.get()));//get all ads
            for(WebElement pageAd : pageAds){//for each ad
                action.moveToElement(pageAd).build().perform();
                verifySQM(pageAd, sqmFrom, sqmTo);//verify square footage
            }
            clickNextResultsPage(driver,wait);//click next results page
        }

        return this;
    }

    /*
     Method to check all ads price is between given boundaries
    */

    public SearchResultsPageObjects verifyAdsPrice(WebDriver driver, WebDriverWait wait, String priceFrom, String priceTo) throws InterruptedException {

        Actions action = new Actions(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get())));
        //get all result page pointers
        List<WebElement> resultsPages = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get()));
        for(int i=0;i<resultsPages.size();i++){//for each results page
            List<WebElement> pageAds = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_ENTRY.get()));//get all ads
            for(WebElement pageAd : pageAds){//for each ad
                action.moveToElement(pageAd).build().perform();
                verifyAdPrice(pageAd, priceFrom, priceTo);//verify price
            }
            clickNextResultsPage(driver,wait);//click next results page
        }

        return this;
    }

    /*
     Method to check all ad photos don't exceed the given number
    */

    public SearchResultsPageObjects verifyAdsPhotosNumber(WebDriver driver, WebDriverWait wait) throws InterruptedException {

        Actions action = new Actions(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get())));
        //get all result page pointers
        List<WebElement> resultsPages = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get()));
        for(int i=0;i<resultsPages.size();i++){//for each results page
            List<WebElement> pageAds = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_ENTRY.get()));//get all ads
            for(WebElement pageAd : pageAds){//for each ad
                action.moveToElement(pageAd).build().perform();
                verifyAdPhotos(driver, pageAd);//verify number of photos
            }
            clickNextResultsPage(driver,wait);//click next results page
        }

        return this;
    }

    /*
     Method to go back to the first page of the results
    */

    public SearchResultsPageObjects goBacktoFirstPage(WebDriver driver){

        driver.findElement(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_FIRST_PAGE.get())).click();

        return this;
    }

    /*
     Method to go back to the first page of the results
    */

    public SearchResultsPageObjects sortResultsDesc(WebDriver driver, WebDriverWait wait,String sortingOption) {

        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(By.cssSelector(SearchResultsUIElements.BTN_SORT_RESULTS.get()))).build().perform();
        driver.findElement(By.cssSelector(SearchResultsUIElements.BTN_SORT_RESULTS.get())).click();//click on sort results button
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SearchResultsUIElements.DIV_SORT_OPTION.get())));
        List<WebElement> sortOptions = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_SORT_OPTION.get()));//get all result options
        for(WebElement sortOption : sortOptions){//for each result option
           if(sortOption.getText().equals(sortingOption)){//check text
               driver.get(sortOption.getAttribute("href"));//select the given one if found
               break;
           }
        }

        return this;
    }

    /*
     Method to verify Ads price descending
    */

    public SearchResultsPageObjects verifyAdsPriceDescending(WebDriver driver, WebDriverWait wait) throws InterruptedException {

        Actions action = new Actions(driver);
        String currentPrice="0";
        String previousPrice="0";
        boolean firstElement = true;

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get())));
        //get all result page pointers
        List<WebElement> resultsPages = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get()));
        for(int i=0;i<resultsPages.size();i++){//for each results page
            List<WebElement> pageAds = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_ENTRY.get()));//get all ads
            for(WebElement pageAd : pageAds){//for each ad
                action.moveToElement(pageAd).build().perform();
                currentPrice = pageAd.findElement(By.cssSelector(SearchResultsUIElements.DIV_AD_ENTRY_PRICE.get())).getText().replaceAll("[^0-9]", "");//get current ad price
                if(!firstElement)//if it's not the first lad compare with previous ad price
                    Assert.isTrue(Integer.parseInt(currentPrice) <= Integer.parseInt(previousPrice),"Ad price is sorted correctly.");
                previousPrice = currentPrice;//after the comparison the current price becomes previous
                firstElement = false;//after the first repetition make this boolean false
            }
            clickNextResultsPage(driver,wait);//click next results page
        }

        return this;
    }

    /*
     Method to verify Plus ads are first
    */

    public SearchResultsPageObjects verifyPlusAds(WebDriver driver, WebDriverWait wait) throws InterruptedException {

        Actions action = new Actions(driver);
        boolean currentPlusStatus=false;
        boolean previousPlusStatus=false;
        boolean firstElement = true;

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get())));
        //get all result page pointers
        List<WebElement> resultsPages = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get()));
        for(int i=0;i<resultsPages.size();i++){//for each results page
            List<WebElement> pageAds = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_ENTRY.get()));//get all ads
            for(WebElement pageAd : pageAds){//for each ad
                action.moveToElement(pageAd).build().perform();
                driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);//reduce implicit wait as we are going to check if the plus element is present
                if( pageAd.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_ENTRY_PLUS.get())).size() >0 )//if ad contains plus element
                 currentPlusStatus = true;//current ad is plus
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//increase again implicit wait
                if(!firstElement) {//if it's not the first ad
                    if(currentPlusStatus)//if current ad is plus
                    Assert.isTrue(previousPlusStatus,"The current ad is plus, as was the previous one.");//check if previous was also plus
                }
                previousPlusStatus = currentPlusStatus;//after the comparison give the value of the plus status to previousPlusStatus
                firstElement = false;//after first repetition we are not at the first element
            }
            clickNextResultsPage(driver,wait);//click next results page
        }

        return this;
    }

    /*
     Method to verify telephone button is present
    */

    public SearchResultsPageObjects verifyPhoneButton(WebDriver driver, WebDriverWait wait) throws InterruptedException {

        Actions action = new Actions(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get())));
        //get all result page pointers
        List<WebElement> resultsPages = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_RESULTS_PAGE.get()));
        for(int i=0;i<resultsPages.size();i++){//for each results page
            List<WebElement> pageAds = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_ENTRY.get()));//get all ads
            for(int j=0; j < pageAds.size();j++){//for every ad
                pageAds = driver.findElements(By.cssSelector(SearchResultsUIElements.DIV_AD_ENTRY.get()));//we need this because after clicking on ad and then coming back to the search results the list of ads is not valid
                action.moveToElement(pageAds.get(j)).build().perform();
                pageAds.get(j).findElement(By.cssSelector(SearchResultsUIElements.DIV_AD_ENTRY_TITLE.get())).click();//click on ad
                Assert.isTrue( driver.findElements(By.cssSelector(SearchResultsUIElements.BTN_SHOW_PHONE_NUMBER.get())).size()>0,"The phone number button is present on ad.");//verify phone button presence
                driver.findElement(By.cssSelector(SearchResultsUIElements.BTN_BACK_TO_SEARCH.get())).click();//click to go back to search results
            }
            clickNextResultsPage(driver,wait);//click next results page
        }

        return this;
    }

}
