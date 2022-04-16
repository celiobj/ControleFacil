/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Classes.Cliente;
import DAO.AccessDatabase;
import GUI.Principal;
import GUI.VerificarUsuario;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;
import javax.swing.JTable;

/**
 *
 * @author celio
 */
public class RepositorioCliente implements RepositorioClienteInterface{

    public void adcionar(Cliente cli) {
        try{

        				AccessDatabase a = new AccessDatabase();
        				Connection con = a.conectar();
                		Statement st = con.createStatement();
        				st.execute("INSERT INTO Clientes(cod_cliente,nome,end_res, end_trab, tel_res, tel_res2, tel_trab,tel_trab2, outros) VALUES('"+cli.getCod_cliente()+"','"+cli.getNome()+"','"+cli.getEnd_res()+"','"+cli.getEnd_trab()+"','"+cli.getTel_res()+"','"+cli.getTel_res2()+"','"+cli.getTel_trab()+"','"+cli.getTel_trab2()+"','"+cli.getOutros()+"')");
                        con.close();

        			}catch(Exception t){
        				System.out.println(t.getMessage());


        			}
    }

    public void alterar(String cod, Cliente cli) {
               
         try{

                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("UPDATE Clientes SET nome='"+cli.getNome()+"', end_res='"+cli.getEnd_res()+"', end_trab='"+cli.getEnd_trab()+"',tel_res='"+cli.getTel_res()+"',tel_res2 = '"+cli.getTel_res2()+"',tel_trab='"+cli.getTel_trab()+"', tel_trab2 = '"+cli.getTel_trab2()+"', outros='"+cli.getOutros()+"' WHERE cod_cliente = "+cod+"");
                rs.next();
                con.close();
             

            }catch(Exception t){
                System.out.println(t.getMessage());
                

            }


    }

    public Cliente procurar(String codnul) {
        try{

                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM Clientes WHERE cod_cliente = "+codnul+"");
                rs.next();
                Cliente cli = new Cliente();
                cli.setCod_cliente(codnul);
                cli.setNome(rs.getString("nome"));
                cli.setEnd_res(rs.getString("end_res"));
                cli.setEnd_trab(rs.getString("end_trab"));
                cli.setTel_res(rs.getString("tel_res"));
                cli.setTel_res2(rs.getString("tel_res2"));
                cli.setTel_trab(rs.getString("tel_trab"));
                cli.setTel_trab2(rs.getString("tel_trab2"));
                cli.setOutros(rs.getString("outros"));
                con.close();
                return cli;

            }catch(Exception t){
                System.out.println(t.getMessage());
                return null;

            }
    }

    public Cliente procurarNome(String nome) {
        try{
                
                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM Clientes WHERE nome = '"+nome+"'");
                rs.next();
                Cliente cli = new Cliente();
                cli.setNome(rs.getString("nome"));
                cli.setEnd_res(rs.getString("end_res"));
                cli.setEnd_trab(rs.getString("end_trab"));
                cli.setTel_res(rs.getString("tel_res"));
                cli.setTel_res2(rs.getString("tel_res2"));
                cli.setTel_trab(rs.getString("tel_trab"));
                cli.setTel_trab2(rs.getString("tel_trab2"));
                cli.setOutros(rs.getString("outros"));
                con.close();
                return cli;

            }catch(Exception t){
                System.out.println(t.getMessage());
                return null;

            }
    }

    public JTable listarTodos() {
        try{
                Vector cabecalho = new Vector();
                Vector linhas = new Vector();
                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT cod_cliente,nome,outros FROM Clientes");
                rs.next();

                ResultSetMetaData rsmd = rs.getMetaData();
                cabecalho.addElement("Código");
                cabecalho.addElement("Nome");
                cabecalho.addElement("Obs");

                        do {
                            linhas.addElement(proximaLinha(rs, rsmd));
                        } while (rs.next());

                        JTable tabela = new JTable(linhas, cabecalho);

                        con.close();
                        return tabela;


        			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}

    }

     public Cliente verificarExistente(String nome) {
         try{
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Clientes WHERE nome = ('"+nome+"')");
            rs.next();
            Cliente cli = new Cliente();
            try{
                cli.setCod_cliente(rs.getString("cod_cliente"));
                cli.setNome(rs.getString("nome"));
                con.close();
                return cli;

            }catch(Exception ez){
           
                con.close();
                cli.setNome("");
                return cli;
            }

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}
        
    }

    public JTable listarClientes(String ref) {
      

       try{
                Vector cabecalho = new Vector();
                Vector linhas = new Vector();
                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT cod_emp,nome,valor,juros,pagou,data_emp,data_pag,garantia,status,pagamento,valor_rec FROM Emprestimos WHERE nome=('"+ref+"') ORDER BY data_emp desc");
                rs.next();
                              
                ResultSetMetaData rsmd = rs.getMetaData();
                cabecalho.addElement("Código");
                cabecalho.addElement("Nome");
                cabecalho.addElement("Valor");
                cabecalho.addElement("Juros");
                cabecalho.addElement("Pagou");
                cabecalho.addElement("Data");
                cabecalho.addElement("Bom P/");
                cabecalho.addElement("Garantia");
                cabecalho.addElement("Status");
                cabecalho.addElement("Pagamento");
                cabecalho.addElement("Saldo");
                        
                        do {
                            linhas.addElement(proximaLinha(rs, rsmd));
                        } while (rs.next());
                        
                        JTable tabela = new JTable(linhas, cabecalho);

                        con.close();
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
