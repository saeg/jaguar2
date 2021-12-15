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
package br.usp.each.saeg.jaguar.codeforest.model;

import java.util.Collection;
import java.util.Collections;

import javax.xml.bind.annotation.XmlAttribute;

public abstract class Requirement extends SuspiciousElement {
	
	public enum Type {
		LINE, DUA;
	}

	@XmlAttribute
	public abstract Type getType();
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends SuspiciousElement> getChildren() {
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public String toString() {
		return "Requirement [name=" + name + ", number=" + number
				+ ", location=" + location + ", suspiciousValue="
				+ suspiciousValue + "]";
	}
	
	@Override
	@XmlAttribute
	public Integer getLocation() {
		return location;
	}
	@Override
	public void setLocation(Integer location) {
		this.location = location;
	}
	
}
