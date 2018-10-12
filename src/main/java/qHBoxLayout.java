import javax.swing.*;
import java.awt.*;

public class qHBoxLayout extends qBoxLayout
{
    public qHBoxLayout(JComponent parent)
    {
        super(parent, Direction.Horizontal);
    }

    @Override
    public final Dimension getMaximumSize()
    {
        int maxHeight = 0;

        for (qLayoutItem item : items())
        {
            if (item.widget().isVisible() && !(item.widget() instanceof Box.Filler))
            {
                maxHeight = Integer.max(maxHeight, item.widget().getHeight());
            }
        }

        final Insets insets = getInsets();
        return new Dimension(Integer.MAX_VALUE, maxHeight + insets.top + insets.bottom);
    }

    @Override
    protected final void update()
    {
        if (totalFactor() == 0)
        {
            return;
        }

        final int availableWidth = getWidth() - getMinimumSize().width - 1;

        if (availableWidth <= 0)
        {
            return;
        }

        final float totalFactor = totalFactor();
        for (qLayoutItem item : items())
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
