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
package br.usp.each.saeg.jaguar.core.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CodeUtils {
	
	private CodeUtils() {
	}
	
	public static String getCodeFromAbsolutePath(String absolutePath) throws IOException {
		File clazz = new File(absolutePath);
		return getCodeFromAbsolutePath(clazz);
	}
	
	public static String getCodeFromAbsolutePath(File classFile) throws IOException {
		return FileUtils.readFileToString(classFile);
	}
	
}
