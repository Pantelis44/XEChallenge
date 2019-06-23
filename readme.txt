The solution was developed in IntelliJ IDEA as a maven project and utilizes Java(1.8.0_211),
 Selenium, TestNG and Spring.The main concept is to create page objects with all the 
actions that can take place in the Real Estate page(RealEstateObjects) and in the Search Results
page(SearchResultsPageObjects). As well as with the corresponding dictionaries, containing 
all the needed locators. The tests use these objects to perform all the needed checks. 
Instead of creating a new object for these actions, spring @Autowired was used. For this 
purpose a configuration file(spring-config.xml) was created.The tests run in Chrome(75.0.3770.100) and for this
the relevant chromedriver file(chromedriver.exe) has been included in the suite. You may
have to change this depending on the chrome version you are going to use. The xml file
that runs the suite is xeChallenge.xml. You should ideally open this project with IntelliJ,
but any solution supporting maven should work. Clean and build skipping tests and then select
to run xeChallenge.xml.
