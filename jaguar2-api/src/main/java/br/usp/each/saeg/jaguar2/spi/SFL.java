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
package br.usp.each.saeg.jaguar2.spi;

import org.jacoco.core.analysis.IClassCoverage;

import br.usp.each.saeg.badua.core.analysis.SourceLineDefUseChain;

public interface SFL {

	void updateRequirement(String className, String methodDesc, String methodName, int methodId, int duaIndex,
			SourceLineDefUseChain dua, boolean failed);

	void updateRequirement(IClassCoverage clazz, int lineNumber, boolean failed);

}
