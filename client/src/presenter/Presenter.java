package presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import view.View;
import model.Model;

public class Presenter implements Observer {
	private Model model;
	private View view;
	private HashMap<String, Command> commands;

	public Presenter() {
		this.commands = new HashMap<String, Command>();
		initializeCommands();
	}

	@Override
	public void update(Observable obs, Object obj) {
		DataObj o = (DataObj) obj;
		if (obs instanceof View) {
				
				String command;
				command = getCommand((String) o.getData());
				o.setData(getParam((String) o.getData()));
				if (commands.containsKey(command))
					commands.get(command).doCommand(o);
				else
					view.display(new DataObj("wrong input", -1));
		}
		else   {
			view.display(o);
			
		}
	}

	public void setView(View view) {
		this.view = view;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Model getModel() {
		return this.model;
	}

	public View getView() {
		return this.view;
	}

	/**
	 * This method is getting The parameters for the command ,it separates the
	 * parameters from the command .
	 * 
	 * @param input
	 * @return String[]
	 */
	public String[] getParam(String input) {
		if (!input.contains("<"))
			return new String[0];
		String param = input.substring(input.indexOf('<') - 1);
		String[] tmp = param.split("(<)|(>)");
		ArrayList<String> finalParam = new ArrayList<String>();
		for (String string2 : tmp) {
			if (!string2.equals(" "))
				finalParam.add(string2);
		}
		String[] a = finalParam.stream().toArray(String[]::new);
		return a;
	}

	/**
	 * This method returns the command from the entire string that the user has
	 * entered, it seperates the command from the parameters.
	 * 
	 * @param input
	 * @return String
	 */
	public String getCommand(String input) {
		if (input.contains("<"))
			return input.substring(0, (input.indexOf('<')) - 1);
		else
			return input;
	}

	/**
	 * This method i initilizes all the commands in our hash map.
	 */
	private void initializeCommands() {
		this.commands.put("dir", new DirCommand());
		this.commands.put("generate 3d maze", new Generate3dMazeCommand());
		this.commands.put("display", new DisplayCommand());
		this.commands.put("save maze", new SaveMazeCommand());
		this.commands.put("load maze", new LoadMazeCommand());
		this.commands.put("maze size", new MazeSizeCommand());
		this.commands.put("file size", new FileSizeCommand());
		this.commands.put("display cross section by", new DisplayCrossSectionByCommand());
		this.commands.put("solve", new SolveCommand());
		this.commands.put("display solution", new DisplaySolutionCommand());
		this.commands.put("exit", new exitCommand());

	}

	public HashMap<String, Command> getCommands() {
		return this.commands;
	}

	/**
	 * This method adds a command to the hash map of commands.
	 * 
	 * @param key
	 * @param command
	 */
	public void addCommand(String key, Command command) {
		this.commands.put(key, command);
	}

	/*
	 * public void setSolution(Solution<Position> solution) {
	 * ArrayList<State<Position>> moves=solution.getSolution();
	 * ArrayList<String> array=new ArrayList<>(); for (State<Position> move :
	 * moves) { array.add(move.toString()); }
	 * 
	 * view.display(array.stream().toArray(String[]::new)); }
	 */
	/**
	 * This class is the Dir Command class which displays all the files and
	 * folders in specific path. The user command is : dir <path>
	 */
	public class DirCommand implements Command {

		@Override
		public void doCommand(DataObj o) {
			String[] params = (String[]) o.getData();

			if (params.length != 1)
				view.display(new DataObj("wrong input", o.serialNumber));
			else {
				File dir = new File(params[0]);
				if (dir.isDirectory()) {
					System.out.println("path:" + params[0]);
					String[] files = dir.list();
					view.display(new DataObj(files, o.serialNumber));
				} else
					view.display(new DataObj("The path name is not a directory", o.serialNumber));
			}

		}
	}

	/**
	 * This class is the Generate 3d MazeCommand which generates a maze . The
	 * user command is : generate 3d maze <maze name> <x,y,z> <Dfs,Simple> .
	 * (the xyz are positive numbers).
	 * 
	 */
	public class Generate3dMazeCommand implements Command, Callable<int[][][]> {

		@Override
		public void doCommand(DataObj o) {
			String[] params = (String[]) o.getData();

			if (params.length == 3) {
				model.generate3dMaze(o);
			} else
				view.display(new DataObj("wrong input", o.serialNumber));
		}

		@Override
		public int[][][] call() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/**
	 * This class is the Display Command which displays the maze, that the user
	 * has requested The user command is : display <maze name>
	 */
	public class DisplayCommand implements Command {

		@Override
		public void doCommand(DataObj o) {
			String[] params = (String[]) o.getData();
			if (params.length == 1)
				model.display(o);
			else
				view.display(new DataObj("wrong input", o.serialNumber));
		}

	}

	/**
	 * This class is the Save Maze Command Which saves the maze in its compress
	 * form on a file. The user command is : save maze <maze name> <file name>.
	 *
	 */
	public class SaveMazeCommand implements Command {

		@Override
		public void doCommand(DataObj o) {
			String[] params = (String[]) o.getData();
			if (params.length == 2)
				model.saveMaze(o);
			else
				view.display(new DataObj("wrong input", o.serialNumber));
		}

	}

	/**
	 * This class is the load maze command which loads a maze from a file and
	 * gives it a new name . The user command : load maze <file name> <maze
	 * name>.
	 */
	public class LoadMazeCommand implements Command {

		@Override
		public void doCommand(DataObj o) {
			String[] params = (String[]) o.getData();
			if (params.length == 2)
				model.loadMaze(o);
			else
				view.display(new DataObj("wrong input", o.serialNumber));
		}
	}

	/**
	 * This class is the Maze size command which returns the maze size in the
	 * memory. The user command : maze size <maze name>.
	 *
	 */
	public class MazeSizeCommand implements Command {

		@Override
		public void doCommand(DataObj o) {
			String[] params = (String[]) o.getData();
			if (params.length == 1)
				model.mazeSize(o);
			else
				view.display(new DataObj("wrong input", o.serialNumber));

		}

	}

	/**
	 * This class is the File size command which returns the maze size in the
	 * file. The user command : file size <file name>.
	 *
	 */
	public class FileSizeCommand implements Command {

		@Override
		public void doCommand(DataObj o) {
			String[] params = (String[]) o.getData();
			if (params.length == 1)
				model.fileSize(o);
			else
				view.display(new DataObj("wrong input", o.serialNumber));
		}

	}

	/**
	 * This class is the Display Cross Section by command which displays the
	 * cross section of a maze by its axis and index and name. The user command
	 * : display cross section by <x,y,z> <index> <maze name>.
	 */
	public class DisplayCrossSectionByCommand implements Command {

		@Override
		public void doCommand(DataObj o) {
			String[] params = (String[]) o.getData();
			if (params.length == 3)
				model.displayCrossSectionBy(o);
			else
				view.display(new DataObj("wrong input", o.serialNumber));

		}

	}

	/**
	 * This class is the Solve command which solves the maze and tells its been
	 * solved. The user command is : solve <maze name> <algorithm>.
	 */
	public class SolveCommand implements Command {

		@Override
		public void doCommand(DataObj o) {
			String[] params = (String[]) o.getData();
			if (params.length == 3||(params.length==2))
				model.solve(o);
				
			else
				view.display(new DataObj("wrong input", o.serialNumber));
		}

	}

	/**
	 * This class is the Display solution command which displays the steps it
	 * took to reach the solution. The user command is : display solution <maze
	 * name>.
	 */
	public class DisplaySolutionCommand implements Command {

		@Override
		public void doCommand(DataObj o) {
			String[] params = (String[]) o.getData();
			if (params.length == 1)
				model.getSolution(o);
			else
				view.display(new DataObj("wrong input", o.serialNumber));
		}

	}

	/**
	 * This class is the exit command which exits from the cli and closes all
	 * open threads and files.
	 */
	public class exitCommand implements Command {

		@Override
		public void doCommand(DataObj o) {
			model.exit();
			view.exit();

		}
	}

}
