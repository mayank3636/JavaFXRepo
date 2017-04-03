package application;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientName {
	
	public String setHostName() {
	 String hostname = "Unknown";

		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
			System.out.println(hostname);
		} catch (UnknownHostException ex) {
			System.out.println("Hostname can not be resolved");
		}
		return hostname;

	}

}
