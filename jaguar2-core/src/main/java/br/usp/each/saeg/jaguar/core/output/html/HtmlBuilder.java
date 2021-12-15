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

import br.usp.each.saeg.jaguar.core.model.core.requirement.AbstractTestRequirement;
import br.usp.each.saeg.jaguar.core.model.core.requirement.DuaTestRequirement;
import br.usp.each.saeg.jaguar.core.model.core.requirement.LineTestRequirement;
import br.usp.each.saeg.jaguar.core.utils.StringUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.*;

import static br.usp.each.saeg.jaguar.core.output.html.HtmlDomTree.*;

public class HtmlBuilder {
	
	private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
	
	public static final String ALIGN_RIGHT = "align-right";
	
	static {
			numberFormat.setMaximumFractionDigits(2);
			numberFormat.setMinimumFractionDigits(2);
	}
	
	public String transformJavaCodeToHtml(String javaCode, AbstractTestRequirement abstractTestRequirement) {
		return transformJavaCodeToHtml(javaCode, Collections.singleton(abstractTestRequirement));
	}
	
	public String transformJavaCodeToHtml(String javaCode, Collection<AbstractTestRequirement> requirementsForThisClass) {
		List<String> rowsInJavaCode = Arrays.asList(javaCode.split("\\n"));
		
		HtmlTagBuilder olHtmlTagBuilder = new HtmlTagBuilder(OL);
		olHtmlTagBuilder.setCssClass("linenumbers");
		
		for (int rowIndex = 0; rowIndex < rowsInJavaCode.size() - 1; rowIndex++) {
			
			final int rowNumber = rowIndex + 1 /* To use in lambda need to be final */;

			String codeLine = rowsInJavaCode.get(rowIndex).replaceAll("<(?=\\w)", "$0 ");
			
			Optional<AbstractTestRequirement> optionalTestRequirementWithMostHighSuspeciosForThisLine = requirementsForThisClass.stream()
					.filter(abstractTestRequirement -> {
						if (abstractTestRequirement instanceof LineTestRequirement) {
							LineTestRequirement lineTestRequirement = (LineTestRequirement) abstractTestRequirement;
							return Objects.equals(lineTestRequirement.getLineNumber(), rowNumber);
						} else {
							DuaTestRequirement duaTestRequirement = (DuaTestRequirement) abstractTestRequirement;
							return
									Objects.equals(duaTestRequirement.getDef(), rowNumber)
											|| Objects.equals(duaTestRequirement.getUse(), rowNumber)
											|| Objects.equals(duaTestRequirement.getTarget(), rowNumber)
									;
						}
					}).max(Comparator.comparingDouble(AbstractTestRequirement::getSuspiciousness));
			
			if (optionalTestRequirementWithMostHighSuspeciosForThisLine.isPresent()) {
				
				AbstractTestRequirement requirement = optionalTestRequirementWithMostHighSuspeciosForThisLine.get();
				Double currentSuspiciousness = requirement.getSuspiciousness();
				String cssClass = getSuspiciousnessCssColor(currentSuspiciousness);

				codeLine = new HtmlTagBuilder(SPAN)
						.addCssClass(cssClass)
						.setId(requirement.getUuid().toString())
						.setAriaLabel("Suspiciousness of this line is " + currentSuspiciousness)
						.setData("suspiciousness", String.valueOf(currentSuspiciousness))
						.setInnerHtml(new HtmlTagBuilder(ABBR)
										.setTitle("Suspiciousness of this line is " + currentSuspiciousness)
										.setInnerHtml(codeLine).build()
									)
						.build()
				;
			}
			
			String newListItem = new HtmlTagBuilder(LI)
					.setInnerHtml(codeLine)
					.setSiblingHtml(System.lineSeparator())
					.build();
			
			olHtmlTagBuilder.addInnerHtml(newListItem);
		}
		
		return new HtmlTagBuilder(PRE)
				.setInnerHtml(
						new HtmlTagBuilder(CODE)
								.setCssClass("language-java")
								.setInnerHtml(olHtmlTagBuilder.build())
								.build()
				).build();
	}
	
	public String getSuspiciousnessCssColor(double suspiciousness) {
		if (suspiciousness >= 0.75D) {
			return "suspiciousness-more-than-or-equal-75";
		} else if (suspiciousness >= 0.5D) {
			return "suspiciousness-more-than-or-equal-50";
		} else if (suspiciousness >= 0.25D) {
			return "suspiciousness-more-than-or-equal-25";
		} else {
			return "suspiciousness-below-25";
		}
	}
	
	public String buildTDTagForClassList(Map<File, File> htmlFileMapByClassFile, MultiValuedMap<File, AbstractTestRequirement> requirementsGroupByClassFile) {
		
		StringBuilder allTableDatas = new StringBuilder();
		
		for (Map.Entry<File, File> classFileAndHtmlClassFile : htmlFileMapByClassFile.entrySet()) {
			allTableDatas.append(
					buildTDTagForClassFile(
							classFileAndHtmlClassFile.getValue(),
							requirementsGroupByClassFile.get(classFileAndHtmlClassFile.getKey()))
			);
		}
		
		return allTableDatas.toString();
	}
	
	public String buildTDTagForClassFile(File htmlClassFile, Collection<AbstractTestRequirement> testRequirementsForTheClass) {
		String htmlClassFileAbsolutePath = htmlClassFile.getAbsolutePath();
		
		AbstractTestRequirement abstractTestRequirementWithHigherSuspiciousness = testRequirementsForTheClass
				.stream()
				.max(Comparator.comparingDouble(AbstractTestRequirement::getSuspiciousness))
				.orElseThrow(NullPointerException::new);

		double suspValue = abstractTestRequirementWithHigherSuspiciousness.getSuspiciousness();
		
		return new HtmlTagBuilder(TR).addInnerHtml(
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(htmlClassFileAbsolutePath)
										.addInnerHtml(abstractTestRequirementWithHigherSuspiciousness.getClassName())
										.build()
						)
						.build(),
				new HtmlTagBuilder(TD) //add bar
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(htmlClassFileAbsolutePath)
										.addInnerHtml(
												new HtmlTagBuilder(DIV)
														.setCssClass("suspBar")
														.addInnerHtml(
																new HtmlTagBuilder(DIV)
																		.setCssClass("redBar")
																		.setInlineStyle(defineSuspBarCss((int) Math.round((suspValue + 0.1) * 10)))
																		.build()
														).build()
										).build()
						)
						.build(),
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(htmlClassFileAbsolutePath)
										.addCssClass(ALIGN_RIGHT)
										.addInnerHtml(
												numberFormat.format(suspValue)
										)
										.build()
						)
						.build(),
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(htmlClassFileAbsolutePath)
										.addCssClass(ALIGN_RIGHT)
										.addInnerHtml(
												numberFormat.format(abstractTestRequirementWithHigherSuspiciousness.getCef())
										)
										.build()
						)
						.build(),
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(htmlClassFileAbsolutePath)
										.addCssClass(ALIGN_RIGHT)
										.addInnerHtml(
												numberFormat.format(abstractTestRequirementWithHigherSuspiciousness.getCep())
										)
										.build()
						)
						.build(),
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(htmlClassFileAbsolutePath)
										.addCssClass(ALIGN_RIGHT)
										.addInnerHtml(
												numberFormat.format(abstractTestRequirementWithHigherSuspiciousness.getCnf())
										)
										.build()
						)
						.build(),
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(htmlClassFileAbsolutePath)
										.addCssClass(ALIGN_RIGHT)
										.addInnerHtml(
												numberFormat.format(abstractTestRequirementWithHigherSuspiciousness.getCnp())
										)
										.build()
						)
						.build()
		).build();
	}
	
	public String buildTDTagForTestRequirementList(Map<AbstractTestRequirement, File> htmlFileMapByTestRequirement) {
		StringBuilder allTableDatas = new StringBuilder();
		
		for (Map.Entry<AbstractTestRequirement, File> abstractTestRequirementFileEntry : htmlFileMapByTestRequirement.entrySet()) {
			allTableDatas.append(
					buildTDTagForTestRequirement(abstractTestRequirementFileEntry.getKey(), abstractTestRequirementFileEntry.getValue().getAbsolutePath())
			);
		}
		
		return allTableDatas.toString();
	}
	
	public String buildTDTagForTestRequirementList(Collection<AbstractTestRequirement> abstractTestRequirementList, String linkToAnchor) {
		StringBuilder allTableDatas = new StringBuilder();
		
		for (AbstractTestRequirement abstractTestRequirement : abstractTestRequirementList) {
			allTableDatas.append(
					buildTDTagForTestRequirement(abstractTestRequirement, linkToAnchor)
			);
		}
		
		return allTableDatas.toString();
	}
	
	public String buildTDTagForTestRequirement(AbstractTestRequirement abstractTestRequirement, String linkToAnchor) {
		String contentForLocation;
		
		if (abstractTestRequirement instanceof DuaTestRequirement) {
			DuaTestRequirement duaRequirement = (DuaTestRequirement) abstractTestRequirement;
			
			contentForLocation =
					duaRequirement.getDef()
							+ "("
							+ StringUtils.concateStringsWithSeparatorBetween(
									String.valueOf(duaRequirement.getUse()))
							+ ")";
		} else {
			LineTestRequirement lineRequirement = (LineTestRequirement) abstractTestRequirement;
			contentForLocation = String.valueOf(lineRequirement.getLineNumber());
		}

		double suspValue = abstractTestRequirement.getSuspiciousness();
		String href = linkToAnchor + "#" + abstractTestRequirement.getUuid();
		
		return new HtmlTagBuilder(TR).addInnerHtml(
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(href)
										.addInnerHtml(
											contentForLocation
										)
										.build()
						)
						.build(),
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(href)
										.addInnerHtml(abstractTestRequirement.getMethodSignature())
										.build()
						)
						.build(),
				new HtmlTagBuilder(TD) //add bar
						.setInnerHtml(
								
								new HtmlTagBuilder(A)
										.setHref(href)
										.addInnerHtml(
												new HtmlTagBuilder(DIV)
														.setCssClass("suspBar")
														.addInnerHtml(
																new HtmlTagBuilder(DIV)
																		.setCssClass("redBar")
																		.setInlineStyle(defineSuspBarCss((int) Math.round((suspValue + 0.1) * 10)))
																		.build()
														).build()
										).build()
						)
						.build(),
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(href)
										.addCssClass(ALIGN_RIGHT)
										.addInnerHtml(numberFormat.format(suspValue))
										.build()
						)
						.build(),
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(href)
										.addCssClass(ALIGN_RIGHT)
										.addInnerHtml(numberFormat.format(abstractTestRequirement.getCef()))
										.build()
						)
						.build(),
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(href)
										.addCssClass(ALIGN_RIGHT)
										.addInnerHtml(numberFormat.format(abstractTestRequirement.getCep()))
										.build()
						)
						.build(),
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(href)
										.addCssClass(ALIGN_RIGHT)
										.addInnerHtml(numberFormat.format(abstractTestRequirement.getCnf()))
										.build()
						)
						.build(),
				new HtmlTagBuilder(TD)
						.setInnerHtml(
								new HtmlTagBuilder(A)
										.setHref(href)
										.addCssClass(ALIGN_RIGHT)
										.addInnerHtml(numberFormat.format(abstractTestRequirement.getCnp()))
										.build()
						)
						.build()
		).build();
	}
	
	public String getStringFromHtmlTemplate(String templatePath) throws IOException {
		File htmlTemplateFile = new File(templatePath);
		return FileUtils.readFileToString(htmlTemplateFile);
	}
	
	public String getStringFromHtmlTemplate(InputStream inputStream) throws IOException {
		return IOUtils.toString(inputStream);
	}

	public String defineSuspBarCss(int value){
		if (value <= 1)
			return "display: none";
		return "grid-column-end: " + value;
	}
}
