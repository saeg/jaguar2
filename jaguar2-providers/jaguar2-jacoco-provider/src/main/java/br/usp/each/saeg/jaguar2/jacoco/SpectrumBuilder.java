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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICoverageVisitor;
import org.jacoco.core.analysis.IMethodCoverage;

class SpectrumBuilder implements ICoverageVisitor {

    private final Map<String, ClassSpectrum> classes = new HashMap<String, ClassSpectrum>();

    private final Map<String, SourceFileSpectrum> sourceFiles = new HashMap<String, SourceFileSpectrum>();

    final UpdateSpectrum updateTestFailed = new UpdateSpectrum(true);

    final UpdateSpectrum updateTestPassed = new UpdateSpectrum(false);

    @Override
    public void visitCoverage(final IClassCoverage classCoverage) {
        final ClassSpectrum cs = new ClassSpectrum(classCoverage);
        for (final IMethodCoverage methodCoverage : classCoverage.getMethods()) {
            cs.addMethod(methodCoverage);
        }
        classes.put(cs.getName(), cs);
        if (cs.getSourceFileName() != null) {
            getSourceFile(cs.getSourceFileName(), cs.getPackageName());
        }
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
            if (cs.getSourceFileName() != null) {
                final SourceFileSpectrum sourceFile =
                        getSourceFile(cs.getSourceFileName(), cs.getPackageName());
                sourceFile.increment(classCoverage, testFailed);
            }
        }

    }

    Collection<ClassSpectrum> getClasses() {
        // Using tree map to guarantee iteration order (for reproducible builds/validation)
        return new TreeMap<String, ClassSpectrum>(classes).values();
    }

    Collection<SourceFileSpectrum> getSourceFiles() {
        // Using tree map to guarantee iteration order (for reproducible builds/validation)
        return new TreeMap<String, SourceFileSpectrum>(sourceFiles).values();
    }

    private SourceFileSpectrum getSourceFile(
            final String fileName, final String packageName) {

        final String key = packageName + '/' + fileName;
        SourceFileSpectrum sourceFile = sourceFiles.get(key);
        if (sourceFile == null) {
            sourceFile = new SourceFileSpectrum(fileName, packageName);
            sourceFiles.put(key, sourceFile);
        }
        return sourceFile;
    }

}
