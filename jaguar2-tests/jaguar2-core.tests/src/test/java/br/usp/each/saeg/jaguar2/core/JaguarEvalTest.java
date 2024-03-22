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
package br.usp.each.saeg.jaguar2.core;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.usp.each.saeg.jaguar2.api.Heuristic;
import br.usp.each.saeg.jaguar2.api.ILineSpectrum;
import br.usp.each.saeg.jaguar2.spi.CoverageController;
import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

@RunWith(MockitoJUnitRunner.class)
public class JaguarEvalTest {

    @Mock
    private CoverageController controllerMock;

    @Mock
    private SpectrumExporter exporterMock;

    @Mock
    private Heuristic heuristicMock;

    private Jaguar jaguar;

    private double expectedValue;

    @Before
    public void setUp() {
        jaguar = new Jaguar(controllerMock, exporterMock, heuristicMock);
        expectedValue = Math.random();
    }

    @Test
    public void testEval_1() {
        // Given
        when(heuristicMock.eval(0, 0, 0, 1)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(false);

        // When
        final double value = jaguar.eval(new Spectrum(0, 0));

        // Then
        verify(heuristicMock, times(1)).eval(0, 0, 0, 1);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_2() {
        // Given
        when(heuristicMock.eval(0, 0, 1, 0)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(false);

        // When
        final double value = jaguar.eval(new Spectrum(0, 1));

        // Then
        verify(heuristicMock, times(1)).eval(0, 0, 1, 0);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_3() {
        // Given
        when(heuristicMock.eval(0, 1, 0, 0)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(true);

        // When
        final double value = jaguar.eval(new Spectrum(0, 0));

        // Then
        verify(heuristicMock, times(1)).eval(0, 1, 0, 0);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_4() {
        // Given
        when(heuristicMock.eval(1, 0, 0, 0)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(true);

        // When
        final double value = jaguar.eval(new Spectrum(1, 0));

        // Then
        verify(heuristicMock, times(1)).eval(1, 0, 0, 0);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_5() {
        // Given
        when(heuristicMock.eval(0, 0, 0, 2)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(false);
        jaguar.testFinished(false);

        // When
        final double value = jaguar.eval(new Spectrum(0, 0));

        // Then
        verify(heuristicMock, times(1)).eval(0, 0, 0, 2);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_6() {
        // Given
        when(heuristicMock.eval(0, 0, 1, 1)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(false);
        jaguar.testFinished(false);

        // When
        final double value = jaguar.eval(new Spectrum(0, 1));

        // Then
        verify(heuristicMock, times(1)).eval(0, 0, 1, 1);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_7() {
        // Given
        when(heuristicMock.eval(0, 0, 2, 0)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(false);
        jaguar.testFinished(false);

        // When
        final double value = jaguar.eval(new Spectrum(0, 2));

        // Then
        verify(heuristicMock, times(1)).eval(0, 0, 2, 0);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_8() {
        // Given
        when(heuristicMock.eval(0, 2, 0, 0)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(true);
        jaguar.testFinished(true);

        // When
        final double value = jaguar.eval(new Spectrum(0, 0));

        // Then
        verify(heuristicMock, times(1)).eval(0, 2, 0, 0);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_9() {
        // Given
        when(heuristicMock.eval(1, 1, 0, 0)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(true);
        jaguar.testFinished(true);

        // When
        final double value = jaguar.eval(new Spectrum(1, 0));

        // Then
        verify(heuristicMock, times(1)).eval(1, 1, 0, 0);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_10() {
        // Given
        when(heuristicMock.eval(2, 0, 0, 0)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(true);
        jaguar.testFinished(true);

        // When
        final double value = jaguar.eval(new Spectrum(2, 0));

        // Then
        verify(heuristicMock, times(1)).eval(2, 0, 0, 0);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_11() {
        // Given
        when(heuristicMock.eval(0, 1, 0, 1)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(false);
        jaguar.testFinished(true);

        // When
        final double value = jaguar.eval(new Spectrum(0, 0));

        // Then
        verify(heuristicMock, times(1)).eval(0, 1, 0, 1);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_12() {
        // Given
        when(heuristicMock.eval(1, 0, 0, 1)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(false);
        jaguar.testFinished(true);

        // When
        final double value = jaguar.eval(new Spectrum(1, 0));

        // Then
        verify(heuristicMock, times(1)).eval(1, 0, 0, 1);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval_13() {
        // Given
        when(heuristicMock.eval(0, 1, 1, 0)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(false);
        jaguar.testFinished(true);

        // When
        final double value = jaguar.eval(new Spectrum(0, 1));

        // Then
        verify(heuristicMock, times(1)).eval(0, 1, 1, 0);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    @Test
    public void testEval1_14() {
        // Given
        when(heuristicMock.eval(1, 0, 1, 0)).thenReturn(expectedValue);

        // And
        jaguar.testFinished(false);
        jaguar.testFinished(true);

        // When
        final double value = jaguar.eval(new Spectrum(1, 1));

        // Then
        verify(heuristicMock, times(1)).eval(1, 0, 1, 0);

        // And
        Assert.assertEquals(expectedValue, value, 0);
    }

    private static class Spectrum implements ILineSpectrum {

        private final int cef;

        private final int cep;

        public Spectrum(final int cef, final int cep) {
            this.cef = cef;
            this.cep = cep;
        }

        @Override
        public int getCef() {
            return cef;
        }

        @Override
        public int getCep() {
            return cep;
        }

    }

}
