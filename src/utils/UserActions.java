package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

public class UserActions {
	private WebDriver driver;
	private Actions actions;

	public UserActions(WebDriver driver) {
		super();
		this.driver = driver;
		this.actions = new Actions(driver);
	}

	public void clickOnMatchedCoordinate(int[] coordinate, WebElement targetElement) {
		// assume that the coordinate is x,y,w,h
		int x = (coordinate[0] + coordinate[2] / 2) - targetElement.getSize().getWidth() / 2;
		int y = (coordinate[1] + coordinate[3] / 2) - targetElement.getSize().getHeight() / 2;
		System.out.println("==target: " + x + ":" + y);
		Action clickonCoordinate = actions.moveToElement(targetElement).moveByOffset(x, y).click().build();
		clickonCoordinate.perform();
	}
}
