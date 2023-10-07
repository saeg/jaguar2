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

import org.jacoco.core.analysis.IMethodCoverage;

import br.usp.each.saeg.jaguar2.api.IMethodSpectrum;

class MethodSpectrum extends CodeSpectrum implements IMethodSpectrum {

    private final IMethodCoverage coverage;

    MethodSpectrum(final IMethodCoverage coverage) {
        this.coverage = coverage;
    }

    @Override
    public String getName() {
        return coverage.getName();
    }

    @Override
    public String getDesc() {
        return coverage.getDesc();
    }

}
