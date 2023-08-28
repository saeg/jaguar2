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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.usp.each.saeg.jaguar2.api.Heuristic;
import br.usp.each.saeg.jaguar2.api.IClassSpectrum;
import br.usp.each.saeg.jaguar2.spi.CoverageController;
import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

@RunWith(MockitoJUnitRunner.class)
public class JaguarTest {

    @Mock
    private CoverageController controllerMock;

    @Mock
    private SpectrumExporter exporterMock;

    @Mock
    private Heuristic heuristicMock;

    private Jaguar jaguar;

    @Before
    public void setUp() {
        jaguar = new Jaguar(controllerMock, exporterMock, heuristicMock);
    }

    @Test
    public void testRunStartedCallControllerInit() {
        // When
        jaguar.testRunStarted();

        // Then
        verify(controllerMock, times(1)).init();
    }

    @Test
    public void testStartedCallControllerReset() {
        // When
        jaguar.testStarted();

        // Then
        verify(controllerMock, times(1)).reset();
    }

    @Test
    public void testFinishSuccessCallControllerResetWithFalse() {
        // When
        jaguar.testFinished(false);

        // Then
        verify(controllerMock, times(1)).save(false);
    }

    @Test
    public void testFinishFailCallControllerResetWithTrue() {
        // When
        jaguar.testFinished(true);

        // Then
        verify(controllerMock, times(1)).save(true);
    }

    @Test
    public void testRunFinishedCallControllerAnalyze() throws Exception {
        // When
        jaguar.testRunFinished();

        // Then
        verify(controllerMock, times(1)).analyze();
    }

    @Test
    public void testRunFinishedCallExporterInit() throws Exception {
        // When
        jaguar.testRunFinished();

        // Then
        verify(exporterMock, times(1)).init();
    }

    @Test
    public void testRunFinishedCallExporterShutdown() throws Exception {
        // When
        jaguar.testRunFinished();

        // Then
        verify(exporterMock, times(1)).shutdown();
    }

    @Test
    public void testRunFinishedCallExporterWrite0() throws Exception {
        // Given
        doReturn(Collections.emptyList()).when(controllerMock).analyze();

        // When
        jaguar.testRunFinished();

        // Then
        verify(exporterMock, times(0)).write(any(IClassSpectrum.class), same(jaguar));
    }

    @Test
    public void testRunFinishedCallExporterWrite1() throws Exception {
        // Given
        final IClassSpectrum spectrum = mock(IClassSpectrum.class);
        doReturn(Collections.singleton(spectrum)).when(controllerMock).analyze();

        // When
        jaguar.testRunFinished();

        // Then
        verify(exporterMock, times(1)).write(spectrum, jaguar);
    }

    @Test
    public void testRunFinishedCallExporterWrite2() throws Exception {
        // Given
        final IClassSpectrum spectrum1 = mock(IClassSpectrum.class);
        final IClassSpectrum spectrum2 = mock(IClassSpectrum.class);
        doReturn(Arrays.asList(spectrum1, spectrum2)).when(controllerMock).analyze();

        // When
        jaguar.testRunFinished();

        // Then
        verify(exporterMock, times(1)).write(spectrum1, jaguar);
        verify(exporterMock, times(1)).write(spectrum2, jaguar);
    }

    @Test
    public void testFailedPassedCounterInitialState() {
        Assert.assertEquals(0, jaguar.getFailedTests());
        Assert.assertEquals(0, jaguar.getPassedTests());
    }

    @Test
    public void testFinishSuccessIncrementsPassedTests() {
        // When
        jaguar.testFinished(false);

        // Then
        Assert.assertEquals(0, jaguar.getFailedTests());
        Assert.assertEquals(1, jaguar.getPassedTests());
    }

    @Test
    public void testFinishFailIncrementsFailedTests() {
        // When
        jaguar.testFinished(true);

        // Then
        Assert.assertEquals(1, jaguar.getFailedTests());
        Assert.assertEquals(0, jaguar.getPassedTests());
    }

    @Test
    public void testFinishSuccessSuccessIncrementsPassedTestsTwoTimes() {
        // When
        jaguar.testFinished(false);
        jaguar.testFinished(false);

        // Then
        Assert.assertEquals(0, jaguar.getFailedTests());
        Assert.assertEquals(2, jaguar.getPassedTests());
    }

    @Test
    public void testFinishFailFailIncrementsFailedTestsTwoTimes() {
        // When
        jaguar.testFinished(true);
        jaguar.testFinished(true);

        // Then
        Assert.assertEquals(2, jaguar.getFailedTests());
        Assert.assertEquals(0, jaguar.getPassedTests());
    }

    @Test
    public void testFinishSuccessFailIncrementsCountersCorrectly() {
        // When
        jaguar.testFinished(false);
        jaguar.testFinished(true);

        // Then
        Assert.assertEquals(1, jaguar.getFailedTests());
        Assert.assertEquals(1, jaguar.getPassedTests());
    }

    @Test
    public void testFinishFailSuccessIncrementsCountersCorrectly() {
        // When
        jaguar.testFinished(true);
        jaguar.testFinished(false);

        // Then
        Assert.assertEquals(1, jaguar.getFailedTests());
        Assert.assertEquals(1, jaguar.getPassedTests());
    }

}
