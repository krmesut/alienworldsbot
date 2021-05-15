package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import controllers.LogController;
import entities.PlayerProfile;
import entities.gameflow.GameScreens;
import entities.gameflow.WindowTypes;
import entities.logs.LogMessageTypes;

public class WaitFor {
	private boolean isLocked = true;
	private long tickIntervalInMiliseconds = 500;
	private long timeoutInMiliseconds = 20000;

	public WaitFor(float timeoutInSeconds) {
		super();
		this.timeoutInMiliseconds = (long) (timeoutInSeconds * 1000);
	}

	public WaitFor(float timeoutInSeconds, float intervalInSeconds) {
		super();
		this.timeoutInMiliseconds = (long) (timeoutInSeconds * 1000);
		this.tickIntervalInMiliseconds = (long) (intervalInSeconds * 1000);
	}

	private void updateLockedState(boolean isLocked) {
		this.isLocked = isLocked;
	}

	private void throwError(String details) throws Exception {
		if (timeoutInMiliseconds == 0) {
			throw new Exception("\n===WaitFor: Timeout error\n" + details);
		}
	}

	private void timeoutDeduction() {
		timeoutInMiliseconds -= tickIntervalInMiliseconds;
	}

	public void waitForVisibilityById(WebDriver driver, String id) throws Exception {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String message = "\n=== Checking for visibility by id: " + id;
				LogController.showLog(null, message, LogMessageTypes.WARNING);
				if ((driver.findElements(By.id(id)).size() > 0) || timeoutInMiliseconds == 0) {
					timer.cancel();
					updateLockedState(false);
				} else {
					timeoutDeduction();
				}
			}
		}, tickIntervalInMiliseconds, tickIntervalInMiliseconds);
		while (isLocked) {
			String loop = "\n---looping in locked for visibility by id: " + id;
			LogController.showLog(null, loop, LogMessageTypes.WARNING);
			Thread.sleep(tickIntervalInMiliseconds);
			// loop until unlock
			throwError("waitForVisibilityById: " + id);
		}
	}

	public void waitForVisibilityByXpath(WebDriver driver, String xpath) throws Exception {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String message = "\n=== Checking for visibility by xpath: " + xpath;
				LogController.showLog(null, message, LogMessageTypes.WARNING);
				if ((driver.findElements(By.xpath(xpath)).size() > 0) || timeoutInMiliseconds == 0) {
					timer.cancel();
					updateLockedState(false);
				} else {
					timeoutDeduction();
				}
			}
		}, tickIntervalInMiliseconds, tickIntervalInMiliseconds);
		while (isLocked) {
			String loopMessage = "\n---looping in locked for visibility by xpath: " + xpath;
			LogController.showLog(null, loopMessage, LogMessageTypes.WARNING);
			Thread.sleep(tickIntervalInMiliseconds);
			// loop until unlock
			throwError("waitForVisibilityByXpath: " + xpath);
		}
	}

	public ArrayList<int[]> waitForMatchedCoordinatesByImage(WebElement element, String imageName) throws Exception {
		ImageRecognizer ir = new ImageRecognizer();
		Utils utils = new Utils();
		while (true) {
			String message = "\n=== Checking for visibility by image: " + imageName;
			LogController.showLog(null, message, LogMessageTypes.WARNING);

			String toSearchImagePath = "";
			toSearchImagePath = utils.takeScreenshotOf(element);
			ArrayList<int[]> matchedCoordinates = ir.findImage(imageName, toSearchImagePath);
			if (matchedCoordinates.size() > 0) {
				return matchedCoordinates;
			} else {
				timeoutDeduction();
			}

			Thread.sleep(tickIntervalInMiliseconds);
			throwError("waitForMatchedCoordinatesByImage: " + imageName);
		}
	}

	public boolean waitForScreenToAppear(WebElement element, GameScreens screen) throws Exception {
		ImageRecognizer ir = new ImageRecognizer();
		Utils utils = new Utils();
		while (true) {
			String message = "\n=== Checking for visibility of screen: " + screen;
			LogController.showLog(null, message, LogMessageTypes.WARNING);

			String toSearchImagePath = "";
			toSearchImagePath = utils.takeScreenshotOf(element);
			boolean isScreenAppear = ir.isOnScreen(screen, toSearchImagePath);
			if (isScreenAppear) {
				return true;
			} else {
				timeoutDeduction();
			}

			Thread.sleep(tickIntervalInMiliseconds);
			throwError("waitForScreenToAppear: " + screen);
		}
	}

	public void waitForWindowToAppear(WebDriver driver, HashMap<WindowTypes, String> allHandleWindows,
			WindowTypes targetWindow) throws Exception {
		Utils utils = new Utils();

		while (true) {
			String message = "\n=== Checking for visibility of a window: " + targetWindow;
			LogController.showLog(null, message, LogMessageTypes.WARNING);

			utils.updateAllHandleWindows(driver, allHandleWindows);
			if (allHandleWindows.get(targetWindow) != null) {
				break;
			} else {
				timeoutDeduction();
			}

			throwError("waitForWindowToAppear: " + targetWindow);
			Thread.sleep(tickIntervalInMiliseconds);
		}
	}
}
