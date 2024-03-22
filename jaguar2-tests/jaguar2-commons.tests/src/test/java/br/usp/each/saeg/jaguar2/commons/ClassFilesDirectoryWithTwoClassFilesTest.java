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

import org.junit.Assert;
import org.junit.Test;

public class ClassFilesDirectoryWithTwoClassFilesTest extends AbstractClassFilesTest {

    @Override
    public void setUp() {
        newFile("Some.class");
        newFile("Another.class");
        newFileOtherFolder("Other.class");
        newFileOtherFolder("OtherAnother.class");
        super.setUp();
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(classFiles.isEmpty());
    }

    @Test
    public void testSomeClassIsFound() {
        Assert.assertNotNull(classFiles.get("Some"));
    }

    @Test
    public void testAnotherClassIsFound() {
        Assert.assertNotNull(classFiles.get("Another"));
    }

    @Test
    public void testOtherClassIsFound() {
        Assert.assertNotNull(classFiles.get("Other"));
    }

    @Test
    public void testOtherAnotherClassIsFound() {
        Assert.assertNotNull(classFiles.get("OtherAnother"));
    }

}
