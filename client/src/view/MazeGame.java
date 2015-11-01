package view;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.MessageBox;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import algorithms.search.State;

public class MazeGame {
	
	Maze3d maze3d;
	Position startCharacter;
	Position goalCharacter;
	Position currentCharacter;
	MazeDisplayer mazeDisplayer;
	KeyListener keyListener=null;
	GameSound sound;
	Timer timer;
	TimerTask task;
	boolean flag=false;
	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Maze3d getMaze3d() {
		return maze3d;
		
	}

	public void setMaze3d(Maze3d maze3d) {
		this.maze3d = maze3d;
	}

	public MazeGame(MazeDisplayer mazeDisplayer, Maze3d maze3d) {
		mazeDisplayer.setFocus();
		this.sound=new GameSound("bugs_ask.wav");
		this.maze3d = maze3d;
		this.startCharacter = maze3d.getStartPosition();
		this.currentCharacter=new Position(startCharacter);
		this.goalCharacter=maze3d.getGoalPosition();
		this.mazeDisplayer = mazeDisplayer;
		//croos by floor
		this.mazeDisplayer.setMazeData(maze3d.getCrossSectionByZ(startCharacter.getZ()));
		this.mazeDisplayer.setLevel(startCharacter.getZ());
		//set startCharacter position
		this.mazeDisplayer.setCharacterPosition(startCharacter.getX(),startCharacter.getY());
		if(goalCharacter.getZ()==this.mazeDisplayer.getLevel())
			this.mazeDisplayer.setGoalPostion(goalCharacter.getX(), goalCharacter.getY());
		else
			this.mazeDisplayer.setGoalPostion(-1, -1);
	}
	/*
	public void MarkLadders()
	{
		for (int z = 0; z < array.length; z++) {
			for (int x = 0; x < array.length; x++) {
				for (int y = 0; y < array.length; y++) {
					
				}
			}
		}
	}
*/
	public void play() {
		//maze3d.print3DMaze();
		keyListener = new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.keyCode;

				switch (keyCode) {
				
				case SWT.ARROW_UP:
					if(	mazeDisplayer.moveUp())
						currentCharacter.setX(currentCharacter.getX()-1);
					
					if(currentCharacter.equals(maze3d.getGoalPosition()))
					{
							win();
					}
					
					break;
					
					
				case SWT.ARROW_DOWN:
					if(mazeDisplayer.moveDown())
						currentCharacter.setX(currentCharacter.getX()+1);
					
					if(currentCharacter.equals(maze3d.getGoalPosition()))
					{
						win();
					}
					break;
					
					
				case SWT.ARROW_LEFT:
					if(mazeDisplayer.moveLeft())
						currentCharacter.setY(currentCharacter.getY()-1);
					
					if(currentCharacter.equals(maze3d.getGoalPosition()))
					{
						win();
					}
					break;
					
					
				case SWT.ARROW_RIGHT:
					if(mazeDisplayer.moveRight())
						currentCharacter.setY(currentCharacter.getY()+1);
					
						if(currentCharacter.equals(maze3d.getGoalPosition()))
						{
							win();
						}
					break;
					
					
				case SWT.PAGE_UP:
					if (maze3d.validPosition(currentCharacter.getX(), currentCharacter.getY(), currentCharacter.getZ()+1)&&(!maze3d.thereIsAWall(currentCharacter.getX(),currentCharacter.getY(), currentCharacter.getZ()+1))) {
						currentCharacter.setZ(currentCharacter.getZ()+1);
						mazeDisplayer.setLevel(currentCharacter.getZ());
						if(mazeDisplayer.getLevel()!=goalCharacter.getZ())
							mazeDisplayer.setGoalPostion(-1,-1);
						else
							mazeDisplayer.setGoalPostion(goalCharacter.getX(), goalCharacter.getY());
						mazeDisplayer.setMazeData(maze3d.getCrossSectionByZ(currentCharacter.getZ()));
						mazeDisplayer.redraw();
						if(currentCharacter.equals(maze3d.getGoalPosition()))
						{
							win();
						}
					}  
					else
						System.out.println("cant go up");
					break;
					
					
				case SWT.PAGE_DOWN:
					if (maze3d.validPosition(currentCharacter.getX(), currentCharacter.getY(), currentCharacter.getZ()-1)&&(!maze3d.thereIsAWall(currentCharacter.getX(),currentCharacter.getY(), currentCharacter.getZ()-1))) {
						currentCharacter.setZ(currentCharacter.getZ()-1);
						mazeDisplayer.setLevel(currentCharacter.getZ());
						if(mazeDisplayer.getLevel()!=goalCharacter.getZ())
							mazeDisplayer.setGoalPostion(-1,-1);
						else
							mazeDisplayer.setGoalPostion(goalCharacter.getX(), goalCharacter.getY());
						mazeDisplayer.setMazeData(maze3d.getCrossSectionByZ(currentCharacter.getZ()));
						mazeDisplayer.redraw();
						if(currentCharacter.equals(maze3d.getGoalPosition()))
						{
							win();
						}
					}  
					else
						System.out.println("cant go down");
					break;
					
					
				default:
					break;
				}
			}
		};
		
		mazeDisplayer.addKeyListener(keyListener);
	}
	
	public void pause() {
		mazeDisplayer.removeKeyListener(keyListener);
	}
	public void win()
	{
		//win picture
		/*
		EndWindow endWindow=new EndWindow(this.mazeDisplayer.getShell(), "Game Over");
		endWindow.open("sources/elmaer won.jpg");
		*/
		//win message
		MessageBox messageBox = new MessageBox(this.mazeDisplayer.getShell(), SWT.ICON_INFORMATION|SWT.OK);
		messageBox.setMessage("Congratulations you won !!!");
		messageBox.setText("END GAME");
		int response = messageBox.open();
		
		if (response == SWT.OK)
			sound.play();
			//this.mazeDisplayer.getShell().close();
			
	}
	
	public Position getCurrentCharacter(){
		return this.currentCharacter;
	}
	
	
	public void manageSolution(Solution<Position> solution){
		displaySolution(solution);
	}
	
	private void displaySolution(Solution<Position> s){

		ArrayList<State<Position>> solution = s.getSolution();		
		 timer=new Timer();
		task=new TimerTask() {
			
			int i =1;
			
			
			@Override
			public void run() {
				setFlag(true);
				mazeDisplayer.getDisplay().syncExec(new Runnable() {
					@Override
					public void run() {

						if (currentCharacter.getZ()<solution.get(i).getState().getZ()) {;
							currentCharacter.setZ(currentCharacter.getZ()+1);
							mazeDisplayer.setMazeData(maze3d.getCrossSectionByZ(currentCharacter.getZ()));
							mazeDisplayer.setLevel(currentCharacter.getZ());
							
							if(mazeDisplayer.getLevel()!=goalCharacter.getZ())
								mazeDisplayer.setGoalPostion(-1,-1);
							else
								mazeDisplayer.setGoalPostion(goalCharacter.getX(), goalCharacter.getY());
							mazeDisplayer.redraw();
						}
						else if (currentCharacter.getZ()>solution.get(i).getState().getZ()) {
							//D
							currentCharacter.setZ(currentCharacter.getZ()-1);
							mazeDisplayer.setMazeData(maze3d.getCrossSectionByZ(currentCharacter.getZ()));
							mazeDisplayer.setLevel(currentCharacter.getZ());
							if (currentCharacter.getZ()==maze3d.getGoalPosition().getZ()) {
								mazeDisplayer.setGoalPostion(maze3d.getGoalPosition().getX(), maze3d.getGoalPosition().getY());
							}
							else {
								mazeDisplayer.setGoalPostion(-1,-1);
							}
							mazeDisplayer.redraw();
						}
						else {
							currentCharacter.setX(solution.get(i).getState().getX());
							currentCharacter.setY(solution.get(i).getState().getY());
							mazeDisplayer.setCharacterPosition(currentCharacter.getX(), currentCharacter.getY());
							
						}
						
					}
				});
				i++;
				if (solution.size()==i) {
					task.cancel();
					timer.cancel();
					mazeDisplayer.getDisplay().syncExec(new Runnable() {
						
						@Override
						public void run() {
							win();
							
						}
					});
				}
				
			}
		};				
		timer.scheduleAtFixedRate(task, 0, 120);	
		
	
		
	}
	public void closeGame()
	{
		if(task!=null)
			task.cancel();
		if(timer!=null)
			timer.cancel();	
	}

}
