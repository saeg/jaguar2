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

public class HtmlTagBuilder {
    private String id = "";
    private String tag = "";
    private String cssClass = "";
    private String innerHtml = "";
    private String inlineStyle = "";
    private String siblingHtml = "";
    private String href = "";
    private String ariaLabel = "";
    private String title = "";
    private String dataKey = "";
    private String dataValue = "";

    private HtmlTagBuilder() {}

    public HtmlTagBuilder(String tag) {
        this.tag = tag;
    }

    public HtmlTagBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public HtmlTagBuilder setCssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }
    
    public HtmlTagBuilder addCssClass(String newCssClass) {
        this.cssClass = this.cssClass == null ?
            newCssClass : this.cssClass + " " + newCssClass;
        return this;
    }

    public HtmlTagBuilder setInnerHtml(String innerHtml) {
        this.innerHtml = innerHtml;
        return this;
    }
    
    public HtmlTagBuilder addInnerHtml(String newInnerHtml) {
        this.innerHtml = this.innerHtml == null ?
                newInnerHtml :
                this.innerHtml + newInnerHtml
        ;
        return this;
    }
    
    public HtmlTagBuilder addInnerHtml(String... newInnerHtml) {
        StringBuilder allNewInnerHtmlConcatenate = new StringBuilder();
        for (String newInnerHtmlTemp : newInnerHtml) {
            allNewInnerHtmlConcatenate.append(newInnerHtmlTemp);
        }
        return this.addInnerHtml(allNewInnerHtmlConcatenate.toString());
    }
    
    public HtmlTagBuilder setInlineStyle(String inlineStyle) {
        this.inlineStyle = inlineStyle;
        return this;
    }
    
    public HtmlTagBuilder setSiblingHtml(String siblingHtml) {
        this.siblingHtml = siblingHtml;
        return this;
    }
    
    public HtmlTagBuilder setHref(String href) {
        this.href = href;
        return this;
    }

    public HtmlTagBuilder setAriaLabel(String ariaLabel) {
        this.ariaLabel = ariaLabel;
        return this;
    }

    public HtmlTagBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public HtmlTagBuilder setData(String dataKey, String dataValue) {
        this.dataKey = dataKey;
        this.dataValue = dataValue;
        return this;
    }

    public String build() {
        return new HtmlTag(
                this.id,
                this.tag,
                this.cssClass,
                this.siblingHtml,
                this.innerHtml,
                this.inlineStyle,
                this.href,
                this.ariaLabel,
                this.title,
                this.dataKey,
                this.dataValue
        ).buildTag();
    }
}
