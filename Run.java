package src_basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import fr.ubordeaux.simpleUI.Application;
import fr.ubordeaux.simpleUI.ApplicationRunnable;
import fr.ubordeaux.simpleUI.Arena;
import fr.ubordeaux.simpleUI.TimerRunnable;
import fr.ubordeaux.simpleUI.TimerTask;

public class Run implements ApplicationRunnable<Item> {

	private int width;
	private int height;

	public Run(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void run(final Arena<Item> arg0, Collection<? extends Item> arg1, ArrayList<RandomIA> players) {
		MouseListener mouseHandler = new MouseListener();
		ArrayList<Planet> p = new ArrayList<Planet>();
		for (Item item : arg1) {
			if (item instanceof Planet) {
				Planet planet = (Planet) item;
				p.add(planet);
			}
		}

		/*
		 * We build the graphical interface by adding the graphical component
		 * corresponding to the Arena - by calling createComponent - to a JFrame.
		 */
		final JFrame frame = new JFrame("Test Arena");

		/*
		 * This is our KeyHandler that will be called by the Arena in case of key events
		 */
		KeyListener keyListener = new KeyListener(frame);

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(arg0.createComponent(this.width, this.height, mouseHandler, keyListener));
		frame.pack();
		frame.setVisible(true);

		/*
		 * We initially draw the component
		 */
		arg0.refresh();

		/*
		 * We ask the Application to call the following run function every seconds. This
		 * method just refresh the component.
		 */
		Application.timer(100, new TimerRunnable() {

			public void run(TimerTask timerTask) {
				arg0.refresh();
				for(RandomIA pl : players) {
					pl.act(p);
				}
				for (Item item : arg1) {
					if(item instanceof Spaceship) {
						Spaceship s = (Spaceship) item;
						s.move(p);
					}
				}
			}

		});
		Application.timer(1000, new TimerRunnable() {
			public void run(TimerTask timerTask) {
				arg0.refresh();
				for (Planet planet : p) {
					planet.setReserve(1);
				}
			}
		});
	}

	@Override
	public void run(Arena<Item> arenaModel, Collection<? extends Item> itemCollection) {
		// TODO Auto-generated method stub
		
	}

}

