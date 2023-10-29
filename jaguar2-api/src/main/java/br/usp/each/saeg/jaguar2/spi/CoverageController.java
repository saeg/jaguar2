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
package br.usp.each.saeg.jaguar2.spi;

import java.io.IOException;

import br.usp.each.saeg.jaguar2.api.IBundleSpectrum;

/**
 * A service that controls code coverage data.
 */
public interface CoverageController {

    /**
     * Initialize the coverage controller service.
     */
    void init();

    /**
     * Reset runtime code coverage data.
     */
    void reset();

    /**
     * Dump runtime code coverage data.
     *
     * @param reset a flag indicating that code coverage data should
     *              also be reset.
     *
     * @throws IOException in case of exceptions during dump.
     */
    void dump(boolean reset) throws IOException;

    /**
     * Save runtime code coverage data for further analysis. The data is
     * flagged when executed by a failing test case.
     *
     * @param testFailed a flag indicating that code coverage data are
     *                   executed by a failing test case.
     */
    void save(boolean testFailed);

    /**
     * Analyze the saved runtime code coverage data.
     *
     * @return a {@link IBundleSpectrum} of the analyzed runtime code
     *         coverage data.
     */
    IBundleSpectrum analyze();

    /**
     * Destroy the current state for the controller.
     *
     * The same instance can be used for new analysis.
     */
    void destroy();

}
