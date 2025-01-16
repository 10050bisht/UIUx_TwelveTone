package billing;


	import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.chrome.ChromeDriver;
	import io.github.bonigarcia.wdm.WebDriverManager;
	import org.testng.annotations.*;
	import org.apache.poi.ss.usermodel.*;
	import org.apache.poi.xssf.usermodel.XSSFWorkbook;

	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.util.List;

	public class SingleLessonCountTestNG {
	    WebDriver driver;
	    Workbook workbook;
	    Sheet sheet;
	    int rowNum = 0;

	    @BeforeMethod
	    public void setup() {
	        WebDriverManager.chromedriver().setup();
	        driver = new ChromeDriver();
	        driver.manage().window().maximize();
	        driver.get("https://stage.schedulehub.io/");
	    }

	    @Test
	    public void calculateSingleLessonCounts() throws InterruptedException, IOException {
	        // Login
	        driver.findElement(By.name("email")).sendKeys("test@gmail.com");
	        driver.findElement(By.name("password")).sendKeys("123456");
	        driver.findElement(By.className("_submitBtn_1e7ib_9600")).click();
	        Thread.sleep(4000);

	        // Navigate to Lesson Schedule
	        driver.findElement(By.partialLinkText("Schedule")).click();
	        driver.findElement(By.xpath("//span[text()='Lesson Schedule']")).click();
	        Thread.sleep(4000);

	        // Initialize Excel workbook and sheet
	        workbook = new XSSFWorkbook();
	        sheet = workbook.createSheet("Student Summary");

	        // Add header row
	        Row headerRow = sheet.createRow(rowNum++);
	        headerRow.createCell(0).setCellValue("Week");
	        headerRow.createCell(1).setCellValue("Day");
	        headerRow.createCell(2).setCellValue("Total Students");
	        headerRow.createCell(3).setCellValue("Trial Students");
	        headerRow.createCell(4).setCellValue("Makeup Students");
	        headerRow.createCell(5).setCellValue("Actual Students");

	        int totalSum = 0;
	        int maxDays = 31; // Total days to consider for calculation

	        for (int week = 0; week < 5; week++) {
	            int weeklySum = 0;

	            for (int day = 1 + (week * 7); day <= Math.min((week + 1) * 7, maxDays); day++) {
	                // Open calendar and select date
	                WebElement calendarIcon = driver.findElement(By.xpath("//button[contains(@class, 'MuiButtonBase-root MuiIconButton')]"));
	                calendarIcon.click();
	                Thread.sleep(2000);

	                WebElement dateButton = driver.findElement(By.xpath("//button[@role='gridcell' and contains(@class, 'MuiPickersDay-root') and text()='" + day + "']"));
	                dateButton.click();
	                Thread.sleep(4000);

	                List<WebElement> classElements = driver.findElements(By.xpath("//p[contains(text(), 'GL60')]//ancestor::div[contains(@class, '_card_')]"));
	                int totalStudentCount = 0;
	                int trialStudentCount = 0;
	                int makeupStudentCount = 0;

	                for (WebElement classElement : classElements) {
	                    // Total Class Students
	                    List<WebElement> students = classElement.findElements(By.xpath(".//p[contains(@class, '_listItem_wbyfb_289') and contains(text(), 'yrs')]"));
	                    totalStudentCount += students.size();

	                    // Trial Class Students
	                    List<WebElement> trialElements = classElement.findElements(By.xpath(".//div[contains(@class, '_backgroundPurple_wbyfb_451')]//p[contains(@class, '_listItem_wbyfb_289') and contains(text(), 'yrs')]"));
	                    trialStudentCount += trialElements.size();

	                    // Makeup Class Students
	                    List<WebElement> makeupElements = classElement.findElements(By.xpath(".//div[contains(@class, '_backgroundMakeup_wbyfb_455')]//p[contains(@class, '_listItem_wbyfb_289') and contains(text(), 'yrs')]"));
	                    makeupStudentCount += makeupElements.size();
	                }

	                int actualStudentCount = totalStudentCount - (trialStudentCount + makeupStudentCount);

	                // Add row to Excel for the current day
	                Row row = sheet.createRow(rowNum++);
	                row.createCell(0).setCellValue("Week " + (week + 1));
	                row.createCell(1).setCellValue("Day " + day);
	                row.createCell(2).setCellValue(totalStudentCount);
	                row.createCell(3).setCellValue(trialStudentCount);
	                row.createCell(4).setCellValue(makeupStudentCount);
	                row.createCell(5).setCellValue(actualStudentCount);

	                // Add to weekly sum
	                weeklySum += actualStudentCount;
	            }

	            // Add weekly total row
	            Row weeklyRow = sheet.createRow(rowNum++);
	            weeklyRow.createCell(0).setCellValue("Week " + (week + 1) + " Total");
	            weeklyRow.createCell(5).setCellValue(weeklySum);

	            totalSum += weeklySum;
	        }

	        // Add monthly total row
	        Row monthlyRow = sheet.createRow(rowNum++);
	        monthlyRow.createCell(0).setCellValue("Monthly Total");
	        monthlyRow.createCell(5).setCellValue(totalSum);

	        // Write the Excel file to disk
	        try (FileOutputStream fileOut = new FileOutputStream("GL60_Lesson_Count.xlsx")) {
	            workbook.write(fileOut);
	        }

	        System.out.println("Data written to Excel file successfully.");
	    }

	    @AfterMethod
	    public void teardown() throws IOException {
	        if (driver != null) {
	            driver.quit();
	        }
	        if (workbook != null) {
	            workbook.close();
	        }
	    }
	}

