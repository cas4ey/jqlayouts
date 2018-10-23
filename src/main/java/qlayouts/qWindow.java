package du.ui.qlayouts;

import javax.swing.*;
import java.awt.*;

public class qWindow extends JFrame
{
    public qWindow() throws HeadlessException
    {
        super();
    }

    public qWindow(GraphicsConfiguration gc)
    {
        super(gc);
    }

    public qWindow(String title) throws HeadlessException
    {
        super(title);
    }

    public qWindow(String title, GraphicsConfiguration gc)
    {
        super(title, gc);
    }

    @Override
    public final Dimension getMinimumSize()
    {
        return getContentPane().getMinimumSize();
    }

    @Override
    public final Dimension getMaximumSize()
    {
        return getContentPane().getMaximumSize();
    }

    @Override
    public final Dimension getPreferredSize()
    {
        return getContentPane().getPreferredSize();
    }
}
