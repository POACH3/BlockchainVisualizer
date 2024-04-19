/*
 * Author: 		 T. Stratton
 * Date started: 18 APR 2024
 * Last updated: 19 APR 2024
 * 
 * File Contents:
 *  
 * 
 * Notes:
 * 	Fix so API key isn't a field.
 * 	Fix wei to BNB conversion.
 * 
 */

package cryptotransactionvisualizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Wallet {
	
	//String API_KEY; // FIXME
	
	String address; // wallet address
	double balance; // wallet balance in BNB
	
	final static long CRYPTO_COIN_UNIT = 1_000_000_000_000_000_000L; // Wei = 10^-18
	final static BigInteger CRYPTO_COIN_UNIT2 = new BigInteger("1000000000000000000"); // Wei = 10^-18
	final static double CRYPTO_COIN_UNIT3 = 1_000_000; // Wei = 10^-18
	
	
	ArrayList<Transaction> transactions = new ArrayList<>(); // all transactions of the searched wallet
	ArrayList<String> senders = new ArrayList<>(); 		     // wallets searched wallet has received from
	ArrayList<String> receivers = new ArrayList<>();		 // wallets searched wallet has sent to
	
	
	public Wallet(String address) // FIXME
	{
		//this.API_KEY = api_key; // FIXME
		
		this.address = address.toLowerCase();
		getBalance();
		getTransactions();
		getTransactedWallets();
	}
	
	
	/**
	 * Gets the number of transactions between this wallet
	 * and the provided wallet address.
	 * 
	 * @param otherWallet
	 * @return
	 */
	public int transactionCount(String otherWallet)
	{
		int count = 0; // number of transactions
		
		for (Transaction transaction : transactions)
		{
			if (transaction.getSender() == otherWallet || transaction.getReceiver() == otherWallet)
			{
				count++;
			}
		}
		
		return count;
	}
	
	
	/**
	 * 
	 * 
	 */
	public void getTransactedWallets()
	{
		for (Transaction t : transactions)
		{
			
			String sender = t.getSender();;
			String receiver = t.getReceiver();;
			
			if (!sender.equals(this.address) && !senders.contains(sender)) {
				this.senders.add(sender);
			}
			
			if (!receiver.equals(this.address) && !receivers.contains(receiver)) {
				this.receivers.add(receiver);
			}
			
			//System.out.println("From: " + sender + "   To: " + receiver);
		}
		
//		System.out.println("\n" + this.address + " received transactions from these wallets:\n");
//		for (String s : senders)
//		{
//			System.out.println(s);
//		}
//		
//		System.out.println("\n" + this.address + " sent transactions to these wallets:\n");
//		for (String s : receivers)
//		{
//			System.out.println(s);
//		}
		

	}
	
//	public ArrayList<String> getWalletsReceivedFrom(String address)
//	{
//		ArrayList<String> walletsReceivedFrom = new ArrayList<String>();
//		
//		for (Transaction t : transactions)
//		{
//			if (t.getSender() != address)
//			{
//				walletsReceivedFrom.add(t.getSender());
//			}
//		}
//		
//		return walletsReceivedFrom;
//	}
//	
//	
//	public ArrayList<String> getWalletsSentTo(String address)
//	{
//		ArrayList<String> walletsSentTo = new ArrayList<String>();
//
//		for (Transaction t : transactions)
//		{
//			if (t.getReceiver() != address)
//			{
//				walletsSentTo.add(t.getReceiver());
//			}
//		}
//		
//		return walletsSentTo;
//	}
	
	
	/**
	 * Gets the transactions for the provided wallet.
	 * 
	 * @param walletAddress
	 * @return
	 */
	public void getTransactions()
	{
		String enumAction = WalletActions.TXLIST.name().toLowerCase();
		String urlString = "https://api.bscscan.com/api?module=account" + 
				"&action=" + enumAction + 
				"&address=" + this.address + 
				"&startblock=" + 0 + 
				"&endblock=" + 99999999 + 
				"&page=" + 1 +					// optional pagination
				"&offset=" + 10 + 				// number of transactions per page (max 10000)
				"&sort=asc" + 					// ascending order
				"&apikey=" + BlockchainApp.API_KEY;
		
		//20356945

		String line = "";

		try
		{

			URL url = new URL(urlString);
			
			BlockchainApp.callLimiter.requestToken(); // get permission to make call
			try (InputStream stream = url.openStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
					// BufferedWriter writer = new BufferedWriter(new FileWriter("info.txt"));
					Scanner input = new Scanner(System.in))
			{

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

		}
		catch (MalformedURLException e)
		{
			System.out.print("Problem encountered regarding the following URL:\n" + urlString
					+ "\nEither no legal protocol could be found or the string could not be parsed.");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.print("Attempting to open a stream from the following URL:\n" + urlString + "\ncaused a problem.");
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
		for (int i = 0; i < allTxData.length; i++)
		{
			if (i == 0)
				allTxData[i] = allTxData[i].substring(1, allTxData[i].length());
			else
				allTxData[i] = allTxData[i].substring(2, allTxData[i].length());
		}
		
		// unparsed json individual transactions
//		for (String s : allTxData) {
//			System.out.println(s);
//		}
//		System.out.println("\n");
		
		
		HashMap<String, String> txData = new HashMap<>();
		
		
		for (int i = 0; i < allTxData.length; i++)
		{
			String[] singleTxData = allTxData[i].split(",");
			
//			for (String s : singleTxData) {
//				System.out.println(s);
//			}
			
			for (int j = 0; j < singleTxData.length; j++)
			{
				String[] txKvp = singleTxData[j].split(":");
				//System.out.println("Length of txKvp[1]: " + txKvp[1].length());
				if (txKvp.length > 1) {
					txData.put(txKvp[0].substring(1,txKvp[0].length()-1), (txKvp[1].substring(1,txKvp[1].length()-1)).toLowerCase());
				}
				else
				{
					txData.put(txKvp[0].substring(1,txKvp[0].length()-1), "");
				}

				//txData.put(txKvp[0], txKvp[1]);

			}
			
//			Transaction tx = new Transaction(Integer.parseInt(txData.get("blockNumber")),
//					unixTimeToDate(Integer.parseInt(txData.get("timeStamp"))),
//					txData.get("hash"),
//					txData.get("from"),
//					txData.get("to"),
//					convertWeiToBNB(txData.get("value")),
//					Integer.parseInt(txData.get("isError")),
//					Integer.parseInt(txData.get("txreceipt_status")));
			
			Transaction tx = new Transaction(Integer.parseInt(txData.get("blockNumber")),
					unixTimeToDate(Integer.parseInt(txData.get("timeStamp"))),
					txData.get("hash"),
					txData.get("from"),
					txData.get("to"),
					Double.parseDouble(txData.get("value")),
					Integer.parseInt(txData.get("isError")),
					Integer.parseInt(txData.get("txreceipt_status")));
			
			transactions.add(tx);
		}
		
		
		
//		String[] singleTX_1D = new String[20];
//		String[][] singleTX_2D = new String[20][2];
//		
//		for (int i = 0; i < allTxData.length; i++)
//		{
//			
//			singleTX_1D = allTxData[i].split(",");
//			
//			for (int j = 0; j < singleTX_1D.length; j++)
//				singleTX_2D[j] = singleTX_1D[j].split(":");
//			
//			for (int k = 0; k < singleTX_2D.length; k++)
//			{
//				if (singleTX_2D[k][1].length() > 2)
//					singleTX_2D[k][1] = singleTX_2D[k][1].substring(1,singleTX_2D[k][1].length()-1);
//				
//				System.out.println(singleTX_2D[k][0] + " : " + singleTX_2D[k][1]);
//			}
//			
//			//System.out.println("\n");
//			
//			Transaction tx = new Transaction(Integer.parseInt(singleTX_2D[0][1]),
//					unixTimeToDate(Integer.parseInt(singleTX_2D[1][1])),
//					singleTX_2D[2][1],
//					singleTX_2D[6][1],
//					singleTX_2D[7][1],
//					convertWeiToBNB(singleTX_2D[8][1]), // double
//					Integer.parseInt(singleTX_2D[11][1]),
//					Integer.parseInt(singleTX_2D[12][1]));
			
//			walletTransactions.add(tx);
			//System.out.println("\n");
//		}
			
		//return null;
	}
	
	
	
	/**
	 * Gets the current balance in BNB of the provided wallet.
	 * 
	 * @param walletAddress
	 * @return
	 */
	public void getBalance()
	{
		String enumAction = WalletActions.BALANCE.name().toLowerCase();
		String urlString = "https://api.bscscan.com/api?module=account" + 
							"&action=" + enumAction + 
							"&address=" + this.address + 
							"&apikey=" + BlockchainApp.API_KEY;

		String line = "";

		try
		{
			URL url = new URL(urlString);
			
			BlockchainApp.callLimiter.requestToken(); // get permission to make call
			try (InputStream stream = url.openStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
					// BufferedWriter writer = new BufferedWriter(new FileWriter("info.txt"));
					Scanner input = new Scanner(System.in))
			{
				line = reader.readLine();
			}
		}
		catch (MalformedURLException e)
		{
			System.out.print("Problem encountered regarding the following URL:\n" + urlString
					+ "\nEither no legal protocol could be found or the string could not be parsed.");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.print("Attempting to open a stream from the following URL:\n" + urlString + "\ncaused a problem.");
			e.printStackTrace();
		}
		
		// unparsed json balance result
		//System.out.println(line);
		//{"status":"1","message":"OK","result":"6999188248887296596"}
		
		// parsing what is returned
		String[] messages = line.split(",");
		double balance = 0;
		String balanceStr = "";

		try
		{

			String weiBalance = line.substring(39,line.length() - 2);
			//balance = convertWeiToBNB(weiBalance);
			balance = Double.parseDouble(weiBalance);

		}
		catch (NumberFormatException | ArrayIndexOutOfBoundsException e)
		{
			System.err.println("Error: " + line);
		}

//		System.out.println(balance2.divide(CRYPTO_COIN_UNIT2) + " BNB");
//		System.out.println(balance1 / CRYPTO_COIN_UNIT3 + " BNB");
//		System.out.println(String.valueOf((balance3 / CRYPTO_COIN_UNIT) * Math.pow(10,12)) + " BNB");
		
		//return balance;
		this.balance = balance;
	} // end of getBalance()
	
	
	/**
	 * Converts Wei to BNB.
	 * 
	 * @param wei
	 * @return
	 */
	public static double convertWeiToBNB (String wei)
	{
		double bnb;
		StringBuilder sb = new StringBuilder("0000000000");
		
		
		//balanceStr = new StringBuilder(weiBalance).insert((int) (weiBalance.length() - Math.log10(CRYPTO_COIN_UNIT)), ".").toString(); // move decimal to convert to BNB


		sb = sb.append(wei);
		sb = sb.insert((int) (sb.length() - Math.log10(CRYPTO_COIN_UNIT)), ".");
		bnb = Double.parseDouble(sb.toString());
		
		return bnb;
	} // end of convertWei()
	
	
	/**
	 * Converts unix time to a standard date.
	 * 
	 * @param unixTime
	 * @return
	 */
	public Date unixTimeToDate(int unixTime)
	{	
		Date date = new Date((long)unixTime*1000);
		
//		System.out.println(time.getTime());
//		Date time2 = new Date((long)time.getTime());
//		System.out.println(time2);
		
		return date;
	} // end of unixTimeToDate()
}
