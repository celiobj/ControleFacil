/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Classes.CondicoesPagamento;
import Persistencia.RepositorioCondPag;
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
public final class CadCondPag extends JPanel{
    
    JLabel  descricao, parcelas;
    JTextField tDescricao, tParcelas;
    JButton cadastrar,alterar ,limpar, sair;
    JTable condicoes;
    JScrollPane scroller;
    RepositorioCondPag rcp;
    CondicoesPagamento cp;
    
    
    public CadCondPag(){
       
            JLabel tela = new JLabel("... :::   Cadastro de Condições de Pagamento   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
            
             
             descricao = new JLabel("Descrição: ");
             tDescricao = new JTextField(10);
             
                      
             parcelas = new JLabel("Parcelas: ");
             tParcelas = new JTextField(45);
             
             cadastrar = new JButton("Cadastrar");
             alterar = new JButton("Alterar");
             limpar = new JButton("Limpar");
             sair = new JButton("Sair");
             
           
             
             descricao.setBounds(10, 20, 180, 20);
             tDescricao.setBounds(120, 20, 200, 20);
             
             parcelas.setBounds(10, 80, 100, 20);
             tParcelas.setBounds(120, 80, 180, 20);
             
             cadastrar.setBounds(20, 130, 100, 20);
             alterar.setBounds(350,450,100,20);
             limpar.setBounds(120, 130, 100, 20);
             sair.setBounds(220, 130, 100, 20);
             
           
             
             add(descricao);
             add(tDescricao);
             
             add(parcelas);
             add(tParcelas);
             
             add(cadastrar);
             add(alterar);
             add(limpar);
             add(sair);
             
             alterar.setEnabled(false);
             atualizaTabela();
             
             
             cadastrar.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent e){

                       cp = new CondicoesPagamento();
                       rcp = new RepositorioCondPag();
                       
                       cp.setDescricao(tDescricao.getText());
                       cp.setParcelas(tParcelas.getText());
                     
                        rcp.adicionar(cp);
                     
                        tDescricao.setText("");
                        tParcelas.setText("");
                        
                        atualizaTabela();
                       
                       
                       
                       
            	}
            });
             
             condicoes.addMouseListener(new MouseListener(){
                            	

                        @Override
                        public void mouseClicked(MouseEvent e) {
                           
                            rcp = new RepositorioCondPag();
                            cp = rcp.procurar(condicoes.getModel().getValueAt(condicoes.getSelectedRow(),0).toString());
                            //JOptionPane.showMessageDialog(null, car.getModelo());
                            tDescricao.setText(cp.getDescricao());
                            tParcelas.setText(cp.getParcelas());
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

                       rcp = new RepositorioCondPag();
                       cp = rcp.procurar(condicoes.getModel().getValueAt(condicoes.getSelectedRow(),0).toString());
                       CondicoesPagamento alterado = new CondicoesPagamento();
                       
                       alterado.setDescricao(tDescricao.getText());
                       alterado.setParcelas(tParcelas.getText());
                        
                       rcp.alterar(cp, alterado);
                     
                      tDescricao.setText("");
                      tParcelas.setText("");
                      alterar.setEnabled(false);
                      cadastrar.setEnabled(true);
                      atualizaTabela();
            	}
            });
             
             
             limpar.addActionListener(new ActionListener(){
                 public void actionPerformed(ActionEvent e){

                      tDescricao.setText("");
                      tParcelas.setText("");
                      alterar.setEnabled(false);
                      cadastrar.setEnabled(true);
                                       
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
             rcp = new RepositorioCondPag();
             condicoes = rcp.consultaTodos();
             scroller = new JScrollPane(condicoes, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
             scroller.setBounds(350, 50, 720, 400);
             add(scroller);
    }
    
}
