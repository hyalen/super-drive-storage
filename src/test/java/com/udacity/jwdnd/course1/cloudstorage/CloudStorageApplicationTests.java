package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {
	// user's login info
	private final String USERNAME = "hyalen";
	private final String PASSWORD = "hyalen123";

	// notes inputs
	private final String NOTE_TITLE = "This is a title";
	private final String NOTE_DESCRIPTION = "This is a description";
	private final String NOTE_TITLE_EDIT = "This is another title";
	private final String NOTE_DESCRIPTION_EDIT = "This is another description";

	// credentials inputs
	private final String CREDENTIAL_URL = "www.google.com";
	private final String CREDENTIAL_USERNAME = "newUsername";
	private final String CREDENTIAL_PASSWORD = "password123";
	private final String CREDENTIAL_URL_EDIT = "www.google.com";
	private final String CREDENTIAL_USERNAME_EDIT = "newUsername";
	private final String CREDENTIAL_PASSWORD_EDIT = "password123";

	@LocalServerPort
	private int port;

	@Autowired
	private EncryptionService encryptionService;

	@Autowired
	private CredentialService credentialService;

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

	public void getDriver(String route) {
		driver.get("http://localhost:" + this.port + "/" + route);
	}

	public void getPage(String route, String expectedTitle) throws InterruptedException {
		getDriver(route);
		Assertions.assertEquals(expectedTitle, driver.getTitle());
		Thread.sleep(500);
	}

	// test that verifies that an unauthorized user can only access the login and signup pages
	@Test
	@Order(1)
	public void restrictUnauthorizedAccess() throws InterruptedException {
		getPage("login", "Login");
		getPage("signup", "Signup Page");

		// accessing the homepage without authentication, it will redirect to the login page
		getPage("home", "Login");
	}

	// test that signs up a new user, logs in, verifies that the home page is accessible,
	// logs out, and verifies that the home page is no longer accessible
	@Test
	@Order(2)
	public void accessUserFlow() throws InterruptedException {
		signupRandomUser("Hyalen", "Neves Caldeira", USERNAME, PASSWORD);
		login(USERNAME, PASSWORD);
		getPage("home", "Home Page");
		logout();
		getPage("home", "Login");
	}

	public void signupRandomUser(String firstName, String lastName, String username, String password) throws InterruptedException {
		getDriver("signup");

		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.sendKeys(firstName);
		Thread.sleep(200);

		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.sendKeys(lastName);
		Thread.sleep(200);

		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(username);
		Thread.sleep(200);

		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		Thread.sleep(200);

		WebElement signUpButton = driver.findElement(By.id("signup"));
		signUpButton.click();
		Thread.sleep(500);
	}

	public void login(String username, String password) throws InterruptedException {
		getDriver("login");

		WebElement usernameInput = driver.findElement(By.id("inputUsername"));
		usernameInput.sendKeys(username);
		Thread.sleep(200);

		WebElement passwordInput = driver.findElement(By.id("inputPassword"));
		passwordInput.sendKeys(password);
		Thread.sleep(200);

		WebElement loginButton = driver.findElement(By.id("login-submit"));
		loginButton.click();
		Thread.sleep(500);
	}

	public void logout() throws InterruptedException {
		WebElement logoutButton = driver.findElement(By.id("logout"));
		logoutButton.click();
		Thread.sleep(500);
	}

	// test that creates a note, and verifies it is displayed
	@Test
	@Order(3)
	public void noteCreate() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		login(USERNAME, PASSWORD);

		clickElement("nav-notes-tab");
		clickElement("show-note-modal");

		WebElement noteTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		noteTitleElement.sendKeys(NOTE_TITLE);

		WebElement noteDescriptionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		noteDescriptionElement.sendKeys(NOTE_DESCRIPTION);

		WebElement submitNoteElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note")));
		submitNoteElement.click();

		checksIfSuccessInsertionAndRedirectsToHome(wait);

		clickElement("nav-notes-tab");

		// checks if the insertion is shown in the UI properly
		WebElement navNotes = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
		assertThat(navNotes.getText().contains(NOTE_TITLE));
		assertThat(navNotes.getText().contains(NOTE_DESCRIPTION));
	}

	@Test
	@Order(4)
	public void NoteEdit() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		login(USERNAME, PASSWORD);

		clickElement("nav-notes-tab");

		WebElement editNoteElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note")));
		editNoteElement.click();

		WebElement noteTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		noteTitleElement.clear();
		noteTitleElement.sendKeys(NOTE_TITLE_EDIT);

		WebElement noteDescriptionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		noteDescriptionElement.clear();
		noteDescriptionElement.sendKeys(NOTE_DESCRIPTION_EDIT);

		WebElement submitNoteElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note")));
		submitNoteElement.click();

		checksIfSuccessInsertionAndRedirectsToHome(wait);

		clickElement("nav-notes-tab");

		// checks if the insertion is shown in the UI properly
		WebElement navNotes = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
		assertThat(navNotes.getText().contains(NOTE_TITLE_EDIT)).isTrue();
		assertThat(navNotes.getText().contains(NOTE_DESCRIPTION_EDIT)).isTrue();
	}

	@Test
	@Order(5)
	public void NoteDelete() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		login(USERNAME, PASSWORD);

		clickElement("nav-notes-tab");

		WebElement editNoteElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note")));
		editNoteElement.click();

		checksIfSuccessInsertionAndRedirectsToHome(wait);

		clickElement("nav-notes-tab");

		WebElement navNotes = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
		assertThat(navNotes.getText().contains(NOTE_TITLE_EDIT)).isFalse();
		assertThat(navNotes.getText().contains(NOTE_DESCRIPTION_EDIT)).isFalse();
	}

	@Test
	@Order(6)
	public void credentialCreate() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		login(USERNAME, PASSWORD);

		clickElement("nav-credentials-tab");
		clickElement("show-credentials-modal");

		WebElement credentialUrlElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		credentialUrlElement.sendKeys(CREDENTIAL_URL);

		WebElement credentialUsernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		credentialUsernameElement.sendKeys(CREDENTIAL_USERNAME);

		WebElement credentialPasswordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		credentialPasswordElement.sendKeys(CREDENTIAL_PASSWORD);

		WebElement submitNoteElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-credential")));
		submitNoteElement.click();

		checksIfSuccessInsertionAndRedirectsToHome(wait);

		clickElement("nav-credentials-tab");

		// getting the encrypted password
		Credential credential = credentialService.getCredentialById(1);

		// checks if the insertion is shown in the UI properly
		WebElement navCredentials = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
		assertThat(navCredentials.getText().contains(CREDENTIAL_URL)).isTrue();
		assertThat(navCredentials.getText().contains(CREDENTIAL_USERNAME)).isTrue();

		// comparing the encrypted password stored in the DB with the one that is shown inside the UI
		assertThat(navCredentials.getText().contains(credential.getPassword())).isTrue();
	}

	@Test
	@Order(7)
	public void CredentialEdit() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		login(USERNAME, PASSWORD);

		clickElement("nav-credentials-tab");

		WebElement editNoteElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-credential")));
		editNoteElement.click();

		Credential credential = credentialService.getCredentialById(1);
		String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());

		WebElement credentialUrlElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		credentialUrlElement.clear();
		credentialUrlElement.sendKeys(CREDENTIAL_URL_EDIT);

		WebElement credentialUsernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		credentialUsernameElement.clear();
		credentialUsernameElement.sendKeys(CREDENTIAL_USERNAME_EDIT);

		WebElement credentialPasswordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));

		// verifies that the displayed password in the modal is the same that the decrypted one stored in the DB
		Assertions.assertEquals(credentialPasswordElement.getAttribute("value"), decryptedPassword);

		credentialPasswordElement.clear();
		credentialPasswordElement.sendKeys(CREDENTIAL_PASSWORD_EDIT);

		WebElement submitNoteElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-credential")));
		submitNoteElement.click();

		checksIfSuccessInsertionAndRedirectsToHome(wait);

		clickElement("nav-credentials-tab");

		// checks if the insertion is shown in the UI properly
		WebElement navCredentials = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
		assertThat(navCredentials.getText().contains(CREDENTIAL_URL_EDIT)).isTrue();
		assertThat(navCredentials.getText().contains(CREDENTIAL_USERNAME_EDIT)).isTrue();
	}

	@Test
	@Order(8)
	public void CredentialDelete() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		login(USERNAME, PASSWORD);

		clickElement("nav-credentials-tab");

		WebElement removeCredentialElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-credential")));
		removeCredentialElement.click();

		checksIfSuccessInsertionAndRedirectsToHome(wait);

		clickElement("nav-credentials-tab");

		WebElement navNotes = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
		assertThat(navNotes.getText().contains(CREDENTIAL_URL_EDIT)).isFalse();
		assertThat(navNotes.getText().contains(CREDENTIAL_USERNAME_EDIT)).isFalse();
		assertThat(navNotes.getText().contains(CREDENTIAL_PASSWORD_EDIT)).isFalse();
	}

	public void checksIfSuccessInsertionAndRedirectsToHome(WebDriverWait wait) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		WebElement successDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-title")));
		Assertions.assertEquals("Success", successDiv.getText());

		clickElement("return-home");
	}

	public void clickElement(String elementId) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementId)));
		executor.executeScript("arguments[0].click()", element);
	}
}
