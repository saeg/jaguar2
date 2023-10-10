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

import br.usp.each.saeg.jaguar2.api.IPackageSpectrum;

class PackageSpectrum implements IPackageSpectrum {

    private final String name;

    private final Collection<ClassSpectrum> classes;

    private final Collection<SourceFileSpectrum> sourceFiles;

    PackageSpectrum(final String name,
            final Collection<ClassSpectrum> classes,
            final Collection<SourceFileSpectrum> sourceFiles) {

        this.name = name;
        this.classes = classes;
        this.sourceFiles = sourceFiles;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<ClassSpectrum> getClasses() {
        return classes;
    }

    @Override
    public Collection<SourceFileSpectrum> getSourceFiles() {
        return sourceFiles;
    }

}
