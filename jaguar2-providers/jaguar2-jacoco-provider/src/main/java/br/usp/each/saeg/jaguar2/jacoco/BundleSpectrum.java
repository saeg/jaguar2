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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import br.usp.each.saeg.jaguar2.api.IBundleSpectrum;

public class BundleSpectrum implements IBundleSpectrum {

    private final Collection<PackageSpectrum> packages;

    public BundleSpectrum(
            final Collection<ClassSpectrum> classes,
            final Collection<SourceFileSpectrum> sourceFiles) {

        packages = groupByPackages(classes, sourceFiles);
    }

    @Override
    public Collection<PackageSpectrum> getPackages() {
        return packages;
    }

    private static Collection<PackageSpectrum> groupByPackages(
            final Collection<ClassSpectrum> classes,
            final Collection<SourceFileSpectrum> sourceFiles) {

        // group classes by package name
        final Map<String, Collection<ClassSpectrum>> classesByPackage =
                new LinkedHashMap<String, Collection<ClassSpectrum>>();
        for (final ClassSpectrum cs : classes) {
            Collection<ClassSpectrum> packageClasses = classesByPackage.get(cs.getPackageName());
            if (packageClasses == null) {
                packageClasses = new ArrayList<ClassSpectrum>();
                classesByPackage.put(cs.getPackageName(), packageClasses);
            }
            packageClasses.add(cs);
        }
        // group source files by package name
        final Map<String, Collection<SourceFileSpectrum>> sourcesByPackage =
                new LinkedHashMap<String, Collection<SourceFileSpectrum>>();
        for (final SourceFileSpectrum sfs : sourceFiles) {
            Collection<SourceFileSpectrum> packageSources = sourcesByPackage.get(sfs.getPackageName());
            if (packageSources == null) {
                packageSources = new ArrayList<SourceFileSpectrum>();
                sourcesByPackage.put(sfs.getPackageName(), packageSources);
            }
            packageSources.add(sfs);
        }

        final Set<String> packageNames = new LinkedHashSet<String>();
        packageNames.addAll(classesByPackage.keySet());
        packageNames.addAll(sourcesByPackage.keySet());

        // create packages
        final Collection<PackageSpectrum> result = new ArrayList<PackageSpectrum>();
        for (final String packageName : packageNames) {
            Collection<ClassSpectrum> c = classesByPackage.get(packageName);
            if (c == null) {
                c = Collections.emptyList();
            }
            Collection<SourceFileSpectrum> s = sourcesByPackage.get(packageName);
            if (s == null) {
                s = Collections.emptyList();
            }
            result.add(new PackageSpectrum(packageName, c, s));
        }
        return result;
    }

}
