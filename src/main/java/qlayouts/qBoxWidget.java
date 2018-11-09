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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class qBoxWidget extends Box implements ComponentListener
{
    public enum Direction { Horizontal, Vertical }

    private int m_totalFactor = 0;
    private int m_spacing = 0;
    private final AtomicReference<Dimension> m_minSize  = new AtomicReference<>(new Dimension(0, 0));
    private final AtomicReference<Dimension> m_maxSize  = new AtomicReference<>(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    private final AtomicReference<Dimension> m_prefSize = new AtomicReference<>(new Dimension(0, 0));
    private final ArrayList<qBoxWidgetItem> m_layoutItems = new ArrayList<>();
    private final ArrayList<Box.Filler> m_autoSpacers = new ArrayList<>();
    private final ArrayList<Dimension> m_manualSpacers = new ArrayList<>();
    private final Direction m_direction;
    private final MinMaxSizeChangeListener m_minmaxSizeListener = new MinMaxSizeChangeListener(this);
    private final PrefSizeChangeListener m_prefSizeListener = new PrefSizeChangeListener(this);
    private final AtomicInteger m_update = new AtomicInteger(0);
    private final AtomicInteger m_changeCount = new AtomicInteger(0);

    protected abstract void updateSize();
    protected abstract Dimension calcMaximumSize();

    qBoxWidget(Direction direction)
    {
        super(direction == Direction.Horizontal ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS);
        m_direction = direction;
        setMargins(0);
        addComponentListener(this);
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
        setMargins(i.top, value, i.bottom, i.right);
    }

    public final void setRightMargin(int value)
    {
        final Insets i = getInsets();
        setMargins(i.top, i.left, i.bottom, value);
    }

    public final void setTopMargin(int value)
    {
        final Insets i = getInsets();
        setMargins(value, i.left, i.bottom, i.right);
    }

    public final void setBottomMargin(int value)
    {
        final Insets i = getInsets();
        setMargins(i.top, i.left, value, i.right);
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
    public final Dimension minimumSize()
    {
        return this.getMinimumSize();
    }

    @Override
    public final Dimension preferredSize()
    {
        return this.getPreferredSize();
    }

    @Override
    public final Dimension getMinimumSize()
    {
        return new Dimension(m_minSize.get());
    }

    @Override
    public final Dimension getMaximumSize()
    {
        return new Dimension(m_maxSize.get());
    }

    @Override
    public final Dimension getPreferredSize()
    {
        return new Dimension(m_prefSize.get());
    }

    private Dimension calcMinimumSize()
    {
        return calcCustomSize(Component::getMinimumSize);
    }

    private Dimension calcPreferredSize()
    {
        return calcCustomSize(Component::getPreferredSize);
    }

    private Dimension calcCustomSize(SizeGetter sizeGetter)
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

        for (final Dimension size : m_manualSpacers)
        {
            d.width += size.width;
            d.height += size.height;
        }

        int maxSize = 0;
        for (qBoxWidgetItem item : m_layoutItems)
        {
            if (!item.widget().isVisible() || item.widget() instanceof Box.Filler)
            {
                continue;
            }

            final Dimension wd = sizeGetter.get(item.widget());
            if (m_direction == Direction.Horizontal)
            {
                d.width += wd.width;
                maxSize = Integer.max(maxSize, wd.height);
            }
            else
            {
                d.height += wd.height;
                maxSize = Integer.max(maxSize, wd.width);
            }
        }

        if (m_direction == Direction.Horizontal)
        {
            d.height += maxSize;
        }
        else
        {
            d.width += maxSize;
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
    }

    @Override
    public final void componentHidden(ComponentEvent e)
    {
        if (e.getComponent() != this)
        {
            delayedUpdate(this::updateOnVisibilityChange);
        }
    }

    @Override
    public final void componentMoved(ComponentEvent e) {}

    @Override
    public final void componentShown(ComponentEvent e)
    {
        if (e.getComponent() != this)
        {
            delayedUpdate(this::updateOnVisibilityChange);
        }
    }

    public final void addWidget(JComponent widget) throws qException
    {
        addWidget(widget, 0);
    }

    public final void addWidget(JComponent widget, int stretch) throws qException
    {
        if (widget == getParent())
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

//        if (stretch == 0)
//        {
//            widget.setMinimumSize(widget.getPreferredSize());
//        }

        widget.addComponentListener(this);
        widget.addPropertyChangeListener("minimumSize", m_minmaxSizeListener);
        widget.addPropertyChangeListener("maximumSize", m_minmaxSizeListener);
        widget.addPropertyChangeListener("preferredSize", m_prefSizeListener);

        updateOnVisibilityChange();
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
        m_layoutItems.add(new qBoxWidgetItem(widget, stretchFactor));
        m_totalFactor += stretchFactor;
    }

    private void addHorizontalSpacing(int size)
    {
        final Dimension spacingSize = new Dimension(size, 0);
        m_manualSpacers.add(spacingSize);
        add(createRigidArea(spacingSize));
    }

    private void addVerticalSpacing(int size)
    {
        final Dimension spacingSize = new Dimension(0, size);
        m_manualSpacers.add(spacingSize);
        add(createRigidArea(spacingSize));
    }

    private void beginUpdating()
    {
        m_update.incrementAndGet();
    }

    private void endUpdating()
    {
        m_update.decrementAndGet();
    }

    private boolean isUpdating()
    {
        return m_update.get() > 0;
    }

    private void update()
    {
        beginUpdating();
        updateSize();
        endUpdating();
    }

    private void recalculateSizeAll()
    {
        final Dimension minSize = calcMinimumSize();
        final Dimension maxSize = calcMaximumSize();
        final Dimension prefSize = calcPreferredSize();

        m_minSize.set(minSize);
        m_maxSize.set(maxSize);
        m_prefSize.set(prefSize);

        setMinimumSize(minSize);
        setMaximumSize(maxSize);
        setPreferredSize(prefSize);
    }

    private void updateOnVisibilityChange()
    {
        recalculateSizeAll();
        update();
    }

    private void delayedUpdate(Runnable func)
    {
        if (m_changeCount.getAndIncrement() == 0)
        {
            SwingUtilities.invokeLater(() -> {
                func.run();
                m_changeCount.set(0);
            });
        }
    }

    private Dimension updateMinimumSize()
    {
        final Dimension size = calcMinimumSize();
        m_minSize.set(size);
        setMinimumSize(size);
        return size;
    }

    private Dimension updateMaximumSize()
    {
        final Dimension size = calcMaximumSize();
        m_maxSize.set(size);
        setMaximumSize(size);
        return size;
    }

    private Dimension updatePreferredSize()
    {
        final Dimension size = calcPreferredSize();
        m_prefSize.set(size);
        setPreferredSize(size);
        return size;
    }

    protected final ArrayList<qBoxWidgetItem> items()
    {
        return m_layoutItems;
    }

    private interface SizeGetter
    {
        Dimension get(Component widget);
    }

    private abstract class ChangeListener implements PropertyChangeListener
    {
        final qBoxWidget self;
        ChangeListener(qBoxWidget parent) { self = parent; }

        @Override
        public final void propertyChange(PropertyChangeEvent e)
        {
            if (!self.isUpdating())
            {
                change();
            }
        }

        protected abstract void change();
    }

    private class MinMaxSizeChangeListener extends ChangeListener
    {
        private final AtomicInteger m_changeCount = new AtomicInteger(0);

        MinMaxSizeChangeListener(qBoxWidget parent)
        {
            super(parent);
        }

        @Override
        protected final void change()
        {
            self.delayedUpdate(() -> {
                self.firePropertyChange("minimumSize", null, self.updateMinimumSize());
                self.firePropertyChange("maximumSize", null, self.updateMaximumSize());
            });
        }
    }

    private class PrefSizeChangeListener extends ChangeListener
    {
        private final AtomicInteger m_changeCount = new AtomicInteger(0);

        PrefSizeChangeListener(qBoxWidget parent)
        {
            super(parent);
        }

        @Override
        protected final void change()
        {
            self.delayedUpdate(() -> self.firePropertyChange("preferredSize", null, self.updatePreferredSize()));
        }
    }
}
