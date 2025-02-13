package new_twelvetone;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
// import org.testng.annotations.AfterMethod;
// import org.testng.annotations.BeforeMethod;
// import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Create_Users {
	WebDriver driver;

	// @BeforeMethod
	private void setup() {
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

		// WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement settingIcon1 = driver.findElement(By.xpath("//img[@alt='setting-icon']"));
		settingIcon1.click();

	}

	// @Test
	private void blank_fields() {
		System.out.println("test case 1");

	}

	// @AfterMethod
	private void close_browae() {
		// driver.close();

	}

}
