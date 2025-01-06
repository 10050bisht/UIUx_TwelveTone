package Twelve_tone;

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

public class ExportCode_To_Generate_BillingReport {       // Reuse the code here 


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
	private void exportClientDetailsToExcel(String xpath, String sheetName, String outputFileName, String statusColumnValue) throws InterruptedException, IOException {
	    Thread.sleep(5000); // Wait for the page to load

	    // Locate all rows based on the provided XPath
	    List<WebElement> clientRows = driver.findElements(By.xpath(xpath));

	    // Initialize Excel workbook
	    Workbook workbook = new XSSFWorkbook();
	    Sheet clientDetailsSheet = workbook.createSheet(sheetName);

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
	    double totalDue = 0.0;
	    double totalPaid = 0.0;

	    // Initialize row index for writing client details
	    int rowIndex = 1;

	    // Loop through each row and extract data
	    for (WebElement row : clientRows) {
	        List<WebElement> cells = row.findElements(By.tagName("td"));

	        // Create a new row in the Excel sheet
	        Row excelRow = clientDetailsSheet.createRow(rowIndex++);

	        // Extract and parse values
	        String invoiceText = cells.get(5).getText().replace("$", "").replace(",", "").trim();
	        String surchargeText = cells.get(6).getText().replace("$", "").replace(",", "").trim();
	        String discountText = cells.get(7).getText().replace("$", "").replace(",", "").trim();
	        String creditsText = cells.get(8).getText().replace("$", "").replace(",", "").trim();
	        String dueText = cells.get(9).getText().replace("$", "").replace(",", "").trim();
	        String paidText = cells.get(10).getText().replace("$", "").replace(",", "").trim();

	        double invoice = invoiceText.isEmpty() ? 0.0 : Double.parseDouble(invoiceText);
	        double surcharge = surchargeText.isEmpty() ? 0.0 : Double.parseDouble(surchargeText);
	        double discount = discountText.isEmpty() ? 0.0 : Double.parseDouble(discountText);
	        double credits = creditsText.isEmpty() ? 0.0 : Double.parseDouble(creditsText);
	        double due = dueText.isEmpty() ? 0.0 : Double.parseDouble(dueText);
	        double paid = paidText.isEmpty() ? 0.0 : Double.parseDouble(paidText);

	        // Write data to the Excel row
	        excelRow.createCell(0).setCellValue(cells.get(0).getText()); // Client Name
	        excelRow.createCell(1).setCellValue(cells.get(1).getText()); // Program
	        excelRow.createCell(2).setCellValue(cells.get(2).getText()); // Payment Type
	        excelRow.createCell(3).setCellValue(cells.get(3).getText()); // Invoice ID
	        excelRow.createCell(4).setCellValue(cells.get(4).getText()); // Billing Date
	        excelRow.createCell(5).setCellValue(invoice); // Total Invoice
	        excelRow.createCell(6).setCellValue(surcharge); // Surcharge
	        excelRow.createCell(7).setCellValue(discount); // Discount
	        excelRow.createCell(8).setCellValue(credits); // Credits
	        excelRow.createCell(9).setCellValue(due); // Total Due
	        excelRow.createCell(10).setCellValue(paid); // Total Paid
	        excelRow.createCell(11).setCellValue(statusColumnValue); // Status

	        // Update totals
	        totalInvoice += invoice;
	        totalSurcharge += surcharge;
	        totalDiscount += discount;
	        totalCredits += credits;
	        totalDue += due;
	        totalPaid += paid;
	    }

	    // Add totals row
	    Row totalRow = clientDetailsSheet.createRow(rowIndex);
	    totalRow.createCell(0).setCellValue("Total");
	    totalRow.createCell(5).setCellValue(totalInvoice);
	    totalRow.createCell(6).setCellValue(totalSurcharge);
	    totalRow.createCell(7).setCellValue(totalDiscount);
	    totalRow.createCell(8).setCellValue(totalCredits);
	    totalRow.createCell(9).setCellValue(totalDue);
	    totalRow.createCell(10).setCellValue(totalPaid);

	    // Save the Excel file
	    try (FileOutputStream fileOut = new FileOutputStream(outputFileName)) {
	        workbook.write(fileOut);
	        System.out.println("Excel file created: " + outputFileName);
	    } finally {
	        workbook.close();
	    }
	}



@Test
private void ALLClientRowsToExcel() throws InterruptedException, IOException {
    exportClientDetailsToExcel(
        "//tr[contains(@class, 'MuiTableRow-root css-2ygh3t')]", 
        "All Client Details", 
        "Billing_reports/BillingReport.xlsx", 
        "all"
    );
}


@Test
private void paid_amounts() throws InterruptedException, IOException {
    exportClientDetailsToExcel(
        "//tr[.//div[contains(@class, 'MuiStack-root') and contains(@class, 'css-j7qwjs')]//span[contains(text(), 'Paid')]]", 
        "Paid Client Details", 
        "Billing_reports/Paid_BillingReport.xlsx", 
        "Paid"
    );
}

@Test
private void Failed_amounts() throws InterruptedException, IOException {
    exportClientDetailsToExcel(
    		"//tr[.//div[contains(@style, 'background: rgb(231, 102, 102)') and text()='Failed']]", 
        "Failed Client Details", 
        "Billing_reports/Failed_BillingReport.xlsx", 
        "Failed"
    );
}


@Test
private void Scheuled_Clients() throws InterruptedException, IOException {
    exportClientDetailsToExcel(
    		"//tr[.//div[contains(@style, 'background: rgb(130, 97, 244)') and text()='Scheduled']]", 
        "Scheduled Client Details", 
        "Billing_reports/Scheduled_BillingReport.xlsx", 
        "Scheduled"
      
    );
}




	@AfterMethod
	private void close_browae() {
//				driver.close();  

	}  

}

