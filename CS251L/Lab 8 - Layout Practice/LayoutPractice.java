/**
 * Anish Adhikari UNM- aaadhikari999@gmail.com
 * CS 251 - Lab 8 Layout Practice
 * 
 * This program is a sample swing GUI program in preparation for our final lab that
 * will be using swing in a game fashion.
 * 
 * The entire functionality of this program is to count how many button presses occur on a button.
 * 
 * LayoutPractice.java
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;



public class LayoutPractice extends JFrame {
    
    int intClicks;  
    JButton btnCount, btnCrazy, btnReset;
    JTextPane tpTitle;
    JTextArea taClicks;
    DrawingPanel pnlShapes;
    JPanel pnlInfo;

    public static void main(String[] args) {
        LayoutPractice lp = new LayoutPractice();
        lp.setTitle("Layout Practice - Anthony Galczak");
        lp.setSize(600,500);
        lp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lp.setLocationRelativeTo(null);
        lp.setVisible(true);
    }
    
    public LayoutPractice() {
        initComponents();
        btnCount.addActionListener(new countButtonListener());
        btnCrazy.addActionListener(new crazyButtonListener());
        btnReset.addActionListener(new resetButtonListener());
    }
    
    /**
     * Method for initializing all the swing components for this program.
     * This will be called once when the constructor is called to build the scene.
     */
    public void initComponents() {
        intClicks = 0;
        
        btnCount = new JButton("Click me for a random circle!");
        btnCrazy = new JButton("I heard you like circles");
        btnReset = new JButton("Reset clicks");
        tpTitle = new JTextPane();
        taClicks = new JTextArea();
        pnlShapes = new DrawingPanel();
        pnlInfo = new JPanel();
        
        update();
        
        // Setting some style to my title text pane
        Font f1 = new Font("TimesRoman", Font.BOLD, 24);
        tpTitle.setText("Layout Practice!");
        tpTitle.setEditable(false);
        tpTitle.setFont(f1);
        tpTitle.setBackground(Color.CYAN);
        SimpleAttributeSet attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
        tpTitle.setParagraphAttributes(attribs, true);
        tpTitle.setPreferredSize(new Dimension(getWidth(), 30));
        
        // Text Area for how many times the user has clicked
        taClicks.setEditable(false);
        taClicks.setBackground(Color.CYAN);
        
        // Area for drawing shapes
        pnlShapes.setLayout(new GridLayout());
        
        // Grid for holding labels/buttons for user I/O
        pnlInfo.setLayout(new GridLayout(2,2));
        pnlInfo.add(btnCount);
        pnlInfo.add(taClicks);
        pnlInfo.add(btnCrazy);
        pnlInfo.add(btnReset);
        pnlInfo.setBackground(Color.CYAN);
        
        // 3 primary sections
        add(tpTitle, BorderLayout.PAGE_START);
        add(pnlShapes, BorderLayout.CENTER);
        add(pnlInfo, BorderLayout.SOUTH);
        
    }
    
    /**
     * Custom panel for drawing components on the screen.
     * Calls a circle drawing method.
     */
    public class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawCircles(g, intClicks);
        }
    }
    
    /**
     * Method for drawing random circles.
     * @param g The graphics object to paint on.
     * @param clicks How many circles to draw, corresponds with user's clicks also.
     */
    public void drawCircles(Graphics g, int clicks) {
        int randomXPos, randomYPos, randomSize, randomColor;
        Random rand = new Random(Instant.now().getNano());
        
        Color[] colors = {Color.BLUE, Color.BLACK, Color.CYAN, Color.RED, Color.GREEN,
                Color.WHITE, Color.YELLOW, Color.DARK_GRAY, Color.PINK, Color.MAGENTA
        };
        
        for(int i = 0; i < clicks; ++i) {
            randomXPos = rand.nextInt(getWidth() - 50);
            randomYPos = rand.nextInt(getHeight() - 50);
            randomSize = rand.nextInt(150) + 50;
            randomColor = rand.nextInt(10);
            g.setColor(colors[randomColor]);
            g.fillOval(randomXPos, randomYPos, randomSize, randomSize);       
        }
    }
    
    /**
     * Action listener for the regular button. Will display a 
     * JOptionPane for the user to ooh and aah at.
     *
     */
    public class countButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            intClicks++;
            update();
            JOptionPane.showMessageDialog(null, "You have clicked " + intClicks + " time(s).");
        }
    }
    
    /**
     * Action listener for the crazy button. Will end up displaying a ton of
     * circles on screen.
     *
     */
    public class crazyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            intClicks = 9001;
            update();
        }
    }
    
    /**
     * Action listener for the reset button. Will reset the clicks to 0.
     */
    public class resetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            intClicks = 0;
            update();
        }
    }
    
    /**
     * Simple update method that will repaint the center panel and the clicks label
     */
    public void update() {
        pnlShapes.repaint();
        taClicks.setText("Button Clicks: " + intClicks);
    }

}
