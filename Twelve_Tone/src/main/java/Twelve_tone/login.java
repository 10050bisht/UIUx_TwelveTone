package Twelve_tone;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import io.github.bonigarcia.wdm.WebDriverManager;

public class login { // Reuse the code and commented code is same code 

	 WebDriver driver;


	@BeforeMethod
	private void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://stage.schedulehub.io/");
		driver.manage().window().maximize();


	}
	
	
	
	    // Helper method to clear and optionally set input field values
	    private void setInputField(WebElement element, String value) {
	        element.clear();
	        if (value != null) {
	            element.sendKeys(value);
	        }
	    }

	    // Helper method to perform login actions
	    public void performLogin(String emailValue, String passwordValue) {
	        WebElement email = driver.findElement(By.name("email"));
	        setInputField(email, emailValue);

	        WebElement password = driver.findElement(By.name("password"));
	        setInputField(password, passwordValue);

	        WebElement submitBtn = driver.findElement(By.className("_submitBtn_1e7ib_96"));
	        submitBtn.click();
	    }

	    // Helper method to get toaster message text
	    private String getToasterMessage() {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Toastify__toast-body")));
	        return toastElement.getText().trim();
	    }

	    @Test(priority = 1) // check login with blank email and blank password
	    public void Login_with_Blanks_fields() {
	        performLogin("", ""); // Call helper method with blank values
	        String actualMessage = getToasterMessage();
	        String expectedMessage = "Email Required";
	        Assert.assertEquals(actualMessage, expectedMessage, "The toast message does not match the expected value.");
	    }

	    @Test(priority = 2) // check login with blank email and valid password
	    public void Blank_email_valid_Password() {
	        performLogin("", "123456"); // Call helper method with blank email and valid password
	        String actualMessage = getToasterMessage();
	        String expectedMessage = "Email Required";
	        Assert.assertEquals(actualMessage, expectedMessage, "The toast message does not match the expected value.");
	    }
	    @Test(priority = 3) // Check login with blank email and invalid password
	    public void Blank_email_Invalid_Password() {
	        performLogin("", "654321");
	        String actualMessage = getToasterMessage();
	        String expectedMessage = "Email Required";
	        Assert.assertEquals(actualMessage, expectedMessage, "The toast message does not match the expected value.");
	    }

	    @Test(priority = 4) // Check login with valid email and blank password
	    public void Valid_email_Blank_Password() {
	        performLogin("test@gmail.com", "");
	        String actualMessage = getToasterMessage();
	        String expectedMessage = "Password Required";
	        Assert.assertEquals(actualMessage, expectedMessage, "The toast message does not match the expected value.");
	    }

	    @Test(priority = 5) // Check login with invalid email and blank password
	    public void Invalid_email_Blank_Password() {
	        performLogin("test123@gmail.com", "");
	        String actualMessage = getToasterMessage();
	        String expectedMessage = "Password Required";
	        Assert.assertEquals(actualMessage, expectedMessage, "The toast message does not match the expected value.");
	    }

	    @Test(priority = 6) // Check login with invalid email and invalid password
	    public void Invalid_Email_and_password() {
	        performLogin("test123@gmail.com", "1234563");
	        String actualMessage = getToasterMessage();
	        String expectedMessage = "Invalid Email Address.";
	        Assert.assertEquals(actualMessage, expectedMessage, "The toast message does not match the expected value.");
	    }

	    @Test(priority = 7) // Check login with valid email and valid password
	    public void valid_login() {
	        performLogin("test@gmail.com", "123456");
	        System.out.println("Login successfully");
	        
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Login success')]")));
	        String actualMessage = toastElement.getText().trim();
	        String expectedMessage = "Login success";
	        Assert.assertEquals(actualMessage, expectedMessage, "The toast message does not match the expected value.");
	    }
	

	
	

	
//		@Test(priority = 1)   // check login with  blank  email and blank password
//		public void Login_with_Blanks_fields() throws InterruptedException {
//			WebElement email = driver.findElement(By.name("email"));
//			email.clear();
//	//		email.sendKeys("");
//	 
//			WebElement password = driver.findElement(By.name("password"));
//			password.clear();
//	//		password.sendKeys("");
//	
//			WebElement submitBtn = driver.findElement(By.className("_submitBtn_1e7ib_96"));
//			submitBtn.click();
//	
//			// Wait for the toaster message to appear
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Toastify__toast-body")));
//	
//			// Get the toaster message text
//			String actualMessage = toastElement.getText();
//	
//			// Define the expected message
//			String expectedMessage = "Email Required";
//	
//			// Verify the toaster message
//			Assert.assertEquals(actualMessage.trim(), expectedMessage, "The toast message does not match the expected value.");
//	//		Thread.sleep(6000);
//	
//		}
//	
//		@Test(priority = 2) // check login with blank  email and valid  password
//		public void Blank_email_valid_Password() throws InterruptedException {
//			WebElement email = driver.findElement(By.name("email"));
//			email.clear();
//	//		email.sendKeys("");
//	
//			WebElement password = driver.findElement(By.name("password"));
//			password.clear();
//			password.sendKeys("123456");
//	
//			WebElement submitBtn = driver.findElement(By.className("_submitBtn_1e7ib_96"));
//			submitBtn.click();
//	
//			// Wait for the toaster message to appear
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Toastify__toast-body")));
//	
//			// Get the toaster message text
//			String actualMessage = toastElement.getText();
//	
//			// Define the expected message
//			String expectedMessage = "Email Required";
//	
//			// Verify the toaster message
//			Assert.assertEquals(actualMessage.trim(), expectedMessage, "The toast message does not match the expected value.");
//	//		Thread.sleep(6000);
//	
//		}
//	
//		@Test(priority = 3)  // check login with blank  email and Invalid  password
//		public void Blank_email_Invalid_Password() throws InterruptedException {
//			WebElement email = driver.findElement(By.name("email"));
//			email.clear();
//	//		email.sendKeys("");
//	
//			WebElement password = driver.findElement(By.name("password"));
//			password.clear();
//			password.sendKeys("654321");
//	
//			WebElement submitBtn = driver.findElement(By.className("_submitBtn_1e7ib_96"));
//			submitBtn.click();
//	
//			// Wait for the toaster message to appear
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Toastify__toast-body")));
//	
//			// Get the toaster message text
//			String actualMessage = toastElement.getText();
//	
//			// Define the expected message
//			String expectedMessage = "Email Required";
//	
//			// Verify the toaster message
//			Assert.assertEquals(actualMessage.trim(), expectedMessage, "The toast message does not match the expected value.");
//	//		Thread.sleep(6000);
//	
//		}
//	
//		@Test(priority = 4) 
//		public void Valid_email_Blank_Password() throws InterruptedException {
//	
//			WebElement email = driver.findElement(By.name("email"));
//			email.clear();
//			email.sendKeys("test@gmail.com");
//	
//			WebElement password  = driver.findElement(By.name("password"));
//			password.clear();
//			password.sendKeys("");
//	
//			WebElement sumbit_btn  =  driver.findElement(By.className("_submitBtn_1e7ib_96"));
//			sumbit_btn.click();
//			//
//	
//			// Wait for the toaster message to appear
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Toastify__toast-body")));
//	
//			// Get the toaster message text
//			String actualMessage = toastElement.getText();
//	
//			// Define the expected message
//			String expectedMessage = "Password Required";
//	
//			// Verify the toaster message
//			Assert.assertEquals(actualMessage.trim(), expectedMessage, "The toast message does not match the expected value.");
//	
//	//		Thread.sleep(6000);
//		}
//	
//		@Test(priority = 5) 
//		public void Invalid_email_Blank_Password() throws InterruptedException {
//	
//			WebElement email = driver.findElement(By.name("email"));
//			email.clear();
//			email.sendKeys("test123@gmail.com");
//	
//			WebElement password  = driver.findElement(By.name("password"));
//			password.clear();
//	//		password.sendKeys("");
//	
//			WebElement sumbit_btn  =  driver.findElement(By.className("_submitBtn_1e7ib_96"));
//			sumbit_btn.click();
//			//
//	
//			// Wait for the toaster message to appear
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Toastify__toast-body")));
//	
//			// Get the toaster message text
//			String actualMessage = toastElement.getText();
//	
//			// Define the expected message
//			String expectedMessage = "Password Required";
//	
//			// Verify the toaster message
//			Assert.assertEquals(actualMessage.trim(), expectedMessage, "The toast message does not match the expected value.");
//	
//	//		Thread.sleep(6000);
//		}
//	
//	
//		@Test(priority = 6)  // check login with Invalid email and Invalid  password
//		private void Invalid_Email_and_password() throws InterruptedException {
//	
//	
//			WebElement email = driver.findElement(By.name("email"));
//			email.clear();
//			email.sendKeys("test123@gmail.com");
//	
//			WebElement password  = driver.findElement(By.name("password"));
//			password.clear();
//			password.sendKeys("1234563");
//	
//			WebElement sumbit_btn  =  driver.findElement(By.className("_submitBtn_1e7ib_96"));
//			sumbit_btn.click();
//			
//	
//			// Wait for the toaster message to appear
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("Toastify__toast-body")));
//	
//			// Get the toaster message text
//			String actualMessage = toastElement.getText();
//	
//			// Define the expected message
//			String expectedMessage = "Invalid Email Address.";
//			
//			// Verify the toaster message
//			Assert.assertEquals(actualMessage.trim(), expectedMessage, "The toast message does not match the expected value.");
//	
//	//		Thread.sleep(6000);
//		}
//
//	@Test(priority = 7)  //check login with valid email and valid password
//	private void valid_login() throws InterruptedException {
//	    WebElement email = driver.findElement(By.name("email"));
//	    email.clear();
//	    email.sendKeys("test@gmail.com");
//
//	    WebElement password = driver.findElement(By.name("password"));
//	    password.clear();
//	    password.sendKeys("123456");
//
//	    WebElement submitBtn = driver.findElement(By.className("_submitBtn_1e7ib_96"));
//	    submitBtn.click();
//	    System.out.println("Login successfully");
//
//	    // Wait for the toaster message to appear
//	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//	    WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Login success')]")));
//
//	    // Get the toaster message text
//	    String actualMessage = toastElement.getText();
//	    System.out.println("Actual Toast Message: " + actualMessage);
//
//	    // Define the expected message
//	    String expectedMessage = "Login success";
//
//	    // Verify the toaster message
//	    Assert.assertEquals(actualMessage.trim(), expectedMessage, "The toast message does not match the expected value.");
//	}

	
	@AfterMethod
	public void cleanUp() {
//		driver.manage().deleteAllCookies();
////		driver.navigate().refresh();
//			    driver.close();
	}
}