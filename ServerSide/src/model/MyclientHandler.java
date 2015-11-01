package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import algorithms.demo.Maze3dSearchable;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.MyMaze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.search.Astar;
import algorithms.search.Bfs;
import algorithms.search.MazeAirDistance;
import algorithms.search.MazeManhattanDistance;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import presenter.DataObj;

public class MyclientHandler  implements ClientHandler{

	private static final int ERROR          = -1;
	private static final int  GENERATE_GAME =  1;
	private static final int  SOLVE_GAME    =  2;
	private HashMap<Maze3d, Solution<Position>> mazeAndSolution;
	
	@SuppressWarnings("unchecked")
	public MyclientHandler() {
		try {
			FileInputStream fis = new FileInputStream("mazeAndSolution.gz");
			GZIPInputStream gzis = new GZIPInputStream(fis);
			BufferedInputStream in1 = new BufferedInputStream(gzis);
			ObjectInputStream in = new ObjectInputStream(in1);
			this.mazeAndSolution = (HashMap<Maze3d, Solution<Position>>) in.readObject();
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			this.mazeAndSolution = new HashMap<Maze3d, Solution<Position>>();
			
		}
	}
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
		
		
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(inFromClient));
			
			Integer problem=(Integer) in.readObject();
			
			System.out.println(problem);
			
				//1 for generate maze
				if(problem==GENERATE_GAME)
				{	String dimensions =(String) in.readObject();
					System.out.println(dimensions);
					String genAlgorithm =(String) in.readObject();
					ObjectOutputStream out =new ObjectOutputStream(new BufferedOutputStream(outToClient));
					out.writeObject(generateMaze(dimensions,genAlgorithm));
					out.flush();
				}
				//2 for solve maze
				if(problem==SOLVE_GAME)
				{
					System.out.println("hello solve game");
					String  solveAlgo=(String) in.readObject();
					System.out.println(solveAlgo);
					Maze3d maze3d=(Maze3d) in.readObject();
					maze3d.print3DMaze();
					ObjectOutputStream out =new ObjectOutputStream(new BufferedOutputStream(outToClient));
					out.writeObject(solveMaze(solveAlgo,maze3d));
					out.flush();
				}
					
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	
		
		
	}
	
	private DataObj generateMaze(String dimensions,String algo)
	{
	
				if (algo.equals("Dfs")) {
					String[] tmp = dimensions.split(",");
					int x = Integer.valueOf(tmp[0]);
					int y = Integer.valueOf(tmp[1]);
					int z = Integer.valueOf(tmp[2]);
					MyMaze3dGenerator mg = new MyMaze3dGenerator();
					Maze3d maze = mg.generate(x, y, z);
					DataObj data=new DataObj(maze, 1);
					return data;
					

				} else if (algo.equals("Simple")) {
					String[] tmp = dimensions.split(",");
					int x = Integer.valueOf(tmp[0]);
					int y = Integer.valueOf(tmp[1]);
					int z = Integer.valueOf(tmp[2]);
					SimpleMaze3dGenerator mg = new SimpleMaze3dGenerator();
					Maze3d maze = mg.generate(x, y, z);
					DataObj data=new DataObj(maze, 1);
					return data;
				
				} else {
				      return new DataObj("wrong input",ERROR);
				
				}

	} 
			
	private DataObj solveMaze(String solveAlgo,Maze3d maze3d)
	{
		Searcher<Position> searcher;
		Solution<Position> solution;
		System.out.println(solveAlgo);
		maze3d.print3DMaze();
		DataObj data=new DataObj(null, SOLVE_GAME);
		if(mazeAndSolution.containsKey(maze3d))
		{
			data.setData(mazeAndSolution.get(maze3d));
			return data;
		}
		else
		{
		
		switch (solveAlgo) {
		case "Astar AirDistance":
			searcher = new Astar<Position>(new MazeAirDistance());
			solution = searcher.search(new Maze3dSearchable(maze3d));
			 break;
		case "Astar ManhattanDistance":
			searcher = new Astar<Position>(new MazeManhattanDistance());
			solution = searcher.search(new Maze3dSearchable(maze3d));
			 break;
		case "Bfs":
			searcher = new Bfs<Position>();
			solution = searcher.search(new Maze3dSearchable(maze3d));
			break;
		default:
			data.setData("invalid algorithm");
			data.setSerialNumber(ERROR);
			return data;
		}
		}
		System.out.println("before saving to hash");
		mazeAndSolution.put(maze3d, solution);
		System.out.println("after saving to hash");
		data.setData(solution);
		return data;
		}
	
		

		
	
	@Override
	public void close()
	{
		try {
			// writing to xml file
			/*
			 * XMLEncoder xmlFile= new XMLEncoder(new BufferedOutputStream(new
			 * FileOutputStream("Properties.xml"))); xmlFile.writeObject(p);
			 * xmlFile.flush(); xmlFile.close();
			 */
			// writing to zip file the hashmap.
			FileOutputStream fos = new FileOutputStream("mazeAndSolution.gz");
			GZIPOutputStream gzos = new GZIPOutputStream(fos);
			BufferedOutputStream out1 = new BufferedOutputStream(gzos);
			ObjectOutputStream out = new ObjectOutputStream(out1);
			out.writeObject(mazeAndSolution);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
}
