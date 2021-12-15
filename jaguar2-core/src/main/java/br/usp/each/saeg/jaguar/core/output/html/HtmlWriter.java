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
package br.usp.each.saeg.jaguar.core.output.html;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import br.usp.each.saeg.jaguar.codeforest.model.Requirement;
import br.usp.each.saeg.jaguar.core.model.core.requirement.AbstractTestRequirement;
import br.usp.each.saeg.jaguar.core.utils.FileUtils;
import br.usp.each.saeg.jaguar2.core.heuristic.Heuristic;

public class HtmlWriter {
	
	public static final String HTML_FILES_FOLDER_NAME = ".html_folder";
	public static final String CSS_FILES_FOLDER_NAME = ".css_folder";
	public static final String IMG_FILES_FOLDER_NAME = ".img_folder";
	public static final String JS_FILES_FOLDER_NAME = ".js_folder";
	public static final String HTML_TYPE_FOR_FILE = ".html";
	
	private final HtmlBuilder htmlBuilder;
	private final List<AbstractTestRequirement> testRequirements;
	private Requirement.Type testRequirementType;
	private final Heuristic heuristic;
	private final File projectDirectory;
	private final String outputFile;
	
	public HtmlWriter(
			HtmlBuilder htmlBuilder,
			List<AbstractTestRequirement> testRequirements,
			Requirement.Type testRequirementType,
			Heuristic heuristic,
			File projectDirectory,
			String outputFile
	) {
		this.htmlBuilder = htmlBuilder;
		this.testRequirements = testRequirements;
		this.testRequirementType = testRequirementType;
		this.heuristic = heuristic;
		this.projectDirectory = projectDirectory;
		this.outputFile = outputFile;
	}
	
	public void generateHtmlForLineType() throws IOException {
		new HtmlWriterLineType(
				htmlBuilder,
				projectDirectory,
				testRequirements,
				testRequirementType,
				heuristic,
				outputFile
		).write();
	}
	
	public void generateHtmlForDuaType() throws IOException {
		new HtmlWriterDuaType(
				htmlBuilder,
				projectDirectory,
				testRequirements,
				testRequirementType,
				heuristic,
				outputFile
		).write();
	}
	
	public static void writeImgFiles(File subHtmlFolder, String imgFilesFolderName) throws IOException {

		File imgFolder = FileUtils.createOrGetFolder(subHtmlFolder.getAbsolutePath(), imgFilesFolderName);

		FileUtils.copyFile(imgFolder, getResource("html-output/img/jaguar-icon.png"), "jaguar-icon.png");

	}
	
	public static void writeCssFiles(File subHtmlFolder, String cssFilesFolderName) throws IOException {
		
		File cssFolder = FileUtils.createOrGetFolder(subHtmlFolder.getAbsolutePath(), cssFilesFolderName);

		
		FileUtils.copyFile(cssFolder, getResource("html-output/css/stackoverflow-light.css"), "stackoverflow-light.css");
		
		FileUtils.copyFile(cssFolder, getResource("html-output/css/style.css"), "style.css");
		
		FileUtils.copyFile(cssFolder, getResource("html-output/css/test-requirement-code-template.css"), "test-requirement-code-template.css");
		
		FileUtils.copyFile(cssFolder, getResource("html-output/css/test-requiremente-table-template.css"), "test-requiremente-table-template.css");
	}

	public static void writeJsFiles(File subHtmlFolder, String jsFilesFolderName) throws IOException {

		File jsFolder = FileUtils.createOrGetFolder(subHtmlFolder.getAbsolutePath(), jsFilesFolderName);

		FileUtils.copyFile(jsFolder, getResource("html-output/js/table-controls.js"), "table-controls.js");

		FileUtils.copyFile(jsFolder, getResource("html-output/js/focus-code-line.js"), "focus-code-line.js");

		FileUtils.copyFile(jsFolder, getResource("html-output/js/highlight.min.js"), "highlight.min.js");

	}
	
	public static InputStream getResource(String resourceName) throws IOException {
		return HtmlWriter.class.getClassLoader().getResourceAsStream(resourceName);
	}

	
	
}
