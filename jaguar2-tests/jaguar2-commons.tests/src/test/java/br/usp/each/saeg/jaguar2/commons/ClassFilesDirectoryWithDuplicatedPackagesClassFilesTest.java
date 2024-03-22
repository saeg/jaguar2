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

import org.junit.Assert;
import org.junit.Test;

public class ClassFilesDirectoryWithDuplicatedPackagesClassFilesTest
        extends AbstractClassFilesTest {

    private File some;

    private File another;

    private File foo;

    private File bar;

    private File fooBar;

    private File otherSome;

    private File otherAnother;

    private File otherFoo;

    private File otherBar;

    private File otherFooBar;

    @Override
    public void setUp() {
        some = newFile("Some.class");
        another = newFile("Another.class");
        foo = newFile("Foo.class", "foo");
        bar = newFile("Bar.class", "bar");
        fooBar = newFile("FooBar.class", "foo", "bar");
        otherSome = newFileOtherFolder("Some.class");
        otherAnother = newFileOtherFolder("Another.class");
        otherFoo = newFileOtherFolder("Foo.class", "foo");
        otherBar = newFileOtherFolder("Bar.class", "bar");
        otherFooBar = newFileOtherFolder("FooBar.class", "foo", "bar");
        super.setUp();
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(classFiles.isEmpty());
    }

    @Test
    public void testSomeClassIsFound() {
        final File file = classFiles.get("Some");
        Assert.assertEquals(some, file);
        Assert.assertNotEquals(otherSome, file);
    }

    @Test
    public void testAnotherClassIsFound() {
        final File file = classFiles.get("Another");
        Assert.assertEquals(another, file);
        Assert.assertNotEquals(otherAnother, file);
    }

    @Test
    public void testPackageFooClassFooIsFound() {
        final File file = classFiles.get("foo/Foo");
        Assert.assertEquals(foo, file);
        Assert.assertNotEquals(otherFoo, file);
    }

    @Test
    public void testPackageBarClassBarIsFound() {
        final File file = classFiles.get("bar/Bar");
        Assert.assertEquals(bar, file);
        Assert.assertNotEquals(otherBar, file);
    }

    @Test
    public void testPackageFooNestedPackageBarClassFooBarIsFound() {
        final File file = classFiles.get("foo/bar/FooBar");
        Assert.assertEquals(fooBar, file);
        Assert.assertNotEquals(otherFooBar, file);
    }

}
