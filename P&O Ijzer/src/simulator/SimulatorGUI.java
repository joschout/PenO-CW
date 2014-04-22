package simulator;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import coordinate.GridPoint;
import javax.swing.SwingConstants;

public class SimulatorGUI extends JFrame implements ActionListener, SimulatorObserver {

	// Components
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCurrPos;
	private JLabel lblWelkom;
	private JLabel lblHuidigePositie;
	private JLabel lblDoelpositie;
	private JTextField txtTargetPos;
	private JTextField txtCurrHeight;
	private JTextField txtTargetHeight;
	private JTextField txtUpdateInterval;
	private JTextField txtPosStep;
	private JTextField txtHeightStep;
	private JButton btnStartSimulator;
	private JButton btnAndereDoelpositie;
	private JButton btnAndereDoelhoogte;
	private JButton btnAndereUpdateInterval;
	private JButton btnAnderePositiestap;
	private JButton btnAndereHoogtestap;
	
	// Simulator
	
	private Simulator simulator;
	
	private Simulator getSimulator() {
		return this.simulator;
	}
	
	public void update(Simulator simulator) {
		final Simulator refCopy = simulator;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				SimulatorGUI.this.txtTargetPos.setText(refCopy.getTargetPosition().toString());
				SimulatorGUI.this.txtCurrPos.setText(refCopy.getPosition().toString());
				SimulatorGUI.this.txtCurrHeight.setText(Double.toString(refCopy.getHeight()));
				SimulatorGUI.this.txtTargetHeight.setText(Double.toString(refCopy.getTargetHeight()));
				SimulatorGUI.this.txtUpdateInterval.setText(Integer.toString(refCopy.getUpdateInterval()));
				SimulatorGUI.this.txtPosStep.setText(Double.toString(refCopy.getPositionStep()));
				SimulatorGUI.this.txtHeightStep.setText(Double.toString(refCopy.getHeightStep()));
				
			}
		});
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == btnStartSimulator) {
			this.makeSimulator();
		}
		else if (source == btnAndereDoelpositie) {
			this.inputTargetPosition();
		}
		else if (source == btnAndereDoelhoogte) {
			this.inputTargetHeight();
		}
		else if (source == btnAndereUpdateInterval) {
			this.inputUpdateInterval();
		}
		else if (source == btnAnderePositiestap) {
			this.inputPositionStep();
		}
		else if (source == btnAndereHoogtestap) {
			this.inputHeightStep();
		}
	}
	
	private void makeSimulator() {
		// Snel simulator maken of complex
		if (this.getSimulator() != null) {
			return;
		}
		Object[] choices = { "Default", "Zelf" };
		int choice = JOptionPane.showOptionDialog(this, "Default simulator of zelf instellen?", "Simulator starten", JOptionPane.YES_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
		if (choice == 0) {
			this.simulator = new Simulator();
			simulator.register(this);
			this.update(simulator);
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					SimulatorGUI.this.lblWelkom.setText(simulator.getName());
				}
			});
			simulator.run();
		}
		else if (choice == 1) {
			boolean validInput = false;
			String name = null;
			GridPoint startPosition = null;
			GridPoint targetPosition = null;
			double startHeight = -1;
			double targetHeight = -1;
			double posStep = -1;
			double heightStep = -1;
			int updateInterval = -1;
			// Startpositie
			while (! validInput) {
				name = JOptionPane.showInputDialog(this, "Geef de naam op");
				if (name == null || name.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Minstens één karakter verwacht");
					continue;
				}
				validInput = true;
			}
			validInput = false;
			while (! validInput) {
				try {
					String position = JOptionPane.showInputDialog(this, "Geef startpositie (in de vorm \"<x>,<y>\")");
					String[] parts = position.split(",");
					if (parts.length != 2) {
						JOptionPane.showMessageDialog(this, "Verwacht: <x>,<y>");
						continue;
					}
					double x = Double.parseDouble(parts[0]);
					double y = Double.parseDouble(parts[1]);
					startPosition = new GridPoint(x, y);
					validInput = true;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Getallen verwacht");
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(this, "Getallen verwacht");
				}
			}
			validInput = false;
			while (! validInput) {
				try {
					String position = JOptionPane.showInputDialog(this, "Geef doelpositie (in de vorm \"<x>,<y>\")");
					String[] parts = position.split(",");
					if (parts.length != 2) {
						JOptionPane.showMessageDialog(this, "Verwacht: <x>,<y>");
						continue;
					}
					double x = Double.parseDouble(parts[0]);
					double y = Double.parseDouble(parts[1]);
					targetPosition = new GridPoint(x, y);
					validInput = true;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Getallen verwacht");
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(this, "Getallen verwacht");
				}
			}
			validInput = false;
			while (! validInput) {
				String heightString = JOptionPane.showInputDialog(this, "Geef beginhoogte");
				try {
					startHeight = Double.parseDouble(heightString);
					validInput = true;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Getal verwacht");
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(this, "Getal verwacht");
				}
			}
			validInput = false;
			while (! validInput) {
				String heightString = JOptionPane.showInputDialog(this, "Geef doelhoogte");
				try {
					targetHeight = Double.parseDouble(heightString);
					validInput = true;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Getal verwacht");
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(this, "Getal verwacht");
				}
			}
			validInput = false;
			while (! validInput) {
				String heightString = JOptionPane.showInputDialog(this, "Geef stap in cm voor positie per iteratie");
				try {
					posStep = Double.parseDouble(heightString);
					validInput = true;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Getal verwacht");
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(this, "Getal verwacht");
				}
			}
			validInput = false;
			while (! validInput) {
				String heightString = JOptionPane.showInputDialog(this, "Geef stap in cm voor hoogte per iteratie");
				try {
					heightStep = Double.parseDouble(heightString);
					validInput = true;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Getal verwacht");
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(this, "Getal verwacht");
				}
			}
			validInput = false;
			while (! validInput) {
				String heightString = JOptionPane.showInputDialog(this, "Geef aantal milliseconden tussen updates");
				try {
					updateInterval = Integer.parseInt(heightString);
					validInput = true;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Getal verwacht");
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(this, "Getal verwacht");
				}
			}
			this.simulator = new Simulator(name, startPosition, targetPosition, startHeight, targetHeight, updateInterval, posStep, heightStep);
			simulator.register(this);
			this.update(simulator);
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					SimulatorGUI.this.lblWelkom.setText(simulator.getName());
				}
			});
			simulator.run();
		}
	}
	
	private void inputTargetPosition() {
		boolean validInput = false;
		while (! validInput) {
			String position = JOptionPane.showInputDialog(this, "Geef doelpositie (in de vorm \"<x>,<y>\")");
			try {
				String[] parts = position.split(",");
				if (parts.length != 2) {
					JOptionPane.showMessageDialog(this, "Verwacht: <x>,<y>");
					continue;
				}
				double x = Double.parseDouble(parts[0]);
				double y = Double.parseDouble(parts[1]);
				GridPoint targetPosition = new GridPoint(x, y);
				this.simulator.setTargetPosition(targetPosition);
				validInput = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Getallen verwacht");
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(this, "Getallen verwacht");
			}
		}
	}
	
	private void inputTargetHeight() {
		boolean validInput = false;
		while (! validInput) {
			String heightString = JOptionPane.showInputDialog(this, "Geef doelhoogte");
			try {
				double targetHeight = Double.parseDouble(heightString);
				simulator.setTargetHeight(targetHeight);
				validInput = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Getal verwacht");
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(this, "Getal verwacht");
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		}
	}
	
	private void inputPositionStep() {
		boolean validInput = false;
		while (! validInput) {
			String stepString = JOptionPane.showInputDialog(this, "Geef stap in cm voor positie per iteratie");
			try {
				double positionStep = Double.parseDouble(stepString);
				simulator.setPositionStep(positionStep);
				validInput = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Getal verwacht");
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(this, "Getal verwacht");
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		}
	}
	
	private void inputHeightStep() {
		boolean validInput = false;
		while (! validInput) {
			String stepString = JOptionPane.showInputDialog(this, "Geef stap in cm voor hoogte per iteratie");
			try {
				double positionStep = Double.parseDouble(stepString);
				simulator.setHeightStep(positionStep);
				validInput = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Getal verwacht");
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(this, "Getal verwacht");
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		}
	}
	
	private void inputUpdateInterval() {
		boolean validInput = false;
		while (! validInput) {
			String updateIntervalString = JOptionPane.showInputDialog(this, "Geef aantal milliseconden tussen updates");
			try {
				int updateInterval = Integer.parseInt(updateIntervalString);
				simulator.setUpdateInterval(updateInterval);
				validInput = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Getal verwacht");
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(this, "Getal verwacht");
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimulatorGUI frame = new SimulatorGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

	/**
	 * Create the frame.
	 */
	public SimulatorGUI() {
		setForeground(Color.DARK_GRAY);
		setBackground(Color.GRAY);
		setTitle("Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 606, 405);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtCurrPos = new JTextField();
		txtCurrPos.setBounds(10, 76, 212, 25);
		txtCurrPos.setBackground(Color.WHITE);
		txtCurrPos.setEditable(false);
		contentPane.add(txtCurrPos);
		txtCurrPos.setColumns(10);
		
		lblWelkom = new JLabel("Welkom");
		lblWelkom.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelkom.setBounds(10, 11, 570, 25);
		contentPane.add(lblWelkom);
		
		btnStartSimulator = new JButton("Start simulator");
		btnStartSimulator.setBounds(234, 47, 145, 23);
		contentPane.add(btnStartSimulator);
		btnStartSimulator.addActionListener(this);
		
		lblHuidigePositie = new JLabel("Huidige positie (cm)");
		lblHuidigePositie.setBounds(10, 51, 132, 14);
		contentPane.add(lblHuidigePositie);
		
		lblDoelpositie = new JLabel("Doelpositie (cm)");
		lblDoelpositie.setBounds(10, 126, 107, 14);
		contentPane.add(lblDoelpositie);
		
		txtTargetPos = new JTextField();
		txtTargetPos.setBackground(Color.WHITE);
		txtTargetPos.setEditable(false);
		txtTargetPos.setBounds(10, 151, 212, 25);
		contentPane.add(txtTargetPos);
		txtTargetPos.setColumns(10);
		
		JLabel lblHuidigeHoogte = new JLabel("Huidige hoogte (cm)");
		lblHuidigeHoogte.setBounds(418, 51, 139, 14);
		contentPane.add(lblHuidigeHoogte);
		
		txtCurrHeight = new JTextField();
		txtCurrHeight.setBackground(Color.WHITE);
		txtCurrHeight.setEditable(false);
		txtCurrHeight.setBounds(384, 76, 173, 25);
		contentPane.add(txtCurrHeight);
		txtCurrHeight.setColumns(10);
		
		JLabel lblDoelhoogte = new JLabel("Doelhoogte (cm)");
		lblDoelhoogte.setBounds(418, 126, 101, 14);
		contentPane.add(lblDoelhoogte);
		
		txtTargetHeight = new JTextField();
		txtTargetHeight.setBackground(Color.WHITE);
		txtTargetHeight.setEditable(false);
		txtTargetHeight.setBounds(384, 151, 173, 25);
		contentPane.add(txtTargetHeight);
		txtTargetHeight.setColumns(10);
		
		btnAndereDoelpositie = new JButton("Andere doelpositie");
		btnAndereDoelpositie.setBounds(10, 187, 156, 23);
		contentPane.add(btnAndereDoelpositie);
		btnAndereDoelpositie.addActionListener(this);
		
		btnAndereDoelhoogte = new JButton("Andere doelhoogte");
		btnAndereDoelhoogte.setBounds(400, 187, 180, 23);
		contentPane.add(btnAndereDoelhoogte);
		btnAndereDoelhoogte.addActionListener(this);
		
		txtUpdateInterval = new JTextField();
		txtUpdateInterval.setBackground(Color.WHITE);
		txtUpdateInterval.setEditable(false);
		txtUpdateInterval.setBounds(234, 290, 145, 25);
		contentPane.add(txtUpdateInterval);
		txtUpdateInterval.setColumns(10);
		
		JLabel lblUpdateInterval = new JLabel("Update interval");
		lblUpdateInterval.setBounds(244, 265, 86, 14);
		contentPane.add(lblUpdateInterval);
		
		JLabel lblPositiestapcm = new JLabel("Positiestap (cm)");
		lblPositiestapcm.setBounds(10, 235, 107, 14);
		contentPane.add(lblPositiestapcm);
		
		txtPosStep = new JTextField();
		txtPosStep.setBackground(Color.WHITE);
		txtPosStep.setEditable(false);
		txtPosStep.setBounds(10, 263, 156, 25);
		contentPane.add(txtPosStep);
		txtPosStep.setColumns(10);
		
		JLabel lblHoogtestap = new JLabel("Hoogtestap (cm)");
		lblHoogtestap.setBounds(418, 235, 139, 14);
		contentPane.add(lblHoogtestap);
		
		txtHeightStep = new JTextField();
		txtHeightStep.setBackground(Color.WHITE);
		txtHeightStep.setEditable(false);
		txtHeightStep.setBounds(418, 265, 139, 25);
		contentPane.add(txtHeightStep);
		txtHeightStep.setColumns(10);
		
		btnAnderePositiestap = new JButton("Andere positiestap");
		btnAnderePositiestap.setBounds(10, 299, 173, 23);
		contentPane.add(btnAnderePositiestap);
		btnAnderePositiestap.addActionListener(this);
		
		btnAndereHoogtestap = new JButton("Andere hoogtestap");
		btnAndereHoogtestap.setBounds(418, 301, 162, 23);
		contentPane.add(btnAndereHoogtestap);
		btnAndereHoogtestap.addActionListener(this);
		
		btnAndereUpdateInterval = new JButton("Andere update interval");
		btnAndereUpdateInterval.setBounds(217, 326, 190, 23);
		contentPane.add(btnAndereUpdateInterval);
		btnAndereUpdateInterval.addActionListener(this);
	}
}
