package du.ui.qlayouts;

import java.awt.GridBagConstraints;

public enum qAlignment
{
    TopLeft    (Horizontal.Left,   Vertical.Top,    GridBagConstraints.NORTHWEST),
    Top        (Horizontal.Center, Vertical.Top,    GridBagConstraints.NORTH),
    TopRight   (Horizontal.Right,  Vertical.Top,    GridBagConstraints.NORTHEAST),
    Left       (Horizontal.Left,   Vertical.Center, GridBagConstraints.WEST),
    Center     (Horizontal.Center, Vertical.Center, GridBagConstraints.CENTER),
    Right      (Horizontal.Right,  Vertical.Center, GridBagConstraints.EAST),
    BottomLeft (Horizontal.Left,   Vertical.Bottom, GridBagConstraints.SOUTHWEST),
    Bottom     (Horizontal.Center, Vertical.Bottom, GridBagConstraints.SOUTH),
    BottomRight(Horizontal.Right,  Vertical.Bottom, GridBagConstraints.SOUTHEAST);

    public enum Horizontal
    {
        Left(0), Center(0.5f), Right(1);

        private final float m_value;
        public final float value() { return m_value; }
        Horizontal(float value) { m_value = value; }
    }

    public enum Vertical
    {
        Top(0), Center(0.5f), Bottom(1);

        private final float m_value;
        public final float value() { return m_value; }
        Vertical(float value) { m_value = value; }
    }

    private final Horizontal m_horizontal;
    private final Vertical   m_vertical;
    private final int        m_anchor;

    public final Horizontal horizontal() { return m_horizontal; }
    public final Vertical vertical()     { return m_vertical; }
    public final int anchor()            { return m_anchor; }

    qAlignment(Horizontal h, Vertical v, int anchor)
    {
        m_horizontal = h;
        m_vertical   = v;
        m_anchor     = anchor;
    }
}
