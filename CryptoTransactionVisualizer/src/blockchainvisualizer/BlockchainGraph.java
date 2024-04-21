/*
 * Author: 		 T. Stratton
 * Date started: 15 NOV 2023
 * Last updated: 17 APR 2024
 * 
 * File Contents:
 *  
 * 
 * Notes:
 * 
 * 
 */

package blockchainvisualizer;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.DataOutputStream;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
//import java.net.HttpURLConnection;
import java.net.MalformedURLException;
//import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
//import java.sql.Date;

public class BlockchainGraph {

	

	public static void main(String[] args)
	{

		
		
		
		
		
		
		

		// == HttpURLConnection IMPLEMENTATION ==//

//		URL url;
//
//		try {
//			url = new URL(urlString);
//
//			HttpURLConnection con;
//
//			try {
//				con = (HttpURLConnection) url.openConnection();
//				//con.setRequestMethod("GET");
//			}
//
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

//		HttpURLConnection connection = null;
//
//		  try {
//		    //Create connection
//		    URL url = new URL(urlString);
//		    connection = (HttpURLConnection) url.openConnection();
//		    connection.setRequestMethod("POST");
//		    connection.setRequestProperty("Content-Type", 
//		        "application/x-www-form-urlencoded");
//
//		    connection.setRequestProperty("Content-Length", 
//		        Integer.toString(urlParameters.getBytes().length));
//		    connection.setRequestProperty("Content-Language", "en-US");  
//
//		    connection.setUseCaches(false);
//		    connection.setDoOutput(true);
//
//		    //Send request
//		    DataOutputStream wr = new DataOutputStream (
//		        connection.getOutputStream());
//		    wr.writeBytes(urlParameters);
//		    wr.close();
//
//		    //Get Response  
//		    InputStream is = connection.getInputStream();
//		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
//		    String line;
//		    while ((line = rd.readLine()) != null) {
//		      response.append(line);
//		      response.append('\r');
//		    }
//		    rd.close();
//		    return response.toString();
//		  } catch (Exception e) {
//		    e.printStackTrace();
//		    return null;
//		  } finally {
//		    if (connection != null) {
//		      connection.disconnect();
//		    }
//		  }
//		}

//		URL url = null;
//		try {
//			url = new URL(urlString);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		HttpURLConnection conn = null;
//		try {
//			conn = (HttpURLConnection) url.openConnection();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			conn.setRequestMethod("GET");
//		} catch (ProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		BufferedReader in = null;
//		try {
//			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		StringBuffer sb = new StringBuffer();
//
//		try {
//			while ((line = in.readLine()) != null) {
//				sb.append(line);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		try {
//			in.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		HttpURLConnection connection = null;
//
//		  try {
//		    //Create connection
//		    URL url = new URL(urlString);
//		    connection = (HttpURLConnection) url.openConnection();
//		    connection.setRequestMethod("POST");
////		    connection.setRequestProperty("Content-Length", 
////			        Integer.toString(urlParameters.getBytes().length));
//		    
//		    
//		    
//		    connection.setConnectTimeout(5000);
//		    connection.setReadTimeout(5000);
//
//
//		    //Get Response  
//		    InputStream is = connection.getInputStream();
//		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
//		    while ((line = rd.readLine()) != null) {
//		      response.append(line);
//		      response.append('\r');
//		    }
//		    rd.close();
//		  } catch (Exception e) {
//		    e.printStackTrace();
//		  } finally {
//		    if (connection != null) {
//		      connection.disconnect();
//		    }
//		  }

		// System.out.println(line);

		// System.out.println(enumAction.toUpperCase() + ": " + getBalance(line) + "
		// BNB");
		

	} // end of main()

	
	
	
	
	

} // end of Graph




