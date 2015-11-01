package view;
/**
 * @author Koby osterman @ Raz Romano 
 * 
 *  This class is CLI class which  is the command line interface.
 *  In this class we are responsible for the Input from the user and for the Output to the user.
 */
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import controller.Controller;




public class CLI {
	
	Controller controller;
	private BufferedReader in;
	private PrintWriter out;
	private Thread mainThread;
	
	private Boolean loop= true;
	/**
	 * The cli constructor which gets InputStreamReader and OutputsteamWriter and a hash map of commands.
	 * @param in
	 * @param out
	 * @param commands
	 */
	public CLI(InputStreamReader in,OutputStreamWriter out,Controller controller){
		this.in=new BufferedReader(in);
		this.out=new PrintWriter(out);
		this.controller=controller;
	}
	/**
	 * The method returns a  BufferReader  for Input.
	 * @return BufferReader.
	 */
	public BufferedReader getIn(){
		return this.in;
	}
	/**
	 * This method returns a PrintWriter for Output.
	 * @return PrintWriter
	 */
	public PrintWriter getOut(){
		return this.out;
	}
	public void display(Object obj )
	{
		if (obj instanceof String []) {
			String [] arrString = (String []) obj;
			for (int i = 0; i < arrString.length; i++) {
				out.println(arrString[i]);
				out.flush();
			}
		}
		else if (obj instanceof String) {
			String string = (String) obj;
				out.println(string);
				out.flush();
			}
		else{
			out.println(obj);
			out.flush();
		}
		
	}
	
	/**
	 * This method starts a thread which is getting commands from the user.
	 * 
	 */
	public void start(){
		controller.command("display commands");
		mainThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				String input;
				try {
					   do{
						   out.flush();
                           input=in.readLine();						
                           if(input!="")
                           {
                        	   controller.command(input);
                           }
                           else
                           {
                        	   out.println("wrong input");
                        	   out.flush();
                           }
					   }while(loop);	
					 
					}
			 catch (IOException e) {
				e.printStackTrace();
			 }
				
			}
		});
		mainThread.start();
		
		
	}

	
	
	public void exit(){
		loop=false;
		out.println("closes the program good bye ");
		out.flush();
	}

}
