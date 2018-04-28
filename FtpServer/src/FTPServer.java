import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

public class FTPServer {
	
	public static void main(String[] args) {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(1888);
			Socket client = serverSocket.accept();
			if(client == null) {
				log("accept client failed");
			}else {
				log("accept client");
				new ClientThread(client).start();
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log(e.getMessage());
		}
	}
	
	public static void log(String str) {
		System.out.println("server:"+str);
	}
}
