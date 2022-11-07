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

import java.util.Collection;

import br.usp.each.saeg.jaguar2.CoverageControllerLoader;
import br.usp.each.saeg.jaguar2.SpectrumExporterLoader;
import br.usp.each.saeg.jaguar2.api.Heuristic;
import br.usp.each.saeg.jaguar2.api.IClassSpectrum;
import br.usp.each.saeg.jaguar2.api.ILineSpectrum;
import br.usp.each.saeg.jaguar2.api.ISpectrumVisitor;
import br.usp.each.saeg.jaguar2.api.SpectrumEval;
import br.usp.each.saeg.jaguar2.core.heuristic.Ochiai;
import br.usp.each.saeg.jaguar2.spi.CoverageController;
import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

public class Jaguar implements SpectrumEval {

    private final CoverageController controller;

    private final Heuristic heuristic;

    private int failedTests;

    private int passedTests;

    public Jaguar(final CoverageController controller, final Heuristic heuristic) {
        this.controller = controller;
        this.heuristic = heuristic;
        failedTests = 0;
        passedTests = 0;
    }

    public Jaguar() {
        this(new CoverageControllerLoader().load(), new Ochiai());
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
     */
    public void testRunFinished() {
        final Collection<SpectrumExporter> exporters = new SpectrumExporterLoader().load();
        for (final SpectrumExporter exporter : exporters) {
            exporter.init();
        }
        if (controller != null) {
            controller.analyze(new ISpectrumVisitor() {
                @Override
                public void visitSpectrum(final IClassSpectrum spectrum) {
                    for (final SpectrumExporter exporter : exporters) {
                        exporter.write(spectrum, Jaguar.this);
                    }
                }
            });
        }
        for (final SpectrumExporter exporter : exporters) {
            exporter.shutdown();
        }
    }

    public int getFailedTests() {
        return failedTests;
    }

    public int getPassedTests() {
        return passedTests;
    }

    @Override
    public double eval(final ILineSpectrum spectrum) {
        return heuristic.eval(
                spectrum.getCef(), failedTests - spectrum.getCef(),
                spectrum.getCep(), passedTests - spectrum.getCep());
    }

}
