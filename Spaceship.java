package src_basic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Spaceship extends Item{
	
	private Item objective;
	private String squadronId;
	
	public Spaceship(int w, Point2D c, int pl, Planet p, String squadronId) {
		super(w, c, pl);
		objective = p;
		this.squadronId = squadronId;
	}
	
	public String getSquadronId() {
		return this.squadronId;
	}
	
	public boolean isMovable() {
		return true;
	}
	
	public Item getObjective() {
		return this.objective;
	}
	
	public void setObjective(Item p) {
		this.objective = p;
	}
	
	public boolean equals(Item o) {
		if(!(o instanceof Spaceship)) {
			return false;
		}
		Spaceship s = (Spaceship) o;
		if(this.getCenter().equals(s.getCenter()) && this.getWidth() == s.getWidth()) {
			return true;
		}
		return false;
	}
	
	@Override
    public boolean contains(Point2D p) {
        double w = getWidth() / 2;
        return (this.getCenter().getX() - w <= p.getX() && p.getX() <= this.getCenter().getX() + w)
                && (this.getCenter().getY() - w <= p.getY() && p.getY() <= this.getCenter().getY() + w);
    }

	public void partialMove(Point2D p) {
		
		double[] t = baseTrajectory(p);

		if (!p.equals(this.getCenter())) {
			double newx = getCenter().getX();
			double newy = getCenter().getY();
			if (newx > this.getObjective().getCenter().getX()) {
				newx--;
			} else {
				newx++;
			}
			newy = t[0]*newx + t[1];
			this.getCenter().setLocation(newx, newy);
			
		}
	}
	
	public double[] baseTrajectory(Point2D o) {
		Point2D c = this.getCenter();
		
		 // we want a and b, ax + b = y being the equation of the line going through c and o
		
		double xc = c.getX();
		double yc = c.getY();
		double xo = o.getX();
		double yo = o.getY();
		
		double a = (yc - yo)/(xc - xo);
		double b = yc - a*xc;
		
		double[] t = new double[2];
		t[0] = a;
		t[1] = b;
		
		return t;
	}
	
	public boolean intersectVertical(double[] t, double x, double ymin, double ymax) {
		double y = t[0]*x + t[1];
		if (y <= ymax && y >= ymin) {
			return true;
		}
		return false;
	}
	
	public boolean intersectHorizontal(double[] t, double y, double xmin, double xmax) {
		double x = (y - t[1])/t[0];
		if (x <= xmax && x >= xmin) {
			return true;
		}
		return false;
	}
	
	
	
	public boolean checkCollision(Planet p) {
		
		double xs = this.getCenter().getX();
		double ys = this.getCenter().getY();
		
		
		double[] t = this.baseTrajectory(this.getObjective().getCenter());
		double a = t[0];
		double b = t[1];
		
		Point2D c = p.getCenter();
		double w = p.getWidth();
		
		double xp = c.getX();
		double yp = c.getY();
		
		//we check if the ship's trajectory crosses one of the planets sides
		
		//if the ship is on the left of the center of the planet :
		
		if(xs < xp){
			if(intersectVertical(t, xp-w, yp-w, yp+w)) {
				return true;
			}
		}
		
		//if the ship is above the center of the planet :
		
		if(ys > yp) {
			if(intersectHorizontal(t, yp+w, xp-w, xp+w)) {
				return true;
			}
		}
		
		//if the ship is on the right of the center of the planet :
		
		if(xs > xp) {
			if(intersectVertical(t, xp+w, yp-w, yp+w)) {
				return true;
			}
		}
		
		//if the ship is under the center of the planet :
		
		if(ys < yp) {
			if(intersectHorizontal(t, yp-w, xp-w, xp+w)) {
				return true;
			}
		}
		
		//If the function didn't return at this point, the ship's trajectory doesn't cross any of the planet's sides
		
		return false;
		
	}
	
    public void wayAround(Planet p, double[] t) {
		
		double xs = this.getCenter().getX();
		double ys = this.getCenter().getY();
		double xp = p.getCenter().getX();
		double yp = p.getCenter().getY();
		double w = p.getWidth();
		//the half diagonal of a square is equal to side*sqrt(2)/2. Here,  this.getWidth() returns half the side 
		//of the current object, so :
		double hdiag = this.getWidth()*Math.sqrt(2);
		
		while(checkCollision(p)) {
			//if we consider the center of the current planet as the center of a system :
			
			//if the ship is in the lower left quarter :
			
		    if (xs <= xp && ys <= yp) {
		    	if (t[0] >= 0) {
			        if (intersectHorizontal(t, yp-w, xp-w, xp+w)) {
				        Point2D o = new Point2D.Double(xp + w + hdiag, yp - w - hdiag);
				        partialMove(o);
			        }
			        else if (intersectVertical(t, xp-w, yp-w, yp+w)) {
			    	    Point2D o = new Point2D.Double(xp - w - hdiag, yp + w + hdiag);
				        partialMove(o);
			        }
		        }
		    	else if (t[0]< 0) {
		    		Point2D o = new Point2D.Double(xp - w - hdiag, yp - w - hdiag);
		    		partialMove(o);
		    	}
		    }
		    
		    //upper left quarter :
		    
		    if (xs <= xp && ys >= yp) {
		    	if (t[0] <= 0) {
		    	    if (intersectHorizontal(t, yp+w, xp-w, xp+w)) {
		    		    Point2D o = new Point2D.Double(xp + w + hdiag, yp + w + hdiag);
		    		    partialMove(o);
		    	    }
		    	    else if (intersectVertical(t, xp-w, yp-w, yp+w)) {
		    		    Point2D o = new Point2D.Double(xp - w - hdiag, yp - w - hdiag);
		    		    partialMove(o);
		    	    }
		        }
		    	else if (t[0] > 0) {
		    		Point2D o = new Point2D.Double(xp - w - hdiag, yp + w + hdiag);
		    		partialMove(o);
		    	}
		    }
		    
		    //upper right quarter :
		    
		    if (xs >= xp && ys >= yp) {
		    	if (t[0] >= 0) {
		    	    if (intersectHorizontal(t, yp+w, xp-w, xp+w)) {
		    		    Point2D o = new Point2D.Double(xp - w - hdiag, yp + w + hdiag);
		    		    partialMove(o);
		    	    }
		    	    else if (intersectVertical(t, xp+w, yp-w, yp+w)) {
		    		    Point2D o = new Point2D.Double(xp + w + hdiag, yp - w - hdiag);
		    		    partialMove(o);
		    	    }
		        }
		    	else if (t[0] < 0) {
		    		Point2D o = new Point2D.Double(xp + w + hdiag, yp + w + hdiag);
		    		partialMove(o);
		    	}
		    }
		    
		    //lower right quarter :
		    
		    if (xs >= xp && ys <= yp) {
		    	if (t[0] <= 0) {
		    	    if (intersectHorizontal(t, yp-w, xp-w, xp+w)) {
		    		    Point2D o = new Point2D.Double(xp - w - hdiag, yp - w - hdiag);
		    		    partialMove(o);
		    	    }
		    	    else if (intersectVertical(t, xp+w, yp-w, yp+w)) {
		    		    Point2D o = new Point2D.Double(xp + w + hdiag, yp + w + hdiag);
		    		    partialMove(o);
		    	    }
		        }
		    	else if (t[0] > 0) {
		    		Point2D o = new Point2D.Double(xp + w + hdiag, yp + w + hdiag);
		    	}
		    }  
		}		
	}
	
	public Planet nextCollision(List<Planet> pl) {
		
		Iterator<Planet> it = pl.iterator();
		int ind = 0;
		int indkeep = 0;
		Point2D pkeep = this.getObjective().getCenter();
		
		if (this.getCenter().getX() <= this.getObjective().getCenter().getX()) {
			while (it.hasNext()) {
				Planet p = it.next();
				if (p.getCenter().getX() < pkeep.getX()) {
					indkeep = ind;
					pkeep.setLocation(p.getCenter().getX(), p.getCenter().getY());
				}
				ind += 1;
			}
		}
		else {
			while (it.hasNext()) {
				Planet p = it.next();
				if (p.getCenter().getX() > pkeep.getX()) {
					indkeep = ind;
					pkeep.setLocation(p.getCenter().getX(), p.getCenter().getY());
				}
				ind += 1;
			}
		}
		Planet p = pl.get(indkeep);
		return p;
		
	}
	
	public void move(List<Planet> pl) {
				
		double left = Math.min(this.getCenter().getX(), this.getObjective().getCenter().getX());
		double right = Math.max(this.getCenter().getX(), this.getObjective().getCenter().getX());
		double up = Math.max(this.getCenter().getY(), this.getObjective().getCenter().getY());
		double bottom = Math.min(this.getCenter().getY(), this.getObjective().getCenter().getY());
		
		if (!(this.getObjective().contains(this))) {
			
			double[] t = this.baseTrajectory(this.getObjective().getCenter());
			ArrayList<Planet> l2 = new ArrayList<Planet>();
			Iterator<Planet> it = pl.iterator();
			
			while (it.hasNext()) {
				Planet p = it.next();
				if (p.getCenter().getX() <= right && p.getCenter().getX() >= left && p.getCenter().getY() <= up && this.getCenter().getY() >= bottom) {
					if (checkCollision(p)) {
						l2.add(p);
					}
				}
				
			}
			if (l2.isEmpty() || (l2.size() == 1 && l2.get(0).equals(this.getObjective()))) {
				partialMove(this.getObjective().getCenter());
			}
			Planet p = nextCollision(l2);
			wayAround(p, t);
			
		} else {
			land();
		}
			
	}
	
	public void land() {
		if (this.getObjective() instanceof Planet) {
			Planet p = (Planet) this.getObjective();
			if (this.getObjective().getPlayer() == this.getPlayer()) {
				this.getObjective().setReserve(1);
			}
			else if (this.getObjective().getPlayer() == 0) {
				this.getObjective().setPlayer(this.getPlayer());
			}
			else {
				if (p.getReserve() == 0) {
					this.getObjective().setPlayer(this.getPlayer());
				}
				else {
					this.getObjective().setReserve(-1);
				}
			}
		}
		else {
			throw new RuntimeException("Non valid objective");
		}
		
	}
	
	@Override
	public void draw(Graphics2D arg0) {
		if (!(this.getObjective().contains(this))) {
			Point2D pos = this.getCenter();
			int x = (int) pos.getX(), y = (int) pos.getY(), w = this.getWidth();
			arg0.setColor(Color.blue);
			arg0.fillRect(x - w / 2, y - w / 2, w, w);
		}
	}

	

}
