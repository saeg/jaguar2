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

import java.util.LinkedList;
import java.util.List;

import br.usp.each.saeg.badua.agent.rt.internal_d2401f0.Agent;
import br.usp.each.saeg.badua.agent.rt.internal_d2401f0.core.data.ExecutionData;
import br.usp.each.saeg.badua.agent.rt.internal_d2401f0.core.data.IExecutionDataVisitor;
import br.usp.each.saeg.badua.agent.rt.internal_d2401f0.core.runtime.RuntimeData;
import br.usp.each.saeg.badua.core.data.ExecutionDataStore;
import br.usp.each.saeg.jaguar2.spi.CoverageController;

public class BaDuaController implements CoverageController {

    private final Agent agent;

    private final List<ExecutionDataStore> failExecutionDataStores;

    private final List<ExecutionDataStore> successExecutionDataStores;

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
    }

}
