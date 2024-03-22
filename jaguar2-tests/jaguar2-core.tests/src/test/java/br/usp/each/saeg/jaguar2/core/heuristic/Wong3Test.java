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
package br.usp.each.saeg.jaguar2.core.heuristic;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class Wong3Test {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { 0, 0, 0, 0,  0.000 },

            { 1, 0, 0, 0,  1.000 },
            { 0, 1, 0, 0,  0.000 },
            { 0, 0, 1, 0, -1.000 },
            { 0, 0, 0, 1,  0.000 },

            { 1, 1, 0, 0,  1.000 },
            { 1, 0, 1, 0,  0.000 },
            { 1, 0, 0, 1,  1.000 },
            { 0, 1, 1, 0, -1.000 },
            { 0, 1, 0, 1,  0.000 },
            { 0, 0, 1, 1, -1.000 },

            { 1, 1, 1, 0,  0.000 },
            { 1, 1, 0, 1,  1.000 },
            { 1, 0, 1, 1,  0.000 },
            { 0, 1, 1, 1, -1.000 },

            { 1, 1, 1, 1,  0.000 },

            {   5,  1,    0,   1,   5.000 },
            {   1,  3,    5,   1,  -1.300 },
            {   2,  1,    3,   2,  -0.100 },
            {  10,  5,    1,   2,   9.000 },
            {   1,  3,   10,   4,  -1.800 },
            {  10,  1,    0,   1,  10.000 },
            {  10,  1,    2,   1,   8.000 },
            {  10,  0,    2,   0,   8.000 },
            {  10,  0,    3,   0,   7.900 },
            {  10,  0,   10,   0,   7.200 },
            {  10,  0,   11,   0,   7.199 },
            {   0, 10,   10,   5,  -2.800 },
            {  10,  5, 1000,  15,   6.210 },
            { 100, 15,   10, 500,  97.200 },
            {  12, 30,   20, 100,   9.190 },
        });
    }

    private final int cef;
    private final int cnf;
    private final int cep;
    private final int cnp;
    private final double expected;

    public Wong3Test(final int cef, final int cnf, final int cep,
            final int cnp, final double expected) {

        this.cef = cef;
        this.cnf = cnf;
        this.cep = cep;
        this.cnp = cnp;

        this.expected = expected;
    }

    @Test
    public void test() {
        Assert.assertEquals(expected, new Wong3().eval(cef, cnf, cep, cnp), 0.001);
    }

}
