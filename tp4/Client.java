package tp4;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String userName;
	
	public Client(Socket socket,String userName) {
		try {
				this.socket = socket;
				this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.userName=userName;
	        }catch(IOException e) {
				closeEverything(socket,bufferedReader,bufferedWriter);		
			}
	}
	public void sendMessage() {
		try {
			bufferedWriter.write(userName);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
			Scanner scanner = new Scanner(System.in);
			
			while(socket.isConnected()) {
				String messageToSend = scanner.nextLine();
				bufferedWriter.write(userName + ": "+ messageToSend );
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
			
		}catch(IOException e) {
			closeEverything(socket,bufferedReader,bufferedWriter);		
		}
	}
	public void listenForMessage() {
		new Thread(new Runnable(){

			@Override
			public void run() {
				String msgFromGroupeChat;
				while(socket.isConnected()) {
					try {
						msgFromGroupeChat = bufferedReader.readLine();
						System.out.println(msgFromGroupeChat);
					}catch(IOException e) {
						closeEverything(socket,bufferedReader,bufferedWriter);		
					}
				}
				// TODO Auto-generated method stub
				
			}
			
			
		}).start();
	}
	public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter) {
		try {
			if(bufferedReader != null) {
				bufferedReader.close();
			}
			if(bufferedWriter != null) {
				bufferedWriter.close();
			}
			if(socket != null) {
				socket.close();
			}
		}catch(IOException e) {
			 e.printStackTrace();
		     
			 
		 }
	}
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your username for the groupe chat : ");
		String userName = scanner.nextLine();
		Socket socket =new Socket("localhost",2021);
		Client client =new Client(socket,userName);
		client.listenForMessage();
		client.sendMessage();
		
	}

}
