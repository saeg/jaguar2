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

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class ClassFilesEmptyDirectoryTest extends AbstractClassFilesTest {

    @Test
    public void testIsEmpty() throws IOException {
        Assert.assertTrue(classFiles.isEmpty());
    }

}
