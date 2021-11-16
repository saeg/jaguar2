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

public class Minus implements Heuristic {

    @Override
    public double eval(
            final int cef, final int cnf,
            final int cep, final int cnp) {

        double susp = 0.0d;
        double sMinus = 0.0d;
        double fMinus = 0.0d;
        double pMinus = 0.0d;

        final double dCef = cef;
        final double dCep = cep;
        final int cefPlusCnf = cef + cnf;
        final int cepPlusCnp = cep + cnp;

        if (cef > 0) {
            if (cep > 0) {
                susp = (dCef / cefPlusCnf)
                        / (dCef / cefPlusCnf + dCep / cepPlusCnp);
            } else {
                susp = 1;
            }
        }

        if (cefPlusCnf > 0) {
            fMinus = dCef / cefPlusCnf;
        }

        if (cepPlusCnp > 0) {
            pMinus = dCep / cepPlusCnp;
        }

        if (fMinus != 1) {
            sMinus = (1 - fMinus) / (2 - fMinus - pMinus);
        }

        susp -= sMinus;
        return susp;
    }

}
