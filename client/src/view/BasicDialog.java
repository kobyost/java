package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public abstract class BasicDialog extends Dialog {

	String text;

	public BasicDialog(Shell shell, String text) {
		super(shell);
		this.text = text;
	}

	abstract void initWidgets(Shell dialog);
    
	public void open(String path) {

		Shell parent = getParent();

		Shell dialog = new Shell(parent, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL); //SWT.DIALOG_TRIM
		dialog.setText(text);
		
		loadBackgroundImage(path,dialog);
		
		initWidgets(dialog);
		
		dialog.open();

		Display display = parent.getDisplay();

		while (!dialog.isDisposed()) {

			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public void open(int width, int height) {

		Shell parent = getParent();

		Shell dialog = new Shell(parent, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL); //SWT.DIALOG_TRIM
		dialog.setText(text);
		dialog.setSize(width, height);
		
		initWidgets(dialog);
		
		dialog.open();

		Display display = parent.getDisplay();

		while (!dialog.isDisposed()) {

			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	
	public void loadBackgroundImage(String path, Shell shell) {

		try {
			Image image = new Image(shell.getDisplay(), path);
			Rectangle rect = image.getBounds();
			shell.setSize(rect.width+20, rect.height+46);
			shell.setMinimumSize(rect.width+20, rect.height+46);
			shell.setBackgroundImage(image);

			shell.addListener(SWT.Resize, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					ImageData data = image.getImageData();
					Image m = new Image(shell.getDisplay(),
							data.scaledTo(shell.getBounds().width-20, shell.getBounds().height-46));
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
