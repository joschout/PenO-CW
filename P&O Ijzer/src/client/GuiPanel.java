/**
 * TODO: Refactor alles
 */

package client;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.DefaultCaret;

public class GuiPanel implements ActionListener
{	
	// alle panels
	private JPanel guipanel = new JPanel(); 
	private JPanel logpanel = new JPanel();
	private JPanel infopanel = new JPanel();
	private JPanel motorpanel = new JPanel();
	private JPanel qrcodepanel = new JPanel();
	private JPanel arrows = new JPanel();

	// alle labels
	private JLabel log = new JLabel("Log :");
	private JLabel info = new JLabel("Info :");
	private JLabel hoogte = new JLabel("Hoogte :");
	private JLabel motor1 = new JLabel("Motor 1 :");
	private JLabel motor2 = new JLabel("Motor 2 :");
	private JLabel motor3 = new JLabel("Motor 3 :");
	private JLabel motor4 = new JLabel("Motor 4 :");
	private JLabel qrcode = new JLabel("Meest recente QR-code :");
	private JLabel lamp1 = new JLabel("");
	private JLabel lamp2 = new JLabel("");
	private JLabel lamp3 = new JLabel("");
	private JLabel lamp4 = new JLabel("");

	// alle buttons
	private JButton logfiles = new JButton("Vorige logfiles");
	private JButton scanQRCode = new JButton("Scan QR-code");
	private BasicArrowButton arrowup = new BasicArrowButton(SwingConstants.NORTH);
	private BasicArrowButton arrowleft = new BasicArrowButton(SwingConstants.WEST);
	private BasicArrowButton arrowright = new BasicArrowButton(SwingConstants.EAST);
	private BasicArrowButton arrowdown = new BasicArrowButton(SwingConstants.SOUTH);
	
	private JTextArea logTextArea;

	// lettertype
	private final Font font = new Font("Calibri", Font.BOLD, 16);

	private GuiController guiController;
	
	/**
	 * Hou de meest recent label die overeenstemt met de meest recente gescande QR-code
	 * bij om het te verwijderen van de QR-code panel voor geheugenmanagement.
	 */
	private JLabel mostRecentQRCodeLabel;

	public GuiPanel() {
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
		addPanelToGUI(logpanel, 0, 0, 300, 1000);
		addPanelToGUI(infopanel, 300, 0, 700, 500);
		addPanelToGUI(motorpanel, 300, 500, 300, 250);
		addPanelToGUI(qrcodepanel, 600, 500, 400, 500);
		addPanelToGUI(arrows, 300, 750, 300, 250);

		// voeg labels toe aan het correcte panel
		addLabelToPanel(log, 5, 0, 100, 30, logpanel);
		addLabelToPanel(info, 5, 0, 100, 30, infopanel);
		addLabelToPanel(motor1, 5, 25, 100, 30, motorpanel);
		addLabelToPanel(motor2, 5, 75, 100, 30, motorpanel);
		addLabelToPanel(motor3, 5, 125, 100, 30, motorpanel);
		addLabelToPanel(motor4, 5, 175, 100, 30, motorpanel);
		addLabelToPanel(qrcode, 5, 0, 400, 30, qrcodepanel);
		addLabelToPanel(hoogte, 5, 0, 400, 100, infopanel);

		addLabelToPanel(lamp1, 200, 30, 20, 20, motorpanel);
		turnLightOff(lamp1);
		addLabelToPanel(lamp2, 200, 80, 20, 20, motorpanel);
		turnLightOff(lamp2);
		addLabelToPanel(lamp3, 200, 130, 20, 20, motorpanel);
		turnLightOff(lamp3);
		addLabelToPanel(lamp4, 200, 180, 20, 20, motorpanel);
		turnLightOff(lamp4);

		// voeg buttons toe aan hun panel
		addButtonToPanel(logfiles, 25, 850, 250, 30, KeyEvent.VK_L, logpanel);
		addButtonToPanel(scanQRCode, 200, 5, 100, 30, KeyEvent.VK_3, qrcodepanel);

		addArrowToPanel(arrowup, 115, 55, 70, 70, KeyEvent.VK_UP, arrows);
		addArrowToPanel(arrowleft, 45, 125, 70, 70, KeyEvent.VK_LEFT, arrows);
		addArrowToPanel(arrowright, 185, 125, 70, 70, KeyEvent.VK_RIGHT, arrows);
		addArrowToPanel(arrowdown, 115, 125, 70, 70, KeyEvent.VK_DOWN, arrows);
		
		// voeg text area voor log toe
		addLogTextAreaToLogPanel(5, 30, 275, 750);

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
		button.setBorder(BorderFactory.createLineBorder(Color.pink)); // maak de randen roos
		button.addActionListener(this); // bindt een actie aan deze button
		button.setMnemonic(key); // verbind de gegeven toets aan deze button, wanneer de toets en alt samen worden ingedrukt wordt de actie erachter getriggerd
		panel.add(button);
	}

	public void addArrowToPanel(BasicArrowButton button, int x, int y, int width, int length, int key, JPanel panel)
	{
		// voeg button toe aan het correcte panel
		button.setLocation(x, y); // cošrdinaat van linkerbovenhoek van button op zijn panel
		button.setSize(width, length); // breedte en lengte van deze button
		button.setBorder(BorderFactory.createLineBorder(Color.black)); // maak de randen zwart
		button.addActionListener(this); // bindt een actie aan deze button
		button.setMnemonic(key); // verbind de gegeven toets aan deze button, wanneer de toets en alt samen worden ingedrukt wordt de actie erachter getriggerd
		panel.add(button);
	}
	
	public void addLogTextAreaToLogPanel(int x, int y, int width, int length) {
		logTextArea = new JTextArea("");
		logTextArea.setLineWrap(true);
		logTextArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret) logTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		logTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
		JScrollPane scrollPane = new JScrollPane(logTextArea);
		scrollPane.setBounds(x,y,width,length);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.logpanel.add(scrollPane, BorderLayout.CENTER);
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

	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == arrowup)
		{
			JOptionPane.showMessageDialog(null,"Je hebt het bovenste pijltje ingedrukt!","",
					JOptionPane.PLAIN_MESSAGE);
		}
		else if (source == arrowleft)
		{
			JOptionPane.showMessageDialog(null,"Je hebt het linkse pijltje ingedrukt!","",
					JOptionPane.PLAIN_MESSAGE);
		}
		else if (source == arrowright)
		{
			JOptionPane.showMessageDialog(null,"Je hebt het rechtse pijltje ingedrukt!","",
					JOptionPane.PLAIN_MESSAGE);
		}
		else if (source == arrowdown)
		{
			JOptionPane.showMessageDialog(null,"Je hebt het onderste pijltje ingedrukt!","",
					JOptionPane.PLAIN_MESSAGE);
		}
		else if (source == scanQRCode)
		{
			String decoded;
			try {
				decoded = this.guiController.newQRReading();
				if (decoded == null) {
					JOptionPane.showMessageDialog(null, "De zeppelin kon geen QR-code decoderen.");
				}
				try {
					this.showImage();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Fout bij het tonen van de foto, zie standard out");
					e.printStackTrace();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Fout bij de zeppelin een QR-code te laten lezen, zie standard out.");
				e.printStackTrace();
			}
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
			writer.println("Eerste regel van logbestand");
			writer.println("Tweede regel van logbestand");
			writer.println("...");
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


	private void showImage() throws IOException, IllegalStateException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException
	{
		ImageIcon image = new ImageIcon(this.guiController.getLastScannedImage());
		JLabel label = new JLabel("", image, JLabel.CENTER);
		if (this.mostRecentQRCodeLabel != null)
		{
			qrcodepanel.remove(this.mostRecentQRCodeLabel);
			qrcodepanel.revalidate();
			qrcodepanel.repaint();
		}
		this.mostRecentQRCodeLabel = label;
		this.mostRecentQRCodeLabel.setSize(100,100);
		//JOptionPane.showMessageDialog(null, label);
		qrcodepanel.add(label);
	}

	private static void createAndShowGUI() throws RemoteException, NotBoundException {

		JFrame.setDefaultLookAndFeelDecorated(true);
		final JFrame frame = new JFrame("GUI P&O");

		// aanmaken van de guipanel
		final GuiPanel guipanel = new GuiPanel();

		// aanmaken scrollpane
		JPanel gui = guipanel.setGuipanel();  
		JScrollPane scrollpane = new JScrollPane(gui);   
		gui.setPreferredSize(new Dimension(1000, 1000)); // bepaalt de grootte van het scrollgebied, dus tot waar we kunnen scrollen om de gui te zien

		// aanmaken en opzetten van de content pane
		frame.add(scrollpane, BorderLayout.CENTER);
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
		frame.setSize(1000, 1000);
		frame.setVisible(true);
	}

	public static void main(String[] args) throws RemoteException, NotBoundException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGUI();
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
		
		private ArrayList<Boolean> activeMotors;
		boolean leftOn;
		boolean rightOn;
		boolean downwardOn;
		
		public Void doInBackground() throws RemoteException, InterruptedException {
			while (true) {
				leftOn = GuiPanel.this.guiController.leftIsOn();
				rightOn = GuiPanel.this.guiController.rightIsOn();
				downwardOn = GuiPanel.this.guiController.downwardIsOn();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							GuiPanel.this.hoogte.setText("Hoogte : "
									+ GuiPanel.this.guiController.getHeight());
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						decideLightColours();
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
	}
	
	private class logUpdater extends SwingWorker<Void, Void> {
		
		private String logText;
		
		public Void doInBackground() throws InterruptedException {
			while (true) {
				try {
					logText = GuiPanel.this.guiController.readLogFile();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							GuiPanel.this.logTextArea.setText(logText);
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
}
