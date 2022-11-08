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

import java.io.IOException;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import br.usp.each.saeg.jaguar2.core.Jaguar;

public class JaguarJUnitRunListener extends RunListener {

    private final Jaguar jaguar;

    private boolean fail;

    private boolean skip;

    public JaguarJUnitRunListener(final Jaguar jaguar) {
        this.jaguar = jaguar;
    }

    public JaguarJUnitRunListener() {
        this(new Jaguar());
    }

    @Override
    public void testRunStarted(final Description description) {
        jaguar.testRunStarted();
    }

    @Override
    public void testStarted(final Description description) {
        fail = false;
        skip = false;
        jaguar.testStarted();
    }

    @Override
    public void testFinished(final Description description) {
        if (!skip) {
            jaguar.testFinished(fail);
        }
    }

    @Override
    public void testFailure(final Failure failure) {
        fail = true;
    }

    @Override
    public void testAssumptionFailure(final Failure failure) {
        skip = true;
    }

    @Override
    public void testRunFinished(final Result result) throws IOException {
        jaguar.testRunFinished();
    }

}
