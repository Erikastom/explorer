package com.company;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EventObject;

public class View {
    private Controller controller;
    private JButton previousButton;
    private JButton nextButton;
    private JTextField textField;
    private JComboBox<String> comboBox;
    private String[] headerNames;
    private JTable table;
    private JTree tree;
    private FileSystemModel fileSystemModel;
    private JMenuBar menuBar;
    private JMenuItem menuItem;
    private JPopupMenu popupMenu;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public String[] getHeaderNames() {
        return headerNames;
    }

    public JTable getTable() {
        return table;
    }

    public void setBoxData(String[] array) {
        comboBox.setModel(new DefaultComboBoxModel<>(array));
    }

    public void setTableData(String[][] array) {
        table.setModel(new DefaultTableModel(array, headerNames = new String[]
                {"File name", "Creation time", "File type", "File size"}));
    }

    public void setPath(String path) {
        tree.setModel(new FileSystemModel(new File(path)));
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public String getSelectedTreePath() {
        return String.valueOf(tree.getSelectionPath());
    }

    public void setTableRowHeight(int row, int rowHeight) {
        table.setRowHeight(row, rowHeight);
    }

    public void setPreviousButtonEnable(boolean b) {
        previousButton.setEnabled(b);
    }

    public void setNextButtonEnable(boolean b) {
        nextButton.setEnabled(b);
    }

    public int getBoxIndex() {
        return comboBox.getSelectedIndex();
    }

    public void setTextFieldText(String text) {
        textField.setText(text);
    }

    public String getTextField() {
        return textField.getText();
    }

    public void create() {
        JFrame frame = new JFrame("Explorer");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        previousButton = new JButton("<");
        previousButton.setBounds(40, 10, 45, 45);
        previousButton.addActionListener(e -> controller.handlePreviousButtonClick());
        frame.add(previousButton);

        nextButton = new JButton(">");
        nextButton.setBounds(100, 10, 45, 45);
        //nextButton.addActionListener(e -> controller.handleNextButtonClick());
        frame.add(nextButton);

        textField = new JTextField();
        textField.setBounds(200, 10, 670, 30);
        frame.add(textField);

        textField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleEnterButtonClick();
            }
        });

        comboBox = new JComboBox<>();
        comboBox.setBounds(20, 500, 70, 25);
        frame.add(comboBox);

        JButton uploadButton = new JButton("Upload");
        uploadButton.setBounds(100, 500, 90, 25);
        uploadButton.addActionListener(e -> controller.handleUploadButtonClick());
        frame.add(uploadButton);

        table = new JTable() {
            @Override
            public boolean editCellAt(int row, int column, EventObject e) {
                return false;
            }
        };

        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 12 ));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12 ));

        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(200, 80, 670, 450);
        frame.add(scrollPane);

        //Tracking double click on th Jtable
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (e.getClickCount() == 2) {
                        try {
                            controller.handleTableMouseDoubleClick();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                table.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        Point p = e.getPoint();
                        table.changeSelection(table.rowAtPoint(p), table.columnAtPoint(p), false, false);
                    }
                });
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                table.clearSelection();
            }
        });

        //Right button track + popup menu
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (e.getClickCount() == 1) {
                        table.setComponentPopupMenu(popupMenu);
                    }
                }
            }
        });

        popupMenu = new JPopupMenu();
        menuItem = new JMenuItem("Properties");

        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    int rowAtPoint = table.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table));
                    if (event.getClickCount() == 1) {
                        if (rowAtPoint > -1) {
                            table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                        controller.handlePopupMenuClick();
                    }
                }
            }
        });

        popupMenu.add(menuItem);

        tree = new JTree(fileSystemModel);
        tree.setEditable(false);

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        //Icon closedIcon = new ImageIcon("src/Images/folder_icon.png");
        //renderer.setClosedIcon(closedIcon);


        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setBounds(20, 80, 150, 400);
        frame.add(treeScrollPane);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    controller.handleTreeMouseDoubleClick();
                }
            }
        });




        frame.setVisible(true);
    }
}
