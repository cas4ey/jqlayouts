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

public class qHBoxWidget extends qBoxWidget
{
    public qHBoxWidget()
    {
        super(Direction.Horizontal);
    }

    @Override
    public final Dimension calcMaximumSize()
    {
        int maxHeight = 0;

        for (qBoxWidgetItem item : items())
        {
            final Component widget = item.widget();
            if (widget.isVisible() && !(widget instanceof Box.Filler))
            {
                final int prefHeight = widget.getPreferredSize().height;
                final int currHeight = widget.getHeight();
                final int h = Integer.max(currHeight, prefHeight);
                maxHeight = Integer.max(maxHeight, h);
            }
        }

        final Insets insets = getInsets();
        return new Dimension(Integer.MAX_VALUE, maxHeight + insets.top + insets.bottom);
    }

    @Override
    protected final void updateSize()
    {
        if (totalFactor() == 0)
        {
            return;
        }

        final int availableWidth = getWidth() - getMinimumSize().width - 1;

        if (availableWidth < 0)
        {
            setSize(getWidth() - availableWidth, getHeight());
            return;
        }

        final float totalFactor = totalFactor();
        for (qBoxWidgetItem item : items())
        {
            if (item.stretchFactor() == 0 || !item.widget().isVisible())
            {
                continue;
            }

            final boolean isFiller = item.widget() instanceof Box.Filler;
            final int minWidth = isFiller ? 0 : item.widget().getMinimumSize().width;
            final int width = minWidth + (int)Math.floor((float)(availableWidth * item.stretchFactor()) / totalFactor);
            final int preferredWidth = item.widget().getPreferredSize().width;
            final int currentWidth = item.widget().getWidth();

            if (preferredWidth != width || currentWidth != width)
            {
                if (isFiller)
                {
                    Box.Filler filler = (Box.Filler)item.widget();
                    final Dimension d = new Dimension(width, 0);
                    filler.changeShape(d, d, d);
                }
                else
                {
                    item.widget().setPreferredSize(new Dimension(width, item.widget().getPreferredSize().height));
                    item.widget().setSize(width, item.widget().getHeight());
                }
            }
        }
    }
}
