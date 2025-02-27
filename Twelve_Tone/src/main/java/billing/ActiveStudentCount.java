package billing;



import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ActiveStudentCount {
// Feb Month Active Student count from Lesson Schedule page, to compare the Billing Reports Active Students


	 public static void main(String[] args) throws InterruptedException {
	        WebDriverManager.chromedriver().setup();
	        WebDriver driver = new ChromeDriver();
	        driver.get("https://stage.schedulehub.io/");

	        driver.manage().window().maximize();

	        driver.findElement(By.name("email")).sendKeys("test@gmail.com");
	        driver.findElement(By.name("password")).sendKeys("123456");
	        driver.findElement(By.className("_submitBtn_8rox6_99")).click();

	        Thread.sleep(4000);

	        WebElement Schedule_menu = driver.findElement(By.partialLinkText("Schedule"));
	        Schedule_menu.click();

	        WebElement lessonSchedule = driver.findElement(By.xpath("//span[text()='Lesson Schedule']"));
	        lessonSchedule.click();

	

	        int totalSum = 0;
	        int maxDays = 30;
	        int totalWeeks = 4; // Number of weeks
	        int weekCount = 0; // To count actual weeks with students

	        // Iterate through weeks
	        for (int week = 0; week < totalWeeks; week++) {
	            int weeklySum = 0; // Reset weekly sum at the start of each week

	            // Set to store unique students per week
	            Set<String> weeklyUniqueStudents = new HashSet<>();

	            // Iterate through days of the week
	            for (int day = 1 + (week * 7); day <= Math.min((week + 1) * 7, maxDays); day++) {

	                // Open calendar and select the specific day
	                WebElement calendar = driver.findElement(By.xpath("//button[contains(@class, 'MuiIconButton-root')]"));
	                calendar.click();
	                Thread.sleep(1000);
	                
	                WebElement date = driver.findElement(By.xpath("//button[contains(@class, 'MuiPickersDay-root') and text()='" + day + "']"));
	                date.click();
	                Thread.sleep(2000);

	                // Daily Class Summary Data Extraction
	                List<WebElement> studentElements = driver.findElements(By.xpath("//p[contains(text(), 'yrs')]"));

	                // Set to store unique students for the day
	                Set<String> dailyUniqueStudents = new HashSet<>();
	                
	                for (WebElement student : studentElements) {
	                    dailyUniqueStudents.add(student.getText().trim()); // Add unique student names
	                }

	                // Add unique students to weekly set
	                weeklyUniqueStudents.addAll(dailyUniqueStudents);

	                System.out.println("Unique Student Count on Day " + day + ": " + dailyUniqueStudents.size());
	            }

	            // Weekly sum is the number of unique students in the week
	            weeklySum = weeklyUniqueStudents.size();
	            
	            if (weeklySum > 0) { // Consider only weeks with students
	                weekCount++;
	            }

	            totalSum += weeklySum;

	            System.out.println("Total Unique Active Students in Week " + (week + 1) + ": " + weeklySum);
	        }

	        // Calculate weekly average
	        double averageWeeklyStudents = (weekCount > 0) ? (double) totalSum / weekCount : 0;

	        // Print total sum for the month
	        System.out.println("Total Unique Active Students in the Month: " + totalSum);
	        System.out.println("Average Unique Students Per Week: " + averageWeeklyStudents);
	               
	    }
	}
