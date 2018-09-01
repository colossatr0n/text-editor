/*
Assignment: CS1410 - Project
Program: FileHandlingHelpers
Programmer:
 * Thackery Archuletta
 * Reynaldo Pena
 * Nathan Orfanos
 * Miles Godfrey
Created: Jul 25, 2018
*/

/**
 * File: FileHandlingHelpers.java
 * Thackery Archuletta
 * Reynaldo Pena
 * Nathan Orfanos
 * Miles Godfrey
 */
package gui;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
* Class: FileHandlingHelpers
* File handling functions designed to accompany a GUI. Includes save, save as,
* open, and new functionality. 
 *
 */
public class FileHandlingHelpers {
	
	/**
	 * Field: gui Component - The gui component that gets passed through the constructor
	 */
	private static Component gui;
	
	
	/**
	 * Constructor FileHandlingHelpers
	 * @param gui gui component
	 */
	public FileHandlingHelpers(Component gui) {
		this.gui = gui;
	}

	/**
	 * Method Save - Saves text to a file that already exists. If the file DNE, SaveAs() is called.
	 * @param textFileName saved text file
	 * @param textArea JTextArea of the gui component. Used to get text which will be the content of the file.
	 * @return saved text file
	 */
	public static File Save(File textFileName, JTextArea textArea) {
		// If text file name is null or the text file DNE, then SaveAs() will be used instead.
		if (textFileName != null && textFileName.exists()) {
			BufferedWriter outputFile = null;
			try {
				// Prepare the output file.
				outputFile = new BufferedWriter(new FileWriter(textFileName));
				// Write text from the text area to the output file
				textArea.write(outputFile);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (outputFile != null) {
					try {
						outputFile.close();
					} catch (IOException ex) {
					}
				}
			}
		}
		else { // File DNE or the file name was null. Saves as a new file.
			textFileName = SaveAs(textFileName, textArea);
		}
		return textFileName;
	}

	/**
	 * Method SaveAs - creates text file and saves to it.
	 * @param textFileName saved text file
	 * @param textAreax JTextArea of the gui component. Used to get text which will be the content of the file.
	 * @return saved text file
	 */
	public static File SaveAs(File textFileName, JTextArea textArea) {
		// File chooser window
		JFileChooser SaveAsWindow = new JFileChooser();
		// Only allow .txt
		FileFilter textFileFilter = new FileNameExtensionFilter("Text File", "txt");
		SaveAsWindow.setFileFilter(textFileFilter);
		SaveAsWindow.setApproveButtonText("Save");
		// Get output of the window
		int statusOutput = SaveAsWindow.showOpenDialog(gui);
		if (statusOutput != JFileChooser.APPROVE_OPTION) {
			return textFileName;
		}
		// create the new text file
		textFileName = new File(SaveAsWindow.getSelectedFile() + "");
		if (!textFileName.getAbsolutePath().endsWith(".txt")) {
			textFileName = new File(textFileName.getAbsolutePath() + ".txt");
		}
		int response = -1;
		// Create alert for if the file already exists.
		JOptionPane alert = new JOptionPane();
		if (textFileName.exists()) {
			response = alert.showConfirmDialog(gui, "The file already exists. Would you like to overwrite it?", "File Already Exists", JOptionPane.YES_NO_OPTION);
		}
		if (response != alert.NO_OPTION) {
			BufferedWriter outputFile = null;
			try {
				// Prepare the output file
				outputFile = new BufferedWriter(new FileWriter(textFileName));
				// 
				textArea.write(outputFile);
				// Write text from the text area to the output file
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (outputFile != null) {
					try {
						outputFile.close();
					} catch (IOException ex) {
					}
				}
			}	
		}
		return textFileName;
	}

	/**
	 * Method Open - Open an existing .txt file
	 * @param textFileName text file 
	 * @param textArea JTextArea of the gui component. Used to get text which will be the content of the file.
	 * @return text file
	 */
	public static File Open(File textFileName, JTextArea textArea) {
		// File chooser
		JFileChooser OpenWindow = new JFileChooser();
		// Only allow .txt files
		FileFilter textFileFilter = new FileNameExtensionFilter("Text File", "txt");
		OpenWindow.setFileFilter(textFileFilter);
		// Get output of window
		OpenWindow.setApproveButtonText("Open");
		int statusOutput = OpenWindow.showOpenDialog(gui);
		if (statusOutput != JFileChooser.APPROVE_OPTION) {
			return textFileName;
		}
		// set text file
		textFileName = new File(OpenWindow.getSelectedFile() + "");
		Writer inputFile = null;
		BufferedReader input = null;
		try {
			// Read from file
			FileReader reader = new FileReader(textFileName);
			BufferedReader buffReader = new BufferedReader(reader);
			// Have text area read the the file and write it to itself.
			textArea.read(buffReader, null);
			buffReader.close();

		} catch (Exception e) {
			System.out.println("Error");
		}
		return textFileName;
	}
	
	/**
	 * Method New - starts a new text file
	 * @param textFileName text file 
	 * @param textArea JTextArea of the gui component. Used to get text which will be the content of the file.
	 * @return
	 */
	public static File New(File textFileName, JTextArea textArea) {
		// reset text file name
		textFileName = null;
		// reset text area
		textArea.setText("");
		return textFileName;
	}

}
