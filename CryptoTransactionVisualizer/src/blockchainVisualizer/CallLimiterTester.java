package blockchainVisualizer;

public class CallLimiterTester {

	public static void main(String[] args) {

		final int RATE_LIMIT = 5;
		CallLimiter callLimiter = new CallLimiter(RATE_LIMIT);
		Thread limiterThread = new Thread(callLimiter);
		limiterThread.start();
		
		
		for (int i = 0; i < 100; i++)
		{
			System.out.println("token: " + callLimiter.requestToken() + " result: " + i);
		}

	}

}
