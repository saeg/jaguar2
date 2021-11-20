/**
 * Copyright (c) 2021, 2021 University of Sao Paulo and Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Roberto Araujo - initial API and implementation and/or initial documentation
 */
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
