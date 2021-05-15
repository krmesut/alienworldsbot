package controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import drivers.RunningDriver;
import entities.GeneralSettings;
import entities.PlayerProfile;
import entities.gameflow.GameScreens;
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
	private String xpathToCanvas = "//div[@id='unityContainer']/canvas[@id='#canvas']";

	public MiningCenter(WebDriver driver, PlayerProfile profile) {
		// TODO Auto-generated constructor stub
		this.profile = profile;
		this.driver = driver;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// initialize action
		action = new UserActions(driver);

//		while (!GeneralSettings.isStopRunning) {
		try {
			// wait for the game to load
			wait = new WaitFor(20, 1);
			wait.waitForWindowToAppear(driver, allHandleWindows, WindowTypes.GAME);
			clickOnLoginButton();
			clickToOpenMiningHub();
			clickToClaimTLM();
			wait = new WaitFor(20);
			wait.waitForWindowToAppear(driver, allHandleWindows, WindowTypes.LOGIN_TO_WALLET);
			utils.switchToWindow(driver, allHandleWindows, WindowTypes.LOGIN_TO_WALLET);
			for (WebElement checkbox : driver.findElements(By.xpath(
					"//div[contains(@class,'authorize-transaction-container')]/div[contains(@class,'remember')]/label/span[contains(text(),'Always sign those transactions')]"))) {
				checkbox.click();
			}
			String captchaResult = solveCaptcha();
			String script = "document.getElementById('g-recaptcha-response').innerHTML='" + captchaResult + "';";
			System.out.println("--- Script: " + script);
			utils.javascriptExecuter(driver, script);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		}
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
		ArrayList<int[]> matchedCoordinates = wait.waitForMatchedCoordinatesByImage(getGameElement(), "btnLogin2.png");
		// click on the login button
		action.clickOnMatchedCoordinate(matchedCoordinates.get(0), getGameElement());
	}

	private void clickToOpenMiningHub() throws Exception {
		utils.switchToWindow(driver, allHandleWindows, WindowTypes.GAME);
		// wait for Mine button to appear
		wait = new WaitFor(20, 3);
		ArrayList<int[]> matchedCoordinates = wait.waitForMatchedCoordinatesByImage(getGameElement(), "btnMine.png");
		// click on the login button
		action.clickOnMatchedCoordinate(matchedCoordinates.get(0), getGameElement());
	}

	private void clickToClaimTLM() throws Exception {
		utils.switchToWindow(driver, allHandleWindows, WindowTypes.GAME);
		// wait for Mine button to appear
		wait = new WaitFor(20, 3);
		ArrayList<int[]> matchedCoordinates = wait.waitForMatchedCoordinatesByImage(getGameElement(),
				"btnClaimBottom.png");
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
		String xpathToRecaptchaOverlay = "//iframe[contains(@title,'recaptcha challenge')]";
		String xpathToHiddenInput = "//input[contains(@id,'recaptcha-token')]";
		String xpathToVerifyButton = "//div[contains(@id,'rc-imageselect')]/div[contains(@class,'rc-footer')]/div[contains(@class,'rc-controls')]/div/div[contains(@class,'verify-button-holder')]/button";

		utils.switchToWindow(driver, allHandleWindows, WindowTypes.LOGIN_TO_GAME);

		wait = new WaitFor(20);
		wait.waitForVisibilityByXpath(driver, xpathToUsernamField);
		System.out.println("--- Current frame: " + utils.getCurrentFrame(driver));
		System.out.println("--- Filling Username...." + profile.getUsername());
		WebElement usernameField = driver.findElement(By.xpath(xpathToUsernamField));
		usernameField.clear();
		usernameField.sendKeys(profile.getUsername());

		System.out.println("--- Filling Password...." + profile.getPassword());
		WebElement passwordField = driver.findElement(By.xpath(xpathToPasswordField));
		passwordField.clear();
		passwordField.sendKeys(profile.getPassword());

//		System.out.println("--- Current frame: " + utils.getCurrentFrame(driver));
//		String script = "document.getElementById('g-recaptcha-response').innerHTML='" + captchaResult + "';";
//		System.out.println("--- Script: " + script);
//		utils.javascriptExecuter(driver, script);
//		script = "document.getElementById('g-recaptcha-response-1').innerHTML='" + captchaResult + "';";
//		System.out.println("--- Script: " + script);
//		utils.javascriptExecuter(driver, script);

		// click to show captcha overlay
//		System.out.println("--- Clicking Captcha....");
//		driver.switchTo().frame(driver.findElement(By.xpath(xpathToVisibleReCaptchaiFrame)));
//		for (WebElement rcAnchor : driver.findElements(By.xpath(xpathToReCaptchaAnchor))) {
//			rcAnchor.click();
//		}
		// find the proper captcha iframe
//		WebElement captchaFrame = null;
//		int retries = 0;
//		int maxRetries = 5;
//		while (retries < maxRetries) {
//			driver.switchTo().defaultContent();
//			wait = new WaitFor(20);
//			wait.waitForVisibilityByXpath(driver, xpathToRecaptchaOverlay);
//			System.out.println("--- retry: " + retries);
//			try {
//				List<WebElement> allFrames = driver.findElements(By.xpath(xpathToRecaptchaOverlay));
//				System.out.println("--- found " + allFrames.size() + " frame(s)");
//				for (WebElement frame : allFrames) {
//					System.out.println("--- found frame: " + frame.getAttribute("title"));
//					driver.switchTo().frame(frame);
//					for (WebElement hiddenInput : driver.findElements(By.xpath(xpathToHiddenInput))) {
//						System.out.println("--- found hidden input: " + hiddenInput.getAttribute("value") + ".");
//						String script = "document.getElementById('recaptcha-token').setAttribute('value', '"
//								+ captchaResult + "');";
//						utils.javascriptExecuter(driver, script);
//						System.out.println("--- update hidden input: " + hiddenInput.getAttribute("value") + ".");
//						if (hiddenInput.getAttribute("value").length() > 0) {
//							captchaFrame = frame;
//						}
//					}
//					driver.switchTo().defaultContent();
//				}
//				retries = maxRetries;
//			} catch (StaleElementReferenceException stale) {
//				// TODO: handle exception
//				stale.printStackTrace();
//				System.out.println("--- retrying.....");
//				retries++;
//			} catch (JavascriptException stale) {
//				// TODO: handle exception
//				stale.printStackTrace();
//				System.out.println("--- retrying.....");
//				retries++;
//			}
//		}

		// switch to captch overlay and click on verify button
//		driver.switchTo().frame(captchaFrame);
//		for (WebElement btnVerify : driver.findElements(By.xpath(xpathToVerifyButton))) {
//			System.out.println("--- verify button found.....");
//			btnVerify.click();
//		}

		// switch to main page and click on login button
//		driver.switchTo().defaultContent();
//		WebElement loginButton = driver.findElement(By.xpath(xpathToLoginButton));
//		loginButton.click();
	}

	private String solveCaptcha() throws Exception {
		String xpathToReCaptchaiFrame = "//iframe[@title = 'reCAPTCHA']";
		driver.switchTo().defaultContent();
		System.out.println("--- Current frame: " + utils.getCurrentFrame(driver));
		String captchaSrc = driver.findElement(By.xpath(xpathToReCaptchaiFrame)).getAttribute("src");
		System.out.println("--- Captcha link: " + captchaSrc);

		String captchaKey = utils.parseQueryUrl(captchaSrc).get("k").get(0);
		System.out.println("--- Captcha key: " + captchaKey);

		String captchaResult = requestCaptchResult(captchaKey).trim();
		System.out.println("--- Captcha result: " + captchaResult);
		return captchaResult;
	}

	private String requestCaptchResult(String key) throws Exception {
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
