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

public class McCon implements Heuristic {

    @Override
    public double eval(
            final int cef, final int cnf,
            final int cep, final int cnp) {

        double susp = 0.0d;
        final int cefPlusCnf = cef + cnf;
        final int cefPlusCep = cef + cep;
        if (cefPlusCnf > 0 && cefPlusCep > 0) {
            susp = (double) (cef * cef - cnf * cep) / (cefPlusCnf * cefPlusCep);
        }
        return susp;
    }

}
