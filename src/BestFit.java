public class BestFit implements FitModel{
    private int[] blockSize, processSize, allocation;
    int availableMemory;
    BestFit(int[] blockSize, int[] processSize) {
        this.blockSize = blockSize.clone();
        this.processSize = processSize.clone();
        allocation = new int[processSize.length];
        for (int i = 0; i < allocation.length; i++) {
            allocation[i] = -1;
        }
        availableMemory = 0;
        for (int i = 0; i < blockSize.length; i++) availableMemory += blockSize[i];
        fit();
    }
    private void fit() {
        for (int i=0; i< processSize.length; i++)
        {
            int current_best = -1;
            for (int j=0; j< blockSize.length; j++)
            {
                if (blockSize[j] >= processSize[i])
                {
                    if (current_best == -1)
                        current_best = j;
                    else if (blockSize[current_best] > blockSize[j])
                        current_best = j;
                }
            }
            if (current_best != -1)
            {
                allocation[i] = current_best;
                blockSize[current_best] -= processSize[i];
                availableMemory -= processSize[i];
            }
        }
    }

    public int[] getAllocation() {
        return allocation;
    }

    public int getAvailableMemory(){
        return availableMemory;
    }
}
