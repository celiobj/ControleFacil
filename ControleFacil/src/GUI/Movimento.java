/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Classes.Dia;
import Classes.Movimentacao;
import DAO.AccessDatabase;
import Persistencia.RepositorioDia;
import Persistencia.RepositorioMovimentacao;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author celio
 */
public class Movimento extends JPanel{
    
    JLabel tela, descricao, valor, tipo, saldo, saldoOff, labelData;
    JTextField tDescricao, tValor;
    JComboBox  tTipo;
    String tipos[]={"","Débito","Crédito"};
    JTable entradas;
    DefaultTableModel dtm;
    Vector linhas,dados,cabecalho;
    JScrollPane scroller;
    Double soma = null;
    Double dif = null;
    JButton botaoInserir, botaoExcluir, botaoGravar,botaoFechar, imprimir;
    Calendar data;
    int dia, dia_semana, mes, ano;
    public static String data_hoje; 
    int data_amanha;
    ImageIcon imageIncluir = new ImageIcon("Icones\\incluir.png");
    ImageIcon imageExcluir = new ImageIcon("Icones\\excluir.png");
    ImageIcon imageSalvar = new ImageIcon("Icones\\salvar.png");
    ImageIcon imageFechar = new ImageIcon("Icones\\fechar.png");
    ImageIcon imageImprimir = new ImageIcon("Icones\\imprimir.png");
    RepositorioMovimentacao rm;
    RepositorioDia rd;
    Movimentacao mov;
    Dia diaClasse;
    Vector<Movimentacao> movimentosDiarios;
    
    
    
    public Movimento() throws ParseException{
       
            JLabel tela = new JLabel("... :::   Controle de Caixa   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
            
                     
                    data = Calendar.getInstance();
                    dia = data.get(Calendar.DAY_OF_MONTH);
                    dia_semana = data.get(Calendar.DAY_OF_WEEK);
                    mes = data.get(Calendar.MONTH);
                    ano = data.get(Calendar.YEAR);
                    data_hoje = +dia+"/"+(mes+1)+"/"+ano;
                    if(dia<10 && mes<10){
                      data_hoje = "0"+dia+"/0"+(mes+1)+"/"+ano;
                  }else
                      if(dia<10 && mes >=10 ){
                          data_hoje = "0"+dia+"/"+(mes+1)+"/"+ano;
                      }else
                          if(dia>=10 && mes <10){
                              data_hoje = dia+"/0"+(mes+1)+"/"+ano;
                          }else{
                              data_hoje = dia+"/"+(mes+1)+"/"+ano;
                          }
            
                    
            labelData = new JLabel(data_hoje);
            saldo = new JLabel("0");
            saldoOff = new JLabel("0");
            botaoInserir = new JButton();
            botaoInserir.setIcon(imageIncluir);
            botaoInserir.setToolTipText("Incluir"); 
            botaoExcluir = new JButton();
            botaoExcluir.setIcon(imageExcluir);
            botaoExcluir.setToolTipText("Excluir"); 
            botaoGravar = new JButton();
            botaoGravar.setIcon(imageSalvar);
            botaoGravar.setToolTipText("Salvar"); 
            botaoFechar = new JButton();
            botaoFechar.setIcon(imageFechar);
            botaoFechar.setToolTipText("Fechar Caixa");
            imprimir = new JButton();
            imprimir.setIcon(imageImprimir);
            imprimir.setToolTipText("Imprimir");
            
           
            descricao = new JLabel("Descrição: ");
            valor = new JLabel("Valor: ");
            tipo = new JLabel("Tipo: ");
            tDescricao = new JTextField();
            tValor = new JTextField();
            tTipo = new JComboBox(tipos);
            cabecalho = new Vector();
            linhas = new Vector();
            dados = new Vector();
            rm = new RepositorioMovimentacao();
            
            
            cabecalho.addElement("Cód");
            cabecalho.addElement("Tipo");
            cabecalho.addElement("Decrição");
            cabecalho.addElement("Valor");
            
            
            
            entradas = new JTable(linhas,cabecalho);
            dtm = (DefaultTableModel)entradas.getModel();
            scroller = new JScrollPane(entradas, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            entradas.getColumnModel().getColumn(0).setPreferredWidth(100);
            entradas.getColumnModel().getColumn(1).setPreferredWidth(100);
            entradas.getColumnModel().getColumn(2).setPreferredWidth(300);
            entradas.getColumnModel().getColumn(3).setPreferredWidth(100);
            
           
            labelData.setBounds(550, 45, 100, 50);
            descricao.setBounds(10, 10, 80, 20);
            valor.setBounds(10, 40, 80, 20);
            tipo.setBounds(10, 70, 80, 20);
            tDescricao.setBounds(80, 10, 200, 20);
            tValor.setBounds(80, 40, 80, 20);
            tTipo.setBounds(80, 70, 150, 20);
            scroller.setBounds(10, 130, 600, 100);
            saldo.setBounds(550, 100, 200, 20);
            botaoInserir.setBounds(610, 130, 50, 50);
            botaoExcluir.setBounds(610, 180, 50, 50);
            botaoFechar.setBounds(665, 130, 50, 50);
            imprimir.setBounds(720, 130, 50, 50);
            
            
            add(labelData);
            add(descricao);
            add(valor);
            add(tipo);
            add(tDescricao);
            add(tValor);
            add(tTipo);
            add(scroller);
            add(saldo);
            add(botaoInserir);
            //add(botaoExcluir);
            add(botaoFechar);
            add(imprimir);
           
            try{
                 
                rd = new RepositorioDia();
                Dia diaint = rd.pegarUltimo();
               
                String retorno = null;
                retorno = rm.verificarMovimentacao(Principal.paraInserirData(data_hoje));
                if(retorno==null){
               
                    
                    Movimentacao movi = new Movimentacao();
                    movi.setData(Principal.paraInserirData(data_hoje));
                    movi.setDescricao("Saldo Anterior");
                    movi.setTipo("Crédito");
                    movi.setValor(diaint.getSaldo_atual());
                    rm.adicionar(movi);
                    Double total = Double.parseDouble(saldoOff.getText());
                    dtm.addRow(new Object[]{rm.pegarUltimoCodigo(),"Crédito","Saldo Anterior",movi.getValor()});
                    
                    for (int i=0; i<entradas.getRowCount(); i++) //Pegue o número de linhas da tabela.  
                        {    

                            Double valor_mostrar = Double.parseDouble(movi.getValor());     

                             soma = valor_mostrar + total;    

                        }  
                BigDecimal bd_valor_total = new BigDecimal (soma);  
                NumberFormat nf_valor_total = NumberFormat.getCurrencyInstance(); 
                saldo.setText("Em caixa: "+Principal.paraFormatoDinheiro(soma));
                saldoOff.setText(soma.toString());
                
                    
                }else{
                    //JOptionPane.showMessageDialog(null, "Entrou aqui!!!");
                    double debito = 0;
                    double  debitoAux = 0;
                    double credito = 0;
                    double  creditoAux = 0;
                    
                    movimentosDiarios = rm.listarMovimentacao(Principal.paraInserirData(data_hoje));
                  
                                   
                    for(int a=0;a<movimentosDiarios.size();a++){
                        
                   
                      
                       String operacao = movimentosDiarios.elementAt(a).getTipo();
                                          
                         switch (operacao) {
                           
                            case "Crédito":
                                creditoAux = Double.parseDouble(Principal.formatoParaInserir(movimentosDiarios.elementAt(a).getValor()));
                                credito = credito + creditoAux;
                               
                                break;
                            case "Débito":
                                 debitoAux =  Double.parseDouble(Principal.formatoParaInserir(movimentosDiarios.elementAt(a).getValor()));
                                 debito = debito + debitoAux;
                               

                             break;
                        }
                        dif = credito-debito;
                        BigDecimal dife = BigDecimal.valueOf(dif);
                        NumberFormat nf_dife = NumberFormat.getCurrencyInstance();
                        saldo.setText(Principal.paraFormatoDinheiro(dif));
                        saldoOff.setText(dif.toString());
                        dtm.addRow(new Object[]{movimentosDiarios.elementAt(a).getCodMovimentacao(),movimentosDiarios.elementAt(a).getTipo(),movimentosDiarios.elementAt(a).getDescricao(),movimentosDiarios.elementAt(a).getValor()});
                        
                        
                    }
                    
                    }
            }catch(ArrayIndexOutOfBoundsException aoe){
                
                JOptionPane.showMessageDialog(null, aoe.getMessage());
                
            }
            
            botaoInserir.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    
                  rm = new RepositorioMovimentacao();
                  mov = new Movimentacao();
                  if(tTipo.getSelectedItem().toString().equals("Crédito")){
                      
                  mov.setTipo(tTipo.getSelectedItem().toString());
                        try {
                            mov.setValor(Principal.formatoParaInserir(tValor.getText()));
                        } catch (ParseException ex) {
                            Logger.getLogger(Movimento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                  mov.setDescricao(tDescricao.getText());
                  mov.setData(Principal.paraInserirData(data_hoje));
                  rm.adicionar(mov);
                    
                  Double total = Double.parseDouble(saldoOff.getText());
                  
                            
                  
                 dtm.addRow(new Object[]{rm.pegarUltimoCodigo(),tTipo.getSelectedItem().toString(),tDescricao.getText(), tValor.getText()});
                 
                  for (int i=0; i<entradas.getRowCount(); i++) //Pegue o número de linhas da tabela.  
                  {    
    
                  Double valor_mostrar = null;     
                          try {
                              valor_mostrar = Double.parseDouble(Principal.formatoParaInserir(String.valueOf( dtm.getValueAt(i,3))));
                          } catch (ParseException ex) {
                              Logger.getLogger(Movimento.class.getName()).log(Level.SEVERE, null, ex);
                          }
    
                   soma = valor_mostrar + total;    

                  }  
                  BigDecimal bd_valor_total = new BigDecimal (soma);  
                  NumberFormat nf_valor_total = NumberFormat.getCurrencyInstance(); 
                  saldo.setText(Principal.paraFormatoDinheiro(soma));
                  saldoOff.setText(soma.toString());
                  
                  
                  
                  
                  tDescricao.setText("");
                  tValor.setText("");
                  tTipo.setSelectedIndex(0);
                      
                  }else
                      if(tTipo.getSelectedItem().toString().equals("Débito")){
                       
                    mov.setTipo(tTipo.getSelectedItem().toString());
                        try {
                            mov.setValor(Principal.formatoParaInserir(tValor.getText()));
                        } catch (ParseException ex) {
                            Logger.getLogger(Movimento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    mov.setDescricao(tDescricao.getText());
                    mov.setData(Principal.paraInserirData(data_hoje));
                    rm.adicionar(mov);
                    
                    Double total = Double.parseDouble(saldoOff.getText());
                  
                   
                 
                                  
                 
                 dtm.addRow(new Object[]{rm.pegarUltimoCodigo(),tTipo.getSelectedItem().toString(),tDescricao.getText(), tValor.getText()});
                 
                  for (int i=0; i<entradas.getRowCount(); i++) //Pegue o número de linhas da tabela.  
                  {    
    
                  Double valor_mostrar = null;     
                              try {
                                  valor_mostrar = Double.parseDouble(Principal.formatoParaInserir(String.valueOf( dtm.getValueAt(i,3))));
                              } catch (ParseException ex) {
                                  Logger.getLogger(Movimento.class.getName()).log(Level.SEVERE, null, ex);
                              }
    
                   soma = total - valor_mostrar;    

                  }  
                  BigDecimal bd_valor_total = new BigDecimal (soma);  
                  NumberFormat nf_valor_total = NumberFormat.getCurrencyInstance(); 
                  saldo.setText(Principal.paraFormatoDinheiro(soma));
                  saldoOff.setText(soma.toString());
                  
                  
                  
                  tDescricao.setText("");
                  tValor.setText("");
                  tTipo.setSelectedIndex(0);
                          
                      }
                    
                
                  
                }                   
            });
        
        
         botaoExcluir.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                
                    
                    try{
                   Double total = Double.parseDouble(saldoOff.getText());
   
                    
                    int l = entradas.getSelectedRow();
                    

                    
                    rm.remover(dtm.getValueAt(l, 0).toString());
                    if(dtm.getValueAt(l, 1).toString().equals("Débito")){
                        
                    Double valor_mostrar = null;     
                            try {
                                valor_mostrar = Double.parseDouble(Principal.formatoParaInserir(String.valueOf( dtm.getValueAt(l,3))));
                            } catch (ParseException ex) {
                                Logger.getLogger(Movimento.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    dtm.removeRow(l);
                    //JOptionPane.showMessageDialog(null, itens.getRowCount());
                    for(int i=0; i<entradas.getRowCount(); i++) {

                   
                                  
                    }
                    soma = total + valor_mostrar; 

                  
                    
                  BigDecimal bd_valor_total = new BigDecimal (soma);  
                  NumberFormat nf_valor_total = NumberFormat.getCurrencyInstance(); 
                  saldo.setText(nf_valor_total.format(bd_valor_total));
                  saldoOff.setText(soma.toString());
                  
                        
                    }else
                       if(dtm.getValueAt(l, 1).toString().equals("Crédito")){
                           
                           Double valor_mostrar = null;     
                            try {
                                valor_mostrar = Double.parseDouble(Principal.formatoParaInserir(String.valueOf( dtm.getValueAt(l,3))));
                            } catch (ParseException ex) {
                                Logger.getLogger(Movimento.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    dtm.removeRow(l);
                    //JOptionPane.showMessageDialog(null, itens.getRowCount());
                    for(int i=0; i<entradas.getRowCount(); i++) {

                    }
                    soma = total - valor_mostrar; 

                  
                    
                  BigDecimal bd_valor_total = new BigDecimal (soma);  
                  NumberFormat nf_valor_total = NumberFormat.getCurrencyInstance(); 
                  saldo.setText(nf_valor_total.format(bd_valor_total));
                  saldoOff.setText(soma.toString());
                           
                       }
                    
                }catch(ArrayIndexOutOfBoundsException ae){
                    
                          
                      }    
                }
                
                
               
            });
         
         botaoFechar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                
                    int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente fechar o caixa?");
                    if(resposta==0){
                        
                        rd = new RepositorioDia();
                        diaClasse = new Dia();
                        
                        diaClasse.setDia_mes(Principal.paraInserirData(data_hoje));
                        diaClasse.setSaldo_anterior(rd.pegarUltimo().getSaldo_atual());
                        
                        
                        String valaux = (saldo.getText()).substring(0,saldo.getText().length());
                        
                        String val = null;
                        if(valaux.substring(0, 1).equals("-")){
                            
                            val = (saldo.getText()).substring(0,1)+ saldo.getText().substring(4,saldo.getText().length());
                            JOptionPane.showMessageDialog(null, val);
                        }else{
                            val = saldo.getText().substring(3,saldo.getText().length());
                        }
                        
                        try {
                            diaClasse.setSaldo_atual(Principal.formatoParaInserir(val));
                        } catch (ParseException ex) {
                            Logger.getLogger(Movimento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        diaClasse.setSituacao("Fechado");
                     
                        rd.adicionar(diaClasse);
                        
                        
                    } 
                    
                    
                }
                
                
               
            });
         
         imprimir.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){

                           JasperPrint rel = null;
                           
                     try{

                       AccessDatabase a = new AccessDatabase();
                       Connection con = a.conectar();
                       HashMap map = new HashMap();
                       Statement st = con.createStatement();
                       String query = "Select * from `bd_ultracaixa`.`movimentacao` where data='"+Principal.paraInserirData(data_hoje)+"'";
                       ResultSet rs = st.executeQuery( query );
                       JRResultSetDataSource jrRS = new JRResultSetDataSource( rs );
                       String arquivoJasper = "Relatorios//diario.jasper";
                       rel = JasperFillManager.fillReport(arquivoJasper, map, jrRS);
                       
                       JasperViewer.viewReport(rel, false);
                       con.close();

                                       }catch(Exception t){
                                               System.out.println(t.getMessage());
                               }
                     
                   

                  }});
         
            setLayout(null);
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createBevelBorder(1, Color.DARK_GRAY, Color.DARK_GRAY));
            setSize(600,400);
            setVisible(true);
            revalidate();
            repaint();
                    
    }
    
}
