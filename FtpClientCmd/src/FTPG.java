import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class FTPG {
	public final static String USERNAME="username:";
	public final static String CONNECT = "connect";
	public final static String CONNECT_SUCCED = "connectsucceed";
	Socket socket;
	InetAddress address;
	String host = "127.0.0.1";
	int port = 1888;
	Scanner userInput;
	PrintWriter pw;
	BufferedReader br;
	
	public void initClient() {
		try {
			userInput = new Scanner(System.in);
			socket = new Socket(host,port);
		    pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		    br =new BufferedReader(new InputStreamReader(socket.getInputStream()));
			sendCmd(CONNECT);
			while(true) {
				String read = recvCmd(br);
				processCMD(read);
				if(read.equals("exit")) {
					break;
				}
			}
		}catch(Exception e) {
			log(e.getMessage());
		}
		
		
	}
	private void processCMD(String str) {
		log(str);
		switch(str) {
		   case CONNECT_SUCCED:
			  log("received:"+str);
			  String recvCMD =userInput.nextLine();
			  sendCmd(recvCMD);
			  break;
		}
		if(str.equals(USERNAME)) {
			
		}
	}
	private void log(String str) {
		System.out.println("client:"+str);
	}
	
	private void sendCmd(String str) {
		pw.write(str);	
		pw.flush();
	}
	
	private String recvCmd(BufferedReader br) {
		String read = "recvCmdFailed";
		try {
			read = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return read;
	}
	
	public static void main(String[] args) {
		new FTPG().initClient();
	}
}
