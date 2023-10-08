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
package br.usp.each.saeg.jaguar2.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import br.usp.each.saeg.jaguar2.api.IBundleSpectrum;
import br.usp.each.saeg.jaguar2.api.ILineSpectrum;
import br.usp.each.saeg.jaguar2.api.IPackageSpectrum;
import br.usp.each.saeg.jaguar2.api.ISourceFileSpectrum;
import br.usp.each.saeg.jaguar2.api.SpectrumEval;
import br.usp.each.saeg.jaguar2.spi.SpectrumExporter;

/**
 * A {@link SpectrumExporter} implementation that writes to a XML file
 */
public class XmlExporter implements SpectrumExporter {

    private XMLStreamWriter writer;

    /**
     * Default constructor
     */
    public XmlExporter() {
    }

    @Override
    public void init()
            throws XMLStreamException, FactoryConfigurationError, IOException {

        writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
                new BufferedWriter(
                        new FileWriter(
                                new File("target", "jaguar2.xml"))));

        writer.writeStartDocument();
        writer.writeStartElement("report");
    }

    @Override
    public void write(final IBundleSpectrum bundle, final SpectrumEval eval)
            throws XMLStreamException {

        writer.writeStartElement("tests");
        writer.writeAttribute("fail", String.valueOf(eval.getFailedTests()));
        writer.writeAttribute("pass", String.valueOf(eval.getPassedTests()));
        writer.writeEndElement();

        for (final IPackageSpectrum packageSpectrum : bundle.getPackages()) {
            writer.writeStartElement("package");
            writer.writeAttribute("name", packageSpectrum.getName());
            for (final ISourceFileSpectrum spectrum : packageSpectrum.getSourceFiles()) {
                write(spectrum, eval);
            }
            writer.writeEndElement();
        }
    }

    private void write(final ISourceFileSpectrum spectrum, final SpectrumEval eval)
            throws XMLStreamException {

        writer.writeStartElement("sourcefile");
        writer.writeAttribute("name", spectrum.getName());

        for (int nr = spectrum.getFirstLine(); nr <= spectrum.getLastLine(); nr++) {
            final ILineSpectrum line = spectrum.getLine(nr);
            final double susp = eval.eval(line);
            if (susp > 0.0d) {
                final int cef = eval.getCef(line);
                final int cep = eval.getCep(line);

                writer.writeStartElement("line");
                writer.writeAttribute("nr", String.valueOf(nr));
                writer.writeAttribute("cef", String.valueOf(cef));
                writer.writeAttribute("cep", String.valueOf(cep));
                writer.writeAttribute("susp", String.format("%f", susp));
                writer.writeEndElement();
            }
        }

        writer.writeEndElement();
    }

    @Override
    public void shutdown() throws XMLStreamException {
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.close();
    }

}
