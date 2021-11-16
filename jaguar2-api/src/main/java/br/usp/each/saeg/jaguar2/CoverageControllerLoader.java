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
package br.usp.each.saeg.jaguar2;

import java.util.Iterator;
import java.util.ServiceLoader;

import br.usp.each.saeg.jaguar2.spi.CoverageController;

/**
 * A coverage controller service loading facility.
 */
public class CoverageControllerLoader {

    private final ServiceLoader<CoverageController> serviceLoader;

    public CoverageControllerLoader(
            final ServiceLoader<CoverageController> serviceLoader) {
        this.serviceLoader = serviceLoader;
    }

    public CoverageControllerLoader() {
        this(ServiceLoader.load(CoverageController.class));
    }

    /**
     * Loads and returns a coverage controller service.
     *
     * This implementation returns the first {@link CoverageController}
     * from underlying {@link ServiceLoader}.
     *
     * @return a coverage controller service or <code>null</code> if there
     *         is no providers.
     */
    public CoverageController load() {
        final Iterator<CoverageController> iterator = serviceLoader.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

}
