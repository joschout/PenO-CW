package coordinate;
import java.awt.EventQueue;

import javax.swing.JFrame;

//import java.awt.GridBagLayout;
import java.awt.SystemColor;

import javax.swing.JPanel;

//import java.awt.GridBagConstraints;
//import java.awt.Insets;

import javax.swing.border.LineBorder;
//import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.Color;

import javax.swing.JButton;
//import javax.swing.BoxLayout;

//import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
//import java.rmi.NotBoundException;
import java.rmi.RemoteException;
//import java.util.List;

//import javax.swing.ButtonModel;
//import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
//import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSlider;
import javax.swing.JLabel;
//import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
//import javax.swing.Timer;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
//import javax.swing.filechooser.FileNameExtensionFilter;

import client.GuiControllerAlternative2;
import RabbitMQ.RabbitMQControllerClient;
import zeppelin.*;
//import coordinate.SwingApp.MotorTimer;

//import javax.swing.AbstractAction;
//import javax.swing.Action;


//TIMER
//import javax.swing.Timer;


public class SwingApp {

	private JFrame frame;
	
	private JButton setTargetHeight, logfiles;
	private JLabel doelhoogteLabel;
	private JPanel logpanel, lightpanel, fieldpanel, variablepanel, arrowpanel;
	private JTextArea huidigeHoogte, targetHoogte, logTextArea, KpValueHeight, KdValueHeight, KiValueHeight, safetyIntervalValueHeight, KpValueAngle, KdValueAngle, KiValueAngle, safetyIntervalValueAngle;
	private JSlider setKpHeight, setKiHeight, setKdHeight,  setKpAngle, setKiAngle, setKdAngle;
//	private JSlider setSafetyIntervalHeight, setSafetyIntervalAngle;
//	private BasicArrowButton arrowup, arrowdown, arrowleft, arrowright;
	public RabbitMQControllerClient rabbitMQControllerClient;
	public GuiControllerAlternative2 guiController;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {				
					SwingApp window = new SwingApp();
					window.frame.setVisible(true);
					window.setVariables();
				
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		});
	}

	

	/**
	 * Create the application.
	 */
	public SwingApp() {

		
	
		guiController = new GuiControllerAlternative2(this, new Zeppelin(new GridPoint(0, 0),100));
	
		/////////////////////////////////////////////////////
		HeightAndMotorWorker motorUpdater = new HeightAndMotorWorker();
		LogUpdater logUpdater = new LogUpdater();
		motorUpdater.execute();
		logUpdater.execute();
		this.rabbitMQControllerClient = new RabbitMQControllerClient(this);
		/////////////////////////////////////////////////////
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(SystemColor.menu);
		frame.setBounds(100, 100, 937, 641);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		logpanel = new JPanel();
		logpanel.setToolTipText("");
		logpanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		logpanel.setBounds(0, 0, 375, 328);
		frame.getContentPane().add(logpanel);
		logpanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setToolTipText("");
		scrollPane.setBounds(10, 11, 355, 245);
		logpanel.add(scrollPane);
		
		logTextArea = new JTextArea();
		logTextArea.setEditable(false);
		scrollPane.setViewportView(logTextArea);
		
		logfiles = new JButton("Vorige logfiles");
		logfiles.setFont(new Font("Tahoma", Font.PLAIN, 14));
		logfiles.setBounds(219, 278, 146, 23);
		logpanel.add(logfiles);
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		panel_1.setBounds(373, 0, 548, 414);

		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		fieldpanel = new FieldPanel(85,20,1,1,this);;
		fieldpanel.setBounds(10, 11, 528, 392);
		panel_1.add(fieldpanel);
		
		JPanel heightpanel = new JPanel();
		heightpanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		heightpanel.setBounds(0, 323, 152, 190);
		frame.getContentPane().add(heightpanel);
		heightpanel.setLayout(null);
		
		setTargetHeight = new JButton("Pas hoogte aan");
		setTargetHeight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog(null, "Voer nieuwe doelhoogte in.");
				double height = Double.parseDouble(input);
				
				
					guiController.setTargetHeight(height);
			
				
				targetHoogte.setText(Double.toString(height));
			}
		});
		setTargetHeight.setFont(new Font("Tahoma", Font.PLAIN, 14));
		setTargetHeight.setBounds(7, 21, 135, 23);
		heightpanel.add(setTargetHeight);
		
		JLabel label = new JLabel("Hoogte:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(7, 55, 69, 19);
		heightpanel.add(label);
		
		huidigeHoogte = new JTextArea();
		huidigeHoogte.setEditable(false);
		huidigeHoogte.setBounds(94, 54, 48, 22);
		heightpanel.add(huidigeHoogte);
		
		doelhoogteLabel = new JLabel("Doelhoogte:");
		doelhoogteLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		doelhoogteLabel.setBounds(7, 97, 87, 19);
		heightpanel.add(doelhoogteLabel);
		
		targetHoogte = new JTextArea();
		targetHoogte.setEditable(false);
		targetHoogte.setBounds(94, 96, 48, 22);
		heightpanel.add(targetHoogte);
		
		lightpanel = new JPanel();
		lightpanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		lightpanel.setBounds(0, 510, 375, 93);
		frame.getContentPane().add(lightpanel);
		lightpanel.setLayout(null);
				
		variablepanel = new JPanel();
		variablepanel.setBounds(373, 406, 548, 197);
		frame.getContentPane().add(variablepanel);
		variablepanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		variablepanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("KpH");
		lblNewLabel.setBounds(10, 36, 25, 14);
		variablepanel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		KpValueHeight = new JTextArea();
		KpValueHeight.setEditable(false);
		KpValueHeight.setBounds(45, 36, 48, 22);
		variablepanel.add(KpValueHeight);
		
		setKpHeight = new JSlider();
		setKpHeight.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				double input = setKpHeight.getValue();
				KpValueHeight.setText(Double.toString(input));
			}
		});
		setKpHeight.setBounds(92, 33, 171, 17);
		variablepanel.add(setKpHeight);
		
		setKdHeight = new JSlider();
		setKdHeight.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				double input = setKdHeight.getValue();
				KdValueHeight.setText(Double.toString(input));
			}
		});
		setKdHeight.setBounds(92, 71, 171, 17);
		variablepanel.add(setKdHeight);
		
		KdValueHeight = new JTextArea();
		KdValueHeight.setEditable(false);
		KdValueHeight.setBounds(45, 69, 48, 22);
		variablepanel.add(KdValueHeight);
		
		JLabel lblKdh = new JLabel("KdH");
		lblKdh.setBounds(10, 74, 25, 14);
		variablepanel.add(lblKdh);
		lblKdh.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblKih = new JLabel("KiH");
		lblKih.setBounds(10, 105, 25, 14);
		variablepanel.add(lblKih);
		lblKih.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		KiValueHeight = new JTextArea();
		KiValueHeight.setEditable(false);
		KiValueHeight.setBounds(45, 102, 48, 22);
		variablepanel.add(KiValueHeight);
		
		setKiHeight = new JSlider();
		setKiHeight.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				double input = setKiHeight.getValue();
				KiValueHeight.setText(Double.toString(input));
			}
		});
		setKiHeight.setBounds(92, 102, 171, 17);
		variablepanel.add(setKiHeight);
		
		JLabel lblKpa = new JLabel("KpA");
		lblKpa.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblKpa.setBounds(273, 36, 25, 14);
		variablepanel.add(lblKpa);
		
		KpValueAngle = new JTextArea();
		KpValueAngle.setEditable(false);
		KpValueAngle.setBounds(308, 36, 48, 22);
		variablepanel.add(KpValueAngle);
		
		setKpAngle = new JSlider();
		setKpAngle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				double input = setKpAngle.getValue();
				KpValueAngle.setText(Double.toString(input));
			}
		});
		setKpAngle.setBounds(355, 33, 171, 17);
		variablepanel.add(setKpAngle);
		
		JLabel lblKih_1 = new JLabel("KdA");
		lblKih_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblKih_1.setBounds(273, 74, 25, 14);
		variablepanel.add(lblKih_1);
		
		KdValueAngle = new JTextArea();
		KdValueAngle.setEditable(false);
		KdValueAngle.setBounds(308, 69, 48, 22);
		variablepanel.add(KdValueAngle);
		
		setKdAngle = new JSlider();
		setKdAngle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				double input = setKdAngle.getValue();
				KdValueAngle.setText(Double.toString(input));
			}
		});
		setKdAngle.setBounds(355, 71, 171, 17);
		variablepanel.add(setKdAngle);
		
		JLabel lblKia = new JLabel("KiA");
		lblKia.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblKia.setBounds(273, 105, 25, 14);
		variablepanel.add(lblKia);
		
		KiValueAngle = new JTextArea();
		KiValueAngle.setEditable(false);
		KiValueAngle.setBounds(308, 102, 48, 22);
		variablepanel.add(KiValueAngle);
		
		setKiAngle = new JSlider();
		setKiAngle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				double input = setKiAngle.getValue();
				KiValueAngle.setText(Double.toString(input));
			}
		});
		setKiAngle.setBounds(355, 102, 171, 17);
		variablepanel.add(setKiAngle);
		
		JLabel lblSafetyh = new JLabel("SafetyH");
		lblSafetyh.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSafetyh.setBounds(10, 139, 48, 19);
		variablepanel.add(lblSafetyh);
		
		safetyIntervalValueHeight = new JTextArea();
		safetyIntervalValueHeight.setEditable(false);
		safetyIntervalValueHeight.setBounds(68, 138, 48, 22);
		variablepanel.add(safetyIntervalValueHeight);
		
//		setSafetyIntervalHeight = new JSlider();
//		setSafetyIntervalHeight.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//				double input = setSafetyIntervalHeight.getValue();
//				safetyIntervalValueHeight.setText(Double.toString(input));
//			}
//		});
//		setSafetyIntervalHeight.setBounds(126, 141, 137, 17);
//		variablepanel.add(setSafetyIntervalHeight);
		
		JLabel lblSafetya = new JLabel("SafetyA");
		lblSafetya.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSafetya.setBounds(273, 137, 48, 19);
		variablepanel.add(lblSafetya);
		
		safetyIntervalValueAngle = new JTextArea();
		safetyIntervalValueAngle.setEditable(false);
		safetyIntervalValueAngle.setBounds(331, 136, 48, 22);
		variablepanel.add(safetyIntervalValueAngle);
		
//		setSafetyIntervalAngle = new JSlider();
//		setSafetyIntervalAngle.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//				double input = setSafetyIntervalAngle.getValue();
//				safetyIntervalValueAngle.setText(Double.toString(input));
//			}
//		});
//		setSafetyIntervalAngle.setBounds(389, 139, 137, 17);
//		variablepanel.add(setSafetyIntervalAngle);
		
		arrowpanel = new JPanel();
		arrowpanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		arrowpanel.setBounds(149, 323, 226, 190);
		frame.getContentPane().add(arrowpanel);
		arrowpanel.setLayout(null);
		
//		arrowup = new BasicArrowButton(SwingConstants.NORTH);
//		arrowup.setBackground(Color.CYAN);
//		arrowup.setFont(new Font("Tahoma", Font.PLAIN, 14));
//		arrowup.setBounds(81, 23, 66, 56);
//		arrowpanel.add(arrowup);
//		addTimerToArrowButton(arrowup);
		
//		arrowleft = new BasicArrowButton(SwingConstants.WEST);
//		arrowleft.setFont(new Font("Tahoma", Font.PLAIN, 14));
//		arrowleft.setBackground(Color.CYAN);
//		arrowleft.setBounds(10, 80, 66, 56);
//		arrowpanel.add(arrowleft);
//		addTimerToArrowButton(arrowleft);
		
//		arrowright = new BasicArrowButton(SwingConstants.EAST);
//		arrowright.setFont(new Font("Tahoma", Font.PLAIN, 14));
//		arrowright.setBackground(Color.CYAN);
//		arrowright.setBounds(150, 80, 66, 56);
//		arrowpanel.add(arrowright);
//		addTimerToArrowButton(arrowright);
		
//		arrowdown = new BasicArrowButton(SwingConstants.SOUTH);
//		arrowdown.setFont(new Font("Tahoma", Font.PLAIN, 14));
//		arrowdown.setBackground(Color.CYAN);
//		arrowdown.setBounds(81, 80, 66, 56);
//		arrowpanel.add(arrowdown);
//		addTimerToArrowButton(arrowdown);
	}
	
	
//	/**
//	 * Voeg timer toe aan gegeven pijlknop. Zo wordt ervoor gezorgd dat de juiste motor
//	 * wordt aangezet als je op de knop drukt en ook wordt afgezet als je de knop loslaat.
//	 * @param arrowButton
//	 * 		  Pijlknop waaraan timer moet worden toegevoegd. Verwacht wordt dat dit
//	 *        arrowup, arrowleft, arrowright of arrowdown is.
//	 */
//	public void addTimerToArrowButton(BasicArrowButton arrowButton) {
//		final ButtonModel bModel = arrowButton.getModel();
//		final MotorTimer timer = new MotorTimer(arrowButton);
//		bModel.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent cEvt) {
//	            if (bModel.isPressed() && !timer.isRunning()) {
//	               timer.start(); // zet de juiste motor aan.
//	            } else if (!bModel.isPressed() && timer.isRunning()) {
//	               timer.stop();
//	               try {
//	            	// de instructie om de motor te stoppen moet hier, omdat
//	            	// de interface voor Timer niet toelaat om het daar te zetten.
//					SwingApp.this.guiController.stopRightAndLeftMotor();
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
//	            }
//	         }
//		});
//	}
	
	
	/**
	 * Update de respectievelijke tekstvelden met de waarden voor de procesconstante,
	 * de derivative constante, integraalconstante en de huidige hoogte van de zeppelin.
	 * @throws RemoteException
	 * 		   Verbinding met de zeppelin is weggevallen.
	 */
	public void setVariables() throws RemoteException {
		this.guiController.askKpHeight();
		this.guiController.askKiHeight();
		this.guiController.askKdHeight();
		this.guiController.askKpAngle();
		this.guiController.askKiAngle();
		this.guiController.askKdAngle();
				
		this.KpValueHeight.setText(Double.toString(guiController.getKpHeight()));
		this.KdValueHeight.setText(Double.toString(guiController.getKdHeight()));
		this.KiValueHeight.setText(Double.toString(guiController.getKiHeight()));
		this.targetHoogte.setText(Double.toString(guiController.getHeight()));
	}
	
	
	
	
	
	
//	public RabbitMQControllerClient getRabbitMQControllerClient(){
//		return rabbitMQControllerClient;
//	}
//	
//	public void setRabbitMQControllerClient(RabbitMQControllerClient rabbitMQControllerClient){
//		this.rabbitMQControllerClient = rabbitMQControllerClient;
//	}
//	
 
	public GuiControllerAlternative2 getGuiController() {
		return guiController;
	}




	public RabbitMQControllerClient getRabbitMQControllerClient() {
		return rabbitMQControllerClient;
	}




	public void setRabbitMQControllerClient(
			RabbitMQControllerClient rabbitMQControllerClient) {
		this.rabbitMQControllerClient = rabbitMQControllerClient;
	}




	public void setGuiController(GuiControllerAlternative2 guiController) {
		this.guiController = guiController;
	}




	/**
	 * Los gekopieerd uit GuiPanel
	 * @param event
	 */
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		
		if (source == setTargetHeight) {
			String input = JOptionPane.showInputDialog(null, "Voer nieuwe doelhoogte in.");
			double height = Double.parseDouble(input);
			
				this.guiController.setTargetHeight(height);
			
				JOptionPane.showMessageDialog(null, "Fout bij het aanpassen van doelhoogte, zie standard out");
				
			
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
//		else if (source == setSafetyIntervalHeight) {
//			String input = JOptionPane.showInputDialog(null, "Voer nieuw safety intervalHeight in.");
//			double safety = Double.parseDouble(input);
//			try {
//				this.guiController.setSafetyIntervalHeight(safety);
//			} catch (RemoteException e) {
//			}
//			this.safetyIntervalValueHeight.setText(Double.toString(safety));
//		}
		
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
//		else if (source == setSafetyIntervalAngle) {
//			String input = JOptionPane.showInputDialog(null, "Voer nieuw safety intervalAngle in.");
//			double safety = Double.parseDouble(input);
//			try {
//				this.guiController.setSafetyIntervalAngle(safety);
//			} catch (RemoteException e) {
//			}
//			this.safetyIntervalValueAngle.setText(Double.toString(safety));
//		}
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
	
	////////////////////
	/**
	 * Verzorgt het constant updaten van het hoogteveld, het groen en roodmaken van
	 * de motorlabels en het tonen van een QR-code mocht er één beschikbaar zijn gemaakt
	 * door de zeppelin.
	 * 
	 *
	 */
	private class HeightAndMotorWorker extends SwingWorker<Void, Void> {
		
//		boolean leftOn;
//		boolean rightOn;
//		boolean downwardOn;
		
		public Void doInBackground() throws  InterruptedException {
			while (true) {
//				leftOn = SwingApp.this.guiController.leftIsOn();
//				rightOn = SwingApp.this.guiController.rightIsOn();
//				downwardOn = SwingApp.this.guiController.downwardIsOn();
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
//						try {
							SwingApp.this.huidigeHoogte.setText(Double.toString(SwingApp.this.guiController.getHeight()));
							SwingApp.this.targetHoogte.setText(Double.toString(SwingApp.this.guiController.getTargetHeight()));
//						} catch (RemoteException e) {
//							e.printStackTrace();
//						}
					}
				});
				// Waarschijnlijk de bron van de imprecisie van de motorlabels.
				// Er kan overwogen worden om dit te verlagen.
				Thread.sleep(1000);
			}
		}
		

	}
	
	/**
	 * Verzorgt het constant updaten van de log.
	 *
	 */
	private class LogUpdater extends SwingWorker<Void, Void> {
		
		// moet een klassevariabele zijn omdat het enige alternatief is dat het
		// een final lokale variabele is in doInBackground(), wat te streng is
		// voor onze doeleinden.
		private String logText;
		
		public Void doInBackground() throws InterruptedException {
			while (true) {
				try {
					logText = SwingApp.this.guiController.readLog();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							SwingApp.this.logTextArea.append(logText);
						}
					});
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Thread.sleep(1000);
			}
		}
	}
	
//	/**
//	 * Zorgt ervoor dat, als je op een pijlknop drukt, er een timer gaat die
//	 * een event vuurt. Op basis van die event kan je extern checken of de knop nog wordt
//	 * ingedrukt. Zorgt er ook voor dat de juiste motor wordt aangesproken voor de 
//	 * juiste pijlknop.
//	 *
//	 */
//	public class MotorTimer extends Timer {
//
//		BasicArrowButton arrowButton;
//		
//		public MotorTimer(BasicArrowButton arrowButton) throws IllegalArgumentException {
//			super(100, null);
//			if (! (arrowButton == SwingApp.this.arrowup || arrowButton == SwingApp.this.arrowleft ||
//					arrowButton == SwingApp.this.arrowright || arrowButton == SwingApp.this.arrowdown)) {
//				throw new IllegalArgumentException("Timerconstructor moet bestaande pijltoets hebben als argument");
//			}
//			this.arrowButton = arrowButton;
//			ActionListener listener = new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					if (MotorTimer.this.arrowButton == SwingApp.this.arrowup) {
//						try {
//							SwingApp.this.guiController.goForward();
//						} catch (RemoteException e1) {
//							e1.printStackTrace();
//						}
//					}
//					else if (MotorTimer.this.arrowButton == SwingApp.this.arrowleft) {
//						try {
//							SwingApp.this.guiController.goLeft();
//						} catch (RemoteException e1) {
//							e1.printStackTrace();
//						}
//					}
//					else if (MotorTimer.this.arrowButton == SwingApp.this.arrowright) {
//						try {
//							SwingApp.this.guiController.goRight();
//						} catch (RemoteException e1) {
//							e1.printStackTrace();
//						}
//					}
//					else if (MotorTimer.this.arrowButton == SwingApp.this.arrowdown) {
//						try {
//							SwingApp.this.guiController.goBackward();
//						} catch (RemoteException e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//				
//			};
//			super.removeActionListener(null);
//			super.addActionListener(listener);
//		}
//	}
}


