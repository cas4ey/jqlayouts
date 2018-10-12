import java.awt.*;

public final class qSizePolicy
{
    public enum Policy
    {
        Fixed,
        Minimum,
        Maximum,
        Expanding,
        MinimumExpanding,
        Ignored
    }

    private enum GridFill
    {
        None      (0x00, GridBagConstraints.NONE),
        Horizontal(0x01, GridBagConstraints.HORIZONTAL),
        Vertical  (0x02, GridBagConstraints.VERTICAL),
        Both      (0x03, GridBagConstraints.BOTH);

        final int mask;
        final int fill;

        GridFill(int bitmask, int gridfill)
        {
            mask = bitmask;
            fill = gridfill;
        }

        static GridFill fromMask(int mask)
        {
            for (GridFill v : values())
            {
                if (v.mask == mask)
                {
                    return v;
                }
            }

            return None;
        }
    }

    private final Policy m_horizontal;
    private final Policy m_vertical;

    public qSizePolicy(Policy h, Policy v)
    {
        m_horizontal = h;
        m_vertical = v;
    }

    public final Policy horizontal()
    {
        return m_horizontal;
    }

    public final Policy vertical()
    {
        return m_vertical;
    }

    public final int gridFillDirection()
    {
        int bitmask = GridFill.None.mask;

        switch (m_horizontal)
        {
            case Fixed:            break;
            case Minimum:          bitmask = GridFill.Horizontal.mask; break;
            case Maximum:          bitmask = GridFill.Horizontal.mask; break;
            case Expanding:        break;
            case MinimumExpanding: break;
            case Ignored:          break;
        }

        switch (m_vertical)
        {
            case Fixed:            break;
            case Minimum:          break;
            case Maximum:          break;
            case Expanding:        break;
            case MinimumExpanding: break;
            case Ignored:          break;
        }

        return GridFill.fromMask(bitmask).fill;
    }
}
