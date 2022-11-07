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
import org.junit.Test;

import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

public class SpectrumExporterLoaderTest {

    @Test
    public void loadsSpectrumExportersCorrectly() {
        final SpectrumExporterLoader loader = new SpectrumExporterLoader();
        final Collection<SpectrumExporter> exporters = loader.load();
        Assert.assertEquals(1, exporters.size());
        Assert.assertTrue(exporters.iterator().next() instanceof DummySpectrumExporter);
    }

}
