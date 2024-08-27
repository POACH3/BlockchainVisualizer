/*
 * Author: T. Stratton
 * Date adapted: 26 AUG 2024
 * Last updated: 27 AUG 2024
 * 
 * File Contents:
 *  
 * 
 * Notes:
 * 
 * 
 */

package blockchainVisualizer;

import java.awt.Color;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;

/**
 * 
 */
public class GUI {

	private static int width = 1400;  // window width
	private static int height = 1000; // window height
	
	private static Color maxGreen = new Color(0, 230, 0, 155);
	private static Color minGreen = new Color(0, 100, 0, 155);
	private static Color maxRed = new Color(230, 0, 0, 155);
	private static Color minRed = new Color(100, 0, 0, 155);
	private static Color walletNormal = new Color(100, 115, 165, 155);
	private static Color walletHover = new Color(150, 165, 200, 155);
	
	
	
	public static void drawWallet(double xCenter, double yCenter, boolean mouse)
	{
		if (mouse == true)
		{
			StdDraw.setPenColor(walletHover);
			StdDraw.filledCircle(width/2, height/2, 52);
			
			//StdDraw.setPenColor(StdDraw.DARK_GRAY);
			//StdDraw.text(width/2, height/2 - 100, "wallet address");
		}
		else
		{		
			StdDraw.setPenColor(walletNormal);
			StdDraw.filledCircle(xCenter, yCenter, 50);
		}
	}
	
	public static void drawSearchWallet(boolean mouse)
	{
		drawWallet(width/2, height/2, mouse);
	}
	
	
	public static void drawFirstDegree(ArrayList<Integer> wallets)
	//public static void drawFirstDegree(ArrayList<Wallet> wallets)
	{
		double xPivot = width/2;  // x-coord around which the wallets are arranged
		double yPivot = height/2; // y-coord around which the wallets are arranged

		ArrayList<double[]> walletLocations = new ArrayList<double[]>();
		
		for (int i = 0; i < 360; i = i + (360 / wallets.size()))
		{
			double distance; // related to number of total 1st deg wallets and this wallet's size
			double angle; 	 // related to number of total 1st deg wallets and each wallet size
			
			distance = 300;
			angle = i;
			
			double xCenter = xPivot + polarToCartesian(distance, angle)[0];
			double yCenter = yPivot + polarToCartesian(distance, angle)[1];
			
			double[] array = {xCenter, yCenter};
			walletLocations.add(array);
			
			// draw wallets
			StdDraw.setPenColor(walletNormal);
			StdDraw.filledCircle(xCenter, yCenter, 40);
			
			// draw transactions
			drawTransaction(xPivot, yPivot, xCenter, yCenter);
		}
		
		

		
	}
	
	//public static void drawFirstDegWallets(ArrayList<Wallet> wallets)
//	public static void drawFirstDegWallets(ArrayList<Integer> wallets)
//	{
//		double xPivot = width/2;  // x-coord around which the wallets are arranged
//		double yPivot = height/2; // y-coord around which the wallets are arranged
//		
//		for (int i = 0; i < 360; i = i + (360 / wallets.size()))
//		{
//			double distance; // related to number of total 1st deg wallets and this wallet's size
//			double angle; 	 // related to number of total 1st deg wallets and each wallet size
//			
//			distance = 300;
//			angle = i;
//			
//			double xCenter = xPivot + polarToCartesian(distance, angle)[0];
//			double yCenter = yPivot + polarToCartesian(distance, angle)[1];
//			
//			StdDraw.setPenColor(walletNormal);
//			StdDraw.filledCircle(xCenter, yCenter, 40);
//		}
//	}
//	
//	public static void drawFirstDegTransactions(double xPivot, double yPivot, ArrayList<double[]> walletLocations)
//	{
//		for (double[] walletLocation : walletLocations)
//		{
//			drawTransaction(xPivot, yPivot, walletLocation[0], walletLocation[1]);
//		}
//	}
	
	public static void drawTransaction(double fromWalletX, double fromWalletY, double toWalletX, double toWalletY)
	{
		double fromWalletR = 50;
		double toWalletR = 40;
		
		double theta = angle(fromWalletX, fromWalletY, toWalletX, toWalletY);
		
		double tailX = fromWalletX + polarToCartesian(fromWalletR, theta)[0];
		double tailY = fromWalletY + polarToCartesian(fromWalletR, theta)[1];
		double headX = toWalletX - polarToCartesian(toWalletR, theta)[0];
		double headY = toWalletY - polarToCartesian(toWalletR, theta)[1];
		
		drawArrowCartesian(tailX, tailY, headX, headY);
	}
	
	public static void drawArrowPolar(double tailX, double tailY, double distance, double angle)
	{
		double arrowheadLength = 16;
		double arrowheadWidth = 12;
		
		// draw arrow line
		double lineHeadX = tailX + polarToCartesian(distance - arrowheadLength, angle)[0]; // x-coord head of line
		double lineHeadY = tailY + polarToCartesian(distance - arrowheadLength, angle)[1]; // y-coord head of line
		
		StdDraw.setPenColor(maxGreen);
		StdDraw.line(tailX, tailY, lineHeadX, lineHeadY);
		
		// draw arrow head
		double[] arrowheadX = new double[3];
		double[] arrowheadY = new double[3];
		
		arrowheadX[0] = lineHeadX + polarToCartesian(arrowheadLength, angle)[0]; // x-coord arrowhead tip
		arrowheadY[0] = lineHeadY + polarToCartesian(arrowheadLength, angle)[1]; // y-coord arrowhead tip
		
		arrowheadX[1] = lineHeadX + polarToCartesian(arrowheadWidth/2, angle - 90)[0];
		arrowheadY[1] = lineHeadY + polarToCartesian(arrowheadWidth/2, angle - 90)[1];
		
		arrowheadX[2] = lineHeadX + polarToCartesian(arrowheadWidth/2, angle + 90)[0];
		arrowheadY[2] = lineHeadY + polarToCartesian(arrowheadWidth/2, angle + 90)[1];
		
		//add fourth point to sweep arrowhead
		
		StdDraw.setPenColor(maxRed);
		StdDraw.filledPolygon(arrowheadX, arrowheadY);
	}
	
	public static void drawArrowCartesian(double tailX, double tailY, double headX, double headY)
	{
		double distance = Math.sqrt(Math.pow(headX - tailX, 2) + Math.pow(headY - tailY, 2));
		double angle = angle(tailX, tailY, headX, headY);
		
		drawArrowPolar(tailX, tailY, distance, angle);
	}
	
	/**
	 * Converts polar (r, theta) coordiate pair to (x, y) coordinate pair.
	 * @param r
	 * @param theta
	 * @return
	 */
	public static double[] polarToCartesian(double r, double theta)
	{
		double[] cartesianCoord = new double[2];
		
		cartesianCoord[0] = r * Math.cos(Math.toRadians(theta));
		cartesianCoord[1] = r * Math.sin(Math.toRadians(theta));
		
		return cartesianCoord;
	}
	
	/**
	 * Converts (x, y) coordinate pair to (r, theta) coordinate pair. Theta is in degrees.
	 * @param x
	 * @param y
	 * @return
	 */
	public static double[] cartesianToPolar(double x, double y)
	{
		double[] polarCoord = new double[2];
		
		polarCoord[0] = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		polarCoord[1] = Math.toDegrees(Math.atan2(y - 0, x - 0));
		
		return polarCoord;
	}
	
	/**
	 * Returns the direction (angle in degrees from the 
	 * positive x-axis) from the start point to the end point.
	 * 
	 * @param xStart
	 * @param yStart
	 * @param xEnd
	 * @param yEnd
	 * @return
	 */
	public static double angle(double xStart, double yStart, double xEnd, double yEnd)
	{
		double theta = Math.toDegrees(Math.atan2(yEnd - yStart, xEnd - xStart));
		
		if (theta < 0) { theta = 360 + theta; }
		
		return theta;
	}
	
	public static boolean mouseOver()
	{
		double mouseX = StdDraw.mouseX();
		double mouseY = StdDraw.mouseY();
		
		if (mouseX < width/2 && mouseY > height/2) {
			return true;
		}
		
		return false;
	}
	
	
	public static void main(String[] args)
	{	
		// setup
		StdDraw.setTitle("Blockchain Visualizer");
		
		StdDraw.setCanvasSize(width, height);
		StdDraw.clear(StdDraw.BLACK);
		StdDraw.setXscale(0,width);
		StdDraw.setYscale(0,height);
		
		
		
		//TEST
		ArrayList<Integer> wallets = new ArrayList<>(); //stand in for ArrayList<Wallet> wallets
		double[] array = new double[4];
		
		wallets.add(1); // value, transaction1, transaction2, transaction3
		wallets.add(2);
		wallets.add(3);
		wallets.add(4);
		wallets.add(5);
		wallets.add(6);
		wallets.add(20);
		wallets.add(8);
		wallets.add(9);
		wallets.add(10);
		wallets.add(11);
		
		
		// draw loop
		while (true) 
		{
			StdDraw.enableDoubleBuffering();
			StdDraw.clear(StdDraw.BLACK);
			
			// search interface
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.rectangle(229, height - 20, 225, 16);
			StdDraw.textLeft(15, height - 22, "ENTER A WALLET ADDRESS");
			
			StdDraw.rectangle(500, height - 20, 40, 16);
			StdDraw.text(500, height - 22, "SEARCH");

			
			// mouse over information
			// 		wallet circle:     address, net value
			//  	transaction arrow: total transactions, net value, value sent, value received, volume?
			String address = "0xf3d7d404D3B8A5ab5a45D7573e44a6CFf37D3c89";
			double value = 5.5;
			StdDraw.setPenColor(StdDraw.DARK_GRAY);
			StdDraw.textLeft(width - 600, height - 15, "WALLET ADDRESS: " + address);
			StdDraw.textLeft(width - 600, height - 35, "WALLET VALUE: " + value + " BNB");

			
			// check for mouse over
			
			
			// draw circles and arrows
			drawSearchWallet(mouseOver());
			drawFirstDegree(wallets);
			
//			drawFirstDegWallets(wallets);
//			drawFirstDegTransactions(width/2, height/2, walletLocations);
			//drawSecondDegWallets();
//			for (int i = 0; i < 360; i = i + 360/wallets.size())
//			{
//				drawTransaction(width/2, height/2, 1400, 1000);
//			}
			
			//drawTransaction(width/2, height/2, width/2 + 300, height/2);
			//drawArrowPolar(width/2, height/2, 1400, 1000);
			
			

			
			//drawTransaction(0);


			
			
			StdDraw.show();
			StdDraw.pause(100);
		}
		
		
		
		
//		double[] posX = {1, Math.sqrt(2)/2, 0, -Math.sqrt(2)/2, -1, -Math.sqrt(2)/2, 0, Math.sqrt(2)/2, 1};
//		double[] posY = {0, Math.sqrt(2)/2, 1, Math.sqrt(2)/2, 0, -Math.sqrt(2)/2, -1, -Math.sqrt(2)/2, 0};
//		
//		for (int i = 0; i < posX.length; i++) 
//		{
//			System.out.println(angle(0, 0, posX[i], posY[i]));
//		}
		

		
	}

}
