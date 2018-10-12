import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public abstract class qBoxLayout extends Box implements ComponentListener
{
    public enum Direction { Horizontal, Vertical }

    private int m_totalFactor = 0;
    private int m_spacing = 0;
    private final ArrayList<qLayoutItem> m_layoutItems = new ArrayList<>();
    private final ArrayList<Box.Filler> m_autoSpacers = new ArrayList<>();
    private final JComponent m_parent;
    private final Direction m_direction;

    protected abstract void update();

    qBoxLayout(JComponent parent, Direction direction)
    {
        super(direction == Direction.Horizontal ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS);

        m_direction = direction;
        m_parent = parent;

        setMargins(0);

        parent.setLayout(new BorderLayout());
        parent.add(this, BorderLayout.CENTER);

        //setSize(parent.getSize());

        parent.addComponentListener(this);
        addComponentListener(this);
    }

    public final JComponent parent()
    {
        return m_parent;
    }

    public final Direction direction()
    {
        return m_direction;
    }

    protected final int totalFactor()
    {
        return m_totalFactor;
    }

    public final void setMargins(int top, int left, int bottom, int right)
    {
        //setBorder(new LineBorder(m_direction == Direction.Horizontal ? Color.red : Color.blue, 1));
        setBorder(new EmptyBorder(top, left, bottom, right));
    }

    public final void setMargins(int value)
    {
        setMargins(value, value, value, value);
    }

    public final void setMargins(int horizontal, int vertical)
    {
        setMargins(vertical, horizontal, vertical, horizontal);
    }

    public final void setLeftMargin(int value)
    {
        final Insets i = getInsets();
        setBorder(new EmptyBorder(i.top, value, i.bottom, i.right));
    }

    public final void setRightMargin(int value)
    {
        final Insets i = getInsets();
        setBorder(new EmptyBorder(i.top, i.left, i.bottom, value));
    }

    public final void setTopMargin(int value)
    {
        final Insets i = getInsets();
        setBorder(new EmptyBorder(value, i.left, i.bottom, i.right));
    }

    public final void setBottomMargin(int value)
    {
        final Insets i = getInsets();
        setBorder(new EmptyBorder(i.top, i.left, value, i.right));
    }

    public final void setSpacing(int spacing)
    {
        m_spacing = Integer.max(spacing, 0);

        if (!m_autoSpacers.isEmpty())
        {
            Dimension size;

            if (m_direction == Direction.Horizontal)
            {
                size = new Dimension(m_spacing, 0);
            }
            else
            {
                size = new Dimension(0, m_spacing);
            }

            for (Box.Filler space : m_autoSpacers)
            {
                space.changeShape(size, size, size);
            }
        }
    }

    public final int spacing()
    {
        return m_spacing;
    }

    @Override
    public final Dimension getMinimumSize()
    {
        final Insets insets = getInsets();
        final Dimension d = new Dimension(insets.left + insets.right,
                                          insets.top + insets.bottom);

        final int spacing = m_spacing * m_autoSpacers.size();
        if (m_direction == Direction.Horizontal)
        {
            d.width += spacing;
        }
        else
        {
            d.height += spacing;
        }

        for (qLayoutItem item : m_layoutItems)
        {
            if (!item.widget().isVisible() || item.widget() instanceof Box.Filler)
            {
                continue;
            }

            final Dimension wd = item.widget().getMinimumSize();
            d.setSize(d.width + wd.width, d.height + wd.height);
        }

        return d;
    }

    @Override
    public final Dimension getPreferredSize()
    {
        final Insets insets = getInsets();
        final Dimension d = new Dimension(insets.left + insets.right,
                                          insets.top + insets.bottom);

        final int spacing = m_spacing * m_autoSpacers.size();
        if (m_direction == Direction.Horizontal)
        {
            d.width += spacing;
        }
        else
        {
            d.height += spacing;
        }

        for (qLayoutItem item : m_layoutItems)
        {
            if (!item.widget().isVisible() || item.widget() instanceof Box.Filler)
            {
                continue;
            }

            final Dimension wd = item.widget().getPreferredSize();
            d.setSize(d.width + wd.width, d.height + wd.height);
        }

        return d;
    }

    @Override
    public final void componentResized(ComponentEvent e)
    {
        if (e.getComponent() == this)
        {
            update();
        }
        else if (e.getComponent() == m_parent)
        {
            setPreferredSize(m_parent.getPreferredSize());
            setSize(m_parent.getSize());
        }
    }

    @Override
    public final void componentHidden(ComponentEvent e)
    {
        if (e.getComponent() != this)
        {
            update();
        }
    }

    @Override
    public final void componentMoved(ComponentEvent e) {}

    @Override
    public final void componentShown(ComponentEvent e)
    {
        if (e.getComponent() != this)
        {
            update();
        }
    }

    public final void addWidget(JComponent widget) throws qException
    {
        addWidget(widget, 0);
    }

    public final void addWidget(JComponent widget, int stretch) throws qException
    {
        if (widget == m_parent)
        {
            throw new qException("Can't add widget into itself");
        }

        if (!m_layoutItems.isEmpty() &&
                (m_layoutItems.size() > 1 || !(m_layoutItems.get(0).widget() instanceof Box.Filler)))
        {
            Dimension size;

            if (m_direction == Direction.Horizontal)
            {
                size = new Dimension(m_spacing, 0);
            }
            else
            {
                size = new Dimension(0, m_spacing);
            }

            final Box.Filler spacer = (Box.Filler)createRigidArea(size);
            m_autoSpacers.add(spacer);
            add(spacer);
        }

        add(widget);
        stretch = Integer.max(stretch, 0);
        addItem(widget, stretch);

        if (stretch == 0)
        {
            widget.setMinimumSize(widget.getPreferredSize());
        }

        widget.addComponentListener(this);
    }

    public final void addWidget(JComponent widget, int stretch, qAlignment alignment) throws qException
    {
        widget.setAlignmentX(alignment.horizontal().value());
        widget.setAlignmentY(alignment.vertical().value());
        addWidget(widget, stretch);
    }

    public final void addWidget(JComponent widget, qAlignment alignment) throws qException
    {
        addWidget(widget, 0, alignment);
    }

    public final void addSpacing(int size)
    {
        if (size <= 0)
        {
            return;
        }

        if (m_direction == Direction.Horizontal)
        {
            addHorizontalSpacing(size);
        }
        else
        {
            addVerticalSpacing(size);
        }
    }

    public final void addStretch(int factor)
    {
        if (factor <= 0)
        {
            return;
        }

        final Component stretch = createRigidArea(new Dimension(0, 0));
        add(stretch);
        addItem(stretch, factor);
    }

    private void addItem(Component widget, int stretchFactor)
    {
        m_layoutItems.add(new qLayoutItem(widget, stretchFactor));
        m_totalFactor += stretchFactor;
    }

    private void addHorizontalSpacing(int size)
    {
        add(createRigidArea(new Dimension(size, 0)));
    }

    private void addVerticalSpacing(int size)
    {
        add(createRigidArea(new Dimension(0, size)));
    }

    protected final ArrayList<qLayoutItem> items()
    {
        return m_layoutItems;
    }
}
