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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import br.usp.each.saeg.jaguar2.api.IBundleSpectrum;

public class BundleSpectrum implements IBundleSpectrum {

    private final Collection<PackageSpectrum> packages;

    public BundleSpectrum(final Collection<ClassSpectrum> classes) {
        this.packages = groupByPackages(classes);
    }

    @Override
    public Collection<PackageSpectrum> getPackages() {
        return packages;
    }

    private static Collection<PackageSpectrum> groupByPackages(
            final Collection<ClassSpectrum> classes) {

        // group classes by package name
        final Map<String, Collection<ClassSpectrum>> classesByPackage = new LinkedHashMap<String, Collection<ClassSpectrum>>();
        for (final ClassSpectrum cs : classes) {
            Collection<ClassSpectrum> packageClasses = classesByPackage.get(cs.getPackageName());
            if (packageClasses == null) {
                packageClasses = new ArrayList<ClassSpectrum>();
                classesByPackage.put(cs.getPackageName(), packageClasses);
            }
            packageClasses.add(cs);
        }

        // create packages
        final Collection<PackageSpectrum> result = new ArrayList<PackageSpectrum>();
        for (final String packageName : classesByPackage.keySet()) {
            result.add(new PackageSpectrum(packageName, classesByPackage.get(packageName)));
        }
        return result;
    }

}
