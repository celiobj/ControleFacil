/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Classes.Objeto;
import Classes.TipoObj;
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
public class RepositorioObjeto implements RepositorioObjetoInterface{

    @Override
    public void adicionar(Objeto obj) {
    
        try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("INSERT INTO `bd_controleFacil`.`objeto` (`idtipo_objeto`,`descricao`,`status`,`situacao`) VALUES ('"+obj.getCodTipoObj()+"','"+obj.getDescricao()+"','"+obj.getStatus()+"','"+obj.getSituacao()+"');");
                con.close();
                        
                 JOptionPane.showMessageDialog(null,"Objeto cadastrado com sucesso!!!");
                        
                        
                       
                  
                }catch(Exception t){
                    
                
                    JOptionPane.showMessageDialog(null,"Registro não inserido!!! Inconsistência nos valores inseridos - "+t.getMessage());
                    
                  
                  }
        
    }
    
    @Override
    public Objeto procurar(String cod) {
    
        try{

                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controleFacil.objeto WHERE idobjeto = '"+cod+"'");
                rs.next();
               
                Objeto obj = new Objeto();
                obj.setCodObj(rs.getString("idobjeto"));
                obj.setCodTipoObj(rs.getString("idtipo_objeto"));
                obj.setDescricao(rs.getString("descricao"));
                obj.setStatus(rs.getString("status"));
                obj.setSituacao(rs.getString("situacao"));
                               
                con.close();
                return obj ;

            }catch(Exception t){
                System.out.println(t.getMessage());
                return null;

            }
        
    }
    

       @Override
    public void alterar(Objeto novo, Objeto antigo) {
    
        try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("UPDATE `bd_controlefacil`.`objeto` SET `idtipo_objeto`='"+novo.getCodTipoObj()+"',`descricao`='"+novo.getDescricao()+"'`status`='"+novo.getStatus()+"' `situacao`='"+novo.getSituacao()+"' WHERE `idobjeto`='"+antigo.getCodObj()+"'");
                con.close();
                        
                 JOptionPane.showMessageDialog(null,"Objeto alterado com sucesso!!!");
                        
                        
                       
                  
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
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controleFacil.objeto ORDER BY idobjeto");
                rs.next();
                ResultSetMetaData rsmd = rs.getMetaData();
                cabecalho.addElement("Cód");
                cabecalho.addElement("Tipo");
                cabecalho.addElement("Descrição");
                cabecalho.addElement("Status");
                do {
                    linhas.addElement(proximaLinha(rs, rsmd));
                } while (rs.next());
                tabela = new JTable(linhas, cabecalho);
            }
                tabela.getColumnModel().getColumn(0).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(250);
                tabela.getColumnModel().getColumn(2).setPreferredWidth(250);
                tabela.getColumnModel().getColumn(3).setPreferredWidth(250);
                
                return tabela;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}
        
    }
    @Override
    public Vector listarTipos() {
        
        Vector retorno = new Vector();
        retorno.addElement(" ");
          try{

              
                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controlefacil.tipo_objeto LIMIT 0, 1000");
                while(rs.next()){
                  
                    retorno.addElement(rs.getString("idtipo_objeto"));
                };
                               
                con.close();
                return retorno;

            }catch(Exception t){
                System.out.println(t.getMessage());
                return null;
        
    }
        
    }
    @Override
    public JTable listarPorTipo(String cod) {
    
         try{
            Vector cabecalho = new Vector();
            Vector linhas = new Vector();
            AccessDatabase a = new AccessDatabase();
            JTable tabela;
            try (Connection con = a.conectar()) {
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controleFacil.objeto WHERE `idobjeto` ='"+cod+"' ORDER BY idobjeto");
                rs.next();
                ResultSetMetaData rsmd = rs.getMetaData();
                cabecalho.addElement("Cód");
                cabecalho.addElement("Tipo");
                cabecalho.addElement("Descrição");
                cabecalho.addElement("Status");
                do {
                    linhas.addElement(proximaLinha(rs, rsmd));
                } while (rs.next());
                tabela = new JTable(linhas, cabecalho);
            }
                tabela.getColumnModel().getColumn(0).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(250);
                tabela.getColumnModel().getColumn(2).setPreferredWidth(250);
                tabela.getColumnModel().getColumn(3).setPreferredWidth(250);
                
                return tabela;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}
        
    }
     @Override
    public Vector<Objeto> todos() {
         
          Vector retorno = new Vector();
          retorno.addElement(" ");
          Objeto obj;
          try{

              
                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controlefacil.objeto  ORDER BY idobjeto LIMIT 0, 1000");
                while(rs.next()){
                  obj = new Objeto();
                  
                  obj.setCodObj(rs.getString("idobjeto"));
                  obj.setCodTipoObj(rs.getString("idtipo_objeto"));
                  obj.setDescricao(rs.getString("descricao"));
                  obj.setStatus(rs.getString("status"));
                  
                  retorno.addElement(obj);
                };
                               
                con.close();
                return retorno;

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