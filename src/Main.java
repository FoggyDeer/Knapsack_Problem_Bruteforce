import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File file = new File("Data.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath());
        }catch (IOException e){
            System.out.println("\u001B[31mError while reading file\u001B[0m");
            return;
        }

        double knapsackCapacity = Integer.parseInt(lines.getFirst().split(" ")[0]);
        int itemsCount = Integer.parseInt(lines.getFirst().split(" ")[1]);

        double[] itemsValues = mapStringToDoubleArray(lines.get(1).split(","));
        double[] itemsSizes = mapStringToDoubleArray(lines.get(2).split(","));

        if (itemsValues.length != itemsCount) {
            System.out.println("\u001B[31mProvided wrong length of Values array\u001B[0m");
            return;
        } else if (itemsSizes.length != itemsCount) {
            System.out.println("\u001B[31mProvided wrong length of Sizes array\u001B[0m");
            return;
        }

        double bestValue = 0;
        double tempValue;
        double bestSize = knapsackCapacity;
        double tempSize;
        int[] bestItemsCombination = new int[itemsCount];
        int[] itemsCombination = new int[itemsCount];

        long iterationsTotalCount = (long) Math.pow(2, itemsCount);
        long printFrequency = (long) Math.pow(10, (int) Math.log10(iterationsTotalCount) - 1) ;

        for(long j = 0; j < iterationsTotalCount; j++){
            if(j % printFrequency == 0 && j != 0){
                System.out.println("Current iteration: " + j + "/" + iterationsTotalCount);
                System.out.println("Best items combination: " + Arrays.toString(bestItemsCombination));
                System.out.println("Best value: " + bestValue);
                System.out.println("-----------------------------------");
                System.out.println();
            }

            if(doesArrayFit(itemsCombination, itemsSizes, knapsackCapacity)){
                tempValue = countItemsValue(itemsCombination, itemsValues);
                tempSize = countItemsSize(itemsCombination, itemsSizes);
                if(tempValue > bestValue || (tempValue == bestValue && tempSize < bestSize)){
                    bestValue = tempValue;
                    bestSize = tempSize;
                    System.arraycopy(itemsCombination, 0, bestItemsCombination, 0, itemsCount);
                }
            }

            incrementBinaryArray(itemsCombination);
        }
        System.out.println("Best items combination: " + Arrays.toString(bestItemsCombination));
        System.out.println("Best items value: " + bestValue);
        System.out.println("Knapsack fill level: " + bestSize / knapsackCapacity * 100 + "%");
    }

    public static void incrementBinaryArray(int[] array){
        int temp = 1;
        for (int i = array.length - 1; i >=0 && temp == 1; i--) {
            if(array[i] == 0) {
                array[i] = 1;
                temp = 0;
            } else {
                array[i] = 0;
            }
        }
    }

    public static boolean doesArrayFit(int[] itemsToPack, double[] itemsSizes, double knapsackCapacity){
        return knapsackCapacity >= countItemsSize(itemsToPack, itemsSizes);
    }

    public static double countItemsSize(int[] itemsToCount, double[] itemsSizes){
        double countedSize = 0;
        for(int i = 0; i < itemsToCount.length; i++){
            if(itemsToCount[i] == 1){
                countedSize += itemsSizes[i];
            }
        }
        return countedSize;
    }

    public static double countItemsValue(int[] itemsToCount, double[] itemsValues){
        double countedValue = 0;
        for(int i = 0; i < itemsToCount.length; i++){
            if(itemsToCount[i] == 1){
                countedValue += itemsValues[i];
            }
        }
        return countedValue;
    }

    public static double[] mapStringToDoubleArray(String[] array){
        double[] newArray = new double[array.length];
        for(int i = 0; i < array.length; i++){
            newArray[i] = Double.parseDouble(array[i]);
        }
        return newArray;
    }
}