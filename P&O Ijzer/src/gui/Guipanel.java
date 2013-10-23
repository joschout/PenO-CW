package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;

public class Guipanel implements ActionListener
{
  // alle panels
  private JPanel guipanel = new JPanel(); 
  private JPanel logpanel = new JPanel();
  private JPanel infopanel = new JPanel();
  private JPanel motorpanel = new JPanel();
  private JPanel qrcodepanel = new JPanel();
  private JPanel arrows = new JPanel();
  
  // alle labels
  private JLabel log;
  private JLabel info;
  private JLabel motor1;
  private JLabel motor2;
  private JLabel motor3;
  private JLabel motor4;
  private JLabel qrcode; 
  private JLabel lamp1;
  private JLabel lamp2;
  private JLabel lamp3;
  private JLabel lamp4;
  
  // alle buttons
  JButton logfiles;
  JButton updateDistance;
  BasicArrowButton arrowup;
  BasicArrowButton arrowleft;
  BasicArrowButton arrowright;
  BasicArrowButton arrowdown;
  
  // lettertype
  Font font = new Font("Calibri", Font.BOLD, 16);
  
  public JPanel setGuipanel() // de frame-constructor methode
  { 
	guipanel.setLayout(null);
	
	// voeg panels toe aan GUI
	logpanel.setLayout(null); // zichtbaar maken
	logpanel.setLocation(0, 0); // cošrdinaat van linkerbovenhoek van dit panel op guipanel
	logpanel.setSize(300,1000); // breedte en lengte van dit panel
	logpanel.setBorder(BorderFactory.createLineBorder(Color.black)); // randen van panel tonen in het zwart
    guipanel.add(logpanel); // panel toevoegen aan meest algemene panel (guipanel) 
    
    infopanel.setLayout(null); 
    infopanel.setLocation(300, 0);
    infopanel.setSize(700, 500);
    infopanel.setBorder(BorderFactory.createLineBorder(Color.black));
    guipanel.add(infopanel);
    
    motorpanel.setLayout(null); 
    motorpanel.setLocation(300, 500);
    motorpanel.setSize(300, 250);
    motorpanel.setBorder(BorderFactory.createLineBorder(Color.black));
    guipanel.add(motorpanel);
    
    qrcodepanel.setLayout(null); 
    qrcodepanel.setLocation(600, 500);
    qrcodepanel.setSize(400, 500);
    qrcodepanel.setBorder(BorderFactory.createLineBorder(Color.black));
    guipanel.add(qrcodepanel);
    
    arrows.setLayout(null); 
    arrows.setLocation(300, 750);
    arrows.setSize(300, 250);
    arrows.setBorder(BorderFactory.createLineBorder(Color.black));
    guipanel.add(arrows);
    
    // Voeg labels toe aan het correcte panel
    log = new JLabel("Log :"); // label aanmaken met naam
    log.setLocation(5, 0); // cošrdinaat van linkerbovenhoek van label op zijn panel
    log.setSize(100,30); // breedte en lengte van dit label
    log.setFont(font); // lettertype wijzigen
    logpanel.add(log); // label toevoegen aan correcte panel
    
    info = new JLabel("Info :");
    info.setLocation(5, 0);
    info.setSize(100,30);
    info.setFont(font);
    infopanel.add(info);
    
    motor1 = new JLabel("Motor1 :");
    motor1.setLocation(5, 25);
    motor1.setSize(100, 30);
    motor1.setFont(font);
    motorpanel.add(motor1);
    
    motor2 = new JLabel("Motor2 :");
    motor2.setLocation(5, 75);
    motor2.setSize(100, 30);
    motor2.setFont(font);
    motorpanel.add(motor2);
    
    motor3 = new JLabel("Motor3 :");
    motor3.setLocation(5, 125);
    motor3.setSize(100, 30);
    motor3.setFont(font);
    motorpanel.add(motor3);
    
    motor4 = new JLabel("Motor4 :");
    motor4.setLocation(5, 175);
    motor4.setSize(100, 30);
    motor4.setFont(font);
    motorpanel.add(motor4);
    
    qrcode = new JLabel("Meest recente QR code :"); 
    qrcode.setLocation(5, 0);
    qrcode.setSize(400, 30);
    qrcode.setFont(font);
    qrcodepanel.add(qrcode);
    
    lamp1 = new JLabel("");
    lamp1.setOpaque(true); // achtergrond transparant maken
    lamp1.setBackground(Color.red); // achtergrondkleur op rood zetten
    lamp1.setLocation(200, 30);
    lamp1.setSize(20,20);
    motorpanel.add(lamp1);    
    
    lamp2 = new JLabel("");
    lamp2.setOpaque(true);
    lamp2.setBackground(Color.red);
    lamp2.setLocation(200, 80);
    lamp2.setSize(20,20);
    motorpanel.add(lamp2);  
    
    lamp3 = new JLabel("");
    lamp3.setOpaque(true);
    lamp3.setBackground(Color.red);
    lamp3.setLocation(200, 130);
    lamp3.setSize(20,20);
    motorpanel.add(lamp3);  
    
    lamp4 = new JLabel("");
    lamp4.setOpaque(true);
    lamp4.setBackground(Color.red);
    lamp4.setLocation(200, 180);
    lamp4.setSize(20,20);
    motorpanel.add(lamp4);  
    
    // voeg buttons toe aan hun panel
    logfiles = new JButton("Vorige logfiles");
    logfiles.setLocation(25, 850);
    logfiles.setSize(250, 30);
    logfiles.setFont(font);
    logfiles.addActionListener(this); // bindt een actie aan deze button
    logfiles.setMnemonic(KeyEvent.VK_L); // verbind de L-toets aan deze button, wanneer L en alt samen worden ingedrukt wordt de actie erachter getriggerd
    logpanel.add(logfiles);
    
    
    arrowup = new BasicArrowButton(SwingConstants.NORTH); // windrichting geeft richting pijl aan
    arrowup.setLocation(115, 55);
    arrowup.setSize(70, 70);
    arrowup.setBorder(BorderFactory.createLineBorder(Color.black));
    arrowup.addActionListener(this);
    arrowup.setMnemonic(KeyEvent.VK_UP); // verbind de bovenste pijltjestoets aan deze button, wanneer het bovenste pijltje en alt samen worden ingedrukt wordt de actie erachter getriggerd
    arrows.add(arrowup);
    
    arrowleft = new BasicArrowButton(SwingConstants.WEST);
    arrowleft.setLocation(45, 125);
    arrowleft.setSize(70, 70);
    arrowleft.setBorder(BorderFactory.createLineBorder(Color.black));
    arrowleft.addActionListener(this);
    arrowleft.setMnemonic(KeyEvent.VK_LEFT); // verbind de linkse pijltjestoets aan deze button, wanneer het linkse pijltje en alt samen worden ingedrukt wordt de actie erachter getriggerd
    arrows.add(arrowleft);
    
    arrowright = new BasicArrowButton(SwingConstants.EAST);
    arrowright.setLocation(185, 125);
    arrowright.setSize(70, 70);
    arrowright.setBorder(BorderFactory.createLineBorder(Color.black));
    arrowright.addActionListener(this);
    arrowright.setMnemonic(KeyEvent.VK_RIGHT); // verbind de rechtse pijltjestoets aan deze button, wanneer het rechtse pijltje en alt samen worden ingedrukt wordt de actie erachter getriggerd
    arrows.add(arrowright);
    
    arrowdown = new BasicArrowButton(SwingConstants.SOUTH);
    arrowdown.setLocation(115, 125);
    arrowdown.setSize(70, 70);
    arrowdown.setBorder(BorderFactory.createLineBorder(Color.black));
    arrowdown.addActionListener(this);
    arrowdown.setMnemonic(KeyEvent.VK_DOWN); // verbind de onderste pijltjestoets aan deze button, wanneer het onderste pijltje en alt samen worden ingedrukt wordt de actie erachter getriggerd
    arrows.add(arrowdown);
    
    updateDistance = new JButton();
    updateDistance.setLocation(100, 100);
    updateDistance.setSize(30, 30);
    updateDistance.setBorder(BorderFactory.createLineBorder(Color.pink));
    updateDistance.addActionListener(this);
    infopanel.add(updateDistance);
    
    return guipanel;
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
    else if (source == updateDistance)
    {
    	JOptionPane.showMessageDialog(null, TestClient.sensorReading());
    }
    else if(source == logfiles)
    {  
  	  try 
  	  {  
  		  //Logfile aangemaakt en opgeslagen.
  		  PrintWriter writer = new PrintWriter("logfile.txt");
  		  
  		  //Tekst schrijven naar de logfile.
  	  	  writer.println("Eerste regel van logbestand");
  	  	  writer.println("Tweede regel van logbestand");
  	  	  writer.println("...");
  	  	  writer.close();
  	  	  
  	  	  //Logfile-dialog
  	  	  JTextArea textLog = new JTextArea(50,60); //JTextArea is een 'multi-line area' waar gewone tekst getoond kan worden. 
  	  	  textLog.read(new FileReader("logfile.txt"), null); //Leest de text uit de logfile en zet het in de JTextArea.
  	  	  textLog.setEditable(false);
  	  	  
  	  	  JOptionPane.showMessageDialog(null, new JScrollPane(textLog),"Logfile", JOptionPane.PLAIN_MESSAGE); //JScrollPane zorgt ervoor dat je kan scrollen in de logfile (horizontaal en verticaal).
  	  }  
  	  catch (Exception e) 
  	  {  
  		  e.printStackTrace();  
  	  } 
    }
  }
  
  private static void createAndShowGUI() {
	  
	   JFrame.setDefaultLookAndFeelDecorated(true);
	   JFrame frame = new JFrame("GUI P&O");

	   // aanmaken van de guipanel en scrollpane
	   Guipanel guipanel = new Guipanel();
	   JPanel gui = guipanel.setGuipanel();  
	   JScrollPane scrollpane = new JScrollPane(gui);   
	   gui.setPreferredSize(new Dimension(1000, 1000)); // bepaalt de grootte van het scrollgebied, dus tot waar we kunnen scrollen om de gui te zien
	      
	   // aanmaken en opzetten van de content pane
	   frame.add(scrollpane, BorderLayout.CENTER);
	   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   frame.setSize(1000, 1000);
	   frame.setVisible(true);
  }

public static void main(String[] args) {
      createAndShowGUI();
  }


}
