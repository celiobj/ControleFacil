/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Classes.Objeto;
import Persistencia.RepositorioObjeto;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author celio
 */
public final class ControleObjetos extends JPanel{
    
    
   
    JButton aluguel, reserva;
    JTable objetos;
    JScrollPane scroller;
    ControleObjetosAluguel coa;
    ControleObjetosReserva cor;
    RepositorioObjeto ro;
    Objeto obj;
    
    public ControleObjetos(){
       
        
            JLabel tela = new JLabel("... :::   Controle de Objetos   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
        
            
            aluguel = new JButton("Aluguel");
            aluguel.setBounds(50, 20, 100, 20);
            
            reserva = new JButton("Reserva");
            reserva.setBounds(150, 20, 100, 20);
            
            
            
            add(aluguel);
            add(reserva);
            
            
            atualizaTabela();
            
             aluguel.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent e){

                       removeTudo();
                    
                    coa =  new ControleObjetosAluguel();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    coa.setBounds(0, 50, 1100, 449);
                    coa.add(ferramentas);
                   
                    aluguel.setEnabled(false);
                    reserva.setEnabled(true);
                    
                    
                    add(coa);
                    revalidate();
                    repaint();
                   
                                       
            	}
            });
             
             reserva.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent e){

                       removeTudo();
                    
                    cor =  new ControleObjetosReserva();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    cor.setBounds(0, 50, 1100, 449);
                    cor.add(ferramentas);
                   
                    aluguel.setEnabled(true);
                    reserva.setEnabled(false);
                    
                    add(cor);
                    revalidate();
                    repaint();
                   
                                       
            	}
            });
            
            setLayout(null);
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createBevelBorder(1, Color.DARK_GRAY, Color.DARK_GRAY));
            setSize(800,600);
            setVisible(true);
            revalidate();
            repaint();
    }
    public void removeTudo(){
        
                    try{  
                      
                    remove(scroller);
                    }catch(NullPointerException npe){};
        
                    try{  
                      
                    remove(coa);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    remove(cor);
                    }catch(NullPointerException npe){};
                    
                   
           
                    
       }

     public void atualizaTabela(){
        
        
             try{
             remove(scroller);
                    
                 }catch(NullPointerException e){}
             ro = new RepositorioObjeto();
             objetos = ro.consultaTodos();
             scroller = new JScrollPane(objetos, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
             scroller.setBounds(350, 50, 720, 400);
             add(scroller);
    }
}

