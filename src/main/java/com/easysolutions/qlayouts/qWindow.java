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
