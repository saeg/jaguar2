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
package br.usp.each.saeg.jaguar2.junit;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.usp.each.saeg.jaguar2.core.Jaguar;

@RunWith(MockitoJUnitRunner.class)
public class JaguarJUnitRunListenerTest {

    @Mock
    private Jaguar jaguarMock;

    private JaguarJUnitRunListener listener;

    private InOrder inOrder;

    @Before
    public void setUp() {
        listener = new JaguarJUnitRunListener(jaguarMock);
        inOrder = inOrder(jaguarMock);
    }

    // ---

    @Test
    public void testSuccess() {
        // When
        successTest();

        // Then
        verifySuccessTest();
        verifyNoMoreInteractions();
    }

    @Test
    public void testFail() {
        // When
        failTest();

        // Then
        verifyFailTest();
        verifyNoMoreInteractions();
    }

    @Test
    public void testAssumptionFailure() {
        // When
        assumptionFailureTest();

        // Then
        verifyAssumptionFailure();
        verifyNoMoreInteractions();
    }

    // ---

    @Test
    public void testSuccessAndSuccess() {
        // When
        successTest();
        successTest();

        // Then
        verifySuccessTest();
        verifySuccessTest();
        verifyNoMoreInteractions();
    }

    @Test
    public void testSuccessAndFail() {
        // When
        successTest();
        failTest();

        // Then
        verifySuccessTest();
        verifyFailTest();
        verifyNoMoreInteractions();
    }

    @Test
    public void testSuccessAndAssumptionFailure() {
        // When
        successTest();
        assumptionFailureTest();

        // Then
        verifySuccessTest();
        verifyAssumptionFailure();
        verifyNoMoreInteractions();
    }

    // ---

    @Test
    public void testFailAndSuccess() {
        // When
        failTest();
        successTest();

        // Then
        verifyFailTest();
        verifySuccessTest();
        verifyNoMoreInteractions();
    }

    @Test
    public void testFailAndFail() {
        // When
        failTest();
        failTest();

        // Then
        verifyFailTest();
        verifyFailTest();
        verifyNoMoreInteractions();
    }

    @Test
    public void testFailAndAssumptionFailure() {
        // When
        failTest();
        assumptionFailureTest();

        // Then
        verifyFailTest();
        verifyAssumptionFailure();
        verifyNoMoreInteractions();
    }

    // ---

    @Test
    public void testAssumptionFailureAndSuccess() {
        // When
        assumptionFailureTest();
        successTest();

        // Then
        inOrder.verify(jaguarMock, times(2)).testStarted();
        inOrder.verify(jaguarMock).testFinished(eq(false));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testAssumptionFailureAndFail() {
        // When
        assumptionFailureTest();
        failTest();

        // Then
        inOrder.verify(jaguarMock, times(2)).testStarted();
        inOrder.verify(jaguarMock).testFinished(eq(true));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testAssumptionFailureAndAssumptionFailure() {
        // When
        assumptionFailureTest();
        assumptionFailureTest();

        // Then
        inOrder.verify(jaguarMock, times(2)).testStarted();
        inOrder.verifyNoMoreInteractions();
    }

    // ---

    private void successTest() {
        final Description desc = mock(Description.class);
        listener.testStarted(desc);
        listener.testFinished(desc);
    }

    private void failTest() {
        final Description desc = mock(Description.class);
        final Failure failure = mock(Failure.class);
        listener.testStarted(desc);
        listener.testFailure(failure);
        listener.testFinished(desc);
    }

    private void assumptionFailureTest() {
        final Description desc = mock(Description.class);
        final Failure failure = mock(Failure.class);
        listener.testStarted(desc);
        listener.testAssumptionFailure(failure);
        listener.testFinished(desc);
    }

    private void verifySuccessTest() {
        inOrder.verify(jaguarMock).testStarted();
        inOrder.verify(jaguarMock).testFinished(eq(false));
    }

    private void verifyFailTest() {
        inOrder.verify(jaguarMock).testStarted();
        inOrder.verify(jaguarMock).testFinished(eq(true));
    }

    private void verifyAssumptionFailure() {
        inOrder.verify(jaguarMock).testStarted();
    }

    private void verifyNoMoreInteractions() {
        inOrder.verifyNoMoreInteractions();
    }

}
