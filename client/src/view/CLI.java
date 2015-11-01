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
import java.util.ArrayList;
import java.util.Observable;




public class CLI {
	
	private BufferedReader in;
	private PrintWriter out;
	private Thread mainThread;
	private Observable o;
	private Boolean loop= true;
	/**
	 * The cli constructor which gets InputStreamReader and OutputsteamWriter and a hash map of commands.
	 * @param in
	 * @param out
	 * @param commands
	 */
	public CLI(InputStreamReader in,OutputStreamWriter out,Observable o){
		this.in=new BufferedReader(in);
		this.out=new PrintWriter(out);
		this.o=o;
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
	
	/**
	 * This method starts a thread which is getting commands from the user.
	 * 
	 */
	public void start(){
		
		mainThread=new Thread(new Runnable() {
			
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				String input;
				try {
					   do{
						   mainThread.sleep(1*1000);
						   System.out.print("Please enter a command: ");
                           input=in.readLine();						
					     
					       o.notifyObservers(input);
						 
					   }while(loop);	
					 
					}
			 catch (IOException e) {
				e.printStackTrace();
			 } catch (InterruptedException e) {
				e.printStackTrace();
			}
				
			}
		});
		mainThread.start();
		
		
	}

	
	/**
	 * This method is geting The parameters for the command ,it separets the parameters from the command .
	 * @param input
	 * @return String[]
	 */
	public String[] getParam(String input){
		if(!input.contains("<"))
			return new String[0];
		String param=input.substring(input.indexOf('<')-1);
		String [] tmp=param.split("(<)|(>)");
		ArrayList<String> finalParam=new ArrayList<String>();
		for (String string2 : tmp) {
			if(!string2.equals(" "))
				finalParam.add(string2);
		}
		String[] a=finalParam.stream().toArray(String[]::new);
		return a;
	}
	/**
	 * This method returns the command from the entire string that the user  has entered, it seperates the command from the parameters.
	 * @param input
	 * @return String 
	 */
	public String getCommand(String input){
		if(input.contains("<"))
		      return input.substring(0,(input.indexOf('<'))-1);
		else
			return input;
	}
	
	public void exit(){
		loop=false;
	}

}
