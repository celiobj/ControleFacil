/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Classes.CondicoesPagamento;
import Classes.Produto;
import Persistencia.RepositorioCondPag;
import Persistencia.RepositorioConta;
import Persistencia.RepositorioProduto;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author celio
 */
public final class saidaProduto extends JPanel{
    
    JButton gravar, limpar, sair;
    JLabel tela, qtd, produtos, fontPag, formPag;
    JComboBox tProdutos, tFontPag,tFormPag;
    JTextField tQtd;
    String contas[]={"","LocaSul","Priscila","Marquinho", "Santander", "Bradesco","Dinheiro"};
    Calendar data;
    int dia, mes, ano;
    RepositorioProduto rp;
    RepositorioCondPag rcp;
    RepositorioConta rc;
    Produto produto;
    CondicoesPagamento cp;
    
    
    public saidaProduto(){
        
            JLabel tela = new JLabel("... :::   Saída de Produtos   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
       
        
                  
            fontPag = new JLabel("Fonte do Pagamento: ");
            formPag = new JLabel("Forma do Pagamento");
            produtos = new JLabel("Produtos: ");
            qtd = new JLabel("Qtd: ");
            tQtd = new JTextField(40);
            rp = new RepositorioProduto();
            tProdutos = new JComboBox(filtrarObjetos(rp.todos()));
            rcp = new RepositorioCondPag();
            tFormPag = new JComboBox(filtrarCondicoes(rcp.todas()));
            tFontPag = new JComboBox(contas);
            
            produtos.setBounds(10, 70, 100, 20);
            tProdutos.setBounds(150,70 , 200, 20);
            qtd.setBounds(10, 100, 50, 20);
            tQtd.setBounds(150,100,50,20);
            fontPag.setBounds(10, 130, 150, 20);
            tFontPag.setBounds(150, 130, 250, 20);
            formPag.setBounds(10, 160, 150, 20);
            tFormPag.setBounds(150, 160, 250, 20);
            
            gravar = new JButton("Gravar");
            gravar.setBounds(50, 380, 100, 20);
            limpar = new JButton("Limpar");
            limpar.setBounds(150, 380, 100, 20);
            sair = new JButton("sair");
            sair.setBounds(250, 380, 100, 20);
            
            
            
            add(gravar);
            add(limpar);
            //add(sair);
            add(tQtd);
            add(tProdutos);
            add(tFontPag);
            add(produtos);
            add(qtd);
            add(fontPag);
           // add(formPag);
           // add(tFormPag);
           
            
            gravar.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent e){

                   rp = new RepositorioProduto();
                   rc = new RepositorioConta();
                   rcp = new RepositorioCondPag();
                   produto = rp.procurar(tProdutos.getSelectedItem().toString());
                   //cp = rcp.procurar(tFormPag.getSelectedItem().toString());
                   int antes = Integer.parseInt(produto.getQtd());
                   int soma = Integer.parseInt(tQtd.getText());
                   if(antes==0||antes < soma){
                       
                       JOptionPane.showMessageDialog(null,"Produto insuficiente no estoque!!! \nDisponível: "+antes+"\nSolicitado: "+soma);
                       
                   }else{
                   
                   int novaQtd = antes - soma;
                   Double valor = null;
                       try {
                           valor = soma * Double.parseDouble(Principal.formatoParaInserir((produto.getValor())));
                       } catch (ParseException ex) {
                           Logger.getLogger(saidaProduto.class.getName()).log(Level.SEVERE, null, ex);
                       }
                   String valorTexto = Double.toString(valor);
                   rc.depositar("Crédito", valorTexto, "BcoBrasil", "Venda de "+soma+" "+produto.getDescProduto()+"(s)");
                   rp.vender(produto, novaQtd);
                   
                   }
               }
                
            });
            
            sair.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent e){

                   
                   setVisible(false);
           
               }
                
            });
            
            setLayout(null);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createBevelBorder(1, Color.DARK_GRAY, Color.DARK_GRAY));
            setSize(800,500);
            setVisible(true);
            revalidate();
            repaint();
    }
    
    public Vector filtrarObjetos(Vector<Produto> produtos){
        
        Vector retorno = new Vector();
        retorno.addElement(" ");
        String linha;
        //JOptionPane.showMessageDialog(null,objetos.get(1).getDescricao());
        for(int a = 1; a<= produtos.size()-1;a++){
            
           linha = produtos.get(a).getCodProduto();
           retorno.addElement(linha);
        }
        
        return retorno;
    }
    
     public Vector filtrarCondicoes(Vector<CondicoesPagamento> cond){
        
        Vector retorno = new Vector();
        retorno.addElement(" ");
        String linha;
        //JOptionPane.showMessageDialog(null,objetos.get(1).getDescricao());
        for(int a = 1; a<= cond.size()-1;a++){
            
           linha = cond.get(a).getCod_CondPag();
           retorno.addElement(linha);
        }
        
        return retorno;
    }
    
}
