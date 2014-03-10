package coordinate;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


//TIMER
//import javax.swing.Timer;


public class SwingApp {

	private JFrame frame;
	
	private JButton setTargetHeight ;
	private JTextArea huidigeHoogte, targetHoogte, logTextArea, KpValueHeight, KdValueHeight, KiValueHeight, safetyIntervalValueHeight, KpValueAngle, KdValueAngle, KiValueAngle, safetyIntervalValueAngle;
	private JSlider setKpHeight, setKiHeight, setKdHeight, setSafetyIntervalHeight, setKpAngle, setKiAngle, setKdAngle, setSafetyIntervalAngle;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingApp window = new SwingApp();
					window.frame.setVisible(true);
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
		
		JPanel logpanel = new JPanel();
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
		
		JButton btnLogfiles = new JButton("Vorige logfiles");
		btnLogfiles.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnLogfiles.setBounds(219, 278, 146, 23);
		logpanel.add(btnLogfiles);
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		panel_1.setBounds(373, 0, 548, 414);

		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JPanel fieldpanel = new FieldPanel(85,20,1,1);
		//JPanel panel_6 = new Field(0,0,1,1);
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
				/**
				try {
					this.guiController.setTargetHeight(height);
				} catch (RemoteException e) {
					JOptionPane.showMessageDialog(null, "Fout bij het aanpassen van doelhoogte, zie standard out");
					e.printStackTrace();
				}
				*/
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
		
		JLabel label_1 = new JLabel("Doelhoogte:");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_1.setBounds(7, 97, 87, 19);
		heightpanel.add(label_1);
		
		targetHoogte = new JTextArea();
		targetHoogte.setEditable(false);
		targetHoogte.setBounds(94, 96, 48, 22);
		heightpanel.add(targetHoogte);
		
		JPanel lightpanel = new JPanel();
		lightpanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		lightpanel.setBounds(0, 510, 375, 93);
		frame.getContentPane().add(lightpanel);
		lightpanel.setLayout(null);
		
		JLabel lblLinks = new JLabel("Links");
		lblLinks.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLinks.setHorizontalAlignment(SwingConstants.CENTER);
		lblLinks.setBounds(24, 31, 87, 37);
		lblLinks.setOpaque(true);
		lblLinks.setBackground(Color.red);
		lightpanel.add(lblLinks);
		
		JLabel lblRechts = new JLabel("Rechts");
		lblRechts.setOpaque(true);
		lblRechts.setHorizontalAlignment(SwingConstants.CENTER);
		lblRechts.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblRechts.setBackground(Color.RED);
		lblRechts.setBounds(143, 31, 87, 37);
		lightpanel.add(lblRechts);
		
		JLabel lblMidden = new JLabel("Midden");
		lblMidden.setOpaque(true);
		lblMidden.setHorizontalAlignment(SwingConstants.CENTER);
		lblMidden.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMidden.setBackground(Color.RED);
		lblMidden.setBounds(260, 31, 87, 37);
		lightpanel.add(lblMidden);
		
		JPanel variablepanel = new JPanel();
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
		
		setSafetyIntervalHeight = new JSlider();
		setSafetyIntervalHeight.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				double input = setSafetyIntervalHeight.getValue();
				safetyIntervalValueHeight.setText(Double.toString(input));
			}
		});
		setSafetyIntervalHeight.setBounds(126, 141, 137, 17);
		variablepanel.add(setSafetyIntervalHeight);
		
		JLabel lblSafetya = new JLabel("SafetyA");
		lblSafetya.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSafetya.setBounds(273, 137, 48, 19);
		variablepanel.add(lblSafetya);
		
		safetyIntervalValueAngle = new JTextArea();
		safetyIntervalValueAngle.setEditable(false);
		safetyIntervalValueAngle.setBounds(331, 136, 48, 22);
		variablepanel.add(safetyIntervalValueAngle);
		
		setSafetyIntervalAngle = new JSlider();
		setSafetyIntervalAngle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				double input = setSafetyIntervalAngle.getValue();
				safetyIntervalValueAngle.setText(Double.toString(input));
			}
		});
		setSafetyIntervalAngle.setBounds(389, 139, 137, 17);
		variablepanel.add(setSafetyIntervalAngle);
		
		JPanel arrowpanel = new JPanel();
		arrowpanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		arrowpanel.setBounds(149, 323, 226, 190);
		frame.getContentPane().add(arrowpanel);
		arrowpanel.setLayout(null);
		
		BasicArrowButton arrowup = new BasicArrowButton(SwingConstants.NORTH);
		arrowup.setBackground(Color.CYAN);
		arrowup.setFont(new Font("Tahoma", Font.PLAIN, 14));
		arrowup.setBounds(81, 23, 66, 56);
		arrowpanel.add(arrowup);
		
		BasicArrowButton arrowleft = new BasicArrowButton(SwingConstants.WEST);
		arrowleft.setFont(new Font("Tahoma", Font.PLAIN, 14));
		arrowleft.setBackground(Color.CYAN);
		arrowleft.setBounds(10, 80, 66, 56);
		arrowpanel.add(arrowleft);
		
		BasicArrowButton arrowright = new BasicArrowButton(SwingConstants.EAST);
		arrowright.setFont(new Font("Tahoma", Font.PLAIN, 14));
		arrowright.setBackground(Color.CYAN);
		arrowright.setBounds(150, 80, 66, 56);
		arrowpanel.add(arrowright);
		
		BasicArrowButton arrowdown = new BasicArrowButton(SwingConstants.SOUTH);
		arrowdown.setFont(new Font("Tahoma", Font.PLAIN, 14));
		arrowdown.setBackground(Color.CYAN);
		arrowdown.setBounds(81, 80, 66, 56);
		arrowpanel.add(arrowdown);
	}
}
