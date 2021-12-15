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

public class OperationalSystemUtils {
	
	private static String operationalSystem = null;
	
	private static String fileSeparator = null;
	
	private OperationalSystemUtils() {
	}
	
	public static String getOsName() {
		if(operationalSystem == null) { operationalSystem = System.getProperty("os.name"); }
		return operationalSystem;
	}
	
	public static boolean isWindows()
	{
		return getOsName().startsWith("Windows");
	}
	
	public static boolean isLinux(){
		return getOsName().startsWith("Linux");
	}
	
	public static String systemFileSeparator(){
		
		if(fileSeparator == null){
			fileSeparator = System.getProperty("file.separator");
		}
		
		return fileSeparator;
	}
	
}
