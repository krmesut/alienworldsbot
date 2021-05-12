package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import drivers.RunningDriver;
import entities.PlayerProfile;
import entities.gameflow.WindowTypes;
import utils.UserActions;
import utils.Utils;
import utils.WaitFor;

public class MiningCenter implements Runnable {
	private WebDriver driver;
	private PlayerProfile profile;
	private Utils utils = new Utils();
	private HashMap<WindowTypes, String> allHandleWindows = new HashMap<WindowTypes, String>();
	private WaitFor wait;
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

		while (true) {
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
		String xpathToReCaptchaiFrame = "//div[@id = 'root']//div[@class = 'home']//div[contains(@class,'visible-mob-only')]//div[contains(@class,'button-container')]//iframe[contains(@title,'reCAPTCHA')]";
		String xpathToReCaptchaCheckmark = "//div[contains(@id, 'rc-anchor-container')]/div[contains(@class, 'rc-anchor-content')]//div[contains(@class,'recaptcha-checkbox-checkmark')]";
		String xpathTOReCaptchaAnchor = "//div[contains(@id, 'rc-anchor-container')]/div[contains(@class, 'rc-anchor-content')]//div[contains(@class,'rc-anchor-center-container')]/div/span[@id = 'recaptcha-anchor']";

		utils.switchToWindow(driver, allHandleWindows, WindowTypes.LOGIN_TO_GAME);

		wait = new WaitFor(20);
		wait.waitForVisibilityByXpath(driver, xpathToUsernamField);
		WebElement usernameField = driver.findElement(By.xpath(xpathToUsernamField));
		usernameField.clear();
		usernameField.sendKeys(profile.getUsername());
		WebElement passwordField = driver.findElement(By.xpath(xpathToPasswordField));
		passwordField.clear();
		passwordField.sendKeys(profile.getPassword());

		driver.switchTo().frame(driver.findElement(By.xpath(xpathToReCaptchaiFrame)));
		for (WebElement rcAnchor : driver.findElements(By.xpath(xpathTOReCaptchaAnchor))) {
			rcAnchor.click();
		}

//		for (WebElement rcCheckmark : driver.findElements(By.xpath(xpathToReCaptchaCheckmark))) {
//			rcCheckmark.click();
//		}

		driver.switchTo().defaultContent();
		WebElement loginButton = driver.findElement(By.xpath(xpathToLoginButton));
		loginButton.click();
	}

}
