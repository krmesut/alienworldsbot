package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import controllers.LogController;
import entities.gameflow.WindowTypes;
import entities.logs.LogMessageTypes;

public class Utils {

	public void javascriptExecuter(WebDriver driver, String script) {
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(script);
		} else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
	}

	public void scrollToElement(WebElement element, WebDriver driver) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.perform();
	}

	public void scrollElementToViewport(WebElement element, WebDriver driver) {
		String script = "arguments[0].scrollIntoView();";
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(script, element);
		} else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
	}

	public void scrollElementToViewport(WebElement element, WebDriver driver, boolean isToTop) {
		String script = "arguments[0].scrollIntoView(true);";
		if (!isToTop) {
			script = "arguments[0].scrollIntoView(false);";
		}
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(script, element);
		} else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
	}

	public void scrollElementToViewportThenClick(WebElement element, WebDriver driver, boolean isToTop) {
		String script = "arguments[0].scrollIntoView(true);";
		if (!isToTop) {
			script = "arguments[0].scrollIntoView(false);";
		}
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(script, element);
		} else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
		element.click();
	}

	public void scrollToTop(WebDriver driver) {
		javascriptExecuter(driver, "document.documentElement.scrollTop = 0;");
	}

	public void scrollToElement(WebDriver driver, WebElement scrollWrapper, WebElement elementToScroll) {
		String script = "arguments[0].scrollTop = arguments[1].offsetTop;";
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(script, scrollWrapper, elementToScroll);
		} else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
	}

	public void scrollToElementByArrowKeyThenClick(WebElement elementToClick, WebElement elementToScroll) {
		boolean isClicked = false;
		while (!isClicked) {
			try {
				elementToClick.click();
				isClicked = true;
			} catch (Exception e) {
				// TODO: handle exception
				LogController.showLog(e, "", LogMessageTypes.ERROR);
				elementToScroll.click();
				elementToScroll.sendKeys(Keys.ARROW_UP);
			}
		}

	}

	public void mouseHoverOver(WebElement element, WebDriver driver) {
		// Instantiate Action Class
		Actions actions = new Actions(driver);
		// Mouse hover over element
		actions.moveToElement(element).perform();
	}

	public void openUrlWithTimeout(WebDriver driver, String url, int timeout) {
		driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
		try {
			driver.get(url);
		} catch (Exception e) {
			// TODO: handle exception
			LogController.showLog(e, "Failed to load " + url, LogMessageTypes.ERROR);
			throw e;
		}
	}

	public String removeInvisibleCharacters(String input) {
		String output = input.trim().replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "");
		return output;
	}

	public String escapeQuotationMarkForXpath(String input) {
		String[] splittedText = input.trim().split("'");
		String output = "";
		for (String text : splittedText) {
			output += ",\\\"'\\\",\\\"" + text + "\\\"";
		}
		return output.replaceAll("^,\\\\\\\"'\\\\\\\",\\\\\\\"", "\"");
	}

	public String copyFileToTempDir(String fileName) throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
		tempDir += File.separator + "tempDir";
		File dir = new File(tempDir);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(dir.getAbsoluteFile(), fileName);
		if (!file.exists()) {
			InputStream is = (getClass().getResourceAsStream(File.separator + fileName));
			Files.copy(is, file.getAbsoluteFile().toPath());
		} else {
		}
		file.setExecutable(true);
		file.deleteOnExit();
		return file.getAbsolutePath();
	}

	public String copyFileToTempDir(InputStream is, String fileName) throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
		tempDir += File.separator + "tempDir";
		File dir = new File(tempDir);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(dir.getAbsoluteFile(), fileName);
		if (!file.exists()) {
			Files.copy(is, file.getAbsoluteFile().toPath());
		} else {
			Files.copy(is, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		}

		file.setExecutable(true);
		file.deleteOnExit();
		return file.getAbsolutePath();
	}

	public String copyFileToTempDir(Path sourceFilePath, String fileName) throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
		tempDir += File.separator + "tempDir";
		File dir = new File(tempDir);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(dir.getAbsoluteFile(), fileName);
		if (!file.exists()) {
			Files.copy(sourceFilePath, file.getAbsoluteFile().toPath());
		} else {
			Files.copy(sourceFilePath, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		}

		file.setExecutable(true);
		file.deleteOnExit();
		return file.getAbsolutePath();
	}

	public String takeScreenshotOfAParticularElement(WebElement element) throws IOException {
		String tagName = element.getTagName();
		String id = element.getAttribute("id");
		String name = element.getAttribute("name");
		String fileName = "";

		if (id.length() > 0 && name.length() > 0) {
			fileName = tagName + "_" + name + "_" + id + ".png";
		} else if (id.length() > 0) {
			fileName = tagName + "_" + id + ".png";
		} else if (name.length() > 0) {
			fileName = tagName + "_" + name + ".png";
		} else {
			fileName = tagName + ".png";
		}

		File ss = element.getScreenshotAs(OutputType.FILE);
		String pathToScreenshot = copyFileToTempDir(ss.toPath(), fileName);
		return pathToScreenshot;
	}

	public String takeScreenshotOfAParticularElement(String prefix, WebElement element) throws IOException {
		String tagName = element.getTagName().trim().toLowerCase();
		String id = "";
		if (element.getAttribute("id") != null) {
			id = element.getAttribute("id").trim().toLowerCase();
		}
		String name = "";
		if (element.getAttribute("name") != null) {
			name = element.getAttribute("name").trim().toLowerCase();
		}
		String fileName = "";

		if (id.length() > 0 && name.length() > 0) {
			fileName = tagName + "_" + name + "_" + id + ".png";
		} else if (id.length() > 0) {
			fileName = tagName + "_" + id + ".png";
		} else if (name.length() > 0) {
			fileName = tagName + "_" + name + ".png";
		} else {
			fileName = tagName + ".png";
		}
		fileName = prefix.trim().toLowerCase() + "_" + fileName;
		fileName = fileName.replace("#", "");
		File ss = element.getScreenshotAs(OutputType.FILE);
		System.out.println("-- SS path: " + ss.getAbsolutePath());
		String pathToScreenshot = copyFileToTempDir(ss.toPath(), fileName);
//		return pathToScreenshot;
		return ss.getAbsolutePath();
	}

	public ImageIcon getImageIconFromResourceFile(String fileName) throws IOException {
		ImageIcon icon;
		InputStream stream = getClass().getResourceAsStream(File.separator + fileName);
		if (stream != null) {
			icon = new ImageIcon(ImageIO.read(stream));
			return icon;
		} else {
			return null;
		}

	}

	public BufferedImage getBufferedImageFromResourceFile(String fileName) throws IOException {
		BufferedImage icon;
		InputStream stream = getClass().getResourceAsStream(File.separator + fileName);
		if (stream != null) {
			icon = ImageIO.read(stream);
			return icon;
		} else {
			return null;
		}

	}

	public void updateAllHandleWindows(WebDriver driver, HashMap<WindowTypes, String> allHandleWindows) {
		String xpathToCanvas = "//div[@id='unityContainer']/canvas[@id='#canvas']";
		String xpathToPasswordField = "//div[@id = 'root']//div[@class = 'home']//div[contains(@class,'visible-mob-only')]//div[contains(@class,'button-container')]//input[@name='password']";
		String xpathToUsernamField = "//div[@id = 'root']//div[@class = 'home']//div[contains(@class,'visible-mob-only')]//div[contains(@class,'button-container')]//input[@name='userName']";
		String xpathToLoginWalletButton = "//div[@id = 'root']//section[@class = 'page-container']//button/div";

		Set<String> allWindows = driver.getWindowHandles();
		System.out.println("*** Updating All Handle Windows: " + allWindows.size());
		for (String windowName : allWindows) {
			driver.switchTo().window(windowName);
			if (driver.findElements(By.xpath(xpathToCanvas)).size() > 0) {
				allHandleWindows.put(WindowTypes.GAME, windowName);
				System.out.println("*** " + WindowTypes.GAME + ": " + windowName + " : " + driver.getCurrentUrl());
			} else if (driver.findElements(By.xpath(xpathToPasswordField)).size() > 0) {
				allHandleWindows.put(WindowTypes.LOGIN_TO_GAME, windowName);
				System.out.println(
						"*** " + WindowTypes.LOGIN_TO_GAME + ": " + windowName + " : " + driver.getCurrentUrl());
			} else if (driver.findElements(By.xpath(xpathToLoginWalletButton)).size() > 0) {
				allHandleWindows.put(WindowTypes.LOGIN_TO_WALLET, windowName);
				System.out.println(
						"*** " + WindowTypes.LOGIN_TO_WALLET + ": " + windowName + " : " + driver.getCurrentUrl());
			} else {
				System.out.println("***  NONE : " + windowName + " : " + driver.getCurrentUrl());
			}
		}
	}

	public void switchToWindow(WebDriver driver, HashMap<WindowTypes, String> allHandleWindows,
			WindowTypes targetWindow) {
		String windowName = allHandleWindows.get(targetWindow);
		driver.switchTo().window(windowName);
		System.out.println("*** Switch to: " + targetWindow + ": " + windowName + " : " + driver.getCurrentUrl());
	}
}
