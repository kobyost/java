package controller;

import model.Model;
import view.View;

public interface Controller {

	public void display(Object obj);
	public void command(Object obj);
	public void setModel(Model model);
	public void setView(View view);
	
}
