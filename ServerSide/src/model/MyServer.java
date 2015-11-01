package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class MyServer {

	int port;
	ServerSocket server;
	ClientHandler clientHandler;
	int numOfClients;
	ExecutorService threadpool;
	HashMap<String, Socket> clients;
	volatile boolean serverIsAlive;
//	AtomicInteger OnlineClients;
	Thread mainServerThread;
	
	int clientsHandled=0;
	
	public MyServer(int port,ClientHandler clinetHandler,int numOfClients) {
		this.port=port;
		this.clientHandler=clinetHandler;
		this.numOfClients=numOfClients;
		this.clients=new HashMap<>();
		this.serverIsAlive=false;
		if(numOfClients < 1)
			numOfClients=3;
	}
	public String[] getClients()
	{
		String[] allClients =new String[clients.size()];
		Set<String> hashTostring =clients.keySet();
		hashTostring.toArray(allClients);
		return allClients;
	}
	public synchronized void addClient(Socket socket)
	{
		clients.put(socket.getInetAddress().toString(), socket);
	
	}
	public synchronized void removeClient(String ip)
	{
		this.clients.remove(ip);
		
	}
	public boolean serverIsalive()
	{
		return this.serverIsAlive;
	}
	
	public void start() throws Exception{
		server=new ServerSocket(port);
		server.setSoTimeout(10*1000);
		threadpool=Executors.newFixedThreadPool(numOfClients);
		serverIsAlive=true;
		
		mainServerThread=new Thread(new Runnable() {			
			@Override
			public void run() {
				while(serverIsAlive){
					try {
						final Socket someClient=server.accept();
						if(someClient!=null){
							addClient(someClient);
							threadpool.execute(new Runnable() {									
								@Override
								public void run() {
									try{		
										clientsHandled++;
										clientHandler.handleClient(someClient.getInputStream(), someClient.getOutputStream());
										someClient.close();
										removeClient(someClient.getInetAddress().toString());									
									}catch(IOException e){
										e.printStackTrace();
									}									
								}
							});								
						}
					}
					catch (SocketTimeoutException e){
						
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			} // end of the mainServerThread task
		});
		
		mainServerThread.start();
		
	}
	
	public void close() throws Exception{
		if(serverIsAlive==true)
		serverIsAlive=false;	
		// do not execute jobs in queue, continue to execute running threads
		
		threadpool.shutdown();
		// wait 10 seconds over and over again until all running jobs have finished
		@SuppressWarnings("unused")
		boolean allTasksCompleted=false;
		while(!(allTasksCompleted=threadpool.awaitTermination(10, TimeUnit.SECONDS)));
		
		

		mainServerThread.join();		
		
		
		server.close();
		
	}
	
	
}
