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
import java.util.HashMap;
import java.util.Map;

/**
 * Utility that cache all .class files from a directory and look up
 * them based on the VM class name.
 *
 * Note: We use the folder structure as the package name and the file
 * name as class name.
 */
public class ClassFiles {

    private static final String DOT_CLASS = ".class";

    private final Map<String, File> classFilesCache = new HashMap<String, File>();

    /**
     * Utility that cache all .class files from a directory and look up
     * them based on the VM class name.
     *
     * @param classesDirs the directories to search for .class files.
     */
    public ClassFiles(final File... classesDirs) {
        for (final File file : classesDirs) {
            populateClassFilesCache(file, "");
        }
    }

    private void populateClassFilesCache(final File dir, final String path) {
        final File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (final File file : files) {
            final String name = file.getName();
            if (file.isDirectory()) {
                populateClassFilesCache(file, path + name + "/");
            } else if (name.endsWith(DOT_CLASS)) {
                final String className = path
                        + name.substring(0, name.length() - DOT_CLASS.length());

                if (!classFilesCache.containsKey(className)) {
                    classFilesCache.put(className, file);
                }
            }
        }
    }

    /**
     * Check if cache is empty or not.
     *
     * @return <code>true</code> if there is no .class files cached,
     *         <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return classFilesCache.isEmpty();
    }

    /**
     * Get a {@link File} from a given .class based on the VM class name.
     *
     * @param vmClassName the VM class name;
     *
     *                    Note: We use the folder structure as the package
     *                    name and the file name as class name.
     *
     * @return a {@link File} from a given .class file of
     *         <code>null</code> if the class file is not cached.
     */
    public File get(final String vmClassName) {
        return classFilesCache.get(vmClassName);
    }

}
