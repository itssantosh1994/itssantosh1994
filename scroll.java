import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ScrollRightTillEnd {
    public static void main(String[] args) {
        // Set the path to your WebDriver executable
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        // Initialize the WebDriver (Chrome in this example)
        WebDriver driver = new ChromeDriver();

        // Open the desired webpage
        driver.get("https://www.example.com");

        // Scroll to the right of the page till end
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        long lastWidth = (long) js.executeScript("return document.documentElement.scrollLeft;");
        while (true) {
            js.executeScript("window.scrollBy(1000, 0);");
            try {
                Thread.sleep(1000); // Wait to allow scroll to happen
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long currentWidth = (long) js.executeScript("return document.documentElement.scrollLeft;");
            if (lastWidth == currentWidth) {
                break; // Break the loop if the scroll position hasn't changed
            }
            lastWidth = currentWidth;
        }

        // Optionally, wait for a few seconds to see the result
        try {
            Thread.sleep(5000); // 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Close the browser
        driver.quit();
    }
}
