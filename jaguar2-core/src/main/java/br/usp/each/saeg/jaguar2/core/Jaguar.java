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

import br.usp.each.saeg.jaguar2.CoverageControllerLoader;
import br.usp.each.saeg.jaguar2.spi.CoverageController;

public class Jaguar {

    private final CoverageController controller;

    public Jaguar(final CoverageController controller) {
        this.controller = controller;
    }

    public Jaguar() {
        this(new CoverageControllerLoader().load());
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
    }

    /**
     * Called when all tests have finished.
     */
    public void testRunFinished() {
        if (controller != null) {
            controller.analyze();
        }
    }

}
