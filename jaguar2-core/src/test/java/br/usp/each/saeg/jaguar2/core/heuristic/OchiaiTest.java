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
public class OchiaiTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { 0, 0, 0, 0,  0.000 },

            { 1, 0, 0, 0,  1.000 },
            { 0, 1, 0, 0,  0.000 },
            { 0, 0, 1, 0,  0.000 },
            { 0, 0, 0, 1,  0.000 },

            { 1, 1, 0, 0,  0.707 },
            { 1, 0, 1, 0,  0.707 },
            { 1, 0, 0, 1,  1.000 },
            { 0, 1, 1, 0,  0.000 },
            { 0, 1, 0, 1,  0.000 },
            { 0, 0, 1, 1,  0.000 },

            { 1, 1, 1, 0,  0.500 },
            { 1, 1, 0, 1,  0.707 },
            { 1, 0, 1, 1,  0.707 },
            { 0, 1, 1, 1,  0.000 },

            { 1, 1, 1, 1,  0.500 },

            {   5,  1,    0,   1,   0.912 },
            {   1,  3,    5,   1,   0.204 },
            {   2,  1,    3,   2,   0.516 },
            {  10,  5,    1,   2,   0.778 },
            {   1,  3,   10,   4,   0.150 },
            {  10,  1,    0,   1,   0.953 },
            {  10,  1,    2,   1,   0.870 },
            {  10,  0,    2,   0,   0.912 },
            {  10,  0,    3,   0,   0.877 },
            {  10,  0,   10,   0,   0.707 },
            {  10,  0,   11,   0,   0.690 },
            {   0, 10,   10,   5,   0.000 },
            {  10,  5, 1000,  15,   0.081 },
            { 100, 15,   10, 500,   0.889 },
            {  12, 30,   20, 100,   0.327 },
        });
    }

    private final int cef;
    private final int cnf;
    private final int cep;
    private final int cnp;
    private final double expected;

    public OchiaiTest(final int cef, final int cnf, final int cep,
            final int cnp, final double expected) {

        this.cef = cef;
        this.cnf = cnf;
        this.cep = cep;
        this.cnp = cnp;

        this.expected = expected;
    }

    @Test
    public void test() {
        Assert.assertEquals(expected, new Ochiai().eval(cef, cnf, cep, cnp), 0.001);
    }

}
