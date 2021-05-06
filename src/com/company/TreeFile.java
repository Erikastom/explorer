package com.company;

import java.io.File;

public class TreeFile extends File {
    public TreeFile(File parent, String child) {
        super(parent, child);
    }

    public String toString() {
        return getName();
    }
}
