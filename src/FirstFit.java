public class FirstFit implements FitModel {
    private int[] blockSize, processSize, allocation;
    int availableMemory;
    FirstFit(int[] blockSize, int[] processSize) {
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
        for (int i = 0; i < processSize.length; i++) {
            for (int j = 0; j < blockSize.length; j++) {
                if (blockSize[j] >= processSize[i]) {
                    allocation[i] = j;
                    blockSize[j] -= processSize[i];
                    availableMemory -= processSize[i];
                    break;
                }
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
