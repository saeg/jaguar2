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
package br.usp.each.saeg.jaguar2.commons;

import java.io.File;

import br.usp.each.saeg.jaguar2.spi.CoverageController;

public abstract class ClassFilesController implements CoverageController {

    public static final String JAGUAR2_CLASSES_DIRS_PROPERTY_NAME = "jaguar2.classesDirs";

    protected File[] classesDirs;

    protected ClassFiles classFiles;

    @Override
    public void init() {
        classesDirs = getClassesDirs();
        classFiles = new ClassFiles(classesDirs);
    }

    @Override
    public void destroy() {
        classesDirs = null;
        classFiles = null;
    }

    private static File[] getClassesDirs() {
        final String value = System.getProperty(JAGUAR2_CLASSES_DIRS_PROPERTY_NAME);
        if (value == null) {
            return new File[0];
        }
        final String[] paths = value.split(File.pathSeparator);
        final File[] files = new File[paths.length];
        for (int i = 0; i < paths.length; i++) {
            files[i] = new File(paths[i]);
        }
        return files;
    }

}
