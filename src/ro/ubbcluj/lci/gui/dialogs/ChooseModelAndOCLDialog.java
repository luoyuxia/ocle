package ro.ubbcluj.lci.gui.dialogs;


import ro.ubbcluj.lci.gui.Actions.AToolsActions;
import ro.ubbcluj.lci.gui.FileSelectionData;
import ro.ubbcluj.lci.gui.mainframe.ProjectManager;
import ro.ubbcluj.lci.gui.tools.AFileFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChooseModelAndOCLDialog extends JDialog {
    private JPanel modelChoosePanel = new JPanel();
    private JButton chooseModelButton = new JButton("选择模型文件");
    private JTextField modelFileField = new JTextField("模型文件");
    private AFileFilter xmlModelFilter = new AFileFilter("xml", "model files (*.xmi)");
    private DefaultListModel xmlListModel = new DefaultListModel();

    private AFileFilter oclFilter = new AFileFilter("ocl", "Ocl text files (*.ocl)");
    private DefaultListModel oclListModel = new DefaultListModel();
    private List tempFiles = new ArrayList();

    private JPanel oclChoosePanel = new JPanel();
    private JButton chooseOCLButton = new JButton("选择OCL文件");
    private JTextField oclFileField = new JTextField("OCL文件");

    private JPanel buttonPanel = new JPanel();
    private JButton oclCheckButton = new JButton("验证");
    public ChooseModelAndOCLDialog(Frame owner) {
        super(owner, "模型验证");
        this.initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        initModelChoosePanel();
        add(modelChoosePanel, BorderLayout.NORTH);

        initOclChoosePanel();
        add(oclChoosePanel, BorderLayout.CENTER);

        initButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        registerListener();
        registerWindowsEvent();

        setSize(500, 200);
        setLocationRelativeTo( null );
        setVisible(true);
    }

    private void registerWindowsEvent() {
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                closeDialog();
            }
        });
    }

    private void initModelChoosePanel() {
        modelChoosePanel.setLayout(new FlowLayout());
        modelChoosePanel.add(new JLabel("Model File: "));
        modelChoosePanel.add(modelFileField);
        modelFileField.setEnabled(false);
        modelFileField.setPreferredSize(new Dimension(200, 20));
        modelChoosePanel.add(chooseModelButton);
    }

    private void initOclChoosePanel() {
        oclChoosePanel.setLayout(new FlowLayout());
        oclChoosePanel.add(new JLabel(" OCL 文件: "));
        oclChoosePanel.add(oclFileField);
        oclFileField.setEnabled(false);
        oclFileField.setPreferredSize(new Dimension(200, 20));
        oclChoosePanel.add(chooseOCLButton);
    }

    private void registerListener() {
        final ChooseModelAndOCLDialog dialog = this;
        this.chooseModelButton.addActionListener(new ActionListener() {
            AFileFilter[] filters = { dialog.xmlModelFilter };
            public void actionPerformed(ActionEvent e) {
                File[] files = AFileFilter.chooseFiles(dialog.getOwner(),
                        "添加", filters);
                StringBuffer s = new StringBuffer();
                dialog.xmlListModel.clear();
                for(int j = 0; j < (files != null ? files.length : -1); ++j) {
                    if (files[j] != null) {
                        s.append(files[j].getPath());
                        try {
                            dialog.xmlListModel.addElement(new FileSelectionData(files[j].getCanonicalPath(),
                                    false));
                        } catch (IOException var7) {
                            System.err.println("File does not exist!");
                        }
                    }
                }
                dialog.modelFileField.setText(s.toString());
            }
        });

        this.chooseOCLButton.addActionListener(new ActionListener() {
            AFileFilter[] filters = { dialog.oclFilter };
            public void actionPerformed(ActionEvent e) {
                File[] files = AFileFilter.chooseFiles(dialog.getOwner(),
                        "添加", filters);
                StringBuffer s = new StringBuffer();
                dialog.oclListModel.clear();
                for(int j = 0; j < (files != null ? files.length : -1); ++j) {
                    if (files[j] != null) {
                        s.append(files[j].getPath());
                        try {
                            dialog.oclListModel.addElement(new FileSelectionData(files[j].getCanonicalPath(),
                                    false));
                        } catch (IOException var7) {
                            System.err.println("File does not exist!");
                        }
                    }
                }
                dialog.oclFileField.setText(s.toString());
            }
        });

        this.oclCheckButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.tempFiles.clear();
                String tmpProjectName = "tmpOclProject";
                String tmpProjectFile = "tmpProjectFile";
                List tmpPaths = ProjectManager.getInstance().newEmptyProject(tmpProjectName, tmpProjectFile,getXmlFiles(), getOclFiles());
                AToolsActions.compileAction.actionPerformed(null);
                dialog.tempFiles.addAll(tmpPaths);
                dialog.closeDialog();
            }
        });
    }

    private void initButtonPanel() {
        buttonPanel.setLayout( new FlowLayout(FlowLayout.RIGHT) );
        buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttonPanel.add(oclCheckButton);
    }

    private Object[] getXmlFiles() {
        DefaultListModel xmlFiles = (DefaultListModel)xmlListModel;
        return xmlFiles != null ? xmlFiles.toArray() : null;
    }

    private Object[] getOclFiles() {
        DefaultListModel oclfiles = (DefaultListModel)oclListModel;
        return oclfiles != null ? oclfiles.toArray() : null;
    }

    private void closeDialog() {
        for (int i = 0; i < tempFiles.size(); i++) {
            File file = new File(tempFiles.get(i).toString());
            if (file.exists()) {
                file.delete();
            }
        }
        this.dispose();
    }


}
