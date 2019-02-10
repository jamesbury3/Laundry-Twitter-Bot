package laundryApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  

public class NameView implements ActionListener {
	
	private JPanel washPanel;
	private JPanel dryPanel;
	private JPanel mainPanel;
	private JButton[] washButtons;
	private JButton[] dryButtons;
	private String[] names;
	private Date date2;
	private boolean wash_in_use;
	private boolean dryer_in_use;
	private JLabel lastUsedPanel;
	private String last_used_wash;
	private String last_used_dry;
	private String last_used_text;
	private int wash_index;
	private int dry_index;
	final private String a;
	final private String b;
	final private String c;
	final private String d;
	final private JLabel details;
	private int washTime;
	private int dryTime;
	
	public NameView() {
		washTime = 0;
		dryTime = 0;
		washPanel = new JPanel();
		washPanel.setLayout(new GridLayout(4, 2));
		washPanel.setBackground(Color.WHITE);
		dryPanel = new JPanel();
		dryPanel.setLayout(new GridLayout(4, 2));
		dryPanel.setBackground(Color.WHITE);
		a = "<html><font size=\"+3\">";
		b = "</font></html>";
		c = "<div style='text-align: center;'>";
		d = "</div>";
		last_used_text = "<html><font size=\"+2\">Washing Machine Last Used By: <br/>"
				+ "Dryer Last Used By: ";
		details = new JLabel(a+"No longer connected"
				+ " to the internet"+b);
		details.setHorizontalAlignment(JLabel.CENTER);
		details.setVerticalAlignment(JLabel.CENTER);
		lastUsedPanel = new JLabel(last_used_text);
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,3));
		last_used_wash = "";
		last_used_dry = "";
		wash_index = 0;
		dry_index = 0;
		
		wash_in_use = false;
		dryer_in_use = false;
		
		names = new String[8];
		names[0] = "Name 1";
		names[1] = "Name 2";
		names[2] = "Name 3";
		names[3] = "Name 4";
		names[4] = "Name 5";
		names[5] = "Name 6";
		names[6] = "Name 7";
		names[7] = "Name 8";
 		
		washButtons = new JButton[names.length];
		for(int i = 0; i < names.length; i++) {
			washButtons[i] = new JButton(a +" " + names[i] + " Washing Machine" + b);
			washButtons[i].addActionListener(this);
			washButtons[i].setActionCommand(names[i].toLowerCase()+"wash");
		}
		
		dryButtons = new JButton[names.length];
		for(int i = 0; i < names.length; i++) {
			dryButtons[i] = new JButton(a +" " + names[i] + " Dryer" + b);
			dryButtons[i].addActionListener(this);
			dryButtons[i].setActionCommand(names[i].toLowerCase()+"dry");
		}
		
		for(int i = 0; i < names.length; i++) {
			washButtons[i].setBackground(Color.CYAN);
			washPanel.add(washButtons[i]);
			dryButtons[i].setBackground(Color.YELLOW);
			dryPanel.add(dryButtons[i]);
		}
		
		mainPanel.add(lastUsedPanel);
		mainPanel.add(washPanel);
		mainPanel.add(dryPanel);
	}
	
	public JPanel getPanel() {
		return mainPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();
		
		int i = 0;
		
		if(cmd.contains("wash")) {
			for(i = 0; i < names.length; i++) {
				if(cmd.toLowerCase().contains(names[i].toLowerCase())) {
					if(!wash_in_use) {
						setLastWash(names[i]);
						setLastUsedByText();
						setWashIndex(i);
						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");  
					    Date date = new Date();  
					    System.out.println(formatter.format(date));
					    
					    washButtons[getWashIndex()].setBackground(Color.RED);
						wash_in_use = true;
						
						Twitter twitter = TwitterFactory.getSingleton();
					    Status status;
						try {
							status = twitter.updateStatus(names[getWashIndex()] + " is using washing machine at " + formatter.format(date));
							
							System.out.println("Successfully updated the status");
							System.out.println(names[getWashIndex()] + " is using washing machine");
							new java.util.Timer().scheduleAtFixedRate( 
							        new java.util.TimerTask() {
							            int i = 60;
							            public void run() {
							            	
							            	washButtons[getWashIndex()].setText(a + names[getWashIndex()] + " Washing Machine: " 
							            	+ i + " mins left" + b);
							            	
							            	if(i == 0) {
							            		Twitter twitter = TwitterFactory.getSingleton();
							            		Status status;
								    			try {
								    				date2 = new Date();
								    				status = twitter.updateStatus(names[getWashIndex()] + "'s clothes are probably done in the wash now: " + formatter.format(date2));
								    				washButtons[getWashIndex()].setBackground(Color.CYAN);
								    				washButtons[getWashIndex()].setText(a +" " + names[getWashIndex()] + " Washing Machine" + b);
									    			wash_in_use = false;
									    			cancel();
								    			} catch (TwitterException e1) {
								    				// TODO Auto-generated catch block
								    				e1.printStackTrace();
								    			}
							            	}
							            	i--;
							            }
							        }, 0, 60 * 1000
							);
						} 
						catch (TwitterException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							JFrame frame = new JFrame("Error Message");
		    				frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
							frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
							frame.getContentPane().add(details);
							frame.setUndecorated(true);
							frame.setVisible(true);
							System.out.println("There is an error");
							
							washButtons[getWashIndex()].setBackground(Color.CYAN);
			    			wash_in_use = false;
						}
					}
				}
			}
		} else {
			for(i = 0; i < names.length; i++) {
				if(cmd.toLowerCase().contains(names[i].toLowerCase())) {
					if(!dryer_in_use) {
						setLastDry(names[i]);
						setLastUsedByText();
						setDryIndex(i);
						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");  
					    Date date = new Date();  
					    System.out.println(formatter.format(date));
					    
					    dryButtons[getDryIndex()].setBackground(Color.RED);
						dryer_in_use = true;
						
						Twitter twitter = TwitterFactory.getSingleton();
					    Status status;
						try {
							status = twitter.updateStatus(names[getDryIndex()] + " is using dryer at " + formatter.format(date));
							
							System.out.println("Successfully updated the status");
							System.out.println(names[getDryIndex()] + " is using the dryer");
							new java.util.Timer().scheduleAtFixedRate( 
							        new java.util.TimerTask() {
							            int i = 60;
							            public void run() {
							            	
							            	dryButtons[getDryIndex()].setText(a + names[getDryIndex()] + " Dryer: " 
							            	+ i + " mins left" + b);
							            	
							            	if(i == 0) {
							            		Twitter twitter = TwitterFactory.getSingleton();
							            		Status status;
								    			try {
								    				date2 = new Date();
								    				status = twitter.updateStatus(names[getDryIndex()] + "'s clothes are probably done in the dryer now: " + formatter.format(date2));
								    				dryButtons[getDryIndex()].setBackground(Color.CYAN);
								    				dryButtons[getDryIndex()].setText(a +" " + names[getDryIndex()] + " Dryer " + b);
									    			dryer_in_use = false;
									    			cancel();
								    			} catch (TwitterException e1) {
								    				// TODO Auto-generated catch block
								    				e1.printStackTrace();
								    			}
							            	}
							            	i--;
							            }
							        }, 0, 60 * 1000
							);
						} 
						catch (TwitterException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							JFrame frame = new JFrame("Error Message");
		    				frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
							frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
							frame.getContentPane().add(details);
							frame.setUndecorated(true);
							frame.setVisible(true);
							System.out.println("There is an error");
							
							dryButtons[getDryIndex()].setBackground(Color.CYAN);
			    			dryer_in_use = false;
						}
					}
				}
			}
		}
	}
	
	public void setWashIndex(int i) {
		wash_index = i;
	}
	
	public int getWashIndex() {
		return wash_index;
	}
	
	public void setDryIndex(int i) {
		dry_index = i;
	}
	
	public int getDryIndex() {
		return dry_index;
	}
	
	public void setLastUsedByText() {
		lastUsedPanel.setText("<html><font size=\"+2\">Washing Machine Last Used By: " + getLastWash() + "<br/>" 
								+ "Dryer Last Used By: " + getLastDry());
	}
	
	public void setLastWash(String s) {
		last_used_wash = s;
	}
	
	public String getLastWash() {
		return last_used_wash;
	}
	
	public void setLastDry(String s) {
		last_used_dry = s;
	}
	
	public String getLastDry() {
		return last_used_dry;
	}
}
