/**
 * Communicatielaag tussen de client en de zeppelin. Status opvragen van de zeppelin en relevante
 * files downloaden van de zeppelin gebeurt via deze klasse.
 */

package client;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


import server.ZeppelinServer;
import zeppelin.MainProgramInterface;


public class GuiControllerAlternative {
	/**
	 * Zeppelin-object gehaald van de server
	 */
	private MainProgramInterface zeppelin;
	
	public GuiControllerAlternative() throws NotBoundException, IllegalStateException, IOException {
		setZeppelin();
	}

	/**
	 * Associeer een zeppelin-object met deze GUI.
	 * @param zeppelin
	 * 		Een zeppelin-object geïmporteerd vanop de Pi.
	 */
	public void setZeppelin(MainProgramInterface zeppelin)
	{
		this.zeppelin = zeppelin;
	}

	/**
	 * Vraagt de hoogte van de zeppelin op.
	 * @return
	 * @throws RemoteException
	 */
	public double getHeight() throws RemoteException {
		return this.zeppelin.getHeight();
	}

	
	public boolean checkRegistryFound(){
		return this.registryFound;
	}
	
	public boolean checkZeppelinFound(){
		return this.zeppelinFound;
	}
	
	private boolean registryFound = false;
	private boolean zeppelinFound = false;
	/**
	 * Zoekt een zeppelin-object op het IP-adres gereserveerd voor de zeppelin.
	 * Communicatie gebeurt daarna tussen de client en het gevonden zeppelin-object.n
	 * 		   Er bestaat geen zeppelin-object.
	 */
	public void setZeppelin()  throws RemoteException, NotBoundException{
		Registry registry;
		MainProgramInterface zeppelin;
		try {
			registry = LocateRegistry.getRegistry(ZeppelinServer.PI_HOSTNAME,1099);
			registryFound = true;
			
			try {
				zeppelin = (MainProgramInterface) registry.lookup("Zeppelin");
				this.zeppelin = zeppelin;
				zeppelinFound = true;
			} catch (AccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (RemoteException e) {
			registryFound = false;
			e.printStackTrace();
		}
		
		
		
		
	}
	
	/**
	 * Geeft aan of de linkermotor aanstaat.
	 * @throws RemoteException
	 */
	public boolean leftIsOn() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
			return this.zeppelin.leftIsOn();
		}
		else return false;
	}
	
	/**
	 * Geeft aan of de rechtermotor aanstaat.
	 * @throws RemoteException
	 */
	public boolean rightIsOn() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return this.zeppelin.rightIsOn();
		}
		else return false;
	}
	
	/**
	 * Geeft aan of de naar-beneden-gerichte motor aanstaat.
	 * @return
	 * @throws RemoteException
	 */
	public boolean downwardIsOn() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return this.zeppelin.downwardIsOn();
		}
		else return false;
	}
	
	/**
	 * Laat de zeppelin vooruit gaan.
	 * @throws RemoteException
	 */
	public void goForward() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		this.zeppelin.goForward();
		}
	}
	
	/**
	 * Laat de zeppelin achteruit gaan.
	 * @throws RemoteException
	 */
	public void goBackward() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		this.zeppelin.goBackward();
		}
	}
	
	/**
	 * Laat de zeppelin naar links draaien.
	 * @throws RemoteException
	 */
	public void goLeft() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		this.zeppelin.turnLeft();
		}
	}
	
	/**
	 * Laat de zeppelin naar rechts draaien.
	 * @throws RemoteException
	 */
	public void goRight() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		this.zeppelin.turnRight();
		}
	}
	
	/**
	 * Laat de zeppelin de horizontaal gerichte motoren stoppen.
	 * @throws RemoteException
	 */
	public void stopRightAndLeftMotor() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		this.zeppelin.stopRightAndLeft();
		}
	}
	
	/**
	 * Haal de doelhoogte van de zeppelin.
	 * @throws RemoteException
	 */
	public double getTargetHeight() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return this.zeppelin.getTargetHeight();
		}else return 0;
	}
	
	/**
	 * Laat de zeppelin streven naar een nieuwe hoogte.
	 * @param height
	 *        Nieuwe hoogte.
	 * @throws RemoteException
	 */
	public void setTargetHeight(double height) throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		this.zeppelin.setTargetHeight(height);
		}
	}
	
	/**
	 * Leest de log op de zeppelin.
	 * @throws IllegalStateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String readLog() throws IllegalStateException, FileNotFoundException, IOException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return this.zeppelin.readLog();
		} else return "Geen zeppelin geinitialiseerd, dus geen log";
	}

	/**
	 * Laat de zeppelin zijn activiteiten stoppen; sluit achteraf het programma af.
	 */
	public void exit() {
		try {
			this.zeppelin.exit();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	/**
	 * Update de procesconstante voor de hoogte.
	 * @param kp
	 * @throws RemoteException 
	 */
	public void setKpHeight(double kp) throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		zeppelin.getHeightController().getpController().setKp(kp);
		}
	}
	
	/**
	 * Update de integraalconstante voor de hoogte.
	 * @param ki
	 * @throws RemoteException 
	 */
	public void setKiHeight(double ki) throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		zeppelin.getHeightController().getpController().setKi(ki);
		}
	}
	
	/**
	 * Update de derivative constante voor de hoogte.
	 * @param kd
	 * @throws RemoteException 
	 */
	public void setKdHeight(double kd) throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		zeppelin.getHeightController().getpController().setKd(kd);
		}
	}
	
	/**
	 * Haalt de procesconstante voor de hoogte.
	 * @throws RemoteException
	 */
	public double getKpHeight() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return zeppelin.getHeightController().getpController().getKp();
		}else return 0;
	}
	
	/**
	 * Haalt de derivate constante voor de hoogte.
	 * @throws RemoteException
	 */
	public double getKdHeight() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return zeppelin.getHeightController().getpController().getKd();
		} else return 0;
	}
	
	/**
	 * Haalt de integraalconstante voor de hoogte.
	 * @throws RemoteException
	 */
	public double getKiHeight() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return zeppelin.getHeightController().getpController().getKi();
		} else return 0;
	}
	
	// wordt deze methode zelfs gebruikt?
	/**
	 * Zet het interval rond de doelhoogte waar de verticale motor alle activiteit
	 * moet stoppen.
	 * @param safetyInterval
	 *        Waarde die een interval maakt volgens [getTargetHeight - safetyInterval, getTargetHeight + safetyInterval]
	 * @throws RemoteException
	 */
	public void setSafetyIntervalHeight(double safetyInterval) throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		zeppelin.getHeightController().setSafetyIntervalHeight(safetyInterval);
		}
	}
	
	/**
	 * Haalt het veiligheidsinterval voor de hoogte.
	 * @throws RemoteException
	 */
	public double getSafetyIntervalHeight() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return zeppelin.getHeightController().getSafetyIntervalHeight();
		} else return 0;
	}
	
	/**
	 * Update de procesconstante voor de hoek.
	 * @param kp
	 * @throws RemoteException 
	 */
	public void setKpAngle(double kp) throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		zeppelin.getRotationController().getpController().setKp(kp);
		}
	}
	
	/**
	 * Update de integraalconstante voor de hoek.
	 * @param ki
	 * @throws RemoteException 
	 */
	public void setKiAngle(double ki) throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		zeppelin.getRotationController().getpController().setKi(ki);
		}
	}
	
	/**
	 * Update de derivative constante voor de hoek.
	 * @param kd
	 * @throws RemoteException 
	 */
	public void setKdAngle(double kd) throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		zeppelin.getRotationController().getpController().setKd(kd);
		}
	}
	
	/**
	 * Haal de procesconstante voor de hoek.
	 * @throws RemoteException
	 */
	public double getKpAngle() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return zeppelin.getRotationController().getpController().getKp();
		} else return 0;
	}
	
	/**
	 * Haal de derivative constante voor de hoek.
	 * @throws RemoteException
	 */
	public double getKdAngle() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return zeppelin.getRotationController().getpController().getKd();
		} else return 0;
	}
	
	/**
	 * Haal de integraalconstante voor de hoek.
	 * @throws RemoteException
	 */
	public double getKiAngle() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return zeppelin.getRotationController().getpController().getKi();
		}else return 0;
	}
	
	/**
	 * Zet het interval rond de doelhoek waar de zeppelin de horizontale motoren alle
	 * activiteit moet laten stoppen.
	 * @param safetyInterval
	 * 		  Maakt een interval rond de doelhoek volgens [getTargetAngle - safetyInterval, getTargetAngle + safetyInterval].
	 * 		  Natuurlijk gerekend modulo 360.
	 * @throws RemoteException
	 */
	public void setSafetyIntervalAngle(double safetyInterval) throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		zeppelin.getRotationController().setSafetyIntervalAngle(safetyInterval);
		}
	}
	
	/**
	 * Haal het interval waar de zeppelin de horizontale motoren niet meer mag laten werken.
	 * @throws RemoteException
	 */
	public double getSafetyIntervalAngle() throws RemoteException {
		if (checkRegistryFound()&&checkZeppelinFound()) {
		return zeppelin.getRotationController().getSafetyIntervalAngle();
		}else return 0;
	}


}
