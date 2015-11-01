package view;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;



// this is (1) the common type, and (2) a type of widget
// (1) we can switch among different MazeDisplayers
// (2) other programmers can use it naturally
public abstract class MazeDisplayer extends Canvas{
	
	// just as a stub...
	//Maze3dGenerator mg=new MyMaze3dGenerator();
	
	//Maze3d maze=mg.generate(14, 14, 14);
	//int[][] mazeData=maze.getCrossSectionByZ(this.maze.getStartPosition().getZ());
	
	int[][] mazeData=null;

	
	public MazeDisplayer(Composite parent, int style) {
		super(parent, style);
	}

	public void setMazeData(int[][] mazeData){
		this.mazeData=mazeData;
	}
	
	public abstract  boolean  setCharacterPosition(int row,int col);	
	
	public abstract void setLevel(int z);
	public abstract int getLevel();	
	
	public abstract boolean moveUp();
	public abstract  boolean moveDown();
	public abstract  boolean moveLeft();
	public  abstract boolean moveRight();
	
	public abstract void setGoalPostion(int x,int y);
	
}