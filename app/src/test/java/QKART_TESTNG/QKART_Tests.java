package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;

import static org.testng.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.Group;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

public class QKART_Tests {

    static RemoteWebDriver driver;
    //WebDriver driver;
    public static String lastGeneratedUserName;

    @BeforeSuite(alwaysRun = true)
    public static void createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);
        System.out.println("createDriver()");
    }

    /*
     * Testcase01: Verify a new user can successfully register
     */
         @Test(priority = 1, description  = "Verify that a new user can register and login to QKart", groups = {"Sanity"})
         public void TestCase01() throws InterruptedException {
        Boolean status;
        //  logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");
        //  takeScreenshot(driver, "StartTestCase", "TestCase1");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
         status = registration.registerUser("testUser1", "abc@123", true);
        assertTrue(status, "Failed to register new user");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
         status = login.PerformLogin(lastGeneratedUserName, "abc@123");
         //logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        assertTrue(status, "Failed to login with registered user");

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();

        //  logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status
        //  ? "PASS" : "FAIL");
        //  takeScreenshot(driver, "EndTestCase", "TestCase1");
    }

    @Test(priority = 2, description = "Verify that an existing user is not allowed to re-register on QKart", groups = {"Sanity"})
    public void  TestCase02() throws InterruptedException {
        Boolean status;
        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Create New User");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);
        assertFalse(status, "Failed to login with registered user");
    }

    @Test(priority = 3, description = "Verify the functionality of search text box", groups = {"Sanity"})
    public void TestCase03() throws InterruptedException {
        boolean status;
        // Verify the functionality of search text box
        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for the "yonex" product
        status = homePage.searchForProduct("YONEX");

        //assertTrue(status,"product are displayed");

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();
        // Verify the search results are available

        // int search_count = searchResults.size();
        // assertEquals(search_count, 0, "given result are matched");

        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();

            assert elementText.toUpperCase().contains("YONEX") : "Test Case Failure. Test Results contains unexpected values: " + elementText;
        }

        // Search for product
        status = homePage.searchForProduct("Gesundheit");


        // Verify no search results are found
        searchResults = homePage.getSearchResults();

        

        if (searchResults.isEmpty()) {
            if (homePage.isNoResultFound()) {
                Assert.assertTrue(true, "Successfully validated that no products found message is displayed");
            } else {
                Assert.fail("Test Case Fail. Expected: no results, actual: Results were available");
            }
        } else {
            Assert.fail("Test Case Fail. Expected: no results, actual: Results were available");
        }
    }

    /*
     * Verify the presence of size chart and check if the size chart content is as
     * expected
     */

    @Test(priority = 4, description = "Verify the existence of size chart for certain items and validate contents of size chart", groups = {"Regression"})
    public void TestCase04() throws InterruptedException {
        // TestCase 4 Start test case: Verify the presence of size Chart
        boolean status = false;
    
        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();
    
        // Search for a product and get card content elements of search results
        status = homePage.searchForProduct("Running Shoes");
        List<WebElement> searchResults = homePage.getSearchResults();
    
        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));
    
        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);
    
            if (result.verifySizeChartExists()) {
                Assert.assertTrue(true, "Successfully validated presence of Size Chart Link");
    
                // Verify if the size dropdown exists
                status = result.verifyExistenceofSizeDropdown(driver);
                Assert.assertTrue(status, "Validated presence of drop down");
    
                // Open the size chart
                if (result.openSizechart()) {
                    // Verify if the size chart contents match the expected values
                    if (result.validateSizeChartContents(expectedTableHeaders, expectedTableBody, driver)) {
                        Assert.assertTrue(true, "Successfully validated contents of Size Chart Link");
                    } else {
                        Assert.fail("Failure while validating contents of Size Chart Link");
                        status = false;
                    }
    
                    // Close the size chart modal
                    status = result.closeSizeChart(driver);
                } else {
                    Assert.fail("Test Case Fail. Failure to open Size Chart");
                }
            } else {
                Assert.fail("Test Case Fail. Size Chart Link does not exist");
            }
        }
    
        Assert.assertTrue(status, "End Test Case: Validated Size Chart Details");
    }
    
    @Test(priority = 5, description = "Verify that a new user can add multiple products in to the cart and Checkout", groups = {"Sanity"})
    public void TestCase05() throws InterruptedException {
        Boolean status;
        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
       
        assertTrue(status, "Register User not able to log in");

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // if (!status) {
        //     logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        //     logStatus("End TestCase", "Test Case 5: Happy Flow Test Failed : ", status ? "PASS" : "FAIL");
        // }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("YONEX");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        status = homePage.searchForProduct("Tan");
        homePage.addProductToCart("Tan Leatherette Weekender Duffle");

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        // Place the order
        checkoutPage.placeOrder();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");

        // Go to the home page
        homePage.navigateToHome();

        // Log out the user
        homePage.PerformLogout();

        // logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");
    }

    @Test(priority = 6, description = "Verify that the contents of the cart can be edited", groups = {"Regression"})
    @Parameters({"Xtend","Yarine"})
    public void   TestCase06(String prod1, String prod2) throws InterruptedException {
        Boolean status;
        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        
        //assertTrue(status, "Register User not able to log in");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        assertTrue(status, "not able to login in qkart");

        homePage.navigateToHome();
        status = homePage.searchForProduct(prod1);
        homePage.addProductToCart("Xtend Smart Watch");

        status = homePage.searchForProduct(prod2);
        homePage.addProductToCart("Yarine Floor Lamp");

        // update watch quantity to 2
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);

        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);

        // update watch quantity again to 1
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();

        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
        } catch (TimeoutException e) {
            System.out.println("Error while placing order in: " + e.getMessage());
        }

        status = driver.getCurrentUrl().endsWith("/thanks");

        homePage.navigateToHome();
        homePage.PerformLogout();
    }

    @Test(priority = 7, description = "Verify that insufficient balance error is thrown when the wallet balance is not enough", groups = {"Regression"})
    public void TestCase07() throws InterruptedException {
        Boolean status;

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);

        assertTrue(status, "Register User not able to log in");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("Stylecon");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set ");

        homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set ", 10);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = checkoutPage.verifyInsufficientBalanceMessage();
    }

    @Test(priority = 8, description = "Verify that a product added to a cart is available when a new tab is added", groups = {"Sanity"})
    public void TestCase08() throws InterruptedException {
        Boolean status = false;

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        String currentURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

        driver.get(currentURL);
        Thread.sleep(2000);

        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedResult);

        driver.close();

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
    }

    @Test(priority = 9, description = "Verify that privacy policy and about us links are working fine", groups = {"Regression"})
    public void TestCase09() throws InterruptedException {
        Boolean status = false;

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        String basePageURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        status = driver.getCurrentUrl().equals(basePageURL);

        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
        WebElement PrivacyPolicyHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = PrivacyPolicyHeading.getText().equals("Privacy Policy");

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
        driver.findElement(By.linkText("Terms of Service")).click();

        handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
        WebElement TOSHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = TOSHeading.getText().equals("Terms of Service");

        driver.close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

    }

    @Test(priority = 10, description = "Verify that the contact us dialog works fine", groups = {"Regression"})
    public void TestCase10() throws InterruptedException {

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        driver.findElement(By.xpath("//*[text()='Contact us']")).click();

        WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        name.sendKeys("crio user");
        WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        email.sendKeys("criouser@gmail.com");
        WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        message.sendKeys("Testing the contact us page");

        WebElement contactUs = driver.findElement(
                By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));

        contactUs.click();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.invisibilityOf(contactUs));

    }

    @Test(priority = 11, description = "Ensure that the Advertisement Links on the QKART page are clickable", groups = {"Regression"})
    public void TestCase11() throws InterruptedException {
        Boolean status = false;

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX Smash Badminton Racquet");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1  addr Line 2  addr line 3");
        checkoutPage.selectAddress("Addr line 1  addr Line 2  addr line 3");
        checkoutPage.placeOrder();
        Thread.sleep(3000);

        String currentURL = driver.getCurrentUrl();

        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

        status = Advertisements.size() == 3;
        //logStatus("Step ", "Verify that 3 Advertisements are available", status ? "PASS" : "FAIL");

        WebElement Advertisement1 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        //logStatus("Step ", "Verify that Advertisement 1 is clickable ", status ? "PASS" : "FAIL");

        driver.get(currentURL);
        Thread.sleep(3000);

        WebElement Advertisement2 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
    }

    @AfterSuite
    public static void quitDriver() {
        System.out.println("quit()");
        driver.quit();
    }

    // public static void logStatus(String type, String message, String status) {

    //     System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
    //             message, status));
    // }

    // public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
    //     try {
    //         File theDir = new File("/screenshots");
    //         if (!theDir.exists()) {
    //             theDir.mkdirs();
    //         }
    //         String timestamp = String.valueOf(java.time.LocalDateTime.now());
    //         String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);
    //         TakesScreenshot scrShot = ((TakesScreenshot) driver);
    //         File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
    //         File DestFile = new File("screenshots/" + fileName);
    //         FileUtils.copyFile(SrcFile, DestFile);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
}

