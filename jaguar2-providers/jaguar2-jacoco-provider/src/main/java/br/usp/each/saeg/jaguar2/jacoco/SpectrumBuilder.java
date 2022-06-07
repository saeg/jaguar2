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

import java.util.HashMap;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICoverageVisitor;
import org.jacoco.core.analysis.IMethodCoverage;

import br.usp.each.saeg.jaguar2.api.ISpectrumVisitor;

public class SpectrumBuilder implements ICoverageVisitor {

    private final Map<String, ClassSpectrum> classes = new HashMap<String, ClassSpectrum>();

    public final UpdateSpectrum updateTestFailed = new UpdateSpectrum(true);

    public final UpdateSpectrum updateTestPassed = new UpdateSpectrum(false);

    @Override
    public void visitCoverage(final IClassCoverage classCoverage) {
        final ClassSpectrum cs = new ClassSpectrum(classCoverage);
        for (final IMethodCoverage methodCoverage : classCoverage.getMethods()) {
            cs.addMethod(methodCoverage);
        }
        classes.put(cs.getName(), cs);
    }

    private class UpdateSpectrum implements ICoverageVisitor {

        private final boolean testFailed;

        public UpdateSpectrum(final boolean testFailed) {
            this.testFailed = testFailed;
        }

        @Override
        public void visitCoverage(final IClassCoverage classCoverage) {
            final ClassSpectrum cs = classes.get(classCoverage.getName());
            cs.increment(classCoverage, testFailed);
            for (final IMethodCoverage methodCoverage : classCoverage.getMethods()) {
                cs.getMethod(methodCoverage).increment(methodCoverage, testFailed);
            }
        }

    }

    public void accept(final ISpectrumVisitor spectrumVisitor) {
        for (final ClassSpectrum cs : classes.values()) {
            spectrumVisitor.visitSpectrum(cs);
        }
    }

}
