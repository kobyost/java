package view;

import java.util.Observable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public abstract class BasicWindow extends Observable implements Runnable {

	@Override
	public void notifyObservers(Object arg) {
		this.setChanged();
		super.notifyObservers(arg);
	}

	Display display;
	Shell shell;

	public BasicWindow(String title, int width, int height) {
		display = new Display();
		shell = new Shell(display);
		shell.setSize(width, height);
		shell.setText(title);
	}

	public BasicWindow(String title, String pathImage) {
		display = new Display();
		shell = new Shell(display);
		shell.setText(title);
		loadBackgroundImage(pathImage, shell);
	}

	abstract void initWidgets();

	@Override
	public void run() {

		initWidgets();

		shell.open();

		// main event loop
		while (!shell.isDisposed()) { // while window isn't closed

			// 1. read events, put then in a queue.
			// 2. dispatch the assigned listener
			if (!display.readAndDispatch()) { // if the queue is empty
				display.sleep(); // sleep until an event occurs
			}

		} // shell is disposed

		display.dispose(); // dispose OS components
	}

	public void loadBackgroundImage(String path, Shell shell) {

		try {
			Image image = new Image(shell.getDisplay(), path);
			Rectangle rect = image.getBounds();
			shell.setSize(rect.width+18, rect.height+47);
			//600x400
			shell.setMinimumSize(rect.width+27, rect.height+50);
			shell.setBackgroundImage(image);
			
			shell.addListener(SWT.Resize, new Listener() {
			
				@Override
				public void handleEvent(Event arg0) {
					ImageData data = image.getImageData();
					Image m = new Image(shell.getDisplay(),
							data.scaledTo(shell.getBounds().width-15, shell.getBounds().height-35));
					shell.setBackgroundImage(m);
				}
			});
		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Cannot load image - " + path);
			System.exit(1);
		}
	}
}
