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
package br.usp.each.saeg.jaguar.core;

import java.util.Collection;
import java.util.HashMap;

import org.eclipse.jdt.core.Signature;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;

import br.usp.each.saeg.badua.core.analysis.SourceLineDefUseChain;
import br.usp.each.saeg.jaguar.core.model.core.requirement.AbstractTestRequirement;
import br.usp.each.saeg.jaguar.core.model.core.requirement.DuaTestRequirement;
import br.usp.each.saeg.jaguar.core.model.core.requirement.LineTestRequirement;
import br.usp.each.saeg.jaguar2.spi.SFL;

/**
 * This class store the SFL coverage information.
 * 
 * @author Henrique Ribeiro
 */
public class JaguarSFL implements SFL {


	private HashMap<AbstractTestRequirement, AbstractTestRequirement> testRequirements = new HashMap<AbstractTestRequirement, AbstractTestRequirement>();

	/**
	 * Update the testRequirement info. If it does not exist, create a new one.
	 * If the test has failed, increment the cef (executed and failed coefficient)
	 * If the test has passed, increment the cep (executed and passed coefficient)
	 * 
	 * @param clazz
	 *            the class name, including package
	 * @param lineNumber
	 *            the line number
	 * @param failed
	 *            if the test has failed
	 * 
	 */
	@Override
	public void updateRequirement(
			String className, 
			String methodDesc, 
			String methodName, 
			int methodId, 
			int duaIndex, 
			SourceLineDefUseChain dua, boolean failed) {

		AbstractTestRequirement testRequirement = new DuaTestRequirement(className, duaIndex, dua.def, dua.use,
				dua.target, dua.var);
		AbstractTestRequirement foundRequirement = testRequirements.get(testRequirement);
		
		if (foundRequirement == null) {
			testRequirement.setClassFirstLine(0);
			testRequirement.setMethodLine(dua.def);
			
			String methodSignature = Signature.toString(methodDesc, methodName, null, false, true);		
			testRequirement.setMethodSignature(extractName(methodSignature, className));
			testRequirement.setMethodId(methodId);
			testRequirements.put(testRequirement, testRequirement);
		} else {
			testRequirement = foundRequirement;
		}

		if (failed) {
			testRequirement.increaseFailed();
		} else {
			testRequirement.increasePassed();
		}

	}


	/**
	 * Update the testRequirement info. If it does not exist, create a new one.
	 * If the test has failed, increment the cef (coefficient of executed and
	 * failed) If the test has passed, increment the cep (coefficient of
	 * executed and passed)
	 * 
	 * @param clazz
	 *            the class name, including package
	 * @param lineNumber
	 *            the line number
	 * @param failed
	 *            if the test has failed
	 * 
	 */
	@Override
	public void updateRequirement(IClassCoverage clazz, int lineNumber, boolean failed) {
		AbstractTestRequirement testRequirement = new LineTestRequirement(clazz.getName(), lineNumber);
		AbstractTestRequirement foundRequirement = testRequirements.get(testRequirement);

		if (foundRequirement == null) {
			testRequirement.setClassFirstLine(clazz.getFirstLine());
			Collection<IMethodCoverage> methods = clazz.getMethods();
			Integer methodId = 0;
			for (IMethodCoverage method : methods) {
				methodId++;
				if (method.getLine(lineNumber) != org.jacoco.core.internal.analysis.LineImpl.EMPTY) {
					testRequirement.setMethodLine(method.getFirstLine());
					String methodSignature = Signature.toString(method.getDesc(), method.getName(), null, false, true);
					testRequirement.setMethodSignature(extractName(methodSignature, clazz.getName()));
					testRequirement.setMethodId(methodId);
					break;
				}
			}
			testRequirements.put(testRequirement, testRequirement);
		} else {
			testRequirement = foundRequirement;
		}

		if (failed) {
			testRequirement.increaseFailed();
		} else {
			testRequirement.increasePassed();
		}
	}

	/**
	 * Remove the return value and replace method name by Class name if it is
	 * init();
	 * 
	 * @param methodName
	 *            method complete signature
	 * @param className
	 * @return method name without return value
	 */
	private String extractName(String methodName, String className) {
		methodName = methodName.substring(methodName.indexOf(" ") + 1);
		if (methodName.equals("<init>()")) {
			String[] classNameSplited = className.split("/");
			methodName = classNameSplited[classNameSplited.length - 1] + "()";
		}
		return methodName;
	}


	/**
	 * @return the testRequirements
	 */
	public HashMap<AbstractTestRequirement, AbstractTestRequirement> getTestRequirements() {
		return testRequirements;
	}


	/**
	 * @param testRequirements the testRequirements to set
	 */
	public void setTestRequirements(HashMap<AbstractTestRequirement, AbstractTestRequirement> testRequirements) {
		this.testRequirements = testRequirements;
	}
	
}
