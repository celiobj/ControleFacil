	/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Classes.Cliente;
import Classes.Emprestimo;
import Persistencia.RepositorioCliente;
import Persistencia.RepositorioConta;
import Persistencia.RepositorioEmprestimo;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;


/**
 *
 * @author celio
 */
public class RealizarEmprestimo extends JPanel {

    JComboBox fonte;
    String contas[]={"","LocaSul","Priscila","Marquinho", "Santander", "Bradesco","Dinheiro"};
    JButton gravar, limpar, sair;
    JLabel nome, end_res,end_trab,valor, data_emp,data_dia, data_pag, juros, garantia,tel_res,tel_res2,tel_trab,tel_trab2, obs, pegouDe, obsEmp;
    JTextField tNome, tValor,tEnd_res,tEnd_trab, tJuros, tGarantia, tObs, tObsEmp;
    JFormattedTextField tData_pag,tData_emp,tTel_res,tTel_res2,tTel_trab,tTel_trab2;
    MaskFormatter mascaraData_emp,mascaraData_pag, mascaraTelefone,mascaraTelefone2,mascaraTelefone3,mascaraTelefone4;
    Calendar data;
    int dia, mes, ano;
    RepositorioEmprestimo re;
    RepositorioCliente rc;
    RepositorioConta rco;
  
    
        public RealizarEmprestimo() {
            
            
            JLabel tela = new JLabel("... :::   Realizar Empréstimo   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
            
            gravar = new JButton("|Gravar|");
            limpar = new JButton("|Limpar|");
            sair = new JButton("|Sair|");
            nome = new JLabel("Nome: ");
            end_res = new JLabel("End Res: ");
            tel_res = new JLabel("Tel: ");
            tel_res2 = new JLabel("Tel-2: ");
            end_trab = new JLabel("End Trab: ");
            tel_trab = new JLabel("Tel-3: ");
            tel_trab2 = new JLabel("Tel-4: ");
            valor = new JLabel("Valor: ");
            data_dia = new JLabel("");
            data_emp = new JLabel("Data : ");
            data_pag = new JLabel("Bom P/ : ");
            juros = new JLabel("Juros: ");
            garantia = new JLabel("Garantia: ");
            obs = new JLabel("Observações: ");
            pegouDe = new JLabel("Pegou de: ");
            obsEmp = new JLabel("Observação: ");
            tObsEmp = new JTextField(50);
            fonte = new JComboBox(contas);
            tObs = new JTextField(20);
            tNome = new JTextField(40);
            tEnd_res = new JTextField(200);
            tEnd_trab = new JTextField(200);
            tValor = new JTextField(5);
            tJuros = new JTextField(6);
            tGarantia = new JTextField(30);
            
            sair.setMnemonic(KeyEvent.VK_R);
            limpar.setMnemonic(KeyEvent.VK_L);
            gravar.setMnemonic(KeyEvent.VK_G);
                      
            tNome.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
            tEnd_res.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
            tEnd_trab.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
            tValor.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
            tJuros.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
            tGarantia.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
            tObs.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
            
           try{
        	mascaraData_pag = new MaskFormatter("##/##/####");
                mascaraData_emp = new MaskFormatter("##/##/####");
                mascaraTelefone = new MaskFormatter("####-####");
                mascaraTelefone2 = new MaskFormatter("####-####");
                mascaraTelefone3 = new MaskFormatter("####-####");
                mascaraTelefone4 = new MaskFormatter("####-####");
                
           }catch (Exception exp){};
                    tData_pag = new JFormattedTextField(mascaraData_pag);
                    tData_emp = new JFormattedTextField(mascaraData_emp);
                    tTel_res = new JFormattedTextField(mascaraTelefone);
                    tTel_res2 = new JFormattedTextField(mascaraTelefone2);
                    tTel_trab = new JFormattedTextField(mascaraTelefone3);
                    tTel_trab2 = new JFormattedTextField(mascaraTelefone4);
             
             tTel_res.setFont(new Font("Times New Roman", Font.BOLD, 14));  
             tTel_res2.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
             tTel_trab.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
             tTel_trab2.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
             tData_pag.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
             tData_emp.setFont(new Font("Times New Roman", Font.BOLD, 14)); 
             tObsEmp.setFont(new Font("Times New Roman", Font.BOLD, 14));

            data = Calendar.getInstance();
            dia = data.get(Calendar.DAY_OF_MONTH);
            mes = data.get(Calendar.MONTH);
            ano = data.get(Calendar.YEAR);
            data_dia.setText("Data:  "+dia+"/"+(mes+1)+"/"+ano);
                                    
            gravar.setBounds(20,340,100,20);
            limpar.setBounds(130,340,100,20);
            sair.setBounds(240,340,100,20);
            nome.setBounds(20,100,100,20);
            tNome.setBounds(60,100,340,20);
            end_res.setBounds(20,130,100,20);
            tEnd_res.setBounds(80,130,320,20);
            tel_res.setBounds(420,130,40,20);
            tTel_res.setBounds(460,130,80,20);
            tel_res2.setBounds(560,130,40,20);
            tTel_res2.setBounds(600,130,80,20);
            end_trab.setBounds(20,160,100,20);
            tEnd_trab.setBounds(80,160,320,20);
            tel_trab.setBounds(420,160,40,20);
            tTel_trab.setBounds(460,160,80,20);
            tel_trab2.setBounds(560,160,80,20);
            tTel_trab2.setBounds(600,160,80,20);
            valor.setBounds(20,190,100,20);
            tValor.setBounds(60,190,100,20);
            pegouDe.setBounds(180,190,100,20);
            fonte.setBounds(250,190,130,20);
            obsEmp.setBounds(390,190,100,20);
            tObsEmp.setBounds(465,190,200,20);
            data_dia.setBounds(660,50,100,20);
            data_emp.setBounds(20,220,130,20);
            tData_emp.setBounds(100,220,100,20);
            data_pag.setBounds(20,250,100,20);
            tData_pag.setBounds(100,250,100,20);
            juros.setBounds(20,280,100,20);
            tJuros.setBounds(80,280,100,20);
            garantia.setBounds(20,310,100,20);
            tGarantia.setBounds(80,310,100,20);
            obs.setBounds(200,310,100,20);
            tObs.setBounds(300,310,200,20);
            
            add(end_res);
            add(tEnd_res);
            add(tel_res);
            add(tTel_res);      
            add(tel_res2);
            add(tTel_res2);
            add(end_trab);
            add(tEnd_trab);
            add(tel_trab);
            add(tTel_trab);
            add(tel_trab2);
            add(tTel_trab2);
            add(gravar);
            add(limpar);
            add(sair);
            add(nome);
            add(tNome);
            add(valor);
            add(tValor);
            add(fonte);
            add(pegouDe);
            add(data_dia);
            add(data_emp);
            add(tData_emp);
            add(data_pag);
            add(tData_pag);
            add(juros);
            add(tJuros);
            add(garantia);
            add(tGarantia);
            add(obs);
            add(tObs);
            add(obsEmp);
            add(tObsEmp);

            obsEmp.setVisible(false);
            tObsEmp.setVisible(false);
    
            
            gravar.addActionListener(new ActionListener(){
                public void actionPerformed (ActionEvent e){
                    String pagamento = null;


                    rc = new RepositorioCliente();
                    Cliente cli = rc.verificarExistente(tNome.getText());
                    if(cli.getNome().equals(tNome.getText())){
                        JOptionPane.showMessageDialog(null,"Nome já cadastrado com o código: "+cli.getCod_cliente()+" !!!\nNão é permitido clientes com o mesmo nome\nEscolha outro e confirme o empréstimo");
                         }
                     else{                  

                      if(tNome.getText().equals("")||tValor.getText().equals("")||tData_emp.getText().equals("  /  /    ")||tData_pag.getText().equals("  /  /    ")){
                        JOptionPane.showMessageDialog(null, "Preencha os campos corretamente!! \n\n Os campos: \n     *Nome\n     *Valor\n     *Data\n     *Bom p/\n -Não podem ser vazios!!!");
                    }else{
                          
                if(fonte.getSelectedItem().toString().equals("")){
                      JOptionPane.showMessageDialog(null, "Fonte do Empréstimo não informada, volte e selecione uma opção!!!");
                  }else{
                          re = new RepositorioEmprestimo();
                          long cod = re.gerarCodigo();
 
                          if(fonte.getSelectedItem().toString().equals("Marquinho")){
                        pagamento = "Marquinho - "+tObsEmp.getText()+"";
                        rco = new RepositorioConta();
                        String valor = null;
                          try {
                              valor = Principal.formatoParaInserir(tValor.getText());
                          } catch (ParseException ex) {
                              Logger.getLogger(RealizarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                          }
                        rco.emprestar("Emprestar",valor,"BcoBrasil","Emprestar para "+cod+" - "+tNome.getText(),tData_emp.getText());
                    }else
                        if(fonte.getSelectedItem().toString().equals("Santander")){
                             pagamento = "Santander - "+tObsEmp.getText()+"";
                            rco = new RepositorioConta();
                            String valor = null;
                          try {
                              valor = Principal.formatoParaInserir(tValor.getText());
                          } catch (ParseException ex) {
                              Logger.getLogger(RealizarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                          }
                            rco.emprestar("Emprestar",valor, "BcoReal","Emprestar para "+cod+" - "+tNome.getText(),tData_emp.getText());
                        }else
                            if(fonte.getSelectedItem().toString().equals("Dinheiro")){
                                pagamento = "Dinheiro - "+tObsEmp.getText()+"";
                                rco = new RepositorioConta();
                                String valor = null;
                          try {
                              valor = Principal.formatoParaInserir(tValor.getText());
                          } catch (ParseException ex) {
                              Logger.getLogger(RealizarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                          }
                                rco.emprestar("Emprestar",valor,"Dinheiro","Emprestar para "+cod+" - "+tNome.getText(),tData_emp.getText());
                            }else
                                if(fonte.getSelectedItem().toString().equals("Bradesco")){
                                     pagamento = "Bradesco - "+tObsEmp.getText()+"";
                                    rco = new RepositorioConta();
                                    String valor = null;
                          try {
                              valor = Principal.formatoParaInserir(tValor.getText());
                          } catch (ParseException ex) {
                              Logger.getLogger(RealizarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                          }
                                    rco.emprestar("Emprestar",valor, "BcoBrad","Emprestar para "+cod+" - "+tNome.getText(),tData_emp.getText());
                                }else
                                    if(fonte.getSelectedItem().toString().equals("LocaSul")){
                                         pagamento = "LocaSul - "+tObsEmp.getText()+"";
                                        rco = new RepositorioConta();
                                        String valor = null;
                          try {
                              valor = Principal.formatoParaInserir(tValor.getText());
                          } catch (ParseException ex) {
                              Logger.getLogger(RealizarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                          }
                                        rco.emprestar("Emprestar",valor, "Locasul","Emprestar para "+cod+" - "+tNome.getText(),tData_emp.getText());
                                    }else
                                        if(fonte.getSelectedItem().toString().equals("Priscila")){
                                             pagamento = "Priscila - "+tObsEmp.getText()+"";
                                            rco = new RepositorioConta();
                                            String valor = null;
                          try {
                              valor = Principal.formatoParaInserir(tValor.getText());
                          } catch (ParseException ex) {
                              Logger.getLogger(RealizarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                          }
                                            rco.emprestar("Emprestar",valor, "Pris","Emprestar para "+cod+" - "+tNome.getText(),Principal.paraInserirData(tData_emp.getText()));
                                        }

                               
                    Emprestimo emp = new Emprestimo();
                    String codigo = ""+cod+"";
                    emp.setCod(codigo);
                    emp.setNome(tNome.getText());
                            try {
                                emp.setValor(Principal.formatoParaInserir(tValor.getText()));
                            } catch (ParseException ex) {
                                Logger.getLogger(RealizarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    emp.setPagou("0");
                    emp.setData_emp(Principal.paraInserirData(tData_emp.getText()));
                    emp.setData_pag(Principal.paraInserirData(tData_pag.getText()));
                    if(tJuros.getText().equals("")){
                        emp.setJuros("0");
                    }else{
                          try {
                              emp.setJuros(Principal.formatoParaInserir(tJuros.getText()));
                          } catch (ParseException ex) {
                              Logger.getLogger(RealizarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                          }
                    }
                    emp.setGarantia(tGarantia.getText());
                    emp.setStatus("Aberto");
                    emp.setRenov("0");
                    double valor_rec = Double.parseDouble(emp.getValor()) + Double.parseDouble(emp.getJuros());
                    emp.setValor_rec(String.valueOf(valor_rec));
                    emp.setPagamento(pagamento);
                    re.adicionar(emp);
                    
                     

                    JOptionPane.showMessageDialog(null,"Código do Empréstimo é: "+cod);

                   // Cliente cli = new Cliente();
                    cli.setCod_cliente(""+cod+"");
                    cli.setNome(tNome.getText());
                    cli.setEnd_res(tEnd_res.getText());
                    cli.setEnd_trab(tEnd_trab.getText());
                    cli.setTel_res(tTel_res.getText());
                    cli.setTel_res2(tTel_res2.getText());
                    cli.setTel_trab(tTel_trab.getText());
                    cli.setTel_trab2(tTel_trab2.getText());
                    cli.setOutros(tObs.getText());
                    rc.adcionar(cli);

                   

                    tNome.setText("");
                    tEnd_res.setText("");
                    tEnd_trab.setText("");
                    tTel_res.setText("");
                    tTel_res2.setText("");
                    tTel_trab.setText("");
                    tTel_trab2.setText("");
                    tValor.setText("");
                    tData_emp.setText("");
                    tData_pag.setText("");
                    tJuros.setText("");
                    tGarantia.setText("");
                    tObs.setText("");
                    tObsEmp.setText("");
                    fonte.setSelectedIndex(0);
                  }

                         }
                        
                    }
                }
            });
            
            limpar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    tNome.setText("");
                    tEnd_res.setText("");
                    tEnd_trab.setText("");
                    tTel_res.setText("");
                    tTel_res2.setText("");
                    tTel_trab.setText("");
                    tTel_trab2.setText("");
                    tValor.setText("");
                    tData_emp.setText("");
                    tData_pag.setText("");
                    tJuros.setText("");
                    tGarantia.setText("");
                    tObs.setText("");
                    tObsEmp.setText("");
                    fonte.setSelectedIndex(0);
                }
            });
            
            sair.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent e){

                   setVisible(false);
           
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
