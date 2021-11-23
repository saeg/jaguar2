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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.usp.each.saeg.jaguar2.spi.CoverageController;

@RunWith(MockitoJUnitRunner.class)
public class JaguarTest {

    @Mock
    private CoverageController controllerMock;

    private Jaguar jaguar;

    @Before
    public void setUp() {
        jaguar = new Jaguar(controllerMock);
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
    public void testRunFinishedCallControllerAnalyze() {
        // When
        jaguar.testRunFinished();

        // Then
        verify(controllerMock, times(1)).analyze();
    }

}
