package com.company;

public class ControllerPathError {
    private ViewPathError viewPathError;
    private View view;

    public void setViewPathError(ViewPathError viewPathError) {
        this.viewPathError = viewPathError;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void start() {
        viewPathError.create();
        setLabelText();
    }

    public void handleOkButtonClick() {
        viewPathError.close();
    }

    public void setLabelText() {
        viewPathError.setLabelText("Cannot locate \"" + view.getTextField() + "\". " +
                "Please, check the spelling and try again");
    }
}
