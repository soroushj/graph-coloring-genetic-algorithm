package graphcoloring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Graph {
  public final String filename;
  private int V;
  private int E;
  private Bag[] adj;

  public Graph(String filename) throws IOException, IllegalArgumentException {
    this.filename = filename;
    BufferedReader input = new BufferedReader(new FileReader(filename));
    String line;
    try {
      while ((line = input.readLine()) != null) {
        String[] p = line.split("\\s");
        if (p.length > 0 && p[0].equals("c")) {
          continue;
        }
        if (p.length == 4 && p[0].equals("p") && p[1].equals("edge") && adj == null) {
          V = Integer.parseInt(p[2]);
          adj = new Bag[V];
          for (int v = 0; v < V; v++) {
            adj[v] = new Bag();
          }
        } else if (p.length == 3 && p[0].equals("e") && adj != null) {
          addEdge(Integer.parseInt(p[1]) - 1, Integer.parseInt(p[2]) - 1);
        }
      }
    } catch (IOException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new IllegalArgumentException();
    } finally {
      input.close();
    }
  }

  public int V() {
    return V;
  }

  public int E() {
    return E;
  }

  private void addEdge(int v, int w) {
    if (v < 0 || v >= V || w < 0 || w >= V) {
      throw new IndexOutOfBoundsException();
    }
    if (v == w) {
      throw new IllegalArgumentException();
    }
    adj[v].add(w);
    adj[w].add(v);
    E++;
  }

  public Iterable<Integer> adj(int v) {
    if (v < 0 || v >= V) {
      throw new IndexOutOfBoundsException();
    }
    return adj[v];
  }

  private class Bag implements Iterable<Integer> {
    private Node first = null;

    public void add(int item) {
      Node oldFirst = first;
      first = new Node();
      first.item = item;
      first.next = oldFirst;
    }

    @Override
    public Iterator<Integer> iterator() {
      return new ListIterator(first);
    }

    private class Node {
      private int item;
      private Node next;
    }

    private class ListIterator implements Iterator<Integer> {
      private Node current;

      public ListIterator(Node first) {
        current = first;
      }

      @Override
      public boolean hasNext() {
        return current != null;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Integer next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        int item = current.item;
        current = current.next;
        return item;
      }
    }
  }
}
