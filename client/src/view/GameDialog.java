package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class GameDialog extends BasicDialog {

	CommandGameGUI gameCommand;
	GameSound sound;
	public GameDialog(Shell shell, String text,CommandGameGUI gameCommand) {
		super(shell, text);
		this.gameCommand=gameCommand;
		this.sound=new GameSound("bugs_goodbye.wav");
	}

	@Override
	void initWidgets(Shell dialog) {
		
		dialog.setLayout(new FormLayout());
		
		RowLayout vertical = new RowLayout();
		vertical.type = SWT.VERTICAL;
		
		Cursor handCursor = new Cursor(dialog.getDisplay(), SWT.CURSOR_HAND);
		dialog.setCursor(handCursor);
		
		dialog.setMinimumSize(1000, 640);
		
		Composite leftPart = new Composite(dialog, 0);

		FormData leftPartFormData = new FormData();
		leftPartFormData.top = new FormAttachment(0, 7);
		leftPartFormData.bottom = new FormAttachment(100, -7);
		leftPartFormData.left = new FormAttachment(0, 7);
		leftPart.setLayoutData(leftPartFormData);

		leftPart.setLayout(new FormLayout());	
		
		Button saveMaze = new Button(leftPart, SWT.CENTER);
		saveMaze.setText("Save Maze");
		
		FormData saveMazeFormData = new FormData();
		saveMazeFormData.top = new FormAttachment(0, 0);
		saveMazeFormData.left = new FormAttachment(0, 0);
		saveMazeFormData.right = new FormAttachment(100, 0);
		saveMaze.setLayoutData(saveMazeFormData);
		saveMaze.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				gameCommand.saveGame();
				saveMaze.setEnabled(false);
			}			
		});

		Button solve = new Button(leftPart, SWT.CENTER);
		solve.setText("Solve Maze");
		
		FormData SolveFormData = new FormData();
		SolveFormData.top = new FormAttachment(saveMaze, 7);
		SolveFormData.left = new FormAttachment(0, 0);
		saveMazeFormData.right = new FormAttachment(100, 0);
		solve.setLayoutData(SolveFormData);
		
		solve.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				gameCommand.pause();
				solve.setEnabled(false);
				saveMaze.setEnabled(false);
				gameCommand.solveGame();
			}			
		});
		
		Button close = new Button(leftPart, SWT.CENTER);
		close.setText("Close");
		
		FormData closeFormData = new FormData();
		closeFormData.top = new FormAttachment(95, 0);
		closeFormData.left = new FormAttachment(0, 0);
		closeFormData.right = new FormAttachment(100, 0);
		close.setLayoutData(closeFormData);
		
		close.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
				MessageBox messageBox = new MessageBox(dialog, SWT.ICON_QUESTION| SWT.YES|SWT.NO);
				messageBox.setText("Exit Window");
				messageBox.setMessage("Are you sure you want to quit ?");
				int response=messageBox.open();
			if(response==SWT.YES)
			{
				//sound.play();
				 gameCommand.closeGame();
				  dialog.close();	
			}
			}			
		});
		*/		gameCommand.closeGame();
				dialog.dispose();
			}
			});	
		/*
		ShellListener ShellExitListener = new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent arg0) {
				gameCommand.closeGame();
				dialog.dispose();
				super.shellClosed(arg0);
			}
		};
		
		dialog.addShellListener(ShellExitListener);
		*/
		dialog.addListener(SWT.Close, new Listener() {

			@Override
			public void handleEvent(org.eclipse.swt.widgets.Event arg0) {
				int style = SWT.ICON_QUESTION | SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox(dialog, style);
				messageBox.setText("Information");
				messageBox.setMessage("Are you sure you want to exit?");
				if(messageBox.open() == SWT.YES)
				{
					sound.play();
					gameCommand.closeGame();
					dialog.dispose();
				}
				else
					arg0.doit=false;
				
			}
		    });
		
		
		
		
		Composite rightPart = new Composite(dialog, 0);

		FormData rightPartFormData = new FormData();
		rightPartFormData.top = new FormAttachment(0, 7);
		rightPartFormData.bottom = new FormAttachment(100, -7);
		rightPartFormData.left = new FormAttachment(leftPart, 7);
		rightPartFormData.right = new FormAttachment(100, -7);
		rightPart.setLayoutData(rightPartFormData);
		
		rightPart.setLayout(new FormLayout());
		
		gameCommand.playGame(rightPart);
		
	}

}
