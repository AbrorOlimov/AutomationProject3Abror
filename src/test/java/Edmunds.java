import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Edmunds {
    public static void main(String[] args) throws InterruptedException {
        //Initializing driver
        WebDriver driver = null;

        try{
            driver = new ChromeDriver();
            driver.manage().window().maximize(); // Maximizing window
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // Adding implicit wait time

//---------------------------------------------------1-----------------------------------------------------------
            driver.get("https://www.edmunds.com/");  // Navigating to the website

//---------------------------------------------------2-----------------------------------------------------------
            driver.findElement(By.xpath("//a[@href='/inventory/srp.html?inventorytype=used%2Ccpo']")).click(); // Navigating to "Shop Used" btn

//---------------------------------------------------3-----------------------------------------------------------
            WebElement zipCode = driver.findElement(By.xpath("//input[@name='zip']")); //finding zipcode input element & initializing it
            zipCode.sendKeys(Keys.COMMAND+"a", Keys.DELETE); // clearing existing text
            zipCode.sendKeys("22031"); //writing our intended zipcode

//---------------------------------------------------4-----------------------------------------------------------
            WebElement checkbox = driver.findElement(By.xpath("(//span[@class='checkbox checkbox-icon size-18 icon-checkbox-unchecked3 text-gray-form-controls'])[2]")); //initializing checkbox element for "Only show local listings"
            checkbox.click(); // clicking the checkbox

//---------------------------------------------------5-----------------------------------------------------------
            Select dropdownObj = new Select(driver.findElement(By.xpath("//select[@id='usurp-make-select']"))); // getting the dropdown element and initializing the web element location
            dropdownObj.selectByVisibleText("Tesla"); // selecting "Tesla" option from the dropdown

//---------------------------------------------------6-----------------------------------------------------------
            String addModelSelect = new Select(driver.findElement(By.xpath("//select[@name='model']"))).getFirstSelectedOption().getText(); // getting the string format of the first element (default one) which is "Add Model"
            Assert.assertEquals(addModelSelect, "Add Model"); // check if we are actually getting the default "Add value" option for model

            WebElement minYear = driver.findElement(By.xpath("//input[@id='min-value-input-Year']"));
            minYear.sendKeys(Keys.COMMAND+"a", Keys.DELETE);      // clearing input
            Thread.sleep(1000);
            minYear.sendKeys("2012", Keys.ENTER);   // minimum year - 2012
            WebElement maxYear = driver.findElement(By.xpath("//input[@id='max-value-input-Year']"));
            maxYear.sendKeys(Keys.COMMAND+"a", Keys.DELETE);     // clearing input
            Thread.sleep(1000);;
            maxYear.sendKeys( "2023", Keys.ENTER);  // maximum year - 2012

//---------------------------------------------------7-----------------------------------------------------------
            List<String> models = List.of("Add Model", "Model 3", "Model S", "Model X", "Model Y", "Cybertruck", "Roadster"); // Creating a list of expected models
            Select modelsDropdown = new Select(driver.findElement(By.xpath("//select[@name='model']"))); // dropdown element

            List<WebElement> allModels = modelsDropdown.getOptions(); // Storing all options from the dropdown into a list
            List<String> actualModels = new ArrayList<>(); // creating arraylist to store all actual model options as a string format
            for (WebElement allModel : allModels) {   //iterate & store into arraylist
                actualModels.add(allModel.getText());
            }

            Assert.assertEquals(actualModels, models);  // comparing actual VS expected models

//---------------------------------------------------8-----------------------------------------------------------
            modelsDropdown.selectByVisibleText("Model 3"); // Choosing "Model 3" from the dropdown

//---------------------------------------------------9-----------------------------------------------------------
            Thread.sleep(3000);
            WebElement minYearTesla = driver.findElement(By.xpath("//input[@id='min-value-input-Year']"));
            minYearTesla.sendKeys(Keys.COMMAND+"a", Keys.DELETE);  // clearing min year
            Thread.sleep(1000);
            minYearTesla.sendKeys("2020", Keys.ENTER);  // set min year - 2020
            Thread.sleep(3000);

//---------------------------------------------------10-----------------------------------------------------------
            List<WebElement> titles = driver.findElements(By.xpath("//div[@class='size-16 font-weight-bold mb-0_5 text-blue-30']")); // getting all title elements from the page and storing them
            for (WebElement title : titles) {
                System.out.println(title.getText()); // printing all titles
            }

            Assert.assertEquals(titles.size(), 21); //verify if the number of all displayed titles is 21

            String result = "Tesla Model 3";

            // creating a new arraylist to store all years and at the same time verify if each title contains relevant text
            List<Integer> years = new ArrayList<>();
            for (WebElement title : titles) {
                Assert.assertTrue(title.getText().toLowerCase().contains(result.toLowerCase()));
                years.add(Integer.parseInt(title.getText().substring(0,4)));
            }

//---------------------------------------------------11-----------------------------------------------------------

            int minRange = 2020;
            int maxRange = 2023;

            // check if each car's year is within the range
            for (Integer year : years) {
                Assert.assertTrue(year >= minRange && year <= maxRange);
            }
//---------------------------------------------------12-----------------------------------------------------------
            Select sortBy = new Select(driver.findElement(By.xpath("//select[@id='sort-by']")));
            sortBy.selectByVisibleText("Price: Low to High");     // sorting results on the web page according to their price
            Thread.sleep(3000);

            List<WebElement> priceElements = driver.findElements(By.xpath("//span[@class='heading-3']")); //storing all price elements

            List<Integer> prices = new ArrayList<>();
            // remove unnecessary chars from price ($ and ,) and store them into a separate arraylist
            for (WebElement priceElement : priceElements) {
                prices.add(Integer.parseInt(priceElement.getText().replaceAll("[$,]", "")));
            }

            final List<Integer> pricesSorted = new ArrayList<>(prices); // create a copy of the arraylist
            Collections.sort(pricesSorted); // sort the copy

            Assert.assertTrue(prices.equals(pricesSorted)); // verify if original is sorted as its copy

//---------------------------------------------------13-----------------------------------------------------------

            sortBy.selectByVisibleText("Price: High to Low");     // sorting results on the web page according to their price
            Thread.sleep(3000);

            List<WebElement> priceElements2 = driver.findElements(By.xpath("//span[@class='heading-3']")); //storing all price elements

            List<Integer> pricesHighToLow = new ArrayList<>();
            // remove unnecessary chars from price ($ and ,) and store them into a separate arraylist
            for (WebElement priceElement : priceElements2) {
                pricesHighToLow.add(Integer.parseInt(priceElement.getText().replaceAll("[$,]", "")));
            }

            final List<Integer> pricesHighToLowSorted = new ArrayList<>(pricesHighToLow); // create a copy of the arraylist
            Collections.sort(pricesHighToLowSorted, Collections.reverseOrder()); // sort the copy

            Assert.assertTrue(pricesHighToLow.equals(pricesHighToLowSorted)); // verify if original is sorted as its copy

//---------------------------------------------------14-----------------------------------------------------------
            sortBy.selectByVisibleText("Mileage: Low to High");
            Thread.sleep(3000);

            List<WebElement> mileages = driver.findElements(By.xpath("//span[@title=\"Car Mileage\"]/following-sibling::span[@class=\"text-cool-gray-30\"]"));
            List<Integer> mileageHighToLow = new ArrayList<>();
            for (WebElement mileage: mileages) {
                mileageHighToLow.add(Integer.parseInt(mileage.getText().replaceAll("[,miles]", "").trim()));
            }
            final List<Integer> mileagesSorted = new ArrayList<>(mileageHighToLow);
            Collections.sort(mileagesSorted);

            Assert.assertTrue(mileageHighToLow.equals(mileagesSorted));

//---------------------------------------------------15-----------------------------------------------------------
            int lastIndex = mileages.size();
            String lastTitle = driver.findElement(By.xpath("(//div[@class='size-16 font-weight-bold mb-0_5 text-blue-30'])["+ lastIndex +"]")).getText();
            String lastPrice = driver.findElement(By.xpath("(//span[@class='heading-3'])["+ lastIndex +"]")).getText();
            String lastMileage = driver.findElement(By.xpath("(//span[@title=\"Car Mileage\"]/following-sibling::span[@class=\"text-cool-gray-30\"])["+ lastIndex +"]")).getText();

            driver.findElement(By.xpath("(//img[@data-test='image'])["+ lastIndex +"]")).click();

//---------------------------------------------------16-----------------------------------------------------------

            Assert.assertEquals(driver.findElement(By.xpath("//span[@data-testid]")).getText(), lastPrice);
            Assert.assertEquals(driver.findElement(By.xpath("//h1[@class='d-inline-block mb-0 heading-2 mt-0_25']")).getText(), lastTitle);

//---------------------------------------------------17-----------------------------------------------------------

            driver.navigate().back();

            WebElement divElement = driver.findElement(By.xpath("(//div[@class='usurp-inventory-card-photo pos-r desktop-photo srp-expanded'])[" + lastIndex + "]"));
            Assert.assertTrue(divElement.findElement(By.xpath("//div[@class='bg-white text-gray-darker']")).isDisplayed());

        }
//---------------------------------------------------18-----------------------------------------------------------
        finally {
            Thread.sleep(5000); // wait 5 secs before quiting
            driver.quit(); //quit the browser
        }
    }
}
