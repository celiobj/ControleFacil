/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

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
public class RepositorioTipoObj implements RepositorioTipoObjInterface{

    @Override
    public void adicionar(TipoObj tipo) {
    
        try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("INSERT INTO `bd_controleFacil`.`tipo_objeto` (`tipo`) VALUES ('"+tipo.getTipo()+"');");
                con.close();
                        
                 JOptionPane.showMessageDialog(null,"Tipo cadastrado com sucesso!!!");
                        
                        
                       
                  
                }catch(Exception t){
                    
                
                    JOptionPane.showMessageDialog(null,"Registro não inserido!!! Inconsistência nos valores inseridos - "+t.getMessage());
                    
                  
                  }
        
    }
    
    @Override
    public TipoObj procurar(String cod) {
    
        try{

                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controleFacil.tipo_objeto WHERE idtipo_objeto = '"+cod+"'");
                rs.next();
               
                TipoObj tipo = new TipoObj();
                tipo.setCodTipoObj(rs.getString("idtipo_objeto"));
                tipo.setTipo(rs.getString("tipo"));
                               
                con.close();
                return tipo ;

            }catch(Exception t){
                System.out.println(t.getMessage());
                return null;

            }
        
    }
    

       @Override
    public void alterar(TipoObj novo, TipoObj antigo) {
    
        try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("UPDATE `bd_controlefacil`.`tipo_objeto` SET `tipo`='"+novo.getTipo()+"' WHERE `idtipo_objeto`='"+antigo.getCodTipoObj()+"'");
                con.close();
                        
                 JOptionPane.showMessageDialog(null,"Tipo alterado com sucesso!!!");
                        
                        
                       
                  
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
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controleFacil.tipo_objeto ORDER BY idtipo_objeto");
                rs.next();
                ResultSetMetaData rsmd = rs.getMetaData();
                cabecalho.addElement("Cód");
                cabecalho.addElement("Tipo");
                do {
                    linhas.addElement(proximaLinha(rs, rsmd));
                } while (rs.next());
                tabela = new JTable(linhas, cabecalho);
            }
                tabela.getColumnModel().getColumn(0).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(250);
                
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