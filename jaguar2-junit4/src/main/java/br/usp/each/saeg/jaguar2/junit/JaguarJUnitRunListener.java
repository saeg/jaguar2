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

import br.usp.each.saeg.jaguar2.CoverageControllerLoader;
import br.usp.each.saeg.jaguar2.SpectrumExporterLoader;
import br.usp.each.saeg.jaguar2.api.Heuristic;
import br.usp.each.saeg.jaguar2.core.Jaguar;
import br.usp.each.saeg.jaguar2.core.heuristic.Ochiai;
import br.usp.each.saeg.jaguar2.spi.CoverageController;
import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

/**
 * A JUnit 4 {@link RunListener} class implementation that interacts
 * with Jaguar. Most of the {@link RunListener} methods calls are
 * delegated to a {@link Jaguar} class instance. Tests with failed
 * assumptions are ignored. We keep a state if test failed/succeed so
 * Jaguar will got this information when test finishes.
 */
public class JaguarJUnitRunListener extends RunListener {

    /**
     * Name of the property that allows enable no dump feature.
     */
    public static final String JAGUAR2_NO_DUMP = "jaguar2.noDump";

    private final Jaguar jaguar;

    private boolean fail;

    private boolean skip;

    /**
     * Instantiate a {@link JaguarJUnitRunListener} for a given instance
     * of the {@link Jaguar} class.
     *
     * @param jaguar a instance of the {@link Jaguar} class.
     */
    public JaguarJUnitRunListener(final Jaguar jaguar) {
        this.jaguar = jaguar;
    }

    /**
     * Instantiate a {@link JaguarJUnitRunListener} for a new instance of
     * the {@link Jaguar} class.
     *
     * The {@link Jaguar} instance will use a {@link CoverageController}
     * and a {@link SpectrumExporter} loaded by the service loading
     * facilities {@link CoverageControllerLoader} and
     * {@link SpectrumExporterLoader}, respectively.
     *
     * The heuristic is always {@link Ochiai}, but probably we will allow
     * dynamic load different {@link Heuristic} implementations in the
     * future.
     *
     * This method is mainly used by Maven Surefire Plugin for attaching
     * the listener to the tests. Note that it requires that the listener
     * have a no-args constructor.
     *
     * @see <a href=
     *      "https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit.html#using-custom-listeners-and-reporters">Using
     *      Custom Listeners and Reporters</a>
     */
    public JaguarJUnitRunListener() {
        this(new Jaguar(
                new CoverageControllerLoader().load(),
                new SpectrumExporterLoader().load(),
                new Ochiai(),
                Boolean.getBoolean(JAGUAR2_NO_DUMP)));
    }

    @Override
    public void testRunStarted(final Description description) {
        jaguar.testRunStarted();
    }

    @Override
    public void testStarted(final Description description) throws IOException {
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
    public void testRunFinished(final Result result) throws Exception {
        jaguar.testRunFinished();
    }

}
