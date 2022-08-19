import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Arguments {
    public SortType sortType;
    public DataType dataType;
    public String outFile;
    public List<String> inFiles;

    public Arguments() {
        this.sortType = SortType.ASC;
        this.dataType = null;
        this.outFile = null;
        this.inFiles = null;
    }


    public Arguments(SortType sortType, DataType dataType, String outFile, List<String> inFiles) {
        this.sortType = sortType;
        this.dataType = dataType;
        this.outFile = outFile;
        this.inFiles = inFiles;
    }

    protected enum SortType { ASC, DES }
    protected enum DataType { STR, INT }

    //public static Arguments getDataType(){
    //    return Arguments.DataType;
    //}
    public static Arguments ArgumentsParser(String[] args) {

        if (args.length != 0) {
            SortType sortType = SortType.ASC;
            DataType dataType = null;

            // чтение аргументов
            Object[] arguments = Arrays.stream(args)
                    .filter(arg -> arg.startsWith("-")).toArray();

            for (Object arg : arguments) {
                String string = arg.toString();
                switch (string) {
                    case "-a" -> sortType = SortType.ASC;
                    case "-d" -> sortType = SortType.DES;
                    case "-s" -> dataType = DataType.STR;
                    case "-i" -> dataType = DataType.INT;
                }
            }

            if (dataType == null) {
                throw new RuntimeException("\n\nIncorrect data type argument! Check the correctness of the argument on the command line!\nRight starting arguments:\n\t-i  -  integer\n\t-s  -  string\n");
            }

            String outFile = null;
            List<String> inFiles = new ArrayList<>();
            for (String arg : args) {
                if (arg.endsWith(".txt")) {
                    if (outFile == null) {
                        outFile = arg;
                    } else {
                        inFiles.add(arg);
                    }
                }
            }
            if (outFile == null) {
                throw new RuntimeException("\nNo entered output files!\nPlease check command line for the name of the output file and that there is a file extension \"*.txt\"\n");
            }
            if (inFiles.isEmpty()) {
                throw new RuntimeException("\nNo entered input files!\nPlease check command line for the name of the input files and that there is a file extension \"*.txt\"\n");
            }
            return new Arguments(sortType, dataType, outFile, inFiles);
        } else { throw new RuntimeException("Пустая командная строка"); }
    }


}
