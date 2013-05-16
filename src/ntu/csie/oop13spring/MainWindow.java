package ntu.csie.oop13spring;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWindow extends javax.swing.JFrame {
    protected JPanel Combat_Panel;
    protected JPanel Main_Panel;
    protected JPanel []Stat_PanelArr;
    private JPanel Stat_Panel;
    private JMenu jMenu1;
    private JMenu jMenu3;
    private JMenuBar jMenuBar1;
    
    public MainWindow(String title) {
        initComponents();
        setTitle(title);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        getContentPane();
    }

    private void initComponents() {
        Main_Panel = new JPanel();
        Stat_Panel = new JPanel();
        Stat_PanelArr = new JPanel[8];
        Combat_Panel = new ImagePanel(POOUtil.getCWD() + "images/background.png");
        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu();
        jMenu3 = new JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 600));
        setResizable(false);

        Main_Panel.setPreferredSize(new java.awt.Dimension(800, 551));
        Main_Panel.setLayout(null);

        Stat_Panel.setBackground(new java.awt.Color(153, 153, 255));
        Stat_Panel.setPreferredSize(new java.awt.Dimension(1000, 100));
        Stat_Panel.setLayout(new java.awt.GridLayout(1, 8));

        for (int i=0; i<Stat_PanelArr.length; i++){
            Stat_PanelArr[i] = new JPanel();
            Stat_PanelArr[i].setBackground(Stat_Panel.getBackground());
            Stat_PanelArr[i].setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(204, 204, 204)));

            GroupLayout P1_Stat_PanelLayout = new javax.swing.GroupLayout(Stat_PanelArr[i]);
            Stat_PanelArr[i].setLayout(P1_Stat_PanelLayout);
            P1_Stat_PanelLayout.setHorizontalGroup(
                P1_Stat_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 124, Short.MAX_VALUE)
            );
            P1_Stat_PanelLayout.setVerticalGroup(
                P1_Stat_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 59, Short.MAX_VALUE)
            );

            Stat_Panel.add(Stat_PanelArr[i]);
        }

        Main_Panel.add(Stat_Panel);
        Stat_Panel.setBounds(0, 1, 1000, 60);

        Combat_Panel.setBackground(new java.awt.Color(0, 204, 204));
        Combat_Panel.setLayout(null);
        Main_Panel.add(Combat_Panel);
        Combat_Panel.setBounds(0, 60, 1000, 490);

        getContentPane().add(Main_Panel, java.awt.BorderLayout.CENTER);
        Main_Panel.getAccessibleContext().setAccessibleDescription("");

        jMenuBar1.setBackground(new java.awt.Color(204, 255, 255));
        jMenuBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jMenu1.setText("Credit");
        jMenu1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu3.setText("Exit");
        jMenu3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu3);
        setJMenuBar(jMenuBar1);
        pack();
    }

    protected JPanel getStatPanels(int i){
        return Stat_PanelArr[i-1];
    }
    
    private void jMenu3MouseClicked(MouseEvent evt) {
        AlertFrame frame = new AlertFrame("thank you for playing POOArena!!");
        frame.setVisible(true);        
        dispose();
    }

    private void jMenu1MouseClicked(MouseEvent evt) {
        new AlertFrame("Written by B99902006 Po-Hsien Wang").setVisible(true);
    }
}
