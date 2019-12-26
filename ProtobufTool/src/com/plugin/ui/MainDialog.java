package com.plugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.plugin.config.SysConfig;
import com.plugin.entitiy.ProtoFileEntity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class MainDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Project project;
	/**
	 * Create the dialog.
	 */
	public MainDialog(Project project) {
        super(WindowManager.getInstance().suggestParentWindow(project), ModalityType.APPLICATION_MODAL);
        this.project = project;
//        setAlwaysOnTop(true);
	    setTitle("Protobuf Tool");
	    setResizable(false);
		setBounds(100, 100, 298, 436);
		getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
        Font font = new Font(null, 0, 14);
        JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 292, 21);
		getContentPane().add(menuBar);
		
		JMenu mnProperties = new JMenu("properties");
        mnProperties.setFont(font);
		menuBar.add(mnProperties);
		
		JMenuItem mntmSetting = new JMenuItem("setting");
        mntmSetting.setFont(font);
		mntmSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                ConfigDialog ui = new ConfigDialog(MainDialog.this, project);
				ui.setVisible(true);
//                ui.show();
			}
		});
		
        JMenuItem mntmRefresh = new JMenuItem("refresh");
        mnProperties.add(mntmRefresh);
        mntmRefresh.setFont(font);
        mntmRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reload();
            }
        });
        mnProperties.add(mntmSetting);

		
		JButton btnNewButton = new JButton("ChoseAll");
        btnNewButton.setFont(font);
		btnNewButton.setBounds(10, 28, 81, 28);
		getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel model = (DefaultListModel) list.getModel();
                ListSelectionModel selectionModel = list.getSelectionModel();
                int size = model.size();
                for (int i = 0; i < size; i++) {
                    selectionModel.addSelectionInterval(i , i);
                }
            }
        });
		
		JButton btnNewButton_1 = new JButton("Gen");
        btnNewButton_1.setFont(font);
		btnNewButton_1.setBounds(213, 28, 69, 28);
		getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _gen(list.getSelectedValuesList());
                list.clearSelection();
            }
        });

        List<ProtoFileEntity> fileList = new ArrayList<>();
        if (SysConfig.getValue(project, SysConfig.FILE_PATH) != null){
            File file = new File(SysConfig.getValue(project, SysConfig.FILE_PATH));
            _getProtoFiles(file, fileList);
            fileList.sort(new Comparator<ProtoFileEntity>() {
                @Override
                public int compare(ProtoFileEntity o1, ProtoFileEntity o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }

        JScrollPane scrollPane = new JBScrollPane();
        scrollPane.setBounds(0, 57, 298, 345);
        getContentPane().add(scrollPane);
        
        JButton btnNewButton_2 = new JButton("ClearChose");
        btnNewButton_2.setFont(font);
        btnNewButton_2.setBounds(101, 28, 102, 28);
        getContentPane().add(btnNewButton_2);
        btnNewButton_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list.getSelectionModel().clearSelection();
            }
        });

        list = new JBList<>(fileList);
        scrollPane.setViewportView(list);
        list.setFont(font);

        list.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                } else {
                    super.addSelectionInterval(index0, index1);
                }
            }
        });

//        init();
	}

//    @Nullable
//    @Override
//    protected JComponent createCenterPanel() {
//        return contentPanel;
//    }

    JList list;
	public void reload(){
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.removeAllElements();
        Vector<ProtoFileEntity> fileList = new Vector<>();
        File file = new File(SysConfig.getValue(project, SysConfig.FILE_PATH));
        _getProtoFiles(file, fileList);
        fileList.sort(new Comparator<ProtoFileEntity>() {
            @Override
            public int compare(ProtoFileEntity o1, ProtoFileEntity o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        fileList.forEach(k -> model.addElement(k));
    }

    private void _getProtoFiles(File file, List<ProtoFileEntity> fileList){
        if (file == null)
            return;
        if (file.isDirectory() && file.listFiles() != null){
            for (File listFile : file.listFiles()) {
                _getProtoFiles(listFile, fileList);
            }
        }else if (file.isFile() && _fileSuffix(file).equals("proto") && !file.getName().contains("CmdNumber.proto")){
            fileList.add(new ProtoFileEntity(file.getName(), file.getAbsolutePath()));
        }
    }

    private String _fileSuffix(File file){
        return file.getName().substring(file.getName().lastIndexOf(".") + 1);
    }

    private void _gen(List<ProtoFileEntity> selectedValuesList){
	    if (selectedValuesList == null || selectedValuesList.size() == 0){
            Messages.showMessageDialog(project, "Please Chose Gen File!", "ERROR", Messages.getInformationIcon());
	        return;
        }

        String err = "";
	    String serr = "";
        String cmd = SysConfig.getValue(project, SysConfig.PROTOC_PATH) + " --java_out=" + SysConfig.getValue(project, SysConfig.OUT_PATH) + " --proto_path=";
        for (ProtoFileEntity entity : selectedValuesList) {
            try {
                Process process = Runtime.getRuntime().exec(cmd + entity.getPath() + " " + entity.getFullPath());
                System.out.println("SUCCESS Gen File " + entity.getName() + "!");
                String s = _trans(process.getErrorStream());
                if (s.length() > 0)
                    serr += s + "\n";
            } catch (Exception e){
                err += entity.getName() + "\n";
            }
        }

        String res = "";
        if (err.length() > 0){
            res += "Gen Failed File: \n" + err;
        }
        if (serr.length() > 0){
            res += "\n" + "Gen Error:" + serr;
        }
        if (res.length() > 0){
            Messages.showMessageDialog(project, res, "ERROR", Messages.getErrorIcon());
        }else {
            Messages.showMessageDialog(project, "Gen All Success!", "Success", Messages.getInformationIcon());
        }
    }

    private String _trans(InputStream stream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            StringBuilder b = new StringBuilder();
            while ((line = br.readLine()) != null) {
                b.append(line).append("\n");
            }
            return b.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
