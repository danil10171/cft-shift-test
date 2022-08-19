public class Main {
    public static void main(String[] args) {
        Arguments mergeFiles = Arguments.ArgumentsParser(args);
        System.out.println("SortType: " + mergeFiles.sortType);
        System.out.println("DataType: " + mergeFiles.dataType);
        System.out.println("Output file: " + mergeFiles.outFile);
        System.out.println("Input files: " + mergeFiles.inFiles);
        MergeFiles.Merge(mergeFiles.sortType, mergeFiles.dataType, mergeFiles.outFile, mergeFiles.inFiles);
    }
}
