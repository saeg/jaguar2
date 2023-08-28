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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.usp.each.saeg.jaguar2.api.MultipleSpectrumExporter;
import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

public class SpectrumExporterLoaderTest {

    private SpectrumExporter exporter;

    @Before
    public void setUp() {
        exporter = new SpectrumExporterLoader().load();
    }

    @Test
    public void loadsSpectrumExporterCorrectly() {
        Assert.assertTrue(exporter instanceof MultipleSpectrumExporter);
    }

    @Test
    public void loadsInternalSpectrumExportersCorrectly() {
        final Collection<SpectrumExporter> exporters = 
                ((MultipleSpectrumExporter) exporter).getExporters();

        Assert.assertEquals(1, exporters.size());
        Assert.assertTrue(exporters.iterator().next() instanceof DummySpectrumExporter);
    }

}
