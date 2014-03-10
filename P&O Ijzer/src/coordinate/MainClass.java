package coordinate;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class MainClass extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//van links naar rechts
	public static int windowX =1000 ;
	//van boven naar onder
	public static int windowY =1000;
	
	public MainClass() {

        initUI();
    }
    
    
    /**
     * Dit zorgt voor het GUI-venster
     */
    private void initUI() {
        
    	//zet de titel va het kader
        setTitle("Playfield");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //add(new Field(0, 400, 400));
        add(new Field(50,50,2,2));

        //grootte van het venster in pixels
        setSize(windowX, windowY);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                
                MainClass lines = new MainClass();
                lines.setVisible(true);
            }
        });
    }
}