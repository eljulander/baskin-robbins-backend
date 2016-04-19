package mmap;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;




public class Render {
	
	public static BufferedImage image;
     public static void renderMenu(){
        JFrame frame = new JFrame();
 		frame.setSize(800,600);
// 		frame.setUndecorated(true);
 		
 		
 		frame.setIconImage(image);
 		frame.setTitle("Baskin Robbins");
 		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		frame.setResizable(false);
 		frame.setLocationRelativeTo(null);
 		
 		JPanel p = new JPanel();
 		p.setBounds(0, 100, frame.getWidth(), 50);
// 		p.setBackground(Color.red);
 		String optionz[] = {"Start Scanning", "Stop Scanning", "Force Run"};
 		for(String s: optionz)
 		{
 		Button b = new Button(s);
 		b.setName(s);
 		b.setBounds(0, 0, 50, 50);
 		b.addActionListener(new CA());
 		p.add(b);
 		}
 		frame.add(p);
 		
 		JPanel status = new JPanel();
// 		status.setBackgroundColor(Color.blue);
 		status.setBounds(0,0, 800, 200);
 		
 	    h = new Header("Status: "+MMap.status,Color.BLACK);
 		h.setBounds(status.getBounds());
 		h.setPos(50, 200);
 		
 		log = new Header(System.out.toString(),Color.BLACK);
 		log.setBounds(status.getBounds());
 		log.setPos(50, 300);
 		
 		
 		status.add(h);
 		status.add(log);
 		frame.add(status);
 		
 		System.out.println(status.getBounds());
 		
 	
 		
 	
 		
 		frame.setVisible(true);
 		
 	}
     
     public static Header h;
     public static Header log;
    	 
  
}

class CA implements ActionListener{
    public void actionPerformed(ActionEvent e) {
    	Button b = (Button)e.getSource();
    	switch(b.getName()){
    	case "Start Scanning":
    		MMap.status = "Status: Running...";
    		Render.h.text = MMap.status;
    		Render.h.textColor = Color.green;
    		System.out.println(b.getName());
    		Render.h.repaint();
    		MMap.preform();
    		break;
    	case "Stop Scanning":
    		MMap.status = "Status: Idle...";
    		Render.h.text = MMap.status;
    		Render.h.textColor = Color.RED;
    		System.out.println(b.getName());
    		Render.h.repaint();
    		break;
    	case "Force Run":
    		try {
				MMap.start();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		break;
    	}
    	
//        Main.openDesktop(b.getName());
    }
 }

class Header extends Canvas{
	public String text;
	public Color textColor;
	public int x;
	public int y;
	public Header(String textz, Color colorz){
		text = textz;
		textColor  = colorz;
	}
	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g.setFont(new Font("TimesRoman", Font.BOLD, 24)); 
		g2.setColor(textColor);
		g2.drawString(text, x, y);
	}
	
}
