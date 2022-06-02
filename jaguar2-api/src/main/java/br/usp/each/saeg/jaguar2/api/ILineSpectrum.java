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
 * Spectrum data of a single line.
 */
public interface ILineSpectrum {

    /**
     * Returns the CEF counter. Count (C) of tests that executed (E) this
     * line and the test failed (F).
     *
     * @return CEF counter.
     */
    int getCef();

    /**
     * Return the CEP counter. Count (C) of tests that executed (E) this
     * line and the test passed (P).
     *
     * @return CEP counter.
     */
    int getCep();

}
