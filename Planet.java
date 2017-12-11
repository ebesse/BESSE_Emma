package src_basic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Planet extends Item{
	
	private int production; // seconds between ship creation
	private int reserve;
	private int prodwidth;
	private int percentage;
	
	public Planet(int w, Point2D c, int pl, int pr, int r, int prodwidth) {
		super(w, c, pl);
		production = pr;
		reserve = r;
		prodwidth = prodwidth;
	}
	
	public int getPercentage() {
		return this.getPercentage();
	}
	
	public void addPercentage(int n) {
		if (this.getPercentage() + n >= 0 && this.getPercentage() + n <= 100) {
			this.percentage += n;
		}
		else if (this.getPercentage() + n < 0) {
			this.percentage = 0;
		}
		else if (this.getPercentage() + n > 0) {
			this.percentage = 100;
		}
	}
	public void setPercentage(int n) {
		if (n <= 100 && n >= 0) {
			this.percentage = n;
		}
		else if (n < 0) {
			this.percentage = 0;
		}
		else if (n > 100) {
			this.percentage = 100;
		}
	}
	
	public void setReserve(int n) {
		this.reserve = this.reserve + n;
	}
	
	public int getReserve() {
		return this.reserve;
	}
	
	public int getProductionWidth() {
		return this.prodwidth;
	}

	public boolean isMovable() {
		return false;
	}
	
	public boolean equals(Item o) {
		if(!(o instanceof Planet)) {
			return false;
		}
		Planet p = (Planet) o;
		if (this.getCenter().equals(p.getCenter()) && this.getWidth() == p.getWidth() && !(p.isMovable())) {
			return true;
		}
		return false;
	}
	
	public double distance(Planet p) {
		double x = this.getCenter().getX();
		double y = this.getCenter().getY();
		double xp = p.getCenter().getX();
		double yp = p.getCenter().getY();
		
		return Math.sqrt((y-yp)*(y-yp) + (x-xp)*(x-xp));
	}
	
	public boolean isFarEnough(Planet p, double d) {
		double hdiag1 = this.getWidth()*Math.sqrt(2);
		double hdiag2 = p.getWidth()*Math.sqrt(2);
		return (this.distance(p) >= hdiag1 + hdiag2 + d);
	}
	
	public boolean overlaps(Spaceship s) {
		Point2D c = s.getCenter();
		double w = s.getWidth();
		Point2D lowerLeft = new Point2D.Double (c.getX() - w, c.getY() - w);
		Point2D lowerRight = new Point2D.Double (c.getX() + w, c.getY() - w);
		Point2D upperLeft = new Point2D.Double (c.getX() - w, c.getY() + w);
		Point2D upperRight = new Point2D.Double (c.getX() + w, c.getY() + w);
		
		if (this.contains(lowerLeft) || this.contains(lowerRight) || this.contains(upperLeft) || this.contains(upperRight)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void draw(Graphics2D arg0) {
		Point2D pos = this.getCenter();
		int x = (int) pos.getX(), y = (int) pos.getY(), w = this.getWidth();
		arg0.setColor(Color.green);
		arg0.fillRect(x - w / 2, y - w / 2, w, w);
	}
	
	public List<Point2D> takeOffSpots(){
		int w = this.getProductionWidth();
		int n = this.getWidth()/(w+1);
		ArrayList<Point2D> spots = new ArrayList<Point2D>();
		for (int i = 0; i < n; ++i) {
			//takeoff spots along the lower side
			Point2D spot1 = new Point2D.Double(this.getCenter().getX() - this.getWidth() - 1 - w + (n*(w+1)), this.getCenter().getY() - this.getWidth() - 1 - w );
			
			//takeoff spots along the upper side
			Point2D spot2 = new Point2D.Double(this.getCenter().getX() - this.getWidth() - 1 - w + (n*(w+1)), this.getCenter().getY() + this.getWidth() + 1 + w);
			
			//takeoff spots along the left side
			Point2D spot3 = new Point2D.Double(this.getCenter().getX() - this.getWidth() - 1 - w, this.getCenter().getY() - this.getWidth() - 1 - w + (n*(w+1)));
			
			//takeoff spots along the right side
			Point2D spot4 = new Point2D.Double(this.getCenter().getX() + this.getWidth() + 1 + w, this.getCenter().getY() - this.getWidth() - 1 - w + (n*(w+1)));
			
			spots.add(spot1);
			spots.add(spot2);
			spots.add(spot3);
			spots.add(spot4);
		}
		return spots;
	}
	
	public static String squadronName() {
		StringBuffer sb = new StringBuffer();
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		for (int i = 0; i < 6; ++i) {
			Random rand = new Random();
			int r = rand.nextInt(alphabet.length());
			sb.append(alphabet.charAt(r));
		}
		return sb.toString();
	}
	
	public List<Spaceship> takeOff(Planet objective) {
		String squadronId = squadronName();
		int width = this.getProductionWidth();
		ArrayList<Spaceship> squadron = new ArrayList<Spaceship>();
		int size = (this.getReserve()*this.getPercentage())/100;
		List<Point2D> spots = this.takeOffSpots();
		if (size > spots.size()) {
			int n = (size/spots.size()) + 1;
			System.out.println("Trop de vaisseaux : " + n + " décollage nécessaires");
		}
		Iterator<Point2D> it = spots.iterator();
		int cpt = 0;
		while (it.hasNext() && cpt < size) {
			Point2D p = it.next();
			Spaceship s = new Spaceship(width, p, this.getPlayer(), objective, squadronId);
			squadron.add(s);
		}
		this.setReserve(-size);
		return squadron;
	}

}
