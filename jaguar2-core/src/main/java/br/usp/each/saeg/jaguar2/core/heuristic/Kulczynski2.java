/**
 * Copyright (c) 2021, 2021 University of Sao Paulo and Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Henrique Lemos - initial API and implementation and/or initial documentation
 */
package br.usp.each.saeg.jaguar2.core.heuristic;

import br.usp.each.saeg.jaguar2.api.Heuristic;

/**
 * Implementation of the Kulczynski2 heuristic.
 */
public class Kulczynski2 implements Heuristic {

    /**
     * Default constructor
     */
    public Kulczynski2() {
    }

    @Override
    public double eval(
            final int cef, final int cnf,
            final int cep, final int cnp) {

        double susp = 0.0d;
        if (cef > 0) {
            final double dCef = cef;
            susp = 0.5 * (dCef / (cef + cnf) + dCef / (cef + cep));
        }
        return susp;
    }

}
