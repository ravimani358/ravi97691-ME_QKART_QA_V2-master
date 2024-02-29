package QKART_SANITY_LOGIN.Module1;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchResult {
    WebElement parentElement;

    public SearchResult(WebElement SearchResultElement) {
        this.parentElement = SearchResultElement;
    }

    /*
     * Return title of the parentElement denoting the card content section of a
     * search result
     */
    public String getTitleofResult() throws InterruptedException {
        String titleOfSearchResult = "";
        // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
        // Find the element containing the title (product name) of the search result and
        // assign the extract title text to titleOfSearchResult        
        Thread.sleep(3000);
        
        List<WebElement> searchResults = parentElement.findElements(By.xpath("//p[contains(@class ,'css-yg30e6')]"));
        
       for(WebElement search_prod : searchResults) {
    	   titleOfSearchResult = search_prod.getText();
    	   System.out.println(titleOfSearchResult);
       }

        return titleOfSearchResult;
    }

    /*
     * Return Boolean denoting if the open size chart operation was successful
     */
    public Boolean openSizechart() {
        try {

            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            // Find the link of size chart in the parentElement and click on it
            Thread.sleep(3000);
            parentElement.findElement(By.xpath("//button[text()='Size chart']")).click();;

            return true;
        } catch (Exception e) {
            System.out.println("Exception while opening Size chart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the close size chart operation was successful
     */
    public Boolean closeSizeChart(WebDriver driver) {
        try {
            Thread.sleep(2000);
            Actions action = new Actions(driver);

            // Clicking on "ESC" key closes the size chart modal
            action.sendKeys(Keys.ESCAPE);
            action.perform();
            Thread.sleep(2000);
            return true;
        } catch (Exception e) {
            System.out.println("Exception while closing the size chart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean based on if the size chart exists
     */
    public Boolean verifySizeChartExists() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            /*
             * Check if the size chart element exists. If it exists, check if the text of
             * the element is "SIZE CHART". If the text "SIZE CHART" matches for the
             * element, set status = true , else set to false
             */

            WebElement size_buttons = parentElement.findElement(By.xpath("//button[text()='Size chart']"));
        
        	String button_name = size_buttons.getText();
        	
        	System.out.println(button_name);
        	
        	if(button_name.equals("SIZE CHART")) {
        		status = true;
        		
        		System.out.println("SIZE CHART"+status);
        	}else {
        		status = false;
        		System.out.println("SIZE CHART"+status);
        	}

            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if the table headers and body of the size chart matches the
     * expected values
     */
    public Boolean validateSizeChartContents(List<String> expectedTableHeaders, List<List<String>> expectedTableBody,
            WebDriver driver) {
        Boolean status = true;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            /*
             * Locate the table element when the size chart modal is open
             * 
             * Validate that the contents of expectedTableHeaders is present as the table
             * header in the same order
             * 
             * Validate that the contents of expectedTableBody are present in the table body
             * in the same order
             */

            
        // Extracting actual table headers and body values
        List<WebElement> actualTableHeaders = driver.findElements(By.xpath("//table/thead/tr/th"));
        List<WebElement> actualTableData = driver.findElements(By.xpath("//table/tbody/tr/td"));
        boolean headersMatch = true;
            Thread.sleep(1000);
        // Comparing actual and expected table headers
        for (int i = 0; i < actualTableHeaders.size(); i++) {
            String actualHeader = actualTableHeaders.get(i).getText();
            String expectedHeader = expectedTableHeaders.get(i);
        //Thread.sleep(1000);
        
            if (!actualHeader.equals(expectedHeader)) {
                //Thread.sleep(3000);
                headersMatch = false;
                System.out.println("Table header at index " + i + " does not match the expected header.");
                break; // Break the loop on the first mismatch
            }
        }
        
        // Check if all headers match
        if (headersMatch) {
            System.out.println("All table headers match the expected headers.");
        } else {
            System.out.println("Not all table headers match the expected headers.");
        }

        // Comparing actual and expected table body values
        boolean bodyMatches = true;
        for (int i = 0; i < expectedTableBody.size(); i++) {
            for (int j = 0; j < expectedTableBody.get(i).size(); j++) {
                int index = i * expectedTableHeaders.size() + j;
                String actualValue = actualTableData.get(index).getText();
                if (!actualValue.equals(expectedTableBody.get(i).get(j))) {
                    bodyMatches = false;
                    break;
                }
            }
        }

        // Checking if both headers and body match the expectations
        if (headersMatch && bodyMatches) {
            System.out.println("Table matches the expected values.");
        } else {
            System.out.println("Table does not match the expected values.");
        }
            return status;

        } catch (Exception e) {
            System.out.println("Error while validating chart contents");
            return false;
        }
    }

    /*
     * Return Boolean based on if the Size drop down exists
     */
    public Boolean verifyExistenceofSizeDropdown(WebDriver driver) {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            // If the size dropdown exists and is displayed return true, else return false

            Thread.sleep(2000);
            status = driver.findElement(By.xpath("//label[text() = 'Size']")).isDisplayed();
               if(status ==  true) {
                   status = true;
                   System.out.println(status);
               }
            return status;
        } catch (Exception e) {
            return status;
        }
    }
}