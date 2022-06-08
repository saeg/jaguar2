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
package br.usp.each.saeg.jaguar2.jacoco;

import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.analysis.ILine;
import org.jacoco.core.analysis.ISourceNode;

import br.usp.each.saeg.jaguar2.api.ICodeSpectrum;

public class CodeSpectrum implements ICodeSpectrum {

    private LineSpectrum[] lines;

    private int offset;

    public CodeSpectrum() {
        offset = UNKNOWN_LINE;
    }

    @Override
    public int getFirstLine() {
        return offset;
    }

    @Override
    public int getLastLine() {
        return lines == null ? UNKNOWN_LINE : offset + lines.length - 1;
    }

    @Override
    public LineSpectrum getLine(final int nr) {
        if (lines == null || nr < getFirstLine() || nr > getLastLine()) {
            return LineSpectrum.EMPTY;
        }
        final LineSpectrum line = lines[nr - offset];
        return line == null ? LineSpectrum.EMPTY : line;
    }

    private void ensureCapacity(final int first, final int last) {
        if (first == UNKNOWN_LINE || last == UNKNOWN_LINE) {
            return;
        }
        if (lines == null) {
            offset = first;
            lines = new LineSpectrum[last - first + 1];
        } else {
            final int newFirst = Math.min(getFirstLine(), first);
            final int newLast = Math.max(getLastLine(), last);
            final int newLength = newLast - newFirst + 1;
            if (newLength > lines.length) {
                final LineSpectrum[] newLines = new LineSpectrum[newLength];
                System.arraycopy(lines, 0, newLines, offset - newFirst, lines.length);
                offset = newFirst;
                lines = newLines;
            }
        }
    }

    public void increment(final ISourceNode coverage, final boolean testFailed) {
        final int firstLine = coverage.getFirstLine();
        if (firstLine != UNKNOWN_LINE) {
            final int lastLine = coverage.getLastLine();
            ensureCapacity(firstLine, lastLine);
            for (int i = firstLine; i <= lastLine; i++) {
                final ILine line = coverage.getLine(i);
                switch (line.getInstructionCounter().getStatus()) {
                case ICounter.FULLY_COVERED:
                case ICounter.PARTLY_COVERED:
                    lines[i - offset] = getLine(i).increment(testFailed);
                }
            }
        }
    }

}
