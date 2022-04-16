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
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author celio
 */
public class ControlaBcoBrad extends JPanel{

    JButton extrato,transferir,depositar, sacar,ok,oksacar,okdepositar,oktransferir,limpar, sair;
    JComboBox destino;
    String contas[]={"","Priscila","LocaSul","Marquinho","Santander","Dinheiro"};
    JLabel valor,saldoExtrato, acao, motivo;
    JTextField tValor, tMotivo;
    JScrollPane scroller;
    JTable tabela;
    RepositorioConta rc;


        public ControlaBcoBrad(){

            
            JLabel tela = new JLabel("... :::   Controle Bradesco   ::: ...");
            tela.setBounds(400,10,500,20);
            add(tela);
            
            extrato = new JButton("Extrato");
            transferir = new JButton("Transferir");
            sacar =  new JButton("Sacar");
            depositar = new JButton("Depositar");
            motivo = new JLabel("Motivo: ");
            tMotivo = new JTextField(100);
            ok = new JButton("OK");
            oksacar  = new JButton("OK");
            oktransferir  = new JButton("OK");
            okdepositar  = new JButton("OK");
            sair = new JButton("Sair");
            limpar = new JButton("Limpar");
            valor = new JLabel("Valor: ");
            tValor = new JTextField(10);
            saldoExtrato = new JLabel("");
            acao = new JLabel("");
            destino = new JComboBox(contas);

            extrato.setBounds(200,50,100,20);
            transferir.setBounds(350,50,100,20);
            sacar.setBounds(500,50,100,20);
            depositar.setBounds(650,50,100,20);
            sair.setBounds(800,50,100,20);
            acao.setBounds(200,100,100,20);
            valor.setBounds(200,150,100,20);
            tValor.setBounds(240,150,100,20);
            motivo.setBounds(200,180,100,20);
            tMotivo.setBounds(250,180,200,20);
            destino.setBounds(350,150,130,20);
            ok.setBounds(500,150,100,20);
            oktransferir.setBounds(500,150,100,20);
            okdepositar.setBounds(500,150,100,20);
            oksacar.setBounds(500,150,100,20);
            limpar.setBounds(650,150,100,20);
            saldoExtrato.setBounds(50,120,200,20);

            tValor.setFont(new Font("Times New Roman", Font.BOLD, 14));
            tMotivo.setFont(new Font("Times New Roman", Font.BOLD, 14));

            destino.setVisible(false);
            motivo.setVisible(false);
            tMotivo.setVisible(false);
            valor.setVisible(false);
            tValor.setVisible(false);
            ok.setVisible(false);
            oksacar.setVisible(false);
            okdepositar.setVisible(false);
            oktransferir.setVisible(false);
            limpar.setVisible(false);

            add(ok);
            add(acao);
            add(oktransferir);
            add(oksacar);
            add(okdepositar);
            add(sair);
            add(extrato);
            add(transferir);
            add(sacar);
            add(depositar);
            add(limpar);
            add(valor);
            add(tValor);
            add(saldoExtrato);
            add(destino);
            add(motivo);
            add(tMotivo);



            extrato.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                rc = new RepositorioConta();
                Double saldo = rc.saldo("BcoBrad");
                saldoExtrato.setText("Saldo Atual: R$ "+Principal.paraFormatoDinheiro(saldo));
                tabela = rc.extrato("BcoBrad");
                scroller = new JScrollPane(tabela,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        add(scroller);
	        scroller.setBounds(20, 210,1000, 250);
                }
             });

            depositar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                destino.setVisible(false);
                valor.setVisible(true);
                tValor.setVisible(true);
                motivo.setVisible(true);
                tMotivo.setVisible(true);
                okdepositar.setVisible(true);
                limpar.setVisible(true);
                acao.setText("Depósito");

                 }
           });

            sacar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                destino.setVisible(false);
                valor.setVisible(true);
                tValor.setVisible(true);
                motivo.setVisible(true);
                tMotivo.setVisible(true);
                oksacar.setVisible(true);
                limpar.setVisible(true);
                acao.setText("Saque");

                 }
           });

            transferir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                destino.setVisible(true);
                valor.setVisible(true);
                tValor.setVisible(true);
                motivo.setVisible(true);
                tMotivo.setVisible(true);
                oktransferir.setVisible(true);
                limpar.setVisible(true);
                acao.setText("Transferência");

                 }
           });

            oksacar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                rc = new RepositorioConta();
                double saldoDoub = 0;
                    try {
                        saldoDoub = rc.sacar("Saque",Principal.formatoParaInserir(tValor.getText()),"BcoBrad", tMotivo.getText());
                    } catch (ParseException ex) {
                        Logger.getLogger(ControlaBcoBrad.class.getName()).log(Level.SEVERE, null, ex);
                    }
                //BigDecimal saldo = BigDecimal.valueOf(saldoDoub);
                String novoSaldo = Principal.paraFormatoDinheiro(saldoDoub);
                JOptionPane.showMessageDialog(null,"Novo saldo é: "+novoSaldo);
                /*ControlaBcoBrasil cbb = new ControlaBcoBrasil();
                cbb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                setVisible(false);*/
                
                 try{
                destino.setVisible(false);
                valor.setVisible(false);
                tValor.setVisible(false);
                tValor.setText("");
                motivo.setVisible(false);
                tMotivo.setVisible(false);
                tMotivo.setText("");
                acao.setText("");
                saldoExtrato.setText("");
                ok.setVisible(false);
                oksacar.setVisible(false);
                okdepositar.setVisible(false);
                oktransferir.setVisible(false);
                limpar.setVisible(false);
                scroller.setVisible(false);
                remove(scroller);
               }catch(Exception ex){
                    destino.setVisible(false);
                    valor.setVisible(false);
                    tValor.setVisible(false);
                    motivo.setVisible(false);
                    tMotivo.setVisible(false);
                    tMotivo.setText("");
                    tValor.setText("");
                    acao.setText("");
                    saldoExtrato.setText("");
                    ok.setVisible(false);
                    oksacar.setVisible(false);
                    okdepositar.setVisible(false);
                    oktransferir.setVisible(false);
                    limpar.setVisible(false);

               }
                 }
           });

           oktransferir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String banco = null;
                rc = new RepositorioConta();
                double saldoDeNum = rc.saldo("BcoBrad");
                String saldoDe = String.valueOf(saldoDeNum);
                if(destino.getSelectedItem().toString().equals("Santander")){
                    banco = "BcoReal";
                }else
                    if(destino.getSelectedItem().toString().equals("Dinheiro")){
                    banco = "Dinheiro";
                    }else
                        if(destino.getSelectedItem().toString().equals("Marquinho")){
                        banco = "BcoBrasil";
                        }else
                            if(destino.getSelectedItem().toString().equals("Priscila")){
                            banco = "Pris";
                            }else
                                if(destino.getSelectedItem().toString().equals("LocaSul")){
                                banco = "Locasul";
                                }
                double saldoParaNum =  rc.saldo(banco);
                String saldoPara = String.valueOf(saldoParaNum);
                double saldoDoub = 0;
                   try {
                       saldoDoub = rc.transferir("BcoBrad",saldoDe,banco,saldoPara,Principal.formatoParaInserir(tValor.getText()) ,destino.getSelectedItem().toString()+" - "+tMotivo.getText()+"","Banco Bradesco - "+tMotivo.getText()+"");
                   } catch (ParseException ex) {
                       Logger.getLogger(ControlaBcoBrad.class.getName()).log(Level.SEVERE, null, ex);
                   }
                //BigDecimal saldo = BigDecimal.valueOf(saldoDoub);
                String novoSaldo = Principal.paraFormatoDinheiro(saldoDoub);
                JOptionPane.showMessageDialog(null,"Novo saldo é: "+novoSaldo);
               /* ControlaBcoBrasil cbb = new ControlaBcoBrasil();
                cbb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                setVisible(false);*/
               
                try{
                destino.setVisible(false);
                valor.setVisible(false);
                tValor.setVisible(false);
                tValor.setText("");
                motivo.setVisible(false);
                tMotivo.setVisible(false);
                tMotivo.setText("");
                acao.setText("");
                saldoExtrato.setText("");
                ok.setVisible(false);
                oksacar.setVisible(false);
                okdepositar.setVisible(false);
                oktransferir.setVisible(false);
                limpar.setVisible(false);
                scroller.setVisible(false);
                remove(scroller);
               }catch(Exception ex){
                    destino.setVisible(false);
                    valor.setVisible(false);
                    tValor.setVisible(false);
                    tValor.setText("");
                    motivo.setVisible(false);
                    tMotivo.setVisible(false);
                    tMotivo.setText("");
                    acao.setText("");
                    saldoExtrato.setText("");
                    ok.setVisible(false);
                    oksacar.setVisible(false);
                    okdepositar.setVisible(false);
                    oktransferir.setVisible(false);
                    limpar.setVisible(false);

               }
                }
           });

           okdepositar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                rc = new RepositorioConta();
                double saldoDoub = 0;
                   try {
                       saldoDoub = rc.depositar("Depósito",Principal.formatoParaInserir(tValor.getText()),"BcoBrad", tMotivo.getText());
                   } catch (ParseException ex) {
                       Logger.getLogger(ControlaBcoBrad.class.getName()).log(Level.SEVERE, null, ex);
                   }
                //BigDecimal saldo = BigDecimal.valueOf(saldoDoub);
                String novoSaldo = Principal.paraFormatoDinheiro(saldoDoub);
                JOptionPane.showMessageDialog(null,"Novo saldo é: "+novoSaldo);
               /* ControlaBcoBrasil cbb = new ControlaBcoBrasil();
                cbb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                setVisible(false);*/
                
                try{
                destino.setVisible(false);
                valor.setVisible(false);
                tValor.setVisible(false);
                tValor.setText("");
                motivo.setVisible(false);
                tMotivo.setVisible(false);
                tMotivo.setText("");
                acao.setText("");
                saldoExtrato.setText("");
                ok.setVisible(false);
                oksacar.setVisible(false);
                okdepositar.setVisible(false);
                oktransferir.setVisible(false);
                limpar.setVisible(false);
                scroller.setVisible(false);
                remove(scroller);
               }catch(Exception ex){
                    destino.setVisible(false);
                    valor.setVisible(false);
                    tValor.setVisible(false);
                    tValor.setText("");
                    motivo.setVisible(false);
                    tMotivo.setVisible(false);
                    tMotivo.setText("");
                    acao.setText("");
                    saldoExtrato.setText("");
                    ok.setVisible(false);
                    oksacar.setVisible(false);
                    okdepositar.setVisible(false);
                    oktransferir.setVisible(false);
                    limpar.setVisible(false);

               }
                 }
           });

            limpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

               /* ControlaBcoBrasil cbb = new ControlaBcoBrasil();
                cbb.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                setVisible(false);*/
               try{
                destino.setVisible(false);
                valor.setVisible(false);
                tValor.setVisible(false);
                tValor.setText("");
                acao.setText("");
                motivo.setVisible(false);
                tMotivo.setVisible(false);
                tMotivo.setText("");
                saldoExtrato.setText("");
                ok.setVisible(false);
                oksacar.setVisible(false);
                okdepositar.setVisible(false);
                oktransferir.setVisible(false);
                limpar.setVisible(false);
                scroller.setVisible(false);
                remove(scroller);
               }catch(Exception ex){
                    destino.setVisible(false);
                    valor.setVisible(false);
                    tValor.setVisible(false);
                    tValor.setText("");
                    acao.setText("");
                    saldoExtrato.setText("");
                    ok.setVisible(false);
                    oksacar.setVisible(false);
                    okdepositar.setVisible(false);
                    oktransferir.setVisible(false);
                    limpar.setVisible(false);

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
