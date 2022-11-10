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

import br.usp.each.saeg.jaguar2.api.IClassSpectrum;
import br.usp.each.saeg.jaguar2.api.SpectrumEval;

/**
 * A service that exports spectrum data.
 */
public interface SpectrumExporter {

    /**
     * Initialize the exporter service.
     *
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * Write the spectrum data for a class.
     *
     * @param spectrum a {@link IClassSpectrum} that represents spectrum
     *                 data for a single class.
     *
     * @param eval     a {@link SpectrumEval} that calculate a spectrum
     *                 suspiciousness.
     *
     * @throws Exception
     */
    void write(IClassSpectrum spectrum, SpectrumEval eval) throws Exception;

    /**
     * Shutdown the exporter service and cleanup created resources.
     *
     * @throws Exception
     */
    void shutdown() throws Exception;

}
