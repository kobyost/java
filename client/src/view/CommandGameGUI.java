package view;

import java.util.Observable;
import org.eclipse.swt.widgets.Composite;
import presenter.Properties;

public interface CommandGameGUI {
	
	public String getGameName();

	public void generateNewGame();
	public void getGameData();
	public boolean setGameData(Object obj);
	public void playGame(Composite compositeGame);
	
	public void solveGame();
	public void displaySolveGame(Object obj);
	public void getSolutionData();
	
	public void saveGame();
	public void loadGameFile(String filePath);
	public void getLoadedGameFileData(Object obj);
	
	public void openProperties (String filePath);
	public void setProperties (Properties Properties);
	public Properties getProperties ();
	
	public void closeGame();
	public void setObservable(Observable ob);
	public void pause();
	public void exit();
		
}
