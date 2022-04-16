/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Classes.TipoObj;
import Persistencia.RepositorioTipoObj;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author celio
 */
public final class CadTipoObj extends JPanel{
    
    JLabel tipo;
    JTextField tTipo;
    JButton cadastrar,alterar ,limpar, sair;
    JTable tipos;
    JScrollPane scroller;
    RepositorioTipoObj rto;
    TipoObj tipoObj;
    
    public CadTipoObj(){
       
                    
            JLabel tela = new JLabel("... :::   Cadastro de Tipos de Objetos   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
            
             
             tipo = new JLabel("Tipo: ");
             tTipo = new JTextField(80);
             
             cadastrar = new JButton("Cadastrar");
             alterar = new JButton("Alterar");
             limpar = new JButton("Limpar");
             sair = new JButton("Sair");
             
            
             tipo.setBounds(10, 50, 100, 20);
             tTipo.setBounds(100, 50, 200, 20);
             
            
             cadastrar.setBounds(20, 260, 100, 20);
             alterar.setBounds(350,450,100,20);
             limpar.setBounds(120, 260, 100, 20);
             sair.setBounds(220, 260, 100, 20);
             
             
             
             add(tipo);
             add(tTipo);
             
             add(cadastrar);
             add(alterar);
             add(limpar);
             add(sair);
             
             atualizaTabela();
             alterar.setEnabled(false);
             
             cadastrar.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent e){

                       tipoObj = new TipoObj();
                       rto = new RepositorioTipoObj();
                       
                       tipoObj.setTipo(tTipo.getText());
                       rto.adicionar(tipoObj);
                       
                       tTipo.setText("");
                      
                       
                       
                       
                      }
                  });
             
             limpar.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){
                    
                        tTipo.setText("");
                        alterar.setEnabled(false);
                        cadastrar.setEnabled(true);
                       
                       
                       
            	}
            });
             
             tipos.addMouseListener(new MouseListener(){
                            	

                       
                        @Override
                        public void mouseClicked(MouseEvent e) {
                           
                            rto = new RepositorioTipoObj();
                            tipoObj = rto.procurar(tipos.getModel().getValueAt(tipos.getSelectedRow(),0).toString());
                            tTipo.setText(tipoObj.getTipo());
                            
                            alterar.setEnabled(true);
                            cadastrar.setEnabled(false);
                            
                        }

                       
                        @Override
                        public void mousePressed(MouseEvent e) {
                            //throw new UnsupportedOperationException("Not supported yet.");
                        }

                        
                        @Override
                        public void mouseReleased(MouseEvent e) {
                           // throw new UnsupportedOperationException("Not supported yet.");
                        }

                        
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            //throw new UnsupportedOperationException("Not supported yet.");
                        }


                        @Override
                        public void mouseExited(MouseEvent e) {
                           //throw new UnsupportedOperationException("Not supported yet.");
                        
                        }
                        
             });
             
             alterar.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){
                       
                       rto = new RepositorioTipoObj();
                       tipoObj = rto.procurar(tipos.getModel().getValueAt(tipos.getSelectedRow(),0).toString());                       
                       TipoObj novo = new TipoObj();
                       novo.setTipo(tTipo.getText());
                       rto.alterar(novo, tipoObj);
                       tTipo.setText("");
                       
                       atualizaTabela();
                       
                       
            	}
            });
             
             
             sair.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){
                       
                   setVisible(false);
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
    public void atualizaTabela(){
        
        try{
            remove(scroller);
                    
        }catch(NullPointerException e){}
            rto = new RepositorioTipoObj();
            tipos = rto.consultaTodos();
            scroller = new JScrollPane(tipos, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroller.setBounds(350, 50, 720, 400);
            add(scroller);
            revalidate();
            repaint();
            
            
    }
}
