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

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
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
    public void testSuccess() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                successTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                verifySuccessTest();
            }

        });
    }

    @Test
    public void testFail() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                failTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                verifyFailTest();
            }

        });
    }

    @Test
    public void testAssumptionFailure() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                assumptionFailureTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                verifyAssumptionFailure();
            }

        });
    }

    // ---

    @Test
    public void testSuccessAndSuccess() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                successTest();
                successTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                verifySuccessTest();
                verifySuccessTest();
            }

        });
    }

    @Test
    public void testSuccessAndFail() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                successTest();
                failTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                verifySuccessTest();
                verifyFailTest();
            }

        });
    }

    @Test
    public void testSuccessAndAssumptionFailure() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                successTest();
                assumptionFailureTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                verifySuccessTest();
                verifyAssumptionFailure();
            }

        });
    }

    // ---

    @Test
    public void testFailAndSuccess() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                failTest();
                successTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                verifyFailTest();
                verifySuccessTest();
            }

        });
    }

    @Test
    public void testFailAndFail() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                failTest();
                failTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                verifyFailTest();
                verifyFailTest();
            }

        });
    }

    @Test
    public void testFailAndAssumptionFailure() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                failTest();
                assumptionFailureTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                verifyFailTest();
                verifyAssumptionFailure();
            }

        });
    }

    // ---

    @Test
    public void testAssumptionFailureAndSuccess() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                assumptionFailureTest();
                successTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                inOrder.verify(jaguarMock, times(2)).testStarted();
                inOrder.verify(jaguarMock).testFinished(eq(false));
            }

        });
    }

    @Test
    public void testAssumptionFailureAndFail() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                assumptionFailureTest();
                failTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                inOrder.verify(jaguarMock, times(2)).testStarted();
                inOrder.verify(jaguarMock).testFinished(eq(true));
            }

        });
    }

    @Test
    public void testAssumptionFailureAndAssumptionFailure() throws IOException {
        // When
        runTests(new Runnable() {

            @Override
            public void run() {
                assumptionFailureTest();
                assumptionFailureTest();
            }

        });

        // Then
        verifyTests(new Runnable() {

            @Override
            public void run() {
                inOrder.verify(jaguarMock, times(2)).testStarted();
            }

        });
    }

    // ---

    private void runTests(final Runnable runnable) throws IOException {
        listener.testRunStarted(mock(Description.class));
        runnable.run();
        listener.testRunFinished(mock(Result.class));
    }

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

    private void verifyTests(final Runnable runnable) throws IOException {
        inOrder.verify(jaguarMock).testRunStarted();
        runnable.run();
        inOrder.verify(jaguarMock).testRunFinished();
        inOrder.verifyNoMoreInteractions();
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

}
