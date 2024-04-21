/*
 * Author: 		 T. Stratton
 * Date started: 15 NOV 2023
 * Last updated: 19 APR 2024
 * 
 * File Contents:
 *  
 * 
 * Notes:
 * 
 * 
 */

package blockchainvisualizer;

import java.util.Date;


/**
 * This class represents a cryptocurrency transaction by
 * holding all associated information.
 */
public class Transaction
{
	
	private int blockNumber;
	private Date timeStamp;
	private String hash;
	private int nonce;
	private String blockHash;
	private int transactionIndex;
	private String sender; // from
	private String receiver; // to
	private double value;
	private int gas;
	private int gasPrice;
	private boolean isError;
	private boolean txRecieved;
	private String input;
	private String contractAddress;
	private int cumulativeGas;
	private int gasUsed;
	private int confirmations;
	private String methodId;
	private String functionName;
	
	
	public Transaction(int blockNumber, Date timeStamp, String hash, String sendingWallet, String receivingWallet, double value, int isError, int txRecieved)
	{
		
		this.blockNumber = blockNumber;
		this.timeStamp = timeStamp;
		this.hash = hash;
		this.sender = sendingWallet;
		this.receiver = receivingWallet;
		this.value = value;
		
		if (isError == 1)
			this.isError = true;
		else
			this.isError = false;
		
		if (txRecieved == 1)
			this.txRecieved = true;
		else
			this.isError = false;
	} // end of constructor()
	
	public Transaction(int blockNumber, Date timeStamp, String hash, int nonce, String blockHash,
			int transactionIndex, String sendingWallet, String receivingWallet, double value, int gas, int gasPrice,
			int isError, int txRecieved, String input, String contractAddress, int cumulativeGas, 
			int gasUsed, int confirmations, String methodId, String functionName)
	{
		
		this.blockNumber = blockNumber;
		this.timeStamp = timeStamp;
		this.hash = hash;
		this.nonce = nonce;
		this.blockHash = blockHash;
		this.transactionIndex = transactionIndex;
		this.sender = sendingWallet;
		this.receiver = receivingWallet;
		this.value = value;
		this.gas = gas;
		this.gasPrice = gasPrice;
		
		if (isError == 1)
			this.isError = true;
		else
			this.isError = false;
		
		if (txRecieved == 1)
			this.txRecieved = true;
		else
			this.isError = false;
		
		this.input = input;
		this.contractAddress = contractAddress;
		this.cumulativeGas = cumulativeGas;
		this.gasUsed = gasUsed;
		this.confirmations = confirmations;
		this.methodId = methodId;
		this.functionName = functionName;
	} // end of constructor()
	
	public Transaction(int blockNumber, String blockHash, Date timeStamp, String hash, int nonce,
			int transactionIndex, String sendingWallet, String receivingWallet, double value, int gas, int gasPrice,
			String input, String methodId, String functionName, String contractAddress, 
			int cumulativeGas, int txRecieved, int gasUsed, int confirmations, int isError)
	{
		
		this.blockNumber = blockNumber;
		this.timeStamp = timeStamp;
		this.hash = hash;
		this.nonce = nonce;
		this.blockHash = blockHash;
		this.transactionIndex = transactionIndex;
		this.sender = sendingWallet;
		this.receiver = receivingWallet;
		this.value = value;
		this.gas = gas;
		this.gasPrice = gasPrice;
		
		if (isError == 1)
			this.isError = true;
		else
			this.isError = false;
		
		if (txRecieved == 1)
			this.txRecieved = true;
		else
			this.isError = false;
		
		this.input = input;
		this.contractAddress = contractAddress;
		this.cumulativeGas = cumulativeGas;
		this.gasUsed = gasUsed;
		this.confirmations = confirmations;
		this.methodId = methodId;
		this.functionName = functionName;
	} // end of constructor()

	public int getBlockNumber() { return blockNumber; }

	public Date getTimeStamp() { return timeStamp; }

	public String getHash() { return hash; }

	public int getNonce() { return nonce; }

	public String getBlockHash() { return blockHash; }

	public int getTransactionIndex() { return transactionIndex; }

	public String getSender() { return sender; }

	public String getReceiver() { return receiver; }

	public double getValue() { return value; }

	public int getGas() { return gas; }

	public int getGasPrice() { return gasPrice; }

	public boolean isError() { return isError; }

	public boolean isTxRecieved() { return txRecieved; }

	public String getInput() { return input; }

	public String getContractAddress() { return contractAddress; }

	public int getCumulativeGas() { return cumulativeGas; }

	public int getGasUsed() { return gasUsed; }

	public int getConfirmations() { return confirmations; }

	public String getMethodId() { return methodId; }

	public String getFunctionName() { return functionName; }

	@Override
	public String toString() {
		return String.format(" %d  ", blockNumber) + 
				String.format("  %1$tm-%1$td-%1$ty %1$tH:%1$tM ", timeStamp) + 
				String.format("   %s     %s     %s    %.8f BNB", hash, sender, receiver, value);
		//return String.format(" %d     %d    %s     %s     %s    %.8f BNB", blockNumber, timeStamp, hash, fromWallet, toWallet, value);
	}
	
	
	//order of the query results changes?
		//transactionData2[0] = transactionData[0].substring(15,transactionData[0].length()-1);    //blocknumber
		//transactionData2[1] = transactionData[1].substring(13,transactionData[1].length()-1);    //blockhash
		//transactionData2[2] = transactionData[2].substring(13,transactionData[2].length()-1);    //timestamp
		//transactionData2[3] = transactionData[3].substring(8,transactionData[3].length()-1);     //hash
		//transactionData2[4] = transactionData[4].substring(9,transactionData[4].length()-1);     //nonce
		//transactionData2[5] = transactionData[5].substring(20,transactionData[5].length()-1);    //transactionindex
		//transactionData2[6] = transactionData[6].substring(8,transactionData[6].length()-1);     //from
		//transactionData2[7] = transactionData[7].substring(6,transactionData[7].length()-1);     //to
		//transactionData2[8] = transactionData[8].substring(9,transactionData[8].length()-1); 	   //value
		//transactionData2[9] = transactionData[9].substring(7,transactionData[9].length()-1); 	   //gas
		//transactionData2[10] = transactionData[10].substring(12,transactionData[10].length()-1); //gasprice
		//transactionData2[11] = transactionData[11].substring(9,transactionData[11].length()-1);  //input
		//transactionData2[12] = transactionData[12].substring(12,transactionData[12].length()-1); //methodID
		//transactionData2[13] = transactionData[13].substring(10,transactionData[13].length()-1); //functionName
		//transactionData2[14] = transactionData[14].substring(18,transactionData[14].length()-1); //contractAdd
		//transactionData2[15] = transactionData[15].substring(21,transactionData[15].length()-1); //cumGas
		//transactionData2[16] = transactionData[16].substring(20,transactionData[16].length()-1); //txReceipt
		//transactionData2[17] = transactionData[17].substring(11,transactionData[17].length()-1); //gasUsed
		//transactionData2[18] = transactionData[18].substring(17,transactionData[18].length()-1); //confirmations
		//transactionData2[19] = transactionData[19].substring(11,transactionData[19].length()-1); //isError
	
	
}
