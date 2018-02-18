package ru.ketty;
import java.io.*;

public class Main {


    public static void main(String[] args) throws IOException{

        String archive = args[0]; //Получить путь к архиву

        FileExtractor fileExtractor = FileExtractor.of(archive);
        fileExtractor.extract7z(); //Распаковать архив

        FilesReader filesReader = new FilesReader(fileExtractor.getTempFolder()); //Считать ошибки из распакованных файлов

        StatisticsHour statisticsHour = new StatisticsHour(filesReader.readParallel()); //Передать данные для сбора статистики
        statisticsHour.gather();//Собрать статистику ошибок по часам
        statisticsHour.store(fileExtractor.getTempFolder().getParent()); //Выгрузка статистики в текстовый файл
   }
}
