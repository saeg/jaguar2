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
import java.util.LinkedHashMap;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;

import br.usp.each.saeg.jaguar2.api.IClassSpectrum;

public class ClassSpectrum extends CodeSpectrum implements IClassSpectrum {

    private final Map<String, MethodSpectrum> methods = new LinkedHashMap<String, MethodSpectrum>();

    private final IClassCoverage coverage;

    public ClassSpectrum(final IClassCoverage coverage) {
        this.coverage = coverage;
    }

    @Override
    public String getName() {
        return coverage.getName();
    }

    @Override
    public Collection<MethodSpectrum> getMethods() {
        return methods.values();
    }

    public void addMethod(final IMethodCoverage coverage) {
        final MethodSpectrum spectrum = new MethodSpectrum(coverage);
        methods.put(k(coverage.getName(), coverage.getDesc()), spectrum);
    }

    public MethodSpectrum getMethod(final IMethodCoverage coverage) {
        return methods.get(k(coverage.getName(), coverage.getDesc()));
    }

    private String k(final String name, final String desc) {
        final StringBuilder builder = new StringBuilder(name.length() + desc.length());
        builder.append(name);
        builder.append(desc);
        return builder.toString();
    }

}
