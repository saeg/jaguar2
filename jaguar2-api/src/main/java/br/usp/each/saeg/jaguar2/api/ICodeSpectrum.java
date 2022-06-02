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
 * Spectrum data of portions of code that have individual source lines
 * like methods and classes.
 *
 * @see ILineSpectrum
 */
public interface ICodeSpectrum {

    /**
     * Place holder for unknown lines (no debug information).
     */
    int UNKNOWN_LINE = -1;

    /**
     * Returns the number of the first line spectrum data is available. If
     * no line is contained, the method returns {@link #UNKNOWN_LINE}.
     *
     * @return number of the first line or {@link #UNKNOWN_LINE}.
     */
    int getFirstLine();

    /**
     * Returns the number of the last line spectrum data is available. If
     * no line is contained, the method returns {@link #UNKNOWN_LINE}.
     *
     * @return number of the last line or {@link #UNKNOWN_LINE}.
     */
    int getLastLine();

    /**
     * Returns the spectrum data for given line.
     *
     * @param nr line number of interest.
     * @return a {@link ILineSpectrum} for a given line.
     */
    ILineSpectrum getLine(int nr);

}
