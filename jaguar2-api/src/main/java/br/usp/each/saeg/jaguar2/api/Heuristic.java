/**
 * Copyright (c) 2021, 2021 University of Sao Paulo and Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Henrique Lemos - initial API and implementation and/or initial documentation
 */
package br.usp.each.saeg.jaguar2.api;

/**
 * Interface representing a spectrum-base fault localization heuristic
 */
public interface Heuristic {

    /**
     * Evaluate heuristic for a given individual spectrum counters.
     *
     * @param cef count (C) of tests that executed (E) this spectrum and
     *            the test failed (F).
     * @param cnf count (C) of tests that NOT (N) executed this spectrum
     *            and the test failed (F).
     * @param cep count (C) of tests that executed (E) this spectrum and
     *            the test passed (P).
     * @param cnp count (C) of tests that NOT (N) executed this spectrum
     *            and the test passed (P).
     * @return heuristic evaluation.
     */
    double eval(int cef, int cnf, int cep, int cnp);

}
