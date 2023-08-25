package br.usp.each.saeg.jaguar2;

import org.junit.Assert;
import org.junit.Test;

public class MaxTest {

    @Test
    public void test1() {
        final int[] array = { 1, 2, 3 };

        Assert.assertEquals(3, Max.max(array, array.length));
    }

    @Test
    public void test2() {
        final int[] array = { 5, 5, 6 };

        Assert.assertEquals(6, Max.max(array, array.length));
    }

    @Test
    public void test3() {
        final int[] array = { 2, 1, 10 };

        Assert.assertEquals(10, Max.max(array, array.length));
    }

    @Test
    public void test4() {
        final int[] array = { 4, 3, 2 };

        Assert.assertEquals(4, Max.max(array, array.length));
    }

    @Test
    public void test5() {
        final int[] array = { 4 };

        Assert.assertEquals(4, Max.max(array, array.length));
    }

}
