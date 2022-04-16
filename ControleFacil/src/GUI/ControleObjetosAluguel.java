/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Classes.CondicoesPagamento;
import Classes.Objeto;
import Persistencia.RepositorioCondPag;
import Persistencia.RepositorioObjeto;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author celio
 */
public final class ControleObjetosAluguel extends JPanel{
    
    JButton alugar, limpar, sair;
    JLabel tela, objetos,nome,endRes, endTrab,telRes, telRes2, telTrab, telTrab2, data_dia, de, ate, valor, formaPag;
    JComboBox tObjetos, tFormaPag;
    JTextField tNome, tEndRes, tEndTrab, tValor;
    JFormattedTextField tDe, tAte,tTel_res,tTel_res2,tTel_trab,tTel_trab2;
    MaskFormatter mascaraData, mascaraTelefone;
    RepositorioObjeto ro;
    RepositorioCondPag rcp;
    
    
    
    public ControleObjetosAluguel(){
        
            JLabel tela = new JLabel("... :::   Controle de Aluguel   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
       
        
            try{
        	mascaraData = new MaskFormatter("##/##/####");
                mascaraTelefone = new MaskFormatter("####-####");
                
           }catch (Exception exp){};
                    
                    tDe = new JFormattedTextField(mascaraData);
                    tAte = new JFormattedTextField(mascaraData);
                    tTel_res = new JFormattedTextField(mascaraTelefone);
                    tTel_res2 = new JFormattedTextField(mascaraTelefone);
                    tTel_trab = new JFormattedTextField(mascaraTelefone);
                    tTel_trab2 = new JFormattedTextField(mascaraTelefone);
                    
                  
            
            
            objetos = new JLabel("Objetos: ");
            nome = new JLabel("Nome: ");
            endRes = new JLabel("End Res: ");
            telRes = new JLabel("Tel: ");
            telRes2 = new JLabel("Tel-2: ");
            endTrab = new JLabel("End Trab: ");
            telTrab = new JLabel("Tel-3: ");
            telTrab2 = new JLabel("Tel-4: ");
            de = new JLabel("De: ");
            ate = new JLabel("Até: ");
            valor = new JLabel("Valor: ");
            formaPag = new JLabel("Forma de Pagamento: ");
            tNome = new JTextField(40);
            ro = new RepositorioObjeto();
            tObjetos = new JComboBox(filtrarObjetos(ro.todos()));
            rcp = new RepositorioCondPag();
            tFormaPag = new JComboBox(filtrarCondicoes(rcp.todas()));
            tEndRes = new JTextField(200);
            tEndTrab = new JTextField(200);
            tValor = new JTextField(40);
            
            objetos.setBounds(20, 70, 100, 20);
            tObjetos.setBounds(80,70 , 200, 20);
            nome.setBounds(20,100,100,20);
            tNome.setBounds(60,100,340,20);
            endRes.setBounds(20,130,100,20);
            tEndRes.setBounds(80,130,320,20);
            telRes.setBounds(420,130,40,20);
            tTel_res.setBounds(460,130,80,20);
            telRes2.setBounds(560,130,40,20);
            tTel_res2.setBounds(600,130,80,20);
            endTrab.setBounds(20,160,100,20);
            tEndTrab.setBounds(80,160,320,20);
            telTrab.setBounds(420,160,40,20);
            tTel_trab.setBounds(460,160,80,20);
            telTrab2.setBounds(560,160,80,20);
            tTel_trab2.setBounds(600,160,80,20);
            de.setBounds(20, 190, 50, 20);
            tDe.setBounds(80, 190, 100, 20);
            ate.setBounds(20, 220, 50, 20);
            tAte.setBounds(80, 220, 100, 20);
            valor.setBounds(20, 250, 50, 20);
            tValor.setBounds(80, 250, 100, 20);
            formaPag.setBounds(200, 250, 200, 20);
            tFormaPag.setBounds(350, 250, 300, 20);
            
            alugar = new JButton("Alugar");
            alugar.setBounds(50, 400, 100, 20);
            limpar = new JButton("Limpar");
            limpar.setBounds(150, 400, 100, 20);
            sair = new JButton("sair");
            sair.setBounds(250, 400, 100, 20);
            
            
        
            add(alugar);
            add(limpar);
            //add(sair);
            add(endRes);
            add(tEndRes);
            add(telRes);
            add(tTel_res);      
            add(telRes2);
            add(tTel_res2);
            add(endTrab);
            add(tEndTrab);
            add(telTrab);
            add(tTel_trab);
            add(telTrab2);
            add(tTel_trab2);
            add(nome);
            add(tNome);
            add(objetos);
            add(tObjetos);
            add(de);
            add(tDe);
            add(ate);
            add(tAte);
            add(valor);
            add(tValor);
            add(formaPag);
            add(tFormaPag);
           
            
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
    
    public Vector filtrarObjetos(Vector<Objeto> objetos){
        
        Vector retorno = new Vector();
        retorno.addElement(" ");
        String linha;
        //JOptionPane.showMessageDialog(null,objetos.get(1).getDescricao());
        for(int a = 1; a<= objetos.size()-1;a++){
            
           linha = objetos.get(a).getCodObj()+" - "+objetos.get(a).getDescricao();
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
            
           linha = cond.get(a).getCod_CondPag()+" - "+cond.get(a).getDescricao()+" em "+cond.get(a).getParcelas()+" parcela(s).";
           retorno.addElement(linha);
        }
        
        return retorno;
    }
    
}
