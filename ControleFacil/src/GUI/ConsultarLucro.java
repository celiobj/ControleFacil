/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Persistencia.RepositorioConta;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author celio
 */
public class ConsultarLucro extends JPanel{

    JButton ok,limpar,saldo, sair;
    JComboBox destino;
    String contas[]={"","Todos","Bradesco", "Dinheiro","Locasul","Marquinho", "Priscila","Santander"};
    JLabel mar, santa, din,locasul,pris,brad,emprestado,recebido,lucro,tEmpMar,tEmpSanta,tEmpDin,tEmpLocasul,tEmpPris,tEmpBrad, tRecMar, tRecSanta, tRecDin,tRecLocasul,tRecPris,tRecBrad, tLucroMar, tLucroSanta, tLucroDin,tLucroLocasul, tLucroPris, tLucroBrad, data_ini, data_fim,total ,tTotalLucro, tTotalEmp, tTotalRec, saldoMar, saldoSanta, saldoDin,saldoLocasul, saldoPris, saldoBrad, saldoContas;
    JFormattedTextField tData_ini,tData_fim;
    MaskFormatter mascaraData_ini,mascaraData_fim;
    RepositorioConta rc;
    


        public ConsultarLucro(){

            
            JLabel tela = new JLabel("... :::   Consultar Lucro   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
           
            ok = new JButton("OK");
            sair = new JButton("Sair");
            limpar = new JButton("Limpar");
            emprestado = new JLabel("|Emprestado|");
            recebido = new JLabel("|Recebido|");
            lucro = new JLabel("|Lucro|");
            mar = new JLabel("|Marquinho|");
            santa = new JLabel("|Santander|");
            locasul = new JLabel("|Locasul|");
            pris = new JLabel("|Priscila|");
            brad = new JLabel("|Bradesco|");
            din = new JLabel("|Dinheiro|");
            total = new JLabel("|Total|");
            tEmpMar = new JLabel("");
            tEmpSanta = new JLabel("");
            tEmpLocasul = new JLabel("");
            tEmpPris = new JLabel("");
            tEmpBrad = new JLabel("");
            tEmpDin = new JLabel("");
            tRecMar = new JLabel("");
            tRecSanta = new JLabel("");
            tRecLocasul = new JLabel("");
            tRecPris = new JLabel("");
            tRecBrad = new JLabel("");
            tRecDin = new JLabel("");
            tLucroMar = new JLabel("");
            tLucroSanta = new JLabel("");
            tLucroLocasul = new JLabel("");
            tLucroPris = new JLabel("");
            tLucroBrad = new JLabel("");
            tLucroDin = new JLabel("");
            tTotalEmp = new JLabel("");
            tTotalRec = new JLabel("");
            tTotalLucro = new JLabel("");
            //destino = new JComboBox(contas);
            data_ini = new JLabel("de:");
            data_fim = new JLabel("à");

               try{
        	   	mascaraData_ini = new MaskFormatter("##/##/####");
                mascaraData_fim = new MaskFormatter("##/##/####");
            	}catch (Exception exp){};
                    tData_ini = new JFormattedTextField(mascaraData_ini);
                    tData_fim = new JFormattedTextField(mascaraData_fim);


            ok.setBounds(650,50,100,20);
            sair.setBounds(850,50,100,20);
            emprestado.setBounds(190,150,100,20);
            recebido.setBounds(190,180,100,20);
            lucro.setBounds(190,210,100,20);
            mar.setBounds(290,130,100,20);
            tEmpMar.setBounds(300,150,100,20);
            tRecMar.setBounds(300,180,100,20);
            tLucroMar.setBounds(300,210,100,20);
            santa.setBounds(390,130,100,20);
            tEmpSanta.setBounds(400,150,100,20);
            tRecSanta.setBounds(400,180,100,20);
            tLucroSanta.setBounds(400,210,100,20);
            din.setBounds(500,130,100,20);
            tEmpDin.setBounds(500,150,100,20);
            tRecDin.setBounds(500,180,100,20);
            tLucroDin.setBounds(500,210,100,20);
            locasul.setBounds(600,130,100,20);
            tEmpLocasul.setBounds(600,150,100,20);
            tRecLocasul.setBounds(600,180,100,20);
            tLucroLocasul.setBounds(600,210,100,20);
            pris.setBounds(700,130,100,20);
            tEmpPris.setBounds(700,150,100,20);
            tRecPris.setBounds(700,180,100,20);
            tLucroPris.setBounds(700,210,100,20);
            brad.setBounds(800,130,100,20);
            tEmpBrad.setBounds(800,150,100,20);
            tRecBrad.setBounds(800,180,100,20);
            tLucroBrad.setBounds(800,210,100,20);
            total.setBounds(900,130,100,20);
            tTotalEmp.setBounds(900,150,100,20);
            tTotalRec.setBounds(900,180,100,20);
            tTotalLucro.setBounds(900,210,100,20);
            
            data_ini.setBounds(190,100,100,20);
            tData_ini.setBounds(220,100,80,20);
            data_fim.setBounds(310,100,100,20);
            tData_fim.setBounds(330,100,80,20);
          //  destino.setBounds(430,100,130,20);
            limpar.setBounds(750,50,100,20);
            
            

            tData_ini.setFont(new Font("Times New Roman", Font.BOLD, 14));
            tData_fim.setFont(new Font("Times New Roman", Font.BOLD, 14));

            add(ok);
            add(sair);
            add(limpar);
            //  tela.add(destino);
            add(data_fim);
            add(data_ini);
            add(tData_fim);
            add(tData_ini);
            add(recebido);
            add(emprestado);
            add(lucro);
            add(mar);
            add(santa);
            add(din);
            add(locasul);
            add(pris);
            add(brad);
            add(tEmpMar);
            add(tRecMar);
            add(tLucroMar);
            add(tEmpSanta);
            add(tRecSanta);
            add(tLucroSanta);
            add(tEmpDin);
            add(tRecDin);
            add(tLucroDin);
            add(tEmpLocasul);
            add(tRecLocasul);
            add(tLucroLocasul);
            add(tEmpPris);
            add(tRecPris);
            add(tLucroPris);
            add(tEmpBrad);
            add(tRecBrad);
            add(tLucroBrad);
            add(total);
            add(tTotalEmp);
            add(tTotalRec);
            add(tTotalLucro);
                      

            limpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

               /* ControlaBcoBrasil cbb = new ControlaBcoBrasil();
                cbb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                setVisible(false);*/
                
                        tData_fim.setText("");
                        tData_ini.setText("");
                        tEmpMar.setText("");
                        tEmpSanta.setText("");
                        tEmpDin.setText("");
                        tEmpBrad.setText("");
                        tEmpLocasul.setText("");
                        tEmpPris.setText("");
                        tRecMar.setText("");
                        tRecSanta.setText("");
                        tRecDin.setText("");
                        tRecBrad.setText("");
                        tRecLocasul.setText("");
                        tRecPris.setText("");
                        tLucroMar.setText("");
                        tLucroSanta.setText("");
                        tLucroDin.setText("");
                        tLucroBrad.setText("");
                        tLucroLocasul.setText("");
                        tLucroPris.setText("");
                        tTotalEmp.setText("");
                        tTotalRec.setText("");
                        tTotalLucro.setText("");
                       

            }
             });

             

             ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if(tData_ini.getText().equals("  /  /    ")||tData_fim.getText().equals("  /  /    ")){
                    JOptionPane.showMessageDialog(null,"Escolha um período!!!");
                }else{
                        rc = new RepositorioConta();

                        double emprestadoBrasilDoub = rc.consultarEmprestado("BcoBrasil", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal emprestadoBrasil = BigDecimal.valueOf(emprestadoBrasilDoub);
                        double recebidoBrasilDoub = rc.consultarRecebido("BcoBrasil", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal recebidoBrasil = BigDecimal.valueOf(recebidoBrasilDoub);
                        double emprestadoRealDoub = rc.consultarEmprestado("BcoReal", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal emprestadoReal = BigDecimal.valueOf(emprestadoRealDoub);
                        double recebidoRealDoub = rc.consultarRecebido("BcoReal", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal recebidoReal = BigDecimal.valueOf(recebidoRealDoub);
                        double emprestadoDinDoub = rc.consultarEmprestado("Dinheiro", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal emprestadoDin = BigDecimal.valueOf(emprestadoDinDoub);
                        double recebidoDinDoub = rc.consultarRecebido("Dinheiro", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal recebidoDin = BigDecimal.valueOf(recebidoDinDoub);
                        double emprestadoBradDoub = rc.consultarEmprestado("BcoBrad", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal emprestadoBrad = BigDecimal.valueOf(emprestadoBradDoub);
                        double recebidoBradDoub = rc.consultarRecebido("BcoBrad", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal recebidoBrad = BigDecimal.valueOf(recebidoBradDoub);
                        double emprestadoLocSulDoub = rc.consultarEmprestado("Locasul", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal emprestadoLocSul = BigDecimal.valueOf(emprestadoLocSulDoub);
                        double recebidoLocSulDoub = rc.consultarRecebido("Locasul", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal recebidoLocSul = BigDecimal.valueOf(recebidoLocSulDoub);
                        double emprestadoPrisDoub = rc.consultarEmprestado("Pris", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal emprestadoPris = BigDecimal.valueOf(emprestadoPrisDoub);
                        double recebidoPrisDoub = rc.consultarRecebido("Pris", tData_ini.getText(), tData_fim.getText());
                        //BigDecimal recebidoPris = BigDecimal.valueOf(recebidoPrisDoub);
                        
                     

                        double lucroBraDoub = recebidoBrasilDoub - emprestadoBrasilDoub;
                        //BigDecimal lucroBra = BigDecimal.valueOf(lucroBraDoub);
                        double lucroReDoub = recebidoRealDoub - emprestadoRealDoub;
                        //BigDecimal lucroRe = BigDecimal.valueOf(lucroReDoub);
                        double lucroDinDoub = recebidoDinDoub - emprestadoDinDoub;
                        //BigDecimal lucroDin = BigDecimal.valueOf(lucroDinDoub);
                        double lucroBradDoub = recebidoBradDoub - emprestadoBradDoub;
                        //BigDecimal lucroBrad = BigDecimal.valueOf(lucroBradDoub);
                        double lucroPrisDoub = recebidoPrisDoub - emprestadoPrisDoub;
                        //BigDecimal lucroPris = BigDecimal.valueOf(lucroPrisDoub);
                        double lucroLocSulDoub = recebidoLocSulDoub - emprestadoLocSulDoub;
                        //BigDecimal lucrolocSul = BigDecimal.valueOf(lucroLocSulDoub);

                        double emprestadoTotalDoub = emprestadoBrasilDoub + emprestadoRealDoub + emprestadoDinDoub + emprestadoBradDoub + emprestadoLocSulDoub + emprestadoPrisDoub;
                        //BigDecimal emprestadoTotal = BigDecimal.valueOf(emprestadoTotalDoub);
                        double recebidoTotalDoub = recebidoBrasilDoub + recebidoRealDoub + recebidoDinDoub + recebidoBradDoub + recebidoLocSulDoub + recebidoPrisDoub;
                        //BigDecimal recebidoTotal = BigDecimal.valueOf(recebidoTotalDoub);
                        //double lucroTotalDoub =  emprestadoTotalDoub - recebidoTotalDoub;
                        double lucroTotalDoub =  lucroBraDoub+lucroBradDoub+lucroDinDoub+lucroLocSulDoub+lucroPrisDoub+lucroPrisDoub+lucroReDoub;
                        //BigDecimal lucroTotal = BigDecimal.valueOf(lucroTotalDoub);
                                          
                        tEmpMar.setText("R$: "+Principal.paraFormatoDinheiro(emprestadoBrasilDoub));
                        tEmpSanta.setText("R$: "+Principal.paraFormatoDinheiro(emprestadoRealDoub));
                        tEmpDin.setText("R$: "+Principal.paraFormatoDinheiro(emprestadoDinDoub));
                        tEmpBrad.setText("R$: "+Principal.paraFormatoDinheiro(emprestadoBradDoub));
                        tEmpLocasul.setText("R$: "+Principal.paraFormatoDinheiro(emprestadoLocSulDoub));
                        tEmpPris.setText("R$: "+Principal.paraFormatoDinheiro(emprestadoPrisDoub));
                        tRecMar.setText("R$: "+Principal.paraFormatoDinheiro(recebidoBrasilDoub));
                        tRecSanta.setText("R$: "+Principal.paraFormatoDinheiro(recebidoRealDoub));
                        tRecDin.setText("R$: "+Principal.paraFormatoDinheiro(recebidoDinDoub));
                        tRecBrad.setText("R$: "+Principal.paraFormatoDinheiro(recebidoBradDoub));
                        tRecLocasul.setText("R$: "+Principal.paraFormatoDinheiro(recebidoLocSulDoub));
                        tRecPris.setText("R$: "+Principal.paraFormatoDinheiro(recebidoPrisDoub));
                        tLucroMar.setText("R$: "+Principal.paraFormatoDinheiro(lucroBraDoub));
                        tLucroSanta.setText("R$: "+Principal.paraFormatoDinheiro(lucroReDoub));
                        tLucroDin.setText("R$: "+Principal.paraFormatoDinheiro(lucroDinDoub));
                        tLucroBrad.setText("R$: "+Principal.paraFormatoDinheiro(lucroBradDoub));
                        tLucroLocasul.setText("R$: "+Principal.paraFormatoDinheiro(lucroLocSulDoub));
                        tLucroPris.setText("R$: "+Principal.paraFormatoDinheiro(lucroPrisDoub));
                        tTotalEmp.setText("R$: "+Principal.paraFormatoDinheiro(emprestadoTotalDoub));
                        tTotalRec.setText("R$: "+Principal.paraFormatoDinheiro(recebidoTotalDoub));
                        tTotalLucro.setText("R$: "+Principal.paraFormatoDinheiro(lucroTotalDoub));
                        

                }


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
