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
package br.usp.each.saeg.jaguar2.api;

import java.util.Collection;

/**
 * Spectrum data of a single package.
 *
 * @see IClassSpectrum
 * @see ISourceFileSpectrum
 */
public interface IPackageSpectrum {

    /**
     * Returns the fully qualified package name in VM notation.
     *
     * @return fully qualified package name in VM notation.
     */
    String getName();

    /**
     * Returns the spectrum data of classes included in this package.
     *
     * @return a {@link Collection} of {@link IClassSpectrum}.
     */
    Collection<? extends IClassSpectrum> getClasses();

    /**
     * Returns the spectrum data of source files included in this package.
     *
     * @return a {@link Collection} of {@link ISourceFileSpectrum}.
     */
    Collection<? extends ISourceFileSpectrum> getSourceFiles();

}
