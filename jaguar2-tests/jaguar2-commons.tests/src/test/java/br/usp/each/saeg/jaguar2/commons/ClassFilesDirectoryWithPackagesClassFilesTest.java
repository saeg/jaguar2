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

public class ClassFilesDirectoryWithPackagesClassFilesTest extends AbstractClassFilesTest {

    @Override
    public void setUp() {
        newFile("Some.class");
        newFile("Another.class");
        newFile("Foo.class", "foo");
        newFile("Bar.class", "bar");
        newFile("FooBar.class", "foo", "bar");
        newFileOtherFolder("Other.class");
        newFileOtherFolder("OtherAnother.class");
        newFileOtherFolder("OtherFoo.class", "foo");
        newFileOtherFolder("OtherBar.class", "bar");
        newFileOtherFolder("OtherFooBar.class", "foo", "bar");
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
    public void testPackageFooClassFooIsFound() {
        Assert.assertNotNull(classFiles.get("foo/Foo"));
    }

    @Test
    public void testPackageBarClassBarIsFound() {
        Assert.assertNotNull(classFiles.get("bar/Bar"));
    }

    @Test
    public void testPackageFooNestedPackageBarClassFooBarIsFound() {
        Assert.assertNotNull(classFiles.get("foo/bar/FooBar"));
    }

    @Test
    public void testOtherClassIsFound() {
        Assert.assertNotNull(classFiles.get("Other"));
    }

    @Test
    public void testOtherAnotherClassIsFound() {
        Assert.assertNotNull(classFiles.get("OtherAnother"));
    }

    @Test
    public void testPackageFooClassOtherFooIsFound() {
        Assert.assertNotNull(classFiles.get("foo/OtherFoo"));
    }

    @Test
    public void testPackageBarClassOtherBarIsFound() {
        Assert.assertNotNull(classFiles.get("bar/OtherBar"));
    }

    @Test
    public void testPackageFooNestedPackageBarClassOtherFooBarIsFound() {
        Assert.assertNotNull(classFiles.get("foo/bar/OtherFooBar"));
    }

}
