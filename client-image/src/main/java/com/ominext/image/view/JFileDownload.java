package com.ominext.image.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JFileDownload extends JFrame {
    private String textFieldLabel;
    private String buttonLabel;
    private String choosertitle;

    private JLabel label;
    private JTextField textField;
    private JButton button;

    private JFileChooser fileChooser;

    private int mode;
    public static final int MODE_OPEN = 1;
    public static final int MODE_SAVE = 2;

    public JFileDownload(String textFieldLabel, String buttonLabel) {
        this.textFieldLabel = textFieldLabel;
        this.buttonLabel = buttonLabel;

        fileChooser = new JFileChooser();

        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // creates the GUI
        label = new JLabel(textFieldLabel);

        textField = new JTextField(30);
        button = new JButton(buttonLabel);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        });
        add(label);
        add(textField);
        add(button);
    }

    private void buttonActionPerformed(ActionEvent evt) {
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle(choosertitle);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //
        // disable the "All files" option.
        //
        fileChooser.setAcceptAllFileFilterUsed(false);
        //
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " + fileChooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + fileChooser.getSelectedFile());
        }
        else {
            System.out.println("No Selection ");
        }
    }
//
//        if (mode == MODE_OPEN) {
//            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//                textField.setText(fileChooser.getSelectedFile());
//            }
//        } else if (mode == MODE_SAVE) {
//            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
//                textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
//            }
//        }
//    }

    public void addFileTypeFilter(String extension, String description) {
        FileTypeFilter filter = new FileTypeFilter(extension, description);
        fileChooser.addChoosableFileFilter(filter);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getSelectedFilePath() {
        return textField.getText();
    }

    public JFileChooser getFileChooser() {
        return this.fileChooser;
    }
}
