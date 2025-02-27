import java.io.FileOutputStream;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;


public class Demo{
	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeTest
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://dev.schedulehub.io/");

		// Initialize explicit wait
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Login
		driver.findElement(By.name("email")).sendKeys("test@gmail.com");
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.className("_submitBtn_8rox6_99")).click();

		// Wait for navigation to complete
		wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Schedule"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Lesson Schedule']"))).click();
	}

	@Test
	public void capacityCount() throws InterruptedException {    



		int totalSum = 0;
		int maxDays = 28; // Total days to consider for calculation

		for (int week = 0; week < 4; week++) {
			int weeklySum = 0;

			for (int day = 1 + (week * 7); day <= Math.min((week + 1) * 7, maxDays); day++) {
				// Open calendar and select date
				WebElement calendarIcon = driver.findElement(By.xpath("//button[contains(@class, 'MuiButtonBase-root MuiIconButton')]"));
				calendarIcon.click();
				Thread.sleep(2000);

				WebElement dateButton = driver.findElement(By.xpath("//button[@role='gridcell' and contains(@class, 'MuiPickersDay-root') and text()='" + day + "']"));
				dateButton.click();
				Thread.sleep(4000);

				List<WebElement> classElements = driver.findElements(By.xpath("//p[contains(text(), 'P30')]//ancestor::div[contains(@class, '_card_')]"));
				int totalStudentCount = 0;
				int trialStudentCount = 0;
				int makeupStudentCount = 0;

				for (WebElement classElement : classElements) {

					List<WebElement> cancelledTag = classElement.findElements(
							By.xpath(".//p[contains(@class, '_userName_wbyfb_178')]/span[contains(text(), '(CANCELLED)')]")
							);

					if (!cancelledTag.isEmpty()) {
						System.out.println("Skipping cancelled class.");
						continue; // Skip this class
					}

				      // **Count students only for non-cancelled classes**
		            List<WebElement> students = classElement.findElements(By.xpath(".//p[contains(@class, '_listItem_wbyfb_289')][not(contains(text(), 'End Date'))]"));
		            totalStudentCount += students.size();
		        }

		        System.out.println("Day " + day + " Student Count: " + totalStudentCount);

		        // **Add to weekly sum**
		        weeklySum += totalStudentCount;
		    }

		    // **Print weekly student count separately**
		    System.out.println("===== WEEK " + (week + 1) + " TOTAL: " + weeklySum + " Students =====");

		    // **Add to monthly sum**
		    totalSum += weeklySum;
		}

		// **Print final monthly total**
		System.out.println("\n===== MONTHLY TOTAL STUDENTS: " + totalSum + " =====");

		System.out.println("Data written to Excel file successfully.");
	}



	@AfterTest
	public void tearDown() {
		//	        if (driver != null) {
		//	            driver.quit(); // Close the browser only if driver is not null
		System.out.println("Test case passed ");
	}
}
