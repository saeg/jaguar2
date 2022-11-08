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
package br.usp.each.saeg.jaguar2.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import br.usp.each.saeg.jaguar2.api.IClassSpectrum;
import br.usp.each.saeg.jaguar2.api.SpectrumEval;
import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

public class CsvExporter implements SpectrumExporter {

    private PrintWriter writer;

    @Override
    public void init() throws IOException {
        final File output = new File("target", "jaguar2.csv");
        writer = new PrintWriter(new BufferedWriter(new FileWriter(output)));
    }

    @Override
    public void write(final IClassSpectrum spectrum, final SpectrumEval eval) {
        for (int nr = spectrum.getFirstLine(); nr <= spectrum.getLastLine(); nr++) {
            final double susp = eval.eval(spectrum.getLine(nr));
            if (susp > 0.0d) {
                writer.format("%s,%d,%f\n", spectrum.getName(), nr, susp);
            }
        }
    }

    @Override
    public void shutdown() {
        writer.close();
    }

}
