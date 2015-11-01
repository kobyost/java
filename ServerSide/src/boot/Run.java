package boot;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import controller.Controller;
import controller.MyController;
import model.ClientHandler;
import model.Model;
import model.MyclientHandler;
import model.Mymodel;
import view.MyViewCli;
import view.View;

public class Run {

	public static void main(String[] args) {
		ClientHandler clientHandler=new MyclientHandler();
		Controller controller =new MyController();
		Model model=new Mymodel(5400, clientHandler, 3,controller);
		View view = new MyViewCli(new InputStreamReader(System.in), new OutputStreamWriter(System.out), controller);
		controller.setModel(model);
		controller.setView(view);
		view.start();
	}

}
