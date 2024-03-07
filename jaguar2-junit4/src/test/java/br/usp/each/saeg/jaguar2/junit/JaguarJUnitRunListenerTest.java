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

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
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
import org.mockito.junit.MockitoJUnitRunner;

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
    public void testSuccess() throws Exception {
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
    public void testFail() throws Exception {
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
    public void testAssumptionFailure() throws Exception {
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
    public void testSuccessAndSuccess() throws Exception {
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
    public void testSuccessAndFail() throws Exception {
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
    public void testSuccessAndAssumptionFailure() throws Exception {
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
    public void testFailAndSuccess() throws Exception {
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
    public void testFailAndFail() throws Exception {
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
    public void testFailAndAssumptionFailure() throws Exception {
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
    public void testAssumptionFailureAndSuccess() throws Exception {
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
                try {
                    inOrder.verify(jaguarMock, times(2)).testStarted();
                    inOrder.verify(jaguarMock).testFinished(eq(false));
                } catch (final IOException e) {
                    fail();
                }
            }

        });
    }

    @Test
    public void testAssumptionFailureAndFail() throws Exception {
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
                try {
                    inOrder.verify(jaguarMock, times(2)).testStarted();
                    inOrder.verify(jaguarMock).testFinished(eq(true));
                } catch (final IOException e) {
                    fail();
                }
            }

        });
    }

    @Test
    public void testAssumptionFailureAndAssumptionFailure() throws Exception {
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
                try {
                    inOrder.verify(jaguarMock, times(2)).testStarted();
                } catch (final IOException e) {
                    fail();
                }
            }

        });
    }

    // ---

    private void runTests(final Runnable runnable) throws Exception {
        listener.testRunStarted(mock(Description.class));
        runnable.run();
        listener.testRunFinished(mock(Result.class));
    }

    private void successTest() {
        try {
            final Description desc = mock(Description.class);
            listener.testStarted(desc);
            listener.testFinished(desc);
        } catch (final IOException e) {
            fail();
        }
    }

    private void failTest() {
        try {
            final Description desc = mock(Description.class);
            final Failure failure = mock(Failure.class);
            listener.testStarted(desc);
            listener.testFailure(failure);
            listener.testFinished(desc);
        } catch (final IOException e) {
            fail();
        }
    }

    private void assumptionFailureTest() {
        try {
            final Description desc = mock(Description.class);
            final Failure failure = mock(Failure.class);
            listener.testStarted(desc);
            listener.testAssumptionFailure(failure);
            listener.testFinished(desc);
        } catch (final IOException e) {
            fail();
        }
    }

    private void verifyTests(final Runnable runnable) throws Exception {
        inOrder.verify(jaguarMock).testRunStarted();
        runnable.run();
        inOrder.verify(jaguarMock).testRunFinished();
        inOrder.verifyNoMoreInteractions();
    }

    private void verifySuccessTest() {
        try {
            inOrder.verify(jaguarMock).testStarted();
            inOrder.verify(jaguarMock).testFinished(eq(false));
        } catch (final IOException e) {
            fail();
        }
    }

    private void verifyFailTest() {
        try {
            inOrder.verify(jaguarMock).testStarted();
            inOrder.verify(jaguarMock).testFinished(eq(true));
        } catch (final IOException e) {
            fail();
        }
    }

    private void verifyAssumptionFailure() {
        try {
            inOrder.verify(jaguarMock).testStarted();
        } catch (final IOException e) {
            fail();
        }
    }

}
