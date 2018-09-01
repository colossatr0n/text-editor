/*
Assignment: CS1410 - Project
Program: TextEditor
Programmer: 
 * Thackery Archuletta
 * Reynaldo Pena
 * Nathan Orfanos
 * Miles Godfrey
Created: Jul 17, 2018
*/

/**
 * File: TextEditor.java
 * Thackery Archuletta
 * Reynaldo Pena
 * Nathan Orfanos
 * Miles Godfrey
 */
package gui;

import icon.IconButton;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

/**
 * Class: TextEditor
 * Creates a text editor GUI that allows user to type, save text files,
 * open text files, and create new text files. Also has a line number 
 * functionality to keep track of line numbers.
 */

public class TextEditor extends JFrame {

	/**
	 * Field: textArea JTextArea - text area in which user types 
	 */
	private JTextArea textArea;
	/**
	 * Field: textFileName File - text file
	 */
	private File textFileName = null;
	/**
	 * Field: lineNumber int - keeps track of line numbers
	 */
	private int lineNumber = 0;
	/**
	 * Field: numList Set<Integer> - number set that keeps track of existing line numbers
	 */
	private Set<Integer> numList = new HashSet<>();
	/**
	 * Field: firstLineSet boolean - keeps track of whether or not the first line number has been set.
	 * Needed for line number functionality.
	 */
	private boolean firstLineSet = false;
	/**
	 * Field: stringNumbers List<String> - keeps a string representation of the line numbers to facilitate printing them to the GUI.
	 */
	private List<String> stringNumbers = new ArrayList<>();
	/**
	 * Field: max List<Integer> - keeps track of the max line number
	 */
	private List<Integer> max = new ArrayList<>();
	/**
	 * Field: lineNumTextArea JTextArea - uneditable text area where the line numbers are shown
	 */
	private JTextArea lineNumTextArea;

	/**
	 * Constructor TextEditor - constructs GUI.
	 */
	public TextEditor() {

		setVisible(true);
		setTitle("TextEditor");
		setSize(550, 400);
		getContentPane().setBackground(new Color(112, 128, 144));
		// Getting middle of screen
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		// Sets frame to middle of screen no matter what monitor it's on.
		setLocation(width / 2 - getSize().width / 2, height / 2 - getSize().height / 2);

		// Exit Prompt
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int action = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit?",
						JOptionPane.YES_NO_OPTION);
				if (action == 0) {
					System.exit(0);
				}
			}
		});

		// Uniform look and feel across all devices
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Creating menu bar items
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("New");
		mntmNewMenuItem.addActionListener(newActionListener());
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(openActionListener());
		mnNewMenu.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(saveActionListener());
		mnNewMenu.add(mntmSave);

		JMenuItem mntmSaveAs = new JMenuItem("Save As");
		mntmSaveAs.addActionListener(saveAsActionListener());
		mnNewMenu.add(mntmSaveAs);

		// Creating main text area and adding a listener to the caret/cursor
		textArea = new JTextArea();
		textArea.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				calculateLineNumber();
			}
		});
		textArea.setOpaque(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(new EmptyBorder(6, 0, 0, 0));
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		// Creating the toolbar, JButtons, and JSpinner that belong to the toolbar.
		JToolBar toolBar = new JToolBar();

		JButton saveButton = new IconButton(getURL("images/saveIcon.png"), 15, 15).getIconButton();
		saveButton.addActionListener(saveActionListener());

		JButton saveAsButton = new IconButton(getURL("images/saveAsIcon.png"), 15, 15).getIconButton();
		saveAsButton.addActionListener(saveAsActionListener());

		JButton openButton = new IconButton(getURL("images/openIcon.png"), 15, 15).getIconButton();
		openButton.addActionListener(openActionListener());

		JButton newButton = new IconButton(getURL("images/newIcon.png"), 15, 15).getIconButton();
		newButton.addActionListener(newActionListener());

		JSpinner spinner = new JSpinner();
		spinner.setValue(textArea.getFont().getSize());
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int textSize = (int) spinner.getValue();
				textArea.setFont(new Font("Dialog", Font.PLAIN, textSize));
				lineNumTextArea.setFont(new Font("Dialog", Font.PLAIN, textSize));
			}
		});
		spinner.setMaximumSize(new Dimension(45, (int) newButton.getPreferredSize().getHeight()));
		// Adding components to toolbar.
		toolBar.add(saveButton);
		toolBar.add(saveAsButton);
		toolBar.add(openButton);
		toolBar.add(newButton);
		toolBar.add(spinner);
		getContentPane().add(toolBar, BorderLayout.NORTH);

		// Creating line number components
		JPanel lineNumPanel = new JPanel();
		lineNumTextArea = new JTextArea();
		lineNumPanel.add(lineNumTextArea);
		lineNumTextArea.setEditable(false);
		lineNumTextArea.setBackground(new Color(238, 238, 238));

		JScrollPane scrollLineNum = new JScrollPane(lineNumPanel);
		scrollLineNum.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollLineNum.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		getContentPane().add(scrollLineNum, BorderLayout.WEST);
		scrollLineNum.setPreferredSize(new Dimension(30, scrollLineNum.getPreferredSize().height));
	}

	/**
	 * Method getURL gets the url based off the file name
	 * @param filename text file name
	 * @return URL of the file
	 */
	private URL getURL(String filename) {
		URL fileURL = this.getClass().getResource(filename);
		return fileURL;
	}

	/**
	 * Method saveActionListener - returns action listener for Save button
	 * @return action listener for Save button
	 */
	private ActionListener saveActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFileName = FileHandlingHelpers.Save(textFileName, textArea);
			}
		};
	}
	/**
	 * Method saveAsActionListener - returns action listener for SaveAs button
	 * @return action listener for SaveAs button
	 */
	private ActionListener saveAsActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFileName = FileHandlingHelpers.SaveAs(textFileName, textArea);
			}
		};
	}
	/**
	 * Method openActionListener - returns action listener for Open button
	 * @return action listener for Open button
	 */
	private ActionListener openActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFileName = FileHandlingHelpers.Open(textFileName, textArea);
			}
		};
	}
	/**
	 * Method newActionListener - returns action listener for New button
	 * @return action listener for New button
	 */
	private ActionListener newActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFileName = FileHandlingHelpers.New(textFileName, textArea);
			}
		};
	}

	/**
	 * Method calculateLineNumber - calculates line numbers based off caret position
	 */
	private void calculateLineNumber() {
		// Getting font size + 3 and setting it as the divisor
		Integer divisor = (Integer) textArea.getFont().getSize() + 3;
		try {
			// Getting y position
			Integer dividend = (int) textArea.getCaret().getMagicCaretPosition().getY();
			// Calculating line number
			lineNumber = (int) dividend / divisor;
		} catch (NullPointerException e) {
			// Starting position of caret returns null, hence the catch statement. Line number
			// needs to be zero if caret returns null.
			if (firstLineSet == false) {
				this.lineNumber = 0;
				firstLineSet = true;
			}
		}
		// add line number to the set if it DNE
		if (!numList.contains(lineNumber)) {
			numList.add(lineNumber);
			if (lineNumber == 0) {
				String s = "" + (lineNumber + 1);
				lineNumTextArea.append(s);
				stringNumbers.add(s);
				max.add(lineNumber);

			} else {
				String s = "\n" + (lineNumber + 1);
				lineNumTextArea.append(s);
				stringNumbers.add(s);
				max.add(lineNumber);
			}
		} else { // remove max line number if caret position is above/less than most recent line numbers
			String updatedNumbers = "";
			while (true) {
				if (!numList.contains(lineNumber + 1))
					break;
				String currentNumbers = lineNumTextArea.getText();
				updatedNumbers = currentNumbers.substring(0,
						currentNumbers.length() - stringNumbers.get(stringNumbers.size() - 1).length());
				numList.remove(max.get(max.size() - 1));
				max.remove(max.size() - 1);
				stringNumbers.remove(stringNumbers.size() - 1);
				lineNumTextArea.setText(updatedNumbers);
			}
		}
	}

	public static void main(String[] args) {
		TextEditor gui = new TextEditor();
		FileHandlingHelpers fileHelper = new FileHandlingHelpers(gui);
	}

}
