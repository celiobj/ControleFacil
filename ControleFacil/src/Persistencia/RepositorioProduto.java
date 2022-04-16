/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

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
public class RepositorioProduto implements RepositorioProdutoInterface{

    @Override
    public void adicionar(Produto produto) {
    
           try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("INSERT INTO `bd_controleFacil`.`produto` (`desc_produto`,`valor_compra`,`qtd_produto`,`fornecedor`) VALUES ('"+produto.getDescProduto()+"','"+produto.getValor()+"','"+produto.getQtd()+"','"+produto.getFornecedor()+"');");
                con.close();
                        
                 JOptionPane.showMessageDialog(null,"Produto cadastrado com sucesso!!!");
                        
                        
                       
                  
                }catch(Exception t){
                    
                
                    JOptionPane.showMessageDialog(null,"Registro não inserido!!! Inconsistência nos valores inseridos - "+t.getMessage());
                    
                  
                  }
        
    }

    @Override
    public Produto procurar(String cod_produto) {
    
        try{

                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controleFacil.produto WHERE idproduto = '"+cod_produto+"'");
                rs.next();
               
                Produto produto = new Produto();
                produto.setCodProduto(rs.getString("idproduto"));
                produto.setDescProduto(rs.getString("desc_produto"));
                Double valor = Double.parseDouble(rs.getString("valor_compra"));
                produto.setValor(Principal.paraFormatoDinheiro(valor));
                produto.setQtd(rs.getString("qtd_produto"));
                produto.setFornecedor(rs.getString("fornecedor"));
                
               
                
                con.close();
                return produto ;

            }catch(Exception t){
                System.out.println(t.getMessage());
                return null;

            }
        
    }

    @Override
    public void alterar(Produto produto, Produto alterado) {
    
        try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("UPDATE `bd_controlefacil`.`produto` SET `desc_produto`='"+alterado.getDescProduto()+"', `valor_compra`='"+alterado.getValor()+"', `qtd_produto`='"+produto.getQtd()+"', `fornecedor`='"+alterado.getFornecedor()+"'    WHERE `idproduto`='"+produto.getCodProduto()+"'");
                con.close();
                        
                 JOptionPane.showMessageDialog(null,"Produto alterado com sucesso!!!");
                        
                        
                       
                  
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
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controleFacil.produto ORDER BY idproduto");
                rs.next();
                ResultSetMetaData rsmd = rs.getMetaData();
                cabecalho.addElement("Cód");
                cabecalho.addElement("Descrição");
                cabecalho.addElement("Valor");
                cabecalho.addElement("Quantidade");
                cabecalho.addElement("Fornecedor");
                do {
                    linhas.addElement(proximaLinha(rs, rsmd));
                } while (rs.next());
                tabela = new JTable(linhas, cabecalho);
            }
                tabela.getColumnModel().getColumn(0).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(250);
                tabela.getColumnModel().getColumn(2).setPreferredWidth(300);
                tabela.getColumnModel().getColumn(3).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(4).setPreferredWidth(300);
                return tabela;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}
        
    }
    
    @Override
    public Vector<Produto> todos() {
         
          Vector retorno = new Vector();
          retorno.addElement(" ");
          Produto pro;
          try{

              
                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bd_controlefacil.produto  ORDER BY idproduto LIMIT 0, 1000");
                while(rs.next()){
                  pro = new Produto();
                  
                  pro.setCodProduto(rs.getString("idproduto"));
                  pro.setDescProduto(rs.getString("desc_produto"));
                  pro.setValor(rs.getString("valor_compra"));
                  pro.setQtd(rs.getString("qtd_produto"));
                  pro.setFornecedor(rs.getString("fornecedor"));
                  
                  retorno.addElement(pro);
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

    @Override
    public void comprar(Produto produto, int qtd) {
        
        try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("UPDATE `bd_controlefacil`.`produto` SET `qtd_produto`='"+qtd+"' WHERE `idproduto`='"+produto.getCodProduto()+"';");
                con.close();
                        
                 JOptionPane.showMessageDialog(null,"Entrada de produto feita com sucesso!!!");
                        
                        
                       
                  
                }catch(Exception t){
                    
                
                    JOptionPane.showMessageDialog(null,"Registro não inserido!!! Inconsistência nos valores inseridos - "+t.getMessage());
                    
                  
                  }
    
        
        
    }

    @Override
    public void vender(Produto produto, int qtd) {
    
           
        try{
             	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("UPDATE `bd_controlefacil`.`produto` SET `qtd_produto`='"+qtd+"' WHERE `idproduto`='"+produto.getCodProduto()+"';");
                con.close();
                        
                 JOptionPane.showMessageDialog(null,"Saída de produto feita com sucesso!!!");
                        
                        
                       
                  
                }catch(Exception t){
                    
                
                    JOptionPane.showMessageDialog(null,"Registro não inserido!!! Inconsistência nos valores inseridos - "+t.getMessage());
                    
                  
                  }
    
        
     
        
    }
    
}
