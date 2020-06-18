package riweb

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class laborator_7
{		
		
	public static void crawler() throws IOException 
	{
		long globalStartTime = System.nanoTime(); // timpul global

		Set<String> pagesVisited = new HashSet<>();
		LinkedList<String> Q = new LinkedList<>();
		int numberOfPagesCrawled = 0;
		int nrPages = 0;
		Document doc;
		Q.add("http://riweb.tibeica.com/crawl/");

		while (!Q.isEmpty() && nrPages < 100) 
		{
			String link = Q.pop();

			try {
				URL currentUrl = new URL(link);

				String domain = currentUrl.getHost();
				String path = currentUrl.getPath();
				int port = currentUrl.getPort();

				if (path.equals("")) {
					path = "/";
				}

				if (pagesVisited.contains(link)) {
					continue;
				} else {
					pagesVisited.add(link);

					if (!currentUrl.getProtocol().equals("http")) 
																	
					{
						continue;
					}				

					try 
					{					
						doc = Jsoup.connect(link).get();
					} catch (Exception e) 
					{
						Q.remove(link);
						continue;
					}

					Element robots = doc.selectFirst("meta[name=robots]");
					String robotsString = "";
					if (robots == null) 
					{

					} 
					else 
					{
						robotsString = robots.attr("content");
					}

					if (!robotsString.equals("")) 
					{
						if (robotsString.contains("nofollow")) 
						{
							continue;
						}
						if (robotsString.contains("none")) 
						{
							continue;
						}
					}
					System.out.println("Sending HTTP TO THE LINK:" + link);
					requestHTTP(path, domain, port);
					  ++numberOfPagesCrawled;
				}

				Elements elements = doc.select("a[href]");
				for (Element element : elements) 
				{
					String absoluteLink = element.attr("abs:href");
					if (absoluteLink.indexOf("#") != -1) 
					{
						StringBuilder tempLink = new StringBuilder(absoluteLink);
						tempLink.replace(absoluteLink.indexOf("#"), tempLink.length(), "");
						absoluteLink = tempLink.toString();
					}
					Q.add(absoluteLink);
				}

				nrPages++;
				System.out.println();
			} catch (Exception e) 
			{
				continue;
			}
		}	
	System.out.println(numberOfPagesCrawled);
	System.out.println("" + Math.round((double)numberOfPagesCrawled * 60 / ((double)(System.nanoTime() - globalStartTime) / 1000000000.0) * 100.0) / 100.0 + " pagini / minut.");
	}	
}