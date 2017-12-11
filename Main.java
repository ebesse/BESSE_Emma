package src_basic;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import com.sun.glass.ui.Application;

import fr.ubordeaux.simpleUI.ApplicationRunnable;

public class Main {
	
	public static void main(String[] args) {
		Random rand = new Random();
		Point2D o1 = new Point2D.Double(rand.nextInt(1000), rand.nextInt(1000));
		Point2D o2 = new Point2D.Double(rand.nextInt(1000), rand.nextInt(1000));
		int width = 10;
		int production = 1000;
		Planet p1 = new Planet(4*width + rand.nextInt(10*width), o1, 1, 1000, 0, width);
		Planet p2 = new Planet(4*width + rand.nextInt(10*width), o2, 1, 1000, 0, width);
		
		ArrayList<Planet> pl2 = new ArrayList<Planet>();
		pl2.add(p2);
		
		RandomIA player2 = new RandomIA(2, pl2);
		ArrayList<RandomIA> players = new ArrayList<RandomIA>();
		players.add(player2);
		
		ArrayList<Item> itemList = new ArrayList<Item>();
		itemList.add((Item)p1);
		itemList.add((Item)p2);
		int distancemin = 20;
		
		while(itemList.size() <= 8) {
			Point2D o3 = new Point2D.Double(rand.nextInt(1000), rand.nextInt(1000));
			Planet p3 = new Planet(4*width + rand.nextInt(10*width), o3, 0, 1000, 0, width);
			int distanceCheck = 0;
			for (Item item : itemList) {
				Planet p = (Planet) item; 
				if (!(p3.isFarEnough(p, distancemin))) {
					distanceCheck += 1;
				}
			}
			if (distanceCheck == 0) {
				itemList.add((Item)p3);
			}
		}
		
		Run r = new Run(1000, 1000);
		Manager manager = new Manager();
		
		Application.run(itemList, manager, r);
	}

}
