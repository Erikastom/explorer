package com.company;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HeaderListener extends MouseAdapter {

    private Component dispatchComponent;
    private JTableHeader header;
    private int column  = -1;
    private Component editor;

    public HeaderListener(JTableHeader header, int column, Component editor) {
        this.header = header;
        this.column = column;
        this.editor = editor;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    private void setDispatchComponent(MouseEvent e) {
        int col = header.getTable().columnAtPoint(e.getPoint());
        if (col != column || col == -1) return;

        Point p = e.getPoint();
        Point p2 = SwingUtilities.convertPoint(header, p, editor);
        dispatchComponent = SwingUtilities.getDeepestComponentAt(editor, p2.x, p2.y);
    }

    private boolean repostEvent(MouseEvent e) {
        if (dispatchComponent == null) {
            return false;
        }
        MouseEvent e2 = SwingUtilities.convertMouseEvent(header, e, dispatchComponent);
        dispatchComponent.dispatchEvent(e2);
        return true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (header.getResizingColumn() == null) {
            Point p = e.getPoint();

            int col = header.getTable().columnAtPoint(p);
            if (col != column || col == -1) return;

            int index = header.getColumnModel().getColumnIndexAtX(p.x);
            if (index == -1) return;

            editor.setBounds(header.getHeaderRect(index));
            header.add(editor);
            editor.validate();
            setDispatchComponent(e);
            repostEvent(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        repostEvent(e);
        dispatchComponent = null;
        header.remove(editor);
    }
}
