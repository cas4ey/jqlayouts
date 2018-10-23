package du.ui.qlayouts;

final class qBoxWidgetItem
{
    private final java.awt.Component m_widget;
    private final int m_stretchFactor;

    qBoxWidgetItem(java.awt.Component widget, int stretchFactor)
    {
        m_widget = widget;
        m_stretchFactor = stretchFactor;
    }

    final int stretchFactor()
    {
        return m_stretchFactor;
    }

    final java.awt.Component widget()
    {
        return m_widget;
    }
}
