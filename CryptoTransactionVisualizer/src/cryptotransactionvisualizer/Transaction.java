package cryptotransactionvisualizer;

import java.util.Date;

public class Transaction {
	private int blockNumber;
	private Date timeStamp;
	private String hash;
	private int nonce;
	private String blockHash;
	private int transactionIndex;
	private String fromWallet;
	private String toWallet;
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
	private String functionName; // string correct datatype?
	
	
	public Transaction(int blockNumber, Date timeStamp, String hash, String fromWallet, String toWallet, double value, int isError, int txRecieved) {
		
		this.blockNumber = blockNumber;
		this.timeStamp = timeStamp;
		this.hash = hash;
		this.fromWallet = fromWallet;
		this.toWallet = toWallet;
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
			int transactionIndex, String fromWallet, String toWallet, double value, int gas, int gasPrice,
			int isError, int txRecieved, String input, String contractAddress, int cumulativeGas, 
			int gasUsed, int confirmations, String methodId, String functionName) {
		
		this.blockNumber = blockNumber;
		this.timeStamp = timeStamp;
		this.hash = hash;
		this.nonce = nonce;
		this.blockHash = blockHash;
		this.transactionIndex = transactionIndex;
		this.fromWallet = fromWallet;
		this.toWallet = toWallet;
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
			int transactionIndex, String fromWallet, String toWallet, double value, int gas, int gasPrice,
			String input, String methodId, String functionName, String contractAddress, 
			int cumulativeGas, int txRecieved, int gasUsed, int confirmations, int isError) {
		
		this.blockNumber = blockNumber;
		this.timeStamp = timeStamp;
		this.hash = hash;
		this.nonce = nonce;
		this.blockHash = blockHash;
		this.transactionIndex = transactionIndex;
		this.fromWallet = fromWallet;
		this.toWallet = toWallet;
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

	public int getBlockNumber() {
		return blockNumber;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public String getHash() {
		return hash;
	}

	public int getNonce() {
		return nonce;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public int getTransactionIndex() {
		return transactionIndex;
	}

	public String getFromWallet() {
		return fromWallet;
	}

	public String getToWallet() {
		return toWallet;
	}

	public double getValue() {
		return value;
	}

	public int getGas() {
		return gas;
	}

	public int getGasPrice() {
		return gasPrice;
	}

	public boolean isError() {
		return isError;
	}

	public boolean isTxRecieved() {
		return txRecieved;
	}

	public String getInput() {
		return input;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public int getCumulativeGas() {
		return cumulativeGas;
	}

	public int getGasUsed() {
		return gasUsed;
	}

	public int getConfirmations() {
		return confirmations;
	}

	public String getMethodId() {
		return methodId;
	}

	public String getFunctionName() {
		return functionName;
	}

	@Override
	public String toString() {
		return String.format(" %d  ", blockNumber) + 
				String.format("  %1$tm-%1$td-%1$ty %1$tH:%1$tM ", timeStamp) + 
				String.format("   %s     %s     %s    %.8f BNB", hash, fromWallet, toWallet, value);
		//return String.format(" %d     %d    %s     %s     %s    %.8f BNB", blockNumber, timeStamp, hash, fromWallet, toWallet, value);
	}
	
	
	
	
}
