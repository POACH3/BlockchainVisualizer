/*
 * Author: 		 T. Stratton
 * Date started: 18 APR 2024
 * Last updated: 04 JUL 2024
 * 
 * File Contents:
 *  
 * 
 * Notes:
 * 
 * 
 */

package blockchainVisualizer;

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
	
	String address; 													 // wallet address
	double balance; 													 // wallet balance in BNB
	
	ArrayList<Transaction> transactions = new ArrayList<>(); 			 // all transactions of the searched wallet
	ArrayList<String> firstDegreeWallets = new ArrayList<>();			 // all wallets directly associated to this wallet (transactions)
	ArrayList<String> senders = new ArrayList<>(); 		     			 // wallets searched wallet has received from
	ArrayList<String> receivers = new ArrayList<>();		 			 // wallets searched wallet has sent to
	
	HashMap<String, Integer> transactionSentCount = new HashMap<>(); 	 // number of transactions sent by wallet to each other wallet
	HashMap<String, Integer> transactionReceivedCount = new HashMap<>(); // number of transactions received by wallet from each other wallet
	
	HashMap<String, Double> transactionsSentValue = new HashMap<>(); 	 // value of transactions sent by wallet to each other wallet
	HashMap<String, Double> transactionsReceivedValue = new HashMap<>(); // value of transactions received by wallet from each other wallet
	
	
	public Wallet(String address)
	{
		this.address = address.toLowerCase();
		//getBalance(); 												 // set balance later to reduce API calls (batch balance calls)
		
		try
		{
			getTransactions();
		}
		catch (Exception e)
		{
			System.out.println("Error in getting the transactions from BSCScan.\n");
			e.printStackTrace();
		}
		
		setFirstDegreeWallets();
		setTransactionCount();
		setTransactionValue();
	}
	
	
	/**
	 * Gets the value of transactions between this wallet
	 * and all other wallets with which it has transacted.
	 */
	private void setTransactionValue()
	{		
		// initialize value to 0
		for (Transaction transaction : transactions)
		{
			String sender = transaction.getSender();
			String receiver = transaction.getReceiver();
			
			if (sender.equals(this.address) && !transactionsSentValue.containsKey(receiver))
			{				
				transactionsSentValue.put(receiver, 0.0);
			}
			
			if (receiver.equals(this.address) && !transactionsReceivedValue.containsKey(sender))
			{
				transactionsReceivedValue.put(sender, 0.0);
			}
		}

		// update to actual value
		for (Transaction transaction : transactions)
		{
			String sender = transaction.getSender();
			String receiver = transaction.getReceiver();
			
			if (sender.equals(this.address))
			{
				double currentValue = transactionsSentValue.get(receiver);
				transactionsSentValue.put(receiver, currentValue + transaction.getValueBNB());
			}
			else // receiver must be this wallet
			{
				double currentValue = transactionsReceivedValue.get(sender);
				transactionsReceivedValue.put(sender, currentValue + transaction.getValueBNB());
			}
		}		
	} // end of setTransactionValue()
	
	
	/**
	 * Gets the number of transactions between this wallet
	 * and each of the other wallets with which it has transacted.
	 */
	private void setTransactionCount()
	{			
		// initialize number to 0
		for (String sender : senders)
		{
			if (!sender.equals(this.address) && !transactionReceivedCount.containsKey(sender))
			{
				transactionReceivedCount.put(sender, 0);
			}
		}
		for (String receiver : receivers)
		{
			if (!receiver.equals(this.address) && !transactionSentCount.containsKey(receiver))
			{
				transactionSentCount.put(receiver, 0);
			}
		}
		
		// update to actual number
		for (Transaction transaction : transactions)
		{
			String sender = transaction.getSender();
			String receiver = transaction.getReceiver();
			
			//System.out.println(transaction);
			
			if (receiver.equals(this.address))
			{
				int currentNumber = transactionReceivedCount.get(sender);
				currentNumber++;
				transactionReceivedCount.put(sender, currentNumber);
			}
			else // receiver must be the other wallet
			{
				int currentNumber = transactionSentCount.get(receiver);
				currentNumber++;
				transactionSentCount.put(receiver, currentNumber);
			}
		}
	} // end of setTransactionCount()
	
	
	/**
	 * Populates three lists:
	 * 	- all wallet addresses that have sent to or received from this wallet
	 *  - any wallet address that are senders to this wallet
	 * 	- any wallet addresses that are receivers from this wallet
	 */
	private void setFirstDegreeWallets()
	{
		for (Transaction transaction : transactions)
		{
			
			String sender = transaction.getSender();
			String receiver = transaction.getReceiver();
			
			if (!sender.equals(this.address) && !senders.contains(sender)) {
				this.senders.add(sender);
			}
			
			if (!receiver.equals(this.address) && !receivers.contains(receiver)) {
				this.receivers.add(receiver);
			}
			
			if (!firstDegreeWallets.contains(receiver))
			{
				firstDegreeWallets.add(receiver);
			}
			
			if (!firstDegreeWallets.contains(sender))
			{
				firstDegreeWallets.add(sender);
			}
		}
	} // end of setFirstDegreeWallets()
	
	
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
				"&offset=" + 50 + 				// number of transactions per page (max 10000)
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
		//System.out.println(jsonData);
		
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
					Double.parseDouble(txData.get("value")),
					Integer.parseInt(txData.get("isError")),
					Integer.parseInt(txData.get("txreceipt_status")));
			
			transactions.add(tx);
		}

	}
	
	
	/**
	 * Gets the current balance in BNB of the provided wallet.
	 * 
	 * Note: It is best to not use this method, if the balances of multiple
	 * wallets are needed. To save API calls, the balances of up to 20 addresses
	 * at a time may be bundled in one call.
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
		//System.out.println(jsonData);
		//{"status":"1","message":"OK","result":"6999188248887296596"}
		
		// call error
		if (jsonData.equals("{\"status\":\"0\",\"message\":\"NOTOK\",\"result\":\"Max rate limit reached\"}"))
		{
			throw new Exception("Max rate limit reached.");
		}

		double weiBalance = Double.parseDouble(jsonData.substring(39,jsonData.length() - 2));
		double bnbBalance = weiBalance * Math.pow(10, -18);

		this.balance = bnbBalance;
	} // end of getBalance()
	
	
	/**
	 * Converts unix time to a UTC time and date.
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
