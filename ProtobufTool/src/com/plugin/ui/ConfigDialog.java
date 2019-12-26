package com.plugin.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static com.plugin.config.SysConfig.*;

public class ConfigDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField protocTextField;
	private JTextField outPathTextField;
	private JTextField protofileTextField;

	private Project project;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ConfigDialog dialog = new ConfigDialog(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * Create the dialog.
	 */
	private MainDialog dialog;
	public ConfigDialog(MainDialog owner, Project project) {
	    super(owner, true);
	    this.project = project;
	    this.dialog = owner;
        setResizable(false);
//        setAlwaysOnTop(true);
		Font font = new Font(null, 0, 14);
		setBounds(100, 100, 486, 232);
        setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblNewLabel = new JLabel("   protoc path:");
			lblNewLabel.setFont(font);
			lblNewLabel.setBounds(10, 22, 90, 20);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("protofile path:");
			lblNewLabel_1.setFont(font);
			lblNewLabel_1.setBounds(10, 72, 90, 20);
			contentPanel.add(lblNewLabel_1);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("      out path:");
			lblNewLabel_2.setFont(font);
			lblNewLabel_2.setBounds(10, 122, 90, 20);
			contentPanel.add(lblNewLabel_2);
		}

        String protocPath = getValue(project, PROTOC_PATH);
		protocTextField = new JTextField();
		protocTextField.setFont(font);
		protocTextField.setBounds(105, 16, 251, 30);
		contentPanel.add(protocTextField);
		protocTextField.setColumns(10);
		if (protocPath != null)
		    protocTextField.setText(protocPath);

        String outPath = getValue(project, OUT_PATH);
		outPathTextField = new JTextField();
		outPathTextField.setFont(font);
		outPathTextField.setBounds(105, 116, 251, 30);
		contentPanel.add(outPathTextField);
		outPathTextField.setColumns(10);
		if (outPath != null)
		    outPathTextField.setText(outPath);

        String file_path = getValue(project, FILE_PATH);
		protofileTextField = new JTextField();
		protofileTextField.setFont(font);
		protofileTextField.setBounds(105, 66, 251, 30);
		contentPanel.add(protofileTextField);
		protofileTextField.setColumns(10);
        if (file_path != null) {
            protofileTextField.setText(file_path);
        }
		
		JButton protocBtn = new JButton("Browse");
		protocBtn.setFont(font);
		protocBtn.setBounds(366, 16, 93, 30);
		contentPanel.add(protocBtn);
		protocBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				if (protocPath != null) {
					jfc.setCurrentDirectory(new File(protocPath));
				}
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(new JLabel(), "chose protoc path");
				File file = jfc.getSelectedFile();
//				if (file.isFile()) {
                protocTextField.setText(file.getAbsolutePath());
//				}
			}
		});
		
		JButton fileBtn = new JButton("Browse");
		fileBtn.setFont(font);
		fileBtn.setBounds(366, 66, 93, 30);
		contentPanel.add(fileBtn);
		fileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();

                if (file_path != null) {
                    jfc.setCurrentDirectory(new File(file_path));
                }
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.showDialog(new JLabel(), "chose protofile path");
				File file = jfc.getSelectedFile();
				if (file.isDirectory()) {
					protofileTextField.setText(file.getPath());
				}
			}
		});

		JButton outBtn = new JButton("Browse");
		outBtn.setFont(font);
		outBtn.setBounds(366, 116, 93, 30);
		contentPanel.add(outBtn);
		outBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
                if (outPath != null) {
                    jfc.setCurrentDirectory(new File(outPath));
                }
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.showDialog(new JLabel(), "chose out path");
				File file = jfc.getSelectedFile();
				if (file.isDirectory()) {
					outPathTextField.setText(file.getPath());
				}
			}
		});
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setFont(font);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveResults();
                        dispose();
                    }
                });
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(font);
				cancelButton.setActionCommand("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                buttonPane.add(cancelButton);
			}
		}
	}

    public void saveResults() {
        PropertiesComponent props = PropertiesComponent.getInstance();
        props.setValue(PROTOC_PATH, this.protocTextField.getText());
        props = PropertiesComponent.getInstance(this.project);
        props.setValue(FILE_PATH, this.protofileTextField.getText());
        props.setValue(OUT_PATH, this.outPathTextField.getText());

		dialog.reload();
    }
}
