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

final class qGridWidgetItem
{
    private final java.awt.Component m_widget;
    private final java.awt.GridBagConstraints m_constraints = new java.awt.GridBagConstraints();
    private final int m_column;
    private final int m_row;
    private final int m_columnSpan;
    private final int m_rowSpan;

    qGridWidgetItem(java.awt.Component widget, int row, int column, int rowSpan, int columnSpan)
    {
        m_widget = widget;
        m_column = column;
        m_row = row;
        m_columnSpan = columnSpan;
        m_rowSpan = rowSpan;
    }

    final int row()
    {
        return m_row;
    }

    final int column()
    {
        return m_column;
    }

    final int rowSpan()
    {
        return m_rowSpan;
    }

    final int columnSpan()
    {
        return m_columnSpan;
    }

    final java.awt.Component widget()
    {
        return m_widget;
    }

    final java.awt.GridBagConstraints constraints()
    {
        return m_constraints;
    }
}
