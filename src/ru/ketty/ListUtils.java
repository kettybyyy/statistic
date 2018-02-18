package ru.ketty;
import java.util.ArrayList;
import java.util.List;

/**
 * Помощник для работы со списками
 */

public class ListUtils {
    /**
     * Разбиение списка на подсписки.
     * Последняя часть всегда длиннее, если список не делится на равные части
     * @param list список для составления подсписков
     * @param partsNumber количество подсписков на которые надо разделить список
     * @param <T> тип элементов содержащихся в списке
     * @return список подсписков
     */
    public static <T> List<List<T>> choppedOnParts(List<T> list, final int partsNumber) {
        List<List<T>> parts = new ArrayList<List<T>>();
        if (list.size() <= partsNumber) {
            parts.add(list);
            return parts;
        }

        final int N = list.size();
        int numberOfItemsInFullPart = N / partsNumber;
        int currentIndex = 0;
        int nextIndex = 0;
        for (int i = 0; i < partsNumber; i++) {
            //Если создаем последнюю часть, то тогда последний индекс будет равен длине исходного списка
            //Т.о. мы в последнюю часть добавим все что не влезло в равные части
            nextIndex = (i == partsNumber - 1) ? N : currentIndex + numberOfItemsInFullPart;
            parts.add(new ArrayList<T>(
                    list.subList(currentIndex, nextIndex))
            );

            currentIndex += numberOfItemsInFullPart;
        }
        return parts;
    }
}
