/*
 * Author: 		 T. Stratton
 * Date started: 18 APR 2023
 * Last updated: 18 APR 2024
 * 
 * File Contents:
 *  
 * 
 * Notes:
 * 	Maybe change rateLimit to a double so it can be just below the call threshold.
 * 
 */

package cryptotransactionvisualizer;

public class CallLimiter implements Runnable
{
	
	final int rateLimit;      // maximum allowed calls per second
	private int tokenBucket;  // number of available calls
	
	
	/**
	 * This class is a governor for API calls. Number of calls
	 * available to be made is represented as "tokens" in a "bucket".
	 * If no tokens are available for use, the calling thread requesting 
	 * a token is put to sleep for half the time it takes to generate 
	 * another token before checking if one is available.
	 * 
	 * @param rate	the number of permitted calls per second
	 */
	public CallLimiter(int rateLimit)
	{
		this.rateLimit = rateLimit;
		this.tokenBucket = rateLimit;
	}
	
	
	/**
	 * A second thread runs this method to keep track of how many
	 * tokens are available for use.
	 * If the token bucket is full, the thread sleeps for half the time
	 * it takes to generate a token before checking to see if one has
	 * been used.
	 * 
	 */
	@Override
	public void run()
	{
		while (true)
		{
			if (tokenBucket < rateLimit)
			{
				try { Thread.sleep(1000 / rateLimit); } 
				catch (InterruptedException e) { e.printStackTrace(); }
				
				synchronized (this) { this.tokenBucket++; }
			}
			else
			{
				try { Thread.sleep(500 / rateLimit); }
				catch (InterruptedException e)  { e.printStackTrace(); }
			}
		}	
	}
	
	
	/**
	 * If token is not immediately available, calling (first) thread is put to 
	 * sleep for half the time it takes to generate another token before 
	 * checking to see if there is one available for it to use.
	 * 
	 */
	public void requestToken()
	{
		while (!(tokenBucket > 0))
		{
			try { Thread.sleep(500 / rateLimit); } 
			catch (InterruptedException e) { e.printStackTrace(); }
		}

		synchronized (this) { this.tokenBucket--; }
		return;
	}

	
}