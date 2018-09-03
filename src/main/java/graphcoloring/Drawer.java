package graphcoloring;

import java.awt.*;
import java.util.Random;
import javax.swing.JFrame;

public class Drawer extends JFrame {
  private static final long serialVersionUID = 1;
  private Solution solution;
  private Color[] palette;
  private Point[] vertexPoints;
  private final int vertexDiag;
  private final int vertexRad;

  public Drawer(Solution solution) {
    this.solution = solution;
    vertexDiag = 40;
    vertexRad = 20;
    setTitle(solution.graph.filename);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
    setBackground(Color.white);
    palette = new Color[solution.colors];
    for (int i = 0; i < solution.colors; i++) {
      palette[i] = Color.getHSBColor((float) i / solution.colors, 1, 1);
    }
    int margin = 100;
    int xm = getBounds().width - margin * 2;
    int ym = getBounds().height - margin * 2;
    Random rand = new Random();
    vertexPoints = new Point[solution.graph.V()];
    for (int i = 0; i < vertexPoints.length; i++) {
      vertexPoints[i] = new Point(rand.nextInt(xm) + margin, rand.nextInt(ym) + margin);
      for (int j = 0; j < i; j++) {
        if (distance(vertexPoints[i], vertexPoints[j]) < vertexDiag) {
          i--;
          break;
        }
      }
    }
    setVisible(true);
  }

  private int distance(Point p1, Point p2) {
    return (int) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
  }

  @Override
  public void paint(Graphics g) {
    g.setColor(Color.black);
    for (int v = 0; v < solution.graph.V(); v++) {
      for (int w : solution.graph.adj(v)) {
        g.drawLine(vertexPoints[v].x, vertexPoints[v].y, vertexPoints[w].x, vertexPoints[w].y);
      }
    }
    FontMetrics fm = g.getFontMetrics();
    int h = fm.getHeight() / 2;
    for (int v = 0; v < solution.graph.V(); v++) {
      g.setColor(palette[solution.chromosome[v]]);
      g.fillOval(vertexPoints[v].x - vertexRad, vertexPoints[v].y - vertexRad, vertexDiag, vertexDiag);
      g.setColor(Color.black);
      String s = String.valueOf(v + 1);
      g.drawString(s, vertexPoints[v].x - fm.stringWidth(s) / 2, vertexPoints[v].y + h);
    }
  }
}
