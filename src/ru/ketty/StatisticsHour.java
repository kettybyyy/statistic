/**
 * Сборщик статистики
 * @author Karpova E.A.
 */
package ru.ketty;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsHour {

    private List<String> data; //Список часов, когда была обнаружена ошибка

    private final Map<String, Long> statistics = new HashMap<>(); //Статические данные String - час, Long - кол-во ошибок за этот час

    public StatisticsHour(List<String> data) {
        this.data = data;
    }

    /**
     * Сборщик статистики
     * заполняет данными HashMap
     */
    public void gather() {
        for (String time : data) {
            Long count = statistics.getOrDefault(time, 0L);
            statistics.put(time, count + 1);
        }
    }

    /**
     * Сохрание статистики в файл
     * @param folderPath - путь к директории для выгрузки статистики
     *                   (та же папка, где находится архив)
     */
    public void store(String folderPath) {
        File file = new File( folderPath+File.separator+"Statistics.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Long> entry : statistics.entrySet()) {
                bw.append(formFileString(entry));
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Формирование строки для записи в файл статистики
     * @param entry - пара значений из Map <Час, Rол-во ошибок>
     * @return строка для записи в файл выгрузки статистики
     */
    private String formFileString(HashMap.Entry<String, Long> entry) {
        Integer hour = Integer.valueOf(entry.getKey());
        StringBuilder sb = new StringBuilder();
        sb.append("С "+entry.getKey()).append(" по ")
                .append(String.format("%02d", hour + 1))
                .append(": ")
                .append(entry.getValue())
                .append(" ошибок");
        sb.append(System.lineSeparator());

        return sb.toString();
    }

}
