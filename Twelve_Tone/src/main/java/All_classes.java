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

public class All_classes {

	public static void main(String[] args) throws InterruptedException, IOException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.get("https://dev.schedulehub.io/");

		driver.manage().window().maximize();

		driver.findElement(By.name("email")).sendKeys("test@gmail.com");
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.className("_submitBtn_yye06_97")).click();


		Thread.sleep(4000);

		WebElement Schedule_menu = driver.findElement(By.partialLinkText("Schedule"));
		Schedule_menu.click();

		WebElement lessonSchedule = driver.findElement(By.xpath("//span[text()='Lesson Schedule']"));
		lessonSchedule.click();

		Thread.sleep(4000);

        Thread.sleep(3000);

        // Map to store cumulative counts by class type
        Map<String, int[]> classTypeSummaryMap = new HashMap<>();

        // Iterate through the next 30 days
        for (int i = 1; i <= 30; i++) {
            // Open calendar and select the specific day
            WebElement calendar = driver.findElement(By.xpath("//button[contains(@class, 'MuiIconButton-root')]"));
            calendar.click();
            Thread.sleep(1000);

            WebElement date = driver.findElement(By.xpath("//button[contains(@class, 'MuiPickersDay-root') and contains(text(), '" + i + "')]"));
            date.click();
            Thread.sleep(2000);

            // Find all class elements for the day
            List<WebElement> classElements = driver.findElements(By.xpath("//div[contains(@class, 'classItemHeader')]//p[contains(@class, 'classTitle')]"));

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
                classTypeSummaryMap.putIfAbsent(classType, new int[3]); // Initialize the array if not already present
                classTypeSummaryMap.get(classType)[0] += allCount; // Index 0: Non-trial students
                classTypeSummaryMap.get(classType)[1] += trialCount;    // Index 1: Trial students
                classTypeSummaryMap.get(classType)[2] += makeupCount;   // Index 2: Makeup students
            }
        }

        // Create an Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Weekly Class Summary");

        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Class Type");
        header.createCell(1).setCellValue("Total All Students");
        header.createCell(2).setCellValue("Total Trial Students");
        header.createCell(3).setCellValue("Total Makeup Students");
        header.createCell(4).setCellValue("Non-Trial, Non-Makeup Students");

        // Iterate over class types and populate the Excel sheet
        int rowNum = 1; // Start after the header row
        for (Map.Entry<String, int[]> entry : classTypeSummaryMap.entrySet()) {
            String classType = entry.getKey();
            int[] counts = entry.getValue();

            // Subtract trial and makeup counts from total students
            int nonTrialNonMakeupCount = counts[0] - counts[1] - counts[2];

            // Create a new row for each class type
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(classType);
            row.createCell(1).setCellValue(counts[0]);
            row.createCell(2).setCellValue(counts[1]);
            row.createCell(3).setCellValue(counts[2]);
            row.createCell(4).setCellValue(nonTrialNonMakeupCount);
        }

        // Write the Excel file to disk
        try (FileOutputStream fileOut = new FileOutputStream(new File("DevClass_Summary.xlsx"))) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the browser after the task is done
        driver.quit();

        System.out.println("Data written to Excel file successfully.");
    }
}
