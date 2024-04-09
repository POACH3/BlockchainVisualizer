package cryptotransactionvisualizer;

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

	final static long CRYPTO_COIN_UNIT = 1_000_000_000_000_000_000L; // Wei = 10^-18
	final static BigInteger CRYPTO_COIN_UNIT2 = new BigInteger("1000000000000000000"); // Wei = 10^-18
	final static double CRYPTO_COIN_UNIT3 = 1_000_000; // Wei = 10^-18
	final static String API_KEY = "K1541JHJGC6UET8F1AYNB8VW1FPI1K9688";
	static ArrayList<Transaction> walletTransactions = new ArrayList<>();

	public static void main(String[] args) {

		String walletAddress = "0xf3d7d404D3B8A5ab5a45D7573e44a6CFf37D3c89";
		String walletAddress2 = "0xaCD208B1fe8D117169d8C3ebA5aBa8C3effece84";
		
//		System.out.printf("Balance: %.3f BNB\n", getBalance(walletAddress));

		
		
		// == InputStream IMPLEMENTATION ==//

		String enumAction = WalletActions.TXLIST.name().toLowerCase();
		String urlString = "https://api.bscscan.com/api?module=account" + 
				"&action=" + enumAction + 
				"&address=" + walletAddress + 
				"&startblock=" + 0 + 
				"&endblock=" + 99999999 + 
				"&page=" + 1 +					// optional pagination
				"&offset=" + 5 + 				// number of transactions per page (max 10000)
				"&sort=asc" + 					// ascending order
				"&apikey=" + API_KEY;

		String line = "";

		try {

			URL url = new URL(urlString);

			try (InputStream stream = url.openStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
					// BufferedWriter writer = new BufferedWriter(new FileWriter("info.txt"));
					Scanner input = new Scanner(System.in)) {

//						while ((line = reader.readLine()) != null) {
				//
//							if (line != null) {
//								System.out.println(line);
//								writer.append(line + "\n");
//								line = reader.readLine();
//							}
//							
//							input.nextLine();
//						}

				line = reader.readLine();

			}

		} catch (MalformedURLException e) {
			System.out.print("Problem encountered regarding the following URL:\n" + urlString
					+ "\nEither no legal protocol could be found or the string could not be parsed.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out
					.print("Attempting to open a stream from the following URL:\n" + urlString + "\ncaused a problem.");
			e.printStackTrace();
		}
		
		// raw output from BSC Scan
		//System.out.println(line);
		
		// parse
		line = line.substring(1, line.length()-1);
		String[] messages = line.split(",", 3); // unnecessary?
		//System.out.println("\n");
		
		String result = messages[2].substring(10, messages[2].length()-1);
		//System.out.println(result);
		//System.out.println("\n");
		
		String[] allTxData = result.split("}");
		
		// trim off left curly braces and commas
		for (int i = 0; i < allTxData.length; i++) {
			if (i == 0)
				allTxData[i] = allTxData[i].substring(1, allTxData[i].length());
			else
				allTxData[i] = allTxData[i].substring(2, allTxData[i].length());
		}
		
		// unparsed individual transactions
//		for (String s : allTxData) {
//			System.out.println(s);
//		}
//		System.out.println("\n");
		
		String[] singleTX_1D = new String[20];
		String[][] singleTX_2D = new String[20][2];
		
		for (int i = 0; i < allTxData.length; i++) {
			
			singleTX_1D = allTxData[i].split(",");
			
			for (int j = 0; j < singleTX_1D.length; j++)
				singleTX_2D[j] = singleTX_1D[j].split(":");
			
			for (int k = 0; k < singleTX_2D.length; k++) {
				if (singleTX_2D[k][1].length() > 2)
					singleTX_2D[k][1] = singleTX_2D[k][1].substring(1,singleTX_2D[k][1].length()-1);
				
				//System.out.println(singleTX_2D[k][1]);
			}
			
			//System.out.println("\n");
			
			Transaction tx = new Transaction(Integer.parseInt(singleTX_2D[0][1]),
					unixTimeToDate(Integer.parseInt(singleTX_2D[1][1])),
					singleTX_2D[2][1],
					singleTX_2D[6][1],
					singleTX_2D[7][1],
					convertWeiToBNB(singleTX_2D[8][1]), // double
					Integer.parseInt(singleTX_2D[11][1]),
					Integer.parseInt(singleTX_2D[12][1]));
			
			walletTransactions.add(tx);
			//System.out.println("\n");
			

			
//			Transaction tx = new Transaction(Integer.parseInt(singleTX_2D[0][1]),
//					Integer.parseInt(singleTX_2D[1][1]),
//					//singleTX_2D[2][1],
//					//Integer.parseInt(singleTX_2D[3][1]),
//					//singleTX_2D[4][1],
//					//Integer.parseInt(singleTX_2D[5][1]),
//					singleTX_2D[6][1],
//					singleTX_2D[7][1],
//					Long.parseLong(singleTX_2D[8][1]),
//					//Integer.parseInt(singleTX_2D[9][1]),
//					//Integer.parseInt(singleTX_2D[10][1]),
//					Integer.parseInteger(singleTX_2D[11][1]),
//					Integer.parseInteger(singleTX_2D[12][1]),
//					//singleTX_2D[13][1],
//					//singleTX_2D[14][1],
//					//Integer.parseInt(singleTX_2D[15][1]),
//					//Integer.parseInt(singleTX_2D[16][1]),
//					//Integer.parseInt(singleTX_2D[17][1]),
//					//singleTX_2D[18][1],
//					//singleTX_2D[19][1])
//					);
				
		}
		
//		for (int i = 0; i < 20; i++) {
//			System.out.println(singleTX_1D[i]);
//		}
		System.out.println("\n");
		
		
		// query result order changes when I uncomment this
		
		System.out.println("  BLOCK    |      DATE      |                         TRANSACTION HASH                            |                     FROM                     |                      TO                     |      VALUE "); 
		System.out.println("================================================================================================================================================================================================================");
		for (Transaction t : walletTransactions) {
			System.out.println(t);
		}
		

		
//		String[] transactionData = new String[20];
//		String[] transactionData2 = new String[20];
//		
//		for (int i = 0; i < allTxData.length; i++) {
//			
//			transactionData = allTxData[i].split(",");
//			
//			
//			
//			
//			//order of the query results changes
////			transactionData2[0] = transactionData[0].substring(15,transactionData[0].length()-1); //blocknumber
////			transactionData2[1] = transactionData[1].substring(13,transactionData[1].length()-1); //blockhash
////			transactionData2[2] = transactionData[2].substring(13,transactionData[2].length()-1); //timestamp
////			transactionData2[3] = transactionData[3].substring(8,transactionData[3].length()-1); //hash
////			transactionData2[4] = transactionData[4].substring(9,transactionData[4].length()-1); //nonce
////			transactionData2[5] = transactionData[5].substring(20,transactionData[5].length()-1); //transactionindex
////			transactionData2[6] = transactionData[6].substring(8,transactionData[6].length()-1); //from
////			transactionData2[7] = transactionData[7].substring(6,transactionData[7].length()-1); //to
////			transactionData2[8] = transactionData[8].substring(9,transactionData[8].length()-1); //value
////			transactionData2[9] = transactionData[9].substring(7,transactionData[9].length()-1); //gas
////			transactionData2[10] = transactionData[10].substring(12,transactionData[10].length()-1); //gasprice
////			transactionData2[11] = transactionData[11].substring(9,transactionData[11].length()-1); //input
////			transactionData2[12] = transactionData[12].substring(12,transactionData[12].length()-1); //methodID
////			transactionData2[13] = transactionData[13].substring(10,transactionData[13].length()-1); //functionName
////			transactionData2[14] = transactionData[14].substring(18,transactionData[14].length()-1); //contractAdd
////			transactionData2[15] = transactionData[15].substring(21,transactionData[15].length()-1); //cumGas
////			transactionData2[16] = transactionData[16].substring(20,transactionData[16].length()-1); //txReceipt
////			transactionData2[17] = transactionData[17].substring(11,transactionData[17].length()-1); //gasUsed
////			transactionData2[18] = transactionData[18].substring(17,transactionData[18].length()-1); //confirmations
////			transactionData2[19] = transactionData[19].substring(11,transactionData[19].length()-1); //isError
//			
//			//walletTransactions.add(Transaction transaction = new Transaction(transactionData2[0]));
//
//			
//		}
		
		

	
		

		try {

			//String[] elements = messages[2].split("\"");
			
//			for (String s : elements) {
//				System.out.println(s);
//			}

//			balanceStr = new StringBuilder(elements[3])
//					.insert((int) (elements[3].length() - Math.log10(CRYPTO_COIN_UNIT)), ".").toString();
//			balance = Double.parseDouble(balanceStr);

		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.err.println("Error: " + line);
		}

		
		
		
		
		

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

	public static double getBalance(String walletAddress) {

		String enumAction = WalletActions.BALANCE.name().toLowerCase();
		String urlString = "https://api.bscscan.com/api?module=account" + "&action=" + enumAction + "&address="
				+ walletAddress + "&apikey=" + API_KEY;

		String line = "";

		try {

			URL url = new URL(urlString);

			try (InputStream stream = url.openStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
					// BufferedWriter writer = new BufferedWriter(new FileWriter("info.txt"));
					Scanner input = new Scanner(System.in)) {

//				while ((line = reader.readLine()) != null) {
//
//					if (line != null) {
//						System.out.println(line);
//						writer.append(line + "\n");
//						line = reader.readLine();
//					}
//					
//					input.nextLine();
//				}

				line = reader.readLine();
				//System.out.println(line);

			}

		} catch (MalformedURLException e) {
			System.out.print("Problem encountered regarding the following URL:\n" + urlString
					+ "\nEither no legal protocol could be found or the string could not be parsed.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out
					.print("Attempting to open a stream from the following URL:\n" + urlString + "\ncaused a problem.");
			e.printStackTrace();
		}
		
		// parsing what is returned
		String[] messages = line.split(",");
//		double balance1 = 0;
//		BigInteger balance2 = new BigInteger("0");
//		double balance3 = 0;
		double balance = 0;
		String balanceStr = "";

		try {

			String[] elements = messages[2].split("\"");

//			balance1 = Long.parseLong(balanceString[3].substring(0,8));
//			balance2 = new BigInteger(balanceString[3]);
//			balance3 = Double.parseDouble(balanceString[3].substring(0,balanceString[3].length() - 12));

			balanceStr = new StringBuilder(elements[3])
					.insert((int) (elements[3].length() - Math.log10(CRYPTO_COIN_UNIT)), ".").toString();
			balance = Double.parseDouble(balanceStr);

		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.err.println("Error: " + line);
		}

//		System.out.println(balance2.divide(CRYPTO_COIN_UNIT2) + " BNB");
//		System.out.println(balance1 / CRYPTO_COIN_UNIT3 + " BNB");
//		System.out.println(String.valueOf((balance3 / CRYPTO_COIN_UNIT) * Math.pow(10,12)) + " BNB");
		return balance;

	} // end of getBalance()
	
	public static double convertWeiToBNB (String wei) {
		double bnb;
		StringBuilder sb = new StringBuilder("0000000000");

		sb = sb.append(wei);
		sb = sb.insert((int) (sb.length() - Math.log10(CRYPTO_COIN_UNIT)), ".");
		bnb = Double.parseDouble(sb.toString());
		
		return bnb;
	} // end of convertWei()
	
	public static Date unixTimeToDate(int unixTime) {
		
		Date date = new Date((long)unixTime*1000);
		
//		System.out.println(time.getTime());
//		Date time2 = new Date((long)time.getTime());
//		System.out.println(time2);
		
		return date;
	} // end of unixTimeToDate()
	
	
	
	
	

} // end of Graph




