package graphcoloring;

import java.io.PrintWriter;
import java.text.DecimalFormat;

public class Solution {
  public final int[] chromosome;
  public final Graph graph;
  public final int colors;
  public final int generations;
  public final long time; // in nanoseconds

  public Solution(int[] chromosome, Graph graph, int colors, int generations, long time) {
    this.chromosome = chromosome;
    this.graph = graph;
    this.colors = colors;
    this.generations = generations;
    this.time = time;
  }

  public Drawer draw() {
    if (chromosome == null) {
      return null;
    }
    return new Drawer(this);
  }

  public void save() {
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(graph.filename + "." + colors + ".solution");
      writer.print(toString());
    } catch (Exception ex) {
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  @Override
  public String toString() {
    String newline = System.lineSeparator();
    String solution;
    if (chromosome == null) {
      solution = "(no solution)" + newline;
    } else {
      solution = newline;
      for (int v = 0; v < chromosome.length; v++) {
        solution += String.valueOf(v + 1) + " " + chromosome[v] + newline;
      }
    }
    return
      "Vertices: " + graph.V() + newline +
      "Edges: " + graph.E() + newline +
      "Colors: " + colors + newline +
      "Generations: " + generations + newline +
      "Time: " + (new DecimalFormat("0.000")).format(time / 1e9) + "s" + newline +
      "Solution: " + solution;
  }
}
