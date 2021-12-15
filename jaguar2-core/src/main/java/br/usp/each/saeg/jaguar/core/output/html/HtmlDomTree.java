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
package br.usp.each.saeg.jaguar.core.output.html;

public final class HtmlDomTree {
    
    private HtmlDomTree(){}
    
    public static final String SPAN = "span";
    public static final String OL = "ol";
    public static final String LI = "li";
    public static final String CODE = "code";
    public static final String PRE = "pre";
    public static final String TH = "th";
    public static final String TR = "tr";
    public static final String TD = "td";
    public static final String TABLE = "table";
    public static final String P = "p";
    public static final String A = "a";
    public static final String DIV = "div";
    public static final String ABBR = "abbr";

    public static boolean isHeaderCell(String tag) {
        return tag.equals(TH);
    }

    public static boolean isDataCell(String tag) {
        return tag.equals(TD);
    }

    public static boolean isTable(String tag) {
        return tag.equals(TABLE);
    }

}
