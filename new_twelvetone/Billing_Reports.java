package new_twelvetone;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Billing_Reports {

	WebDriver driver;

	@BeforeMethod
	private void setup() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://dev.schedulehub.io/");
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

		WebElement reportsElement = driver.findElement(By.xpath("//div[contains(text(), 'Reports')]"));
		reportsElement.click();

		WebElement billingReportButton = driver.findElement(By.xpath("//button[text()='Billing Report']"));
		billingReportButton.click();

	}

	
	@Test
	private void ALLClientRowsToExcel() throws InterruptedException, IOException {
		 Thread.sleep(10000); // Wait for the page to load
			
			List<WebElement> AllClientRows = driver.findElements(By.xpath("//tr[contains(@class, 'MuiTableRow-root css-2ygh3t')]"));

		    // Initialize Excel workbook
		    Workbook workbook = new XSSFWorkbook();
		    Sheet clientDetailsSheet = workbook.createSheet("Scheduled Client Details");
	
		    // Define headers for the Excel sheet
		    String[] headers = {
		        "Client Name", "Program", "Payment Type", "Invoice ID", "Billing Date", 
		        "Total Invoice", "Surcharge", "Discount", "Credits", "Total Due", "Total Paid", "Status"
		    };
		    
		    // Create a row for headers
		    Row headerRow = clientDetailsSheet.createRow(0);
		    for (int i = 0; i < headers.length; i++) {
		        headerRow.createCell(i).setCellValue(headers[i]);
		    }
	
		    // Initialize variables for totals
		    double totalInvoice = 0.0;
		    double totalSurcharge = 0.0;
		    double totalDiscount = 0.0;
		    double totalCredits = 0.0;
		    double totalDue =  0.0;
		    double totalPaid =  0.0;

	
		    // Initialize row index for writing client details
		    int rowIndex = 1;
	
		    // Loop through each row with "failed" status
		    for (WebElement row : AllClientRows) {
		        List<WebElement> cells = row.findElements(By.tagName("td"));
		        
		        // Create a new row for each "failed" client
		        Row excelRow = clientDetailsSheet.createRow(rowIndex++);
		        
		        // Extract values from the relevant columns and write to Excel
		        String invoiceText = cells.get(5).getText().replace("$", "").replace(",", "").trim();
		        String surchargeText = cells.get(6).getText().replace("$", "").replace(",", "").trim();
		        String discountText = cells.get(7).getText().replace("$", "").replace(",", "").trim();
		        String creditsText = cells.get(8).getText().replace("$", "").replace(",", "").trim();
		        String DuedText = cells.get(9).getText().replace("$", "").replace(",", "").trim();
		        String PaidText = cells.get(10).getText().replace("$", "").replace(",", "").trim();

		        
		        // Parse the values to doubles
		        double invoice = invoiceText.isEmpty() ? 0.0 : Double.parseDouble(invoiceText);
		        double surcharge = surchargeText.isEmpty() ? 0.0 : Double.parseDouble(surchargeText);
		        double discount = discountText.isEmpty() ? 0.0 : Double.parseDouble(discountText);
		        double credits = creditsText.isEmpty() ? 0.0 : Double.parseDouble(creditsText);
		        double Due = DuedText.isEmpty() ? 0.0 : Double.parseDouble(DuedText);
		        double Paid = PaidText.isEmpty() ? 0.0 : Double.parseDouble(PaidText);

	
		        // Write data to the row
		        excelRow.createCell(0).setCellValue(cells.get(0).getText()); // Client Name
		        excelRow.createCell(1).setCellValue(cells.get(1).getText()); // Program
		        excelRow.createCell(2).setCellValue(cells.get(2).getText()); // Payment Type
		        excelRow.createCell(3).setCellValue(cells.get(3).getText()); // Invoice ID
		        excelRow.createCell(4).setCellValue(cells.get(4).getText()); // Billing Date
		        excelRow.createCell(5).setCellValue(invoice); // Total Invoice
		        excelRow.createCell(6).setCellValue(surcharge); // Surcharge
		        excelRow.createCell(7).setCellValue(discount); // Discount
		        excelRow.createCell(8).setCellValue(credits); // Credits
		        excelRow.createCell(9).setCellValue(Due); // Total due
		        excelRow.createCell(10).setCellValue(Paid); // Status (you already know it is "Paid")
		        excelRow.createCell(11).setCellValue(cells.get(11).getText()); //Status
	
		        // Update totals for each column
		        totalInvoice += invoice;
		        totalSurcharge += surcharge;
		        totalDiscount += discount;
		        totalCredits += credits;
		        totalDue += Due;
		        totalPaid += Paid;
		    }
	
		    // Add totals row at the end of the sheet
		    Row totalRow = clientDetailsSheet.createRow(rowIndex);
		    totalRow.createCell(0).setCellValue("Total");
		    totalRow.createCell(5).setCellValue(totalInvoice); // Total Invoice
		    totalRow.createCell(6).setCellValue(totalSurcharge); // Total Surcharge
		    totalRow.createCell(7).setCellValue(totalDiscount); // Total Discount
		    totalRow.createCell(8).setCellValue(totalCredits); // Total Credits
		    totalRow.createCell(9).setCellValue(totalDue); // Total due
		    totalRow.createCell(10).setCellValue(totalPaid); // Total paid

	
		    // Save the Excel file to a specific location
		    try (FileOutputStream fileOut = new FileOutputStream("Billing_reports/AllBillingReport.xlsx")) {
		        workbook.write(fileOut);
		        System.out.println("Excel file created: AllBillingReport.xlsx");
		    } finally {
		        workbook.close();
		    }
		}


	@Test
	private void paid_amounts() throws InterruptedException, IOException {
	    Thread.sleep(5000); // Wait for the page to load

	    // Locate all rows with "Paid" status
	    List<WebElement> paidRows = driver.findElements(By.xpath("//tr[.//div[contains(@class, 'MuiStack-root') and contains(@class, 'css-j7qwjs')]//span[contains(text(), 'Paid')]]"));

	    // Initialize Excel workbook
	    Workbook workbook = new XSSFWorkbook();
	    Sheet paidDetailsSheet = workbook.createSheet("Paid Client Details");

	    // Define headers for the Excel sheet
	    String[] headers = {
	        "Client Name", "Program", "Payment Type", "Invoice ID", "Billing Date", 
	        "Total Invoice", "Surcharge", "Discount", "Credits", "Total Paid", "Status"
	    };
	    
	    // Create a row for headers
	    Row headerRow = paidDetailsSheet.createRow(0);
	    for (int i = 0; i < headers.length; i++) {
	        headerRow.createCell(i).setCellValue(headers[i]);
	    }

	    // Initialize variables for totals
	    double totalInvoice = 0.0;
	    double totalSurcharge = 0.0;
	    double totalDiscount = 0.0;
	    double totalCredits = 0.0;
	    double totalPaid = 0.0;

	    // Initialize row index for writing client details
	    int rowIndex = 1;

	    // Loop through each row with "Paid" status
	    for (WebElement row : paidRows) {
	        List<WebElement> cells = row.findElements(By.tagName("td"));
	        
	        // Create a new row for each "Paid" client
	        Row excelRow = paidDetailsSheet.createRow(rowIndex++);
	        
	        // Extract values from the relevant columns and write to Excel
	        String invoiceText = cells.get(5).getText().replace("$", "").replace(",", "").trim();
	        String surchargeText = cells.get(6).getText().replace("$", "").replace(",", "").trim();
	        String discountText = cells.get(7).getText().replace("$", "").replace(",", "").trim();
	        String creditsText = cells.get(8).getText().replace("$", "").replace(",", "").trim();
	        String paidText = cells.get(10).getText().replace("$", "").replace(",", "").trim();
	        
	        // Parse the values to doubles
	        double invoice = invoiceText.isEmpty() ? 0.0 : Double.parseDouble(invoiceText);
	        double surcharge = surchargeText.isEmpty() ? 0.0 : Double.parseDouble(surchargeText);
	        double discount = discountText.isEmpty() ? 0.0 : Double.parseDouble(discountText);
	        double credits = creditsText.isEmpty() ? 0.0 : Double.parseDouble(creditsText);
	        double paid = paidText.isEmpty() ? 0.0 : Double.parseDouble(paidText);

	        // Write data to the row
	        excelRow.createCell(0).setCellValue(cells.get(0).getText()); // Client Name
	        excelRow.createCell(1).setCellValue(cells.get(1).getText()); // Program
	        excelRow.createCell(2).setCellValue(cells.get(2).getText()); // Payment Type
	        excelRow.createCell(3).setCellValue(cells.get(3).getText()); // Invoice ID
	        excelRow.createCell(4).setCellValue(cells.get(4).getText()); // Billing Date
	        excelRow.createCell(5).setCellValue(invoice); // Total Invoice
	        excelRow.createCell(6).setCellValue(surcharge); // Surcharge
	        excelRow.createCell(7).setCellValue(discount); // Discount
	        excelRow.createCell(8).setCellValue(credits); // Credits
	        excelRow.createCell(9).setCellValue(paid); // Total Paid
	        excelRow.createCell(10).setCellValue("Paid"); // Status (you already know it is "Paid")

	        // Update totals for each column
	        totalInvoice += invoice;
	        totalSurcharge += surcharge;
	        totalDiscount += discount;
	        totalCredits += credits;
	        totalPaid += paid;
	    }

	    // Add totals row at the end of the sheet
	    Row totalRow = paidDetailsSheet.createRow(rowIndex);
	    totalRow.createCell(0).setCellValue("Total");
	    totalRow.createCell(5).setCellValue(totalInvoice); // Total Invoice
	    totalRow.createCell(6).setCellValue(totalSurcharge); // Total Surcharge
	    totalRow.createCell(7).setCellValue(totalDiscount); // Total Discount
	    totalRow.createCell(8).setCellValue(totalCredits); // Total Credits
	    totalRow.createCell(9).setCellValue(totalPaid); // Total Paid

	    // Save the Excel file to a specific location
	    try (FileOutputStream fileOut = new FileOutputStream("Billing_reports/PaidClientDetailsWithTotals.xlsx")) {
	        workbook.write(fileOut);
	        System.out.println("Excel file created: PaidClientDetailsWithTotals.xlsx");
	    } finally {
	        workbook.close();
	    }
	}
	
		
		@Test
		private void findFailedRowAndWriteToExcel() throws InterruptedException, IOException {
				    Thread.sleep(10000); // Wait for the page to load
		
			    // Locate all rows with "failed" status
			    List<WebElement> failedRows = driver.findElements(By.xpath("//tr[.//div[contains(@style, 'background: rgb(231, 102, 102)') and text()='Failed']]"));

//			    List<WebElement> failedRows = driver.findElements(By.xpath("//tr[.//div[contains(@class, 'MuiStack-root') and contains(@class, 'css-j7qwjs')]//span[contains(text(), 'Failed')]]"));
		
			    // Initialize Excel workbook
			    Workbook workbook = new XSSFWorkbook();
			    Sheet failedDetailsSheet = workbook.createSheet("Failed Client Details");
		
			    // Define headers for the Excel sheet
			    String[] headers = {
			        "Client Name", "Program", "Payment Type", "Invoice ID", "Billing Date", 
			        "Total Invoice", "Surcharge", "Discount", "Credits", "Total Due", "Status"
			    };
			    
			    // Create a row for headers
			    Row headerRow = failedDetailsSheet.createRow(0);
			    for (int i = 0; i < headers.length; i++) {
			        headerRow.createCell(i).setCellValue(headers[i]);
			    }
		
			    // Initialize variables for totals
			    double totalInvoice = 0.0;
			    double totalSurcharge = 0.0;
			    double totalDiscount = 0.0;
			    double totalCredits = 0.0;
			    double totalFailed= 0.0;
		
			    // Initialize row index for writing client details
			    int rowIndex = 1;
		
			    // Loop through each row with "failed" status
			    for (WebElement row : failedRows) {
			        List<WebElement> cells = row.findElements(By.tagName("td"));
			        
			        // Create a new row for each "failed" client
			        Row excelRow = failedDetailsSheet.createRow(rowIndex++);
			        
			        // Extract values from the relevant columns and write to Excel
			        String invoiceText = cells.get(5).getText().replace("$", "").replace(",", "").trim();
			        String surchargeText = cells.get(6).getText().replace("$", "").replace(",", "").trim();
			        String discountText = cells.get(7).getText().replace("$", "").replace(",", "").trim();
			        String creditsText = cells.get(8).getText().replace("$", "").replace(",", "").trim();
			        String FailedText = cells.get(9).getText().replace("$", "").replace(",", "").trim();
			        
			        // Parse the values to doubles
			        double invoice = invoiceText.isEmpty() ? 0.0 : Double.parseDouble(invoiceText);
			        double surcharge = surchargeText.isEmpty() ? 0.0 : Double.parseDouble(surchargeText);
			        double discount = discountText.isEmpty() ? 0.0 : Double.parseDouble(discountText);
			        double credits = creditsText.isEmpty() ? 0.0 : Double.parseDouble(creditsText);
			        double failed = FailedText.isEmpty() ? 0.0 : Double.parseDouble(FailedText);
		
			        // Write data to the row
			        excelRow.createCell(0).setCellValue(cells.get(0).getText()); // Client Name
			        excelRow.createCell(1).setCellValue(cells.get(1).getText()); // Program
			        excelRow.createCell(2).setCellValue(cells.get(2).getText()); // Payment Type
			        excelRow.createCell(3).setCellValue(cells.get(3).getText()); // Invoice ID
			        excelRow.createCell(4).setCellValue(cells.get(4).getText()); // Billing Date
			        excelRow.createCell(5).setCellValue(invoice); // Total Invoice
			        excelRow.createCell(6).setCellValue(surcharge); // Surcharge
			        excelRow.createCell(7).setCellValue(discount); // Discount
			        excelRow.createCell(8).setCellValue(credits); // Credits
			        excelRow.createCell(9).setCellValue(failed); // Total failed
			        excelRow.createCell(10).setCellValue("Failed"); // Status (you already know it is "Paid")
		
			        // Update totals for each column
			        totalInvoice += invoice;
			        totalSurcharge += surcharge;
			        totalDiscount += discount;
			        totalCredits += credits;
			        totalFailed += failed;
			    }
		
			    // Add totals row at the end of the sheet
			    Row totalRow = failedDetailsSheet.createRow(rowIndex);
			    totalRow.createCell(0).setCellValue("Total");
			    totalRow.createCell(5).setCellValue(totalInvoice); // Total Invoice
			    totalRow.createCell(6).setCellValue(totalSurcharge); // Total Surcharge
			    totalRow.createCell(7).setCellValue(totalDiscount); // Total Discount
			    totalRow.createCell(8).setCellValue(totalCredits); // Total Credits
			    totalRow.createCell(9).setCellValue(totalFailed); // Total Paid
		
			    // Save the Excel file to a specific location
			    try (FileOutputStream fileOut = new FileOutputStream("Billing_reports/FailedClientDetailsWithTotals.xlsx")) {
			        workbook.write(fileOut);
			        System.out.println("Excel file created: FailedClientDetailsWithTotals.xlsx");
			    } finally {
			        workbook.close();
			    }
			}

	@Test
	private void ScheduledClientRowsToExcel() throws InterruptedException, IOException {
		    Thread.sleep(10000); // Wait for the page to load
	
		    // Locate all rows with "failed" status
		    List<WebElement> scheduledRows = driver.findElements(By.xpath("//tr[.//div[contains(@style, 'background: rgb(130, 97, 244)') and text()='Scheduled']]"));

		    // Initialize Excel workbook
		    Workbook workbook = new XSSFWorkbook();
		    Sheet ScheduledDetailsSheet = workbook.createSheet("Scheduled Client Details");
	
		    // Define headers for the Excel sheet
		    String[] headers = {
		        "Client Name", "Program", "Payment Type", "Invoice ID", "Billing Date", 
		        "Total Invoice", "Surcharge", "Discount", "Credits", "Total Due", "Status"
		    };
		    
		    // Create a row for headers
		    Row headerRow = ScheduledDetailsSheet.createRow(0);
		    for (int i = 0; i < headers.length; i++) {
		        headerRow.createCell(i).setCellValue(headers[i]);
		    }
	
		    // Initialize variables for totals
		    double totalInvoice = 0.0;
		    double totalSurcharge = 0.0;
		    double totalDiscount = 0.0;
		    double totalCredits = 0.0;
		    double totalDue =  0.0;
	
		    // Initialize row index for writing client details
		    int rowIndex = 1;
	
		    // Loop through each row with "failed" status
		    for (WebElement row : scheduledRows) {
		        List<WebElement> cells = row.findElements(By.tagName("td"));
		        
		        // Create a new row for each "failed" client
		        Row excelRow = ScheduledDetailsSheet.createRow(rowIndex++);
		        
		        // Extract values from the relevant columns and write to Excel
		        String invoiceText = cells.get(5).getText().replace("$", "").replace(",", "").trim();
		        String surchargeText = cells.get(6).getText().replace("$", "").replace(",", "").trim();
		        String discountText = cells.get(7).getText().replace("$", "").replace(",", "").trim();
		        String creditsText = cells.get(8).getText().replace("$", "").replace(",", "").trim();
		        String DuedText = cells.get(9).getText().replace("$", "").replace(",", "").trim();
		        
		        // Parse the values to doubles
		        double invoice = invoiceText.isEmpty() ? 0.0 : Double.parseDouble(invoiceText);
		        double surcharge = surchargeText.isEmpty() ? 0.0 : Double.parseDouble(surchargeText);
		        double discount = discountText.isEmpty() ? 0.0 : Double.parseDouble(discountText);
		        double credits = creditsText.isEmpty() ? 0.0 : Double.parseDouble(creditsText);
		        double Due = DuedText.isEmpty() ? 0.0 : Double.parseDouble(DuedText);
	
		        // Write data to the row
		        excelRow.createCell(0).setCellValue(cells.get(0).getText()); // Client Name
		        excelRow.createCell(1).setCellValue(cells.get(1).getText()); // Program
		        excelRow.createCell(2).setCellValue(cells.get(2).getText()); // Payment Type
		        excelRow.createCell(3).setCellValue(cells.get(3).getText()); // Invoice ID
		        excelRow.createCell(4).setCellValue(cells.get(4).getText()); // Billing Date
		        excelRow.createCell(5).setCellValue(invoice); // Total Invoice
		        excelRow.createCell(6).setCellValue(surcharge); // Surcharge
		        excelRow.createCell(7).setCellValue(discount); // Discount
		        excelRow.createCell(8).setCellValue(credits); // Credits
		        excelRow.createCell(9).setCellValue(Due); // Total due
		        excelRow.createCell(10).setCellValue("Scheduled"); // Status (you already know it is "Paid")
	
		        // Update totals for each column
		        totalInvoice += invoice;
		        totalSurcharge += surcharge;
		        totalDiscount += discount;
		        totalCredits += credits;
		        totalDue += Due;
		    }
	
		    // Add totals row at the end of the sheet
		    Row totalRow = ScheduledDetailsSheet.createRow(rowIndex);
		    totalRow.createCell(0).setCellValue("Total");
		    totalRow.createCell(5).setCellValue(totalInvoice); // Total Invoice
		    totalRow.createCell(6).setCellValue(totalSurcharge); // Total Surcharge
		    totalRow.createCell(7).setCellValue(totalDiscount); // Total Discount
		    totalRow.createCell(8).setCellValue(totalCredits); // Total Credits
		    totalRow.createCell(9).setCellValue(totalDue); // Total due
	
		    // Save the Excel file to a specific location
		    try (FileOutputStream fileOut = new FileOutputStream("Billing_reports/ScheduledClientDetailsWithTotals.xlsx")) {
		        workbook.write(fileOut);
		        System.out.println("Excel file created: ScheduledClientDetailsWithTotals.xlsx");
		    } finally {
		        workbook.close();
		    }
		}



	@AfterMethod
	private void close_browae() {
				driver.close();  

	}  

}
