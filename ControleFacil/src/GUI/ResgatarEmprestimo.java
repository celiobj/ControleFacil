package GUI;

import Classes.Emprestimo;
import Persistencia.RepositorioConta;
import Persistencia.RepositorioEmprestimo;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class ResgatarEmprestimo  extends JPanel{

        JComboBox fonte;
        String contas[]={"Financeiro","Bradesco","LocaSul","Priscila","Marquinho", "Santander", "Dinheiro"};
        JButton listar, todos, limpar, sair, resgatar;
        JTextField tNome, tValor,tGarantia, tCod, tJuros, tObs ;
        JFormattedTextField tData_pag, tData_emp;
        MaskFormatter mascaraData_pag, mascaraData_emp;
        JLabel nome,juros, data_pag,data_emp, valor, garantia, cod, obs;
        JTable tabela;
        JScrollPane scroller;
        String  data_res;
        Calendar data;
        int dia, mes, ano;
        RepositorioEmprestimo re;
        RepositorioConta rco;
        Emprestimo emp;


	    	public ResgatarEmprestimo(){

                    JLabel tela = new JLabel("... :::   Resgatar Empréstimo   ::: ...");
                    tela.setBounds(400,10,500,20);
                    add(tela);

	             listar = new JButton("Listar");
	             limpar = new JButton("Limpar");
                     todos = new JButton("Todos");
	             resgatar = new JButton("Resgatar");
	             sair = new JButton("Sair");
	             tNome = new JTextField(40);
	             nome = new JLabel("Nome: ");
	             cod = new JLabel("Cod: ");
	             valor = new JLabel("Valor: ");
                     obs = new JLabel("Observação: ");
                     juros = new JLabel("Juros: ");
                     data_emp = new JLabel("Data: ");
	             data_pag = new JLabel("Bom P/: ");
	             garantia = new JLabel("Garantia: ");
                     fonte = new JComboBox(contas);
	             tGarantia = new JTextField(30);
                     tObs = new JTextField(200);
	             tValor = new JTextField(5);
                     tJuros = new JTextField(5);
	             tCod = new JTextField(3);
                     listar.setVisible(false);


	             try{

	         	   	mascaraData_pag = new MaskFormatter("##/##/####");
                    mascaraData_emp = new MaskFormatter("##/##/####");

	            }catch (Exception exp){};
	            tData_pag = new JFormattedTextField(mascaraData_pag);
                tData_emp = new JFormattedTextField(mascaraData_emp);




	           
	            	   tValor.setFont(new Font("Times New Roman", Font.BOLD, 14));
                           tJuros.setFont(new Font("Times New Roman", Font.BOLD, 14));
	            	   tGarantia.setFont(new Font("Times New Roman", Font.BOLD, 14));
	            	   tData_pag.setFont(new Font("Times New Roman", Font.BOLD, 14));
                           tData_emp.setFont(new Font("Times New Roman", Font.BOLD, 14));
	            	   tNome.setFont(new Font("Times New Roman", Font.BOLD, 14));
	            	   tCod.setFont(new Font("Times New Roman", Font.BOLD, 14));
                           tObs.setFont(new Font("Times New Roman", Font.BOLD, 14)); 

	            	   data = Calendar.getInstance();
	                   dia = data.get(Calendar.DAY_OF_MONTH);
	                   mes = data.get(Calendar.MONTH);
	                   ano = data.get(Calendar.YEAR);
	                   data_res = +dia+"/"+(mes+1)+"/"+ano;

	            cod.setBounds(40,80,100,20);
	            nome.setBounds(40,120,50,20);
	            tNome.setBounds(85,120,340,20);
                    data_emp.setBounds(40,150,100,20);
                    tData_emp.setBounds(85,150,100,20);
	            data_pag.setBounds(40,180,100,20);
	            tData_pag.setBounds(85,180,100,20);
	            valor.setBounds(200,150,80,20);
                    juros.setBounds(200,180,80,20);
                    tJuros.setBounds(235,180,80,20);
	            tValor.setBounds(235,150,80,20);
	            garantia.setBounds(460,150,80,20);
	            tGarantia.setBounds(520,150,200,20);
                    fonte.setBounds(320,150,130,20);
                    obs.setBounds(320,180,130,20);
                    tObs.setBounds(450,180,250,20);
	            tCod.setBounds(80,80,50,20);
	            todos.setBounds(500,80,100,20);
	            limpar.setBounds(600,80,100,20);
                
	            resgatar.setBounds(800,150,100,20);
	            sair.setBounds(700,80,100,20);

                    add(obs);
                    add(tObs);
	            add(nome);
	            add(tNome);
	            add(cod);
	            add(tCod);
                    add(fonte);
	            add(valor);
	            add(tValor);
                    add(juros);
                    add(tJuros);
	            add(garantia);
	            add(tGarantia);
                    add(data_emp);
                    add(tData_emp);
	            add(data_pag);
	            add(tData_pag);
	            add(listar);
                    add(todos);
	            add(limpar);
	            add(resgatar);
	            add(sair);

                    obs.setVisible(false);
                    tObs.setVisible(false);
	            nome.setVisible(false);
	            tNome.setVisible(false);
	            valor.setVisible(false);
	            tValor.setVisible(false);
                    juros.setVisible(false);
                    tJuros.setVisible(false);
	            data_pag.setVisible(false);
	            tData_pag.setVisible(false);
                    data_emp.setVisible(false);
                    tData_emp.setVisible(false);
	            garantia.setVisible(false);
	            tGarantia.setVisible(false);
	            resgatar.setVisible(false);
                    fonte.setVisible(false);

	            tNome.setEditable(false);
                    tCod.setEditable(false);

	            tCod.addActionListener(new ActionListener(){
	            	public void actionPerformed(ActionEvent e){
                        if(tCod.WHEN_FOCUSED==0){
                        if(tCod.getText().equals("")){
                            JOptionPane.showMessageDialog(null,"Insira um Código, clique em |Todos| para mostrar a lista de Finalizados e liberar o campo |Cód|");
                        }else{
                            Emprestimo emp = re.paraResgatar(tCod.getText());
                            if(emp ==null){
                                JOptionPane.showMessageDialog(null,"Código não está associado a nenhum Empréstimo finalizado\n Verifique o código ou consulte nos Empréstimos abertos." );
                                tCod.setText("");
                            }else{

                        obs.setVisible(true);
                        tObs.setVisible(true);
                        fonte.setVisible(true);
                        nome.setVisible(true);
                        tNome.setVisible(true);
                        valor.setVisible(true);
                        tValor.setVisible(true);
                        juros.setVisible(true);
                        tJuros.setVisible(true);
                        data_pag.setVisible(true);
                        tData_pag.setVisible(true);
                        data_emp.setVisible(true);
                        tData_emp.setVisible(true);
                        garantia.setVisible(true);
                        tGarantia.setVisible(true);
                        resgatar.setVisible(true);
                        tCod.setEditable(false);

                      //  re = new RepositorioEmprestimo();
                        tNome.setText(emp.getNome());
                                }
                              }
                             }
	            		}
	            });

                todos.addActionListener(new ActionListener(){
	            	public void actionPerformed(ActionEvent e){

	            		re = new RepositorioEmprestimo();
                        tabela = re.procurarFinalizados();
                        if(tabela.getValueAt(0, 0)== null){
                            JOptionPane.showMessageDialog(null,"Registros não Encontrados");
                        }else{
                        scroller = new JScrollPane(tabela, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	                    add(scroller);
	                    scroller.setBounds(20, 200,1000, 400);
                        tCod.setEditable(true);

                             }
                        }
	            });

	            limpar.addActionListener(new ActionListener(){
	            	public void actionPerformed(ActionEvent e){

                    /*ResgatarEmprestimo re = new ResgatarEmprestimo();
                    re.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    setVisible(false);*/
                    
                   try{
                    tObs.setText("");
                    obs.setVisible(false);
                    tObs.setVisible(false);
                    tCod.setText("");
                    tNome.setText("");
                    tValor.setText("");
                    tJuros.setText("");
                    tData_emp.setText("");
                    tData_pag.setText("");
                    tGarantia.setText("");
                    fonte.setSelectedItem(0);
                    tCod.setEditable(false);
	            scroller.setVisible(false);
                    remove(scroller);
                    nome.setVisible(false);
                    tNome.setVisible(false);
                    valor.setVisible(false);
                    tValor.setVisible(false);
                    juros.setVisible(false);
                    tJuros.setVisible(false);
                    data_emp.setVisible(false);
                    tData_emp.setVisible(false);
                    data_pag.setVisible(false);
                    tData_pag.setVisible(false);
                    garantia.setVisible(false);
                    tGarantia.setVisible(false);
                    resgatar.setVisible(false);
                    fonte.setVisible(false);
                   }catch(Exception ex){
                    tObs.setText("");
                    obs.setVisible(false);
                    tObs.setVisible(false);
                    tCod.setText("");
                    tNome.setText("");
                    tValor.setText("");
                    tJuros.setText("");
                    tData_emp.setText("");
                    tData_pag.setText("");
                    tGarantia.setText("");
                    fonte.setSelectedItem(0);
                    tCod.setEditable(false);
	            	nome.setVisible(false);
                    tNome.setVisible(false);
                    valor.setVisible(false);
                    tValor.setVisible(false);
                    juros.setVisible(false);
                    tJuros.setVisible(false);
                    data_emp.setVisible(false);
                    tData_emp.setVisible(false);
                    data_pag.setVisible(false);
                    tData_pag.setVisible(false);
                    garantia.setVisible(false);
                    tGarantia.setVisible(false);
                    resgatar.setVisible(false);
                    fonte.setVisible(false);
                   }
                   }
	            });

	            resgatar.addActionListener(new ActionListener(){
	            	public void actionPerformed(ActionEvent e){

                        String pagamento = null;
                        if(tValor.getText().equals("")||tData_pag.getText().equals("  /  /    ")){
                            JOptionPane.showMessageDialog(null, "Preencha os campos corretamente!!!\n Os campos:\n     *Bom p/\n     *Valor\n-Não podem ser vazios!!!");
                        }else{

                        int resposta;
                        resposta=JOptionPane.showConfirmDialog(null,"Deseja realmente resgatar?","Confirmação",JOptionPane.YES_NO_OPTION);
                        if(resposta==0){
                            if(fonte.getSelectedItem().toString().equals("Financeiro")){
                                JOptionPane.showMessageDialog(null, "Selecione a Fonte de onde vai ser sacado o dinheiro");
                            }else{
                             if(fonte.getSelectedItem().toString().equals("Marquinho")){
                                 pagamento = "Marquinho - "+tObs.getText()+"";
                                 rco = new RepositorioConta();
                                    try {
                                        rco.emprestar("Emprestar",Principal.formatoParaInserir(tValor.getText()),"BcoBrasil", "Renovar para "+tCod.getText()+" - "+tNome.getText(),tData_emp.getText());
                                    } catch (ParseException ex) {
                                        Logger.getLogger(ResgatarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                }else
                                     if(fonte.getSelectedItem().toString().equals("Santander")){
                                      pagamento = "Santander - "+tObs.getText()+"";
                                      rco = new RepositorioConta();
                                    try {
                                        rco.emprestar("Emprestar",Principal.formatoParaInserir(tValor.getText()),"BcoReal",  "Renovar para "+tCod.getText()+" - "+tNome.getText(),tData_emp.getText());
                                    } catch (ParseException ex) {
                                        Logger.getLogger(ResgatarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    }else
                                        if(fonte.getSelectedItem().toString().equals("Dinheiro")){
                                             pagamento = "Dinheiro - "+tObs.getText()+"";
                                             rco = new RepositorioConta();
                                    try {
                                        rco.emprestar("Emprestar",Principal.formatoParaInserir(tValor.getText()),"Dinheiro",  "Renovar para "+tCod.getText()+" - "+tNome.getText(),tData_emp.getText());
                                    } catch (ParseException ex) {
                                        Logger.getLogger(ResgatarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                        }else
                                             if(fonte.getSelectedItem().toString().equals("Priscila")){
                                              pagamento = "Priscila - "+tObs.getText()+"";
                                              rco = new RepositorioConta();
                                    try {
                                        rco.emprestar("Emprestar",Principal.formatoParaInserir(tValor.getText()),"Pris",  "Renovar para "+tCod.getText()+" - "+tNome.getText(),tData_emp.getText());
                                    } catch (ParseException ex) {
                                        Logger.getLogger(ResgatarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                            }else
                                                 if(fonte.getSelectedItem().toString().equals("LocaSul")){
                                                  pagamento = "LocaSul - "+tObs.getText()+"";
                                                  rco = new RepositorioConta();
                                    try {
                                        rco.emprestar("Emprestar",Principal.formatoParaInserir(tValor.getText()),"Locasul",  "Renovar para "+tCod.getText()+" - "+tNome.getText(),tData_emp.getText());
                                    } catch (ParseException ex) {
                                        Logger.getLogger(ResgatarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                                  }else
                                                         if(fonte.getSelectedItem().toString().equals("Bradesco")){
                                                          pagamento = "Bradesco - "+tObs.getText()+"";
                                                          rco = new RepositorioConta();
                                    try {
                                        rco.emprestar("Emprestar",Principal.formatoParaInserir(tValor.getText()),"BcoBrad",  "Renovar para "+tCod.getText()+" - "+tNome.getText(),tData_emp.getText());
                                    } catch (ParseException ex) {
                                        Logger.getLogger(ResgatarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                                        }  
                             
                        re = new RepositorioEmprestimo();
                                try {
                                    re.resgatar(tCod.getText(),tNome.getText() , Principal.formatoParaInserir(tValor.getText()),Principal.formatoParaInserir(tJuros.getText()),tData_emp.getText(),tData_pag.getText(),tGarantia.getText(),pagamento);
                                } catch (ParseException ex) {
                                    Logger.getLogger(ResgatarEmprestimo.class.getName()).log(Level.SEVERE, null, ex);
                                }

                             try{
                                        tObs.setText("");
                                        obs.setVisible(false);
                                        tObs.setVisible(false);
                                        tCod.setText("");
                                        tNome.setText("");
                                        tValor.setText("");
                                        tJuros.setText("");
                                        tData_emp.setText("");
                                        tData_pag.setText("");
                                        tGarantia.setText("");
                                        fonte.setSelectedItem(0);
                                        tCod.setEditable(false);
                                        scroller.setVisible(false);
                                        remove(scroller);
                                        nome.setVisible(false);
                                        tNome.setVisible(false);
                                        valor.setVisible(false);
                                        tValor.setVisible(false);
                                        juros.setVisible(false);
                                        tJuros.setVisible(false);
                                        data_emp.setVisible(false);
                                        tData_emp.setVisible(false);
                                        data_pag.setVisible(false);
                                        tData_pag.setVisible(false);
                                        garantia.setVisible(false);
                                        tGarantia.setVisible(false);
                                        resgatar.setVisible(false);
                                        fonte.setVisible(false);
                                       }catch(Exception ex){
                                        tObs.setText("");
                                        obs.setVisible(false);
                                        tObs.setVisible(false);
                                        tCod.setText("");
                                        tNome.setText("");
                                        tValor.setText("");
                                        tJuros.setText("");
                                        tData_emp.setText("");
                                        tData_pag.setText("");
                                        tGarantia.setText("");
                                        fonte.setSelectedItem(0);
                                        tCod.setEditable(false);
                                        nome.setVisible(false);
                                        tNome.setVisible(false);
                                        valor.setVisible(false);
                                        tValor.setVisible(false);
                                        juros.setVisible(false);
                                        tJuros.setVisible(false);
                                        data_emp.setVisible(false);
                                        tData_emp.setVisible(false);
                                        data_pag.setVisible(false);
                                        tData_pag.setVisible(false);
                                        garantia.setVisible(false);
                                        tGarantia.setVisible(false);
                                        resgatar.setVisible(false);
                                        fonte.setVisible(false);
                                       }

                              }
                            }
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