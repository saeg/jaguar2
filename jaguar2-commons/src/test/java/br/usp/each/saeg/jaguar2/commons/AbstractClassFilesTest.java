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
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class AbstractClassFilesTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public final TemporaryFolder otherFolder = new TemporaryFolder();

    protected ClassFiles classFiles;

    @Before
    public void setUp() {
        classFiles = new ClassFiles(folder.getRoot(), otherFolder.getRoot());
    }

    private static File newFile(final TemporaryFolder folder,
            final String fileName, final String... paths) {

        try {
            if (paths.length > 0) {
                final File f = new File(folder.newFolder(paths), fileName);
                f.createNewFile();
                return f;
            } else {
                return folder.newFile(fileName);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File newFile(final String fileName, final String... paths) {
        return newFile(folder, fileName, paths);
    }

    public File newFileOtherFolder(final String fileName, final String... paths) {
        return newFile(otherFolder, fileName, paths);
    }

}
