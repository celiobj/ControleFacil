/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author celio
 */
public class PainelControle extends JPanel{
    
 
    
    public PainelControle(){
       
            JLabel tela = new JLabel("... :::   Painel de Controle   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
            
            setLayout(null);
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createBevelBorder(1, Color.DARK_GRAY, Color.DARK_GRAY));
            setSize(800,800);
            setVisible(true);
            revalidate();
            repaint();
    }
    
}
