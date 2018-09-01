/*
Assignment: CS1410 - Project
Program: IconButton
Programmer: 
 * Thackery Archuletta
 * Reynaldo Pena
 * Nathan Orfanos
 * Miles Godfrey
Created: Jul 17, 2018
*/
/**
 * File: IconButton.java
 * Thackery Archuletta
 * Reynaldo Pena
 * Nathan Orfanos
 * Miles Godfrey
 */
package icon;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Class: IconButton - Creates a JButton from an image specified by the user.
 * 
 * @author Thackery Archuletta
 *
 */
public class IconButton {

	/**
	 * Field: filename URL - URL of file
	 */
	private URL filename;
	/**
	 * Field: width int - desired width of image
	 */
	private int width;
	/**
	 * Field: height int - desired height of image
	 */
	private int height;
	/**
	 * Field: imageIcon ImageIcon - the ImageIcon of the image
	 */
	private ImageIcon imageIcon;
	/**
	 * Field: iconButton JButton - button created from image.
	 */
	private JButton iconButton;

	
	/**
	 * Constructor IconButton
	 * @param filename URL of file
	 * @param width desired width of image
	 * @param height desired height of image
	 */
	public IconButton(URL filename, int width, int height) {
		this.filename = filename;
		this.width = width;
		this.height = height;
		//Rescales the image
		imageIcon = rescaleImageIcon(this.filename, this.width, this.height);
		iconButton = new JButton();
		iconButton.setIcon(imageIcon);
	}

	/**
	 * Method rescaleImageIcon - rescales an image file and returns it as an ImageIcon so that
	 * it can be used in a button.
	 * @param filename URL of file
	 * @param width desired width of image
	 * @param height desired height of image
	 * @return ImageIcon
	 */
	private ImageIcon rescaleImageIcon(URL filename, int width, int height) {
		ImageIcon theIcon = new ImageIcon(filename);
		Image theImage = theIcon.getImage();
		Image theNewImage = theImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(theNewImage);
	}

	/**
	 * Method getIconButton - return the icon button
	 * @return JButton
	 */
	public JButton getIconButton() {
		return iconButton;
	}

	/**
	 * Method getImageIcon - returns the ImageIcon
	 * @return ImageIcon
	 */
	public ImageIcon getImageIcon() {
		return imageIcon;
	}

}
