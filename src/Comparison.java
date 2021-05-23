import java.util.IntSummaryStatistics;
import java.util.Random;

public class Comparison {
    public static void print(int[] table, int[] processSize, int[] blockSize){
        System.out.println("\nProcess No.\tProcess Size\tBlock no.    \tBlock Size");
        for (int i = 0; i < table.length; i++) {
            System.out.print("\t" + (i+1) + "\t\t\t" + processSize[i] + "\t\t\t");
            if (table[i] != -1) System.out.print("\t"  + (table[i] + 1) + "\t\t\t   " + blockSize[table[i]]);
            else System.out.print("Not Allocated   \t -");
            System.out.println();
        }
    }
    public static int failedCount(FitModel model, int[] processSize) {
        int[] table = model.getAllocation();
        int availableMemory = model.getAvailableMemory();
        int cnt = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] == -1 && availableMemory >= processSize[i]) cnt++;
        }
        return cnt;
    }

    public static int[] compareModels(int[] blockSize, int[] processSize) {
        FirstFit firstFit = new FirstFit(blockSize, processSize);
        BestFit bestFit = new BestFit(blockSize, processSize);
        WorstFit worstFit = new WorstFit(blockSize, processSize);
        int[] fragments = new int[3];
        fragments[0] = failedCount(firstFit, processSize);
        fragments[1] = failedCount(bestFit, processSize);
        fragments[2] = failedCount(worstFit, processSize);
        return fragments;
    }

    public static void printSummary(IntSummaryStatistics firstFit,
                               IntSummaryStatistics bestFit,
                               IntSummaryStatistics worstFit,
                                    int n
    ) {
        System.out.println("\t\t\t First Fit \tBest Fit \tWorst Fit");
        System.out.println("Total \t\t\t" + firstFit.getSum() + "\t  " + bestFit.getSum() + "\t\t\t" + worstFit.getSum());
        System.out.println("Worst Case \t\t" + firstFit.getMax() + "\t\t  " + bestFit.getMax() + "\t\t\t" + worstFit.getMax());
        System.out.println("Best Case \t\t" + firstFit.getMin() + "\t\t  " + bestFit.getMin() + "\t\t\t   " + worstFit.getMin());
        System.out.println("Average\t\t\t" + firstFit.getAverage() + "\t  " + bestFit.getAverage() + "\t\t" + worstFit.getAverage());
        System.out.printf("Percentage \t\t%.4f%%\t%.4f%%\t\t%.4f%%%n", firstFit.getAverage()*100.0/n, bestFit.getAverage()*100.0/n, worstFit.getAverage()*100.0/n);
    }

    public static void main(String[] args) {
        int no_of_simulation = 1000;
        int no_blocks =1000, no_process = 1000;
        int maxBlockSize = 1000, maxProcessSize = 1000;

        IntSummaryStatistics firstFitFragments = new IntSummaryStatistics();
        IntSummaryStatistics bestFitFragments = new IntSummaryStatistics();
        IntSummaryStatistics worstFitFragments = new IntSummaryStatistics();

        Random random = new Random();
        for (int i = 0; i < no_of_simulation; i++) {
            int[] blockSize = new int[no_blocks];
            int[] processSize = new int[no_process];
            for (int j = 0; j < no_blocks; j++) {
                blockSize[j] = random.nextInt(maxBlockSize);
            }
            for (int j = 0; j < no_process; j++) {
                processSize[j] = random.nextInt(maxProcessSize);
            }

            int[] fragments = compareModels(blockSize, processSize);
            firstFitFragments.accept(fragments[0]);
            bestFitFragments.accept(fragments[1]);
            worstFitFragments.accept(fragments[2]);
        }

        printSummary(firstFitFragments, bestFitFragments, worstFitFragments, no_process);
    }
}
