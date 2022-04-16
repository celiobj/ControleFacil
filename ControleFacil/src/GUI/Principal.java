/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

/**
 *
 * @author celio
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;



/**
 *
 * @author celio
 */
public class Principal extends JFrame {


        JToolBar menu;
        JScrollPane scroller;
        JTable tabela;
        Vector<Vector> preencher;
        MaskFormatter mascaraHora;
        MaskFormatter mascaraDia;
        MaskFormatter mascaraCpf;
        RealizarEmprestimo re;
        AlterarEmprestimo ae;
        Controle co;
        ResgatarEmprestimo res;
        ControlaBcoBrasil cbb;
        ControlaBcoReal cst;
        ControlaDinheiro cdin;
        ControlaPris cpris;
        ControlaLocSul cloc;
        ControlaBcoBrad cbrad;        
        ConsultarLucro cl;
        CadCondPag ccp;
        PainelControle pc;
        Movimento mov;
        ControleObjetos controleObj;
        CadTipoObj cadTipoObj;
        CadObj cadObj;
        CadProduto cadProduto;
        entradaSaida es;
        JMenuBar menuTopo;
        JMenu cadastro,estoque,relatorios, acoes, carros, financeiro;
        JMenuItem cad_tipo_obj, cad_obj,cad_cond_pag, cad_produto,entSaida,realizar,alterar,controle,resgatar,finalizar, sair2, porNome, todos,mar,bcoBrad,locaSul,Pris,bcoSanta,dinheiro, cadCli, lucro, alugar,reservar,renov_alug, despesas, relatorio;
        final Container tela;
        int qtdDia ;
        Calendar data;
        int dia, dia_semana, mes, ano;
        public static String data_hoje; 
        
    
        //Botão Aluguel Carro
        JButton controleObjetos;
        ImageIcon imageControleObjeto = new ImageIcon("Icones\\objetos.jpg");
    
        //Botão Aluguel Carro
        //JButton aluguelCarro;
        //ImageIcon imageAlugCarro = new ImageIcon("Icones\\carro.png");
        
        //Botão Aluguel apartamento
        //JButton aluguelApt;
        //ImageIcon imageAlugApt = new ImageIcon("Icones\\apt.png");
        
         //Botão Painel de controle
        JButton painel_controle;
        ImageIcon imagePainel_controle = new ImageIcon("Icones\\painel_controle.jpg");
             
        //Botão Movimentação diária
        JButton movimentacaoDiaria;
        ImageIcon imageFuncionario = new ImageIcon("Icones\\movimentacao.png");
        
        
        //Botão Sair  
        JButton  sair;
        ImageIcon imageSair = new ImageIcon("Icones\\botao_sair.png"); 
   

  
       

       public Principal() throws EmailException, UnknownHostException {

            super("..:: Controle Fácil - Vs. 1.0 ::..");
            tela = getContentPane();
            setLayout(null);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            setSize(1280,720);
            setLocationRelativeTo(null);
            setVisible(true);
            revalidate();
            repaint();
            
                    
            
            criaMenu();
            
            menuTopo = new JMenuBar();
            
            cadastro = new JMenu("Cadastro");
            cad_tipo_obj = new JMenuItem("Tipo de Objeto");
            cad_obj = new JMenuItem("Objeto");
            cad_cond_pag = new JMenuItem("Condições de Pagamento");
            cadastro.add(cad_tipo_obj);
            cadastro.add(cad_obj);
            cadastro.add(cad_cond_pag);
            
            
            
            acoes = new JMenu("Ações");            
            realizar = new JMenuItem("|Realizar Empréstimo|");
            alterar = new JMenuItem("|Alterar Empréstimo|");
            finalizar = new JMenuItem("|Finalizar Empréstimo|");
            controle = new JMenuItem("|Controle|");
            resgatar = new JMenuItem("|Resgatar|");
            acoes.add(realizar);
            acoes.add(alterar);
            acoes.add(controle);
            acoes.add(resgatar);
            
            
            carros = new JMenu("Carros");
            alugar = new JMenuItem("|Alugar|");
            reservar = new JMenuItem("|Reservar|");
            renov_alug = new JMenuItem("|Renovar/Finalizar|");
            despesas = new JMenuItem("|Despesas|");
            carros.add(alugar);
            carros.add(reservar);
            carros.add(renov_alug);
            carros.add(despesas);
           
            
            
            financeiro = new JMenu("Financeiro");
            mar = new JMenuItem("|Marquinho|");
            bcoBrad = new JMenuItem("|Bradesco|");
            locaSul = new JMenuItem("|LocaSul|");
            Pris = new JMenuItem("|Priscila|");
            bcoSanta = new JMenuItem("|Santander|");
            dinheiro = new JMenuItem("|Dinheiro|");
            lucro = new JMenuItem("|Lucro|");
            financeiro.add(mar);
            financeiro.add(bcoSanta);
            financeiro.add(dinheiro);
            financeiro.add(bcoBrad);
            financeiro.add(Pris);
            financeiro.add(locaSul);
            financeiro.add(lucro);
            
            
            estoque = new JMenu("Estoque");
            cad_produto = new JMenuItem("Cadastro de Produto");
            entSaida = new JMenuItem("Entrada/Saída");
            estoque.add(cad_produto);
            estoque.add(entSaida);
            
            relatorios = new JMenu("Relatórios");
            
            
            menuTopo.add(acoes);
          //  menuTopo.add(carros);
            menuTopo.add(financeiro);
            menuTopo.add(cadastro);
            menuTopo.add(estoque);
            menuTopo.add(relatorios);
            setJMenuBar(menuTopo);
            
            
            
             try{
        	 mascaraHora = new MaskFormatter("##:##:##");
             }catch (Exception exp){};
             
              try{
        	 mascaraCpf = new MaskFormatter("###.###.###-##");
             }catch (Exception exp){};
             
             try{
        	 mascaraDia = new MaskFormatter("##/##/####");
             }catch (Exception exp){};
             
                        
          
            menu.setBounds(1,1,tela.getWidth(),80); 
            tela.add(menu);
           
            realizar.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){

                   
                       removeTudo();
                    
                    re =  new RealizarEmprestimo();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    re.setBounds(50, 100, 1100, 500);
                    re.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                  
                    
                    tela.add(re);
                    tela.revalidate();
                    tela.repaint();
                       
            	}
            });
            
            alterar.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){

                   
                       removeTudo();
                    
                    ae =  new AlterarEmprestimo();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    ae.setBounds(50, 100, 1100, 500);
                    ae.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(ae);
                    tela.revalidate();
                    tela.repaint();
                       
            	}
            });
            
            controle.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){

                   
                       removeTudo();
                    
                    co = new Controle();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    co.setBounds(50, 100, 1100, 500);
                    co.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(co);
                    tela.revalidate();
                    tela.repaint();
                       
            	}
            });
            
            
            resgatar.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){

                   
                       removeTudo();
                    
                    res = new ResgatarEmprestimo();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    res.setBounds(50, 100, 1100, 500);
                    res.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(res);
                    tela.revalidate();
                    tela.repaint();
                       
            	}
            });
            
            
            mar.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){

                   
                       removeTudo();
                    
                    cbb =  new  ControlaBcoBrasil();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    cbb.setBounds(50, 100, 1100, 500);
                    cbb.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(cbb);
                    tela.revalidate();
                    tela.repaint();
                       
            	}
            });
            
            
            bcoSanta.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){

                   
                       removeTudo();
                    
                    cst =  new ControlaBcoReal();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    cst.setBounds(50, 100, 1100, 500);
                    cst.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(cst);
                    tela.revalidate();
                    tela.repaint();
                       
            	}
            });
            
            
            dinheiro.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){

                   
                       removeTudo();
                    
                    cdin =  new ControlaDinheiro();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    cdin.setBounds(50, 100, 1100, 500);
                    cdin.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(cdin);
                    tela.revalidate();
                    tela.repaint();
                       
            	}
            });
            
            
            Pris.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){

                   
                       removeTudo();
                    
                    cpris =  new ControlaPris();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    cpris.setBounds(50, 100, 1100, 500);
                    cpris.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(cpris);
                    tela.revalidate();
                    tela.repaint();
                       
            	}
            });
            
            
            locaSul.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){

                   
                       removeTudo();
                    
                    cloc =  new ControlaLocSul();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    cloc.setBounds(50, 100, 1100, 500);
                    cloc.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(cloc);
                    tela.revalidate();
                    tela.repaint();
                       
            	}
            });
            
                        
            
            bcoBrad.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){

                   
                       removeTudo();
                    
                    cbrad =  new ControlaBcoBrad();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    cbrad.setBounds(50, 100, 1100, 500);
                    cbrad.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(cbrad);
                    tela.revalidate();
                    tela.repaint();
                       
            	}
            });
            
            
            lucro.addActionListener(new ActionListener(){
                
            	public void actionPerformed(ActionEvent e){
                     
                    
                    removeTudo();
                    
                    cl =  new ConsultarLucro();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    cl.setBounds(50, 100, 1100, 500);
                    cl.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(cl);
                    tela.revalidate();
                    tela.repaint();
          
                 }
             });
           
           
             movimentacaoDiaria.addActionListener(new ActionListener(){
                
            	public void actionPerformed(ActionEvent e){
                     
                    removeTudo();
                     try {
                         mov =  new Movimento();
                     } catch (ParseException ex) {
                         Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                     }
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    mov.setBounds(50, 100, 1100, 500);
                    mov.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    painel_controle.setEnabled(true);
                    movimentacaoDiaria.setEnabled(false);
                  
                    
                    tela.add(mov);
                    tela.revalidate();
                    tela.repaint();
          
                 }
             });
             
             controleObjetos.addActionListener(new ActionListener(){
                
            	public void actionPerformed(ActionEvent e){
                     
                    removeTudo();
                    
                    controleObj =  new ControleObjetos();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    controleObj.setBounds(50, 100, 1100, 500);
                    controleObj.add(ferramentas);
                   
                    controleObjetos.setEnabled(false);
                    painel_controle.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                  
                    
                    tela.add(controleObj);
                    tela.revalidate();
                    tela.repaint();
          
                 }
             });
             
             
             painel_controle.addActionListener(new ActionListener(){
                
            	public void actionPerformed(ActionEvent e){
                     
                    
                    removeTudo();
                    
                    pc =  new PainelControle();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    pc.setBounds(50, 100, 1100, 500);
                    pc.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(false);
                  
                    
                    tela.add(pc);
                    tela.revalidate();
                    tela.repaint();
          
                 }
             });
             
             cad_tipo_obj.addActionListener(new ActionListener(){
                
            	public void actionPerformed(ActionEvent e){
                     
                    removeTudo();
                    
                    cadTipoObj =  new CadTipoObj();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    cadTipoObj.setBounds(50, 100, 1100, 500);
                    cadTipoObj.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(cadTipoObj);
                    tela.revalidate();
                    tela.repaint();
          
                 }
             });
             
             cad_obj.addActionListener(new ActionListener(){
                
            	public void actionPerformed(ActionEvent e){
                     
                    removeTudo();
                    
                    cadObj =  new CadObj();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    cadObj.setBounds(50, 100, 1100, 500);
                    cadObj.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(cadObj);
                    tela.revalidate();
                    tela.repaint();
          
                 }
             });
             
             cad_cond_pag.addActionListener(new ActionListener(){
                
            	public void actionPerformed(ActionEvent e){
                     
                    
                    removeTudo();
                    
                    ccp =  new CadCondPag();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    ccp.setBounds(50, 100, 1100, 500);
                    ccp.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(ccp);
                    tela.revalidate();
                    tela.repaint();
          
                 }
             });
             
             cad_produto.addActionListener(new ActionListener(){
                
            	public void actionPerformed(ActionEvent e){
                     
                    
                    removeTudo();
                    
                    cadProduto =  new CadProduto();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    cadProduto.setBounds(50, 100, 1100, 500);
                    cadProduto.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(cadProduto);
                    tela.revalidate();
                    tela.repaint();
          
                 }
             });
             
             entSaida.addActionListener(new ActionListener(){
                
            	public void actionPerformed(ActionEvent e){
                     
                    
                    removeTudo();
                    
                    es =  new entradaSaida();
                    JToolBar ferramentas = new JToolBar("Ferramentas");
                    ferramentas.setBounds(2, 584, 1095, 64);
                    es.setBounds(50, 100, 1100, 500);
                    es.add(ferramentas);
                   
                    controleObjetos.setEnabled(true);
                    movimentacaoDiaria.setEnabled(true);
                    painel_controle.setEnabled(true);
                    
                    tela.add(es);
                    tela.revalidate();
                    tela.repaint();
          
                 }
             });
            
             
             sair.addActionListener(new ActionListener(){
                   @Override
            	public void actionPerformed(ActionEvent e){

                    int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente sair?");
                    if(resposta==0){
                     
                        System.exit(0);   
                    }                    
            	}
            });
               
            
            revalidate();
            repaint();
            //sendEmail();
            
        }
       
    
                public static void main(String args[]) throws EmailException, UnknownHostException{

                        Principal pri = new Principal();
                        pri.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }

       private void criaMenu(){
           
            menu = new JToolBar("Barra de Ferramentas");
            menu.setBackground(Color.white);
            
            controleObjetos= new JButton();
            controleObjetos.setIcon(imageControleObjeto);
            controleObjetos.setToolTipText("Aluguel/Reservas de Objetos"); 
            controleObjetos.setBounds(10, 10, 100, 100);
            menu.add(controleObjetos);
            
           /* aluguelCarro= new JButton();
            aluguelCarro.setIcon(imageAlugCarro);
            aluguelCarro.setToolTipText("Aluguel de Carros"); 
            aluguelCarro.setBounds(10, 10, 100, 100);
            menu.add(aluguelCarro);
            
            aluguelApt = new JButton();
            aluguelApt.setIcon(imageAlugApt);
            aluguelApt.setToolTipText("Aluguel de Apartamentos"); 
            aluguelApt.setBounds(10, 10, 100, 100);
            menu.add(aluguelApt);*/
            
            painel_controle = new JButton();
            painel_controle.setIcon(imagePainel_controle);
            painel_controle.setToolTipText("Painel de Controle"); 
            painel_controle.setBounds(10, 10, 100, 100);
            menu.add(painel_controle);
            
            movimentacaoDiaria = new JButton();
            movimentacaoDiaria.setIcon(imageFuncionario);
            movimentacaoDiaria.setToolTipText("Movimentação"); 
            movimentacaoDiaria.setBounds(10, 10, 100, 100);
            menu.add(movimentacaoDiaria);
                      
          
            
            sair = new JButton();
            sair.setIcon(imageSair);
            sair.setToolTipText("Sair"); 
            sair.setBounds(10, 10, 100, 100);
            menu.add(sair);
       }
       
       public void removeTudo(){
           
                    try{  
                      
                    tela.remove(mov);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(controleObj);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(cadObj);
                    }catch(NullPointerException npe){};
                                    
                    try{  
                      
                    tela.remove(cadTipoObj);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(cadProduto);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(re);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(ae);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(cl);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(cbrad);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(ccp);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(pc);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(cst);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(cdin);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(cpris);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(cloc);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(cbb);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(co);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(res);
                    }catch(NullPointerException npe){};
                    
                    try{  
                      
                    tela.remove(es);
                    }catch(NullPointerException npe){};
                    
           
                    
       }
      
 public static String paraFormatoDinheiro(Double valor){

           NumberFormat formato2 = NumberFormat.getInstance();
           return formato2.format(valor);
           
        }
 
 public static String formatoParaInserir(String valor) throws ParseException{
            
         NumberFormat formato2 = NumberFormat.getInstance();
         return formato2.parse(valor).toString();
            
        }
 
        public static String paraInserirData(String data){
            
            String retorno,dia,mes,ano;
            dia = data.substring(0,2);
            mes = data.substring(3,5);
            ano = data.substring(6,10);
            retorno = ano+mes+dia;
            return retorno;
            
        }
        public static String paraRecuperarData(String data){
            
            String retorno,dia,mes,ano;
            dia = data.substring(8,10);
            mes = data.substring(5,7);
            ano = data.substring(0,4);
            retorno = dia+"/"+mes+"/"+ano;
            return retorno;
            
        }
        
        public String dataInicioMes(String mes)    {  
    
         
            
            String dataInicioMes;  
            dataInicioMes = null;
  
            switch (mes)  
            {  

            case "Janeiro":  
            {  
              dataInicioMes = "01/01";  
              break;  
            }  
            case "Fevereiro":  
            {  
              dataInicioMes = "01/02";  
              break;  
            }  
            case "Março":  
            {  
              dataInicioMes = "01/03";  
              break;  
            }  
            case "Abril":  
            {  
              dataInicioMes = "01/04";  
              break;  
            } 
            case "Maio":  
            {  
              dataInicioMes = "01/05";  
              break;  
            } 
            case "Junho":  
            {  
              dataInicioMes = "01/06";  
              break;  
            }
            case "Julho":  
            {  
              dataInicioMes = "01/07";  
              break;  
            }
            case "Agosto":  
            {  
              dataInicioMes = "01/08";  
              break;  
            }
            case "Setembro":  
            {  
              dataInicioMes = "01/09";  
              break;  
            }
            case "Outubro":  
            {  
              dataInicioMes = "01/10";  
              break;  
            }    
            case "Novembro":  
            {  
              dataInicioMes = "01/11";  
              break;  
            }
            case "Dezembro":  
            {  
              dataInicioMes = "01/12";  
              break;  
            } 
            } 
            
            return dataInicioMes;

  } public String dataFimMes(String mes)    {  
    
         
            
            String dataFimMes;  
            dataFimMes = null;
  
            switch (mes)  
            {  

            case "Janeiro":  
            {  
              dataFimMes = "31/01";  
              break;  
            }  
            case "Fevereiro":  
            {  
              dataFimMes = "28/02";  
              break;  
            }  
            case "Março":  
            {  
              dataFimMes = "31/03";  
              break;  
            }  
            case "Abril":  
            {  
              dataFimMes = "30/04";  
              break;  
            } 
            case "Maio":  
            {  
              dataFimMes = "31/05";  
              break;  
            } 
            case "Junho":  
            {  
              dataFimMes = "30/06";  
              break;  
            }
            case "Julho":  
            {  
              dataFimMes = "31/07";  
              break;  
            }
            case "Agosto":  
            {  
              dataFimMes = "31/08";  
              break;  
            }
            case "Setembro":  
            {  
              dataFimMes = "30/09";  
              break;  
            }
            case "Outubro":  
            {  
              dataFimMes = "31/10";  
              break;  
            }    
            case "Novembro":  
            {  
              dataFimMes = "30/11";  
              break;  
            }
            case "Dezembro":  
            {  
              dataFimMes = "31/12";  
              break;  
            } 
            } 
            
            return dataFimMes;  

  } 
         
  

           public String pesquisarDiaSemana(int _diaSemana)    {  
    
         
            
            String diaSemana = null;  
  
            switch (_diaSemana)  
            {  

            case 1:  
            {  
              diaSemana = "Domingo";  
              break;  
            }  
            case 2:  
            {  
              diaSemana = "Segunda";  
              break;  
            }  
            case 3:  
            {  
              diaSemana = "Terça";  
              break;  
            }  
            case 4:  
            {  
              diaSemana = "Quarta";  
              break;  
            }  
            case 5:  
            {  
              diaSemana = "Quinta";  
              break;  
            }  
            case 6:  
            {  
              diaSemana = "Sexta";  
              break;  
            }  
            case 7:  
            {  
              diaSemana = "Sábado";  
              break;  
            }  

            }  
            return diaSemana;  

  }  
           
           public String dataAtual(){
               
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
               
                    return data_hoje;
           }
           
           public static void sendEmail(String emailPara,String nomePara, String assunto, String mensagem) throws EmailException, UnknownHostException {
    
                String nome =InetAddress.getLocalHost().getHostName();
                SimpleEmail email = new SimpleEmail();
                //Utilize o hostname do seu provedor de email
                //System.out.println("alterando hostname...");
                email.setHostName("smtp.gmail.com");
                //Quando a porta utilizada não é a padrão (gmail = 465)
                email.setSmtpPort(465);
                //Adicione os destinatários
                email.addTo("celiobj@gmail.com", "Célio");
                //Configure o seu email do qual enviará
                email.setFrom("cbjsolutions@gmail.com", "CBJ Solutions");
                //Adicione um assunto
                email.setSubject("Acesso ao Sistema pela máquina "+nome+".");
                //Adicione a mensagem do email
                email.setMsg("A máquina "+nome+" acessou o sistema 'Controle Fácil' licenciado para LOCASUL LOCADORA em "+ data_hoje+".");
                //Para autenticar no servidor é necessário chamar os dois métodos abaixo
                //System.out.println("autenticando...");
                email.setSSL(true);
                email.setAuthentication("cbjsolutions", "slipclown");
                //System.out.println("enviando...");
                email.send();
                //System.out.println("Email enviado!");
}

       

       }
