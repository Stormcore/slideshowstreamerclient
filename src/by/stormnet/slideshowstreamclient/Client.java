package by.stormnet.slideshowstreamclient;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		JFrame frame = new JFrame();
		frame.setBackground(Color.black);
		frame.getContentPane().setBackground(Color.black);
		frame.setSize(960, 540);
		frame.getContentPane().setSize(960, 540);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		Properties prop = new Properties();
		InputStream input = null;
		String host = null;
		
		try {
	 
			input = new FileInputStream("config.properties");
	 
			// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			host = prop.getProperty("host");
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		while (true) {
			Socket s = new Socket(host, 1290);
		    InputStream is = s.getInputStream();
		    
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    byte[] tmp = new byte[4096];
		    int ret = 0;

		    while((ret = is.read(tmp)) > 0)
		    {
		        bos.write(tmp, 0, ret);
		    }
		    
		    byte[] myArray = bos.toByteArray();

	        BufferedImage image = ImageIO.read(new ByteArrayInputStream(myArray));
	        
	        double sourceWidth = image.getWidth();
			System.out.println("Source Width = " + sourceWidth);
			double sourceHeight = image.getHeight();
			System.out.println("Source Height = " + sourceHeight);
			double sourceScale = sourceWidth/sourceHeight;
			
	        double targetWidth = frame.getContentPane().getSize().width;
	        System.out.println("Target Width = " + targetWidth);
	        double targetHeight = frame.getContentPane().getSize().height;
	        System.out.println("Target Height = " + targetHeight);
			
	        double scale = 1;
	        int resultHeight;
	        int resultWidth;
	        
	        if (targetHeight < targetWidth) {
	        	double scalex = (double) targetWidth / sourceWidth;
				System.out.println("Scale x = " + scalex);
				double scaley = (double) targetHeight / sourceHeight;
				System.out.println("Scale y = " + scaley);
				scale = Math.min(scalex, scaley);
				resultHeight = (int) (scale * sourceHeight);
				resultWidth = (int) (scale * sourceWidth);
			}
	        //TODO: Is this else needed?
	        else {
	        	double scalex = (double) targetWidth / sourceWidth;
				System.out.println("Scale x = " + scalex);
				double scaley = (double) targetHeight / sourceHeight;
				System.out.println("Scale y = " + scaley);
				scale = Math.min(scalex, scaley);
				resultHeight = (int) (scale * sourceHeight);
				resultWidth = (int) (sourceScale * sourceHeight);
			}
	        
	        System.out.println("Scale = " + scale);
	        System.out.println("Result Height = " + resultHeight);
	        System.out.println("Result Width = " + resultWidth);
	        
	        Image scaledImage = image.getScaledInstance(resultWidth, resultHeight, Image.SCALE_SMOOTH);
	        
	        frame.getContentPane().removeAll();	
	        JLabel label = new JLabel(new ImageIcon(scaledImage));
	        frame.getContentPane().remove(label);
	        frame.getContentPane().add(label);
	        frame.setVisible(true);
	        
	        s.close();
	        
	        try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
	}

}
