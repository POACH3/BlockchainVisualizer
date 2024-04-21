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
 * 	Add second constructor to allow use for multiple keys.
 * 
 */

package blockchainvisualizer;


/**
 * This class is a governor for API calls. Number of calls
 * available to be made is represented as "tokens" in a "bucket".
 * If no tokens are available for use, the calling thread requesting 
 * a token is put to sleep for half the time it takes to generate 
 * another token before checking if one is available.
 */
public class CallLimiter implements Runnable
{
	
	private int[] rateLimits;   // maximum allowed calls per second
	private int slowestKey;		// index of key with slowest rate limit
	private int fastestKey; 	// index of key with fastest rate limit
	private int[] tokenBuckets; // number of available calls

	
	
	/**
	 * One API key to use.
	 * 
	 * @param rateLimit		the number of permitted calls per second
	 */
	public CallLimiter(int rateLimit)
	{
		this.rateLimits = new int[1];
		this.rateLimits[0] = rateLimit;

		this.tokenBuckets = new int[1];
		this.tokenBuckets[0] = rateLimit;
		
		this.slowestKey = 0;
		this.fastestKey = 0;
	}
	
	/**
	 * Multiple API keys to use.
	 * 
	 * @param numKeys
	 * @param rateLimits
	 */
	public CallLimiter(int numKeys, int[] rateLimits)
	{
		this.rateLimits = new int[numKeys];
		this.rateLimits = rateLimits;
		
		this.tokenBuckets = new int[numKeys];
		for (int i = 0; i < tokenBuckets.length; i++)
		{
			tokenBuckets[i] = rateLimits[i];
		}
		
		this.slowestKey = 0;
		this.fastestKey = 0;
		for (int rateLimit : rateLimits)
		{
			if (rateLimit > fastestKey) { this.fastestKey = rateLimit; }
			if (rateLimit < slowestKey) { this.slowestKey = rateLimit; }
		}
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
		int i = 0;
		boolean[] incrementTokenBucket = new boolean[tokenBuckets.length];
		
		while (true)
		{
			if (i < tokenBuckets.length)
			{
				if (tokenBuckets[i] < rateLimits[i])
				{
					try { Thread.sleep(1000 / rateLimits[i]); } 
					catch (InterruptedException e) { e.printStackTrace(); }
					
					synchronized (this) { this.tokenBuckets[i]++; }
				}
				else
				{
					try { Thread.sleep(500 / rateLimits[slowestKey]); }
					catch (InterruptedException e)  { e.printStackTrace(); }
				}
			}
			
//			if (i < tokenBuckets.length)
//			{
//				if (tokenBuckets[i] < rateLimits[i])
//				{
//					incrementTokenBucket[i] = true;
//				}
//				
//				i++;
//			}
//			else
//			{
//				for (int j = 0; j < incrementTokenBucket.length; j++)
//				{
//					if (incrementTokenBucket[j] == true)
//					{
//						synchronized (this) { this.tokenBuckets[j]++; }
//					}
//					continue;
//				}
//				
//				try { Thread.sleep(500 / rateLimits[slowestKey]); } 
//				catch (InterruptedException e) { e.printStackTrace(); }
//				
//				i = 0;
//			}
			
		}	
	}
	
	
	/**
	 * If token is not immediately available, calling (first) thread is put to 
	 * sleep for half the time it takes to generate another token before 
	 * checking to see if there is one available for it to use.
	 * 
	 */
	public int requestToken()
	{
		int i = 0;
		
		while (!(tokenBuckets[i] > 0))
		{				
			if (i < tokenBuckets.length - 1)
			{
				i++;
			}
			else
			{
				i = 0;
				try { Thread.sleep(500 / rateLimits[fastestKey]); } 
				catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
		
		synchronized (this) { this.tokenBuckets[i]--; }
		
		return i;
	}

	
}