    


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Classes.Cliente;
import Classes.Emprestimo;
import DAO.AccessDatabase;
import Persistencia.RepositorioCliente;
import Persistencia.RepositorioConta;
import Persistencia.RepositorioEmprestimo;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author casa
 */
public class Controle extends JPanel {

    JComboBox acao,fonte;
    String tipo[]={"Ação","Pegou mais", "Rolou", "Amortizou","Finalizar"};
    String contas[]={"Financeiro","LocaSul","Priscila","Marquinho", "Santander", "Bradesco","Dinheiro"};
    JButton saldo,consultar, ver_end,finalizar, renovar,filtrar, limpar,imprimir, sair;
    JLabel valor, cod, nome,end_res,end_trab, juros, valor_pag,tel_res,tel_res2, tel_trab,tel_trab2,outros, data_pag,data_emp, pegou_mais, total, obs_emp;
    JTextField tSaldo,tSaldoFals, tCod, tNome,tEnd_res,tEnd_trab, tJuros, tValor_pag, tPegou_mais, tOutros, tObs_emp;
    JFormattedTextField tData_pag,tData_emp,tTel_res,tTel_res2, tTel_trab, tTel_trab2;
    MaskFormatter mascaraData_pag,mascaraData_emp, mascaraTel, mascaraTel2, mascaraTel3, mascaraTel4;
    RepositorioEmprestimo re;
    RepositorioCliente rc;
    RepositorioConta rco;
    JTable tabela;
    JScrollPane scroller;
    String  data_ren;
   

    public Controle() {

        
            JLabel tela = new JLabel("... :::   Controle   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
        
        consultar = new JButton("|Consultar|");
        acao = new JComboBox(tipo);
        fonte = new JComboBox(contas);
        ver_end = new JButton("|Ver Endereço|");
        saldo = new JButton("|Saldo|");
        filtrar = new JButton("|Filtrar|");
        sair = new JButton("|Sair|");
        renovar = new JButton("|Gravar|");
        imprimir = new JButton("|Imprimir|");
        obs_emp = new JLabel("Observação");
        tObs_emp = new JTextField(250);
       // finalizar = new JButton("|Finalizar|");
        limpar = new JButton("|Limpar|");
        outros = new JLabel("Outros:");
        valor = new JLabel("Saldo: ");
        cod = new JLabel("Código: ");
        nome = new JLabel("Nome: ");
        end_res = new JLabel("End Resid: ");
        end_trab = new JLabel("End Trab: ");
        tel_res = new JLabel("Tel Res: ");
        tel_res2 =  new JLabel("Tel Res2: ");
        tel_trab = new JLabel("Tel Trab: ");
        tel_trab2 = new JLabel("Tel Trab2: ");
        valor_pag = new JLabel("Pagou: ");
        juros = new JLabel("Juros: ");
        data_emp = new JLabel("Data: ");
        data_pag = new JLabel("Bom P/: ");
        pegou_mais = new JLabel("Pegou +: ");
        total = new JLabel();
        tPegou_mais = new JTextField(5);
        tJuros = new JTextField(5);
        tValor_pag = new JTextField(5);
        tSaldo = new JTextField(5);
        tSaldoFals = new JTextField(15);
        tCod = new JTextField(5);
        tNome = new JTextField(40);
        tEnd_res = new JTextField(200);
        tEnd_trab = new JTextField(200);
        tOutros = new JTextField(200);
        
        
        try{
     	   	mascaraData_pag = new MaskFormatter("##/##/####");
                mascaraData_emp = new MaskFormatter("##/##/####");
     	   	mascaraTel = new MaskFormatter("####-####");
     	  	mascaraTel2 = new MaskFormatter("####-####");
     	  	mascaraTel3 = new MaskFormatter("####-####");
     	  	mascaraTel4 = new MaskFormatter("####-####");
                              
        	}catch (Exception exp){};
                 tData_pag = new JFormattedTextField(mascaraData_pag);
                 tData_emp = new JFormattedTextField(mascaraData_emp);
                 tTel_res = new JFormattedTextField(mascaraTel);
                 tTel_res2 = new JFormattedTextField(mascaraTel2);
                 tTel_trab = new JFormattedTextField(mascaraTel3);
                 tTel_trab2 = new JFormattedTextField(mascaraTel4);
                
        
        consultar.setMnemonic(KeyEvent.VK_C);
        sair.setMnemonic(KeyEvent.VK_R);

        cod.setBounds(20,80,50,20);
        tCod.setBounds(80,80,50,20);
        filtrar.setBounds(140,80,100,20);
        nome.setBounds(20,110,100,20);
        tNome.setBounds(60,110,340,20);
        end_res.setBounds(20,140,100,20);
        tEnd_res.setBounds(100,140,300,20);
        end_trab.setBounds(20,170,100,20);
        tEnd_trab.setBounds(100,170,300,20);
        tel_res.setBounds(420,140,60,20);
        tTel_res.setBounds(480,140,80,20);
        tel_res2.setBounds(570,140,60,20);
        tTel_res2.setBounds(630,140,80,20);
        tel_trab.setBounds(420,170,60,20);
        tTel_trab.setBounds(480,170,80,20);
        tel_trab2.setBounds(570,170,60,20);
        tTel_trab2.setBounds(630,170,80,20);
        outros.setBounds(730,140,80,20);
        tOutros.setBounds(790,140,200,20);
        valor.setBounds(20,240,100,20);
        tSaldo.setBounds(60,240,100,20);
        tSaldoFals.setBounds(60,240,100,20);
        pegou_mais.setBounds(320,240,100,20);
        tPegou_mais.setBounds(380,240,100,20);
        valor_pag.setBounds(320,270,100,20);
        tValor_pag.setBounds(380,270,100,20);
        juros.setBounds(320,300,100,20);
        tJuros.setBounds(380,300,100,20);
        obs_emp.setBounds(490,270,100,20);
        tObs_emp.setBounds(580,270,250,20);
        data_emp.setBounds(150,270,100,20);
        data_pag.setBounds(150,300,100,20);
        tData_emp.setBounds(210,270,100,20);
        tData_pag.setBounds(210,300,100,20);
        renovar.setBounds(750,240,100,20);
        total.setBounds(830,280,100,20);
      //  finalizar.setBounds(800,240,100,20);
        limpar.setBounds(850,240,100,20);
        saldo.setBounds(900,280,100,20);
        acao.setBounds(500,240,100,20);
        fonte.setBounds(610,240,130,20);
        consultar.setBounds(500, 80, 100, 20);
        ver_end.setBounds(500,110,140,20);
        sair.setBounds(600, 80, 100, 20);
        imprimir.setBounds(800,80,100,20);;

        add(obs_emp);
        add(tObs_emp);
        add(saldo);
        add(valor);
        add(tSaldo);
        add(tSaldoFals);
        add(filtrar);
        add(nome);
        add(tNome);
        add(end_res);
        add(tEnd_res);
        add(end_trab);
        add(tEnd_trab);
        add(tel_res);
        add(tTel_res);
        add(tel_res2);
        add(tTel_res2);
        add(tel_trab);
        add(tTel_trab);
        add(tel_trab2);
        add(tTel_trab2);
        add(cod);
        add(tCod);
        add(tValor_pag);
        add(valor_pag);
        add(data_pag);
        add(data_emp);
        add(tData_emp);
        add(tData_pag);
        add(juros);
        add(tJuros);
        add(acao);
        add(fonte);
        add(pegou_mais);
        add(tPegou_mais);
       //tela.add(finalizar);
        add(renovar);
        add(ver_end);
        add(limpar);
        add(total);
       //tela.add(scroller);
        add(sair);
        add(imprimir);
        add(consultar);
        add(outros);
        add(tOutros);
        
        tCod.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tEnd_res.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tEnd_trab.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tTel_res.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tTel_res2.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tTel_trab.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tTel_trab2.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tSaldoFals.setFont(new Font("Times New Roman", Font.BOLD, 14));
        tNome.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tValor_pag.setFont(new Font("Times New Roman", Font.BOLD, 14));
        tData_emp.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tData_pag.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tJuros.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tPegou_mais.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
        tOutros.setFont(new Font("Times New Roman", Font.BOLD, 14));
        tObs_emp.setFont(new Font("Times New Roman", Font.BOLD, 14)); 

        obs_emp.setVisible(false);
        tObs_emp.setVisible(false);
        tSaldo.setEditable(false);
        tSaldo.setVisible(false);
        tSaldoFals.setEditable(false);
        tNome.setEditable(false);
        tEnd_res.setEditable(false);
        tEnd_trab.setEditable(false);
        tTel_res.setEditable(false);
        tTel_res2.setEditable(false);
        tTel_trab.setEditable(false);
        tTel_trab2.setEditable(false);
        tOutros.setEditable(false);
        ver_end.setVisible(false);
        tCod.setEditable(false);
        filtrar.setVisible(false);
        fonte.setVisible(false);
        data_emp.setVisible(false);
        tData_emp.setVisible(false);
        data_pag.setVisible(false);
        tData_pag.setVisible(false);
        valor_pag.setVisible(false);
        juros.setVisible(false);
        tJuros.setVisible(false);
        tValor_pag.setVisible(false);
        acao.setVisible(false);
        saldo.setVisible(false);
        tPegou_mais.setVisible(false);
        pegou_mais.setVisible(false);
        imprimir.setVisible(false);
    

     
        acao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(acao.getSelectedItem().equals("Pegou mais")){
                    try {
                        tSaldoFals.setText("R$ "+Principal.paraFormatoDinheiro(Double.parseDouble(Principal.formatoParaInserir(tSaldo.getText()))));
                    } catch (ParseException ex) {
                        Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tPegou_mais.setVisible(true);
                    pegou_mais.setVisible(true);
                    obs_emp.setVisible(true);
                    tObs_emp.setVisible(true);
                    data_emp.setVisible(true);
                    tData_emp.setVisible(true);
                    data_pag.setVisible(true);
                    tData_pag.setVisible(true);
                    valor_pag.setVisible(true);
                    juros.setVisible(true);
                    tJuros.setVisible(true);
                    tValor_pag.setVisible(true);
                    fonte.setVisible(true);
                    fonte.setSelectedItem(0);
                    tPegou_mais.setText("");

                    
                       }else
                     if(acao.getSelectedItem().equals("Rolou")){
                    try {
                        tSaldoFals.setText("R$ "+Principal.paraFormatoDinheiro(Double.parseDouble(Principal.formatoParaInserir(tSaldo.getText()))));
                    } catch (ParseException ex) {
                        Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                         tPegou_mais.setVisible(false);
                         pegou_mais.setVisible(false);
                         obs_emp.setVisible(true);
                         tObs_emp.setVisible(true);
                         data_emp.setVisible(true);
                         tData_emp.setVisible(true);
                         data_pag.setVisible(true);
                         tData_pag.setVisible(true);
                         valor_pag.setVisible(false);
                         juros.setVisible(true);
                         tJuros.setVisible(true);
                         tValor_pag.setVisible(false);
                         tValor_pag.setText("");
                         tPegou_mais.setText("");
                         tJuros.setText("");
                         fonte.setVisible(false);
                         fonte.setSelectedIndex(0);
                       
                          
                        }else
                            if(acao.getSelectedItem().equals("Amortizou")){
                    try {
                        tSaldoFals.setText("R$ "+Principal.paraFormatoDinheiro( (Double.parseDouble(Principal.formatoParaInserir(tSaldo.getText())))));
                    } catch (ParseException ex) {
                        Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                                tPegou_mais.setVisible(false);
                                pegou_mais.setVisible(false);
                                obs_emp.setVisible(true);
                                tObs_emp.setVisible(true);
                                data_emp.setVisible(true);
                                tData_emp.setVisible(true);
                                data_pag.setVisible(true);
                                tData_pag.setVisible(true);
                                valor_pag.setVisible(true);
                                juros.setVisible(true);
                                tJuros.setVisible(true);
                                tValor_pag.setVisible(true);
                                tValor_pag.setText("");
                                fonte.setVisible(true);
                                fonte.setSelectedItem(0);

                            }else
                                if(acao.getSelectedItem().equals("Ação")){

                                    tPegou_mais.setVisible(false);
                                    pegou_mais.setVisible(false);
                                    obs_emp.setVisible(true);
                                    tObs_emp.setVisible(true);
                                    fonte.setVisible(false);
                                    fonte.setSelectedIndex(0);
                                    data_emp.setVisible(false);
                                    tData_emp.setVisible(false);
                                    //tData_emp.setText("");
                                    data_pag.setVisible(false);
                                    tData_pag.setVisible(false);
                                    //tData_pag.setText("");
                                    valor_pag.setVisible(false);
                                    juros.setVisible(false);
                                    tJuros.setVisible(false);
                                    //tJuros.setText("");
                                    tValor_pag.setVisible(false);
                                    obs_emp.setVisible(false);
                                    tObs_emp.setVisible(false);

                                }else
                                    if(acao.getSelectedItem().equals("Finalizar")){
                    try {
                        tSaldoFals.setText("R$ "+Principal.paraFormatoDinheiro( (Double.parseDouble(Principal.formatoParaInserir(tSaldo.getText())))));
                    } catch (ParseException ex) {
                        Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                                        tPegou_mais.setVisible(false);
                                        pegou_mais.setVisible(false);
                                        obs_emp.setVisible(true);
                                        tObs_emp.setVisible(true);
                                        juros.setVisible(false);
                                        tJuros.setVisible(false);
                                        tJuros.setText("");
                                        data_emp.setVisible(true);
                                        tData_emp.setVisible(true);
                                        data_pag.setVisible(false);
                                        tData_pag.setVisible(false);
                                        tData_pag.setText("");
                                        valor_pag.setVisible(false);
                                        tValor_pag.setVisible(false);
                                        tValor_pag.setText("");
                                        fonte.setVisible(true);
                                        fonte.setSelectedItem(0);
                                        


                                    }

                    }
            });


        sair.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent e){

                   setVisible(false);
           
               }
                
            });
        
       consultar.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){

                    try{
                        
                        imprimir.setVisible(false);
                        ver_end.setVisible(false);
                        filtrar.setVisible(false);
                        fonte.setVisible(false);
                        acao.setVisible(false);
                        acao.setSelectedIndex(0);
                        fonte.setSelectedIndex(0);
                        tCod.setEditable(false);
                        obs_emp.setVisible(false);
                        tObs_emp.setVisible(false);
                        tObs_emp.setText("");
                        tCod.setText("");
                        tNome.setText("");
                        tEnd_res.setText("");
                        tEnd_trab.setText("");
                        tTel_res.setText("");
                        tTel_res2.setText("");
                        tTel_trab.setText("");
                        tTel_trab2.setText("");
                        tOutros.setText("");
                        tSaldoFals.setText("");
                        tValor_pag.setText("");
                        tData_pag.setText("");
                        tData_emp.setText("");
                        tJuros.setText("");
                        total.setText("");
                        tPegou_mais.setText("");
                        scroller.setVisible(false);
                        remove(scroller);
                      }catch(Exception ex){
                      
                        imprimir.setVisible(false);
                        ver_end.setVisible(false);
                        filtrar.setVisible(false);
                        fonte.setVisible(false);
                        acao.setVisible(false);
                        acao.setSelectedIndex(0);
                        fonte.setSelectedIndex(0);
                        tCod.setEditable(false);
                        obs_emp.setVisible(false);
                        tObs_emp.setVisible(false);
                        tObs_emp.setText("");
                        tCod.setText("");
                        tNome.setText("");
                        tEnd_res.setText("");
                        tEnd_trab.setText("");
                        tTel_res.setText("");
                        tTel_res2.setText("");
                        tTel_trab.setText("");
                        tTel_trab2.setText("");
                        tOutros.setText("");
                        tSaldoFals.setText("");
                        tValor_pag.setText("");
                        tData_pag.setText("");
                        tData_emp.setText("");
                        tJuros.setText("");
                        total.setText("");
                        tPegou_mais.setText("");

                      }

                tCod.setText("");
                tSaldo.setText("");
                tSaldoFals.setText("");
                saldo.setVisible(true);
                re = new RepositorioEmprestimo();
                tabela = re.procurarAbertos();
                
                   if(tabela.getValueAt(0, 0)==null){
                    JOptionPane.showMessageDialog(null, "Registros não encontrados!!!");
                }else{
                scroller = new JScrollPane(tabela, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	            add(scroller);
	            scroller.setBounds(20, 350,1000, 140);
                double somaDoub = re.somarProcurarAbertos();
                String valor = Principal.paraFormatoDinheiro(somaDoub);
                total.setText("R$ "+valor);
                total.setVisible(false);

                tCod.setEditable(true);
               // filtrar.setVisible(true);


                }
            }
        	
        	}
        );
        saldo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(total.isVisible()==true){
                    total.setVisible(false);
                }else{
                    total.setVisible(true);
                }
                }
            });
       /* finalizar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                if(tSaldo.getText().equals("")||tSaldoFals.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Selecione um Empréstimo Válido!!!");
                }else{
                String pagamento = null;
            	if(fonte.getSelectedItem().toString().equals("")){
                    JOptionPane.showMessageDialog(null, "Escolha a forma de Pagamento");
                }else{
                int resposta;
                resposta=JOptionPane.showConfirmDialog(null,"Deseja realmente finalizar?","Confirmação",JOptionPane.YES_NO_OPTION);
                if(resposta==0){
                    if(fonte.getSelectedItem().toString().equals("Banco do Brasil")){
                        pagamento = "Depositou no Banco do Brasil";
                        rco = new RepositorioConta();
                        rco.depositar(Integer.parseInt(tSaldo.getText()),"BcoBrasil", "Pagamento de "+tNome.getText());
                    }else
                        if(fonte.getSelectedItem().toString().equals("Banco Real")){
                            pagamento = "Depositou no Banco Real";
                            rco = new RepositorioConta();
                            rco.depositar(Integer.parseInt(tSaldo.getText()),"BcoReal", "Pagamento de "+tNome.getText());
                        }else
                            if(fonte.getSelectedItem().toString().equals("Dinheiro")){
                                 pagamento = "Depositou no Dinheiro";
                                 rco = new RepositorioConta();
                                 rco.depositar(Integer.parseInt(tSaldo.getText()),"Dinheiro", "Pagamento de "+tNome.getText());
                                 }
                    re = new RepositorioEmprestimo();
                    re.finalizar(tCod.getText(), tNome.getText(), Integer.parseInt(tSaldo.getText()),pagamento);

                     Controle ctrl = new Controle();
                     ctrl.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                     setVisible(false);
                    try{
                                        ver_end.setVisible(false);
                                        filtrar.setVisible(false);
                                        tCod.setEditable(false);
                                        tCod.setText("");
                                        tNome.setText("");
                                        tEnd_res.setText("");
                                        tEnd_trab.setText("");
                                        tTel_res.setText("");
                                        tTel_res2.setText("");
                                        tTel_trab.setText("");
                                        tTel_trab2.setText("");
                                        tOutros.setText("");
                                        tSaldoFals.setText("");
                                        tValor_pag.setText("");
                                        tData_pag.setText("");
                                        tData_emp.setText("");
                                        tJuros.setText("");
                                        acao.setSelectedIndex(0);
                                        fonte.setSelectedItem(0);
                                        total.setText("");
                                        scroller.setVisible(false);
                                        tela.remove(scroller);
                                      }catch(Exception ex){
                                        ver_end.setVisible(false);
                                        filtrar.setVisible(false);
                                        tCod.setEditable(false);
                                        tCod.setText("");
                                        tNome.setText("");
                                        tEnd_res.setText("");
                                        tEnd_trab.setText("");
                                        tTel_res.setText("");
                                        tTel_res2.setText("");
                                        tTel_trab.setText("");
                                        tTel_trab2.setText("");
                                        tOutros.setText("");
                                        tSaldoFals.setText("");
                                        tValor_pag.setText("");
                                        tData_pag.setText("");
                                        tData_emp.setText("");
                                        tJuros.setText("");
                                        acao.setSelectedIndex(0);
                                        fonte.setSelectedItem(0);
                                        total.setText("");
                                      }

                                 }
                            }
                     }
              }
                });*/
        
        renovar.addActionListener(new ActionListener(){
        	
        	public void actionPerformed(ActionEvent e){
                if(tCod.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Campo código vazio!!!\nSelecione um na lista dos Empréstimos Abertos");
                }else{

                    if(tSaldo.getText().equals("")||tSaldoFals.getText().equals("")){
                        JOptionPane.showMessageDialog(null,"Selecione um Empréstimo Válido!!");
                    }else{
                String pagamento = null;
               if(acao.getSelectedItem().equals("Pegou mais")&&fonte.getSelectedItem().equals("Financeiro")||acao.getSelectedItem().equals("Amortizou")&&fonte.getSelectedItem().equals("Financeiro")||acao.getSelectedItem().equals("Finalizar")&&fonte.getSelectedItem().equals("Financeiro")){
                   JOptionPane.showMessageDialog(null,"Escolha uma forma de Pagamento!!");
               }else{

        		int resposta;
                resposta=JOptionPane.showConfirmDialog(null,"Deseja realmente gravar essa ação?","Confirmação",JOptionPane.YES_NO_OPTION);
               if(resposta==0){
                   rc = new RepositorioCliente();
                   Cliente cli = rc.verificarExistente(tNome.getText());
                   if(acao.getSelectedItem().toString().equals("Pegou mais")){
                       if(tData_emp.getText().equals("  /  /    ")|| tData_pag.getText().equals("  /  /    ")){
                        JOptionPane.showMessageDialog(null,"Preencha os campos de data!!!");
                    }else{
                        if(fonte.getSelectedItem().toString().equals("Marquinho")){
                        pagamento = "Marquinho - "+tObs_emp.getText()+"";
                        rco = new RepositorioConta();
                            try {
                                rco.emprestar("Emprestar",Principal.formatoParaInserir(tPegou_mais.getText()),"BcoBrasil", "Emprestar para "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                rco.receber("Receber", Principal.formatoParaInserir(tValor_pag.getText()), "BcoBrasil", "Receber de "+cli.getCod_cliente()+" - "+tNome.getText(), tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            }else
                                     if(fonte.getSelectedItem().toString().equals("Santander")){
                                      pagamento = "Santander - "+tObs_emp.getText()+"";
                                      rco = new RepositorioConta();
                            try {
                                rco.emprestar("Emprestar",Principal.formatoParaInserir(tPegou_mais.getText()),"BcoReal",  "Emprestar para "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                rco.receber("Receber", Principal.formatoParaInserir(tValor_pag.getText()), "BcoReal", "Receber de "+cli.getCod_cliente()+" - "+tNome.getText(), tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                     }else
                                        if(fonte.getSelectedItem().toString().equals("Dinheiro")){
                                             pagamento ="Dinheiro - "+tObs_emp.getText()+"";
                                             rco = new RepositorioConta();
                            try {
                                rco.emprestar("Emprestar",Principal.formatoParaInserir(tPegou_mais.getText()),"Dinheiro",  "Emprestar para "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                rco.receber("Receber", Principal.formatoParaInserir(tValor_pag.getText()), "Dinheiro", "Receber de "+cli.getCod_cliente()+" - "+tNome.getText(), tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                        }else
                                            if(fonte.getSelectedItem().toString().equals("Priscila")){
                                                 pagamento ="Priscila - "+tObs_emp.getText()+"";
                                                 rco = new RepositorioConta();
                            try {
                                rco.emprestar("Emprestar",Principal.formatoParaInserir(tPegou_mais.getText()),"Pris",  "Emprestar para "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                rco.receber("Receber", Principal.formatoParaInserir(tValor_pag.getText()), "Pris", "Receber de "+cli.getCod_cliente()+" - "+tNome.getText(), tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                            }else
                                                if(fonte.getSelectedItem().toString().equals("LocaSul")){
                                                     pagamento ="LocaSul - "+tObs_emp.getText()+"";
                                                     rco = new RepositorioConta();
                            try {
                                rco.emprestar("Emprestar",Principal.formatoParaInserir(tPegou_mais.getText()),"Locasul",  "Emprestar para "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                rco.receber("Receber", Principal.formatoParaInserir(tValor_pag.getText()), "Locasul", "Receber de "+cli.getCod_cliente()+" - "+tNome.getText(), tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                                }else
                                                    if(fonte.getSelectedItem().toString().equals("Bradesco")){
                                                         pagamento ="Bradesco - "+tObs_emp.getText()+"";
                                                         rco = new RepositorioConta();
                            try {
                                rco.emprestar("Emprestar",Principal.formatoParaInserir(tPegou_mais.getText()),"BcoBrad",  "Emprestar para "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                rco.receber("Receber", Principal.formatoParaInserir(tValor_pag.getText()), "BcoBrad", "Receber de "+cli.getCod_cliente()+" - "+tNome.getText(), tData_emp.getText());
                            } catch (ParseException ex) {
                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                            }
                                                    }
                       
                       re = new RepositorioEmprestimo();
                       Emprestimo emp = re.paraAlterar(tCod.getText());
                       double volta = 0;
                           try {
                               volta = re.renovar(tCod.getText(), tNome.getText(), Principal.formatoParaInserir(tSaldo.getText()), Principal.formatoParaInserir(tPegou_mais.getText()),Principal.formatoParaInserir(tValor_pag.getText()) ,tData_emp.getText(), tData_pag.getText(),Principal.formatoParaInserir(tJuros.getText()), "Aberto - Renov",pagamento, emp.getGarantia());
                           } catch (ParseException ex) {
                               Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                           }
                       JOptionPane.showMessageDialog(null,"Novo saldo: R$ "+Principal.paraFormatoDinheiro(volta));
                     
                       /* Controle ctrl = new Controle();
                        ctrl.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        setVisible(false);*/
                                 try{

                                        tPegou_mais.setVisible(false);
                                        pegou_mais.setVisible(false);
                                        ver_end.setVisible(false);
                                        filtrar.setVisible(false);
                                        fonte.setVisible(false);
                                        acao.setVisible(false);
                                        acao.setSelectedIndex(0);
                                        fonte.setSelectedIndex(0);
                                        tCod.setEditable(false);
                                        obs_emp.setVisible(false);
                                        tObs_emp.setVisible(false);
                                        tObs_emp.setText("");
                                        tCod.setText("");
                                        tNome.setText("");
                                        tEnd_res.setText("");
                                        tEnd_trab.setText("");
                                        tTel_res.setText("");
                                        tTel_res2.setText("");
                                        tTel_trab.setText("");
                                        tTel_trab2.setText("");
                                        tOutros.setText("");
                                        tSaldoFals.setText("");
                                        tValor_pag.setText("");
                                        tData_pag.setText("");
                                        tData_emp.setText("");
                                        tJuros.setText("");
                                        total.setText("");
                                        tPegou_mais.setText("");
                                        scroller.setVisible(false);
                                        remove(scroller);
                                      }catch(Exception ex){
                                       
                                        tPegou_mais.setVisible(false);
                                        pegou_mais.setVisible(false);
                                        ver_end.setVisible(false);
                                        filtrar.setVisible(false);
                                        fonte.setVisible(false);
                                        acao.setVisible(false);
                                        acao.setSelectedIndex(0);
                                        fonte.setSelectedIndex(0);
                                        tCod.setEditable(false);
                                        obs_emp.setVisible(false);
                                        tObs_emp.setVisible(false);
                                        tObs_emp.setText("");
                                        tCod.setText("");
                                        tNome.setText("");
                                        tEnd_res.setText("");
                                        tEnd_trab.setText("");
                                        tTel_res.setText("");
                                        tTel_res2.setText("");
                                        tTel_trab.setText("");
                                        tTel_trab2.setText("");
                                        tOutros.setText("");
                                        tSaldoFals.setText("");
                                        tValor_pag.setText("");
                                        tData_pag.setText("");
                                        tData_emp.setText("");
                                        tJuros.setText("");
                                        total.setText("");
                                        tPegou_mais.setText("");
                                        }
                                      }

                   }else
                       if(acao.getSelectedItem().toString().equals("Rolou")){
                           if(tData_emp.getText().equals("  /  /    ")|| tData_pag.getText().equals("  /  /    ")){
                        JOptionPane.showMessageDialog(null,"Preencha os campos de data!!!");
                    }else{

                       tValor_pag.setText("0");
                       re = new RepositorioEmprestimo();
                       Emprestimo emp = re.paraAlterar(tCod.getText());
                       double volta = 0;
                               try {
                                   volta = re.renovar(tCod.getText(), tNome.getText(), Principal.formatoParaInserir(tSaldo.getText()),"0","0",tData_emp.getText(), tData_pag.getText(),Principal.formatoParaInserir(tJuros.getText()), "Aberto - Juros","Juros "+tObs_emp.getText(),emp.getGarantia());
                               } catch (ParseException ex) {
                                   Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                               }
                       
                       JOptionPane.showMessageDialog(null,"Novo saldo: R$ "+Principal.paraFormatoDinheiro(volta));
                        /* Controle ctrl = new Controle();
                         ctrl.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                         setVisible(false);*/
                             try{
                                   
                                    tPegou_mais.setVisible(false);
                                    pegou_mais.setVisible(false);
                                    ver_end.setVisible(false);
                                    filtrar.setVisible(false);
                                    fonte.setVisible(false);
                                    acao.setVisible(false);
                                    acao.setSelectedIndex(0);
                                    fonte.setSelectedIndex(0);
                                    tCod.setEditable(false);
                                    obs_emp.setVisible(false);
                                    tObs_emp.setVisible(false);
                                    tObs_emp.setText("");
                                    tCod.setText("");
                                    tNome.setText("");
                                    tEnd_res.setText("");
                                    tEnd_trab.setText("");
                                    tTel_res.setText("");
                                    tTel_res2.setText("");
                                    tTel_trab.setText("");
                                    tTel_trab2.setText("");
                                    tOutros.setText("");
                                    tSaldoFals.setText("");
                                    tValor_pag.setText("");
                                    tData_pag.setText("");
                                    tData_emp.setText("");
                                    tJuros.setText("");
                                    total.setText("");
                                    tPegou_mais.setText("");
                                    scroller.setVisible(false);
                                    remove(scroller);
                                  }catch(Exception ex){

                                    tPegou_mais.setVisible(false);
                                    pegou_mais.setVisible(false);
                                    ver_end.setVisible(false);
                                    filtrar.setVisible(false);
                                    fonte.setVisible(false);
                                    acao.setVisible(false);
                                    acao.setSelectedIndex(0);
                                    fonte.setSelectedIndex(0);
                                    tCod.setEditable(false);
                                    obs_emp.setVisible(false);
                                    tObs_emp.setVisible(false);
                                    tObs_emp.setText("");
                                    tCod.setText("");
                                    tNome.setText("");
                                    tEnd_res.setText("");
                                    tEnd_trab.setText("");
                                    tTel_res.setText("");
                                    tTel_res2.setText("");
                                    tTel_trab.setText("");
                                    tTel_trab2.setText("");
                                    tOutros.setText("");
                                    tSaldoFals.setText("");
                                    tValor_pag.setText("");
                                    tData_pag.setText("");
                                    tData_emp.setText("");
                                    tJuros.setText("");
                                    total.setText("");
                                    tPegou_mais.setText("");

                                  }
                            }
                       }else
                           if(acao.getSelectedItem().toString().equals("Finalizar")){
                                if(tData_emp.getText().equals("  /  /    ")){
                                    JOptionPane.showMessageDialog(null,"Preencha os campos de data!!!");
                                 }else{
                               
                        resposta=JOptionPane.showConfirmDialog(null,"Deseja realmente finalizar?","Confirmação",JOptionPane.YES_NO_OPTION);
                        if(resposta==0){
                            if(fonte.getSelectedItem().toString().equals("Marquinho")){
                                pagamento = "Marquinho - "+tObs_emp.getText()+"";
                                rco = new RepositorioConta();
                                try {
                                    rco.receber("Receber",Principal.formatoParaInserir(tSaldo.getText()),"BcoBrasil", "Pagamento de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                } catch (ParseException ex) {
                                    Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }else
                                if(fonte.getSelectedItem().toString().equals("Santander")){
                                    pagamento = "Santander - "+tObs_emp.getText()+"";
                                    rco = new RepositorioConta();
                                try {
                                    rco.receber("Receber",Principal.formatoParaInserir(tSaldo.getText()),"BcoReal", "Pagamento de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                } catch (ParseException ex) {
                                    Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                }else
                                    if(fonte.getSelectedItem().toString().equals("Dinheiro")){
                                         pagamento = "Dinheiro - "+tObs_emp.getText()+"";
                                         rco = new RepositorioConta();
                                try {
                                    rco.receber("Receber",Principal.formatoParaInserir(tSaldo.getText()),"Dinheiro", "Pagamento de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                } catch (ParseException ex) {
                                    Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                         }else
                                            if(fonte.getSelectedItem().toString().equals("Priscila")){
                                                 pagamento = "Priscila - "+tObs_emp.getText()+"";
                                                 rco = new RepositorioConta();
                                try {
                                    rco.receber("Receber",Principal.formatoParaInserir(tSaldo.getText()),"Pris", "Pagamento de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                } catch (ParseException ex) {
                                    Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                                 }else
                                                    if(fonte.getSelectedItem().toString().equals("Bradesco")){
                                                         pagamento = "Bradesco - "+tObs_emp.getText()+"";
                                                         rco = new RepositorioConta();
                                try {
                                    rco.receber("Receber",Principal.formatoParaInserir(tSaldo.getText()),"BcoBrad", "Pagamento de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                } catch (ParseException ex) {
                                    Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                                         }else
                                                            if(fonte.getSelectedItem().toString().equals("LocaSul")){
                                                                 pagamento = "LocaSul - "+tObs_emp.getText()+"";
                                                                 rco = new RepositorioConta();
                                try {
                                    rco.receber("Receber",Principal.formatoParaInserir(tSaldo.getText()),"Locasul", "Pagamento de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                } catch (ParseException ex) {
                                    Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                                                 }
                            re = new RepositorioEmprestimo();
                            Emprestimo emp = re.paraAlterar(tCod.getText());
                                         try {
                                             re.finalizar(tCod.getText(), tNome.getText(), Principal.formatoParaInserir(tSaldo.getText()),tData_emp.getText(),pagamento,emp.getGarantia());
                                         } catch (ParseException ex) {
                                             Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                         }


                           /* Controle ctrl = new Controle();
                             ctrl.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                             setVisible(false);*/
                            try{
                                    tPegou_mais.setVisible(false);
                                    pegou_mais.setVisible(false);
                                    ver_end.setVisible(false);
                                    filtrar.setVisible(false);
                                    fonte.setVisible(false);
                                    acao.setVisible(false);
                                    acao.setSelectedIndex(0);
                                    fonte.setSelectedIndex(0);
                                    tCod.setEditable(false);
                                    obs_emp.setVisible(false);
                                    tObs_emp.setVisible(false);
                                    tObs_emp.setText("");
                                    tCod.setText("");
                                    tNome.setText("");
                                    tEnd_res.setText("");
                                    tEnd_trab.setText("");
                                    tTel_res.setText("");
                                    tTel_res2.setText("");
                                    tTel_trab.setText("");
                                    tTel_trab2.setText("");
                                    tOutros.setText("");
                                    tSaldoFals.setText("");
                                    tValor_pag.setText("");
                                    tData_pag.setText("");
                                    tData_emp.setText("");
                                    tJuros.setText("");
                                    total.setText("");
                                    tPegou_mais.setText("");
                                    scroller.setVisible(false);
                                    remove(scroller);
                                  }catch(Exception ex){
                                    
                                    tPegou_mais.setVisible(false);
                                    pegou_mais.setVisible(false);
                                    ver_end.setVisible(false);
                                    filtrar.setVisible(false);
                                    fonte.setVisible(false);
                                    acao.setVisible(false);
                                    acao.setSelectedIndex(0);
                                    fonte.setSelectedIndex(0);
                                    tCod.setEditable(false);
                                    obs_emp.setVisible(false);
                                    tObs_emp.setVisible(false);
                                    tObs_emp.setText("");
                                    tCod.setText("");
                                    tNome.setText("");
                                    tEnd_res.setText("");
                                    tEnd_trab.setText("");
                                    tTel_res.setText("");
                                    tTel_res2.setText("");
                                    tTel_trab.setText("");
                                    tTel_trab2.setText("");
                                    tOutros.setText("");
                                    tSaldoFals.setText("");
                                    tValor_pag.setText("");
                                    tData_pag.setText("");
                                    tData_emp.setText("");
                                    tJuros.setText("");
                                    total.setText("");
                                    tPegou_mais.setText("");
                                  }
                             }
                        }

                           }else

                                    if(acao.getSelectedItem().toString().equals("Amortizou")){
                                        if(tData_emp.getText().equals("  /  /    ")|| tData_pag.getText().equals("  /  /    ")){
                                                 JOptionPane.showMessageDialog(null,"Preencha os campos de data!!!");
                                                  }else{
                                        if(fonte.getSelectedItem().toString().equals("Marquinho")){
                                              pagamento = "Marquinho - "+tObs_emp.getText()+"";
                                              rco = new RepositorioConta();
                                              String valor = null;
                                                          try {
                                                              valor = Principal.formatoParaInserir(tValor_pag.getText());
                                                          } catch (ParseException ex) {
                                                              Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                                          }
                                              rco.receber("Receber",valor,"BcoBrasil", "Amortização de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                         }else
                                               if(fonte.getSelectedItem().toString().equals("Santander")){
                                                pagamento = "Santander - "+tObs_emp.getText()+"";
                                                rco = new RepositorioConta();
                                                String valor = null;
                                                          try {
                                                              valor = Principal.formatoParaInserir(tValor_pag.getText());
                                                          } catch (ParseException ex) {
                                                              Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                                          }
                                                rco.receber("Receber",valor,"BcoReal",  "Amortização de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                            }else
                                                 if(fonte.getSelectedItem().toString().equals("Dinheiro")){
                                                     pagamento = "Dinheiro - "+tObs_emp.getText()+"";
                                                     rco = new RepositorioConta();
                                                     String valor = null;
                                                          try {
                                                              valor = Principal.formatoParaInserir(tValor_pag.getText());
                                                          } catch (ParseException ex) {
                                                              Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                                          }
                                                     rco.receber("Receber",valor,"Dinheiro",  "Amortização de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                                    }else
                                                         if(fonte.getSelectedItem().toString().equals("Bradesco")){
                                                             pagamento = "Bradesco - "+tObs_emp.getText()+"";
                                                             rco = new RepositorioConta();
                                                             String valor = null;
                                                          try {
                                                              valor = Principal.formatoParaInserir(tValor_pag.getText());
                                                          } catch (ParseException ex) {
                                                              Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                                          }
                                                             rco.receber("Receber",valor,"BcoBrad",  "Amortização de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                                            }else
                                                                 if(fonte.getSelectedItem().toString().equals("Priscila")){
                                                                     pagamento = "Priscila - "+tObs_emp.getText()+"";
                                                                     rco = new RepositorioConta();
                                                                     String valor = null;
                                                          try {
                                                              valor = Principal.formatoParaInserir(tValor_pag.getText());
                                                          } catch (ParseException ex) {
                                                              Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                                          }
                                                                     rco.receber("Receber",valor,"Pris",  "Amortização de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                                                    }else
                                                                         if(fonte.getSelectedItem().toString().equals("LocaSul")){
                                                                             pagamento = "LocaSul - "+tObs_emp.getText()+"";
                                                                             rco = new RepositorioConta();
                                                                             String valor = null;
                                                          try {
                                                              valor = Principal.formatoParaInserir(tValor_pag.getText());
                                                          } catch (ParseException ex) {
                                                              Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                                          }
                                                                             rco.receber("Receber",valor,"Locasul",  "Amortização de "+cli.getCod_cliente()+" - "+tNome.getText(),tData_emp.getText());
                                                                            }

                                       re = new RepositorioEmprestimo();
                                       Emprestimo emp = re.paraAlterar(tCod.getText());
                                       double volta = 0;
                                            try {
                                                volta = re.renovar(tCod.getText(), tNome.getText(), Principal.formatoParaInserir(tSaldo.getText()),"0",Principal.formatoParaInserir(tValor_pag.getText()),tData_emp.getText(), tData_pag.getText(),Principal.formatoParaInserir(tJuros.getText()) , "Aberto - Pagou",pagamento,emp.getGarantia());
                                            } catch (ParseException ex) {
                                                Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                                            }

                                     
                                       JOptionPane.showMessageDialog(null,"Novo saldo: R$ "+Principal.paraFormatoDinheiro(volta));
                                       /* Controle ctrl = new Controle();
                                        ctrl.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                                        setVisible(false);*/

                                        try{
                                                tPegou_mais.setVisible(false);
                                                pegou_mais.setVisible(false);
                                                ver_end.setVisible(false);
                                                filtrar.setVisible(false);
                                                fonte.setVisible(false);
                                                acao.setVisible(false);
                                                acao.setSelectedIndex(0);
                                                fonte.setSelectedIndex(0);
                                                tCod.setEditable(false);
                                                obs_emp.setVisible(false);
                                                tObs_emp.setVisible(false);
                                                tObs_emp.setText("");
                                                tCod.setText("");
                                                tNome.setText("");
                                                tEnd_res.setText("");
                                                tEnd_trab.setText("");
                                                tTel_res.setText("");
                                                tTel_res2.setText("");
                                                tTel_trab.setText("");
                                                tTel_trab2.setText("");
                                                tOutros.setText("");
                                                tSaldoFals.setText("");
                                                tValor_pag.setText("");
                                                tData_pag.setText("");
                                                tData_emp.setText("");
                                                tJuros.setText("");
                                                total.setText("");
                                                tPegou_mais.setText("");
                                                scroller.setVisible(false);
                                                remove(scroller);
                                              }catch(Exception ex){

                                                tPegou_mais.setVisible(false);
                                                pegou_mais.setVisible(false);
                                                ver_end.setVisible(false);
                                                filtrar.setVisible(false);
                                                fonte.setVisible(false);
                                                acao.setVisible(false);
                                                acao.setSelectedIndex(0);
                                                fonte.setSelectedIndex(0);
                                                tCod.setEditable(false);
                                                obs_emp.setVisible(false);
                                                tObs_emp.setVisible(false);
                                                tObs_emp.setText("");
                                                tCod.setText("");
                                                tNome.setText("");
                                                tEnd_res.setText("");
                                                tEnd_trab.setText("");
                                                tTel_res.setText("");
                                                tTel_res2.setText("");
                                                tTel_trab.setText("");
                                                tTel_trab2.setText("");
                                                tOutros.setText("");
                                                tSaldoFals.setText("");
                                                tValor_pag.setText("");
                                                tData_pag.setText("");
                                                tData_emp.setText("");
                                                tJuros.setText("");
                                                total.setText("");
                                                tPegou_mais.setText("");

                                         }
                                                  }
                           }else
                       JOptionPane.showMessageDialog(null, "Escolha uma operação!!!");
                           

                             }
        	          
                        }
                    }
                    
                }
            }
        });
        	
        limpar.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){

               /* Controle ctrl = new Controle();
                ctrl.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                setVisible(false);*/
              try{
                  
                imprimir.setVisible(false);
                tPegou_mais.setVisible(false);
                pegou_mais.setVisible(false);
                saldo.setVisible(false);
                ver_end.setVisible(false);
                filtrar.setVisible(false);
                fonte.setVisible(false);
                acao.setVisible(false);
                acao.setSelectedIndex(0);
                fonte.setSelectedIndex(0);
                tCod.setEditable(false);
                obs_emp.setVisible(false);
                tObs_emp.setVisible(false);
                tObs_emp.setText("");
                tCod.setText("");
                tNome.setText("");
                tEnd_res.setText("");
                tEnd_trab.setText("");
                tTel_res.setText("");
                tTel_res2.setText("");
                tTel_trab.setText("");
                tTel_trab2.setText("");
                tOutros.setText("");
                tSaldoFals.setText("");
                tValor_pag.setText("");
                tData_pag.setText("");
                tData_emp.setText("");
                tJuros.setText("");
                total.setText("");
                tPegou_mais.setText("");
                scroller.setVisible(false);
                remove(scroller);
              }catch(Exception ex){

                imprimir.setVisible(false);
                tPegou_mais.setVisible(false);
                pegou_mais.setVisible(false);
                saldo.setVisible(false);
                ver_end.setVisible(false);
                filtrar.setVisible(false);
                fonte.setVisible(false);
                acao.setVisible(false);
                acao.setSelectedIndex(0);
                fonte.setSelectedIndex(0);
                tCod.setEditable(false);
                obs_emp.setVisible(false);
                tObs_emp.setVisible(false);
                tObs_emp.setText("");
                tCod.setText("");
                tNome.setText("");
                tEnd_res.setText("");
                tEnd_trab.setText("");
                tTel_res.setText("");
                tTel_res2.setText("");
                tTel_trab.setText("");
                tTel_trab2.setText("");
                tOutros.setText("");
                tSaldoFals.setText("");
                tValor_pag.setText("");
                tData_pag.setText("");
                tData_emp.setText("");
                tJuros.setText("");
                total.setText("");
                tPegou_mais.setText("");

              }
       		
        	}
        });
        
        ver_end.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
                
                rc = new RepositorioCliente();
                re = new RepositorioEmprestimo();
                Emprestimo emp = re.paraAlterar(tCod.getText());
                Cliente cli = rc.procurarNome(emp.getNome());
                tEnd_res.setText(cli.getEnd_res());
                tEnd_trab.setText(cli.getEnd_trab());
                tTel_res.setText(cli.getTel_res());
                tTel_res2.setText(cli.getTel_res2());
                tTel_trab.setText(cli.getTel_trab());
                tTel_trab2.setText(cli.getTel_trab2());
                tOutros.setText(cli.getOutros());
        		
        		
        	}
        });

        tPegou_mais.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            if(tPegou_mais.WHEN_FOCUSED==0){
                double somar = 0;
                    try {
                        somar = Double.parseDouble(Principal.formatoParaInserir(tPegou_mais.getText()));
                    } catch (ParseException ex) {
                        Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                String saldoTexto = tSaldoFals.getText();
                double saldo = 0;
                    try {
                        saldo = Double.parseDouble(Principal.formatoParaInserir(saldoTexto.substring(3,saldoTexto.length())));
                    } catch (ParseException ex) {
                        Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                double novoSaldoDoub = saldo + somar;
                


                tSaldoFals.setText("R$ "+Principal.paraFormatoDinheiro(novoSaldoDoub));

            }

                           }
                  });
                  
           tValor_pag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            if(tValor_pag.WHEN_FOCUSED==0){
                double tirar = 0;
                    try {
                        tirar = Double.parseDouble(Principal.formatoParaInserir(tValor_pag.getText()));
                    } catch (ParseException ex) {
                        Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                String saldoTexto = tSaldoFals.getText();
                double saldo = 0;
                    try {
                        saldo = Double.parseDouble(Principal.formatoParaInserir(saldoTexto.substring(3,saldoTexto.length())));
                    } catch (ParseException ex) {
                        Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                double novoSaldoDoub = saldo - tirar;
                


                tSaldoFals.setText("R$ "+Principal.paraFormatoDinheiro(novoSaldoDoub));

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
                String query = "SELECT cod_emp, nome,data_emp, data_pag,garantia,status,valor,juros,pagou, valor_rec FROM Emprestimos WHERE cod_emp = ("+tCod.getText()+")ORDER BY op desc";
		ResultSet rs = st.executeQuery( query );
                JRResultSetDataSource jrRS = new JRResultSetDataSource( rs );
                String arquivoJasper = "relatorio/emprestimos.jasper";
                rel = JasperFillManager.fillReport(arquivoJasper, map, jrRS);
                JasperViewer.viewReport(rel, false);
                con.close();

        			}catch(Exception t){
        				System.out.println(t.getMessage());
      			}
               
           }});
        	
       tCod.addActionListener(new ActionListener() {
          private JTable tabelaAux;

            public void actionPerformed(ActionEvent e) {
                if(tCod.WHEN_FOCUSED==0){

                if(tCod.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Campo código vazio!!!");
                }else{
                    re = new RepositorioEmprestimo();
                    tabelaAux = re.procurar(tCod.getText(),"Abertos");
                    if(tabelaAux.getValueAt(0,0)==null){
                        JOptionPane.showMessageDialog(null,"Código não associado a nenhum emprestimo aberto\n Procure nos finalizados");
                        tCod.setText("");
                    }else{
                        
                imprimir.setVisible(true);
                saldo.setVisible(false);
                tCod.setEditable(false);
                ver_end.setVisible(true);
                total.setVisible(false);
                acao.setVisible(true);
                rc = new RepositorioCliente();
                
                try{
                remove(scroller);
                }catch(Exception ex){};
                tabela = re.procurar(tCod.getText(),"Emprestimos");
	            scroller = new JScrollPane(tabela, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	            add(scroller);
	            scroller.setBounds(20, 350,1000, 140);
                String cod = tabela.getValueAt(0,0).toString();
                
                Double saldo = null;
                        try {
                            saldo = (Double.parseDouble(Principal.formatoParaInserir(tabela.getValueAt(0, 9).toString())));
                        } catch (ParseException ex) {
                            Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
                        }
                String valDin = Principal.paraFormatoDinheiro(saldo);
                tSaldoFals.setText("R$ "+valDin);

                tSaldo.setText(tabela.getValueAt(0,9).toString());
                tNome.setText(rc.procurar(cod).getNome());
                total.setText("");
                
                    }
                 }
                }
            }
            

        });


            setLayout(null);
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createBevelBorder(1, Color.DARK_GRAY, Color.DARK_GRAY));
            setSize(600,400);
            setVisible(true);
            revalidate();
            repaint();
    }
      

   
}