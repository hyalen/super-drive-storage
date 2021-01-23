package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	private static final String FIRST_NAME = "Test";
	private static final String LAST_NAME = "User";
	private static final String USERNAME = "testuser";
	private static final String PASSWORD = "password";

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() throws InterruptedException {
		driver.get(String.format("http://localhost:%d/login", this.port));
		Thread.sleep(1000);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getHomePageUnauthorized() throws InterruptedException {
		driver.get(String.format("http://localhost:%d/home", this.port));
		Thread.sleep(1000);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void newUserFlow() throws InterruptedException {
		signUp();
		login();

		Assertions.assertEquals("Home", driver.getTitle());

		WebElement element = driver.findElement(By.id("logout"));
		element.click();
		Thread.sleep(1000);

		Assertions.assertEquals("Login", driver.getTitle());

		driver.get(String.format("http://localhost:%d/home", this.port));
		Thread.sleep(1000);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	private void signUp() throws InterruptedException {
		driver.get(String.format("http://localhost:%d/signup", this.port));
		Thread.sleep(1000);

		WebElement element = driver.findElement(By.id("inputFirstName"));
		element.sendKeys(FIRST_NAME);

		element = driver.findElement(By.id("inputLastName"));
		element.sendKeys(LAST_NAME);

		element = driver.findElement(By.id("inputUsername"));
		element.sendKeys(USERNAME);

		element = driver.findElement(By.id("inputPassword"));
		element.sendKeys(PASSWORD);

		element = driver.findElement(By.id("signup"));
		element.click();
		Thread.sleep(1000);
	}

	private void login() throws InterruptedException {
		driver.get(String.format("http://localhost:%d/login", this.port));
		Thread.sleep(1000);

		WebElement element = driver.findElement(By.id("inputUsername"));
		element.sendKeys(USERNAME);

		element = driver.findElement(By.id("inputPassword"));
		element.sendKeys(PASSWORD);

		element = driver.findElement(By.id("login"));
		element.click();
		Thread.sleep(1000);
	}

	@Test
	public void notesFlowTest() throws InterruptedException {
		signUp();
		login();

		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();
		Thread.sleep(500);

		WebElement showNoteModel = driver.findElement(By.id("show-note-modal"));
		showNoteModel.click();
		Thread.sleep(500);

		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.sendKeys("TestNoteTitle");

		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.sendKeys("TestNoteDescription");

		WebElement saveNote = driver.findElement(By.id("save-note"));
		saveNote.click();
		Thread.sleep(1000);

		WebElement homeReturn = driver.findElement(By.id("return-home"));
		homeReturn.click();
		Thread.sleep(500);

		WebElement notesTab1 = driver.findElement(By.id("nav-notes-tab"));
		notesTab1.click();
		Thread.sleep(500);

		WebElement savedNote = driver.findElement(By.cssSelector("th.note-title-row"));
		Assertions.assertEquals("TestNoteTitle", savedNote.getText());
		Thread.sleep(500);

		WebElement editNote = driver.findElement(By.cssSelector("button.edit-note"));
		editNote.click();
		Thread.sleep(500);

		WebElement noteTitle1 = driver.findElement(By.id("note-title"));
		noteTitle1.sendKeys("Edit");
		WebElement saveNote1 = driver.findElement(By.id("save-note"));
		saveNote1.click();
		Thread.sleep(1000);


		WebElement homeReturn1 = driver.findElement(By.id("return-home"));
		homeReturn1.click();
		Thread.sleep(1000);

		WebElement notesTab2 = driver.findElement(By.id("nav-notes-tab"));
		notesTab2.click();
		Thread.sleep(500);

		WebElement savedNote1 = driver.findElement(By.cssSelector("th.note-title-row"));
		Assertions.assertEquals("TestNoteTitleEdit", savedNote1.getText());

		WebElement deleteNote = driver.findElement(By.cssSelector("a.delete-note"));
		deleteNote.click();
		Thread.sleep(1000);

		WebElement homeReturn2 = driver.findElement(By.id("return-home"));
		homeReturn2.click();
		Thread.sleep(1000);

		WebElement notesTab3 = driver.findElement(By.id("nav-notes-tab"));
		notesTab3.click();
		Thread.sleep(500);

		boolean ifNotePresent = driver.findElements(By.cssSelector("th.note-title-row")).isEmpty();
		Assertions.assertTrue(ifNotePresent);
	}

	@Test
	public void credentialFlowTest() throws InterruptedException {
		signUp();
		login();

		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab.click();

		WebElement element = driver.findElement(By.id("show-credentials-modal"));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
		Thread.sleep(2000);

		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		Thread.sleep(2000);
		credentialUrl.sendKeys("www.test.co");
		Thread.sleep(2000);

		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.sendKeys("testUsername");

		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.sendKeys("testPassword");

		WebElement credentialSubmit = driver.findElement(By.id("credential-submit"));
		credentialSubmit.click();
		Thread.sleep(1000);

		WebElement homeReturn = driver.findElement(By.id("return-home"));
		homeReturn.click();
		Thread.sleep(500);

		WebElement credentialsTab1 = driver.findElement(By.id("nav-credentials-tab"));
		credentialsTab1.click();
		Thread.sleep(500);

		WebElement savedNote = driver.findElement(By.cssSelector("th.saved-credential-url"));
		Assertions.assertEquals("www.test.co", savedNote.getText());
		Thread.sleep(500);

		WebElement editNote = driver.findElement(By.cssSelector("button.edit-credential"));
		editNote.click();
		Thread.sleep(500);

		WebElement credentialUrl1 = driver.findElement(By.id("credential-url"));
		credentialUrl1.sendKeys(".in");
		WebElement saveCredential = driver.findElement(By.id("credential-submit"));
		saveCredential.click();
		Thread.sleep(1000);


		WebElement homeReturn1 = driver.findElement(By.id("return-home"));
		homeReturn1.click();
		Thread.sleep(1000);

		WebElement notesTab2 = driver.findElement(By.id("nav-credentials-tab"));
		notesTab2.click();
		Thread.sleep(500);

		WebElement savedCredential = driver.findElement(By.cssSelector("th.saved-credential-url"));
		Assertions.assertEquals("www.test.co.in", savedCredential.getText());

		WebElement deleteCredential = driver.findElement(By.cssSelector("a.delete-credential"));
		deleteCredential.click();
		Thread.sleep(1000);

		WebElement homeReturn2 = driver.findElement(By.id("return-home"));
		homeReturn2.click();
		Thread.sleep(1000);

		WebElement credentialTab3 = driver.findElement(By.id("nav-credentials-tab"));
		credentialTab3.click();
		Thread.sleep(500);

		boolean ifCredentialPresent = driver.findElements(By.cssSelector("th.saved-credential-url"))
			.isEmpty();
		Assertions.assertTrue(ifCredentialPresent);
	}
}
