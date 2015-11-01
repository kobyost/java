package presenter;
/**
 * 
 * @author Koby osterman & Raz Romano 
 *
 * This is the interface for the Command design Pattern.
 * Every Command class needs to implement the do command method.
 */
public interface Command {
	
	public void doCommand(DataObj o);

}
