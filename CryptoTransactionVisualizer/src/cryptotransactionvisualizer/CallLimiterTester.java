package cryptotransactionvisualizer;

public class CallLimiterTester {

	public static void main(String[] args) {

		final int RATE_LIMIT = 5;
		CallLimiter callLimiter = new CallLimiter(RATE_LIMIT);
		Thread limiterThread = new Thread(callLimiter);
		limiterThread.start();
		
		
		for (int i = 0; i < 100; i++)
		{
			callLimiter.requestToken();
			System.out.println(i);
		}

	}

}
