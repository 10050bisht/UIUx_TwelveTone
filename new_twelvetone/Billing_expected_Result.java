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

public class Billing_expected_Result {

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

	
//	@Test
//	private void saveALLClientRowsToExcel() throws InterruptedException, IOException {
//		Thread.sleep(5000); // Wait for the page to load
//
//		// Locate rows with "Failed" status
//		List<WebElement> AllClientRows = driver.findElements(By.xpath("//tr[contains(@class, 'MuiTableRow-root css-2ygh3t')]"));
//
//		// Initialize an Excel workbook and sheet
//		Workbook workbook = new XSSFWorkbook();
//		Sheet sheet = workbook.createSheet("Billing Report");
//
//		// Add headers to the Excel sheet
//		String[] headers = {
//				"Client Name", "Program", "Payment Type", "Invoice ID", "Billing Date",
//				"Total Invoice", "Surcharge", "Discount", "Credits",
//				"Total Due", "Total Paid", "Status"
//		};
//		Row headerRow = sheet.createRow(0);
//		for (int i = 0; i < headers.length; i++) {
//			headerRow.createCell(i).setCellValue(headers[i]);
//		}
//
//		// Write the row data to the Excel sheet
//		int rowIndex = 1; // Start from the second row for data
//		for (WebElement row : AllClientRows) {
//			List<WebElement> cells = row.findElements(By.tagName("td"));
//			Row excelRow = sheet.createRow(rowIndex++);
//
//			for (int i = 0; i < cells.size(); i++) {
//				excelRow.createCell(i).setCellValue(cells.get(i).getText());
//			}
//		}
//		// Save the Excel file
//		try (FileOutputStream fileOut = new FileOutputStream("Billing_reports/BillingReport.xlsx")) {
//			workbook.write(fileOut);
//			System.out.println("Excel file created: BillingReport.xlsx");
//		
//		}
//		finally {
//				workbook.close();
//		}
//
//	}
//	
//	
//	
//	@Test
//	public void saveInvoiceAndSurchargeToExcel() throws InterruptedException {
//	    Thread.sleep(5000); // Wait for the page to load
//
//	    // Locate all "Total Invoice" cells
//	    List<WebElement> totalInvoiceCells = driver.findElements(By.xpath("//tr/td[6]"));
//
//	    // Locate all "Surcharge" cells
//	    List<WebElement> surchargeCells = driver.findElements(By.xpath("//tr/td[7]"));
//	    
//	    // Locate all "Discount" cells
//	    List<WebElement> DiscountCells = driver.findElements(By.xpath("//tr/td[8]"));
//	    
//	 // Locate all "Credits" cells
//	    List<WebElement> CreditsCells = driver.findElements(By.xpath("//tr/td[9]"));
//	    
//	    // Locate all "Total Due" cells
//	    List<WebElement> TotalDueCells = driver.findElements(By.xpath("//tr/td[10]"));
//	    
//	    // Locate all "Total Paid" cells
//	    List<WebElement> TotalPaidCells = driver.findElements(By.xpath("//tr/td[11]"));
//
//	    // Initialize variables for summation
//	    double totalInvoiceAmount = 0.0;
//	    double totalSurchargeAmount = 0.0;
//	    double totalDiscountAmount = 0.0;
//	    double totalCreditsAmount = 0.0;
//	    double totalDueAmount = 0.0;
//	    double totalPaidAmount = 0.0;
//	    
//
////	    System.out.println("Invoice and Surcharge Values:");
//
//	    // Prepare workbook and sheet
//	    Workbook workbook = new XSSFWorkbook();
//	    Sheet sheet = workbook.createSheet("ExpectedAmount");
//
//	    // Create a header row
//	    Row headerRow = sheet.createRow(0);
//	    headerRow.createCell(0).setCellValue("Invoice");
//	    headerRow.createCell(1).setCellValue("Surcharge");
//	    headerRow.createCell(2).setCellValue("Discount");
//	    headerRow.createCell(3).setCellValue("creditse");
//	    headerRow.createCell(4).setCellValue("Total Due");
//	    headerRow.createCell(5).setCellValue("Total Paid");
//
//	    // Iterate through both columns and calculate totals
//	    int rowIndex = 1; // Row index for Excel
//	    for (int i = 0; i < totalInvoiceCells.size(); i++) {
//	        String invoiceText = totalInvoiceCells.get(i).getText().replace("$", "").replace(",", "").trim();
//	        String surchargeText = surchargeCells.get(i).getText().replace("$", "").replace(",", "").trim();
//	        String discountText = DiscountCells.get(i).getText().replace("$", "").replace(",", "").trim();
//	        String creditsText = CreditsCells.get(i).getText().replace("$", "").replace(",", "").trim();
//	        String dueText = TotalDueCells.get(i).getText().replace("$", "").replace(",", "").trim();
//	        String paidText = TotalPaidCells.get(i).getText().replace("$", "").replace(",", "").trim();
//
//	        // Parse numeric values
//	        double invoiceValue = 0.0;
//	        double surchargeValue = 0.0;
//	        double discounteValue = 0.0;
//	        double creditsValue = 0.0; 
//	        double dueValue = 0.0;
//	        double paidValue = 0.0;
//	        
////	        try {
//	            invoiceValue = Double.parseDouble(invoiceText);
//	            totalInvoiceAmount += invoiceValue;
////	        } catch (NumberFormatException e) {
////	            System.out.println("Non-numeric Invoice value skipped: " + invoiceText);
////	        }
//
////	        try {
//	            surchargeValue = Double.parseDouble(surchargeText);
//	            totalSurchargeAmount += surchargeValue;
////	        } catch (NumberFormatException e) {
////	            System.out.println("Non-numeric Surcharge value skipped: " + surchargeText);
////	        }
//	        
////	        try {
//	        	discounteValue = Double.parseDouble(discountText);
//	            totalDiscountAmount += discounteValue;
////	        } catch (NumberFormatException e) {
////	            System.out.println("Non-numeric Dscount value skipped: " + discountText);
////	        }
//	        
////	        try {
//	            creditsValue = Double.parseDouble(creditsText);
//	            totalCreditsAmount += creditsValue;
////	        } catch (NumberFormatException e) {
////	            System.out.println("Non-numeric Credit value skipped: " + creditsText);
////	        }
//	        
////	        try {
//	            dueValue = Double.parseDouble(dueText);
//	            totalDueAmount += dueValue;
////	        } catch (NumberFormatException e) {
////	            System.out.println("Non-numeric Due value skipped: " + dueText);
////	        }
//	        
////	        try {
//	        	paidValue = Double.parseDouble(invoiceText);
//	        	totalPaidAmount += paidValue;
////	        } catch (NumberFormatException e) {
////	            System.out.println("Non-numeric Paid value skipped: " + paidText);
////	        }
//	        
//	        
//	        
//
//	        // Write values to Excel
//	        Row row = sheet.createRow(rowIndex++);
//	        row.createCell(0).setCellValue(invoiceText);
//	        row.createCell(1).setCellValue(surchargeText);
//	        row.createCell(2).setCellValue(discountText);
//	        row.createCell(3).setCellValue(creditsText);
//	        row.createCell(4).setCellValue(dueText);
//	        row.createCell(5).setCellValue(paidText);
//
//	        // Print values to console
////	        System.out.println("Invoice: " + invoiceText + ", Surcharge: " + surchargeText);
//	    }
//
//	    // Write total amounts at the end
//	    Row totalRow = sheet.createRow(rowIndex);
////	    totalRow.createCell(0).setCellValue("Total:");
//	    totalRow.createCell(0).setCellValue("Total Invocie: " + totalInvoiceAmount);
//	    totalRow.createCell(1).setCellValue("Total Surchage: " + totalSurchargeAmount);
//	    totalRow.createCell(2).setCellValue("Total Discount: " + totalDiscountAmount);
//	    totalRow.createCell(3).setCellValue("Total Credits: " + totalCreditsAmount);
//	    totalRow.createCell(4).setCellValue("Total Due: " + totalDueAmount);
//	    totalRow.createCell(5).setCellValue("Total Paid:  " + totalPaidAmount);
//
////	    System.out.println("Total Invoice Amount: $" + totalInvoiceAmount);
////	    System.out.println("Total Surcharge Amount: $" + totalSurchargeAmount);
//
//	    // Save the Excel file
//	    try (FileOutputStream fileOut = new FileOutputStream("Billing_reports/ExpectedAmount.xlsx")) {
//	        workbook.write(fileOut);
//	        System.out.println("Excel file created: InvoiceAndSurcharge.xlsx");
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    } finally {
//	        try {
//	            workbook.close();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	}



	@Test
	public void saveAllClientDetailsToExcel() throws InterruptedException, IOException {
	    Thread.sleep(5000); // Wait for the page to load

	    // Locate all client rows
	    List<WebElement> allClientRows = driver.findElements(By.xpath("//tr[contains(@class, 'MuiTableRow-root css-2ygh3t')]"));

	    // Locate all columns for totals
	    List<WebElement> totalInvoiceCells = driver.findElements(By.xpath("//tr/td[6]"));
	    List<WebElement> surchargeCells = driver.findElements(By.xpath("//tr/td[7]"));
	    List<WebElement> discountCells = driver.findElements(By.xpath("//tr/td[8]"));
	    List<WebElement> creditsCells = driver.findElements(By.xpath("//tr/td[9]"));
	    List<WebElement> totalDueCells = driver.findElements(By.xpath("//tr/td[10]"));
	    List<WebElement> totalPaidCells = driver.findElements(By.xpath("//tr/td[11]"));

	    // Initialize variables for summation
	    double totalInvoiceAmount = 0.0;
	    double totalSurchargeAmount = 0.0;
	    double totalDiscountAmount = 0.0;
	    double totalCreditsAmount = 0.0;
	    double totalDueAmount = 0.0;
	    double totalPaidAmount = 0.0;

	    // Initialize Excel workbook
	    Workbook workbook = new XSSFWorkbook();

	    // Sheet for all client details
	    Sheet clientDetailsSheet = workbook.createSheet("Client Details");
	    String[] clientHeaders = {
	        "Client Name", "Program", "Payment Type", "Invoice ID", "Billing Date",
	        "Total Invoice", "Surcharge", "Discount", "Credits",
	        "Total Due", "Total Paid", "Status"
	    };
	    Row clientHeaderRow = clientDetailsSheet.createRow(0);
	    for (int i = 0; i < clientHeaders.length; i++) {
	        clientHeaderRow.createCell(i).setCellValue(clientHeaders[i]);
	    }

	    // Write client details to sheet
	    int clientRowIndex = 1;
	    for (WebElement row : allClientRows) {
	        List<WebElement> cells = row.findElements(By.tagName("td"));
	        Row excelRow = clientDetailsSheet.createRow(clientRowIndex++);

	        for (int i = 0; i < cells.size(); i++) {
	            excelRow.createCell(i).setCellValue(cells.get(i).getText());
	        }
	    }

	    // Sheet for totals
	    Sheet totalsSheet = workbook.createSheet("Totals");
	    String[] totalHeaders = {"Invoice", "Surcharge", "Discount", "Credits", "Total Due", "Total Paid"};
	    Row totalsHeaderRow = totalsSheet.createRow(0);
	    for (int i = 0; i < totalHeaders.length; i++) {
	        totalsHeaderRow.createCell(i).setCellValue(totalHeaders[i]);
	    }

	    // Write totals to sheet
	    int totalsRowIndex = 1;
	    for (int i = 0; i < totalInvoiceCells.size(); i++) {
	        String invoiceText = totalInvoiceCells.get(i).getText().replace("$", "").replace(",", "").trim();
	        String surchargeText = surchargeCells.get(i).getText().replace("$", "").replace(",", "").trim();
	        String discountText = discountCells.get(i).getText().replace("$", "").replace(",", "").trim();
	        String creditsText = creditsCells.get(i).getText().replace("$", "").replace(",", "").trim();
	        String dueText = totalDueCells.get(i).getText().replace("$", "").replace(",", "").trim();
	        String paidText = totalPaidCells.get(i).getText().replace("$", "").replace(",", "").trim();

	        // Parse numeric values
	        double invoiceValue = invoiceText.isEmpty() ? 0.0 : Double.parseDouble(invoiceText);
	        double surchargeValue = surchargeText.isEmpty() ? 0.0 : Double.parseDouble(surchargeText);
	        double discountValue = discountText.isEmpty() ? 0.0 : Double.parseDouble(discountText);
	        double creditsValue = creditsText.isEmpty() ? 0.0 : Double.parseDouble(creditsText);
	        double dueValue = dueText.isEmpty() ? 0.0 : Double.parseDouble(dueText);
	        double paidValue = paidText.isEmpty() ? 0.0 : Double.parseDouble(paidText);

	        // Update totals
	        totalInvoiceAmount += invoiceValue;
	        totalSurchargeAmount += surchargeValue;
	        totalDiscountAmount += discountValue;
	        totalCreditsAmount += creditsValue;
	        totalDueAmount += dueValue;
	        totalPaidAmount += paidValue;

	        // Write values to sheet
	        Row row = totalsSheet.createRow(totalsRowIndex++);
	        row.createCell(0).setCellValue(invoiceValue);
	        row.createCell(1).setCellValue(surchargeValue);
	        row.createCell(2).setCellValue(discountValue);
	        row.createCell(3).setCellValue(creditsValue);
	        row.createCell(4).setCellValue(dueValue);
	        row.createCell(5).setCellValue(paidValue);
	    }

	    // Write total sums to the end of totals sheet
	    Row totalRow = totalsSheet.createRow(totalsRowIndex);
	    totalRow.createCell(0).setCellValue("Total Invoice: " + totalInvoiceAmount);
	    totalRow.createCell(1).setCellValue("Total Surcharge: " + totalSurchargeAmount);
	    totalRow.createCell(2).setCellValue("Total Discount: " + totalDiscountAmount);
	    totalRow.createCell(3).setCellValue("Total Credits: " + totalCreditsAmount);
	    totalRow.createCell(4).setCellValue("Total Due: " + totalDueAmount);
	    totalRow.createCell(5).setCellValue("Total Paid: " + totalPaidAmount);

	    // Save the Excel file
	    try (FileOutputStream fileOut = new FileOutputStream("Billing_reports/CombinedBillingReport.xlsx")) {
	        workbook.write(fileOut);
	        System.out.println("Excel file created: CombinedBillingReport.xlsx");
	    } finally {
	        workbook.close();
	    }
	}
	
	
	@AfterMethod
	private void close_browae() {
		//		driver.close();  

	}  

}

