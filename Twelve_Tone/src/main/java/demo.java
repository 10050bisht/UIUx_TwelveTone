import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class demo {

//	 public static void main(String[] args) throws InterruptedException, IOException {
//	        WebDriverManager.chromedriver().setup();
//	        WebDriver driver = new ChromeDriver();
//	        driver.get("https://dev.schedulehub.io/");
//
//	        driver.manage().window().maximize();
//
//	        driver.findElement(By.name("email")).sendKeys("test@gmail.com");
//	        driver.findElement(By.name("password")).sendKeys("123456");
//	        driver.findElement(By.className("_submitBtn_1e7ib_96")).click();
//
//	        Thread.sleep(4000);
//
//	        WebElement Schedule_menu = driver.findElement(By.partialLinkText("Schedule"));
//	        Schedule_menu.click();
//
//	        WebElement lessonSchedule = driver.findElement(By.xpath("//span[text()='Lesson Schedule']"));
//	        lessonSchedule.click();
//
//	        Thread.sleep(4000);
//
//	        Workbook workbook = new XSSFWorkbook();
//
//	        // Sheets for Excel output
//	        Sheet classSummarySheet = workbook.createSheet("Class Summary");
//	        Sheet dailySummarySheet = workbook.createSheet("Daily Student Summary");
//
//	        // Create headers for "Class Summary" sheet
//	        Row classHeader = classSummarySheet.createRow(0);
//	        classHeader.createCell(0).setCellValue("Class Type");
//	        classHeader.createCell(1).setCellValue("Total All Students");
//	        classHeader.createCell(2).setCellValue("Total Trial Students");
//	        classHeader.createCell(3).setCellValue("Total Makeup Students");
//	        classHeader.createCell(4).setCellValue("Non-Trial, Non-Makeup Students");
//
//	        // Create headers for "Daily Student Summary" sheet
//	        Row dailyHeader = dailySummarySheet.createRow(0);
//	        dailyHeader.createCell(0).setCellValue("Week");
//	        dailyHeader.createCell(1).setCellValue("Day");
//	        dailyHeader.createCell(2).setCellValue("Total Students");
//	        dailyHeader.createCell(3).setCellValue("Trial Students");
//	        dailyHeader.createCell(4).setCellValue("Makeup Students");
//	        dailyHeader.createCell(5).setCellValue("Actual Students");
//
//	        int dailyRowNum = 1;
//	        int classRowNum = 1;
//
//	        Map<String, int[]> classTypeSummaryMap = new HashMap<>();
//
//	        int maxDays = 30;
//	        int totalSum = 0;
//
//	        for (int week = 0; week < 5; week++) {
//	            int weeklySum = 0;
//
//	            for (int day = 1 + (week * 7); day <= Math.min((week + 1) * 7, maxDays); day++) {
//	                WebElement calendarIcon = driver.findElement(By.xpath("//button[contains(@class,  'MuiButtonBase-root MuiIconButton')]"));
//	                calendarIcon.click();
//	                Thread.sleep(2000);
//
//	                WebElement dateButton = driver.findElement(By.xpath("//button[@role='gridcell' and contains(@class, 'MuiPickersDay-root') and text()='" + day + "']"));
//	                dateButton.click();
//	                Thread.sleep(4000);
//
//	                List<WebElement> classElements = driver.findElements(By.xpath("//div[contains(@class, 'classItemHeader')]//p[contains(@class, 'classTitle')]"));
//	                int dailyTotal = 0;
//	                int dailyTrial = 0;
//	                int dailyMakeup = 0;
//
//	                for (WebElement classElement : classElements) {
//	                    String classText = classElement.getText();
//	                    String classType = classText.split(" - ")[0];
//	                    WebElement classContainer = classElement.findElement(By.xpath("./ancestor::div[contains(@class, '_card_')]"));
//
//	                    List<WebElement> trialStudentElements = classContainer.findElements(By.cssSelector("div.MuiStack-root._backgroundPurple_wbyfb_451"));
//	                    List<WebElement> allStudentElements = classContainer.findElements(By.xpath(".//div[contains(@class, '_hoverGrayBg_wbyfb_466')]/p[contains(@class, '_listItem_wbyfb_289') and contains(text(), 'yrs')]"));
//	                    List<WebElement> makeupStudentElements = classContainer.findElements(By.className("_backgroundMakeup_wbyfb_455"));
//
//	                    int trialCount = trialStudentElements.size();
//	                    int allCount = allStudentElements.size();
//	                    int makeupCount = makeupStudentElements.size();
//
//	                    dailyTotal += allCount;
//	                    dailyTrial += trialCount;
//	                    dailyMakeup += makeupCount;
//
//	                    classTypeSummaryMap.putIfAbsent(classType, new int[3]);
//	                    classTypeSummaryMap.get(classType)[0] += allCount;
//	                    classTypeSummaryMap.get(classType)[1] += trialCount;
//	                    classTypeSummaryMap.get(classType)[2] += makeupCount;
//	                }
//
//	                int actualCount = dailyTotal - dailyTrial - dailyMakeup;
//
//	                Row dailyRow = dailySummarySheet.createRow(dailyRowNum++);
//	                dailyRow.createCell(0).setCellValue("Week " + (week + 1));
//	                dailyRow.createCell(1).setCellValue("Day " + day);
//	                dailyRow.createCell(2).setCellValue(dailyTotal);
//	                dailyRow.createCell(3).setCellValue(dailyTrial);
//	                dailyRow.createCell(4).setCellValue(dailyMakeup);
//	                dailyRow.createCell(5).setCellValue(actualCount);
//
//	                weeklySum += actualCount;
//	            }
//
//	            totalSum += weeklySum;
//	        }
//
//	        for (Map.Entry<String, int[]> entry : classTypeSummaryMap.entrySet()) {
//	            String classType = entry.getKey();
//	            int[] counts = entry.getValue();
//
//	            int nonTrialNonMakeupCount = counts[0] - counts[1] - counts[2];
//
//	            Row classRow = classSummarySheet.createRow(classRowNum++);
//	            classRow.createCell(0).setCellValue(classType);
//	            classRow.createCell(1).setCellValue(counts[0]);
//	            classRow.createCell(2).setCellValue(counts[1]);
//	            classRow.createCell(3).setCellValue(counts[2]);
//	            classRow.createCell(4).setCellValue(nonTrialNonMakeupCount);
//	        }
//
//
//	        
//	     // Write the Excel file to disk
//	        try (FileOutputStream fileOut = new FileOutputStream(new File("Combined_Summary.xlsx"))) {
//	            workbook.write(fileOut);
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//
//	        System.out.println("Data written to Excel file successfully.");
//
//	        driver.quit();
//	        workbook.close();
//	    }
//	}

	 public static void main(String[] args) throws InterruptedException, IOException {
	        WebDriverManager.chromedriver().setup();
	        WebDriver driver = new ChromeDriver();
	        driver.get("https://dev.schedulehub.io/");

	        driver.manage().window().maximize();

	        driver.findElement(By.name("email")).sendKeys("test@gmail.com");
	        driver.findElement(By.name("password")).sendKeys("123456");
	        driver.findElement(By.className("_submitBtn_1e7ib_96")).click();

	        Thread.sleep(4000);

	        WebElement Schedule_menu = driver.findElement(By.partialLinkText("Schedule"));
	        Schedule_menu.click();

	        WebElement lessonSchedule = driver.findElement(By.xpath("//span[text()='Lesson Schedule']"));
	        lessonSchedule.click();

	        Thread.sleep(4000);

	        // Create Excel workbook and sheets
	        Workbook workbook = new XSSFWorkbook();
	        Sheet classSummarySheet = workbook.createSheet("Class Summary");
	        Sheet studentSummarySheet = workbook.createSheet("Student Summary");

	         // Add headers to Class Summary Sheet
	        Row classSummaryHeader = classSummarySheet.createRow(0);
	        classSummaryHeader.createCell(0).setCellValue("Date");
	        classSummaryHeader.createCell(1).setCellValue("Class Type");
	        classSummaryHeader.createCell(2).setCellValue("Total All Students");
	        classSummaryHeader.createCell(3).setCellValue("Total Trial Students");
	        classSummaryHeader.createCell(4).setCellValue("Total Makeup Students");
	        classSummaryHeader.createCell(5).setCellValue("Non-Trial, Non-Makeup Students");

	        int classSummaryRowNum = 1;

	        // Add headers to Student Summary Sheet
	        Row headerRow = studentSummarySheet.createRow(0);
	        headerRow.createCell(0).setCellValue("Week");
	        headerRow.createCell(1).setCellValue("Day");
	        headerRow.createCell(2).setCellValue("Total Students");
	        headerRow.createCell(3).setCellValue("Trial Students");
	        headerRow.createCell(4).setCellValue("Makeup Students");
	        headerRow.createCell(5).setCellValue("Actual Students");
	        int studentSummaryRowNum = 1;

	        int totalSum = 0;
	        int maxDays = 30;

	        // Iterate through weeks
	        for (int week = 0; week < 5; week++) {
	            int weeklySum = 0;

	            // Iterate through days of the week
	            for (int day = 1 + (week * 7); day <= Math.min((week + 1) * 7, maxDays); day++) {

	                 // Open calendar and select the specific day
	                WebElement calendar = driver.findElement(By.xpath("//button[contains(@class, 'MuiIconButton-root')]"));
	                 calendar.click();
	                 Thread.sleep(1000);
	                WebElement date = driver.findElement(By.xpath("//button[contains(@class, 'MuiPickersDay-root') and contains(text(), '" + day + "')]"));
	                 date.click();
	                 Thread.sleep(2000);
	               
	               // Daily Class Summary Data Extraction
	                List<WebElement> classElements = driver.findElements(By.xpath("//div[contains(@class, 'classItemHeader')]//p[contains(@class, 'classTitle')]"));

	               // Map to store daily class type summary
	                Map<String, int[]> dailyClassTypeSummaryMap = new HashMap<>();


	                for (WebElement classElement : classElements) {
	                    String classText = classElement.getText();
	                    String classType = classText.split(" - ")[0]; // Extract class type (e.g., GL60, PL60)
	                    WebElement classContainer = classElement.findElement(By.xpath("./ancestor::div[contains(@class, '_card_')]"));

	                    // Locate trial, non-trial, and makeup students within this class container
	                    List<WebElement> trialStudentElements = classContainer.findElements(By.cssSelector("div.MuiStack-root._backgroundPurple_wbyfb_451"));
	                    List<WebElement> allStudentElements = classContainer.findElements(By.xpath(".//div[contains(@class, '_hoverGrayBg_wbyfb_466')]/p[contains(@class, '_listItem_wbyfb_289') and contains(text(), 'yrs')]"));
	                    List<WebElement> makeupStudentElements = classContainer.findElements(By.className("_backgroundMakeup_wbyfb_455"));

	                    int trialCount = trialStudentElements.size();
	                    int allCount = allStudentElements.size();
	                    int makeupCount = makeupStudentElements.size();

	                    // Update counts for this class type in the map
	                    dailyClassTypeSummaryMap.putIfAbsent(classType, new int[3]); // Initialize the array if not already present
	                    dailyClassTypeSummaryMap.get(classType)[0] += allCount; // Index 0: Total students
	                    dailyClassTypeSummaryMap.get(classType)[1] += trialCount; // Index 1: Trial students
	                    dailyClassTypeSummaryMap.get(classType)[2] += makeupCount; // Index 2: Makeup students

	                }


	                 // Write daily class type summary to Class Summary sheet
	                for (Map.Entry<String, int[]> entry : dailyClassTypeSummaryMap.entrySet()) {
	                     String classType = entry.getKey();
	                     int[] counts = entry.getValue();
	                      int nonTrialNonMakeupCount = counts[0] - counts[1] - counts[2];
	                       // Add row to Class Summary Sheet
	                    Row classRow = classSummarySheet.createRow(classSummaryRowNum++);
	                     classRow.createCell(0).setCellValue("Day "+day);
	                    classRow.createCell(1).setCellValue(classType);
	                    classRow.createCell(2).setCellValue(counts[0]);
	                    classRow.createCell(3).setCellValue(counts[1]);
	                    classRow.createCell(4).setCellValue(counts[2]);
	                    classRow.createCell(5).setCellValue(nonTrialNonMakeupCount);
	                  }

	                // Student Summary Data Extraction
	                List<WebElement> classElementsStudent = driver.findElements(By.xpath("//p[contains(text(), 'GL60')]//ancestor::div[contains(@class, '_card_')]"));

	                int totalStudentCount = 0;
	                int trialStudentCount = 0;
	                int makeupStudentCount = 0;

	                for (WebElement classelemnt : classElementsStudent) {
	                    // Total Class Students
	                    List<WebElement> Student = classelemnt.findElements(By.xpath(".//p[contains(@class, '_listItem_wbyfb_289') and contains(text(), 'yrs')]"));
	                    for(WebElement student1: Student) {
	                         totalStudentCount += Student.size();
	                        System.out.println(" day : " +  day + " -- "+ student1.getText());
	                    
	                    // Trial Class Students
	                    List<WebElement> trialElements = classelemnt.findElements(By.xpath(".//div[contains(@class, '_backgroundPurple_wbyfb_451')]//p[contains(@class, '_listItem_wbyfb_289') and contains(text(), 'yrs')]"));
	                        trialStudentCount += trialElements.size();

	                    // Makeup Class Students
	                    List<WebElement> makeupElements = classelemnt.findElements(By.xpath(".//div[contains(@class, '_backgroundMakeup_wbyfb_455')]//p[contains(@class, '_listItem_wbyfb_289') and contains(text(), 'yrs')]"));
	                        makeupStudentCount += makeupElements.size();
	                }}

	                 int actualStudentCount = totalStudentCount - (trialStudentCount + makeupStudentCount);
	                   
	                // Add row to Student Summary Sheet for the current day
	                Row studentRow = studentSummarySheet.createRow(studentSummaryRowNum++);
	                studentRow.createCell(0).setCellValue("Week " + (week + 1));
	                studentRow.createCell(1).setCellValue("Day " + day);
	                studentRow.createCell(2).setCellValue(totalStudentCount);
	                studentRow.createCell(3).setCellValue(trialStudentCount);
	                studentRow.createCell(4).setCellValue(makeupStudentCount);
	                studentRow.createCell(5).setCellValue(actualStudentCount);

	                  // Add to weekly sum
	                 weeklySum += actualStudentCount;
	            }

	                // Add weekly total row
	            Row weeklyRow = studentSummarySheet.createRow(studentSummaryRowNum++);
	            weeklyRow.createCell(0).setCellValue("Week " + (week + 1) + " Total");
	            weeklyRow.createCell(5).setCellValue(weeklySum);

	            totalSum += weeklySum;
	        }

	               // Add monthly total row
	        Row monthlyRow = studentSummarySheet.createRow(studentSummaryRowNum++);
	        monthlyRow.createCell(0).setCellValue("Monthly Total");
	        monthlyRow.createCell(5).setCellValue(totalSum);



	        // Write the Excel file to disk
	        try (FileOutputStream fileOut = new FileOutputStream("CombinedClassSummary.xlsx")) {
	            workbook.write(fileOut);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        System.out.println("Data written to Excel file successfully.");
	        // Close the browser
	        driver.quit();
	        workbook.close();
	    }
	}