
public class WorstFit implements FitModel{
    private int[] blockSize, processSize, allocation;
    int availableMemory;
    WorstFit(int[] blockSize, int[] processSize) {
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
        for (int i=0; i< processSize.length; i++) {
            int current_max = 0;
            for (int j=1; j< blockSize.length; j++) {
                if (blockSize[j] > blockSize[current_max])
                    current_max = j;
            }
            if (blockSize[current_max] >= processSize[i]) {
                allocation[i] = current_max;
                blockSize[current_max] -= processSize[i];
                availableMemory -= processSize[i];
            }
        }
    }
    @Override
    public int[] getAllocation() {
        return allocation;
    }
    @Override
    public int getAvailableMemory(){
        return availableMemory;
    }
}
