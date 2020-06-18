package riw

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.TimeUnit;	
	
public class laborator_5
{
	
	private static final int DNS_SERVER_PORT = 53;
	private static final String DNS_SERVER_ADDRESS = "8.8.8.8";
	private static HashSet<String> robotsDomain = new HashSet<String>();

	static String getIpDNS(String domain) throws IOException 
	{
		InetAddress ipAddress = InetAddress.getByName(DNS_SERVER_ADDRESS);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		dos.writeShort(0x1234);
		// recursive
		dos.writeShort(0x0100);
		dos.writeShort(0x0001);
		dos.writeShort(0x0000);
		dos.writeShort(0x0000);
		dos.writeShort(0x0000);

		String[] domainParts = domain.split("\\.");
		
		for (int i = 0; i < domainParts.length; i++) 
		{
			byte[] domainBytes = domainParts[i].getBytes("UTF-8");
			dos.writeByte(domainBytes.length);
			dos.write(domainBytes);
		}

		dos.writeByte(0x00);
		dos.writeShort(0x0001);
		dos.writeShort(0x0001);

		byte[] dnsFrame = baos.toByteArray();

	        
		try {
				
			DatagramSocket socket = new DatagramSocket();
			socket.setSoTimeout(1000); // setam un timeout de 1 secunda
			DatagramPacket dnsReqPacket = new DatagramPacket(dnsFrame, dnsFrame.length, ipAddress, DNS_SERVER_PORT);
			socket.send(dnsReqPacket);

			byte[] buf = new byte[512];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);

			socket.close();


			int errorCode = buf[3] & 0x0F;
			if (errorCode != 0x00 ) 
			{
				return null;
			} else 
			{
				
			}

			DataInputStream din = new DataInputStream(new ByteArrayInputStream(buf));
			// Transaction ID
			din.readShort();
			//Flags
			din.readShort();
			// Questions
			din.readShort();
			// Answers
			din.readShort();
			// Authority
			din.readShort();
			//Additional RRs
			din.readShort();

			int recLen = 0;
			while ((recLen = din.readByte()) > 0) {
				byte[] record = new byte[recLen];

				for (int i = 0; i < recLen; i++) {
					record[i] = din.readByte();
				}

				// Record:
			}

			// Record Type
			din.readShort();
			// Class
			din.readShort();
			//Field
			din.readShort();
			// Type
			short type = din.readShort();
			// Class
			din.readShort();
			// TTL: 
			int TTL = din.readInt();

			short addrLen = din.readShort();
			String ip = "";

			if (type == 1 && addrLen == 4) {

				for (int i = 0; i < addrLen; i++) {
					ip += "" + String.format("%d", (din.readByte() & 0xFF)) + ".";
				}
				ip = ip.substring(0, ip.length() - 1);

				long timeMillis = System.currentTimeMillis();
				long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
				cacheDNS.put(domain, new dnsTime(ip, TTL + timeSeconds));
			}

			System.out.println("Ip-ul este:" + ip);
			return ip;
       } catch (SocketTimeoutException ste)
       {           
           return null;
       } catch (Exception e) 
	   {
   		// TODO: handle exception
	   }
		return null;
	}
	
	public static void main(String[] args) throws IOException 
	{
		getIpDNS("www.google.com");		
	}
}	