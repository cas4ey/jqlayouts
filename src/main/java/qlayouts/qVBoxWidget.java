package du.ui.qlayouts;

import javax.swing.*;
import java.awt.*;

public class qVBoxWidget extends qBoxWidget
{
    public qVBoxWidget()
    {
        super(Direction.Vertical);
    }

    @Override
    public final Dimension getMaximumSize()
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
        if (totalFactor() == 0)
        {
            return;
        }

        final int availableHeight = getHeight() - getMinimumSize().height - 1;

        if (availableHeight <= 0)
        {
            if (availableHeight < 0)
            {
                setSize(getWidth(), getHeight() - availableHeight);
            }
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
            final int minHeight = isFiller ? 0 : item.widget().getMinimumSize().height;
            final int height = minHeight + (int)Math.floor((float)(availableHeight * item.stretchFactor()) / totalFactor);
            final int preferredHeight = item.widget().getPreferredSize().height;
            final int currentHeight = item.widget().getHeight();

            if (preferredHeight != height || currentHeight != height)
            {
                if (isFiller)
                {
                    Box.Filler filler = (Box.Filler)item.widget();
                    final Dimension d = new Dimension(0, height);
                    filler.changeShape(d, d, d);
                }
                else
                {
                    item.widget().setPreferredSize(new Dimension(item.widget().getPreferredSize().width, height));
                    item.widget().setSize(item.widget().getWidth(), height);
                }
            }
        }
    }
}
