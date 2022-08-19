package ru.cft.Danil10171;

import java.io.*;
import java.util.List;

public class MergeFiles extends Arguments {
    public static void Merge(SortType sortType, DataType dataType, String outFile, List<String> inFiles) {

        int filesNum = inFiles.size();
        BufferedReader[] fileReader = null;
        File directory = new File("");

        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(directory.getAbsolutePath() + "/" + outFile))) {

            fileReader = new BufferedReader[filesNum];
            String[] in = inFiles.toArray(new String[0]);

            for (int i = 0; i < filesNum; i++) {
                fileReader[i] = new BufferedReader(new FileReader(directory.getAbsolutePath() + "/" + in[i]));
            }

            String[] symbols = new String[filesNum];
            for (int i = 0; i < filesNum; i++) {
                symbols[i] = fileReader[i].readLine();
                if (symbols[i] != null && dataType == DataType.INT) {
                    try {
                        Integer.parseInt(symbols[i]);
                    } catch (NumberFormatException exc) {
                        i--;
                    }
                }
            }

            String min, lastMin = null;
            int index = -1;

            while (true) {
                min = null;
                for (int i = 0; i < filesNum; i++) {
                    if (symbols[i] != null) {
                        min = symbols[i];
                        index = i;
                        break;
                    }
                }

                if (min == null) {
                    break;
                }

                for (int i = 0; i < filesNum; i++) {
                    try {
                        if (symbols[i] != null && Compare(sortType,dataType, min, symbols[i])) {
                            min = symbols[i];
                            index = i;
                        }
                    } catch (NumberFormatException exc) {
                        symbols[i] = fileReader[i--].readLine();
                    }
                }

                try {
                    if (lastMin != null && Compare(sortType,dataType, lastMin, min)) {
                        symbols[index] = fileReader[index].readLine();
                        continue;
                    }
                } catch (NumberFormatException exc) {
                    symbols[index] = fileReader[index].readLine();
                    continue;
                }

                if (dataType == DataType.STR && min.matches(".*\\s.*")) {
                    symbols[index] = fileReader[index].readLine();
                    continue;
                }

                fileWriter.write(min);
                fileWriter.write('\n');
                lastMin = min;
                symbols[index] = fileReader[index].readLine();
            }

        } catch (ArrayIndexOutOfBoundsException exc) {
            throw new RuntimeException("\nArray Index Out Of Bounds Exception\n");
        } catch (IOException exc) {
            throw new RuntimeException("\nInput-Output Exception\n");
        } finally {
            try {
                for (int i = 0; i < filesNum; i++) {
                    if (fileReader[i] != null) {
                        fileReader[i].close();
                    }
                }
            } catch (NullPointerException exc) {
                throw new RuntimeException("\nTry close object null:\n");
            } catch (ArrayIndexOutOfBoundsException exc) {
                throw new RuntimeException("\nArrayIndexOutOfBoundsException\n");
            } catch (IOException exc) {
                throw new RuntimeException("\nInput-Output Exception \n");
            }
        }
    }

    private static boolean Compare(SortType sortType, DataType dataType, String s1, String s2) throws NumberFormatException {

        return switch (dataType) {
            case INT -> sortType == SortType.ASC
                    ? Integer.parseInt(s1) > Integer.parseInt(s2)
                    : Integer.parseInt(s1) < Integer.parseInt(s2);

            case STR -> sortType == SortType.ASC
                    ? s1.compareTo(s2) > 0
                    : s1.compareTo(s2) < 0;
        };
    }
}
