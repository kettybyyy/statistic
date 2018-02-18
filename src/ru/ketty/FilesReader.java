package ru.ketty;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesReader {

    private File logFolder;//Каталог с распакованными данными для сбора статистики

    private final List<String> listOfHours = new ArrayList<>(); //Список часов, когда была обнаружена ошибка

    private final Pattern errorPattern = Pattern.compile("ERROR"); //Паттерн для поиска ошибки в лог-файлах

    private final Pattern timePattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"); //Паттерн для поиска времени возникновения ошибки

    public FilesReader(File folder) {
        if (!folder.isDirectory()) throw new UnsupportedOperationException("LogReader work with folders, not files");
        logFolder = folder;
    }

    /**
     * Многопоточное чтение файлов
     *
     * Разделяет обработку списка файлов на кол-во потоков исполнения системы.
     * Списки обрабатываются в отдельных потоках.
     * В результате метод обязатеьно дожидается завершения всех запущенных потоков
     * и возвращает результат.
     * Каждый поток имеет локальное хранилище результатов, которое по итогу работы потока
     * добавляется в общее хранилище результатов синхронизированно.
     * @return listOfHours - список часов, когда была обнаружена ошибка
     */
    public List<String> readParallel() {
        long l = System.currentTimeMillis(); //Время начала чтения файлов
        int cores = Runtime.getRuntime().availableProcessors();
        List<String> plainFiles = FileUtils.plainListOfFiles(logFolder);
        List<List<String>> chopped = ListUtils.choppedOnParts(plainFiles, cores);
        List<Runnable> tasks = new ArrayList<>(); //список исполняемых задач
        List<Thread> threads = new ArrayList<>(); //список потоков, исполняющих задачи

        for (List<String> subList : chopped) {
            tasks.add(new FileStatReaderTask(subList));
        }

        for (Runnable task : tasks) {
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long finalTime = System.currentTimeMillis() - l; //время окончания чтения файлов
        System.out.println("Time for read files: " + finalTime);
        return listOfHours;

    }

    /**
     * Класс FileStatReaderTask позволяет читать файлы
     * в несколько потоков
     */
    private class FileStatReaderTask implements Runnable {

        private List<String> filesToRead; //Список файлов для чтения

        private List<String> localResult = new ArrayList<>(); //Список часов ошибок для отдельного потока

        public FileStatReaderTask(List<String> filesToRead) {
            this.filesToRead = filesToRead;
        }

        @Override
        public void run() {
            for (String fileName : filesToRead) {
                try {
                    FileReader fileReader = new FileReader(fileName); //читаемый файл
                    BufferedReader br = new BufferedReader(fileReader);
                    String line;
                    //Читаем по строкам
                    while ((line = br.readLine()) != null) {
                        //Если в текущей строке найдена "ERROR"
                        if (errorPattern.matcher(line).find()) {
                            //Поиск в текущей строке времени возникновения ошибки
                            Matcher matcher = timePattern.matcher(line);
                            matcher.find();
                            //Добавить в час в список
                            localResult.add(line.substring(matcher.start(), matcher.end()).substring(0, 2));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //Синхронизированное заполнение результирующего списка часов ошибок
            synchronized (listOfHours) {
                listOfHours.addAll(localResult);
            }
        }
    }

}
