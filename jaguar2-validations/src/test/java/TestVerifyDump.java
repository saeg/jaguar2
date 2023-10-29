
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.junit.Assert;
import org.junit.Test;

public class TestVerifyDump {

    @Test
    public void verify() throws XMLStreamException, FileNotFoundException {
        final XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(
            new FileInputStream(
                "../jaguar2-examples/jaguar2-example-junit4-jacoco/target/site/jacoco/jacoco.xml"
            )
        );

        int sessions = 0;
        while (reader.hasNext()) {
            final XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                final StartElement startElement = nextEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("sessioninfo")) {
                    sessions++;
                }
            }
        }

        Assert.assertEquals(6, sessions);
    }

}
