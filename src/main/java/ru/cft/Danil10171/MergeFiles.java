package ru.cft.Danil10171;

import java.io.*;
import java.util.List;

public class MergeFiles extends Arguments {
    public static void Merge(SortType sortType, DataType dataType, String outFile, List<String> inFiles) {

        int filesNum = inFiles.size(); //Кол-во входных файлов
        BufferedReader[] fileReader = null; //Массив входных файлов
        File directory = new File("");

        //Указываем выходной файл
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(directory.getAbsolutePath() + "/" + outFile))) {

            //Открываем все входные файлы для чтения
            fileReader = new BufferedReader[filesNum];
            String[] in = inFiles.toArray(new String[0]);

            for (int i = 0; i < filesNum; i++) {
                fileReader[i] = new BufferedReader(new FileReader(directory.getAbsolutePath() + "/" + in[i]));
            }

            //Записываем в массив по первому символу из каждого входного файла
            String[] symbols = new String[filesNum];
            for (int i = 0; i < filesNum; i++) {
                symbols[i] = fileReader[i].readLine(); //Считываем строку
                if (symbols[i] != null && dataType == DataType.INT) { //Для типа integer: проверяем тип данных, пока не найдем нужный
                    try {
                        Integer.parseInt(symbols[i]);
                    } catch (NumberFormatException exc) {
                        i--; //В случае ошибки - вновь считываем из того же потока
                    }
                }
            }

            String min, lastMin = null; //min - используется для минимальной строки, lastMin - используется для проверки порядка сортировки в файлах
            int index = -1; //Индекс файла, откуда взята минимальная строка

            //Вторая фаза merge sort - слияние файлов в один
            while (true) {
                min = null;

                //Находим первый доступный элемент, указываем его в качестве минимального
                for (int i = 0; i < filesNum; i++) {
                    if (symbols[i] != null) {
                        min = symbols[i];
                        index = i;
                        break;
                    }
                }

                //Проверяем min: если все входные файлы подошли к концу, то min должно быть равно null, выходим из цикла
                if (min == null) {
                    break;
                }

                //Цикл находит минимальный элемент в массиве строк
                for (int i = 0; i < filesNum; i++) {
                    try { //Блок try-catch позволяет отловить тот случай, когда в файле с int данными встречается какой-либо другой тип
                        if (symbols[i] != null && Compare(sortType,dataType, min, symbols[i])) {
                            min = symbols[i];
                            index = i;
                        }
                    } catch (NumberFormatException exc) {
                        symbols[i] = fileReader[i--].readLine(); //При ошибке - пропускаем строку и переходим к следующей, уменьшив i на 1
                    }
                }

                //Если выбранная строка не соответсвует порядку сортировки (сравниваем с lastMin) - пропускаем её, переходим к следующей
                try {
                    if (lastMin != null && Compare(sortType,dataType, lastMin, min)) {
                        symbols[index] = fileReader[index].readLine();
                        continue;
                    }
                } catch (NumberFormatException exc) { //Если min содержит неправильный формат (для int), то также пропускаем строку
                    symbols[index] = fileReader[index].readLine();
                    continue;
                }

                //Если выбранная строка (ключ -s) содержит пробелы, то она - ошибочная, её необходимо убрать
                if (dataType == DataType.STR && min.matches(".*\\s.*")) {
                    symbols[index] = fileReader[index].readLine();
                    continue;
                }

                fileWriter.write(min); //Записываем минимальную строку в выходной файл
                fileWriter.write('\n');
                lastMin = min;
                symbols[index] = fileReader[index].readLine(); //Считываем следующую строку из файла
            }

        } catch (ArrayIndexOutOfBoundsException exc) {
            throw new RuntimeException("\nArray Index Out Of Bounds Exception\n");
        } catch (IOException exc) {
            throw new RuntimeException("\nInput-Output Exception\n");
        } finally {

            //Закрываем потоки данных
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

    //Метод для сравнения двух значений с использованием определённых ключей
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
