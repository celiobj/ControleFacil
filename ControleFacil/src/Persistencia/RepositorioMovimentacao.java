/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;


import Classes.Movimentacao;
import DAO.AccessDatabase;
import GUI.Principal;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author note
 */
public class RepositorioMovimentacao implements RepositorioMovimentacaoInterface{
    
    
    
    @Override
    public void adicionar(Movimentacao mov){
       
        
        
         try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("INSERT INTO `bd_controleFacil`.`movimentacao` (`data`,`tipo`,`valor`,`descricao`) VALUES ('"+mov.getData()+"','"+mov.getTipo()+"','"+mov.getValor()+"','"+mov.getDescricao()+"');");
                con.close();
                        
                 //JOptionPane.showMessageDialog(null,"Registro efetuado com sucesso!!!");
                        
                        
                       
                  
                }catch(Exception t){
                    
                
                    JOptionPane.showMessageDialog(null,"Registro não inserido!!! Inconsistência nos valores inseridos - "+t.getMessage());
                   
                  }
        
    }
    
    public void remover(String cod){
       
        
        
         try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("Delete from `bd_controleFacil`.`movimentacao` where idmovimentacao='"+cod+"'");
                con.close();
                        
                 //JOptionPane.showMessageDialog(null,"Registro efetuado com sucesso!!!");
                        
                        
                       
                  
                }catch(Exception t){
                    
                
                    JOptionPane.showMessageDialog(null,"Registro não inserido!!! Inconsistência nos valores inseridos - "+t.getMessage());
                   
                  }
        
    }
    
      public String pegarUltimoCodigo() {
        String retorno;
          try{

              
                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select MAX(idmovimentacao) from bd_controleFacil.movimentacao;");
                rs.next();              
             
                 retorno = rs.getString(1);
                                              
                con.close();
                return retorno;

            }catch(Exception t){
                System.out.println(t.getMessage());
                return null;
        
    }
       
        
        
    }
    
       public String verificarMovimentacao(String data) {
       
        String retorno ;
          try{

              
                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select MAX(idmovimentacao) from bd_controleFacil.movimentacao where data = '"+data+"';");
                rs.next();              
             
                 retorno = rs.getString(1);
                                              
                con.close();
                return retorno;

            }catch(Exception t){
                System.out.println(t.getMessage());
                return null;
        
    }
       
        
        
    }
    
    
    public Vector<Movimentacao> listarMovimentacao(String data) {

        Vector linhas = new Vector();
        
         try{
          
            
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM bd_controleFacil.movimentacao where data ='"+data+"' ORDER BY idmovimentacao");
            while(rs.next()){
                Movimentacao mov = new Movimentacao();
                mov.setCodMovimentacao(rs.getString("idmovimentacao"));
                mov.setData(rs.getString("data"));
                mov.setTipo(rs.getString("tipo"));
                mov.setDescricao(rs.getString("descricao"));
                Double valor = Double.parseDouble(rs.getString("valor"));
                mov.setValor(Principal.paraFormatoDinheiro(valor));
                linhas.addElement(mov);
                
            };
            
                
                con.close();
               return linhas;

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