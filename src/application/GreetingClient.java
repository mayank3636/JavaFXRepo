package application;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//File Name GreetingClient.java
import java.net.InetAddress;
import java.net.Socket;

public class GreetingClient extends Thread {
	public static final int BUFFER_SIZE = 1024 * 4;
	private byte[] buffer;

	@Override
	public void run() {
		String serverName = "172.22.20.245";
		int port = 443;
		try {
			ModifyCSV modify= new ModifyCSV();
			File outPutFileChecker = new File(System.getenv("APPDATA")+"\\output.csv");
			while(outPutFileChecker.exists()==false){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			modify.modifyCSV(System.getenv("APPDATA"));
			System.out.println("Connecting to " + serverName + " on port " + port);
			Socket client = new Socket(serverName, port);
			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			String hostname = addr.getHostName();
			
		    
		    File f = new File(System.getenv("APPDATA")+"\\"+hostname+InitilizeElement.ILTestName+"Send.csv");
		    while(f.exists()==false){  
		    	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("Error With Command Prompt TypePerf Not Running");
				}
		    }
		    System.out.println(System.getenv("APPDATA")+"\\"+hostname+InitilizeElement.ILTestName+"Send.csv");
			long expect = f.length();
			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
					DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()))) {

				byte[] buffer = new byte[client.getSendBufferSize()];
				dos.writeUTF(f.getName());
				dos.writeLong(expect);

				long left = expect;
				int inlen = 0;
				while (left > 0 && (inlen = bis.read(buffer, 0, (int) Math.min(left, buffer.length))) >= 0) {
					dos.write(buffer, 0, inlen);
					left -= inlen;
				}
				dos.flush();
				if (left > 0) {
					throw new IllegalStateException("We expected " + expect + " bytes but came up short by " + left);
				}
				if (bis.read() >= 0) {
					throw new IllegalStateException(
							"We expected only " + expect + " bytes, but additional data has been added to the file");
				}
			}

			// OutputStream outToServer = client.getOutputStream();
			// DataOutputStream out = new DataOutputStream(outToServer);
			//
			//
			// BufferedInputStream in = new BufferedInputStream(new
			// FileInputStream(f));
			// buffer = new byte[BUFFER_SIZE];
			// int len = 0;
			// while ((len = in.read(buffer)) > 0) {
			// out.write(buffer, 0, len);
			// //System.out.print("#");
			// }
			// InputStream inFromServer = client.getInputStream();
			// DataInputStream in1 = new DataInputStream(inFromServer);
			//
			// System.out.println("Server says " + in1.readUTF());
			client.close();
			// in.close();
		} catch (IOException e) {
			System.out.println("172.22.20.245 is Down currently or Server is not Running");
		}

	}
}