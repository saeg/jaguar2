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

import br.usp.each.saeg.jaguar2.api.IBundleSpectrum;
import br.usp.each.saeg.jaguar2.api.IClassSpectrum;
import br.usp.each.saeg.jaguar2.api.ILineSpectrum;
import br.usp.each.saeg.jaguar2.api.IPackageSpectrum;
import br.usp.each.saeg.jaguar2.api.SpectrumEval;
import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

/**
 * A {@link SpectrumExporter} implementation that writes to a CSV file
 */
public class CsvExporter implements SpectrumExporter {

    private PrintWriter writer;

    @Override
    public void init() throws IOException {
        final File output = new File("target", "jaguar2.csv");
        writer = new PrintWriter(new BufferedWriter(new FileWriter(output)));
    }

    @Override
    public void write(final IBundleSpectrum bundle, final SpectrumEval eval) {
        for (final IPackageSpectrum packageSpectrum : bundle.getPackages()) {
            for (final IClassSpectrum spectrum : packageSpectrum.getClasses()) {
                write(spectrum, eval);
            }
        }
    }

    private void write(final IClassSpectrum spectrum, final SpectrumEval eval) {
        for (int nr = spectrum.getFirstLine(); nr <= spectrum.getLastLine(); nr++) {
            final ILineSpectrum line = spectrum.getLine(nr);
            final double susp = eval.eval(line);
            if (susp > 0.0d) {
                final String className = spectrum.getName();
                final int cef = eval.getCef(line);
                final int cnf = eval.getCnf(line);
                final int cep = eval.getCep(line);
                final int cnp = eval.getCnp(line);
                writer.format("%s,%d,%d,%d,%d,%d,%f\n",
                        className, nr, cef, cnf, cep, cnp, susp);
            }
        }
    }

    @Override
    public void shutdown() {
        writer.close();
    }

}
