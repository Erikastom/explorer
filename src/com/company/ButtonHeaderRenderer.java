package com.company;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class ButtonHeaderRenderer implements TableCellRenderer {

        private JTable table = null;
        private HeaderListener reporter = null;
        private JComponent editor;

        ButtonHeaderRenderer(JComponent editor) {
                this.editor = editor;
                this.editor.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                if (table != null && this.table != table) {
                        this.table = table;
                        final JTableHeader header = table.getTableHeader();
                        if (header != null) {
                                this.editor.setForeground(header.getForeground());
                                this.editor.setBackground(header.getBackground());
                                this.editor.setFont(header.getFont());
                                reporter = new HeaderListener(header, col, this.editor);
                                header.addMouseListener(reporter);
                        }
                }

                if (reporter != null) reporter.setColumn(col);

                return this.editor;
        }
}
