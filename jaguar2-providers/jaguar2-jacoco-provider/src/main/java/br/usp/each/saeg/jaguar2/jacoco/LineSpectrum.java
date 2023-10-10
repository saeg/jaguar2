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
package br.usp.each.saeg.jaguar2.jacoco;

import br.usp.each.saeg.jaguar2.api.ILineSpectrum;

class LineSpectrum implements ILineSpectrum {

    static final LineSpectrum EMPTY = new LineSpectrum(0, 0);

    private final int cef;

    private final int cep;

    private LineSpectrum(final int cef, final int cep) {
        this.cef = cef;
        this.cep = cep;
    }

    @Override
    public int getCef() {
        return cef;
    }

    @Override
    public int getCep() {
        return cep;
    }

    LineSpectrum increment(final boolean testFailed) {
        if (testFailed) {
            return new LineSpectrum(cef + 1, cep);
        } else {
            return new LineSpectrum(cef, cep + 1);
        }
    }

}
