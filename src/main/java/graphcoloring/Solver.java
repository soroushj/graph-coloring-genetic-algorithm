package graphcoloring;

import java.io.IOException;
import java.util.Scanner;

public class Solver {
  public static void main(String[] args) {
    String filename = null;
    String colorsStr = null;
    int colors = 0;
    Scanner sc = new Scanner(System.in);
    System.out.print("Graph file path: ");
    if (args.length >= 1) {
      filename = args[0];
      System.out.println(filename);
    } else {
      filename = sc.next();
    }
    System.out.print("Number of colors: ");
    if (args.length >= 2) {
      colorsStr = args[1];
      System.out.println(colorsStr);
    } else {
      colorsStr = sc.next();
    }
    sc.close();
    try {
      colors = Integer.parseInt(colorsStr);
    } catch (NumberFormatException ex) {
      System.out.println("Invalid number of colors: " + colorsStr);
      System.exit(1);
    }
    Graph graph = null;
    try {
      graph = new Graph(filename);
    } catch (IOException ex) {
      System.out.println("Cannot read graph file: " + filename);
      System.exit(1);
    } catch (IllegalArgumentException ex) {
      System.out.println("Invalid graph file: " + filename);
      System.exit(1);
    }
    System.out.print("Solving... ");
    GA ga = new GA(graph);
    Solution solution = ga.getSolution(colors);
    System.out.println("Done.");
    System.out.println(solution);
    solution.save();
    solution.draw();
  }
}
