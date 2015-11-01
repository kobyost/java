package view;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import model.MyModel;
import presenter.Presenter;

public class UIWindow extends BasicWindow {
Observable o;
GameSound sound;
	public UIWindow(String title, int width, int height) {
		super(title, width, height);
		this.o=new Observable();
		this.sound=new GameSound("toons song.wav");
		this.sound.play();
	}

	public UIWindow(String title, String path) {
		super(title, path);
		this.sound=new GameSound("toons song.wav");
		this.sound.play();
	}

	@Override
	void initWidgets() {

		shell.setLayout(new FormLayout());

		Cursor handCursor = new Cursor(display, SWT.CURSOR_HAND);
		shell.setCursor(handCursor);

		Text text = new Text(shell, SWT.CENTER | SWT.READ_ONLY);
		text.setText("Please Select Interface");

		FormData textFromData = new FormData();
		textFromData.top = new FormAttachment(0, 5);
		textFromData.bottom = new FormAttachment(20, -5);
		textFromData.left = new FormAttachment(0, 5);
		textFromData.right = new FormAttachment(100, -5);
		text.setLayoutData(textFromData);

		Font font = new Font(text.getDisplay(), new FontData("Cooper Black", 25, SWT.BOLD));
		text.setFont(font);
		// text.setForeground(new Color(display, 5, 50, 70, 100));
		text.setBackground(new Color(display, 0, 0, 0, 0));

		Button gui = new Button(shell, SWT.PUSH | SWT.TOGGLE);
		gui.setText("GUI");

		FormData guiFormData = new FormData();
		guiFormData.top = new FormAttachment(30, 10);
		guiFormData.bottom = new FormAttachment(50, -10);
		guiFormData.left = new FormAttachment(40, 10);
		guiFormData.right = new FormAttachment(60, -10);
		gui.setLayoutData(guiFormData);

		gui.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						
						CommandMazeGame game = new CommandMazeGame();
						MyViewGUI gameGUI = new MyViewGUI(" Maze Game Menu","sources/ElmerFudd.jpg",game);
						game.setObservable(gameGUI);
						MyModel m = new MyModel(3);
						Presenter p = new Presenter();
						p.setModel(m);
						p.setView(gameGUI);
						//game.addObserver(p);
						m.addObserver(p);
						gameGUI.addObserver(p);
						sound.stop();
						gameGUI.start();
					}
				}).start();
				
				shell.close();
			}
		});

		Button cli = new Button(shell, SWT.PUSH);
		cli.setText("CLI");

		FormData cliFormData = new FormData();
		cliFormData.top = new FormAttachment(60, 10);
		cliFormData.bottom = new FormAttachment(80, -10);
		cliFormData.left = new FormAttachment(40, 10);
		cliFormData.right = new FormAttachment(60, -10);
		cli.setLayoutData(cliFormData);

		cli.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				MyCliView ui = new MyCliView (new InputStreamReader(System.in), new OutputStreamWriter(System.out));
				MyModel m = new MyModel(3);
				
				Presenter p = new Presenter();
				p.setModel(m);
				p.setView(ui);
				ui.addObserver(p);
				m.addObserver(p);
				sound.stop();
				ui.start();

				shell.close();
			}
		});
	}

}
