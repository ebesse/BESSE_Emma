package src_basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomIA {
	private int player;
	private ArrayList<Planet> planets;
	
	public RandomIA(int pl, ArrayList<Planet> p) {
		this.player = pl;
		this.planets = p;
	}
	
	public void act(ArrayList<Planet> p) {
		Random rand = new Random();
		int r = rand.nextInt(3);
		if (r == 0) {return;}
		
		int s = planets.size();
		int rs = rand.nextInt(s);
		int sp = p.size();
		int rp = rand.nextInt(sp);
		int percentage = rand.nextInt(101);
		
		planets.get(rs).setPercentage(percentage);
		planets.get(rs).takeOff(p.get(rp));
		return;
	}
}
