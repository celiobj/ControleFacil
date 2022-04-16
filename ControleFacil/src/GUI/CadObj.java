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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
public final class CadObj extends JPanel{
    
    JLabel tipo, descricao;
    JComboBox tTipo;    
    JTextField  tDescricao;
    JButton cadastrar,alterar ,limpar, sair;
    RepositorioObjeto ro;
    Objeto obj;
    JTable objetos;
    JScrollPane scroller;
    
    
    public CadObj(){
       
          
            JLabel tela = new JLabel("... :::   Cadastro de Objetos   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
           
             tipo = new JLabel("Tpo: ");
             ro = new RepositorioObjeto();
             tTipo = new JComboBox(ro.listarTipos());
             
             descricao = new JLabel("Descrição: ");
             tDescricao = new JTextField(8);
             
             
             cadastrar = new JButton("Cadastrar");
             alterar = new JButton("Alterar");
             limpar = new JButton("Limpar");
             sair = new JButton("Sair");
             
           
             
             tipo.setBounds(10, 20, 100, 20);
             tTipo.setBounds(100, 20, 180, 20);
             
             descricao.setBounds(10, 50, 100, 20);
             tDescricao.setBounds(100, 50, 120, 20);
             
             cadastrar.setBounds(20, 130, 100, 20);
             alterar.setBounds(350,450,100,20);
             limpar.setBounds(120, 130, 100, 20);
             sair.setBounds(220, 130, 100, 20);
             
           
             
             add(tipo);
             add(tTipo);
             
             add(descricao);
             add(tDescricao);
             
             add(cadastrar);
             add(alterar);
             add(limpar);
             add(sair);
             
             alterar.setEnabled(false);
             atualizaTabela();
             
             cadastrar.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent e){

                       obj = new Objeto();
                       ro = new RepositorioObjeto();
                       
                       obj.setCodTipoObj(tTipo.getSelectedItem().toString());
                       obj.setDescricao(tDescricao.getText());
                       obj.setStatus("Livre");
                       obj.setSituacao("L");
                     
                       ro.adicionar(obj);
                     
                       tTipo.setSelectedIndex(0);
                       tDescricao.setText("");
                       atualizaTabela();
                       
                       
                       
                       
            	}
            });
             
             limpar.addActionListener(new ActionListener(){
                 public void actionPerformed(ActionEvent e){

                      tTipo.setSelectedIndex(0);
                      tDescricao.setText("");
                      alterar.setEnabled(false);
                      cadastrar.setEnabled(true);
                                       
            	}
            });
             
             objetos.addMouseListener(new MouseListener(){
                            	

                        @Override
                        public void mouseClicked(MouseEvent e) {
                           
                           ro = new RepositorioObjeto();
                           obj = ro.procurar(objetos.getModel().getValueAt(objetos.getSelectedRow(),0).toString());
                           //JOptionPane.showMessageDialog(null, car.getModelo());
                           //tTipo.setText(apt.getNum_apt());
                           tDescricao.setText(obj.getDescricao());
                            
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

                       ro = new RepositorioObjeto();
                       obj = ro.procurar(objetos.getModel().getValueAt(objetos.getSelectedRow(),0).toString());
                       Objeto alterado = new Objeto();
                       obj.setCodTipoObj(tTipo.getSelectedItem().toString());
                       obj.setDescricao(tDescricao.getText());
                       obj.setStatus("Ativo");
                       obj.setSituacao("L");
                       ro.alterar(obj, alterado);
                     
                       tTipo.setSelectedIndex(0);
                       tDescricao.setText("");
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
             ro = new RepositorioObjeto();
             objetos = ro.consultaTodos();
             scroller = new JScrollPane(objetos, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
             scroller.setBounds(350, 50, 720, 400);
             add(scroller);
    }
}
