/**
 * TODO: Refactor alles
 */

package client;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.*;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.DefaultCaret;

import net.coobird.thumbnailator.Thumbnails;

public class GuiPanel implements ActionListener
{	
	// alle panels
	private JPanel guipanel = new JPanel(); 
	private JPanel logpanel = new JPanel();
	private JPanel infopanel = new JPanel();
	private JPanel motorpanel = new JPanel();
	private JPanel qrcodepanel = new JPanel();
	private JPanel arrows = new JPanel();
	private JPanel actionsPanel = new JPanel();

	// alle labels
	private JLabel hoogte = new JLabel("Hoogte :");
	private JLabel doelHoogte = new JLabel("Doelhoogte :");
	private JLabel motor1 = new JLabel("   Links");
	private JLabel motor2 = new JLabel("   Rechts");
	private JLabel motor3 = new JLabel("   Onder");
	
	private JLabel qrcode = new JLabel("Meest recente QR-code :");
	private JLabel verwachtVolgnummer = new JLabel("Verwacht volgnummer :");
	private JLabel KpHeight = new JLabel("KpHeight:");
	private JLabel KiHeight = new JLabel("KiHeight:");
	private JLabel KdHeight = new JLabel("KdHeight:");
	private JLabel safetyIntervalHeight = new JLabel("SafetyHeight: ");
	private JLabel KpAngle = new JLabel("KpAngle:");
	private JLabel KiAngle = new JLabel("KiAngle:");
	private JLabel KdAngle = new JLabel("KdAngle:");
	private JLabel safetyIntervalAngle = new JLabel("SafetyAngle: ");

	// alle buttons
	private JButton logfiles = new JButton("Vorige logfiles");
	private JButton setTargetHeight = new JButton("Pas hoogte aan");
	private JButton setKpHeight= new JButton("KpHeight");
	private JButton setKiHeight= new JButton("KiHeight");
	private JButton setKdHeight= new JButton("KdHeight");
	private JButton setSafetyIntervalHeight = new JButton("SafetyHeight");
	private JButton setKpAngle= new JButton("KpAngle");
	private JButton setKiAngle= new JButton("KiAngle");
	private JButton setKdAngle= new JButton("KdAngle");
	private JButton setSafetyIntervalAngle = new JButton("SafetyAngle");
	private BasicArrowButton arrowup = new BasicArrowButton(SwingConstants.NORTH);
	private BasicArrowButton arrowleft = new BasicArrowButton(SwingConstants.WEST);
	private BasicArrowButton arrowright = new BasicArrowButton(SwingConstants.EAST);
	private BasicArrowButton arrowdown = new BasicArrowButton(SwingConstants.SOUTH);
	
	private JTextArea logTextArea = new JTextArea();
	private JTextArea huidigeHoogte = new JTextArea();
	private JTextArea targetHoogte = new JTextArea();
	private JTextArea KpValueHeight = new JTextArea();
	private JTextArea KdValueHeight  = new JTextArea();
	private JTextArea KiValueHeight  = new JTextArea();
	private JTextArea safetyIntervalValueHeight = new JTextArea();
	private JTextArea KpValueAngle = new JTextArea();
	private JTextArea KdValueAngle  = new JTextArea();
	private JTextArea KiValueAngle  = new JTextArea();
	private JTextArea safetyIntervalValueAngle = new JTextArea();

	// lettertype
	private final Font font = new Font("Calibri", Font.PLAIN, 16);

	private GuiController guiController;
	
	/**
	 * Hou de meest recent label die overeenstemt met de meest recente gescande QR-code
	 * bij om het te verwijderen van de QR-code panel voor geheugenmanagement.
	 */
	private JLabel mostRecentQRCodeLabel;

	public GuiPanel() throws RemoteException {
		try {
			guiController = new GuiController();
		} catch (Exception e) {
			System.err.println("Fout bij het verbinden met de zeppelin:");
			e.printStackTrace();
		}
		heightAndMotorWorker motorUpdater = new heightAndMotorWorker();
		logUpdater logUpdater = new logUpdater();
		motorUpdater.execute();
		logUpdater.execute();
	}

	public JPanel setGuipanel() // de frame-constructor methode
	{ 
		guipanel.setLayout(null);

		// voeg panels toe aan GUI
		addPanelToGUI(logpanel, 0, 0, 500, 300);
		
		addPanelToGUI(infopanel, 0, 300, 500, 200);
		addPanelToGUI(motorpanel, 0, 500, 500, 50);
		addPanelToGUI(qrcodepanel, 500, 0, 600, 400);
		addPanelToGUI(arrows, 500, 400, 200, 150);
		addPanelToGUI(actionsPanel, 700, 400, 400, 150);

		// voeg labels toe aan het correcte panel
		addLabelToPanel(motor1, 69, 10, 75, 30, motorpanel);
		addLabelToPanel(motor2, 213, 10, 75, 30, motorpanel);
		addLabelToPanel(motor3, 356, 10, 75, 30, motorpanel);
		addLabelToPanel(qrcode, 5, 300, 400, 30, qrcodepanel);
		addLabelToPanel(verwachtVolgnummer, 5, 320, 400, 30, qrcodepanel);
		
		addLabelToPanel(hoogte, 5, 5, 100, 50, infopanel);		
		addLabelToPanel(doelHoogte, 5, 30, 100, 50, infopanel);
		
		addLabelToPanel(KpHeight, 5, 60, 65, 50, infopanel);
		addLabelToPanel(KdHeight, 160, 60, 65, 50, infopanel);
		addLabelToPanel(KiHeight, 305, 60, 65, 50, infopanel);
		addLabelToPanel(safetyIntervalHeight, 5, 140, 100, 50, infopanel);
		addLabelToPanel(KpAngle, 5, 100, 65, 50, infopanel);
		addLabelToPanel(KdAngle, 160, 100, 65, 50, infopanel);
		addLabelToPanel(KiAngle, 305, 100, 65, 50, infopanel);
		addLabelToPanel(safetyIntervalAngle, 180, 140, 100, 50, infopanel);

		turnLightOff(motor1);
		turnLightOff(motor2);
		turnLightOff(motor3);

		// voeg buttons toe aan hun panel
		addButtonToPanel(logfiles, 350, 270, 150, 30, KeyEvent.VK_L, logpanel);
		
		addButtonToPanel(setTargetHeight, 5, 5, 150, 30, KeyEvent.VK_4, actionsPanel);
		addButtonToPanel(setKpHeight, 5, 40, 55, 30, KeyEvent.VK_4, actionsPanel);
		addButtonToPanel(setKiHeight, 70, 40, 55, 30, KeyEvent.VK_4, actionsPanel);
		addButtonToPanel(setKdHeight, 135, 40, 55, 30, KeyEvent.VK_4, actionsPanel);
		addButtonToPanel(setSafetyIntervalHeight, 5, 80, 100, 30, KeyEvent.VK_4, actionsPanel);

		addButtonToPanel(setKpAngle, 5, 120, 55, 30, KeyEvent.VK_4, actionsPanel);
		addButtonToPanel(setKiAngle, 70, 120, 55, 30, KeyEvent.VK_4, actionsPanel);
		addButtonToPanel(setKdAngle, 135, 120, 55, 30, KeyEvent.VK_4, actionsPanel);
		addButtonToPanel(setSafetyIntervalAngle, 150, 80, 100, 30, KeyEvent.VK_4, actionsPanel);

		addArrowToPanel(arrowup, 75, 25, 50, 50, KeyEvent.VK_UP, arrows, false);
		addArrowToPanel(arrowleft, 25, 75, 50, 50, KeyEvent.VK_LEFT, arrows, false);
		addArrowToPanel(arrowright, 125, 75, 50, 50, KeyEvent.VK_RIGHT, arrows, false);
		addArrowToPanel(arrowdown, 75, 75, 50, 50, KeyEvent.VK_DOWN, arrows, false);
		
		addTimerToArrowButton(arrowup);
		addTimerToArrowButton(arrowleft);
		addTimerToArrowButton(arrowright);
		addTimerToArrowButton(arrowdown);
		
		// voeg text area voor log toe
		addTextAreaToPanelWithScrolling(0, 0, 500, 250, logTextArea, logpanel);
		addTextAreaToPanel(150, 15, 200, 20, huidigeHoogte, infopanel);
		addTextAreaToPanel(150, 45, 200, 20, targetHoogte, infopanel);
		
		addTextAreaToPanel(85, 70, 50, 20, KpValueHeight, infopanel);
		addTextAreaToPanel(230, 70, 50, 20, KdValueHeight, infopanel);
		addTextAreaToPanel(375, 70, 50, 20, KiValueHeight, infopanel);
		addTextAreaToPanel(110, 150, 50, 20, safetyIntervalValueHeight, infopanel);
		addTextAreaToPanel(85, 110, 50, 20, KpValueAngle, infopanel);
		addTextAreaToPanel(230, 110, 50, 20, KdValueAngle, infopanel);
		addTextAreaToPanel(375, 110, 50, 20, KiValueAngle, infopanel);
		addTextAreaToPanel(270, 150, 50, 20, safetyIntervalValueAngle, infopanel);
		
		try {
			targetHoogte.setText(Double.toString(this.guiController.getTargetHeight()));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		try {
			ImageIcon icon = new ImageIcon(ImageIO.read(new File("empty.jpg")));
			mostRecentQRCodeLabel = new JLabel("", icon, JLabel.CENTER);
			mostRecentQRCodeLabel.setLocation(70,10);
			mostRecentQRCodeLabel.setSize(300, 222);
			qrcodepanel.add(mostRecentQRCodeLabel);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return guipanel;
	}



	public void addPanelToGUI(JPanel panel, int x, int y, int width, int length)
	{
		// voeg panel toe aan GUI
		panel.setLayout(null); // zichtbaar maken
		panel.setLocation(x, y); // cošrdinaat van linkerbovenhoek van dit panel op guipanel
		panel.setSize(width, length); // breedte en lengte van dit panel
		panel.setBorder(BorderFactory.createLineBorder(Color.black)); // randen van panel tonen in het zwart

		guipanel.add(panel); // panel toevoegen aan meest algemene panel (guipanel) 
	}

	public void addLabelToPanel(JLabel label, int x, int y, int width, int length, JPanel panel)
	{
		// voeg label toe aan het correcte panel
		label.setLocation(x, y); // cošrdinaat van linkerbovenhoek van label op zijn panel
		label.setSize(width, length); // breedte en lengte van dit label
		label.setFont(font); // lettertype wijzigen

		panel.add(label); // label toevoegen aan correcte panel
	}

	public void addButtonToPanel(JButton button, int x, int y, int width, int length, int key, JPanel panel)
	{
		// voeg button toe aan het correcte panel
		button.setLocation(x, y); // cošrdinaat van linkerbovenhoek van button op zijn panel
		button.setSize(width, length); // breedte en lengte van deze button
		button.setFont(font); // lettertype wijzigen
		//button.setBorder(BorderFactory.createLineBorder(Color.pink)); // maak de randen roos
		button.addActionListener(this); // bindt een actie aan deze button
		button.setMnemonic(key); // verbind de gegeven toets aan deze button, wanneer de toets en alt samen worden ingedrukt wordt de actie erachter getriggerd
		panel.add(button);
	}

	public void addArrowToPanel(BasicArrowButton button, int x, int y, int width, int length, int key, JPanel panel, boolean border)
	{
		// voeg button toe aan het correcte panel
		button.setLocation(x, y); // cošrdinaat van linkerbovenhoek van button op zijn panel
		button.setSize(width, length); // breedte en lengte van deze button
		if (border)
			button.setBorder(BorderFactory.createLineBorder(Color.black)); // maak de randen zwart
		button.setBackground(Color.CYAN);
		button.addActionListener(this); // bindt een actie aan deze button
		button.setMnemonic(key); // verbind de gegeven toets aan deze button, wanneer de toets en alt samen worden ingedrukt wordt de actie erachter getriggerd
		panel.add(button);
	}
	
	public void addTextAreaToPanelWithScrolling(int x, int y, int width, int length, JTextArea textArea, JPanel panel) {
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textArea.setBorder(BorderFactory.createLineBorder(Color.black));
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(x, y, width, length);
		panel.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void addTextAreaToPanel(int x, int y, int width, int length, JTextArea textArea, JPanel panel) {
		textArea.setLineWrap(false);
		textArea.setEditable(false);
		textArea.setBorder(BorderFactory.createLineBorder(Color.black));
		textArea.setSize(width, length);
		textArea.setLocation(x, y);
		panel.add(textArea, BorderLayout.CENTER);
	}
	
	public void addTimerToArrowButton(BasicArrowButton arrowButton) {
		final ButtonModel bModel = arrowButton.getModel();
		final MotorTimer timer = new MotorTimer(arrowButton);
		bModel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent cEvt) {
	            if (bModel.isPressed() && !timer.isRunning()) {
	               timer.start();
	            } else if (!bModel.isPressed() && timer.isRunning()) {
	               timer.stop();
	               try {
					GuiPanel.this.guiController.stopRightAndLeftMotor();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
	            }
	         }
		});
	}

	public void turnLightOff(JLabel light)
	{
		light.setOpaque(true); // achtergrond transparant maken
		light.setBackground(Color.red); // lamp op rood zetten : 'uit'
	}

	public void turnLightOn(JLabel light)
	{
		light.setOpaque(true);
		light.setBackground(Color.green); // lamp op groen zetten : 'aan'
	}
	
	
	public void setVariables() throws RemoteException {
		this.KpValueHeight.setText(Double.toString(guiController.getKpHeight()));
		this.KdValueHeight.setText(Double.toString(guiController.getKdHeight()));
		this.KiValueHeight.setText(Double.toString(guiController.getKiHeight()));
		this.targetHoogte.setText(Double.toString(guiController.getHeight()));
	}
	

	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		
		if (source == setTargetHeight) {
			String input = JOptionPane.showInputDialog(null, "Voer nieuwe doelhoogte in.");
			double height = Double.parseDouble(input);
			try {
				this.guiController.setTargetHeight(height);
			} catch (RemoteException e) {
				JOptionPane.showMessageDialog(null, "Fout bij het aanpassen van doelhoogte, zie standard out");
				e.printStackTrace();
			}
			this.targetHoogte.setText(Double.toString(height));
		}
		else if (source == setKpHeight) {
			String input = JOptionPane.showInputDialog(null, "Voer nieuwe KpHeight in.");
			double kp = Double.parseDouble(input);
			this.guiController.setKpHeight(kp);
			this.KpValueHeight.setText(Double.toString(kp));
		}
		else if (source == setKdHeight) {
			String input = JOptionPane.showInputDialog(null, "Voer nieuwe KdHeight in.");
			double kd = Double.parseDouble(input);
			this.guiController.setKdHeight(kd);
			this.KdValueHeight.setText(Double.toString(kd));
		}
		else if (source == setKiHeight) {
			String input = JOptionPane.showInputDialog(null, "Voer nieuwe KiHeight in.");
			double ki = Double.parseDouble(input);
			this.guiController.setKiHeight(ki);
			this.KiValueHeight.setText(Double.toString(ki));
		}
		else if (source == setSafetyIntervalHeight) {
			String input = JOptionPane.showInputDialog(null, "Voer nieuw safety intervalHeight in.");
			double safety = Double.parseDouble(input);
			try {
				this.guiController.setSafetyIntervalHeight(safety);
			} catch (RemoteException e) {
			}
			this.safetyIntervalValueHeight.setText(Double.toString(safety));
		}
		
		else if (source == setKpAngle) {
			String input = JOptionPane.showInputDialog(null, "Voer nieuwe KpAngle in.");
			double kp = Double.parseDouble(input);
			this.guiController.setKpAngle(kp);
			this.KpValueAngle.setText(Double.toString(kp));
		}
		else if (source == setKdAngle) {
			String input = JOptionPane.showInputDialog(null, "Voer nieuwe KdAngle in.");
			double kd = Double.parseDouble(input);
			this.guiController.setKdAngle(kd);
			this.KdValueAngle.setText(Double.toString(kd));
		}
		else if (source == setKiAngle) {
			String input = JOptionPane.showInputDialog(null, "Voer nieuwe KiAngle in.");
			double ki = Double.parseDouble(input);
			this.guiController.setKiAngle(ki);
			this.KiValueAngle.setText(Double.toString(ki));
		}
		else if (source == setSafetyIntervalAngle) {
			String input = JOptionPane.showInputDialog(null, "Voer nieuw safety intervalAngle in.");
			double safety = Double.parseDouble(input);
			try {
				this.guiController.setSafetyIntervalAngle(safety);
			} catch (RemoteException e) {
			}
			this.safetyIntervalValueAngle.setText(Double.toString(safety));
		}
		else if(source == logfiles)
		{   
			//Logfile aangemaakt en opgeslagen.
			PrintWriter writer = null;
			try {
				writer = new PrintWriter("logfile.txt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			//Tekst schrijven naar de logfile.
			writer.println(logTextArea.getText());
			writer.close();

			//Logfile-dialog
			JTextArea textLog = new JTextArea(50,60); //JTextArea is een 'multi-line area' waar gewone tekst getoond kan worden. 
			try {
				textLog.read(new FileReader("logfile.txt"), null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} //Leest de text uit de logfile en zet het in de JTextArea.
			textLog.setEditable(false);

			JOptionPane.showMessageDialog(null, new JScrollPane(textLog),"Logfile", JOptionPane.PLAIN_MESSAGE); //JScrollPane zorgt ervoor dat je kan scrollen in de logfile (horizontaal en verticaal).
		}  

	}


	private void showImage(ImageIcon image)
	{
		JLabel label = new JLabel("", image, JLabel.CENTER);
		if (this.mostRecentQRCodeLabel != null)
		{
			qrcodepanel.remove(this.mostRecentQRCodeLabel);
			qrcodepanel.revalidate();
			qrcodepanel.repaint();
		}
		this.mostRecentQRCodeLabel = label;
		this.mostRecentQRCodeLabel.setSize(300,222);
		this.mostRecentQRCodeLabel.setLocation(70, 10);
		//JOptionPane.showMessageDialog(null, label);
		qrcodepanel.add(label);
	}

	private static void createAndShowGUI() throws RemoteException, NotBoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	        if ("Nimbus".equals(info.getName())) {
	            UIManager.setLookAndFeel(info.getClassName());
	            break;
	        }
	    }
		
		
		//JFrame.setDefaultLookAndFeelDecorated(true);
		final JFrame frame = new JFrame("GUI P&O");

		// aanmaken van de guipanel
		final GuiPanel guipanel = new GuiPanel();

		// aanmaken scrollpane
		JPanel gui = guipanel.setGuipanel();  
		
		//JScrollPane scrollpane = new JScrollPane(gui);   
		//gui.setPreferredSize(new Dimension(1150, 700)); // bepaalt de grootte van het scrollgebied, dus tot waar we kunnen scrollen om de gui te zien

		// aanmaken en opzetten van de content pane
		frame.add(gui);
		// zorgt ervoor dat de zeppelin veilig afsluit wanneer je op de
		// X-knop drukt
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(frame, 
						"Uitvoering stoppen?", "Applicatie afsluiten", 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					guipanel.guiController.exit();
				}
			}
		});
		frame.setSize(1100, 600);
		frame.setVisible(true);
		guipanel.setVariables();
	}

	public static void main(String[] args) throws RemoteException, NotBoundException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						createAndShowGUI();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (UnsupportedLookAndFeelException e) {
						e.printStackTrace();
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
			String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	private class heightAndMotorWorker extends SwingWorker<Void, Void> {
		
		boolean leftOn;
		boolean rightOn;
		boolean downwardOn;
		
		String[] info;
		ImageIcon icon;
		
		public Void doInBackground() throws RemoteException, InterruptedException {
			while (true) {
				leftOn = GuiPanel.this.guiController.leftIsOn();
				rightOn = GuiPanel.this.guiController.rightIsOn();
				downwardOn = GuiPanel.this.guiController.downwardIsOn();
				if (GuiPanel.this.guiController.qrCodeAvailable()) {
					System.out.println("QR-code beschikbaar!");
					try {
						info = GuiPanel.this.guiController.getLastScannedQrCodeInfo();
						System.out.println(info[1]);
						BufferedImage image = Thumbnails.of(GuiPanel.this.guiController.getImageFromFile(info[0])).size(300, 222).asBufferedImage();
						icon = new ImageIcon(image);
						System.out.println("Icon gemaakt.");
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (FTPIllegalReplyException e) {
						e.printStackTrace();
					} catch (FTPException e) {
						e.printStackTrace();
					} catch (FTPDataTransferException e) {
						e.printStackTrace();
					} catch (FTPAbortedException e) {
						e.printStackTrace();
					}
				}
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							GuiPanel.this.huidigeHoogte.setText(Double.toString(GuiPanel.this.guiController.getHeight()));
							GuiPanel.this.targetHoogte.setText(Double.toString(GuiPanel.this.guiController.getTargetHeight()));
							if (GuiPanel.this.guiController.qrCodeAvailable()) {
								System.out.println("Proberen afbeelding te tonen en gedecodeerde.");
								showImage(icon);
								showDecoded();
							}
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						decideLightColours();
						try {
							if (GuiPanel.this.guiController.qrCodeAvailable()) {
								GuiPanel.this.guiController.consumeQRCode();
							}
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				});
				Thread.sleep(1000);
			}
		}
		
		private void decideLightColours() {
			if (leftOn)
				GuiPanel.this.turnLightOn(GuiPanel.this.motor1);
			else GuiPanel.this.turnLightOff(GuiPanel.this.motor1);
			if (rightOn)
				GuiPanel.this.turnLightOn(GuiPanel.this.motor2);
			else GuiPanel.this.turnLightOff(GuiPanel.this.motor2);
			if (downwardOn)
				GuiPanel.this.turnLightOn(GuiPanel.this.motor3);
			else GuiPanel.this.turnLightOff(GuiPanel.this.motor3);
		}
		
		private void showImage(ImageIcon icon) {
			GuiPanel.this.showImage(icon);
		}
		
		private void showDecoded() {
			GuiPanel.this.qrcode.setText("Meest recente QR-code: " + info[1]);
		}
	}
	
	private class logUpdater extends SwingWorker<Void, Void> {
		
		private String logText;
		
		public Void doInBackground() throws InterruptedException {
			while (true) {
				try {
					logText = GuiPanel.this.guiController.readLog();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							GuiPanel.this.logTextArea.append(logText);
						}
					});
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (FTPIllegalReplyException e) {
					e.printStackTrace();
				} catch (FTPException e) {
					e.printStackTrace();
				} catch (FTPDataTransferException e) {
					e.printStackTrace();
				} catch (FTPAbortedException e) {
					e.printStackTrace();
				}
				Thread.sleep(1000);
			}
		}
	}
	
	private class MotorTimer extends Timer {

		BasicArrowButton arrowButton;
		
		public MotorTimer(BasicArrowButton arrowButton) throws IllegalArgumentException {
			super(100, null);
			if (! (arrowButton == GuiPanel.this.arrowup || arrowButton == GuiPanel.this.arrowleft ||
					arrowButton == GuiPanel.this.arrowright || arrowButton == GuiPanel.this.arrowdown)) {
				throw new IllegalArgumentException("Timerconstructor moet bestaande pijltoets hebben als argument");
			}
			this.arrowButton = arrowButton;
			ActionListener listener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (MotorTimer.this.arrowButton == GuiPanel.this.arrowup) {
						try {
							GuiPanel.this.guiController.goForward();
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
					else if (MotorTimer.this.arrowButton == GuiPanel.this.arrowleft) {
						try {
							GuiPanel.this.guiController.goLeft();
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
					else if (MotorTimer.this.arrowButton == GuiPanel.this.arrowright) {
						try {
							GuiPanel.this.guiController.goRight();
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
					else if (MotorTimer.this.arrowButton == GuiPanel.this.arrowdown) {
						try {
							GuiPanel.this.guiController.goBackward();
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				}
				
			};
			super.removeActionListener(null);
			super.addActionListener(listener);
		}
	}
}
