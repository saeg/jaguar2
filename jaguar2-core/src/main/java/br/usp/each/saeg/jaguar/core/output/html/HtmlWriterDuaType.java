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

import br.usp.each.saeg.jaguar.codeforest.model.Requirement;
import br.usp.each.saeg.jaguar2.core.Jaguar;
import br.usp.each.saeg.jaguar2.core.heuristic.Heuristic;
import br.usp.each.saeg.jaguar.core.model.core.requirement.AbstractTestRequirement;
import br.usp.each.saeg.jaguar.core.utils.CodeUtils;
import br.usp.each.saeg.jaguar.core.utils.FileUtils;
import br.usp.each.saeg.jaguar.core.utils.OperationalSystemUtils;
import br.usp.each.saeg.jaguar.core.utils.TestRequirementUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class HtmlWriterDuaType {
	
	private static final String MAIN_CLASS_TABLE_HTML_TEMPLATE_PATH = "html-output/html/main-class-table-template.html";
	
	private static final String TEST_REQUIREMENT_CODE_HTML_TEMPLATE_PATH = "html-output/html/test-requirement-code-template.html";
	
	private static final String TEST_REQUIREMENT_TABLE_HTML_TEMPLATE_PATH = "html-output/html/test-requirement-table-template.html";
	
	public static final String REPORT_DATE = "$reportDate$";
	
	private final HtmlBuilder htmlBuilder;
	
	private final File projectDirectory;
	
	private final List<AbstractTestRequirement> testRequirementsList;
	
	private final Requirement.Type requirementType;
	
	private final Heuristic heuristic;
	
	private final String outputFolder;
	
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
	
	public HtmlWriterDuaType(
			HtmlBuilder htmlBuilder,
			File projectDirectory,
			List<AbstractTestRequirement> testRequirementsList,
			Requirement.Type requirementType,
			Heuristic heuristic,
			String outputFolder
	) {
		this.htmlBuilder = htmlBuilder;
		this.projectDirectory = projectDirectory;
		this.testRequirementsList = testRequirementsList;
		this.requirementType = requirementType;
		this.heuristic = heuristic;
		this.outputFolder = outputFolder;
	}
	
	public File write() throws IOException {
		
		File reportsFolder = FileUtils.createOrGetFolder(projectDirectory.getAbsolutePath(), outputFolder);
		File subHtmlFolder = FileUtils.createOrGetHiddenFolder(reportsFolder.getAbsolutePath(), HtmlWriter.HTML_FILES_FOLDER_NAME);
		File duaFolder = FileUtils.createOrGetFolder(subHtmlFolder.getAbsolutePath(),"dua");
		
		HtmlWriter.writeCssFiles(subHtmlFolder, HtmlWriter.CSS_FILES_FOLDER_NAME);
		HtmlWriter.writeImgFiles(subHtmlFolder, HtmlWriter.IMG_FILES_FOLDER_NAME);
		HtmlWriter.writeJsFiles(subHtmlFolder, HtmlWriter.JS_FILES_FOLDER_NAME);
		
		MultiValuedMap<File, AbstractTestRequirement> requirementsGroupByClassFile = TestRequirementUtils
				.testRequirementsGroupByClassFile(testRequirementsList, projectDirectory.getAbsolutePath());
		
		File mainHtml = new File(reportsFolder.getAbsolutePath() + OperationalSystemUtils.systemFileSeparator() +
				"index" + HtmlWriter.HTML_TYPE_FOR_FILE
		);
		
		Map<File, File> htmlFileMapByClassFile = new HashMap<>();
		
		for (File classFile : requirementsGroupByClassFile.keySet()) {
			htmlFileMapByClassFile.put(
					classFile,
					createHtmlforClass(duaFolder, classFile, requirementsGroupByClassFile.get(classFile), mainHtml.getAbsolutePath())
			);
		}
		
		org.apache.commons.io.FileUtils.writeStringToFile(
				mainHtml,
				buildContentForMainHtml(htmlFileMapByClassFile, requirementsGroupByClassFile)
		);
		
		return mainHtml;
		
	}
	
	private File createHtmlforClass(File subHtmlFolder, File classFile, Collection<AbstractTestRequirement> abstractTestRequirementsForThisClass, String mainHtmlAbsolutePath) throws IOException {
		
		String classNameWithPath = abstractTestRequirementsForThisClass.iterator().next().getClassName();
		String classNameToHtmlFile = classNameWithPath.replace(OperationalSystemUtils.systemFileSeparator(), "-");
		
		File htmlWithTableForTestRequirements = new File(
				subHtmlFolder.getAbsolutePath()
						+ OperationalSystemUtils.systemFileSeparator()
						+ classNameToHtmlFile
						+ HtmlWriter.HTML_TYPE_FOR_FILE
		);
		
		String[] nameBreakBySeparator = abstractTestRequirementsForThisClass.iterator().next().getClassName().split(
				OperationalSystemUtils.systemFileSeparator()
		);
		String classNameOnly = nameBreakBySeparator[nameBreakBySeparator.length - 1];
		
		Map<AbstractTestRequirement, File> htmlFileMapByTestRequirement = createHtmlFileWithCodeForEachTestRequirement(
				classFile,
				subHtmlFolder,
				classNameToHtmlFile,
				abstractTestRequirementsForThisClass,
				mainHtmlAbsolutePath,
				htmlWithTableForTestRequirements
		);
		
		org.apache.commons.io.FileUtils.writeStringToFile(
				htmlWithTableForTestRequirements,
				buildContentForHtmlTestRequirementsTableFile(
						classNameOnly,
						classNameWithPath,
						htmlFileMapByTestRequirement,
						mainHtmlAbsolutePath
				)
		);
		
		return htmlWithTableForTestRequirements;
	}
	
	private Map<AbstractTestRequirement, File> createHtmlFileWithCodeForEachTestRequirement(
			File classFile,
			File subHtmlFolder,
			String classNameToHtmlFile,
			Collection<AbstractTestRequirement> abstractTestRequirementsForThisClass,
			String mainHtmlAbsolutePath,
			File htmlWithTableForTestRequirements) throws IOException {
		
		Map<AbstractTestRequirement, File> htmlFileMapByTestRequirement = new HashMap<>();
		
		String javaCodeInString = CodeUtils.getCodeFromAbsolutePath(classFile);
		
		for(AbstractTestRequirement abstractTestRequirement : abstractTestRequirementsForThisClass){
			
			File htmlWithCodePaintedForThisTestRequirement = new File(
					subHtmlFolder.getAbsolutePath()
							+ OperationalSystemUtils.systemFileSeparator()
							+ classNameToHtmlFile
							+ "-code-"
							+ abstractTestRequirement.getUuid()
							+ HtmlWriter.HTML_TYPE_FOR_FILE
			);
			
			org.apache.commons.io.FileUtils.writeStringToFile(
					htmlWithCodePaintedForThisTestRequirement,
					buildContentForHtmlCodeFile(
							javaCodeInString,
							htmlWithTableForTestRequirements.getAbsolutePath(),
							abstractTestRequirement,
							mainHtmlAbsolutePath
					)
			);
			
			htmlFileMapByTestRequirement.put(abstractTestRequirement, htmlWithCodePaintedForThisTestRequirement);
		}
		
		return htmlFileMapByTestRequirement;
	}
	
	public String buildContentForHtmlCodeFile(String javaCode, String pathToHtmlWithTableForRequirements, AbstractTestRequirement abstractTestRequirement, String mainHtmlAbsolutePath) throws IOException {
		InputStream stream = HtmlWriter.getResource(TEST_REQUIREMENT_CODE_HTML_TEMPLATE_PATH);
		String htmlTemplateForTestRequirement = htmlBuilder.getStringFromHtmlTemplate(stream);
		stream.close();
		
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$mainHtmlPath$", mainHtmlAbsolutePath);
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$classNameWithPath$", testRequirementsList.iterator().next().getClassName());
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$htmlTable$", pathToHtmlWithTableForRequirements);
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$javaCode$", htmlBuilder.transformJavaCodeToHtml(javaCode, abstractTestRequirement));
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace(REPORT_DATE,  simpleDateFormat.format(new Date()));
		
		return htmlTemplateForTestRequirement;
	}
	
	public String buildContentForHtmlTestRequirementsTableFile(
			String className,
			String classNameWithPath,
			Map<AbstractTestRequirement, File> htmlFileMapByTestRequirement,
			String mainHtmlAbsolutePath
	) throws IOException {
		InputStream stream = HtmlWriter.getResource(TEST_REQUIREMENT_TABLE_HTML_TEMPLATE_PATH);
		String htmlTemplateForTestRequirement = htmlBuilder.getStringFromHtmlTemplate(stream);
		stream.close();
		
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$mainHtmlPath$", mainHtmlAbsolutePath);
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$classNameWithPath$", classNameWithPath);
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$heuristic$", StringUtils.upperCase(
				StringUtils.removeEndIgnoreCase(
						heuristic.getClass().getSimpleName(), "heuristic"
				)
		));
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$requirementType$", requirementType.toString());
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$numberOfTests$", String.valueOf(Jaguar.nTests));
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$numberOfFailTests$", String.valueOf(Jaguar.nTestsFailed));
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$subTitle$", className);
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace(
				"$allTableDataForTestRequirements$",
				htmlBuilder.buildTDTagForTestRequirementList(htmlFileMapByTestRequirement));
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace(REPORT_DATE,  simpleDateFormat.format(new Date()));
		
		return htmlTemplateForTestRequirement;
	}
	
	public String buildContentForMainHtml(Map<File, File> htmlFileMapByClassFile, MultiValuedMap<File, AbstractTestRequirement> requirementsGroupByClassFile) throws IOException {
		InputStream stream = HtmlWriter.getResource(MAIN_CLASS_TABLE_HTML_TEMPLATE_PATH);
		String htmlTemplateForTestRequirement = htmlBuilder.getStringFromHtmlTemplate(stream);
		stream.close();
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$heuristic$", StringUtils.upperCase(
				StringUtils.removeEndIgnoreCase(
						heuristic.getClass().getSimpleName(), "heuristic"
				)
		));
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$requirementType$", requirementType.toString());
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$numberOfTests$", String.valueOf(Jaguar.nTests));
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$numberOfFailTests$", String.valueOf(Jaguar.nTestsFailed));
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace("$allTableDataForClassFiles$", htmlBuilder.buildTDTagForClassList(htmlFileMapByClassFile, requirementsGroupByClassFile));
		htmlTemplateForTestRequirement = htmlTemplateForTestRequirement.replace(REPORT_DATE,  simpleDateFormat.format(new Date()));
		
		return htmlTemplateForTestRequirement;
	}
}
