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
package br.usp.each.saeg.jaguar2.badua;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import br.usp.each.saeg.badua.agent.rt.internal_d2401f0.Agent;
import br.usp.each.saeg.badua.agent.rt.internal_d2401f0.core.data.ExecutionData;
import br.usp.each.saeg.badua.agent.rt.internal_d2401f0.core.data.IExecutionDataVisitor;
import br.usp.each.saeg.badua.agent.rt.internal_d2401f0.core.runtime.RuntimeData;
import br.usp.each.saeg.badua.core.analysis.Analyzer;
import br.usp.each.saeg.badua.core.analysis.ClassCoverage;
import br.usp.each.saeg.badua.core.analysis.ICoverageVisitor;
import br.usp.each.saeg.badua.core.analysis.MethodCoverage;
import br.usp.each.saeg.badua.core.analysis.SourceLineDefUseChain;
import br.usp.each.saeg.badua.core.data.ExecutionDataStore;
import br.usp.each.saeg.jaguar2.commons.ClassFiles;
import br.usp.each.saeg.jaguar2.spi.CoverageController;

public class BaDuaController implements CoverageController {

    private final Agent agent;

    private final List<ExecutionDataStore> failExecutionDataStores;

    private final List<ExecutionDataStore> successExecutionDataStores;

    private File classesDir;

    private ClassFiles classFiles;

    public BaDuaController(final Agent agent) {
        this.agent = agent;
        failExecutionDataStores = new LinkedList<ExecutionDataStore>();
        successExecutionDataStores = new LinkedList<ExecutionDataStore>();
    }

    public BaDuaController() {
        this(Agent.getInstance());
    }

    @Override
    public void init() {
        final Properties props = System.getProperties();
        final String jaguar2ClassesProp = props.getProperty("jaguar2.classes");
        classesDir = new File(jaguar2ClassesProp);
    }

    @Override
    public void reset() {
        agent.reset();
    }

    @Override
    public void save(final boolean testFailed) {
        /*
         * BA-DUA's runtime data.
         */
        final RuntimeData runtimeData = agent.getData();

        /*
         * Convert to BA-DUA core API
         *
         * TODO: Fix this when a public API is available!
         *
         * We a re using BA-DUA agent runtime internal classes and currently
         * an agent public API isn't provided.
         */
        final ExecutionDataStore executionDataStore = new ExecutionDataStore();
        runtimeData.collect(new IExecutionDataVisitor() {
            @Override
            public void visitClassExecution(final ExecutionData data) {
                executionDataStore.visitClassExecution(
                        new br.usp.each.saeg.badua.core.data.ExecutionData(
                                data.getId(), data.getName(),
                                data.getData().clone()));
            }
        });

        if (testFailed) {
            failExecutionDataStores.add(executionDataStore);
        } else {
            successExecutionDataStores.add(executionDataStore);
        }
    }

    @Override
    public void analyze() {
        classFiles = new ClassFiles(classesDir);
        for (final ExecutionDataStore executionDataStore : failExecutionDataStores) {
            analyzeLinesCoveredByTest(executionDataStore, true);
        }
        for (final ExecutionDataStore executionDataStore : successExecutionDataStores) {
            analyzeLinesCoveredByTest(executionDataStore, false);
        }
    }

    private void analyzeLinesCoveredByTest(
            final ExecutionDataStore executionDataStore, final boolean testFailed) {

        final Analyzer analyzer = new Analyzer(executionDataStore, new ICoverageVisitor() {

			@Override
			public void visitCoverage(final ClassCoverage coverage) {
		        for (final MethodCoverage methodCoverage : coverage.getMethods()) {
		            for (final SourceLineDefUseChain defUse : methodCoverage.getDefUses()) {

		            }
		        }
			}

		});
        for (final br.usp.each.saeg.badua.core.data.ExecutionData data : executionDataStore.getContents()) {
            final String vmClassName = data.getName();
            final File classFile = classFiles.get(vmClassName);
            if (classFile != null) {
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(classFile);
                    analyzer.analyzeAll(inputStream, classFile.getPath());
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (final IOException ee) {
                            throw new RuntimeException(ee);
                        }
                    }
                }
            }
        }
    }

}
