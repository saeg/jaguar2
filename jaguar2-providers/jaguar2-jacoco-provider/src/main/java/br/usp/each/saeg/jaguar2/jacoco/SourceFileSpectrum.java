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

import br.usp.each.saeg.jaguar2.api.ISourceFileSpectrum;

public class SourceFileSpectrum extends CodeSpectrum implements ISourceFileSpectrum {

    private final String name;

    private final String packageName;

    public SourceFileSpectrum(final String name, final String packageName) {
        this.name = name;
        this.packageName = packageName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

}
