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
