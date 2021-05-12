package utils;

import java.io.File;
import java.util.ArrayList;

import org.sikuli.script.Finder;
import org.sikuli.script.Image;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;

public class ImageRecognizer {

	public ArrayList<int[]> findImage(String searchImg, String toSearchImgPath) {
		ArrayList<int[]> matchedCoordinates = new ArrayList<>();
//		toSearchImgPath = getClass().getResource(File.separator + "screenshot.png").getPath();
		System.out.println(" --- search image: " + getClass().getResource(File.separator + searchImg).getPath());
		System.out.println(" ---  to search image: " + toSearchImgPath);
		// sikuliX 2.0.5 will fix the issue. Using "Image.create()" is a workaround for
		// now
		Pattern searchImage = new Pattern(Image.create(getClass().getResource(File.separator + searchImg).getPath()));
		double similarity = 1;
		while (matchedCoordinates.size() < 1) {
			Pattern searchImageSimilar = searchImage.similar(similarity);
			Finder objFinder = null;
			objFinder = new Finder(Image.create(toSearchImgPath));
			objFinder.find(searchImageSimilar);
			while (objFinder.hasNext()) {
				Match objMatch = objFinder.next();
				System.out.println("=== " + similarity + ": Match found (x,y,w,h): " + objMatch.x + ", " + objMatch.y
						+ ", " + objMatch.w + ", " + objMatch.h);
				int[] coordinate = { objMatch.x, objMatch.y, objMatch.w, objMatch.h };
				matchedCoordinates.add(coordinate);
			}
			if (matchedCoordinates.size() > 0 || similarity < 0.5) {
				break;
			} else {
				System.out.println("=== " + similarity + ": Not found");
				similarity -= 0.01;
			}
		}
		return matchedCoordinates;
	}
}
