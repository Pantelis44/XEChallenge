package selenium.xeWebClientAutomation.realEstate;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.*;
import selenium.xeWebClientAutomation.pageObjects.RealEstateObjects;
import selenium.xeWebClientAutomation.pageObjects.SearchResultsPageObjects;

public class VerifySearchPageResults extends BaseTest {

    @Autowired
    RealEstateObjects realEstateObjects;

    @Autowired
    SearchResultsPageObjects searchResultsPageObjects;

    private static final String area = "Παγκράτι";
    private static final String priceFrom = "200";
    private static final String priceTo = "700";
    private static final String sqmFrom = "75";
    private static final String sqmTo = "150";
    private static final String sortPriceDesc = "Τιμή (φθίνουσα σειρά)";
    private static final String sortDefault = "προεπιλεγμένα κριτήρια";

    @BeforeClass
    public void startTest() throws InterruptedException {
        setup();
        driver.manage().window().maximize();
        realEstateObjects.pressCookiesButton(driver);
        realEstateObjects.searchToRentInArea(driver,wait,area);
        realEstateObjects.selectAllSearchResults(driver,wait);
        realEstateObjects.closeSearchResults(driver);
        realEstateObjects.fillInPrice(driver,wait,priceFrom,priceTo);
        realEstateObjects.fillInSQM(driver,wait,sqmFrom,sqmTo);
        realEstateObjects.pressSearchButton(driver,wait);
        searchResultsPageObjects.verifyTransitionToSearchResults(driver,wait);
    }

    @AfterClass
    public void endTest(){
        tearDown();
    }

    @Test(priority = 0, description = "Verify results square footage")
    public void verifySQMResults() throws InterruptedException {

        searchResultsPageObjects.verifyAdsSquareFootage(driver,wait,sqmFrom,sqmTo);
        searchResultsPageObjects.goBacktoFirstPage(driver);

    }

    @Test(priority = 1, description = "Verify results price")
    public void verifyPriceResults() throws InterruptedException {

        searchResultsPageObjects.verifyAdsPrice(driver,wait,priceFrom,priceTo);
        searchResultsPageObjects.goBacktoFirstPage(driver);

    }

    @Test(priority = 2, description = "Verify ads number of photos")
    public void verifyNumberOfPhotos() throws InterruptedException {

        searchResultsPageObjects.verifyAdsPhotosNumber(driver,wait);
        searchResultsPageObjects.goBacktoFirstPage(driver);

    }

    @Test(priority = 3, description = "Verify descending price sorting")
    public void verifyDescPriceSorting() throws InterruptedException {

        searchResultsPageObjects.sortResultsDesc(driver,wait,sortPriceDesc);
        searchResultsPageObjects.verifyAdsPriceDescending(driver,wait);
        searchResultsPageObjects.sortResultsDesc(driver,wait,sortDefault);

    }

    @Test(priority = 4, description = "Verify Plus ads appear first")
    public void verifyPlusAds() throws InterruptedException {

        searchResultsPageObjects.verifyPlusAds(driver,wait);
        searchResultsPageObjects.goBacktoFirstPage(driver);

    }

    @Test(priority = 5, description = "Verify telephone button instead of number")
    public void verifyPhoneButton() throws InterruptedException {

        searchResultsPageObjects.verifyPhoneButton(driver,wait);
        searchResultsPageObjects.goBacktoFirstPage(driver);

    }


}
