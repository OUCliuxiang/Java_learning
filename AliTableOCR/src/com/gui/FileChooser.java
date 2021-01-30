package com.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.alibaba.ocr.demo.AliTableOCR;

public class FileChooser {
    static String path;
    static String outputPath;
    static JTextArea infoPrint;
    static JTextField picPath, excelPath;
    static JPanel northPanel, centerPanel, southPanel;

    public static void createWindow() {
        JFrame frame = new JFrame("图片转表格OCR");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createNorthPanel();
        frame.add(northPanel, BorderLayout.NORTH);

        createSouthPanel();
        frame.add(southPanel, BorderLayout.SOUTH);

        createCenterPanel();
        frame.add(centerPanel, BorderLayout.CENTER);

        frame.setSize(500, 400);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void createNorthPanel(){
        File directory = new File("");
        path = directory.getAbsolutePath();

        picPath = new JTextField(path,80);
        excelPath = new JTextField(path,80);
        JButton picButton = createButton("输入", picPath);
        JButton excelButton = createButton("输出", excelPath);

        northPanel = new JPanel(new GridLayout(2,3));
        // northPanel.setLayout(new GridLayout(2,3));

        northPanel.add(new JLabel("输入路径：", SwingConstants.RIGHT));
        northPanel.add(picPath, BorderLayout.WEST);
        northPanel.add(picButton);

        northPanel.add(new JLabel("输出路径：", SwingConstants.RIGHT));
        northPanel.add(excelPath, BorderLayout.WEST);
        northPanel.add(excelButton);
    }

    private static void createCenterPanel(){
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EtchedBorder());
        infoPrint = new JTextArea("开始转换\n");
        infoPrint.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(infoPrint);
        // infoPrint.setEnabled(false);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private static JButton createButton(String name, JTextField textField){
        JButton button = new JButton("选择");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(path);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(null);
                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    textField.setText(file.toString());
                    if (name == "输入"){
                        FileChooser.path = textField.getText();
                        FileChooser.outputPath = textField.getText().replace(
                                "data", "output");
                        excelPath.setText(outputPath);
                    }
                    else if ( name == "输出"){
                        FileChooser.outputPath = textField.getText();
                    }
                }
                else{
                    textField.setText("请重新选择");
                }
            }
        });
        return button;
    }

    private static void createSouthPanel(){
        southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1,2));
        JButton startButton = new JButton("开始");
        JButton stopButton = new JButton("退出");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File files = new File(FileChooser.path);
                File[] fs = files.listFiles();
                for ( File f:fs){
                    if (! f.isDirectory()){
                        AliTableOCR.submit2AliAPI(f.toString(), outputPath);
                        infoPrint.append(String.format("%s 完成转换\n", f.toString()));
                        // long time = AliTableOCR.getUsedTime();
                        // infoPrint.append(String.format("用时%f 秒。\n", (double)time/1000.0));
                        infoPrint.paintImmediately(infoPrint.getBounds());
                    }
                }
            }
        });
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                System.exit(0);
            }
        });
        southPanel.add(startButton);
        southPanel.add(stopButton);
    }
}

