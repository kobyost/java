package view;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import controller.Controller;

public class MyViewCli implements View {
	
	Controller controller;
	CLI cli;
	public MyViewCli(InputStreamReader in,OutputStreamWriter out,Controller controller) {
		this.controller=controller;
		this.cli=new CLI(in, out, controller);
		
	}
	@Override
	public void start() {
		
		cli.start();
	}
	@Override
	public void display(Object obj) {
		cli.display(obj);
		
	}
	@Override
	public void exit() {
		cli.exit();
		
	}
}
