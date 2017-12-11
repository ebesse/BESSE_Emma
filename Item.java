package src_basic;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.List;

public abstract class Item {
	
	private int width;
	private Point2D center;
	private int player;
	
	public Item(int w, Point2D c, int pl) {
		this.width = w;
		this.center.setLocation(c);
		this.player = pl;
	}
	
	public abstract void draw(Graphics2D arg0);
	
	public abstract boolean isMovable();
	
	public void move(List<Planet> pl) {}
	
	public int getWidth() {
		return this.width;
	}
	
	public Point2D getCenter() {
		return this.center;
	}
	
	public int getPlayer() {
		return this.player;
	}
	
	public void setPlayer(int pl) {
		this.player = pl;
	}
	
	public boolean contains(Point2D p) {
		if (p.getX() < this.center.getX() + this.width && p.getX() > this.center.getX() - this.width) {
			if (p.getY() < this.center.getY() + this.width && p.getY() > this.center.getY() - this.width) {
				return true;
			}
			return false;
		}
		return false;
	}
	
    public boolean contains(Item o) {
		
		if (!(this.contains(o.getCenter()))) {
			return false;
		}
		
		int wp = this.getWidth();
		double xp = this.getCenter().getX();
		double yp = this.getCenter().getY();
		int wo = o.getWidth();
		double xo = o.getCenter().getX();
		double yo = o.getCenter().getY();
		
		return (xo-wo >= xp-wp && xo+wo <= xp+wp && yo-wo >= yp-wp && yo+wo <= yp+wp && wo < wp);
		
	}
	
	public void setReserve(int n) {}
	
	public int getReserve() {return -1;}

	public void setObjective(Item item) {}

}
