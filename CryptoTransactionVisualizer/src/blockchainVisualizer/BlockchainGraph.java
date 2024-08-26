/*
 * Authors: T. Stratton, Robert Sedgewick, Kevin Wayne
 * Date adapted: 04 JUL 2024
 * Last updated: 25 AUG 2024
 * 
 * File Contents:
 *  
 * 
 * Notes:
 * 
 * 
 */

package blockchainVisualizer;

import java.util.ArrayList;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 *  The {@code BlockchainGraph} class represents a directed graph, where both the
 *  vertex names and edge names are arbitrary strings.
 *  It is based on the class {@code SymbolDigraph} from algs4.
 *  <p>
 *  By providing mappings between string vertex names and integers,
 *  it serves as a wrapper around the
 *  {@link Digraph} data type, which assumes the vertex names are integers
 *  between 0 and <em>V</em> - 1.
 *  It also supports initializing a symbol digraph from a file.
 *  <p>
 *  This implementation uses an {@link ST} to map from strings to integers,
 *  an array to map from integers to strings, and a {@link Digraph} to store
 *  the underlying graph.
 *  The <em>indexOf</em> and <em>contains</em> operations take time
 *  proportional to log <em>V</em>, where <em>V</em> is the number of vertices.
 *  The <em>nameOf</em> operation takes constant time.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *	@author T. Stratton
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class BlockchainGraph
{
    private ST<String, Integer> nodeST; // string -> index
    private ST<String, Integer> edgeST; // string -> index
    private String[] addresses;         // node index -> node string
    private String[] hashes;			// edge index -> edge string
    public EdgeWeightedDigraph graph;  // the underlying digraph
    
    /**
     * Initializes a digraph from a list of type Transaction, using the
     * wallet addresses and the unique hash associated with the transaction.
     * 
     * @param transactions
     */
    public BlockchainGraph(ArrayList<Transaction> transactions) {
        nodeST = new ST<String, Integer>();
        edgeST = new ST<String, Integer>();
        
        // go through list of transactions and get the sender, receiver, and transaction hash
        
        ArrayList<String[]> transactionInfo = new ArrayList<String[]>();
        for (Transaction transaction : transactions)
        {
        	transactionInfo.add(new String[] {transaction.getSender(), transaction.getReceiver(), transaction.getHash()});
        }
        
        // wrap sender and receiver in pair - new class, string[]
        
        // create a symbol table for transaction hash and edge number pairs
        for (String[] txInfo : transactionInfo)
        {
        	if (!edgeST.contains(txInfo[2])) // verify that each Transaction is unique
        	{
        		edgeST.put(txInfo[2], edgeST.size());
        	}
        }
        
        hashes = new String[edgeST.size()];
        for (String edgeName : edgeST.keys()) {
            hashes[edgeST.get(edgeName)] = edgeName;
        }
        
        // convert ArrayList addresses to ST
        for (String[] txInfo : transactionInfo)
		{
        	for (int i = 0; i < 2; i++) {
        		if (!nodeST.contains(txInfo[i]))
                    nodeST.put(txInfo[i], nodeST.size());
        	}
		}
        
        // inverted index to get string addresses in an array
        addresses = new String[nodeST.size()];
        for (String nodeName : nodeST.keys()) {
            addresses[nodeST.get(nodeName)] = nodeName;
        }

        // build the weighted digraph
        graph = new EdgeWeightedDigraph(nodeST.size());
        for (String[] txInfo : transactionInfo)
        {
            int v = nodeST.get(txInfo[0]);
            int w = nodeST.get(txInfo[1]);
            double weight = edgeST.get(txInfo[2]);
            graph.addEdge(new DirectedEdge(v, w, weight));
        }
    }
    
    /**
     * Initializes a digraph from a file using the specified delimiter.
     * Each line in the file contains the name of a vertex, followed 
     * by the name of a vertex adjacent to the first, followed by the 
     * name of that edge, all separated by the delimiter.
     * 
     * @param filename the name of the file
     * @param delimiter the delimiter between fields
     */
    public BlockchainGraph(String filename, String delimiter)
    {
    	nodeST = new ST<String, Integer>();
        edgeST = new ST<String, Integer>();

        // first pass builds the index by reading strings to associate
        // distinct strings with an index
        In in = new In(filename);
        while (in.hasNextLine())
        {
            String[] a = in.readLine().split(delimiter);
            for (int i = 0; i < 2; i++)
            {
                if (!nodeST.contains(a[i]))
                    nodeST.put(a[i], nodeST.size());
            }
            
            if (!edgeST.contains(a[2]))
                edgeST.put(a[2], edgeST.size());
        }
        
        // inverted index to get string keys in an array
        hashes = new String[edgeST.size()];
        for (String edgeName : edgeST.keys()) {
            hashes[edgeST.get(edgeName)] = edgeName;
        }

        // inverted index to get string keys in an array
        addresses = new String[nodeST.size()];
        for (String name : nodeST.keys())
        {
            addresses[nodeST.get(name)] = name;
        }

        // second pass builds the weighted digraph
        graph = new EdgeWeightedDigraph(nodeST.size());
        in = new In(filename);
        while (in.hasNextLine())
        {
            String[] a = in.readLine().split(delimiter);
            int v = nodeST.get(a[0]);
            int w = nodeST.get(a[1]);
            double weight = edgeST.get(a[2]);
            graph.addEdge(new DirectedEdge(v, w, weight));
        }    
    }

    /**
     * Does the digraph contain the vertex named {@code s}?
     * @param s the name of a vertex
     * @return {@code true} if {@code s} is the name of a vertex, and {@code false} otherwise
     */
    public boolean contains(String s)
    {
        return nodeST.contains(s);
    }

    /**
     * Returns the integer associated with the vertex named {@code s}.
     * @param s the name of a vertex
     * @return the integer (between 0 and <em>V</em> - 1) associated with the vertex named {@code s}
     */
    public int indexOf(String s)
    {
        return nodeST.get(s);
    }

    /**
     * Returns the name of the vertex associated with the integer {@code v}.
     * @param  v the integer corresponding to a vertex (between 0 and <em>V</em> - 1)
     * @return the name of the vertex associated with the integer {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public String nameOf(int v)
    {
        validateVertex(v);
        return addresses[v];
    }

    /**
     * Returns the edge-weighted digraph associated with the symbol graph. It is the client's responsibility
     * not to mutate the digraph.
     *
     * @return the edge-weighted digraph associated with the symbol digraph
     */
    public EdgeWeightedDigraph digraph()
    {
        return graph;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v)
    {
        int V = graph.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
    
    @Override
	public String toString()
    {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append(nodeST.size() + " WALLETS\n");
    	sb.append("                    ADDRESS                       NODE NUMBER\n");
    	sb.append(" ---------------------------------------------------------------\n");
    	for (String name : nodeST.keys())
    	{
    		sb.append("   \"" + name + "\"        " + nodeST.get(name) + "\n");
    	}   	
    	sb.append("\n\n" + edgeST.size() + " TRANSACTIONS\n");
    	sb.append("                                HASH                                     EDGE NUMBER\n");
    	sb.append(" --------------------------------------------------------------------------------------\n");
    	for (String name : edgeST.keys())
    	{
    		sb.append("   \"" + name + "\"       " + edgeST.get(name) + "\n");
    	}
    	sb.append("\n\n");
    	sb.append(graph.toString());
    	
    	return sb.toString();
	}

	/**
     * Unit tests the {@code SymbolDigraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        String filename  = "edgeWeightSymbolDigraphTEST.csv";
        String delimiter = ",";
        BlockchainGraph sg = new BlockchainGraph(filename, delimiter);
        EdgeWeightedDigraph graph = sg.digraph();
        
        StdOut.print("enter a node name: ");
        String source = StdIn.readLine();
        StdOut.println();

            
            if (sg.contains(source)) {
                int s = sg.indexOf(source);
                if (graph.outdegree(s) != 0)
                {
                	for (DirectedEdge edge : graph.adj(s)) {StdOut.println("   " + edge);}
                }
                else
                {
                	StdOut.println("No edges leave this node.");
                }
            }
            else
            {
                StdOut.println("input not contain '" + source + "'");
            }
            
            StdOut.println("\number of wallets: " + sg.addresses.length);
            StdOut.println("addresses: ");
            for (String address : sg.addresses)
            {
            	StdOut.println(" " + address);
            }
            StdOut.println("\nnumber of transactions: " + sg.hashes.length);
            StdOut.println("hashes: ");
            for (String hash : sg.hashes)
            {
            	StdOut.println(" " + hash);
            }
            StdOut.println();
            StdOut.println(graph);
        
    }
}