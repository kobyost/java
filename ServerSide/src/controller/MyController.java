package controller;

import java.util.HashMap;
import java.util.Set;

import model.Model;
import view.View;

public class MyController implements Controller {
	
	Model model;
	View view;
	HashMap<String, Command> commands;
	public MyController() {
		commands=new HashMap<>();	
		commands.put("start server", new Start());
		commands.put("display commands", new DisplayCommand());
		commands.put("exit", new Exit());
		commands.put("close server", new CloseServer());
	}
	@Override
	public void display(Object obj) {
	
			view.display(obj);
	}

	public class Start implements Command {

		@Override
		public void doCommand() {
			model.start();
			
		}
		
	}

			
	public class Exit implements Command{
	

	@Override
	public void doCommand() {
		model.closeServer();
		view.exit();
		
	}
}
	public class CloseServer implements Command{

		@Override
		public void doCommand() {
			model.closeServer();
			
		}
		
	}
	
	public class DisplayCommand implements Command{

		@Override
		public void doCommand() {
			String [] allCommands=new String[commands.size()];
			Set<String> setCommands ;
			setCommands=commands.keySet();
			setCommands.toArray(allCommands);
			view.display(allCommands);
		}
		
		
	}
	@Override
	public void command(Object obj) {
		if (obj instanceof String) {
			String string = (String) obj;
			Command cmd=commands.get(string);
			if(cmd!=null)
			{
				cmd.doCommand();
			}
			else
				view.display("wrong input");
		}
		else
			try {			
				throw new Exception("Wrong input : Bad Object type");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	@Override
	public void setModel(Model model) {
		this.model=model;
		
	}
	@Override
	public void setView(View view) {
		this.view=view;
		
	}
	
	}

	
	
	

