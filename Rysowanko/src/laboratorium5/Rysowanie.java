package laboratorium5;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Rysowanie extends JFrame 
{
	private static final long serialVersionUID = 1L;

	Rysowanie()
	{
		this.setSize(800,800);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		JPanel radioPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		JPanel emptyPanel = new JPanel();
		buttonsPanel.add(colorButton);
		buttonsPanel.add(clearButton);
		radioButtons.add(pencilButton);
		radioButtons.add(rubberButton);
		controlsPanel.setBackground(Color.WHITE);
		radioPanel.add(pencilButton);
		radioPanel.add(rubberButton);
		radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.PAGE_AXIS));
		JPanel panel1 = new JPanel();
		panel1.add(radioPanel, BorderLayout.PAGE_END);

		controlsPanel.add(emptyPanel);
		controlsPanel.add(panel1);
		controlsPanel.add(thicknessSlider);
		controlsPanel.add(buttonsPanel);

		controlsPanel.setLayout(new GridLayout(4,1));
		drawPanel.setBackground(Color.WHITE);

		this.add(drawPanel);
		this.add(controlsPanel, BorderLayout.LINE_END);
		
		this.setJMenuBar(menuBar);
		menuBar.add(menu);
		menu.add(menuItem1);
		menu.add(menuItem2);
		
		pencilButton.setActionCommand("1");
		rubberButton.setActionCommand("2");
		pencilButton.addActionListener(buttonListener);
		rubberButton.addActionListener(buttonListener);
		colorButton.addActionListener(colorListener);
		clearButton.addActionListener(clearListener);
		thicknessSlider.setValue(5);
		thicknessSlider.addChangeListener(thicknessListener);
		menuItem1.addActionListener(saveListener);
		menuItem2.addActionListener(loadListener);
		
			
	}
	
	public class paintPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public paintPanel()
		{
			this.addMouseListener(mouse);
			this.addMouseMotionListener(mouse);
		}
		
		MouseAdapter mouse = new MouseAdapter() 
		{
			public void mouseDragged(MouseEvent e) 
			{
					xPos.add(e.getX());
					yPos.add(e.getY());
					repaint();
			}
			
			public void mouseReleased(MouseEvent e) 
			{
				BufferedImage img = new BufferedImage(drawPanel.getWidth(), drawPanel.getHeight(),BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = img.createGraphics();
				drawPanel.paintAll(g2d);
				drawPanel.image = img;
				repaint();
				drawPanel.xPos.clear(); //zeby sie nie rysowalo kiedy nie jest zaznaczony przycisk 
				drawPanel.yPos.clear();
			}
		};		
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(image,0,0, this);
			
			 Graphics2D g2 = (Graphics2D) g;
			 if(buttonOption == "1")
			 {
				 for(int i=0; i<xPos.size() - 1; i++)
				 {
					 g2.setStroke(new BasicStroke(thickness));
		        	 g2.setColor(this.color);
		             g2.drawLine(xPos.get(i), yPos.get(i), xPos.get(i + 1), yPos.get(i + 1));
				 }
			 }
			 
			 if(buttonOption == "2")
			 {
				 for(int i=0; i<xPos.size() - 1; i++)
				 {
					 g.setColor(Color.WHITE);
		             g.fillRect(xPos.get(i) - 5, yPos.get(i) - 5, 30, 30);
				 }
			 }
		}
		
		BufferedImage image;
		Color color = Color.BLACK;
		ArrayList<Integer> xPos = new ArrayList<Integer>();
	    ArrayList<Integer> yPos = new ArrayList<Integer>();
	    int thickness =5;
	}
	
	ActionListener buttonListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e)
		{
			buttonOption = e.getActionCommand();
		}
	};
	
	
	ActionListener colorListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e) 
		{
            drawPanel.color = JColorChooser.showDialog(null, "Choose Color", drawPanel.getForeground());
            repaint();
        }
	};
	
	ChangeListener thicknessListener = new ChangeListener() 
	{
		public void stateChanged(ChangeEvent e) 
		{
			drawPanel.thickness = thicknessSlider.getValue();
			repaint();
		}
	};
	
	ActionListener clearListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			drawPanel.image = null;
			repaint();
		}
	};
	
	
	ActionListener saveListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{ 
			BufferedImage img = new BufferedImage(drawPanel.getWidth(), drawPanel.getHeight(),BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			drawPanel.paintAll(g2d);
										
			File outputfile = new File("file.png");
			
			try 
			{
				ImageIO.write(img, "png", outputfile);
				System.out.println("File saved successfully");
			} 
			catch (IOException x)
			{
				System.out.println(x.getMessage());
			}
		}
	};
	
	ActionListener loadListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{ 
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("myImage.png").getFile());
			
			BufferedImage img = null;

			try 
			{
				img = ImageIO.read(file);
				System.out.println("File loaded successfully");
			} 
			catch(IOException ex)
			{
				System.out.println(ex.getMessage());
			}					
			drawPanel.image = img;
			repaint();
		}
	};
	
	public static void main(String[] args) 
	{
		Rysowanie frame = new Rysowanie();
		frame.setVisible(true);
	}

	JPanel controlsPanel = new JPanel();
	paintPanel drawPanel = new paintPanel();
	JButton colorButton = new JButton("Color");
	JButton clearButton  = new JButton("Clear");
	JRadioButton pencilButton = new JRadioButton("Pencil");
	JRadioButton rubberButton = new JRadioButton("Rubber");
	JSlider thicknessSlider = new JSlider();
	ButtonGroup radioButtons = new ButtonGroup();
	JMenuBar menuBar = new JMenuBar();
	JMenu menu = new JMenu("Menu");
	JMenuItem menuItem1 = new JMenuItem("Save");
	JMenuItem menuItem2 = new JMenuItem("Load");
	String buttonOption = "";


}
