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
 * Spectrum data of a single class.
 *
 * @see IMethodSpectrum
 * @see ICodeSpectrum
 */
public interface IClassSpectrum extends ICodeSpectrum {

    /**
     * Returns the fully qualified class name in VM notation.
     *
     * @return fully qualified class name in VM notation.
     */
    String getName();

    /**
     * Returns the spectrum data of methods included in this class.
     *
     * @return a {@link Collection} of {@link IMethodSpectrum}.
     */
    Collection<? extends IMethodSpectrum> getMethods();

}
