/*
 * Authors: T. Stratton, Robert Sedgewick, Kevin Wayne
 * Date adapted: 04 JUL 2024
 * Last updated: 04 JUL 2024
 * 
 * File Contents:
 *  
 * 
 * Notes:
 * 	need to add overloaded constructor to accept a hashmap
 * 
 */

package blockchainvisualizer;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.SymbolDigraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 *  The {@code EdgeWeightedSymbolDigraph} class represents a directed graph, where the
 *  vertex names are arbitrary strings and each edge has a weight.
 *  It is based on the class {@code SymbolDigraph} from algs4.
 *  
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
public class EdgeWeightedSymbolDigraph {
    private ST<String, Integer> st;    // string -> index
    private String[] keys;             // index  -> string
    private EdgeWeightedDigraph graph; // the underlying digraph

    /**
     * Initializes a digraph from a file using the specified delimiter.
     * Each line in the file contains
     * the name of a vertex, followed by a list of the names
     * of the vertices adjacent to that vertex, separated by the delimiter.
     * @param filename the name of the file
     * @param delimiter the delimiter between fields
     */
    public EdgeWeightedSymbolDigraph(String filename, String delimiter) {
        st = new ST<String, Integer>();

        // First pass builds the index by reading strings to associate
        // distinct strings with an index
        In in = new In(filename);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);
            for (int i = 0; i < a.length; i++) {
                if (!st.contains(a[i]))
                    st.put(a[i], st.size());
            }
        }

        // inverted index to get string keys in an array
        keys = new String[st.size()];
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }

        // second pass builds the digraph by connecting first vertex on each
        // line to all others
        graph = new EdgeWeightedDigraph(st.size());
        in = new In(filename);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);
            //int v = st.get(a[0]);
            for (int i = 1; i < a.length; i++) {
                //int w = st.get(a[i]);
                int v = st.get(a[0]);
                int w = st.get(a[1]);
                double weight = Double.parseDouble(a[2]);
                graph.addEdge(new DirectedEdge(v, w, weight));
            }
        }
    }

    /**
     * Does the digraph contain the vertex named {@code s}?
     * @param s the name of a vertex
     * @return {@code true} if {@code s} is the name of a vertex, and {@code false} otherwise
     */
    public boolean contains(String s) {
        return st.contains(s);
    }

    /**
     * Returns the integer associated with the vertex named {@code s}.
     * @param s the name of a vertex
     * @return the integer (between 0 and <em>V</em> - 1) associated with the vertex named {@code s}
     */
    public int indexOf(String s) {
        return st.get(s);
    }

    /**
     * Returns the name of the vertex associated with the integer {@code v}.
     * @param  v the integer corresponding to a vertex (between 0 and <em>V</em> - 1)
     * @return the name of the vertex associated with the integer {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public String nameOf(int v) {
        validateVertex(v);
        return keys[v];
    }

    /**
     * Returns the edge-weighted digraph associated with the symbol graph. It is the client's responsibility
     * not to mutate the digraph.
     *
     * @return the edge-weighted digraph associated with the symbol digraph
     */
    public EdgeWeightedDigraph digraph() {
        return graph;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = graph.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Unit tests the {@code SymbolDigraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String filename  = args[0];
        String delimiter = args[1];
        SymbolDigraph sg = new SymbolDigraph(filename, delimiter);
        Digraph graph = sg.digraph();
        while (!StdIn.isEmpty()) {
            String t = StdIn.readLine();
            for (int v : graph.adj(sg.indexOf(t))) {
                StdOut.println("   " + sg.nameOf(v));
            }
        }
    }
}