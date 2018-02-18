package ru.ketty;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Помощник для обработки файлов
 */

public class FileUtils {

    /**
     * Метод plainListOfFiles формирует список файлов, находящихся в директории, куда был распакован архив
     * @param directory - директория, куда был распакован архив
     * @return result - список файлов
     */
    public static List<String> plainListOfFiles(File directory) {
        List<String> result = new ArrayList<>();
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) { //Если директория
                result.addAll(plainListOfFiles(file)); //Получить список ее файлов
            } else {
                result.add(file.getAbsolutePath()); //получить абсолютный путь
            }
        }
        return result;
    }
}
