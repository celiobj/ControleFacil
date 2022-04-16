/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Classes.CondicoesPagamento;
import Classes.Produto;
import DAO.AccessDatabase;
import GUI.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author celio
 */
public class RepositorioCondPag implements RepositorioCondPagInterface{

    @Override
    public void adicionar(CondicoesPagamento cond) {
    
           try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("INSERT INTO `bd_controleFacil`.`cond_pag` (`descricao`,`parcelas`) VALUES ('"+cond.getDescricao()+"','"+cond.getParcelas()+"');");
                con.close();
                        
                 JOptionPane.showMessageDialog(null,"Condição cadastrada com sucesso!!!");
                        
                        
                       
                  
                }catch(Exception t){
                    
                
                    JOptionPane.showMessageDialog(null,"Registro não inserido!!! Inconsistência nos valores inseridos - "+t.getMessage());
                    
                  
                  }
        
    }

    @Override
    public CondicoesPagamento procurar(String cod) {
    
        try{

                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controleFacil.cond_pag  WHERE idcondicoes_pagamento = '"+cod+"'");
                rs.next();
               
                CondicoesPagamento cond = new CondicoesPagamento();
                cond.setCod_CondPag(rs.getString("idcondicoes_pagamento"));
                cond.setDescricao(rs.getString("descricao"));
                cond.setParcelas(rs.getString("parcelas"));
                
               
                
                con.close();
                return cond ;

            }catch(Exception t){
                System.out.println(t.getMessage());
                return null;

            }
        
    }
    
    @Override
    public Vector<CondicoesPagamento> todas() {
    
          Vector retorno = new Vector();
          retorno.addElement(" ");
          CondicoesPagamento cond;
        try{

                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controleFacil.cond_pag");
                while(rs.next()){
               
                cond = new CondicoesPagamento();
                cond.setCod_CondPag(rs.getString("idcondicoes_pagamento"));
                cond.setDescricao(rs.getString("descricao"));
                cond.setParcelas(rs.getString("parcelas"));
                
               
                   retorno.addElement(cond);
                };
                    
                con.close();
                return retorno ;

            }catch(Exception t){
                System.out.println(t.getMessage());
                return null;

            }
        
    }
    
    

    @Override
    public void alterar(CondicoesPagamento antigo, CondicoesPagamento alterado) {
    
        try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("UPDATE `bd_controlefacil`.`cond_pag` SET `descricao`='"+alterado.getDescricao()+"', `parcelas`='"+alterado.getParcelas()+"' WHERE `idcondicoes_pagamento`='"+antigo.getCod_CondPag()+"'");
                con.close();
                        
                 JOptionPane.showMessageDialog(null,"Condição alterada com sucesso!!!");
                        
                        
                       
                  
                }catch(Exception t){
                    
                
                    JOptionPane.showMessageDialog(null,"Registro não inserido!!! Inconsistência nos valores inseridos - "+t.getMessage());
                    
                  
                  }
        
    }

    @Override
    public JTable consultaTodos() {
    
         try{
            Vector cabecalho = new Vector();
            Vector linhas = new Vector();
            AccessDatabase a = new AccessDatabase();
            JTable tabela;
            try (Connection con = a.conectar()) {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controleFacil.cond_pag ORDER BY idcondicoes_pagamento");
                rs.next();
                ResultSetMetaData rsmd = rs.getMetaData();
                cabecalho.addElement("Cód");
                cabecalho.addElement("Descrição");
                cabecalho.addElement("Parcelas");
                
                do {
                    linhas.addElement(proximaLinha(rs, rsmd));
                } while (rs.next());
                tabela = new JTable(linhas, cabecalho);
            }
                tabela.getColumnModel().getColumn(0).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(250);
                tabela.getColumnModel().getColumn(2).setPreferredWidth(300);
                
                return tabela;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}
        
    }
    
    
    
    private Vector proximaLinha(ResultSet rs, ResultSetMetaData rsmd) throws SQLException{
	      	   Vector LinhaAtual = new Vector();

	      	   try{
	      		   for (int i = 1; i <= rsmd.getColumnCount(); ++i){
                                switch(rsmd.getColumnType(i)){
                                    case Types.VARCHAR: LinhaAtual.addElement(rs.getString(i));
                                    break;
                                    case Types.TIMESTAMP: LinhaAtual.addElement(rs.getDate(i).toLocaleString().substring(0, 10));
                                    break;
                                    case Types.INTEGER: LinhaAtual.addElement(rs.getInt(i));
                                    break;
                                    case Types.DECIMAL: LinhaAtual.addElement(Principal.paraFormatoDinheiro(rs.getDouble(i)));
                                    break;
                                    case Types.DOUBLE: LinhaAtual.addElement(Principal.paraFormatoDinheiro(rs.getDouble(i)));
                                    break;
                                    

                                }
	      			   }
	      	   }
	      	   catch(SQLException e){
	      	   }
	      	   return LinhaAtual;


            }
    
}
