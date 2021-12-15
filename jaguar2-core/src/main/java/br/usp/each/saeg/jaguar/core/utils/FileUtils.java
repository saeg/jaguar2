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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import br.usp.each.saeg.commons.io.Files;

/**
 * Provides utilies methods that can be used to search and list files within a
 * directory.
 * 
 * @author Henrique Ribeiro
 * 
 */
public class FileUtils {
	
	private FileUtils() {
	}
	
	private static final String COMMAND_TO_MAKE_FILE_HIDDEN_ON_WINDOWS = "C:\\\\WINDOWS\\\\System32\\\\ATTRIB.EXE +H " /* plus file name */;

	/**
	 * Create classes out of each line of the given text file.
	 * 
	 * @param testsListFile a text file with the name of a class in each line
	 * @return the list of classes
	 */
	public static Class<?>[] getClassesInFile(File testsListFile){
		List<Class<?>> list = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(testsListFile), "UTF-8")); //$NON-NLS-1$
		} catch (UnsupportedEncodingException | FileNotFoundException e1) {
			e1.printStackTrace();
		} 
		try {
			String line;
			list = new ArrayList<Class<?>>();
			try {
				while ((line= br.readLine()) != null) {
					list.add(Class.forName(line));
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list.toArray(new Class<?>[list.size()]);
	}
	
	/**
	 * Search recursively for classes ending with
	 * Test.class within the directory of the given class
	 * 
	 * @param clazz
	 *            the current class
	 * @return list of files ending with Test.class
	 * @throws ClassNotFoundException
	 */
	public static Class<?>[] findTestClasses(Class<?> clazz) throws ClassNotFoundException {
		File testDir = findClassDir(clazz);
		return findTestClasses(testDir);
	}
	
	/**
	 * Search recursively for classes ending with
	 * Test.class within the current directory.
	 * 
	 * @param testDir the directory to be searched
	 * @return list of files ending with Test.class
	 * @throws ClassNotFoundException
	 */
	public static Class<?>[] findTestClasses(File testDir) throws ClassNotFoundException {
		List<File> testClassFiles = findFilesEndingWith(testDir, new String[] { "Test.class" });
		List<Class<?>> classes = convertToClasses(testClassFiles, testDir);
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Convert files into classes.
	 * 
	 * @param classFiles
	 *            files to be searched
	 * @param classesDir
	 *            where the files are saved
	 * @return list of classes
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> convertToClasses(final List<File> classFiles, final File classesDir) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (File file : classFiles) {
			Class<?> c = Class.forName(getClassNameFromFile(classesDir, file));
			if (!Modifier.isAbstract(c.getModifiers())) {
				classes.add(c);
			}
		}

		return classes;
	}

	/**
	 * Get the file name, including package.
	 * 
	 * @param classesDir
	 *            directory where the class is saved
	 * @param file
	 *            class file
	 * @return full class name
	 */
	public static String getClassNameFromFile(final File classesDir, File file) {
		String name = file.getPath().substring(classesDir.getPath().length() + 1).replace('/', '.').replace('\\', '.');
		name = name.substring(0, name.length() - 6);
		return name;
	}

	/**
	 * Search recursively within the given directory for files ending with one
	 * of the Strings.
	 * 
	 * @param dir
	 *            the directory to be searched
	 * @param endsWith
	 *            string array with the end's name.
	 * @return list of files ending with one of the Strings.
	 */
	public static List<File> findFilesEndingWith(final File dir, String[] endsWith) {
		List<File> classFiles = new ArrayList<File>();
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				classFiles.addAll(findFilesEndingWith(file, endsWith));
			} else if (endsWith(file, endsWith)) {
				classFiles.add(file);
			}
		}
		return classFiles;
	}

	/**
	 * Check if the file name name ends with one of the Strings. It is NOT case
	 * sensitive.
	 * 
	 * @param file
	 *            file to be checked
	 * @param endsWith
	 *            string array with the end's name.
	 */
	public static Boolean endsWith(File file, String[] endsWith) {
		Boolean goodFile = false;
		for (String end : endsWith) {
			if (file.getName().toLowerCase().endsWith(end.toLowerCase())) {
				goodFile = true;
				break;
			}
		}
		return goodFile;
	}

	/**
	 * Find the class's parent dir
	 * 
	 * @param clazz
	 *            the class
	 * @return new File object witch is the parent folder
	 */
	public static File findClassDir(Class<?> clazz) {
		try {
			String path = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
			return new File(URLDecoder.decode(path, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	}
	
	/**
	 * Search for a file within a directory.
	 * 
	 * @param directory the directory
	 * @param fileName the file name
	 * @return the file, or null
	 */
	public static File getFile(File directory, final String fileName) {
		File classesDir = null;
		File[] filesReturned = directory.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().equals(fileName);
			}
		});
		if (filesReturned.length == 1){
			classesDir = filesReturned[0];
		}
		return classesDir;
	}
	
	public static File createOrGetFolder(String basePath, String folderName){
		
		File folder = new File(basePath + System.getProperty("file.separator") + folderName);
		
		if(!folder.exists()){
			folder.mkdirs();
		}
		
		return folder;
	}
	
	public static File createOrGetHiddenFolder(String basePath, String folderName) throws IOException {
		
		if(OperationalSystemUtils.isLinux() && !folderName.startsWith(".")){
			folderName = "." + folderName;
		}
		
		File folder = new File(basePath + System.getProperty("file.separator") + folderName);
		
		if(!folder.exists()){
			folder.mkdirs();
		}
		
		if(OperationalSystemUtils.isWindows()){
			Runtime.getRuntime().exec(COMMAND_TO_MAKE_FILE_HIDDEN_ON_WINDOWS + folder.getAbsolutePath());
		}
		
		return folder;
	}
	
	public static void copyFile(File folderWhereToPutCopy, String fileToCopy) throws IOException {
		File originalFile = new File(fileToCopy);
		File copyFile = new File(
				folderWhereToPutCopy.getAbsolutePath() + OperationalSystemUtils.systemFileSeparator()
						+ originalFile.getName()
		);
		org.apache.commons.io.FileUtils.copyFile(originalFile, copyFile);
	}
	
	public static void copyFile(File folderWhereToPutCopy, InputStream fileToCopy, String name) throws IOException {
		
		File copyFile = new File(
				folderWhereToPutCopy.getAbsolutePath() + OperationalSystemUtils.systemFileSeparator()
						+ name
		);
		FileOutputStream fos = new FileOutputStream(copyFile);
		Files.copy(fileToCopy, fos);
		fileToCopy.close();
		fos.close();
	}
}
