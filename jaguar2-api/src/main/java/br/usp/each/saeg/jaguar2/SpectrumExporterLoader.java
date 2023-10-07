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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;

import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

/**
 * A spectrum exporter service loading facility.
 */
public class SpectrumExporterLoader {

    private final ServiceLoader<SpectrumExporter> serviceLoader;

    public SpectrumExporterLoader(
            final ServiceLoader<SpectrumExporter> serviceLoader) {
        this.serviceLoader = serviceLoader;
    }

    public SpectrumExporterLoader() {
        this(ServiceLoader.load(SpectrumExporter.class));
    }

    /**
     * Loads and returns a spectrum exporter service.
     *
     * This implementation returns a {@link MultipleSpectrumExporter}
     * joining all {@link SpectrumExporter} available from underlying
     * {@link ServiceLoader}.
     *
     * @return a spectrum exporter service.
     */
    public SpectrumExporter load() {
        final Collection<SpectrumExporter> exporters = new ArrayList<SpectrumExporter>();
        final Iterator<SpectrumExporter> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            exporters.add(iterator.next());
        }
        return new MultipleSpectrumExporter(exporters);
    }

}
