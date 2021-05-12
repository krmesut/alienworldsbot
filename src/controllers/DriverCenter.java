package controllers;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;

public class DriverCenter {
	private static HashMap<WebDriver, Boolean> driversList;

	public static HashMap<WebDriver, Boolean> getDriversList() {
		if (driversList == null) {
			driversList = new HashMap<WebDriver, Boolean>();
		}
		return driversList;
	}

	public static void terminateAllDrivers() {
		for (WebDriver d : getDriversList().keySet()) {
			if (!d.toString().contains("null")) {
				d.quit();
			}
		}
	}

	public static void appendDriver(WebDriver driver) {
		getDriversList().put(driver, true);
	}
}
