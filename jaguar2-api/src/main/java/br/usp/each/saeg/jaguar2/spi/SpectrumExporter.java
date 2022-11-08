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

import br.usp.each.saeg.jaguar2.api.IClassSpectrum;
import br.usp.each.saeg.jaguar2.api.SpectrumEval;

/**
 * A service that exports spectrum data.
 */
public interface SpectrumExporter {

    /**
     * Initialize the exporter service.
     *
     * @throws IOException
     */
    void init() throws IOException;

    /**
     * Write the spectrum data for a class.
     *
     * @param spectrum a {@link IClassSpectrum} that represents spectrum
     *                 data for a single class.
     *
     * @param eval     a {@link SpectrumEval} that calculate a spectrum
     *                 suspiciousness.
     */
    void write(IClassSpectrum spectrum, SpectrumEval eval);

    /**
     * Shutdown the exporter service and cleanup created resources.
     */
    void shutdown();

}
