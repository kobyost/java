package model;

import presenter.DataObj;

public interface Model {
	/**
	 * This method is responsible for generating a maze and saves him in a hash map by the name the user has picked.
	 * @param param
	 */
	public void generate3dMaze(DataObj o);
	/**
	 * This method displays a maze ,the parameter are: maze name ,dimensions ,algorithm to generate maze. 
	 * @param param
	 */
	public void display(DataObj o);
	/**
	 * This method saves the maze into a file.
	 * The parameters  are : maze name, file name
	 * @param param
	 */
	public void saveMaze(DataObj o);
	/**
	 * This method loads the maze from a file and stores it in a new maze name .
	 * The parameters are: file name ,  new maze name
	 * @param param
	 */
	public void loadMaze(DataObj o);
	
	/**
	 * This method finds the size of a maze in the memory.
	 * The parmeters are : maze name .
	 * @param param
	 */
	public void mazeSize(DataObj o);
	
	/**
	 * This method finds the size of a maze in a file.
	 * The parameters are : file name. 
	 * @param param
	 */
	public void fileSize(DataObj o);
	/**
	 * This method finds the cross section of a maze by an axis and index and  maze name .
	 * The parameters are : axis,index, maze name.
	 * @param param
	 */
	public void displayCrossSectionBy(DataObj o);
	
  /**
   * This method solves the the maze and sends to the view  a string  that says that the maze was solved.
   *  The parameters are : maze name ,algorithm.
   * @param param
   */
	public void solve(DataObj o);
	
	/**
	 * This method finds the steps for the solution of the maze .
	 * The parameters are : maze name .
	 * @param param
	 */
	public void getSolution(DataObj o);
	/**
	 * This method closes all open  threads and files .
	 * @param param
	 */
	public void exit();
}
