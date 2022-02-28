package com.midorlo.batbelt.swing.dnd;


import java.util.EventObject;

import javax.swing.CellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;


/**
 * Abstract base implementation of a CellEditor for a {@link JTreeTable}.
 */
public class AbstractCellEditor implements CellEditor
{
    /**
     * The list of CellEditorListeners
     */
    private final EventListenerList listenerList = new EventListenerList();

    @Override
    public Object getCellEditorValue()
    {
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject e)
    {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent)
    {
        return false;
    }

    @Override
    public boolean stopCellEditing()
    {
        return true;
    }

    @Override
    public void cancelCellEditing()
    {
        // Empty default implementation
    }

    @Override
    public final void addCellEditorListener(CellEditorListener l)
    {
        listenerList.add(CellEditorListener.class, l);
    }

    @Override
    public final void removeCellEditorListener(CellEditorListener l)
    {
        listenerList.remove(CellEditorListener.class, l);
    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type.
     */
    protected final void fireEditingStopped()
    {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == CellEditorListener.class)
            {
                ((CellEditorListener) listeners[i + 1])
                        .editingStopped(new ChangeEvent(this));
            }
        }
    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type.
     */
    protected final void fireEditingCanceled()
    {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == CellEditorListener.class)
            {
                ((CellEditorListener) listeners[i + 1])
                        .editingCanceled(new ChangeEvent(this));
            }
        }
    }
}
