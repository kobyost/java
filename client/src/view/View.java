package view;

import presenter.DataObj;

public interface View {
	
	public void display(DataObj o);
	public void start();
	/**
	 * This method sets the controller to the view.
	 * @param controller
	 */
	public void exit();
}
