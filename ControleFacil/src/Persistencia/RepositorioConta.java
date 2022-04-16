/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Classes.Conta;
import DAO.AccessDatabase;
import GUI.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.JTable;

/**
 *
 * @author celio
 */


public class RepositorioConta implements RepositorioContaInterface{

    int dia, dia_semana, mes, ano;
    public static String data_hoje; 
    Calendar data;
    
    private String dataAtual(){
        
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
                    return Principal.paraInserirData(data_hoje);
        
    }
       
    public void adicionar(Conta co) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double sacar(String op,String valorTexto, String onde,String tipo) {
        double valor = Double.parseDouble(valorTexto);
        
                    

         try{
        		AccessDatabase a = new AccessDatabase();
        		Connection con = a.conectar();
                	Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("SELECT * FROM "+onde+" WHERE (numero) =(SELECT Max(numero) FROM "+onde+")");
                        rs.next();
                        double saldoAnt = Double.parseDouble(rs.getString("saldo"));
                        double saldo = saldoAnt - valor;
                        String saldoTexto = String.valueOf(saldo);
                   	st.executeUpdate("INSERT INTO "+onde+"(operacao,valor,data,saldo, finalidade) VALUES('"+op+"',"+valorTexto+",'"+dataAtual()+"',"+saldoTexto+",'"+tipo+"')");
                        con.close();
                        return saldo;

        			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return 0;

        			}
        
    }

    public double depositar(String op,String valorTexto, String onde, String tipo) {
        double valor = Double.parseDouble(valorTexto);
       try{
                        AccessDatabase a = new AccessDatabase();
        		Connection con = a.conectar();
                	Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("SELECT * FROM "+onde+" WHERE (numero) =(SELECT Max(numero) FROM "+onde+")");
                        rs.next();
                        double saldoAnt = Double.parseDouble(rs.getString("saldo"));
                        double saldo = saldoAnt + valor;
                        String saldoTexto = String.valueOf(saldo);
                   		st.executeUpdate("INSERT INTO "+onde+"(operacao,valor,data,saldo, finalidade) VALUES('"+op+"',"+valorTexto+",'"+dataAtual()+"',"+saldoTexto+",'"+tipo+"')");
                        con.close();
                        return saldo;

        			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return 0;

        			}

    }

    public double transferir(String de,String saldoDeTexto, String para, String saldoParaTexto, String valorTexto, String destino,String fonte) {

                double saldoDe = Double.parseDouble(saldoDeTexto);
                double saldoPara = Double.parseDouble(saldoParaTexto);
                double valor = Double.parseDouble(valorTexto);
        try{
        		AccessDatabase a = new AccessDatabase();
        		Connection con = a.conectar();
                	Statement st = con.createStatement();
                        double novoSaldoct1Doub = saldoDe - valor;
                        String novoSaldoct1 = String.valueOf(novoSaldoct1Doub);
                        double novoSaldoct2Doub = saldoPara + valor;
                        String novoSaldoct2 = String.valueOf(novoSaldoct2Doub);

                        st.executeUpdate("INSERT INTO "+de+"(operacao,valor,data,saldo, finalidade) VALUES('Débito',"+valorTexto+",'"+dataAtual()+"',"+novoSaldoct1+",'Transferiu p/ "+destino+"')");
                        st.executeUpdate("INSERT INTO "+para+"(operacao,valor,data,saldo, finalidade) VALUES('Credito',"+valorTexto+",'"+dataAtual()+"',"+novoSaldoct2+",'Recebeu de "+fonte+"')");

                        con.close();
                        return novoSaldoct1Doub;

        			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return 0;

        			}
    }

    public Conta procurar(String qual) {

         try{
        		AccessDatabase a = new AccessDatabase();
        		Connection con = a.conectar();
                	Statement st = con.createStatement();
        		ResultSet rs = st.executeQuery("SELECT * FROM "+qual+" WHERE (numero) =(SELECT Max(numero) FROM "+qual+")");
                        rs.next();
                        Conta co = new Conta();
                        co.setSaldo(rs.getString("saldo"));
                        con.close();
                        return co;

        			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}
        
    }

     public double saldo(String onde) {
        try{
        		AccessDatabase a = new AccessDatabase();
        		Connection con = a.conectar();
                	Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("SELECT * FROM "+onde+" WHERE (numero) =(SELECT Max(numero) FROM "+onde+")");
                        rs.next();
                        double saldo = Double.parseDouble(rs.getString("saldo"));
                        con.close();
                        return saldo;

        			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return 0;

        			}
    }

      public double emprestar(String op,String valorTexto, String onde, String tipo, String data) {

          double valor = Double.parseDouble(valorTexto);
         
          String dataForm = Principal.paraInserirData(data);
         

           try{
        		AccessDatabase a = new AccessDatabase();
        		Connection con = a.conectar();
                	Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("SELECT * FROM "+onde+" WHERE (numero) =(SELECT Max(numero) FROM "+onde+")");
                        rs.next();
                        double saldoAnt = Double.parseDouble(rs.getString("saldo"));
                        double saldo = saldoAnt - valor;
                        String saldoTexto = String.valueOf(saldo);
                        st.executeUpdate("INSERT INTO "+onde+"(operacao,valor,data,saldo, finalidade) VALUES('"+op+"',"+valorTexto+",'"+dataForm+"',"+saldoTexto+",'"+tipo+"')");
                        con.close();
                        return saldo;

        			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return 0;

        			}

    }

    public double receber(String op,String valorTexto, String onde, String tipo, String data) {

        double valor = Double.parseDouble(valorTexto);
        
        String dataForm = Principal.paraInserirData(data);
        
        try{
        		AccessDatabase a = new AccessDatabase();
        		Connection con = a.conectar();
                	Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("SELECT * FROM "+onde+" WHERE (numero) =(SELECT Max(numero) FROM "+onde+")");
                        rs.next();
                        double saldoAnt = Double.parseDouble(rs.getString("saldo"));
                        double saldo = saldoAnt + valor;
                        String saldoTexto = String.valueOf(saldo);
                        st.executeUpdate("INSERT INTO "+onde+"(operacao,valor,data,saldo, finalidade) VALUES('"+op+"',"+valorTexto+",'"+dataForm+"',"+saldoTexto+",'"+tipo+"')");
                        con.close();
                        return saldo;

        			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return 0;

        			}

    }

    public double consultarEmprestado(String onde, String data_ini, String data_fim) {

            
            String dataIniForm =Principal.paraInserirData(data_ini);

            
            String dataFimForm = Principal.paraInserirData(data_fim);


            try{
                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT SUM(valor) FROM "+onde+" WHERE (operacao)= 'Emprestar' And #"+dataIniForm+"# <=(data) And (data)<=#"+dataFimForm+"#");
                rs.next();
                double emprestado = rs.getDouble(1);
                con.close();
                return emprestado;

        			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return 0;

        			}

    }

    public double consultarRecebido(String onde, String data_ini, String data_fim) {

            
        
        String dataIniForm =Principal.paraInserirData(data_ini);

        String dataFimForm = Principal.paraInserirData(data_fim);
    try{


        		AccessDatabase a = new AccessDatabase();
        		Connection con = a.conectar();
                	Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("SELECT SUM(valor) FROM "+onde+" WHERE (operacao)= 'Receber' And #"+dataIniForm+"# <=(data) And (data)<=#"+dataFimForm+"#");
                        rs.next();
                        double recebido = rs.getDouble(1);

                        con.close();
                        return recebido;

        			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return 0;

        			}

    }
    public JTable extrato(String conta) {
       try{
            Vector cabecalho = new Vector();
            Vector linhas = new Vector();
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT operacao,valor,data,saldo,finalidade  FROM "+conta+" WHERE operacao <>('ajuste') ORDER BY numero desc  ");
            rs.next();
            ResultSetMetaData rsmd = rs.getMetaData();
                 
                 cabecalho.addElement("Operação");
                 cabecalho.addElement("Valor");
                 cabecalho.addElement("Data");
                 cabecalho.addElement("Saldo");
                 cabecalho.addElement("Motivo");
                do {
                    linhas.addElement(proximaLinha(rs, rsmd));
                } while (rs.next());
                JTable tabela = new JTable(linhas, cabecalho);
                con.close();
                tabela.getColumnModel().getColumn(0).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(3).setPreferredWidth(97);
                tabela.getColumnModel().getColumn(4).setPreferredWidth(470);
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
                                    case Types.DATE: LinhaAtual.addElement(rs.getDate(i).toLocaleString().substring(0, 10));
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
