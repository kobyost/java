package model;

import controller.Controller;

public class Mymodel implements Model {

	Controller controller;
	MyServer myServer;
	
	
	public Mymodel(int port ,ClientHandler clientHandler,int numOfClients,Controller controller) {
		
		myServer=new MyServer(port, clientHandler, numOfClients);
		this.controller=controller;
	}
	@Override
	public void onLineClients() {
		if(myServer.serverIsAlive==false)
		{
			controller.display("There are no on");
		}
		
	}

	@Override
	public void closeServer() {
		try {
			if(myServer.serverIsAlive)
			{
			controller.display("Shuting down the server...");
			myServer.close();
			controller.display("Server is close");
			}
			else
				controller.display("The server is not running");
		} catch (Exception e) {
			controller.display("The server was closed in an unsafe mode.");
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void start() {
		try {
			if(myServer.serverIsAlive==false)
			{
			myServer.start();
			controller.display("The Server is running");
			}
			else
				controller.display("server is already closed");
		} catch (Exception e) {
			controller.display("Server could not be initialized");
			e.printStackTrace();
		}
		
	}

}
