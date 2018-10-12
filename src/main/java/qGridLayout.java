import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Collections;

public class qGridLayout extends JPanel implements ComponentListener
{
    public enum Fill
    {
        None      (GridBagConstraints.NONE),
        Horizontal(GridBagConstraints.HORIZONTAL),
        Vertical  (GridBagConstraints.VERTICAL),
        Both      (GridBagConstraints.BOTH);

        private final int m_fill;
        Fill(int fill) { m_fill = fill; }
        final int fill() { return m_fill; }

        static Fill fromAlignment(qAlignment alignment)
        {
            Fill fill = Fill.Both;

            if (alignment.horizontal() != qAlignment.Horizontal.Center)
            {
                fill = Fill.Vertical;
            }

            if (alignment.vertical() != qAlignment.Vertical.Center)
            {
                fill = fill == Fill.Both ? Fill.Horizontal : Fill.None;
            }

            return fill;
        }
    }

    private int m_columnTotalFactor = 0;
    private int m_rowTotalFactor = 0;
    private int m_horizontalSpacing = 0;
    private int m_verticalSpacing = 0;
    private int m_columnCount = 0;
    private int m_rowCount = 0;
    private ArrayList<qGridLayoutItem> m_layoutItems = new ArrayList<>();
    private final ArrayList<Integer> m_columnStretchFactors = new ArrayList<>();
    private final ArrayList<Integer> m_rowStretchFactors = new ArrayList<>();
    private final ArrayList<Box.Filler> m_autoSpacers = new ArrayList<>();
    private final GridBagLayout m_layout = new GridBagLayout();
    private final JComponent m_parent;

    public qGridLayout(JComponent parent)
    {
        setLayout(m_layout);

        m_parent = parent;
        setMargins(0);

        parent.setLayout(new BorderLayout());
        parent.add(this, BorderLayout.CENTER);

        addComponentListener(this);
        parent.addComponentListener(this);
    }

    public final JComponent parent()
    {
        return m_parent;
    }

    public final void setMargins(int top, int left, int bottom, int right)
    {
        setBorder(new LineBorder(Color.red, 1));
        //setBorder(new EmptyBorder(top, left, bottom, right));
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
        final Insets i = m_parent.getInsets();
        setBorder(new EmptyBorder(i.top, value, i.bottom, i.right));
    }

    public final void setRightMargin(int value)
    {
        final Insets i = m_parent.getInsets();
        setBorder(new EmptyBorder(i.top, i.left, i.bottom, value));
    }

    public final void setTopMargin(int value)
    {
        final Insets i = m_parent.getInsets();
        setBorder(new EmptyBorder(value, i.left, i.bottom, i.right));
    }

    public final void setBottomMargin(int value)
    {
        final Insets i = m_parent.getInsets();
        setBorder(new EmptyBorder(i.top, i.left, value, i.right));
    }

    public final void setHorizontalSpacing(int spacing)
    {
        m_horizontalSpacing = Integer.max(spacing, 0);
    }

    public final void setVerticalSpacing(int spacing)
    {
        m_verticalSpacing = Integer.max(spacing, 0);
    }

    public final void setSpacing(int spacing)
    {
        setHorizontalSpacing(spacing);
        setVerticalSpacing(spacing);
    }

    public final int horizontalSpacing()
    {
        return m_horizontalSpacing;
    }

    public final int verticalSpacing()
    {
        return m_verticalSpacing;
    }

//    @Override
//    public final Dimension getMinimumSize()
//    {
//        final Insets insets = getInsets();
//        final Dimension d = new Dimension(insets.left + insets.right,
//                                          insets.top + insets.bottom);
//
//        final int spacing = m_horizontalSpacing * m_autoSpacers.size();
//        if (m_direction == Direction.Horizontal)
//        {
//            d.width += spacing;
//        }
//        else
//        {
//            d.height += spacing;
//        }
//
//        for (qLayoutItem item : m_layoutItems)
//        {
//            if (!item.widget().isVisible() || item.widget() instanceof Filler)
//            {
//                continue;
//            }
//
//            final Dimension wd = item.widget().getMinimumSize();
//            d.setSize(d.width + wd.width, d.height + wd.height);
//        }
//
//        return d;
//    }
//
//    @Override
//    public final Dimension getPreferredSize()
//    {
//        final Insets insets = getInsets();
//        final Dimension d = new Dimension(insets.left + insets.right,
//                                          insets.top + insets.bottom);
//
//        final int spacing = m_horizontalSpacing * m_autoSpacers.size();
//        if (m_direction == Direction.Horizontal)
//        {
//            d.width += spacing;
//        }
//        else
//        {
//            d.height += spacing;
//        }
//
//        for (qLayoutItem item : m_layoutItems)
//        {
//            if (!item.widget().isVisible() || item.widget() instanceof Filler)
//            {
//                continue;
//            }
//
//            final Dimension wd = item.widget().getPreferredSize();
//            d.setSize(d.width + wd.width, d.height + wd.height);
//        }
//
//        return d;
//    }

    @Override
    public final void componentResized(ComponentEvent e)
    {
        if (e.getComponent() == this)
        {
            update();
        }
        else if (e.getComponent() == m_parent)
        {
            setPreferredSize(parent().getPreferredSize());
            setSize(parent().getSize());
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

    public final void addWidget(JComponent widget, int row, int column) throws qException
    {
        addWidget(widget, row, column, 1, 1, qAlignment.Center, Fill.Both);
    }

    public final void addWidget(JComponent widget, int row, int column, qAlignment alignment) throws qException
    {
        addWidget(widget, row, column, 1, 1, alignment, Fill.fromAlignment(alignment));
    }

    public final void addWidget(JComponent widget, int row, int column, Fill fill) throws qException
    {
        addWidget(widget, row, column, 1, 1, qAlignment.Center, fill);
    }

    public final void addWidget(JComponent widget, int row, int column, int rowSpan, int columnSpan) throws qException
    {
        addWidget(widget, row, column, rowSpan, columnSpan, qAlignment.Center, Fill.Both);
    }

    public final void addWidget(JComponent widget, int row, int column, int rowSpan, int columnSpan,
                                qAlignment alignment) throws qException
    {
        addWidget(widget, row, column, rowSpan, columnSpan, alignment, Fill.fromAlignment(alignment));
    }

    public final void addWidget(JComponent widget, int row, int column, int rowSpan, int columnSpan,
                                Fill fill) throws qException
    {
        addWidget(widget, row, column, rowSpan, columnSpan, qAlignment.Center, fill);
    }

    public final void addWidget(JComponent widget, int row, int column, int rowSpan, int columnSpan,
                                qAlignment alignment, Fill fill) throws qException
    {
        if (widget == m_parent)
        {
            throw new qException("Can't add widget into itself");
        }

        rowSpan = Integer.max(rowSpan, 1);
        columnSpan = Integer.max(columnSpan, 1);

        if (row < 0)
        {
            row = m_rowCount;
        }

        if (column < 0)
        {
            column = m_columnCount;
        }

        if (row < m_rowCount && column < m_columnCount)
        {
            final int rcount = Integer.min(m_rowCount, row + rowSpan);
            final int ccount = Integer.min(m_columnCount, column + columnSpan);
            for (int r = row; r < rcount; ++r)
            {
                for (int c = column; c < ccount; ++c)
                {
                    if (getLayoutItem(r, c) != null)
                    {
                        throw new qException(String.format("qGridLayout: The cell (%d; %d) has been already taken!", r, c));
                    }
                }
            }
        }

        extendLayout(row + rowSpan, column + columnSpan);
        final qGridLayoutItem item = new qGridLayoutItem(widget, row, column, rowSpan, columnSpan);
        setLayoutItem(item, row, column, rowSpan, columnSpan);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = fill.fill();
        gbc.anchor = alignment.anchor();
        gbc.gridx = column;
        gbc.gridy = row;
        gbc.gridwidth = columnSpan;
        gbc.gridheight = rowSpan;
        gbc.weightx = calculateColumnWeight(column, columnSpan);
        gbc.weighty = calculateRowWeight(row, rowSpan);

        // Add spacing if set
        if ((m_horizontalSpacing != 0 && column != 0) || (m_verticalSpacing != 0 && row != 0))
        {
            gbc.insets = new Insets(0, 0, 0, 0);
            if (row != 0)
            {
                gbc.insets.top = m_verticalSpacing;
            }

            if (column != 0)
            {
                gbc.insets.left = m_horizontalSpacing;
            }
        }

        widget.setAlignmentX(alignment.horizontal().value());
        widget.setAlignmentY(alignment.vertical().value());
        if (column == 0 && row == 0)
            widget.setMaximumSize(widget.getPreferredSize());
        add(widget, gbc);

        //if (stretch == 0)
        //{
        //widget.setMinimumSize(widget.getPreferredSize());
        //}

        widget.addComponentListener(this);
    }

    public final void setColumnStretch(int column, int factor)
    {
        if (factor < 0)
        {
            return;
        }

        while (m_columnStretchFactors.size() <= column)
        {
            m_columnStretchFactors.add(0);
        }

        final int current = m_columnStretchFactors.get(column);
        m_columnStretchFactors.set(column, factor);
        m_columnTotalFactor += factor - current;
    }

    public final void setRowStretch(int row, int factor)
    {
        if (factor < 0)
        {
            return;
        }

        while (m_rowStretchFactors.size() <= row)
        {
            m_rowStretchFactors.add(0);
        }

        final int current = m_rowStretchFactors.get(row);
        m_rowStretchFactors.set(row, factor);
        m_rowTotalFactor += factor - current;
    }

//    private void addHorizontalSpacing(int size)
//    {
//        add(createRigidArea(new Dimension(size, 0)));
//    }
//
//    private void addVerticalSpacing(int size)
//    {
//        add(createRigidArea(new Dimension(0, size)));
//    }

    private double calculateColumnWeight(int column, int columnSpan)
    {
        if (m_columnTotalFactor == 0)
        {
            return 0;
        }

        double weight = 0;
        final double total = m_columnTotalFactor;
        for (int i = 0; i < columnSpan; ++i)
        {
            final double factor = m_columnStretchFactors.get(column + i);
            weight += factor / total;
        }

        return weight;
    }

    private double calculateRowWeight(int row, int rowSpan)
    {
        if (m_rowTotalFactor == 0)
        {
            return 0;
        }

        double weight = 0;
        final double total = m_rowTotalFactor;
        for (int i = 0; i < rowSpan; ++i)
        {
            final double factor = m_rowStretchFactors.get(row + i);
            weight += factor / total;
        }

        return weight;
    }

    private void extendLayout(int rowCount, int columnCount)
    {
        if (rowCount > m_rowCount || columnCount > m_columnCount)
        {
            rowCount = Integer.max(rowCount, m_rowCount);
            columnCount = Integer.max(columnCount, m_columnCount);

            // TODO: use capacity and more smart grow algorithm
            ArrayList<qGridLayoutItem> layoutItems =
                    new ArrayList<>(Collections.nCopies(rowCount * columnCount, null));

            for (int row = 0; row < m_rowCount; ++row)
            {
                for (int column = 0; column < m_columnCount; ++column)
                {
                    layoutItems.set(row * columnCount + column, getLayoutItem(row, column));
                }
            }

            for (int i = m_rowCount; i < rowCount; ++i)
            {
                m_rowStretchFactors.add(0);
            }

            for (int i = m_columnCount; i < columnCount; ++i)
            {
                m_columnStretchFactors.add(0);
            }

            m_layoutItems = layoutItems;
            m_rowCount = rowCount;
            m_columnCount = columnCount;
        }
    }

    private qGridLayoutItem getLayoutItem(int row, int column)
    {
        return m_layoutItems.get(row * m_columnCount + column);
    }

    private void setLayoutItem(qGridLayoutItem item, int row, int column)
    {
        m_layoutItems.set(row * m_columnCount + column, item);
    }

    private void setLayoutItem(qGridLayoutItem item, int row, int column, int rowSpan, int columnSpan)
    {
        for (int i=0; i < rowSpan; ++i)
        {
            for (int j=0; j < columnSpan; ++j)
            {
                setLayoutItem(item, row + i, column + i);
            }
        }
    }

    private void update()
    {

    }

    @Override
    public AccessibleContext getAccessibleContext()
    {
        if (accessibleContext == null)
        {
            accessibleContext = new AccessibleBox();
        }

        return accessibleContext;
    }

    private class AccessibleBox extends AccessibleAWTContainer
    {
        public AccessibleRole getAccessibleRole()
        {
            return AccessibleRole.FILLER;
        }
    } // inner class AccessibleBox
}
