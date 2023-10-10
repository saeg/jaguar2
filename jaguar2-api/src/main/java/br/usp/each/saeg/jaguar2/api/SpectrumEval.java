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
package br.usp.each.saeg.jaguar2.api;

/**
 * Evaluate individual {@link ILineSpectrum} for a given heuristic and
 * get counts of failed/passed tests.
 */
public interface SpectrumEval {

    /**
     * Get the number of failed tests.
     *
     * @return the number of failed tests.
     */
    int getFailedTests();

    /**
     * Get the number of passed tests.
     *
     * @return the number of passed tests.
     */
    int getPassedTests();

    /**
     * Get the CEF counter of a {@link ILineSpectrum} (count (C) of tests
     * that executed (E) this line spectrum and the test failed (F)).
     *
     * @param spectrum a {@link ILineSpectrum}.
     * @return the CEF counter of a {@link ILineSpectrum}.
     */
    int getCef(ILineSpectrum spectrum);

    /**
     * Get the CNF counter of a {@link ILineSpectrum} (count (C) of tests
     * that NOT (N) executed this line spectrum and the test failed (F)).
     *
     * @param spectrum a {@link ILineSpectrum}.
     * @return the CNF counter of a {@link ILineSpectrum}.
     */
    int getCnf(ILineSpectrum spectrum);

    /**
     * Get the CEP counter of a {@link ILineSpectrum} (count (C) of tests
     * that executed (E) this line spectrum and the test passed (P)).
     *
     * @param spectrum a {@link ILineSpectrum}.
     * @return the CEP counter of a {@link ILineSpectrum}.
     */
    int getCep(ILineSpectrum spectrum);

    /**
     * Get the CNP counter of a {@link ILineSpectrum} (count (C) of tests
     * that NOT (N) executed this line spectrum and the test passed (P)).
     *
     * @param spectrum a {@link ILineSpectrum}.
     * @return the CNP counter of a {@link ILineSpectrum}.
     */
    int getCnp(ILineSpectrum spectrum);

    /**
     * Evaluate a given individual {@link ILineSpectrum}.
     *
     * @param spectrum a {@link ILineSpectrum}.
     * @return evaluation of a {@link ILineSpectrum}.
     */
    double eval(ILineSpectrum spectrum);

}
