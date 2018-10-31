package du.ui.qlayouts;

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
