package src_basic;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.MenuDragMouseEvent;

import fr.ubordeaux.simpleUI.KeyPress;
import fr.ubordeaux.simpleUI.MouseHandler;

public class MouseListener implements MouseHandler<Item> {

	ArrayList<Item> dragList = new ArrayList<Item>();;

	public MouseListener() {
		super();
	}

	@Override
	public void mouseClicked(List<Item> arg0, KeyPress arg1) {
		System.out.println("Select " + arg0 + " " + arg1);
		for (Item testItem : arg0) {
			System.out.println("Mouse click " + testItem);
		}
	}

	@Override
	public void mouseDrag(List<Item> arg0, KeyPress arg1) {
		dragList = new ArrayList<Item>(arg0);
		System.out.println("Drag :" + dragList);
	}

	@Override
	public void mouseDragging(List<Item> arg0, KeyPress arg1) {
		if (!arg0.isEmpty())
			System.out.println("Dragging :" + arg0);
		
	}

	@Override
	public void mouseDrop(List<Item> arg0, KeyPress arg1) {
		System.out.println("Drag& Drop :" + dragList + " => " + arg0 + " using " + arg1.toString());
		for (Item item : dragList) {
			if(item instanceof Spaceship && item.getPlayer() == 1) {
				try{if(!(arg0.isEmpty()) && (arg0.get(0) instanceof Planet))
					item.setObjective(arg0.get(0));
				}catch(IndexOutOfBoundsException e){System.out.println("Empty destination");}
			}
			else if (item instanceof Planet && item.getPlayer() == 1) {
				Planet p = (Planet) item;
				try{if(!(arg0.isEmpty()) && (arg0.get(0) instanceof Planet)) {
					Planet o = (Planet) arg0.get(0);
					o.takeOff(p);
					}
				}catch(IndexOutOfBoundsException e){System.out.println("Empty destination");}
				
			}
		}
	}

	@Override
	public void mouseOver(List<Item> arg0, KeyPress arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(List<Item> arg0, KeyPress arg1, int arg2) {
		System.out.println(arg0 + " using " + arg1.toString() + " wheel rotate " + arg2);
		if (!(arg0.isEmpty()) && arg0.get(0) instanceof Planet && arg0.get(0).getPlayer() == 1) {
			Planet p = (Planet) arg0.get(0);
			if (arg1 == KeyPress.UNKNOWN) {
				//add or substract 1 to the percentage of spaceships to be deployed
				p.addPercentage(arg2*(-1));
			}
			if (arg1 == KeyPress.SHIFT) {
				//add or substract 10 to the same variable
				p.addPercentage(arg2*(-10));
			}
			if (arg1 == KeyPress.CRTL) {
				//add or substract the percentage corresponding to half a squadron
				//(or the maximum possible if there are not enough ships in the reserve
				int r = p.getReserve();
				int halfsquadron = p.takeOffSpots().size()/2;
				p.addPercentage(((halfsquadron/r)+1)*100);
			}
			if (arg1 == KeyPress.ALTGR) {
				//adjusts the percentage to correspond to exactly one squadron
				int r = p.getReserve();
				int squadron = p.takeOffSpots().size();
				p.setPercentage(((squadron/r)+1)*100);
				
			}
		}
	}

}
