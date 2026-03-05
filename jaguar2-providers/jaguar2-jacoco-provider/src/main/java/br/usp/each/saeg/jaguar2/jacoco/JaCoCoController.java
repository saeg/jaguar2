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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jacoco.agent.rt.IAgent;
import org.jacoco.agent.rt.RT;
import org.jacoco.core.analysis.*;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataReader;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.ISessionInfoVisitor;
import org.jacoco.core.data.SessionInfo;

import br.usp.each.saeg.jaguar2.commons.ClassFilesController;
import br.usp.each.saeg.jaguar2.spi.CoverageController;

/**
 * A {@link CoverageController} service implementation that interacts
 * with JaCoCo.
 */
public class JaCoCoController extends ClassFilesController implements CoverageController {

    private final IAgent agent;

    private final List<ExecutionDataStore> failExecutionDataStores;

    private final List<ExecutionDataStore> successExecutionDataStores;

    private SpectrumBuilder spectrumBuilder;

    /**
     * Instantiate a {@link JaCoCoController} for a given agent instance
     * of the JaCoCo runtime.
     *
     * @param agent an {@link IAgent} that allow interaction with JaCoCo
     *              agent runtime.
     */
    public JaCoCoController(final IAgent agent) {
        this.agent = agent;
        failExecutionDataStores = new LinkedList<ExecutionDataStore>();
        successExecutionDataStores = new LinkedList<ExecutionDataStore>();
    }

    /**
     * Instantiate a {@link JaCoCoController} for the default agent
     * instance of the JaCoCo runtime in this JVM.
     */
    public JaCoCoController() {
        this(RT.getAgent());
    }

    @Override
    public void init() {
        super.init();
        agent.setSessionId("bootstrap");
        spectrumBuilder = new SpectrumBuilder();
    }

    @Override
    public void reset() {
        agent.reset();
    }

    @Override
    public void dump(final boolean reset) throws IOException {
        agent.dump(reset);
    }

    @Override
    public void save(final String name, final boolean testFailed) {
        /*
         * Set current session id with name and test status.
         */
        agent.setSessionId((testFailed ? "failed:" : "passed:") + name);

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


        generateCoverageMatrix(testFailed, executionDataStore);

    }

    @Override
    public BundleSpectrum analyze() {
        analyze(new ExecutionDataStore(), spectrumBuilder, Arrays.asList(classesDirs));
        for (final ExecutionDataStore executionDataStore : failExecutionDataStores) {
            final Collection<File> files = classFilesOfStore(executionDataStore);
            analyze(executionDataStore, spectrumBuilder.updateTestFailed, files);
        }
        for (final ExecutionDataStore executionDataStore : successExecutionDataStores) {
            final Collection<File> files = classFilesOfStore(executionDataStore);
            analyze(executionDataStore, spectrumBuilder.updateTestPassed, files);
        }
        return new BundleSpectrum(
                spectrumBuilder.getClasses(), spectrumBuilder.getSourceFiles());
    }

    @Override
    public void destroy() {
        super.destroy();
        failExecutionDataStores.clear();
        successExecutionDataStores.clear();
        spectrumBuilder = null;
    }

    private void analyze(final ExecutionDataStore executionDataStore,
            final ICoverageVisitor coverageVisitor, final Collection<File> files) {

        final Analyzer analyzer = new Analyzer(executionDataStore, coverageVisitor);
        for (final File file : files) {
            try {
                analyzer.analyzeAll(file);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Collection<File> classFilesOfStore(
            final ExecutionDataStore executionDataStore) {

        final Collection<File> result = new ArrayList<File>();
        for (final ExecutionData data : executionDataStore.getContents()) {
            final String vmClassName = data.getName();
            final File classFile = classFiles.get(vmClassName);
            if (classFile != null) {
                result.add(classFile);
            }
        }
        return result;
    }

    private void generateCoverageMatrix(boolean testFailed, ExecutionDataStore executionDataStore){
        /*
        Já parte do tratamento para a matriz
        PASS / FAIL vem da parâmetro do método
        Presumo por enquanto que ele faz o dump de sessão a cada caso de teste
         */

        /*
        Gets the compiled classes directory via a system property
         */
        String classesDirPath = System.getProperty("jaguar2.classesDirs", "target/classes");

        CoverageBuilder coverageBuilder = new CoverageBuilder();
        //Analyzer é quem faz a combinação das probes com as linhas do código
        Analyzer analyzer = new Analyzer(executionDataStore, coverageBuilder);

        PrintStream pStream = null;
        File coverageMatrix = null;

        File classesDir = new File(classesDirPath).getAbsoluteFile();

        if(!classesDir.exists() || !classesDir.isDirectory()){
            return;
        }

        try {

            coverageMatrix = new File("target/coverageMatrix.txt");
            pStream = new PrintStream(new FileOutputStream(coverageMatrix, true));

            analyzer.analyzeAll(classesDir);


            for(IClassCoverage cc : coverageBuilder.getClasses()){
                for(int i = cc.getFirstLine(); i <=cc.getLastLine(); i++){

                    if(cc.getLine(i).getStatus() == ICounter.EMPTY){
                        continue;
                    }
                    switch(cc.getLine(i).getStatus()){

                        case ICounter.NOT_COVERED:
                            pStream.print("0 ");
                            break;
                        case ICounter.FULLY_COVERED:
                            pStream.print("1 ");
                            break;
                        case ICounter.PARTLY_COVERED:
                            pStream.print("1 ");
                            break;
                    }
                }
            }


            //Aqui acaba o meu tratamento da matriz

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(pStream != null){
                if(testFailed){
                    pStream.print('-');
                }else{
                    pStream.print('+');
                }
                pStream.println();
                pStream.flush();

                pStream.close();
            }
        }
    }

}
