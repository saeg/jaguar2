package br.usp.each.saeg.jaguar2;

public class Max {

    public static int max(final int[] array, final int length) {
        int i = 0;
        int max = array[++i];
        while (i < length) {
            if (array[i] > max) {
                max = array[i];
            }
            i++;
        }
        return max;
    }

}
