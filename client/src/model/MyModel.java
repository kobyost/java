package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;
import presenter.DataObj;

public class MyModel extends Observable implements Model {
	// Hash map stores all the mazes.
	private HashMap<String, Maze3d> mazes;
	// Hash map stores all solutions.
	private HashMap<String, Solution<Position>> solutions;
	private Socket theServer; 
	private ExecutorService executor;

	public MyModel(int numOfThreads) {
		this.mazes = new HashMap<String, Maze3d>();
		this.solutions = new HashMap<String, Solution<Position>>();
		this.theServer=null;
		if (numOfThreads > 1)
			this.executor = Executors.newFixedThreadPool(numOfThreads);
		else
			this.executor = Executors.newFixedThreadPool(3);

		
	}

	@Override
	public void notifyObservers(Object arg) {
		this.setChanged();
		super.notifyObservers(arg);
	}

	/**
	 * This method returns the hash map of the mazes.
	 * 
	 * @return HashMap
	 */
	public HashMap<String, Maze3d> getMazes() {
		return this.mazes;
	}

	/**
	 * This method returns the presenter
	 * 
	 * @return presenter
	 */
	public void generate3dMaze(DataObj o) {
		String[] param = (String[]) o.getData();
		String name = param[0];
		String dimensions = param[1];
		String algo = param[2];

		this.executor.submit(new Runnable() {

			@Override
			public void run() {
				if (!(mazes.containsKey(name))) {
					// connect to server

					try {
					    theServer = new Socket("localhost", 5400);
						ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(theServer.getOutputStream()));
						out.writeObject(1);
						out.writeObject(dimensions);
						out.writeObject(algo);
						out.flush();
						ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(theServer.getInputStream()));
						DataObj data=(DataObj) in.readObject();
						Maze3d maze3d=(Maze3d) data.getData();
						
						if (maze3d != null && maze3d instanceof Maze3d) {
							mazes.put(name, maze3d);
							o.setData("maze " + "<" + name + "> " + "is ready");
							notifyObservers(o);
							closeServerSocket();

						} else {
							o.setData("Error data lost from server");
							o.setSerialNumber(-1);
							notifyObservers(o);
							closeServerSocket();
						}
					} catch ( IOException | ClassNotFoundException e) {
						o.setData("Could not establish connection with server");
						o.setSerialNumber(-1);
						notifyObservers(o);

					}

				}

				else {
					o.setData("This maze name allready exist ,please pick different name");
					o.setSerialNumber(-1);
					notifyObservers(o);					

				}
			}
		});

				

			
		

	}

	@Override
	public void display(DataObj o) {
		String[] param = (String[]) o.getData();
		String mazeName = param[0];
		if (mazes.containsKey(mazeName)) {
			o.setData(mazes.get(mazeName));
			notifyObservers(o);
		} else {
			o.setData("This maze name does not exist");
			o.setSerialNumber(-1);
			notifyObservers(o);
		}

	}

	@Override
	public void saveMaze(DataObj o) {
		String[] param = (String[]) o.getData();
		String mazeName = param[0];
		String fileName = param[1];
		if (mazes.containsKey(mazeName)) {
			try {
				FileOutputStream theFile = new FileOutputStream(fileName);
				OutputStream out = new MyCompressorOutputStream(theFile);
				Maze3d maze = mazes.get(mazeName);
				out.write(maze.toByteArray().length);
				out.write(maze.toByteArray());
				out.flush();
				out.close();
				o.setData("<" + mazeName + "> was saved to file: " + "<" + fileName + ">");
				notifyObservers(o);
			} catch (FileNotFoundException e) {
				o.setData(e.toString());
				notifyObservers(o);
				e.printStackTrace();
			} catch (IOException e) {
				o.setData(e.toString());
				notifyObservers(o);
				e.printStackTrace();
			}

		} else {
			o.setData("This maze name does not exist");
			o.setSerialNumber(-1);
			notifyObservers(o);
		}

	}

	@Override
	public void loadMaze(DataObj o) {
		String[] param = (String[]) o.getData();
		String fileName = param[0];
		String newMazeName = param[1];

		if (!(mazes.containsKey(newMazeName))) {
			try {
				InputStream in = new MyDecompressorInputStream(new FileInputStream(fileName));
				int mazesize = in.read();
				byte b[] = new byte[mazesize];
				in.read(b);
				in.close();
				Maze3d loaded = new Maze3d(b);
				this.mazes.put(newMazeName, loaded);
				o.setData("The file <" + fileName + "> was loaded and a new maze has beed saved as <" + newMazeName		+ ">");
				notifyObservers(o);
			} catch (IOException e) {
				o.setData(e.toString());
				notifyObservers(o);
			}
		} else {
			o.setData("This maze name already exist ,please pick different name");
			o.setSerialNumber(-1);
			notifyObservers(o);
		}

	}

	@Override
	public void mazeSize(DataObj o) {
		String[] param = (String[]) o.getData();
		String mazeName = param[0];
		if (mazes.containsKey(mazeName)) {
			o.setData(Integer.toString(mazes.get(mazeName).toByteArray().length));
			notifyObservers(o);
		} else {

			o.setData("This maze name does not exist");
			o.setSerialNumber(-1);
			notifyObservers(o);
		}

	}

	@Override
	public void fileSize(DataObj o) {
		String[] param = (String[]) o.getData();
		String fileName = param[0];
		File file = new File(fileName);
		Long fileSize = file.length();
		if (!(fileSize == 0)) {

			o.setData(Long.toString(fileSize));
			notifyObservers(o);
		} else {

			o.setData("This maze name does not exist on this file");
			o.setSerialNumber(-1);
			notifyObservers(o);
		}

	}

	@Override
	public void displayCrossSectionBy(DataObj o) {
		String[] param = (String[]) o.getData();
		String axis = param[0];
		int index = Integer.parseInt(param[1]);
		String mazeName = param[2];
		if (mazes.containsKey(mazeName)) {
			Maze3d maze = mazes.get(mazeName);
			try {
				if (axis.equals("x")) {
					o.setData(maze.getCrossSectionByX(index));
					notifyObservers(o);

				} else if (axis.equals("y")) {
					o.setData(maze.getCrossSectionByY(index));
					notifyObservers(o);
				} else {
					o.setData(maze.getCrossSectionByZ(index));
					notifyObservers(o);

				}

			} catch (IndexOutOfBoundsException e) {
				o.setData(e.toString());
				o.setSerialNumber(-1);
				notifyObservers(o);

			}

		} else {

			o.setData("This maze name does not exist");
			o.setSerialNumber(-1);
			notifyObservers(o);
		}

	}

	@Override
	public void solve(DataObj o) {
		
		// <name> <algorithm><position>
		this.executor.submit(new Runnable() {
			
			@Override
			public void run() {
				boolean additionalPosition=false;
				String[] param = (String[]) o.getData();
				int length=param.length;
				String mazeName = param[0];
				String algorithm = param[1];
				ObjectOutputStream out = null;
				ObjectInputStream in=null ;
				DataObj data = null;
				Position position=null;
				if(length==3)
				{
				position=new Position(param[2]);
				additionalPosition=true;
				System.out.println(position);
				}
				if(!algorithm.equals("Astar AirDistance")&& !algorithm.equals("Astar ManhattanDistance")&& !algorithm.equals("Bfs"))
				{
					o.setData("No such algorithm");
					o.setSerialNumber(-1);
					notifyObservers(o);
				}
				else if (mazes.containsKey(mazeName)) {
					try
					{
					 theServer = new Socket("localhost", 5400);
					System.out.println("server alive");
				 
					}
					catch(IOException e)
					{
						o.setData("Could not establish connection with server");
						o.setSerialNumber(-1);
						notifyObservers(o);
						return;
					}
	
					Maze3d maze3d = mazes.get(mazeName);	
					
					if(additionalPosition)
					{
						
						if(position.equals(maze3d.getStartPosition()))
						{
						    try {
						    	out = new ObjectOutputStream(new BufferedOutputStream(theServer.getOutputStream()));
								out.writeObject(2);
								out.writeObject(algorithm);
								out.writeObject(maze3d);	 
							    out.flush();
							   
							} catch (IOException  e) {	
								o.setSerialNumber(-1);
								o.setData("Problem with sending to server");
								notifyObservers(o);
								closeServerSocket();
								return;
							}
						    
							  try {
									in= new ObjectInputStream(new BufferedInputStream(theServer.getInputStream()));
									 data=	(DataObj) in.readObject();
								} catch (IOException | ClassNotFoundException e1) {
									o.setData("problem with input from server");;
									o.setSerialNumber(-1);
									notifyObservers(o);
									closeServerSocket();
									return;
								}
						    
							@SuppressWarnings("unchecked")
							Solution<Position> s=(Solution<Position>) data.getData();
							data.setData("solution for " + "<" + mazeName + ">" + " is ready");
							solutions.put(mazeName+"*", s);
							notifyObservers(data);
							try {
								theServer.close();
							} catch (IOException e) {
								
								e.printStackTrace();
							}
							
						}
								
					    else if(!position.equals(maze3d.getStartPosition()))
					    {
					    	 Maze3d newMaze3d =new Maze3d(maze3d);
							 newMaze3d.setStartPosition(new Position(position));						    
							 try {
							    	
							    	out = new ObjectOutputStream(new BufferedOutputStream(theServer.getOutputStream()));
									out.writeObject(2);
									out.writeObject(algorithm);
									out.writeObject(newMaze3d);	 
								    out.flush();
								   
								} catch (IOException  e) {	
									o.setSerialNumber(-1);
									o.setData("Problem with sending to server");
									notifyObservers(o);
									closeServerSocket();
									return;
								}
							    
								  try {
										in= new ObjectInputStream(new BufferedInputStream(theServer.getInputStream()));
										 data=	(DataObj) in.readObject();
									} catch (IOException | ClassNotFoundException e1) {
										o.setData("problem with input from server");;
										o.setSerialNumber(-1);
										notifyObservers(o);
										closeServerSocket();
										return;
									}
								  
							@SuppressWarnings("unchecked")
							Solution<Position> s=(Solution<Position>) data.getData();
							data.setData("solution for " + "<" + mazeName + ">" + " is ready");
							solutions.put(mazeName+"*", s);
							notifyObservers(data); 
							closeServerSocket();
					    }	
					}
					//no additional position
					else
					{
						try {
					    	out = new ObjectOutputStream(new BufferedOutputStream(theServer.getOutputStream()));
							out.writeObject(2);
							out.writeObject(algorithm);
							out.writeObject(maze3d);	 
						    out.flush();
						   
						} catch (IOException  e) {	
							o.setSerialNumber(-1);
							o.setData("Problem with sending to server");
							notifyObservers(o);
							closeServerSocket();
							return;
						}
					    
						  try {
								in= new ObjectInputStream(new BufferedInputStream(theServer.getInputStream()));
								 data=	(DataObj) in.readObject();
							} catch (IOException | ClassNotFoundException e1) {
								o.setData("problem with input from server");;
								o.setSerialNumber(-1);
								notifyObservers(o);
								closeServerSocket();
								return;
							}
						@SuppressWarnings("unchecked")
						Solution<Position> s=(Solution<Position>) data.getData();
						data.setData("solution for " + "<" + mazeName + ">" + " is ready");
						solutions.put(mazeName, s);
						notifyObservers(data);	
						closeServerSocket();
					}
				}
				else {
					o.setData("This maze name does not exist");
					o.setSerialNumber(-1);
					notifyObservers(o);
					closeServerSocket();
				}
			}
			
			
		
				
				
		});	
	}
	private void closeServerSocket(){
		try {
			theServer.close();
		} catch (IOException e1) {
		
			e1.printStackTrace();
		}		
	}
	
	@Override
	public void getSolution(DataObj o) {
		String[] param = (String[]) o.getData();
		String mazeName=param[0];
		if (solutions.containsKey(mazeName)) {
			o.setData(solutions.get(mazeName));
			notifyObservers(o);
		} else {

			o.setData("no solution available for " + "<" + param[0] + ">");
			o.setSerialNumber(-1);
			notifyObservers(o);
		}

	}

	@Override
	public void exit() {
		notifyObservers(new DataObj("closing all open threads...", -2));
			this.executor.shutdown();
		notifyObservers(new DataObj("all  threads  are closed.", -2));
	}
	

}
