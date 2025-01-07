package billing;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
 import org.testng.annotations.BeforeMethod;
 import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Compare_lesson_Schedule {
	WebDriver driver;

	 @BeforeMethod
	private void setup() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://stage.schedulehub.io/");
		driver.manage().window().maximize();

		WebElement email = driver.findElement(By.name("email"));
		email.clear();
		email.sendKeys("test@gmail.com");

		WebElement password = driver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("123456");

		WebElement submitBtn = driver.findElement(By.className("_submitBtn_1e7ib_96"));
		submitBtn.click();
		System.out.println("Login successfully");

		Thread.sleep(5000);

		WebElement Schedule = driver.findElement(By.xpath("//a[contains(text(), 'Schedule')]"));
		Schedule.click();

		WebElement Lesson_Schedule = driver.findElement(By.xpath("//span[contains(text(), 'Lesson Schedule')]"));
		Lesson_Schedule.click();

	}

	 @Test
	 private void Schedule_Lesson() throws InterruptedException {

			Thread.sleep(3000);
			// Locate and click the calendar icon
			WebElement calendarIcon = driver.findElement(By.xpath("//button[contains(@aria-label, 'Choose date')]"));
			calendarIcon.click();

			// Find all date elements in the date picker
			List<WebElement> dateElements = driver
					.findElements(By.xpath("//button[contains(@class, 'MuiPickersDay-root')]"));

			// Loop through all the dates from 1 to the end of the month
			for (int i = 1; i <= dateElements.size(); i++) {
				// Locate the specific date element dynamically based on its text
				WebElement dateElement = driver.findElement(By.xpath("//button[contains(@class, 'MuiPickersDay-root') and text()='" + i + "']"));

				// Click the date
				System.out.println("Selecting date:------------------------ " + dateElement.getText());
				dateElement.click();

				// Wait to simulate user interaction (optional)
				Thread.sleep(5000);

				calendarIcon.click();

				// Locate all class elements with 'PL60' in their title
//			    List<WebElement> classCards = driver.findElements(By.xpath("//div[contains(@class, '_card_wbyfb_148')]"));

				List<WebElement> classCards = driver.findElements(By.xpath("//p[contains(text(), 'PL60')]"));

				// Iterate over each PL60 class element
				for (WebElement classElement : classCards) {
					String className = classElement.getText();
					System.out.println("Class: " + className);

					// Locate the parent container of the current class
					WebElement classContainer = classElement.findElement(By.xpath("./ancestor::div[contains(@class, '_card_')]"));

					// Find all students within the current class container
					List<WebElement> allStudents = classContainer.findElements(
							By.xpath(".//p[contains(@class, '_listItem_wbyfb_289') and contains(text(), 'yrs')]"));
					List<WebElement> trialStudents = classContainer.findElements(By.xpath(
							".//div[contains(@class, '_backgroundPurple_wbyfb_451')]//p[contains(@class, '_listItem_wbyfb_289') and contains(text(), 'yrs')]"));
					List<WebElement> makeupStudents = classContainer.findElements(By.xpath(
							".//div[contains(@class, '_backgroundMakeup_wbyfb_455')]//p[contains(@class, '_listItem_wbyfb_289') and contains(text(), 'yrs')]"));

					// Print each student's details
					for (WebElement student : allStudents) {
						System.out.println("Student: " + student.getText());
					}

					for (WebElement trialStudent : trialStudents) {
						System.out.println("Trial Student: " + trialStudent.getText());
					}

					for (WebElement makeupStudent : makeupStudents) {
						System.out.println("Makeup Student: " + makeupStudent.getText());
					}

					// Print the total number of students in the current class
					System.out.println("Total students in this class: " + allStudents.size());
					System.out.println("Total trial students in this class: " + trialStudents.size());
					System.out.println("Total makeup students in this class: " + makeupStudents.size());
					System.out.println("-------------------------------------------------");
				}
			}

		}

	 
	 

	 @AfterMethod
	public void cleanUp() {
		// driver.manage().deleteAllCookies();
		//// driver.navigate().refresh();
		driver.close();
	}

}
