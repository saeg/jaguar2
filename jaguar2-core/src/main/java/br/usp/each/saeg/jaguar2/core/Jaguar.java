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
package br.usp.each.saeg.jaguar2.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import br.usp.each.saeg.jaguar.codeforest.model.Requirement;
import br.usp.each.saeg.jaguar.core.JaguarSFL;
import br.usp.each.saeg.jaguar.core.heuristic.HeuristicCalculator;
import br.usp.each.saeg.jaguar.core.model.core.requirement.AbstractTestRequirement;
import br.usp.each.saeg.jaguar.core.output.html.HtmlBuilder;
import br.usp.each.saeg.jaguar.core.output.html.HtmlWriter;
import br.usp.each.saeg.jaguar.core.utils.TestRequirementUtils;
import br.usp.each.saeg.jaguar2.CoverageControllerLoader;
import br.usp.each.saeg.jaguar2.core.heuristic.Heuristic;
import br.usp.each.saeg.jaguar2.core.heuristic.Tarantula;
import br.usp.each.saeg.jaguar2.spi.CoverageController;

public class Jaguar {
	
	public static final String REPORTS_FOLDER_NAME = ".jaguar";
	
	public static int nTests = 0;
	
	public static int nTestsFailed = 0;

    private final CoverageController controller;

    public Jaguar(final CoverageController controller) {
        this.controller = controller;
    }

    public Jaguar() {
        this(new CoverageControllerLoader().load());
    }

    /**
     * Called before any tests have been run.
     */
    public void testRunStarted() {
        nTests = 0;
        nTestsFailed = 0;
        if (controller != null) {
            controller.init();
        }
    }

    /**
     * Called when an atomic test is about to be started.
     *
     * The current implementation reset runtime code coverage data as no
     * code executed so far is related to current test.
     */
    public void testStarted() {
        if (controller != null) {
            controller.reset();
        }
    }

    /**
     * Called when an atomic test has finished, whether the test succeeds
     * or fails.
     *
     * The current implementation save runtime code coverage data for
     * further analysis. The data is flagged when executed by a failing
     * test case.
     *
     * @param testFailed a flag indicating that test fails.
     */
    public void testFinished(final boolean testFailed) {
    	nTests++;
    	if (testFailed) {
    		nTestsFailed++;
    	}
        if (controller != null) {
            controller.save(testFailed);
        }
    }

    /**
     * Called when all tests have finished.
     */
    public void testRunFinished() {
        if (controller != null) {
        	JaguarSFL sfl = new JaguarSFL();
            controller.analyze(sfl);
            Heuristic heuristic = new Tarantula();
            HeuristicCalculator calc = new HeuristicCalculator(heuristic, sfl.getTestRequirements().values(), nTests - nTestsFailed, nTestsFailed);
            ArrayList<AbstractTestRequirement> testRequirements = calc.calculateRank();
            
            if(testRequirements.isEmpty()){
    			return;
    		}
    		
    		Requirement.Type testRequirementType = TestRequirementUtils.getType(testRequirements);
    		
            final Properties props = System.getProperties();
            final String projectDirectoryProp = props.getProperty("jaguar2.projectDirectory");
            final String outputFolderProp = props.getProperty("jaguar2.outputFolder");
    		
    		HtmlWriter htmlWriter = new HtmlWriter(
    				new HtmlBuilder(),
    				testRequirements,
    				testRequirementType,
    				heuristic,
    				new File(projectDirectoryProp),
    				outputFolderProp
    		);
    		
    		try {
    			if(Requirement.Type.LINE.equals(testRequirementType)){
        			htmlWriter.generateHtmlForLineType();
        		}else {
        			htmlWriter.generateHtmlForDuaType();
        		}
    		} catch (Exception e) {
    			throw new RuntimeException(e);
			}

        }
        nTests = 0;
        nTestsFailed = 0;
    }

}
