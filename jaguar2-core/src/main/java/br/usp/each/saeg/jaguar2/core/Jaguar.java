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

import java.io.IOException;

import br.usp.each.saeg.jaguar2.api.Heuristic;
import br.usp.each.saeg.jaguar2.api.IBundleSpectrum;
import br.usp.each.saeg.jaguar2.api.ILineSpectrum;
import br.usp.each.saeg.jaguar2.api.SpectrumEval;
import br.usp.each.saeg.jaguar2.spi.CoverageController;
import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

/**
 * This class is the heart of the Jaguar execution.
 *
 * An instance of this class is notified of events of the underlying
 * test framework by method calls {@link Jaguar#testRunStarted()},
 * {@link Jaguar#testStarted()}, {@link Jaguar#testRunFinished()} and
 * {@link Jaguar#testRunFinished()}.
 *
 * By those method calls, the instance will interact with the
 * underlying code coverage provider and also count how many tests
 * succeed/passed.
 *
 * The interaction with the code coverage provider is made by the
 * class {@link CoverageController}. When a test is about to start the
 * current code coverage is dropped/reseted, and when the test
 * finishes the code coverage is saved together with a flag indicating
 * if the test passed or failed.
 *
 * Finally, when the test run finishes, the {@link CoverageController}
 * builds a {@link IBundleSpectrum} that groups each individual line
 * spectrum ({@link ILineSpectrum} and its CEF (count (C) of tests
 * that executed (E) the spectrum and the test failed (F)) and CEP
 * (count (C) of tests that executed (E) the spectrum and the test
 * passed (P)).
 *
 * Each spectrum is evaluated by {@link SpectrumEval} with a given
 * {@link Heuristic} and all the {@link IBundleSpectrum} is
 * written/exported with an implementation of a
 * {@link SpectrumExporter}.
 */
public class Jaguar implements SpectrumEval {

    private final CoverageController controller;

    private final SpectrumExporter exporter;

    private final Heuristic heuristic;

    private final boolean noDump;

    private int failedTests;

    private int passedTests;

    /**
     * Instantiate a {@link Jaguar}.
     *
     * @param controller a {@link CoverageController}.
     * @param exporter   a {@link SpectrumExporter}.
     * @param heuristic  a {@link Heuristic}.
     * @param noDump     enable not dump and only reset the coverage.
     */
    public Jaguar(
            final CoverageController controller,
            final SpectrumExporter exporter,
            final Heuristic heuristic,
            final boolean noDump) {
        this.controller = controller;
        this.exporter = exporter;
        this.heuristic = heuristic;
        this.noDump = noDump;
        failedTests = 0;
        passedTests = 0;
    }

    /**
     * Instantiate a {@link Jaguar}.
     *
     * @param controller a {@link CoverageController}.
     * @param exporter   a {@link SpectrumExporter}.
     * @param heuristic  a {@link Heuristic}.
     */
    public Jaguar(
            final CoverageController controller,
            final SpectrumExporter exporter,
            final Heuristic heuristic) {
        this(controller, exporter, heuristic, false);
    }

    /**
     * Called before any tests have been run.
     */
    public void testRunStarted() {
        if (controller != null) {
            controller.init();
        }
    }

    /**
     * Called when an atomic test is about to be started.
     *
     * The current implementation reset runtime code coverage data as no
     * code executed so far is related to current test.
     *
     * @throws IOException in case of exceptions during dump.
     */
    public void testStarted() throws IOException {
        if (controller != null) {
            if (noDump) {
                controller.reset();
            } else {
                controller.dump(true);
            }
        }
    }

    /**
     * Called when an atomic test has finished, whether the test succeeds
     * or fails.
     *
     * The current implementation save runtime code coverage data for
     * further analysis. The data is flagged when executed by a failing
     * test case.
     *
     * @param testFailed a flag indicating that test fails.
     */
    public void testFinished(final boolean testFailed) {
        if (controller != null) {
            controller.save(testFailed);
        }
        if (testFailed) {
            failedTests++;
        } else {
            passedTests++;
        }
    }

    /**
     * Called when all tests have finished.
     *
     * @throws Exception in case of exceptions during export.
     */
    public void testRunFinished() throws Exception {
        exporter.init();
        if (controller != null) {
            exporter.write(controller.analyze(), this);
        }
        exporter.shutdown();
    }

    @Override
    public int getFailedTests() {
        return failedTests;
    }

    @Override
    public int getPassedTests() {
        return passedTests;
    }

    @Override
    public int getCef(final ILineSpectrum spectrum) {
        return spectrum.getCef();
    }

    @Override
    public int getCnf(final ILineSpectrum spectrum) {
        return failedTests - spectrum.getCef();
    }

    @Override
    public int getCep(final ILineSpectrum spectrum) {
        return spectrum.getCep();
    }

    @Override
    public int getCnp(final ILineSpectrum spectrum) {
        return passedTests - spectrum.getCep();
    }

    @Override
    public double eval(final ILineSpectrum spectrum) {
        return heuristic.eval(
                getCef(spectrum), getCnf(spectrum),
                getCep(spectrum), getCnp(spectrum));
    }

}
