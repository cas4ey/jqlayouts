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
