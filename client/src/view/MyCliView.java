package view;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Observable;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import algorithms.search.State;
import presenter.DataObj;




public class MyCliView extends Observable implements View {
	private CLI cli;
	
	public MyCliView(InputStreamReader in,OutputStreamWriter out) {
		this.cli=new CLI(in,out,this);
	}
	@Override
	public void notifyObservers(Object arg) {
		this.setChanged();
		super.notifyObservers(new DataObj(arg, 0));
	}

	public CLI getCli(){
		return this.cli;
	}

	public void exit(){
		cli.exit();
	}

	@Override
	public void start() {
		cli.start();
		
	}
	
	@Override
	public void display(DataObj o) {
		
		if(o.getData() instanceof String)
		{
			String data= (String) o.getData();
			cli.getOut().println(data);
			cli.getOut().flush();
		}
		else if(o.getData() instanceof int [][])
		{
			int[][] data= (int[][]) o.getData();
			
			for(int x=0;x<data.length;x++)
			{
			    for(int y=0;y<data[0].length;y++)
			    {		    	
					    cli.getOut().print(data[x][y]);
			    }
			    
			    cli.getOut().println();
				
			}
			cli.getOut().flush();
		}
		else if (o.getData()instanceof Maze3d)
		{
			Maze3d maze3d=(Maze3d )o.getData();
			cli.getOut().println("startPosition:"+maze3d.getStartPosition());
			cli.getOut().println("goalPosition:"+maze3d.getGoalPosition());
			int data [][][] =maze3d.getMaze3d();
			for(int k=0;k<data[0][0].length;k++)
			{
				cli.getOut().println("Level "+(k+1));
			    for(int i=0;i<data.length;i++)
			    {
				    for(int j=0;j<data[0].length;j++)
				    {
					    cli.getOut().print(data[i][j][k]);
				    }
				    
				    cli.getOut().println();
			    }
			    cli.getOut().println();
			    cli.getOut().println();
				cli.getOut().flush();
			}
		}
		else if(o.getData()instanceof String[] )
		{
			String[] data =  (String[]) o.getData();
			
			for (int i = 0; i < data.length; i++) {
				cli.getOut().println(data[i]);
			}
			cli.getOut().flush();
		}
		else if(o.getData()instanceof Solution)
		{
			@SuppressWarnings("unchecked")
			Solution<State<Position>> solution=(Solution<State<Position>>) o.getData();
			for(State<State<Position>> state : solution.getSolution())
				cli.getOut().println(state);
		}
		else
			cli.getOut().println("Wrong object for the display method");
		
	}
	
	

}
