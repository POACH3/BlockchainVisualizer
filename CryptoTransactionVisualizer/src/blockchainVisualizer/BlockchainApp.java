/*
 * Author: 		 T. Stratton
 * Date started: 15 NOV 2023
 * Last updated: 26 AUG 2024
 * 
 * File Contents:
 * 	main
 * 	get API key from secrets file
 *  
 * 
 * Notes:
 * 	parse json to see if call results in error
 * 
 *  graph with nodes of type wallet and edges of type transaction
 * 
 */

package blockchainVisualizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Properties;


/*
 * This class uses the BscScan API to pull wallet and transaction
 * information from the Binance Smart Chain blockchain.
 */
public class BlockchainApp
{
	static String API_KEY = "";
	static CallLimiter callLimiter;
	
	public static void main(String[] args)
	{
		try { API_KEY = getSecret("resources/secrets.properties", "api.key1"); }
		catch (Exception ex) { System.out.println("OH NOES!   " + ex.getMessage()); }
		
		// manage API calls
		final int RATE_LIMIT = 4; // can be 5
		callLimiter = new CallLimiter(RATE_LIMIT);
		Thread limiterThread = new Thread(callLimiter);
		limiterThread.start();
		
		//{"status":"0","message":"NOTOK","result":"Max rate limit reached"}
		
		
		String searchedAddress;						 				   // wallet address of searched wallet
		Wallet searchedWallet;										   // searched wallet
		HashMap<String, Wallet> firstDegreeWallets = new HashMap<>();  // wallets with which the searched wallet has transacted
		HashMap<String, Double> walletBalances = new HashMap<>();      // balance of all wallets
		
		String walletAddress = "0xf3d7d404D3B8A5ab5a45D7573e44a6CFf37D3c89";
		String walletAddress2 = "0xaCD208B1fe8D117169d8C3ebA5aBa8C3effece84";
		String walletAddress3 = "0xaAA649A830AF14F38c135e15bB5B08a5e7F2B4eC";
		
		
//		ArrayList<Wallet> wallets = new ArrayList<>();
//		
//		Wallet wallet1 = new Wallet(walletAddress);
//		Wallet wallet2 = new Wallet(walletAddress2);
//		Wallet wallet3 = new Wallet(walletAddress3);
//		
//		wallets.add(wallet1);
//		wallets.add(wallet2);
//		wallets.add(wallet3);
//		
//		HashMap<String,Double> balances = getBalances(wallets);
//		
//		for (HashMap.Entry<String,Double> kvp : balances.entrySet())
//		{
//			System.out.println(kvp.getKey().toString() + ": " + kvp.getValue().toString());
//		}

		System.out.println("Searching wallet: " + walletAddress.toLowerCase()); //testing
		System.out.println(); //testing
		
		searchedAddress = walletAddress.toLowerCase();
	//	searchedWallet = new Wallet(searchedAddress);
		
//		System.out.println("Searching wallet: " + searchedAddress);
//		System.out.println();
		
		// list of transacted wallets
	//	ArrayList<Transaction> searchedWalletTransactions = searchedWallet.transactions;
		
		// info about searched wallet transactions
//		System.out.println(searchedWalletTransactions.size() + " transactions: ");
//		System.out.println();
//		
//		for (Transaction t : searchedWalletTransactions)
//		{
//			System.out.println(t);
//		}
//		System.out.println();
//		
//		System.out.println("Number of transactions sent by this wallet");
//		for (HashMap.Entry<String, Integer> kvp : searchedWallet.transactionSentCount.entrySet())
//		{
//			System.out.println(kvp.getKey() + ", " + searchedWallet.transactionSentCount.get(kvp.getKey()));
//		}
//		System.out.println();
//		
//		System.out.println("Number of transactions received by this wallet");
//		for (HashMap.Entry<String, Integer> kvp : searchedWallet.transactionReceivedCount.entrySet())
//		{
//			System.out.println(kvp.getKey() + ", " + searchedWallet.transactionReceivedCount.get(kvp.getKey()));
//		}
//		System.out.println();
//		
//		System.out.println("Total value of transactions sent by this wallet");
//		for (HashMap.Entry<String, Double> kvp : searchedWallet.transactionsSentValue.entrySet())
//		{
//			System.out.printf("%s, %f BNB\n", kvp.getKey(), searchedWallet.transactionsSentValue.get(kvp.getKey()));
//		}
//		System.out.println();
//		
//		System.out.println("Total value of transactions received by this wallet");
//		for (HashMap.Entry<String, Double> kvp : searchedWallet.transactionsReceivedValue.entrySet())
//		{
//			System.out.printf("%s, %f BNB\n", kvp.getKey(), searchedWallet.transactionsReceivedValue.get(kvp.getKey()));
//		}
//		System.out.println();
		
		// create 1st degree wallets
//		for (String address : searchedWallet.senders)
//		{
//			firstDegreeWallets.put(address, new Wallet(address));
//		}
//		for (String address : searchedWallet.receivers)
//		{
//			firstDegreeWallets.put(address, new Wallet(address));
//		}
		
//		System.out.println();
//		System.out.println("Senders: " + searchedWallet.senders.size() + " (wallets sent to the searched wallet)");
//		for (String sender : searchedWallet.senders)
//		{
//			System.out.println(sender);
//		}
//		System.out.println();
//		
//		System.out.println("Receivers: " + searchedWallet.receivers.size() + " (wallets received from the searched wallet)");
//		for (String receiver : searchedWallet.receivers)
//		{
//			System.out.println(receiver);
//		}
//		System.out.println();
//		
//		System.out.println("First degree wallets: " + searchedWallet.firstDegreeWallets.size());
//		for (String address : searchedWallet.firstDegreeWallets)
//		{
//			System.out.println(address);
//		}
//		
//		
//		for (HashMap.Entry<String, Wallet> kvp : firstDegreeWallets.entrySet())
//		{
//			System.out.println(kvp.getKey());
//		}
//		System.out.println();
//		
//		// set balances of all wallets
//		ArrayList<String> allWallets = new ArrayList<>();
//		allWallets.add(searchedWallet.address);
//		
//		for (HashMap.Entry<String, Wallet> kvp : firstDegreeWallets.entrySet())
//		{
//			allWallets.add(kvp.getKey());
//		}
//		
//		HashMap<String, Double> allWalletBalances;
		try
		{
//			allWalletBalances = getBalances(allWallets);
//
//			searchedWallet.balance = allWalletBalances.get(searchedWallet.address);
//			
//			System.out.println();
//			
//			for (HashMap.Entry<String, Double> kvp : allWalletBalances.entrySet())
//			{
//				//System.out.println(kvp.getKey());
//				//System.out.println(kvp.getValue());
//				
//				if (!kvp.getKey().equals(searchedAddress))
//				{
//					firstDegreeWallets.get(kvp.getKey()).balance = kvp.getValue();
//				}
//			}
//		
//		
//	
//		
//			// balance of all wallets
//			for (HashMap.Entry<String, Wallet> kvp : firstDegreeWallets.entrySet())
//			{
//				walletBalances.put(kvp.getKey(), kvp.getValue().balance);
//			}
//			
//			
//			
//			System.out.print("Current wallet balances: \n");
//			
//			for (HashMap.Entry<String, Double> kvp : allWalletBalances.entrySet())
//			{
//				System.out.printf("%s: %.10f BNB\n", kvp.getKey(), kvp.getValue());
//			}
			
			// number of transactions between each wallet
			//HashMap<String, Integer> numTransactions = searchedWallet.transactionCount;
		
		
			// net value of all transactions between wallets
			//HashMap<String, Double> netValueTransactions = searchedWallet.transactionsNetValue;
			
			
			
			
//			BlockchainGraph graph = new BlockchainGraph(searchedWalletTransactions);
//			System.out.println(graph);
			
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		GUI gui = new GUI();
		
//		Scanner scnr = new Scanner(System.in);
//		boolean cont = true; // user wants to continue program
		
//		while(cont)
//		{
//			System.out.print("Enter a wallet address: ");
//			//String address = scnr.nextLine();
//			String address ="0xf3d7d404D3B8A5ab5a45D7573e44a6CFf37D3c89";
//			
//			
//			System.out.print("Current wallet balance: ");
//			System.out.printf("%.3f BNB\n\n", getBalance(address));
//			
//			System.out.println(address + " received transactions from these wallets:\n");
//			getTransactions(address);
//			for (String s : getWalletsReceivedFrom(address))
//			{
//				System.out.println(s);
//			}
//			System.out.println();
//			
//			System.out.println(address + " sent transactions to these wallets:\n");
//			getWalletsSentTo(address);
//			for (String s : getWalletsSentTo(address))
//			{
//				System.out.println(s);
//			}
//			System.out.println();
//			
//			
//			System.out.println("  BLOCK    |      DATE      |                         TRANSACTION HASH                             |                     FROM                     |                      TO                     |      VALUE "); 
//			System.out.println("================================================================================================================================================================================================================");
//			for (Transaction t : walletTransactions)
//			{
//				System.out.println(t);
//			}
//			
//			
//			
//			System.out.print("Press 'Q' to quit: ");
//			String choice = scnr.nextLine();
//			
//			if (choice == "Q" || choice == "q")
//			{
//				cont = false;
//			}
//		}
			




	} // end of main()
	
	
	/**
	 * Returns the API key from the secrets file.
	 * 
	 * @param keyName		the name of the needed key
	 * @return				the key
	 * @throws IOException	if there is a problem with the provided file
	 */
	public static String getSecret(String filePath, String keyName) throws IOException
	{		
		Properties properties = new Properties();
		
		try (FileInputStream fis = new FileInputStream(filePath))
		{
            properties.load(fis);
            return properties.getProperty(keyName);
        }
		catch (IOException ex)
		{
            throw new IOException("There was a problem finding or loading file: " + filePath);
        }
	}
	
	
	/**
	 * Gets the current balances in BNB of the provided list of 
	 * wallets. This speeds up program execution by making better 
	 * use of the throttled API calls, getting up to 20
	 * balances per call.
	 * 
	 * @param walletList
	 * @throws Exception 
	 */
	public static HashMap<String, Double> getBalances(ArrayList<String> addresses) throws Exception
	{
		String enumAction = WalletActions.BALANCEMULTI.name().toLowerCase();
		
		HashMap<String, Double> balances = new HashMap<>(); // wallet addresses and balances
		StringBuilder walletsSB = new StringBuilder();  	// comma separated list of wallets (format API requires)
		
		
		int numCalls = (addresses.size() / 20);
		if (addresses.size() % 20 > 0) { numCalls++; }
		
		
		
		for (String address : addresses)
		{
			if (addresses.indexOf(address) > 0)
			{
                walletsSB.append(",");
            }
			
			walletsSB.append(address);
		}
		
		//System.out.println(walletsSB.toString());

		String urlString = "https://api.bscscan.com/api?module=account" +
			   "&action=" + enumAction + 
			   "&address=" + walletsSB.toString() +
			   "&tag=" + "latest" + // latest, earliest, pending
			   "&apikey=" + API_KEY;
		
		String jsonData = ""; // json to be returned from call

		try
		{
			URL url = new URL(urlString);
			
			int keyIndex = BlockchainApp.callLimiter.requestToken(); // get permission to make API call
			try (InputStream stream = url.openStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
					// BufferedWriter writer = new BufferedWriter(new FileWriter("info.txt")); // write data to a file?
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
		
		// unparsed json
		//System.out.println(jsonData);
		//{"status":"1","message":"OK","result":[{"account":"0xf3d7d404d3b8a5ab5a45d7573e44a6cff37d3c89","balance":"6999188248887296596"},{"account":"0xacd208b1fe8d117169d8c3eba5aba8c3effece84","balance":"2517136666739920017"},{"account":"0xaaa649a830af14f38c135e15bb5b08a5e7f2b4ec","balance":"39773881844604313239"}]}
		
		// call error
		if (jsonData.equals("{\"status\":\"0\",\"message\":\"NOTOK\",\"result\":\"Max rate limit reached\"}"))
		{
			throw new Exception("Max rate limit reached.");
		}
		
		
		// parsing what is returned
		jsonData = jsonData.substring(1,jsonData.length()-1); // strip outer curly brace
		
		int divider = jsonData.indexOf("result"); 			  // where query status ends and requested data starts
		
		// do stuff with query status here
		
		String result = jsonData.substring(divider + 8); 	  // result of query request (balance data)
		result = result.substring(1,result.length()-1); 	  // strip outer square bracket
		//System.out.println(result);
		
		String[] resultArray = result.split(",");
		
		for (int i = 0; i < resultArray.length; i += 2)
		{
			int accountIndexStart = resultArray[i].indexOf("account") + 10;
			int accountIndexEnd = resultArray[i].length()-1;
			int balanceIndexStart = resultArray[i+1].indexOf("balance") + 10;
			int balanceIndexEnd = resultArray[i+1].length()-2;
			
			String address = resultArray[i].substring(accountIndexStart, accountIndexEnd);
			String weiBalance = resultArray[i+1].substring(balanceIndexStart, balanceIndexEnd);
			
			double balance = convertWeiToBNB(weiBalance);
			
			balances.put(address, balance);
		}
		
		return balances;
	} // end of getBalances()
	
	
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
	

}
