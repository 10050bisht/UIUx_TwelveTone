package new_twelvetone;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import io.github.bonigarcia.wdm.WebDriverManager;

public class client_details {

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

	}


	@Test
	private void all_client() {
		// Initialize total client count and active student count
		int totalClientCount = 0;
		int leadClientCount = 0;
		int SchTrialCount = 0;
		int MSDTrialCount = 0;
		int AttTrialCount = 0;
		int activeClientCount = 0;
		int DroppedClientCount = 0;

		// Create a workbook and sheet for Excel
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Client Details");

		// Create header row
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Parent Info");
		headerRow.createCell(1).setCellValue("Phone Number");
		headerRow.createCell(2).setCellValue("Phone Type");
		headerRow.createCell(3).setCellValue("Lead Status");
		headerRow.createCell(4).setCellValue("Email");
		headerRow.createCell(5).setCellValue("Students");
		headerRow.createCell(6).setCellValue("Lead Type");
		headerRow.createCell(7).setCellValue("Awareness");
		headerRow.createCell(8).setCellValue("Create Date");

		int rowIndex = 1; // Start from the second row

		while (true) {
			// Find all rows in the table
			List<WebElement> rows = driver.findElements(By.xpath("//tr[contains(@class, 'MuiTableRow-root css-2ygh3t')]"));

			// Process each row
			for (WebElement row : rows) {
				Row excelRow = sheet.createRow(rowIndex++);

				// Extract and write each cell's content to the Excel sheet
				List<WebElement> cells = row.findElements(By.tagName("td"));
				if (!cells.isEmpty()) {
					excelRow.createCell(0).setCellValue(cells.get(0).getText()); // Parent Info
					excelRow.createCell(1).setCellValue(cells.get(1).getText()); // Phone Number
					excelRow.createCell(2).setCellValue(cells.get(2).getText()); // Phone Type
					excelRow.createCell(3).setCellValue(cells.get(3).getText()); // Lead Status
					excelRow.createCell(4).setCellValue(cells.get(4).getText()); // Email
					excelRow.createCell(5).setCellValue(cells.get(5).getText()); // Students
					excelRow.createCell(6).setCellValue(cells.get(6).getText()); // Lead Type
					excelRow.createCell(7).setCellValue(cells.get(7).getText()); // Awareness
					excelRow.createCell(8).setCellValue(cells.get(8).getText()); // Create Date

					// Check if the student is active based on a specific condition
					String leadStatus = cells.get(3).getText();
					if ("Lead".equalsIgnoreCase(leadStatus)) {
						activeClientCount++;
					}
					else if("SCH trial".equalsIgnoreCase(leadStatus)) {
						leadClientCount++;
					}
					else if("MSD trial".equalsIgnoreCase(leadStatus)) {
						leadClientCount++;
					}
					else if("ATT Trial".equalsIgnoreCase(leadStatus)) {
						leadClientCount++;
					}
					else if("Active".equalsIgnoreCase(leadStatus)) {
						leadClientCount++;
					}
					else if("Dropped".equalsIgnoreCase(leadStatus)) {
						leadClientCount++;
					}
				}
			}

			totalClientCount += rows.size();

			// Check if the "Next Page" button is enabled/exists
			List<WebElement> nextButtonElements = driver.findElements(By.xpath("//button[@aria-label='Go to next page' and @type='button']"));
			if (nextButtonElements.isEmpty() || !nextButtonElements.get(0).isEnabled()) {
				// Exit loop if the "Next Page" button is not present or not enabled
				break;
			}

			// Scroll to the "Next Page" button and click it
			WebElement nextButton = nextButtonElements.get(0);
			new Actions(driver).scrollToElement(nextButton).perform();
			nextButton.click();

			// Wait for the page to load (adjust the wait as per your application's behavior)
			try {
				Thread.sleep(2000); // Use WebDriverWait in real scenarios instead of Thread.sleep
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		  // Write status counts to the Excel sheet
	    Row totalRow = sheet.createRow(rowIndex++);
	    totalRow.createCell(0).setCellValue("Total Clients:");
	    totalRow.createCell(1).setCellValue(totalClientCount);

	    Row activeClientRow = sheet.createRow(rowIndex++);
	    activeClientRow.createCell(0).setCellValue("Active Clients:");
	    activeClientRow.createCell(1).setCellValue(activeClientCount);

	    Row leadClientRow = sheet.createRow(rowIndex++);
	    leadClientRow.createCell(0).setCellValue("Lead Clients:");
	    leadClientRow.createCell(1).setCellValue(leadClientCount);

	    Row schTrialClientRow = sheet.createRow(rowIndex++);
	    schTrialClientRow.createCell(0).setCellValue("SCHTrial Clients:");
	    schTrialClientRow.createCell(1).setCellValue(SchTrialCount);

	    Row msdTrialClientRow = sheet.createRow(rowIndex++);
	    msdTrialClientRow.createCell(0).setCellValue("MSDTrial Clients:");
	    msdTrialClientRow.createCell(1).setCellValue(MSDTrialCount);

	    Row attTrialClientRow = sheet.createRow(rowIndex++);
	    attTrialClientRow.createCell(0).setCellValue("AttTrial Clients:");
	    attTrialClientRow.createCell(1).setCellValue(AttTrialCount);

	    Row droppedClientRow = sheet.createRow(rowIndex++);
	    droppedClientRow.createCell(0).setCellValue("Dropped Clients:");
	    droppedClientRow.createCell(1).setCellValue(DroppedClientCount);

		// Save Excel file
		try (FileOutputStream fileOut = new FileOutputStream("ClientDetails.xlsx")) {
			workbook.write(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Print total client count and active student count
		System.out.println("Total Clients Count: " + totalClientCount);
		System.out.println("Active Clients Count: " + activeClientCount);
		System.out.println("Lead Clients Count: " + leadClientCount);
		System.out.println("SCH_trial Clients Count: " + SchTrialCount);
		System.out.println("MSD_trail Clients Count: " + MSDTrialCount);
		System.out.println("ATT_trial Clients Count: " + AttTrialCount);
		System.out.println("Dropped Clients Count: " + DroppedClientCount);




	}






	@AfterMethod
	private void close_browae() {
			driver.close();

	}    


}
