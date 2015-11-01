package view;

import java.io.FileNotFoundException;
import java.util.Observable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import presenter.DataObj;
import presenter.ManagerProperties;
import presenter.Properties;

public class CommandMazeGame implements CommandGameGUI {

	// next commands
	public static final int ERROR 				    =-1;
	public static final int GAME_DATA_READY		    = 0;	
	public static final int PLAY_GAME 			    = 1;
	public static final int SOLVE_GAME 	            = 2;
	public static final int DISPLAY_SOLVED_GAME     = 3;
	public static final int LOAD_GAME_SUCCESSFULLY 	= 4;
	public static final int DISPLAY_LOADED_GAME 	= 5;
	public static final int EXIT 				    = 6;
	public static final int GAME_SAVED              = 7;
	
	int mazeNumber;
	Properties properties;
	Maze3d maze3d;
	Observable ob;
	MazeGame mazeGame;
	
	
	public CommandMazeGame() {
		this.properties = new Properties();
		this.mazeNumber=1;
	}
	
	
	
	public MazeGame getMazeGame() {
		return mazeGame;
	}



	public void setMazeGame(MazeGame mazeGame) {
		this.mazeGame = mazeGame;
	}



	@Override
	public void generateNewGame() {
		String cmd="generate 3d maze";
		String mazename="maze"+mazeNumber;
		String dimensions=properties.getNumOfRows()+","+properties.getNumOfColums()+","+properties.getNumOfLevels();
		String algo=properties.getGenrateAlgo();
		DataObj data = new DataObj(cmd+" "+"<"+mazename+">"+" "+"<"+dimensions+">"+" "+"<"+algo+">",GAME_DATA_READY); 
		ob.notifyObservers(data);

	}
	
	@Override
	public void getGameData() {
		String cmd="display";
		String mazename="maze"+mazeNumber;
		DataObj data=new DataObj(cmd+" "+"<"+mazename+">",PLAY_GAME);
		mazeNumber++;
		ob.notifyObservers(data);
	}
	
	@Override
	public boolean setGameData(Object obj) {
		if (obj instanceof Maze3d) {
			maze3d = (Maze3d) obj;
			return true;
		}
		return false;
	}

	@Override
	public void playGame(Composite compositeGame) {
		if (maze3d != null && compositeGame!=null) {
			
			MazeDisplayer mazePaint = new Maze2D(compositeGame, SWT.BORDER);
			FormData mazePaintFromData = new FormData();
			mazePaintFromData.top = new FormAttachment(0, 0);
			mazePaintFromData.bottom = new FormAttachment(100, 0);
			mazePaintFromData.left = new FormAttachment(0, 0);
			mazePaintFromData.right = new FormAttachment(100, 0);
			mazePaint.setLayoutData(mazePaintFromData);
			this.mazeGame = new MazeGame(mazePaint,maze3d);
			mazeGame.play();
		}
	}
	
	@Override
	public void openProperties(String filePath) {
		if (filePath!=null) {
			ManagerProperties manager = new ManagerProperties();
			try {
				properties = manager.loadFile(filePath);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}	
		}
	}
	
	@Override
	public void setProperties(Properties properties) {
		this.properties=properties;	
	}

	@Override
	public Properties getProperties() {
		return new Properties(properties);
	}
	
	@Override
	public String getGameName() {
		return "Maze Game";
	}

	@Override
	public void solveGame() {
		System.out.println("solv1");
		String cmd="solve";
		String mazename="maze"+(mazeNumber-1);
		String algorithm=properties.getSolveAlgo();
		String currentCharacterPosition=mazeGame.getCurrentCharacter().getPositionByString();
		DataObj data=new DataObj(cmd+" "+"<"+mazename+">"+" "+"<"+algorithm+">"+" "+"<"+currentCharacterPosition+">",SOLVE_GAME);
		ob.notifyObservers(data);
	}
	
	public void getSolutionData(){
		System.out.println("solv2");
		String cmd="display solution";
		String mazename="maze"+(mazeNumber-1);
		DataObj data=new DataObj(cmd+" "+"<"+mazename+"*>",DISPLAY_SOLVED_GAME);
		ob.notifyObservers(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void displaySolveGame(Object obj) {
		mazeGame.manageSolution((Solution<Position>)obj);
	}
	public void setObservable(Observable ob)
	{
		this.ob=ob;
	}
	
	public void saveGame(){
		String cmd="save maze";
		String mazename="maze"+(mazeNumber-1);
		DataObj data=new DataObj(cmd+" "+"<"+mazename+">"+" "+"<"+mazename+".mz"+">",GAME_SAVED);
		ob.notifyObservers(data);
	}
	
	@Override
	public void loadGameFile(String filePath) {
		if(filePath!=null)
		{
			int index=filePath.indexOf("maze");
			String temp=filePath.substring(index);
			String cmd="load maze";
			String filename=temp;
			String mazename="maze"+mazeNumber;
			DataObj data=new DataObj(cmd+" "+"<"+filename+">"+" "+"<"+mazename+">", LOAD_GAME_SUCCESSFULLY);
			ob.notifyObservers(data);
		}
			
	
	}
	
	@Override
	public void getLoadedGameFileData(Object obj) {
		String cmd="display";
		String mazename="maze"+(mazeNumber);
		DataObj data=new DataObj(cmd+" "+"<"+mazename+">", DISPLAY_LOADED_GAME);
		mazeNumber++;
		ob.notifyObservers(data);
	
	}
	
	
	
	
	
	
	
	@Override
	public void pause() {
		if (mazeGame!=null) {
			mazeGame.pause();
		}
	}

	@Override
	public void exit() {
		DataObj data=new DataObj("exit",EXIT);
		ob.notifyObservers(data);	
	}
	public void closeGame()
	{
		this.mazeGame.closeGame();
	}

}
