/*
Qt-like layouts for Java swing.


MIT License

Copyright (c) 2018 Victor Zarubkin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.easysolutions.qlayouts;

import javax.swing.*;
import java.awt.*;

public class qVBoxWidget extends qBoxWidget
{
    public qVBoxWidget()
    {
        super(Direction.Vertical);
    }

    @Override
    public final Dimension calcMaximumSize()
    {
        int maxWidth = 0;

        for (qBoxWidgetItem item : items())
        {
            final Component widget = item.widget();
            if (widget.isVisible() && !(widget instanceof Box.Filler))
            {
                final int prefWidth = widget.getPreferredSize().width;
                final int currWidth = widget.getWidth();
                final int w = Integer.max(currWidth, prefWidth);
                maxWidth = Integer.max(maxWidth, w);
            }
        }

        final Insets insets = getInsets();
        return new Dimension(maxWidth + insets.left + insets.right, Integer.MAX_VALUE);
    }

    @Override
    protected final void updateSize()
    {
        int availableSize = getHeight() - getMinimumSize().height;

        if (availableSize > 0) {
            for (qBoxWidgetItem item : items()) {
                if (item.widget().isVisible() && item.stretchFactor() == 0) {
                    availableSize -= item.widget().getHeight();
                }
            }
        }

        if (availableSize < 0) {
            setSize(getWidth(), getHeight() - availableSize);
            return;
        }

        if (totalFactor() == 0) {
            return;
        }

        final float totalFactor = totalFactor();
        for (qBoxWidgetItem item : items()) {
            if (!item.widget().isVisible() || item.stretchFactor() == 0) {
                continue;
            }

            final boolean isFiller = item.widget() instanceof Box.Filler;
            final int minSize = isFiller ? 0 : item.widget().getMinimumSize().height;
            final int size = minSize + (int)Math.floor((float)(availableSize * item.stretchFactor()) / totalFactor);
            final int preferredSize = item.widget().getPreferredSize().height;
            final int currentSize = item.widget().getHeight();

            if (preferredSize > size || currentSize != size) {
                if (isFiller) {
                    Box.Filler filler = (Box.Filler)item.widget();
                    final Dimension d = new Dimension(0, size);
                    filler.changeShape(d, d, d);
                } else {
                    if (preferredSize > size) {
                        item.widget().setPreferredSize(new Dimension(item.widget().getPreferredSize().width, size));
                    }
                    item.widget().setSize(item.widget().getWidth(), size);
                }
            }
        }
    }
}
