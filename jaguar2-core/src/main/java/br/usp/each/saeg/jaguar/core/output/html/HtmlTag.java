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

public class HtmlTag {
    private final String id;
    private final String tag;
    private final String cssClass;
    private final String innerHtml;
    private final String siblingHtml;
    private final String inlineStyle;
    private final String href;
    private final String ariaLabel;
    private final String title;
    private String dataKey = "";
    private String dataValue = "";

    private StringBuilder finalTag;

    public HtmlTag(String id, String tag, String cssClass, String siblingHtml, String innerHtml,
                   String inlineStyle, String href, String ariaLabel, String title,
                   String dataKey,
                   String dataValue) {
        this.id = id;
        this.tag = tag;
        this.cssClass = cssClass;
        this.innerHtml = innerHtml;
        this.siblingHtml = siblingHtml;
        this.inlineStyle = inlineStyle;
        this.href = href;
        this.ariaLabel = ariaLabel;
        this.title = title;
        this.dataKey = dataKey;
        this.dataValue = dataValue;

        this.finalTag = new StringBuilder();
    }

    public String buildTag() {
        finalTag.append("<")
                .append(tag);

        if (!id.isEmpty()) {
            finalTag.append(" id=\"")
                    .append(id)
                    .append("\"");
        }

        if (!cssClass.isEmpty()) {
            finalTag.append(" class=\"")
                    .append(cssClass)
                    .append("\"");
        }

        if (!inlineStyle.isEmpty()) {
            finalTag.append(" style=\"")
                    .append(inlineStyle)
                    .append("\"");
        }
        
        if(!href.isEmpty()){
            finalTag.append(" href=\"")
                    .append(href)
                    .append('"');
        }

        if(!ariaLabel.isEmpty()){
            finalTag.append(" aria-label=\"")
                    .append(ariaLabel)
                    .append('"');
        }

        if(!title.isEmpty()){
            finalTag.append(" title=\"")
                    .append(title)
                    .append('"');
        }

        if (!dataKey.isEmpty() && !dataValue.isEmpty()) {
            finalTag.append(" data-")
                    .append(this.dataKey)
                    .append("=\"")
                    .append(this.dataValue)
                    .append('"');
        }

        finalTag.append(">");

        if (!innerHtml.isEmpty()) {
            finalTag.append(innerHtml);
        }

        finalTag.append("</")
                .append(tag)
                .append(">");

        if (!siblingHtml.isEmpty()) {
            finalTag.append(siblingHtml);
        }

        return this.finalTag.toString();
    }
}
