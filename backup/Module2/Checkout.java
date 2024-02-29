package QKART_SANITY_LOGIN.Module1;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Checkout {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/checkout";

    public Checkout(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToCheckout() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    /*
     * Return Boolean denoting the status of adding a new address
     */
    public Boolean addNewAddress(String addresString) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Click on the "Add new address" button, enter the addressString in the address
             * text box and click on the "ADD" button to save the address
             */
            driver.findElement(By.xpath("//button[text() = 'Add new address']")).click();   
            driver.findElement(By.xpath("(//textarea[contains(@class, 'css-u36398')])[1]")).sendKeys(addresString);
            driver.findElement(By.xpath("//button[text()='Add']")).click();

            return false;
        } catch (Exception e) {
            System.out.println("Exception occurred while entering address: " + e.getMessage());
            return false;

        }
    }

    /*
     * Return Boolean denoting the status of selecting an available address
     */
    public Boolean selectAddress(String addressToSelect) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through all the address boxes to find the address box with matching
             * text, addressToSelect and click on it
             */

            //Thread.sleep(4000);

            System.out.println(">>>>>>>>>>>>>"+addressToSelect);
        
            Thread.sleep(4000);

            driver.findElement(By.xpath("(//p[text()= '"+addressToSelect+"'])/preceding-sibling::span")).click(); 

            return true;
        } catch (Exception e) {
            System.out.println("Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }

    }

    /*
     * Return Boolean denoting the status of place order action
     */
    public Boolean placeOrder() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find the "PLACE ORDER" button and click on it

            WebElement place_order = driver.findElement(By.xpath("//button[text() = 'PLACE ORDER']"));
        
        if(place_order.isDisplayed()) {
            Thread.sleep(3000);
        	place_order.click();
        }

            return true;

        } catch (Exception e) {
            System.out.println("Exception while clicking on PLACE ORDER: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the insufficient balance message is displayed
     */
    public Boolean verifyInsufficientBalanceMessage() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 07: MILESTONE 6
            
            WebElement insufficientBalanceMessage = driver.findElement(By.className("SnackbarItem-message"));
            String actualMessage = insufficientBalanceMessage.getText();
            String expectedMessage = "You do not have enough balance in your wallet for this purchase";

            if (actualMessage.equals(expectedMessage)) {
                System.out.println("Insufficient balance message is displayed correctly.");
                return true;
            } else {
                System.out.println("Insufficient balance message is not displayed correctly.");
                //return false;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Exception while verifying insufficient balance message: " + e.getMessage());
            return false;
        }
    }
}
