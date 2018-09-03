package graphcoloring;

import java.util.HashSet;
import java.util.Random;

public class GA {
  private final Graph graph;
  private final int graphV;
  private final int maxGenerations;
  private final int populationSize;
  // fitness threshold for choosing a parent selection and mutation algorithm
  private final int fitnessThreshold;
  private Random rand;
  private int colors;

  public GA(Graph graph) {
    this(graph, 20000, 50, 4);
  }

  public GA(Graph graph, int maxGenerations, int populationSize, int fitnessThreshold) {
    if (graph == null || maxGenerations < 1 || populationSize < 2) {
      throw new IllegalArgumentException();
    }
    this.graph = graph;
    this.graphV = graph.V();
    this.maxGenerations = maxGenerations;
    this.populationSize = populationSize;
    this.fitnessThreshold = fitnessThreshold;
    this.rand = new Random();
  }

  public Solution getSolution(int colors) {
    this.colors = colors;
    long startTime = System.nanoTime();
    Population population = new Population();
    while (population.bestFitness() != 0 && population.generation() < maxGenerations) {
      population.nextGeneration();
    }
    long endTime = System.nanoTime();
    if (population.bestFitness() == 0) {
      return new Solution(population.bestIndividual(), graph, colors, population.generation(), endTime - startTime);
    }
    return new Solution(null, graph, colors, population.generation(), endTime - startTime);
  }

  private class Population {
    private Individual[] population;
    private int generation = 0;

    public Population() {
      population = new Individual[populationSize];
      for (int i = 0; i < populationSize; i++) {
        population[i] = new Individual();
      }
      sort();
    }

    public void nextGeneration() {
      int halfSize = populationSize / 2;
      Individual children[] = new Individual[halfSize];
      for (int i = 0; i < halfSize; i++) {
        Parents parents = selectParents();
        Individual child = new Individual(parents);
        child.mutate();
        children[i] = child;
      }
      for (int i = 0; i < halfSize; i++) {
        population[populationSize - i - 1] = children[i];
      }
      sort();
      generation++;
    }

    public int[] bestIndividual() {
      return population[0].chromosome;
    }

    public int bestFitness() {
      return population[0].fitness;
    }

    public int generation() {
      return generation;
    }

    private Parents selectParents() {
      return bestFitness() > fitnessThreshold ? selectParents1() : selectParents2();
    }

    private Parents selectParents1() {
      Individual tempParent1, tempParent2, parent1, parent2;
      tempParent1 = population[rand.nextInt(populationSize)];
      do {
        tempParent2 = population[rand.nextInt(populationSize)];
      } while (tempParent1 == tempParent2);
      parent1 = (tempParent1.fitness > tempParent2.fitness ? tempParent2 : tempParent1);
      do {
        tempParent1 = population[rand.nextInt(populationSize)];
        do {
          tempParent2 = population[rand.nextInt(populationSize)];
        } while (tempParent1 == tempParent2);
        parent2 = (tempParent1.fitness > tempParent2.fitness ? tempParent2 : tempParent1);
      } while (parent1 == parent2);
      return new Parents(parent1, parent2);
    }

    private Parents selectParents2() {
      return new Parents(population[0], population[1]);
    }

    private void sort() {
      int n = populationSize;
      do {
        int newn = 0;
        for (int i = 1; i < n; i++) {
          if (population[i - 1].fitness > population[i].fitness) {
            Individual temp = population[i - 1];
            population[i - 1] = population[i];
            population[i] = temp;
            newn = i;
          }
        }
        n = newn;
      } while (n != 0);
    }

    private class Individual {
      // each element of chromosome represents a color
      private int[] chromosome;
      // fitness is defined as the number of 'bad' edges, i.e., edges connecting two
      // vertices with the same color
      private int fitness;

      // random individual
      public Individual() {
        chromosome = new int[graphV];
        for (int i = 0; i < graphV; i++) {
          chromosome[i] = rand.nextInt(colors);
        }
        updateFitness();
      }

      // crossover
      public Individual(Parents parents) {
        chromosome = new int[graphV];
        int crosspoint = rand.nextInt(graphV);
        int c;
        for (c = 0; c <= crosspoint; c++) {
          chromosome[c] = parents.parent1.chromosome[c];
        }
        for (; c < graphV; c++) {
          chromosome[c] = parents.parent2.chromosome[c];
        }
        updateFitness();
      }

      public void mutate() {
        if (bestFitness() > fitnessThreshold) {
          mutate1();
        } else {
          mutate2();
        }
      }

      private void mutate1() {
        for (int v = 0; v < graphV; v++) {
          for (int w : graph.adj(v)) {
            if (chromosome[v] == chromosome[w]) {
              HashSet<Integer> validColors = new HashSet<>();
              for (int c = 0; c < colors; c++) {
                validColors.add(c);
              }
              for (int u : graph.adj(v)) {
                validColors.remove(chromosome[u]);
              }
              if (validColors.size() > 0) {
                chromosome[v] = (int) validColors.toArray()[rand.nextInt(validColors.size())];
              }
              break;
            }
          }
        }
        updateFitness();
      }

      private void mutate2() {
        for (int v = 0; v < graphV; v++) {
          for (int w : graph.adj(v)) {
            if (chromosome[v] == chromosome[w]) {
              chromosome[v] = rand.nextInt(colors);
              break;
            }
          }
        }
        updateFitness();
      }

      private void updateFitness() {
        int f = 0;
        for (int v = 0; v < graphV; v++) {
          for (int w : graph.adj(v)) {
            if (chromosome[v] == chromosome[w]) {
              f++;
            }
          }
        }
        fitness = f / 2;
      }
    }

    private class Parents {
      public final Individual parent1;
      public final Individual parent2;

      public Parents(Individual parent1, Individual parent2) {
        this.parent1 = parent1;
        this.parent2 = parent2;
      }
    }
  }
}
