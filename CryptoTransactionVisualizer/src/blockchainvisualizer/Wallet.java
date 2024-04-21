/*
 * Author: 		 T. Stratton
 * Date started: 18 APR 2024
 * Last updated: 21 APR 2024
 * 
 * File Contents:
 *  
 * 
 * Notes:
 * 	Maybe change weiToBNB() parameter to double?
 * 
 */

package blockchainvisualizer;

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

/**
 * This class is a Binance Smart Chain cryptocurrency wallet.
 * It provides a means to query wallet and transaction information pulled
 * from the BscScan.com through its API.
 * An API key must be provided.
 */
public class Wallet {
	
	String address; // wallet address
	double balance; // wallet balance in BNB
	
	ArrayList<Transaction> transactions = new ArrayList<>(); // all transactions of the searched wallet
	ArrayList<String> senders = new ArrayList<>(); 		     // wallets searched wallet has received from
	ArrayList<String> receivers = new ArrayList<>();		 // wallets searched wallet has sent to
	
	HashMap<String, Integer> numTransactions = new HashMap<>(); // number of transactions between wallets
	HashMap<String, Double> netValueTransactions = new HashMap<>(); // net value of transactions between wallets (negative is more money transferred out than into this wallet)
	//HashMap<String, WalletInfo> walletInfo;
	
	
	public Wallet(String address)
	{
		this.address = address.toLowerCase();
		//getBalance(); // set balance later to reduce API calls
		try {
			getTransactions();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getTransactedWallets();
		//transactionCount();
		transactionValue();
	}
	
	
	/**
	 * Gets the value of transactions between this wallet
	 * and all other wallets with which it has transacted.
	 */
	private void transactionValue()
	{
		for (String sender : senders)
		{
			if (!netValueTransactions.containsKey(sender))
			{
				netValueTransactions.put(sender, 0.0);
			}
		}
		for (String receiver : receivers)
		{
			if (!netValueTransactions.containsKey(receiver))
			{
				netValueTransactions.put(receiver, 0.0);
			}
		}
		
		
		for (Transaction transaction : transactions)
		{
			String sender = transaction.getSender();
			String receiver = transaction.getReceiver();
			
			if (sender == this.address)
			{
				double currentValue = netValueTransactions.get(sender);
				netValueTransactions.put(sender, currentValue - transaction.getValue());
			}
			else // receiver must be this wallet
			{
				double currentValue = netValueTransactions.get(sender);
				netValueTransactions.put(receiver, currentValue + transaction.getValue());
			}
		}
	}
	
	
	/**
	 * Gets the number of transactions between this wallet
	 * and each of the other wallets with which it has transacted.
	 */
	private void transactionCount()
	{		
		System.out.println("numTransactions: " + numTransactions.size());
		
		for (Transaction transaction : transactions)
		{
			String sender = transaction.getSender();
			String receiver = transaction.getReceiver();
			
			if (sender != this.address)
			{
				int currentNumber = numTransactions.get(sender);
				numTransactions.put(sender, currentNumber++);
			}
			else // receiver must be the other wallet
			{
				int currentNumber = numTransactions.get(receiver);
				numTransactions.put(receiver, currentNumber++);
			}
		}
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
		
		// prep numTransactions hashmap with address, 0
		for (String sender : senders)
		{
			if (!numTransactions.containsKey(sender))
			{
				numTransactions.put(sender, 0);
			}
		}
		for (String receiver : receivers)
		{
			if (!numTransactions.containsKey(receiver))
			{
				numTransactions.put(receiver, 0);
			}
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
	 * @throws Exception 
	 */
	public void getTransactions() throws Exception
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


		String jsonData = "";

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

				jsonData = reader.readLine();

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
		System.out.println(jsonData);
		
		// call error
		if (jsonData.equals("{\"status\":\"0\",\"message\":\"NOTOK\",\"result\":\"Max rate limit reached\"}"))
		{
			throw new Exception("Max rate limit reached.");
		}
		
		// parse
		jsonData = jsonData.substring(1, jsonData.length()-1);
		String[] messages = jsonData.split(",", 3); // unnecessary?
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
				if (txKvp.length > 1)
				{
					txData.put(txKvp[0].substring(1,txKvp[0].length()-1), (txKvp[1].substring(1,txKvp[1].length()-1)).toLowerCase());
				}
				else
				{
					txData.put(txKvp[0].substring(1,txKvp[0].length()-1), "");
				}
			}
			
			Transaction tx = new Transaction(Integer.parseInt(txData.get("blockNumber")),
					unixTimeToDate(Integer.parseInt(txData.get("timeStamp"))),
					txData.get("hash"),
					txData.get("from"),
					txData.get("to"),
					convertWeiToBNB(txData.get("value")),
					Integer.parseInt(txData.get("isError")),
					Integer.parseInt(txData.get("txreceipt_status")));
			
			transactions.add(tx);
		}

	}
	
	
	/**
	 * Gets the current balance in BNB of the provided wallet.
	 * 
	 * @param walletAddress
	 * @return
	 * @throws Exception 
	 */
	public void getBalance() throws Exception
	{
		String enumAction = WalletActions.BALANCE.name().toLowerCase();
		String urlString = "https://api.bscscan.com/api?module=account" + 
							"&action=" + enumAction + 
							"&address=" + this.address + 
							"&apikey=" + BlockchainApp.API_KEY;

		String jsonData = "";

		try
		{
			URL url = new URL(urlString);
			
			BlockchainApp.callLimiter.requestToken(); // get permission to make call
			try (InputStream stream = url.openStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
					// BufferedWriter writer = new BufferedWriter(new FileWriter("info.txt"));
					Scanner input = new Scanner(System.in))
			{
				jsonData = reader.readLine();
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
		System.out.println(jsonData);
		//{"status":"1","message":"OK","result":"6999188248887296596"}
		
		// call error
		if (jsonData.equals("{\"status\":\"0\",\"message\":\"NOTOK\",\"result\":\"Max rate limit reached\"}"))
		{
			throw new Exception("Max rate limit reached.");
		}

		String weiBalance = jsonData.substring(39,jsonData.length() - 2);
		double balance = convertWeiToBNB(weiBalance);

		this.balance = balance;
	} // end of getBalance()
	
	
	
	/**
	 * Converts Wei to BNB by inserting a decimal 18 decimal
	 * places to the left (10^-18).
	 * Zeros will be added if necessary to move the decimal.
	 * 
	 * @param wei
	 * @return
	 */
	public static double convertWeiToBNB (String wei)
	{	
		if (wei.equals("0")) { return Double.parseDouble(wei); }
		
		if (wei.length() < 18)
		{
			StringBuilder zeros = new StringBuilder();
			
			for (int i = 0; i < 18 - wei.length(); i++)
			{
				zeros = zeros.append("0");
			}
			
			wei = (zeros.toString()).concat(wei);
		}
		
		StringBuilder bnb = new StringBuilder(wei);
		
		bnb.insert(wei.length() - 18, '.');
		
		return Double.parseDouble(bnb.toString());
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
