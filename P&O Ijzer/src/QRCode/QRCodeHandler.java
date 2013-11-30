package QRCode;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import zeppelin.MainProgramImpl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import ftp.FTPFileInfo;

/**
 * http://www.vineetmanohar.com/2010/09/java-barcode-api/
 * http://stackoverflow.com/questions/11626307/how-to-save-java-swing-imageicon-image-to-file
 * @author Jonas
 *
 */
public class QRCodeHandler {

	private QRCodeReader reader;
	
	public QRCodeHandler() {
		reader = new QRCodeReader();
	}
	
	/**
	 * obsolete
	 * @param icon
	 */
	// save ImageIcon to file
	public void save(ImageIcon icon){
		// methode gevonden op http://stackoverflow.com/questions/11626307/how-to-save-java-swing-imageicon-image-to-file
		Image img = icon.getImage();

		BufferedImage bi = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(img, 0, 0, null);
		g2.dispose();
		try {
			ImageIO.write(bi, "png", new File("/home/r0294084/Downloads/img.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Probeert een QR-code te lezen uit de opgegeven foto.
	 * @param	filename
	 * 			Naam van de foto.
	 * @return	De string die gedecodeerd werd uit de foto. Null als er geen QR-code werd gevonden
	 */
	public String scanQrCode(String filename) {
		try {
			InputStream barCodeInputStream = new FileInputStream(filename);
			BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);  
  
			LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);  
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Result result = reader.decode(bitmap);
			return result.getText();
		} catch (NotFoundException e) {
			return null;
		} catch ( Exception e) {
				 //TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  
	}
	
	public String tryReadQrCode(double zeppelinHeight) throws InterruptedException, IOException {
		String filename = Long.toString(System.currentTimeMillis());
		if (zeppelinHeight <= 160)
			MainProgramImpl.CAMERA_CONTROLLER.takePicture(filename, 800, 600);
		else
			MainProgramImpl.CAMERA_CONTROLLER.takePicture(filename, 1400, 800);
		String decoded = this.scanQrCode(FTPFileInfo.PATH_TO_FTP_FILES + filename + ".jpg");
		if (decoded != null) {
			BufferedWriter output = new BufferedWriter(new FileWriter(
					FTPFileInfo.PATH_TO_FTP_FILES + FTPFileInfo.TIMESTAMPLIST_HOSTFILENAME, true));
			output.append("\n");
			output.append(filename + "," + decoded);
			output.close();
		}
		return decoded;
	}
	
	/**
	 * obsolete
	 * @param input
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ImageIcon encode(String input) {
		try {
			QRCodeWriter writer = new QRCodeWriter();
			BitMatrix bitMatrix = null;
			try {
				Hashtable hintMap = new Hashtable();
				hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
				hintMap.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
				bitMatrix = writer.encode(input, BarcodeFormat.QR_CODE, 300, 300, hintMap);
				BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
				return new ImageIcon(image);
			} catch (WriterException e){
				e.printStackTrace();
				return null;
			}
		} catch(Exception e)  {
			e.printStackTrace();
			return null;
		}
	}
}
