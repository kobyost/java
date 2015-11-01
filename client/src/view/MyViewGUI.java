package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;

import presenter.DataObj;



public class MyViewGUI extends BasicWindow implements View {

	public static final int ERROR 				    =-1;
	public static final int GAME_DATA_READY		    = 0;	
	public static final int PLAY_GAME 			    = 1;
	public static final int SOLVE_GAME 	            = 2;
	public static final int DISPLAY_SOLVED_GAME     = 3;
	public static final int LOAD_GAME_SUCCESSFULLY 	= 4;
	public static final int DISPLAY_LOADED_GAME 	= 5;
	public static final int EXIT 				    = 6;
	public static final int GAME_SAVED              = 7;
	
	CommandGameGUI gameCommand;
	GameSound sound;
	public MyViewGUI(String title, int width, int height,CommandGameGUI gameCommand) {
		super(title, width, height);
		this.gameCommand =gameCommand;
		this.sound=new GameSound("bugs_ask.wav");
	}
	public MyViewGUI(String title, String path,CommandGameGUI gameCommand) {
		super(title, path);
		this.gameCommand =gameCommand;
	}

	@Override
	void initWidgets() {
		
		shell.setLayout(new FormLayout());
		
		Cursor handCursor = new Cursor(display, SWT.CURSOR_HAND);
		shell.setCursor(handCursor);

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem fileMenu = new MenuItem(menu, SWT.CASCADE);
		fileMenu.setText("File");
		
		MenuItem GameMenu = new MenuItem(menu, SWT.CASCADE);
		GameMenu.setText("Game");

		Menu fileSubMenu = new Menu(fileMenu);
		fileMenu.setMenu(fileSubMenu);
		
		Menu GameSubMenu = new Menu(GameMenu);
		GameMenu.setMenu(GameSubMenu);
			
		// open properties - file sub menu
		MenuItem openProperties = new MenuItem(fileSubMenu, SWT.NONE);
		openProperties.setText("Open Properties");
		
		openProperties.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				FileDialog fd=new FileDialog(shell,SWT.OPEN);
				fd.setText("Open Properties");
				fd.setFilterPath("");
				String[] filterExt = { "*.xml" };
				fd.setFilterExtensions(filterExt);
				String filePath = fd.open();	
				gameCommand.openProperties(filePath);
			}
		});
		
		
		// change properties  - file sub menu
		MenuItem changeProperties = new MenuItem(fileSubMenu, SWT.NONE);
		changeProperties.setText("Change Properties");
		
		changeProperties.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PropertiesDialog propertiesDialog = new PropertiesDialog(shell,"Properties");
				propertiesDialog.setProperties(gameCommand.getProperties());
				propertiesDialog.open("sources/whiteback.gif");
				if (propertiesDialog.getFlag()) {
					gameCommand.setProperties(propertiesDialog.getProperties());
				}
			}
		});

		// exit - file sub menu
		MenuItem exit = new MenuItem(fileSubMenu, SWT.NONE);
		exit.setText("Exit");
		
		SelectionListener selectionExitListener = new SelectionAdapter (){
			
			public void widgetSelected(SelectionEvent event) {	
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION| SWT.YES|SWT.NO);
				messageBox.setText("Exit Window");
				messageBox.setMessage("Are you sure you want to quit ?");
				int response=messageBox.open();
			if(response==SWT.YES)
				
				gameCommand.exit();
				
			};
		};
		exit.addSelectionListener(selectionExitListener);
		
		ShellListener ShellExitListener = new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent arg0) {
				gameCommand.exit();
				super.shellClosed(arg0);
			}
		};
		
		shell.addShellListener(ShellExitListener);

			
		// start new game - game sub menu
		MenuItem startGameMenu = new MenuItem(GameSubMenu, SWT.PUSH);
		startGameMenu.setText("Start new game");
		
		// load game - game sub menu
		MenuItem loadGame = new MenuItem(GameSubMenu, SWT.PUSH);
		loadGame.setText("Load game");
		
		loadGame.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				FileDialog fd=new FileDialog(shell,SWT.OPEN);
				fd.setText("Load Game");
				fd.setFilterPath("");
				String[] filterExt = { "*.mz" };
				fd.setFilterExtensions(filterExt);
				String filePath = fd.open();	
				gameCommand.loadGameFile(filePath);
			}
		});
		
		// start new game - Button 
		Button startGameButton = new Button(shell, SWT.PUSH);
		startGameButton.setText("Start New Game");
		startGameButton.setFont(new Font(display, new FontData("Cooper Black", 18, SWT.None)));
		
		FormData startGameButtonFromData = new FormData();
		startGameButtonFromData.top = new FormAttachment(45, 0);
		startGameButtonFromData.left = new FormAttachment(50, -140);
		startGameButtonFromData.right = new FormAttachment(50, 140);
		startGameButton.setLayoutData(startGameButtonFromData);

		SelectionListener startNewGameListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				gameCommand.generateNewGame();
				
				
			};
		};
	
		startGameMenu.addSelectionListener(startNewGameListener);
		startGameButton.addSelectionListener(startNewGameListener);
	}

	@Override
	public void start() {
		run();
	}

	@Override
	public void display(DataObj obj) {
		
		if (obj.getSerialNumber() == -1) {
			display.syncExec(new Runnable() {
				
				@Override
				public void run() {
					
			if (obj.getData() instanceof String) {
				
				String text = (String) obj.getData();
				
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
				messageBox.setText("Error Command");
				messageBox.setMessage(text);
				messageBox.open();
			}
					
				}
			});
			
		}
		else if(obj.getSerialNumber() == GAME_SAVED)
		{
			String text = (String) obj.getData();
			
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
			messageBox.setText("Save Information");
			messageBox.setMessage(text);
			messageBox.open();
		}
		else if (obj.getSerialNumber() == GAME_DATA_READY) {
			gameCommand.getGameData();
		}
		else if (obj.getSerialNumber() == PLAY_GAME) {
			
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
						
					if (gameCommand.setGameData(obj.getData())) {
						GameDialog gameDialog = new GameDialog(shell,gameCommand.getGameName(),gameCommand);
						gameDialog.open(1000, 640);
					}
					
				}
			
			});
		}
		
		else if (obj.getSerialNumber() == SOLVE_GAME) {
			gameCommand.getSolutionData();
		}
		
		
		else if (obj.getSerialNumber() == DISPLAY_SOLVED_GAME) {
			gameCommand.displaySolveGame(obj.getData());
		}	
		
		else if(obj.getSerialNumber()==LOAD_GAME_SUCCESSFULLY){
			gameCommand.getLoadedGameFileData(obj.getData());
			
		}
		
		else if(obj.getSerialNumber()==DISPLAY_LOADED_GAME){
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					    gameCommand.setGameData(obj.getData());
						GameDialog gameDialog = new GameDialog(shell,gameCommand.getGameName(),gameCommand);
						gameDialog.open(1000, 640);
				}
			
			});
			
		}
		
		else if (obj.getSerialNumber() == EXIT) {
			exit();
		}	
	}

	@Override
	public void notifyObservers(Object arg) {
		setChanged();
		super.notifyObservers(arg);
	}
	@Override
	public void exit() {
		display.dispose();
	}


}
