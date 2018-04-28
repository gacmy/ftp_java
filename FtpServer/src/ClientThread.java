import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.Scanner;
/**********
 * ���������ܵ�connect:��ͷ������ У��������Ϣ
 * ���յ�  username:��ͷ������ У�� �û���
 * ���յ� pwd:��ͷ����ϢУ������
 * ���� �ϴ����� ������� ��ʼ������
 * 
 * 
 * *******/
public class ClientThread extends Thread{
	private Socket clientSocket;
	
	public final static String USERNAME = "username:";
	public final static String CONNECT = "connect";
	Reader in;
	PrintWriter pw;
	public ClientThread(Socket socket) {
		clientSocket = socket;
		try {
			InputStream stream = clientSocket.getInputStream();
			OutputStream outStream = clientSocket.getOutputStream();
			in = new InputStreamReader(stream, "UTF-8");
			pw = new PrintWriter(new OutputStreamWriter(outStream));
		}catch(Exception e) {
			log(e.getMessage());
		}
	
		
		
	}
	
	@Override
	public void run() {
		try {
			
			final int bufferSize = 1024;
			final char[] buffer = new char[bufferSize];
			StringBuffer sb = new StringBuffer();
			for (; ; ) {
				int rsz = in.read(buffer, 0, buffer.length);
				if (rsz < 0)
					break;
				sb.append(buffer,0,rsz);
				String recvCmd = sb.toString();
				
				processCMD(recvCmd);
				sb.delete(0, sb.length());
			}
			
			}catch(Exception e) {
				log(e.getMessage());
			}
		
	}
	
	private void processCMD(String cmd) {
		String step = "";
		if(cmd.startsWith(CONNECT)) {
			step = CONNECT;
		}else if(cmd.startsWith(USERNAME)) {
			step = USERNAME;
			
		}
		
		switch(step) {
		case CONNECT:
			Step.connectSuccess();
			processConnect();
			send("connectsucceed");
			break;
		case USERNAME:
			if(Step.isConnect()) {
				
			}else {
				String username;
				processUserName();
			}
			break;
		default:
			send("I don't know this cmd!");
		}
	}
	
	//�����û�������
	private void processUserName() {
		Step.validateUserNameSuccess();
		send("pwd");
	}
	
	//����������Ϣ
	private void processConnect() {
		if(Step.isConnect()) {
			send("Connect Success");
		}else {
			send("connect failed,please connect");
		}
	}
	
	private void send(String cmd) {
		pw.write(cmd+"\r\n");
		pw.flush();
	}
	
	public  void log(String str) {
		System.out.println("server:"+str);
	}

}
