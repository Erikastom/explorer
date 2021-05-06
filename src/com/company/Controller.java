package com.company;

import javax.swing.*;

import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Controller {
    private View view;
    private String path;
    private File[] files;
    private String[] drives;
    private String[][] array;

    public void setView(View view) {
        this.view = view;
    }

    public void start() {
        view.create();
        uploadDrives();
        path = drives[0];
        updateList();
        updateTree();
    }

    private void updateList() {
        File file = new File(path);
        files = file.listFiles();
        view.setPreviousButtonEnable(path.length() > 3);
        view.setTextFieldText(path);
        updateTable();
    }

    private void updateTable() {
        array = new String[files.length][4];
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            array[i][0] = file.getName();
            LocalDateTime time = LocalDateTime.ofEpochSecond(file.lastModified() / 1000, 0, ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");
            array[i][1] = formatter.format(time);
            array[i][2] = getFileType(file);
            array[i][3] = getFileLength(file);
        }
        view.setTableData(array);
        setColumnHeadersToButton();
        setRowHeight();
    }

    public void updateTree() {
        view.setPath(path);
    }

    public void setRowHeight() {
        for (int i = 0; i < array.length; i++) {
            view.setTableRowHeight(i, 19);
        }
    }

    private String getFileType(File file) {
        if (file.isDirectory()) {
            return "File folder";
        }
        String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
        return "File \"" + extension.toUpperCase() + "\"";
    }

    private String getFileLength(File file) {
        if (file.isDirectory()) {
            return "";
        }
        String[] sizes = {"B", "KB", "MB", "GB"};
        long size = file.length();
        int count = 0;
        while (size > 1024) {
            count++;
            size /= 1024;
        }
        return size + " " + sizes[count];
    }

    private void uploadDrives() {
        File[] files = File.listRoots();
        drives = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            drives[i] = files[i].getAbsolutePath();
        }
        view.setBoxData(drives);
    }

    public void handleTableMouseDoubleClick() throws IOException {
        if (files[view.getSelectedRow()].isFile()) {
            if (!Desktop.isDesktopSupported()) {
                System.out.println("Desktop is not supported!");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            desktop.open(files[view.getSelectedRow()]);
        }
        String nextPath = path + files[view.getSelectedRow()].getName() + "\\";
        if (new File(nextPath).list() == null) {
            return;
        }
        path = nextPath;
        updateList();
    }

    public void handleTreeMouseDoubleClick() {
        String substring = view.getSelectedTreePath().substring(1, view.getSelectedTreePath().length() - 1);
        String replaced1 = substring.replace("\\, ", "\\");
        if (new File(replaced1).list() == null) {
            return;
        }
        path = replaced1.replace(", ", "\\");
        updateList();
    }

    public void handlePreviousButtonClick() {
        int index = path.lastIndexOf('\\', path.length() - 2);
        path = path.substring(0, index + 1);
        updateList();
    }

    /*public void handleNextButtonClick() {
        path = history[count];
        updateList();
        count--;
    }*/

    public void handleUploadButtonClick() {
        path = drives[view.getBoxIndex()];
        updateTree();
        updateList();
    }

    public void handleEnterButtonClick() {
        String filePath = view.getTextField();
        if (!new File(filePath).exists()) {
            ControllerPathError controllerPathError = new ControllerPathError();
            ViewPathError viewPathError = new ViewPathError();
            controllerPathError.setViewPathError(viewPathError);
            controllerPathError.setView(view);
            viewPathError.setControllerPathError(controllerPathError);
            controllerPathError.start();
            return;
        }
        if (filePath.lastIndexOf('\\') + 1 != filePath.length()) {
            filePath += "\\";
        }
        path = filePath;
        updateList();
        if (path.length() > 3) {
            path = path.substring(0, 3);
        }
        updateTree();
    }

    public void setColumnHeadersToButton() {
        for (int i = 0; i < view.getHeaderNames().length; i++) {
            JButton button;
            TableColumn column = view.getTable().getColumnModel().getColumn(i);
            column.setHeaderRenderer(new ButtonHeaderRenderer(button = new JButton (view.getHeaderNames()[i])));
            //button.setToolTipText("Click it!");
        }
    }

    public void handlePopupMenuClick() {
        ControllerProperties controllerProperties = new ControllerProperties();
        ViewProperties viewProperties = new ViewProperties();
        controllerProperties.setViewProperties(viewProperties);
        viewProperties.setControllerProperties(controllerProperties);
        controllerProperties.setFolder(files[view.getSelectedRow()].isDirectory());
        controllerProperties.setSYS(getFileType(files[view.getSelectedRow()]).equals("File \"SYS\""));
        controllerProperties.start();
    }
}