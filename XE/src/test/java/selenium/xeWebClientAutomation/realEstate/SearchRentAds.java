package selenium.xeWebClientAutomation.realEstate;

import org.testng.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import selenium.xeWebClientAutomation.pageObjects.RealEstateObjects;
import selenium.xeWebClientAutomation.pageObjects.SearchResultsPageObjects;

public class SearchRentAds extends BaseTest {

    @Autowired
    RealEstateObjects realEstateObjects;

    @Autowired
    SearchResultsPageObjects searchResultsPageObjects;

    private static final String area = "Παγκράτι";
    private static final String priceFrom = "200";
    private static final String priceTo = "700";
    private static final String sqmFrom = "75";
    private static final String sqmTo = "150";

    @BeforeMethod
    public void startTest(){
        setup();
    }

    @AfterMethod
    public void endTest(){
        tearDown();
    }

    @Test(priority = 0, description = "Search properties to rent - verify results")
    public void searchPropertiesToRent() {

        realEstateObjects.searchToRentInArea(driver,wait,area);
        realEstateObjects.verifySearchResultsContainTerm(driver,wait,area);

    }

    @Test(priority = 1, description = "Search properties to rent - select all results - verify all remain selected",dependsOnMethods={"searchPropertiesToRent"})
    public void searchPropertiesToRentSelectAll() {

        realEstateObjects.searchToRentInArea(driver,wait,area);
        realEstateObjects.selectAllSearchResults(driver,wait);
        realEstateObjects.verifyAllResultsRemainSelected(driver);
        realEstateObjects.closeSearchResults(driver);
    }

    @Test(priority = 2, description = "Search properties to rent - fill in price and sqm - verify transition to search results page",dependsOnMethods={"searchPropertiesToRentSelectAll"})
    public void searchToRentFillInPriceSQM() {

        realEstateObjects.searchToRentInArea(driver,wait,area);
        realEstateObjects.selectAllSearchResults(driver,wait);
        realEstateObjects.closeSearchResults(driver);
        realEstateObjects.fillInPrice(driver,wait,priceFrom,priceTo);
        realEstateObjects.fillInSQM(driver,wait,sqmFrom,sqmTo);
        realEstateObjects.pressSearchButton(driver,wait);
        searchResultsPageObjects.verifyTransitionToSearchResults(driver,wait);

    }


}
