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
package br.usp.each.saeg.jaguar2;

import java.util.Collection;
import java.util.Collections;

import br.usp.each.saeg.jaguar2.api.IBundleSpectrum;
import br.usp.each.saeg.jaguar2.api.SpectrumEval;
import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

class MultipleSpectrumExporter implements SpectrumExporter {

    private final Collection<SpectrumExporter> exporters;

    MultipleSpectrumExporter(final Collection<SpectrumExporter> exporters) {
        this.exporters = exporters;
    }

    @Override
    public void init() throws Exception {
        for (final SpectrumExporter exporter : exporters) {
            exporter.init();
        }
    }

    @Override
    public void write(final IBundleSpectrum spectrum, final SpectrumEval eval)
            throws Exception {

        for (final SpectrumExporter exporter : exporters) {
            exporter.write(spectrum, eval);
        }
    }

    @Override
    public void shutdown() throws Exception {
        for (final SpectrumExporter exporter : exporters) {
            exporter.shutdown();
        }
    }

    Collection<SpectrumExporter> getExporters() {
        return Collections.unmodifiableCollection(exporters);
    }

}
