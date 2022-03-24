package servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerChat extends Thread {
	
	private boolean isActive = true;
	private int nombreClients = 0;	
	private List<Conversation> clients = new ArrayList<Conversation>();

	public static void main(String[] args) {
		new ServerChat().start();
	}
	
	@Override
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(1234);
			while (isActive) {
				Socket socket = ss.accept();
				++nombreClients;
				Conversation conversatipn = new Conversation(socket,nombreClients);
				clients.add(conversatipn);
				conversatipn.start();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	class Conversation extends Thread{
		
		protected Socket socket;
		protected int numeroClient;
		
		public Conversation(Socket socket, int numeroClient) {
			this.socket = socket;
			this.numeroClient = numeroClient;
		}
		
		public void BroadcastMessage(String message,Socket socketClient, int num ) {
			try {
				for(Conversation client:clients) {
					if(client.socket != socketClient) {
						if(client.numeroClient==num) {
							PrintWriter pw = new PrintWriter(client.socket.getOutputStream(),true);
							pw.println(message);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			
			try {
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is); 
				BufferedReader br = new BufferedReader(isr);
				
				OutputStream os = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(os,true);
				
				String ipClient = socket.getRemoteSocketAddress().toString();
				System.out.println("Connexion du client numéro "+numeroClient+" IP= "+ipClient);
				pw.println("Bienvenue, vous êtes le client numéro "+numeroClient);
				
				
				while(true) {
					String req = br.readLine();	
					if(req.contains("=>")) {
						String[] requestParams = req.split("=>");
						if(requestParams.length==2);
						String message = requestParams[1];
						int numeroClient = Integer.parseInt(requestParams[0]);
						BroadcastMessage(message, socket, numeroClient);
					}
					else {
						BroadcastMessage(req, socket, -1);
					}
				}
				
				// TODO: handle exception
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
