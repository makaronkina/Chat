package externalHomework;

import java.util.Arrays;


/**
 * Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
 * Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов,
 * идущих после последней четверки. Входной массив должен содержать хотя бы одну четверку,
 * иначе в методе необходимо выбросить RuntimeException. Написать набор тестов для этого метода
 * (по 3-4 варианта входных данных). Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
 */

public class DemoAray {
    public DemoAray() {
    }

    public static int[] separateArray(int[] values) {

        if (values == null || values.length == 0) {
            throw new RuntimeException("Array cannot be empty!");
        }
        int index = -1;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == 4) {
                index = i + 1;
            }
        }

        if (index == -1) {
            throw new RuntimeException("There was no four found!");
        }

        int[] copies = new int[values.length - index];
        System.arraycopy(values, index, copies, 0, copies.length);
        return copies;
    }

    /**
     * Написать метод, который проверяет состав массива из чисел 1 и 4. Если в нем нет хоть одной четверки или единицы,
     * то метод вернет false; Написать набор тестов для этого метода (по 3-4 варианта входных данных).
     */

    public static boolean checkArray(Integer[] array) {
        if (array == null || array.length == 0) {
            throw new RuntimeException("Array cannot be empty!");
        }
        if (!Arrays.asList(array).contains(1) || !Arrays.asList(array).contains(4)) {
            return false;
        }
        return true;

    }


}
