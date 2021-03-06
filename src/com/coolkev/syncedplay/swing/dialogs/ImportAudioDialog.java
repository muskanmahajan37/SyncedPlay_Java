/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.dialogs;

import com.coolkev.syncedplay.model.SoundTableModel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImportAudioDialog extends JDialog {
    
    public static int CANCEL_OPTION = 0;
    public static int APPROVE_OPTION = 1;
    
    static File LastFile = new File(System.getProperty("user.home"));
    
    private int closeState = 0;
    
    private File selectedFile;
    
    private JLabel fileNameLabel;
    private JTextField keyTextField;
    
    public ImportAudioDialog(final Component parent) {
        super();
        initUI(parent);
    }

    private void initUI(final Component parent) {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel label = new JLabel("Import Audio File");
        label.setAlignmentX(0.5f);
        add(label);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
        filePanel.add(new JLabel("File:"));
        filePanel.add(Box.createRigidArea(new Dimension(7, 0)));
        JButton fileSelectButton = new JButton("Select New File");
        fileSelectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser(LastFile);
                FileFilter WAVaudioFilter = new FileNameExtensionFilter("WAV Audio Files", "wav");
                FileFilter AUaudioFilter = new FileNameExtensionFilter("AU Audio Files", "au");
                fileChooser.setFileFilter(AUaudioFilter);
                fileChooser.setFileFilter(WAVaudioFilter);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                  File file = fileChooser.getSelectedFile();
                  LastFile = file;
                  if (SoundTableModel.isFileSupported(file)){
                    selectedFile = file;
                    fileNameLabel.setText(file.getName());
                  } else {
                    ErrorDialog ed = new ErrorDialog("The file is not in a known format!", parent);
                    ed.showDialog();
                  }
                }
            }
        });
        filePanel.add(fileSelectButton);
        filePanel.add(Box.createRigidArea(new Dimension(7, 0)));
        fileNameLabel = new JLabel("No File Selected");
        filePanel.add(fileNameLabel);
        add(filePanel);
        
        add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.X_AXIS));
        keyPanel.add(new JLabel("Keyword:"));
        keyPanel.add(Box.createRigidArea(new Dimension(7, 0)));
        keyTextField = new JTextField("");
        keyTextField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, keyTextField.getPreferredSize().height));
        keyPanel.add(keyTextField);
        add(keyPanel);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(Box.createHorizontalGlue());
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                closeState = CANCEL_OPTION;
                dispose();
            }
        });
        buttonsPanel.add(close);
        JButton approveOption = new JButton("Import!");
        approveOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (selectedFile == null){
                    ErrorDialog ed = new ErrorDialog("You must select a file!", parent);
                    ed.showDialog();
                } else if (keyTextField.getText().length() == 0){
                    ErrorDialog ed = new ErrorDialog("You must provide a keyword!", parent);
                    ed.showDialog();
                } else {
                    closeState = APPROVE_OPTION;
                    dispose();
                }
            }
        });
        buttonsPanel.add(approveOption);
        add(buttonsPanel);
        

        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Import Sound");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pack();
    }
    
    public int showDialog() {
        setVisible(true);
        return closeState;
    }
    
    public File getFile() {
        return selectedFile;
    }
    
    public String getKey() {
        return keyTextField.getText();
    }
}
