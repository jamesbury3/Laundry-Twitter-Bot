package laundryApp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Scanner;

import twitter4j.TwitterException;

public class Main {
	public static void main(String[] args) throws TwitterException, FileNotFoundException {
		
		Scanner s = new Scanner(new File("Laundry App Instructions.txt"));
		String instructions = "<html><font size=\"+2\">";
		while(s.hasNextLine()) { 
			instructions += s.nextLine() + "<br/>"; 
		}
		instructions += "</font></html>";
		
		JLabel instructions_label = new JLabel(instructions);
		
		JPanel wrapper = new JPanel();
		wrapper.setLayout(new GridLayout(1,1));
		wrapper.add(instructions_label);
		
		JFrame frame = new JFrame("Laundry App");
		frame.setSize(500, 500);
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		frame.setContentPane(new NameView().getPanel());
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.add(wrapper);
		frame.setUndecorated(true);
		frame.setVisible(true);
	}
}
