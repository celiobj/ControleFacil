/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Classes.Produto;
import Persistencia.RepositorioProduto;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author celio
 */
public final class entradaSaida extends JPanel{
    
    JLabel  marca, descricao, fornecedor, valor;
    JTextField tMarca,tDescricao, tFornecedor, tValor;
    JButton cadastrar,alterar ,limpar, sair, entProd, saidProd;
    JTable produtos;
    JScrollPane scroller;
    RepositorioProduto rp;
    Produto produto;
    entradaProduto ep;
    saidaProduto sp;
    
    
    public entradaSaida(){
       
            
            JLabel tela = new JLabel("... :::   Movimentação de Produtos   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
            
             descricao = new JLabel("Descrição: ");
             tDescricao = new JTextField(10);
             
             valor = new JLabel("Valor (Sem R$): ");
             tValor = new JTextField(4);
             
             fornecedor = new JLabel("Fornecedor: ");
             tFornecedor = new JTextField(45);
             
             cadastrar = new JButton("Cadastrar");
             alterar = new JButton("Alterar");
             limpar = new JButton("Limpar");
             sair = new JButton("Sair");
             entProd = new JButton("Entrada");
             saidProd = new JButton("Saída");
             
             
             
             descricao.setBounds(10, 20, 180, 20);
             tDescricao.setBounds(120, 20, 200, 20);
             
             valor.setBounds(10, 50, 100, 20);
             tValor.setBounds(120, 50, 80, 20);
             
             fornecedor.setBounds(10, 80, 100, 20);
             tFornecedor.setBounds(120, 80, 180, 20);
             
             cadastrar.setBounds(20, 130, 100, 20);
             alterar.setBounds(350,450,100,20);
             limpar.setBounds(120, 130, 100, 20);
             sair.setBounds(220, 130, 100, 20);
             
             entProd.setBounds(10, 10, 100, 20);
             saidProd.setBounds(110, 10, 100, 20);
             
             
             
             //add(descricao);
             //add(tDescricao);
             
             //add(valor);
             //add(tValor);
             
             //add(fornecedor);
             //add(tFornecedor);
             
             //add(cadastrar);
             //add(alterar);
             //add(limpar);
             //add(sair);
             add(entProd);
             add(saidProd);
             
             
             alterar.setEnabled(false);
             atualizaTabela();
             
             
             cadastrar.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent e){

                       produto = new Produto();
                       rp = new RepositorioProduto();
                       
                        produto.setDescProduto(tDescricao.getText());
                     try {
                        produto.setValor(Principal.formatoParaInserir(tValor.getText()));
                     } catch (ParseException ex) {
                         Logger.getLogger(CadObj.class.getName()).log(Level.SEVERE, null, ex);
                     }
                        produto.setFornecedor(tFornecedor.getText());
                        produto.setQtd("0");
                     
                        rp.adicionar(produto);
                     
                        tDescricao.setText("");
                        tFornecedor.setText("");
                        tValor.setText("");
                        atualizaTabela();
                       
                       
                       
                       
            	}
            });
             
             
             
             entProd.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent e){

                       removeTudo();
                    
                    ep =  new entradaProduto();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    ep.setBounds(0, 30, 800, 430);
                    ep.add(ferramentas);
                   
                    entProd.setEnabled(false);
                    saidProd.setEnabled(true);
                    
                    
                    add(ep);
                    revalidate();
                    repaint();
                   
                                       
            	}
            });
             
             saidProd.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent e){

                       removeTudo();
                    
                    sp =  new saidaProduto();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    sp.setBounds(0, 30, 800, 430);
                    sp.add(ferramentas);
                   
                    saidProd.setEnabled(false);
                    entProd.setEnabled(true);
                    
                    
                    add(sp);
                    revalidate();
                    repaint();
                   
                                       
            	}
            });
             
             limpar.addActionListener(new ActionListener(){
                 public void actionPerformed(ActionEvent e){

                      tDescricao.setText("");
                      tFornecedor.setText("");
                      tValor.setText("");
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
    
      public void removeTudo(){
        
                    try{  
                      
                    remove(scroller);
                    }catch(NullPointerException npe){};
        
                    try{  
                      
                    remove(ep);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    remove(sp);
                    }catch(NullPointerException npe){};
                    
                   
           
                    
       }
    
     public void atualizaTabela(){
        
        
             try{
            remove(scroller);
                    
                 }catch(NullPointerException e){}
             rp = new RepositorioProduto();
             produtos = rp.consultaTodos();
             scroller = new JScrollPane(produtos, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
             scroller.setBounds(350, 50, 720, 400);
             add(scroller);
    }
    
}
