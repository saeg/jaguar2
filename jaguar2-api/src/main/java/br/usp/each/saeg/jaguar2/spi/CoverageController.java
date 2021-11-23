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
     * Save runtime code coverage data for further analysis. The data is
     * flagged when executed by a failing test case.
     *
     * @param testFailed a flag indicating that code coverage data are
     *                   executed by a failing test case.
     */
    void save(final boolean testFailed);

    /**
     * Analyze the saved runtime code coverage data.
     */
    void analyze();

}
