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
 * Implementation of the Wong3 heuristic.
 */
public class Wong3 implements Heuristic {

    @Override
    public double eval(
            final int cef, final int cnf,
            final int cep, final int cnp) {

        double passed = 0.0d;
        if (cep > 2 && cep <= 10) {
            passed = 2 + (0.1 * (cep - 2));
        } else if (cep > 10) {
            passed = 2.8 + (0.001 * (cep - 10));
        } else {
            passed = cep;
        }
        return cef - passed;
    }

}
