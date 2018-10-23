package du.ui.qlayouts;

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
