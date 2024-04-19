/*
 * Author: 		 T. Stratton
 * Date started: 15 NOV 2023
 * Last updated: 19 APR 2024
 * 
 * File Contents:
 *  
 * 
 * Notes:
 * 	Add a secrets file and remove API key.
 * 
 */

package cryptotransactionvisualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


/*
 * This class uses the BscScan API to pull wallet and transaction
 * information from the Binance blockchain.
 * 
 */
public class BlockchainApp
{
	final static String API_KEY = "K1541JHJGC6UET8F1AYNB8VW1FPI1K9688";
	static CallLimiter callLimiter;
	
	public static void main(String[] args)
	{
		
		final int RATE_LIMIT = 4; // can be 5
		callLimiter = new CallLimiter(RATE_LIMIT);
		Thread limiterThread = new Thread(callLimiter);
		limiterThread.start();
		
		
		String searchedAddress;						 				   // wallet address of searched wallet
		Wallet searchedWallet;										   // searched wallet
		HashMap<String, Wallet> firstDegreeWallets = new HashMap<>();  // wallets with which the searched wallet has transacted
		HashMap<String, Double> walletBalances = new HashMap<>();      // balance of all wallets

		
		
		String walletAddress = "0xf3d7d404D3B8A5ab5a45D7573e44a6CFf37D3c89";
		String walletAddress2 = "0xaCD208B1fe8D117169d8C3ebA5aBa8C3effece84";
		String walletAddress3 = "0xaAA649A830AF14F38c135e15bB5B08a5e7F2B4eC";

		searchedAddress = walletAddress;
		searchedWallet = new Wallet(searchedAddress);
		
		// list of transacted wallets
		ArrayList<Transaction> searchedWalletTransactions = searchedWallet.transactions;
		
		// create 1st degree wallets
		for (String address : searchedWallet.senders)
		{
			firstDegreeWallets.put(address, new Wallet(address));
		}
		for (String address : searchedWallet.receivers)
		{
			firstDegreeWallets.put(address, new Wallet(address));
		}
		
		System.out.println("senders: " + searchedWallet.senders.size());
		System.out.println("First deg wallets: " + firstDegreeWallets.size());
		
		
		// balance of all wallets
		for (HashMap.Entry<String, Wallet> kvp : firstDegreeWallets.entrySet())
		{
			walletBalances.put(kvp.getKey(), kvp.getValue().balance);
		}

		// number of transactions between each wallet
		// net value of all transactions between wallets
		
		
		
//		for (Transaction t : searchedWalletTransactions)
//		{
//			System.out.println("HASH: " + t.getHash());
//			System.out.printf("VALUE: %.5f BNB\n", t.getValue());
//			System.out.println("FROM: " + t.getSender());
//			System.out.println("TO: " + t.getReceiver());
//		}
//		
		System.out.print("Current wallet balances: ");
		
		for (HashMap.Entry<String, Double> kvp : walletBalances.entrySet())
		{
			System.out.printf("%.5f BNB\n", kvp.getValue());
		}
			
		
		
	
		
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
		



	} // end of main()
	
	
	
	
	

}
