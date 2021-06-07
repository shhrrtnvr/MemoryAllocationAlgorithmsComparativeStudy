## Memory Allocation Algorithms ComparativeStudy
This project addresses a fundamental issue in memory allocation which is fragmentation. In this project we studied three common memory allocation algorithm namely First Fit, Best Fit and Worst Fit. 
Then we conducted a detailed comparison of these algorithms and their performance in memory utilization and effects of fragmentation on these algorithms.
This was a Lab project in our CSE 202 course in ULAB

## Algorithms Overview
One of the methods for allocating memory is to divide memory into several fixed-sized partitions. 
Each partition may contain exactly one process. In the variable-partition scheme, the operating system keeps a table indicating which parts of memory are available and which are occupied. 
Initially, all memory is available for user processes and is considered one large block of available memory, a hole. 
As processes enter the system, they are put into an input queue. The operating system takes into account the memory requirements of each process and the amount of available memory space in determining which processes are allocated memory. 
At any given time, then, we have a list of available block sizes and an input queue or process size. 
Memory is allocated to processes until the memory requirements of the next process cannot be satisfied.
Following Section describes the Algorthims briefly

#### 1. First Fit
This algorithm allocates the first hole that is big enough. Searching can start either at the beginning of the set of holes or at the location where the previous first-fit search ended. 
Our implementation started searching from the beginning of the blocks. The searching was stopped as soon as we found a free hole that was large enough. 
The following code snippet illustrates to process of searching and allocating. 
Here, the list blockSize holds the list of available memory holes <br>
and list processSize holds the queue of processes that need memory to be allocated
``` java
for (int i = 0; i < processSize.length; i++)
   for (int j = 0; j < blockSize.length; j++){
       if (blockSize[j] >= processSize[i]){
           allocation[i] = j;
           blockSize[j] -= processSize[i];
           break;
	}
}
```
#### 2. Best Fit
Best fit llocates the smallest hole that is big enough. We must search the entire list, unless the list is ordered by size. 
Our implementation had an unordered list of block sizes named blockSize. 
This strategy produces the smallest leftover hole. Following code does the searching and allocation. 
The list processSize holds the queue of processes that needs memory to be allocated.
```java
for (int i=0; i< processSize.length; i++){
   int current_best = -1;
   for (int j=0; j< blockSize.length; j++){
       if (blockSize[j] >= processSize[i]){
           if (current_best == -1)
               current_best = j;
           else if (blockSize[current_best] > blockSize[j])
               current_best = j;
       }
   }
   if (current_best != -1){
       allocation[i] = current_best;
       blockSize[current_best] -= processSize[i];
       availableMemory -= processSize[i];
   }
}
```
#### 3. Worst Fit
Worst Fit allocates the largest hole. Like best fit, we had to search the entire list. 
This strategy produces the largest leftover hole, which may be more useful than the smaller leftover hole from a best-fit approach. 
Following is the code of the searching and allocation part.
``` java
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
```
## Basis of Comparison
As processes are loaded and removed from memory, the free memory space is broken into little pieces.
External fragmentation exists when there is enough total memory space to satisfy a request but the available spaces are not contiguous; storage is fragmented into a large number of small holes. 
All three algorithms suffer from external fragmentation. And our study address this issue by carefully measuring the effect of fragmentation in these alogirthms. <br>
#### Measure of fragmentation
To conduct a comparative study we need a way to measure the effect of fragmentation. The following process of measuring fragmentation and transform framentation from 
qualitative feature to a quantitative feature is unique contribution to this project. <br>
If we go back to the basics and ask what is the problem caused by fragmentation we can find the answer. Because of fragmentation memory becomes fragments and makes it
discontinuous. Threfore, even if there is more memory available than a process needs, it can not find continuous segements to allocate to the process. Lets say, we have
a process that nees 100MB memory and our available block size is 80MB and 70MB. Even though total available memory exceeds the need, we can not allocate it to the process. <br>
We will give the algorithm the available block sizes and list of processes with their memory needs and count the number of process that could not get allocation even 
though there is enough memory available. The algorithm that can allocate memory to many processes will be considered better performing. 

## Implemetation
Our implementation has two parts. 
1. Implementation of Algorithms.
2. Comparison class implementation.
#### Algorithms implementation
All three algorithms <em>FirstFit</em>, <em>BestFit</em> and <em>WorstFit</em> have their own classes and they implement a common interface FitModel. Following is the FitModel API
``` Java
public interface FitModel {
   public int getAvailableMemory();
   public int[] getAllocation();
}
```
This interface has two function getAvailableMemory and getAllocation. <br>
<em>getAvailableMemory</em> returns the amount of memory available after allocation. <br>
<em>getAllocation</em> returns the <em>allocation</em> table which is an array containing the block number allocated to the process number. j<sup>th</sup> block is allocated to i<sup>th</sup> process then `allocation[i] = j` <br>
Inside each three algorithms they all have common functions. For example, FirstFit class API is as following
``` Java
public class FirstFit implements FitModel {
   private int[] blockSize, processSize, allocation;
   int availableMemory;
   FirstFit(int[] blockSize, int[] processSize);
   private void fit();
   @Override
   public int[] getAllocation();
   @Override
   public int getAvailableMemory();
}
```
<em>blockSize</em> array contains availble memory blocks size corresponding to block id. For simplicity block id is the index and value is memory inside the block <br>
<em>processSize</em> array contains memory needed for each process. Similarly, index is the process id and value is the memory needed for the process. <br>
<em>allocation</em> array is the table that contains the maaping of process id -> block id which is explained earlier. <br>
Constructor takes two arguments and initializes varibles with those arguments. And call fit() function. <br>
fit() mainly does the computation and allocates memory to the process according to the algorithm. <br>

#### Comparison class Implementation
For conducting comparison, we implemented the class named Comparison. It has the following API
``` Java
public class Comparison {
   public static void print(int[] table, int[] processSize, int[] blockSize);
   public static int failedCount(FitModel model, int[] processSize);
   public static int[] compareModels(int[] blockSize, int[] processSize);
   public static void printSummary(IntSummaryStatistics firstFit, IntSummaryStatistics bestFit, IntSummaryStatistics worstFit, int n );
   public static void main (String[] args);
}
```
<em>compareModels</em> function takes available block size and process size lists and create instances of three algorithms using these given block size and process size. 
Then it gets the number of times each algorithm failed by calling failedCount. Finally it returns the counts as array to the caller.
<em>failedCount</em> function takes the algorithm model and calculates the effect of fragmentation by counting number of process that could not get memory allocation even though enough memory is available.
## Results
Results from the study will be added later.



