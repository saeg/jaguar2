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
package br.usp.each.saeg.jaguar2.jacoco;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.jacoco.agent.rt.IAgent;
import org.jacoco.agent.rt.RT;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.analysis.ILine;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.analysis.ISourceNode;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataReader;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.ISessionInfoVisitor;
import org.jacoco.core.data.SessionInfo;

import br.usp.each.saeg.jaguar2.commons.ClassFiles;
import br.usp.each.saeg.jaguar2.spi.CoverageController;

public class JaCoCoController implements CoverageController {

    private final IAgent agent;

    private final List<ExecutionDataStore> failExecutionDataStores;

    private final List<ExecutionDataStore> successExecutionDataStores;

    private File classesDir;

    private ClassFiles classFiles;

    public JaCoCoController(final IAgent agent) {
        this.agent = agent;
        failExecutionDataStores = new LinkedList<ExecutionDataStore>();
        successExecutionDataStores = new LinkedList<ExecutionDataStore>();
    }

    public JaCoCoController() {
        this(RT.getAgent());
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
         * JaCoCo's execution data.
         *
         * Don't reset as it will be done before next test start.
         */
        final byte[] executionData = agent.getExecutionData(false);

        /*
         * Instantiate a reader that handles JaCoCo's internal binary format.
         */
        final ExecutionDataReader reader = new ExecutionDataReader(
                new ByteArrayInputStream(executionData));

        /*
         * We don't need session info.
         */
        reader.setSessionInfoVisitor(new ISessionInfoVisitor() {
            @Override
            public void visitSessionInfo(final SessionInfo info) {
            }
        });

        /*
         * But the execution data store we will save for further analysis.
         */
        final ExecutionDataStore executionDataStore = new ExecutionDataStore();
        reader.setExecutionDataVisitor(executionDataStore);

        try {
            /*
             * Accordingly to JaCoCo's JavaDoc:
             *
             * The read method will return `true` if additional data can be
             * expected.
             *
             * Inspecting the source code, looks that it will always return
             * `false`.
             */
            if (reader.read()) {
                throw new IOException("This is not supposed to happen.");
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

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

        final CoverageBuilder coverageBuilder = new CoverageBuilder();
        final Analyzer analyzer = new Analyzer(executionDataStore, coverageBuilder);

        for (final ExecutionData data : executionDataStore.getContents()) {
            final String vmClassName = data.getName();
            final File classFile = classFiles.get(vmClassName);
            if (classFile != null) {
                try {
                    analyzer.analyzeAll(classFile);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (final IClassCoverage classCoverage : coverageBuilder.getClasses()) {
            for (final IMethodCoverage methodCoverage : classCoverage.getMethods()) {

            }
        }
    }

    private static List<Integer> coveredLines(final ISourceNode coverage) {
        final List<Integer> coveredLines = new ArrayList<Integer>();
        for (int nr = coverage.getFirstLine(); nr <= coverage.getLastLine(); nr++) {
            final ILine line = coverage.getLine(nr);
            switch (line.getInstructionCounter().getStatus()) {
            case ICounter.FULLY_COVERED:
            case ICounter.PARTLY_COVERED:
                coveredLines.add(nr);
                break;
            case ICounter.NOT_COVERED:
                break;
            default:
                continue;
            }
        }
        return coveredLines;
    }

}
