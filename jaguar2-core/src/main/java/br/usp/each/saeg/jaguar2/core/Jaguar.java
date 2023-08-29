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

import br.usp.each.saeg.jaguar2.api.Heuristic;
import br.usp.each.saeg.jaguar2.api.IBundleSpectrum;
import br.usp.each.saeg.jaguar2.api.IClassSpectrum;
import br.usp.each.saeg.jaguar2.api.ILineSpectrum;
import br.usp.each.saeg.jaguar2.api.IPackageSpectrum;
import br.usp.each.saeg.jaguar2.api.SpectrumEval;
import br.usp.each.saeg.jaguar2.spi.CoverageController;
import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

public class Jaguar implements SpectrumEval {

    private final CoverageController controller;

    private final SpectrumExporter exporter;

    private final Heuristic heuristic;

    private int failedTests;

    private int passedTests;

    public Jaguar(
            final CoverageController controller,
            final SpectrumExporter exporter,
            final Heuristic heuristic) {
        this.controller = controller;
        this.exporter = exporter;
        this.heuristic = heuristic;
        failedTests = 0;
        passedTests = 0;
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
     */
    public void testStarted() {
        if (controller != null) {
            controller.reset();
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
     * @throws Exception
     */
    public void testRunFinished() throws Exception {
        exporter.init();
        if (controller != null) {
            final IBundleSpectrum bundle = controller.analyze();
            for (final IPackageSpectrum packageSpectrum : bundle.getPackages()) {
                for (final IClassSpectrum spectrum : packageSpectrum.getClasses()) {
                    exporter.write(spectrum, this);
                }
            }
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
