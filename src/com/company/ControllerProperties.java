package com.company;

public class ControllerProperties {
    private ViewProperties viewProperties;
    private boolean isFolder;
    private boolean isSYS;

    public void setViewProperties(ViewProperties viewProperties) {
        this.viewProperties = viewProperties;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public void setSYS(boolean sys) {
        isSYS = sys;
    }

    public void start() {
        viewProperties.create(isFolder ? "src/Images/folder_icon.png" : (isSYS ? "src/Images/SYS_icon.png" : ""));
    }
}
