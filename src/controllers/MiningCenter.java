package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import drivers.RunningDriver;
import entities.GeneralSettings;
import entities.PlayerProfile;
import entities.gameflow.WindowTypes;
import entities.http.HTTPContentTypes;
import entities.http.HTTPMethods;
import utils.HttpRequest;
import utils.UserActions;
import utils.Utils;
import utils.WaitFor;

public class MiningCenter implements Runnable {
	private WebDriver driver;
	private PlayerProfile profile;
	private Utils utils = new Utils();
	private HashMap<WindowTypes, String> allHandleWindows = new HashMap<WindowTypes, String>();
	private WaitFor wait;
	private HttpRequest request;
	private UserActions action;
	private String url;
	private String xpathToCanvas = "//div[@id='unityContainer']/canvas[@id='#canvas']";

	public MiningCenter(String url, PlayerProfile profile) {
		// TODO Auto-generated constructor stub
		this.profile = profile;
		this.url = url;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		initDriver();
		action = new UserActions(driver);
		// open the game
		driver.navigate().to(url);

		while (!GeneralSettings.isStopRunning) {
			try {
				// wait for the game to load
				wait = new WaitFor(20, 1);
				wait.waitForWindowToAppear(driver, allHandleWindows, WindowTypes.GAME);
				clickOnLoginButton();

				// wait for the login window to appear
				wait = new WaitFor(20, 1);
				wait.waitForWindowToAppear(driver, allHandleWindows, WindowTypes.LOGIN_TO_GAME);
				loginByEmail();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void initDriver() {
		RunningDriver runningDriver = new RunningDriver();
		try {
			driver = runningDriver.chromeDriver();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resizeBrowser(800, 600);
	}

	private void resizeBrowser(int width, int height) {
		Dimension d = new Dimension(width, height);
		// Resize current window to the set dimension
		driver.manage().window().setSize(d);
	}

	private WebElement getGameElement() throws Exception {
		WaitFor wait = new WaitFor(20);
		wait.waitForVisibilityByXpath(driver, xpathToCanvas);
		return driver.findElement(By.xpath(xpathToCanvas));
	}

	private void clickOnLoginButton() throws Exception {
		utils.switchToWindow(driver, allHandleWindows, WindowTypes.GAME);
		// wait for Login button to appear
		wait = new WaitFor(20, 3);
		ArrayList<int[]> matchedCoordinates = wait.waitForMatchedCoordinatesByImage(getGameElement(), "btnLogin2.png",
				profile);
		// click on the login button
		action.clickOnMatchedCoordinate(matchedCoordinates.get(0), getGameElement());
	}

	private void loginByEmail() throws Exception {
		String xpathToPasswordField = "//div[@id = 'root']//div[@class = 'home']//div[contains(@class,'visible-mob-only')]//div[contains(@class,'button-container')]//input[@name='password']";
		String xpathToUsernamField = "//div[@id = 'root']//div[@class = 'home']//div[contains(@class,'visible-mob-only')]//div[contains(@class,'button-container')]//input[@name='userName']";
		String xpathToLoginButton = "//div[@id = 'root']//div[@class = 'home']//div[contains(@class,'visible-mob-only')]//div[contains(@class,'button-container')]//button[text() = 'Login']";
		String xpathToVisibleReCaptchaiFrame = "//div[@id = 'root']//div[@class = 'home']//div[contains(@class,'visible-mob-only')]//div[contains(@class,'button-container')]//iframe[contains(@title,'reCAPTCHA')]";
		String xpathToReCaptchaCheckmark = "//div[contains(@id, 'rc-anchor-container')]/div[contains(@class, 'rc-anchor-content')]//div[contains(@class,'recaptcha-checkbox-checkmark')]";
		String xpathToReCaptchaAnchor = "//div[contains(@id, 'rc-anchor-container')]/div[contains(@class, 'rc-anchor-content')]//div[contains(@class,'rc-anchor-center-container')]/div/span[@id = 'recaptcha-anchor']";
		String xpathToReCaptchaiFrame = "//iframe[@title = 'reCAPTCHA']";

		utils.switchToWindow(driver, allHandleWindows, WindowTypes.LOGIN_TO_GAME);

		wait = new WaitFor(20);
		wait.waitForVisibilityByXpath(driver, xpathToUsernamField);
		System.out.println("--- Filling Username....");
		WebElement usernameField = driver.findElement(By.xpath(xpathToUsernamField));
		usernameField.clear();
		usernameField.sendKeys(profile.getUsername());

		System.out.println("--- Filling Password....");
		WebElement passwordField = driver.findElement(By.xpath(xpathToPasswordField));
		passwordField.clear();
		passwordField.sendKeys(profile.getPassword());

		System.out.println("--- Clicking Captcha....");
		driver.switchTo().frame(driver.findElement(By.xpath(xpathToVisibleReCaptchaiFrame)));
		for (WebElement rcAnchor : driver.findElements(By.xpath(xpathToReCaptchaAnchor))) {
			rcAnchor.click();
		}

		driver.switchTo().defaultContent();
		String captchaSrc = driver.findElement(By.xpath(xpathToReCaptchaiFrame)).getAttribute("src");
		System.out.println("--- Captcha link: " + captchaSrc);

		String captchaKey = utils.parseQueryUrl(captchaSrc).get("k").get(0);
		System.out.println("--- Captcha key: " + captchaKey);

//		String captchaResult = solveCaptcha(captchaKey);
		String captchaResult = "03AGdBq2573JiKGLjk7F-VIQGxfUw8sqc66UQGWkoAu73VXAeOX4OdwbldtdnnZN9Ul3jmssTZBM93w_Jy2m2MscUOC0DuWi5Sec6hQZwGtp4GC5cw4S0gXLd47H5t-qqHZMFAWrOAxxfQq02B4gE9F429bLocvUiasMQ1dGJ-Uh0HiKZWgwLyHQAvCVyrP3ToPeuU10-89DusNMCWbLB_AilSvT7WVKRMq2ivZC80_jGY7FO0-4jTFnYRIwmOjT3zmj6xH92Gdu6ARmCJAShI8p1MMeuapRG12tJIfIX0Rs1pQ9ZV1ZGyOyUiC5c7ewwfwOZ1xXyWP-tNtuZl5twnSRBZ2jZAXL1gp_Gizq_oNfjbL94Wqxia-0oUrnHtkZFtIDJwFP0qmNbTFiQH-eKXFygmFK23AaQLWP9iEUlvUcTsli3jxePgKA0Psb13wLH0lZfJBMjgL6zUz5_WEGUVh9eZ_LYFw5-Thz5opyUvxiOQCkZkC7Ij9V200yCfDxtMgm8Gal1Q9l8NaX7JQTYbGNPVMCK-_IdsoQ";
		System.out.println("--- Captcha result: " + captchaResult);

		String script = "document.getElementById('g-recaptcha-response-1').innerHTML='" + captchaResult + "';";
		utils.javascriptExecuter(driver, script);

//		for (WebElement rcCheckmark : driver.findElements(By.xpath(xpathToReCaptchaCheckmark))) {
//			rcCheckmark.click();
//		}

		WebElement loginButton = driver.findElement(By.xpath(xpathToLoginButton));
		loginButton.click();
	}

	private String solveCaptcha(String key) throws Exception {
		String result = "";
		String urlParameters = "";
		String requestID = "";

		request = new HttpRequest(HTTPMethods.POST, "http://2captcha.com/in.php",
				HTTPContentTypes.FORM_URL_ENCODED.label);
		urlParameters = "key=" + GeneralSettings.solveCaptchaApiKey + "&method=userrecaptcha" + "&googlekey=" + key
				+ "&pageurl=https://all-access.wax.io/";
		result = request.execute(urlParameters);
		System.out.println("=== result: " + result);

		if (result.split("\\|").length > 1) {
			requestID = result.split("\\|")[1];
		}

		if (requestID.length() > 0) {
			Thread.sleep(20000);
			result = getCaptchaResult(requestID);

			int retries = 50;
			while ((retries > 0) && (result.isEmpty() || "CAPCHA_NOT_READY".equals(result.trim()))) {
				System.out.println("--- Retrying..... " + retries);
				Thread.sleep(3000);
				result = getCaptchaResult(requestID);
				retries--;
			}

			if (result.isEmpty() || "CAPCHA_NOT_READY".equals(result.trim())) {
				throw new Exception("Failed to solve captcha: " + result);
			} else {
				result = result.split("\\|")[1];
			}

			return result;
		} else {
			throw new Exception("Failed to solve captcha: Invalid request ID : " + result);
		}
	}

	private String getCaptchaResult(String requestID) {
		requestID = requestID.trim();
		request = new HttpRequest(HTTPMethods.GET, "http://2captcha.com/res.php", "");
		String urlParameters = "key=" + GeneralSettings.solveCaptchaApiKey + "&action=get&id=" + requestID;
		String result = request.execute(urlParameters);
		System.out.println("=== response: " + result);
		return result;
	}

}
