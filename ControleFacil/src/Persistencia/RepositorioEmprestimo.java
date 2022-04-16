/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Classes.Emprestimo;
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
public class RepositorioEmprestimo implements RepositorioEmprestimoInterface{

     Calendar data = Calendar.getInstance();
     int dia = data.get(Calendar.DAY_OF_MONTH);
     int mes = data.get(Calendar.MONTH);
     int ano = data.get(Calendar.YEAR);
     String dataAtual = +dia+"/"+(mes+1)+"/"+ano;

    public void adicionar(Emprestimo emp) {
       try{
        	AccessDatabase a = new AccessDatabase();
        	Connection con = a.conectar();
                Statement st = con.createStatement();
        	st.execute("INSERT INTO Emprestimos (cod_emp,nome, valor,juros, pagou, data_emp, data_pag, garantia, status, valor_rec, pagamento) VALUES ('"+emp.getCod()+"','"+emp.getNome()+"',"+emp.getValor()+","+emp.getJuros()+","+emp.getPagou()+",'"+emp.getData_emp()+"','"+emp.getData_pag()+"','"+emp.getGarantia()+"','"+emp.getStatus()+"',"+emp.getValor_rec()+",'"+emp.getPagamento()+"')");
                st.execute("INSERT INTO Abertos ( cod_emp, nome,valor,juros, pagou, data_emp, data_pag, garantia, status, valor_rec, pagamento) VALUES ('"+emp.getCod()+"','"+emp.getNome()+"',"+emp.getValor()+","+emp.getJuros()+","+emp.getPagou()+",'"+emp.getData_emp()+"','"+emp.getData_pag()+"','"+emp.getGarantia()+"','"+emp.getStatus()+"',"+emp.getValor_rec()+",'"+emp.getPagamento()+"')");
                con.close();
                        
                         

        			}catch(Exception t){
        				System.out.println(t.getMessage());
                       

        			}
    }


    public double renovar(String cod, String nome, String saldoTexto, String valorEmprestadoTexto,String valorPagoTexto, String data_emp, String data_pag, String jurosTexto,String tipo, String pagamento, String garantia) {
    
        double saldo = Double.parseDouble(saldoTexto);
        double valorEmprestado = Double.parseDouble(valorEmprestadoTexto);
        double juros = Double.parseDouble(jurosTexto);
        double valorPago = Double.parseDouble(valorPagoTexto);
        double restaDoub = saldo + valorEmprestado + juros - valorPago;
        String resta = String.valueOf(restaDoub);
        String dataEmpForm = Principal.paraInserirData(data_emp);
        String dataPagForm = Principal.paraInserirData(data_pag);
        try{            
        				AccessDatabase a = new AccessDatabase();
        				Connection con = a.conectar();
                		Statement st = con.createStatement();
        				st.executeUpdate("DELETE FROM Abertos WHERE cod_emp = "+cod+"");
                                        st.executeUpdate("INSERT INTO Emprestimos ( cod_emp,nome, valor,juros, pagou, data_emp, data_pag, garantia, status,renov, valor_rec, pagamento) VALUES ('"+cod+"','"+nome+"',"+valorEmprestadoTexto+","+jurosTexto+","+valorPagoTexto+",'"+dataEmpForm+"','"+dataPagForm+"','"+garantia+"','"+tipo+"','Sim',"+resta+",'"+pagamento+"')");
                                        st.executeUpdate("INSERT INTO Abertos ( cod_emp,nome, valor,juros, pagou, data_emp, data_pag, garantia, status,renov, valor_rec, pagamento) VALUES ('"+cod+"','"+nome+"',"+valorEmprestadoTexto+","+jurosTexto+","+valorPagoTexto+",'"+dataEmpForm+"','"+dataPagForm+"','"+garantia+"','"+tipo+"','Sim',"+resta+",'"+pagamento+"')");
                                        con.close();

                        return restaDoub;

        			}catch(Exception t){
        				System.out.println(t.getMessage());

                        return 0;
        			}
    }

    public void finalizar(String cod,String nome, String valorTexto,String data_fin, String pagamento, String garantia) {

        
        try{           
        				AccessDatabase a = new AccessDatabase();
        				Connection con = a.conectar();
                		Statement st = con.createStatement();
        				st.executeUpdate("DELETE FROM Abertos WHERE cod_emp = "+cod+"");
                        st.executeUpdate("INSERT INTO Emprestimos ( cod_emp,nome, valor, pagou, data_emp, data_pag, garantia, status,renov, valor_rec, pagamento) VALUES ('"+cod+"','"+nome+"',"+valorTexto+","+valorTexto+",'"+data_fin+"','"+data_fin+"','"+garantia+"','Finalizado','Não','0','"+pagamento+"')");
                        st.executeUpdate("INSERT INTO Finalizados ( cod_emp,nome, valor_pag,data_pag,garantia,status, pagamento) VALUES ('"+cod+"','"+nome+"',"+valorTexto+",'"+data_fin+"','"+garantia+"','Finalizado','"+pagamento+"')");
                        con.close();


        			}catch(Exception t){
        				System.out.println(t.getMessage());


        			}
    }

    public void resgatar(String cod, String nome, String valorTexto,String jurosTexto,String dataEmp, String dataPag, String garantia, String pagamento) {
       
        double valor = Double.parseDouble(valorTexto);
        double juros = Double.parseDouble(jurosTexto);
        double restaDoub = valor + juros;
        String resta = String.valueOf(restaDoub);

        try{
        				AccessDatabase a = new AccessDatabase();
        				Connection con = a.conectar();
                		Statement st = con.createStatement();
        				st.executeUpdate("DELETE FROM Finalizados WHERE cod_emp = "+cod+"");
                        st.executeUpdate("INSERT INTO Emprestimos ( cod_emp,nome, valor, pagou, data_emp, data_pag, garantia, status,renov,juros, valor_rec,pagamento) VALUES ('"+cod+"','"+nome+"',"+valorTexto+",'0','"+dataEmp+"','"+dataPag+"','"+garantia+"','Aberto - resg','Não',"+jurosTexto+","+resta+",'"+pagamento+"')");
                        st.executeUpdate("INSERT INTO Abertos ( cod_emp,nome, valor, pagou, data_emp, data_pag, garantia, status, renov,juros, valor_rec, pagamento) VALUES ('"+cod+"','"+nome+"',"+valorTexto+",0,'"+dataEmp+"','"+dataPag+"','"+garantia+"','Aberto - resg','Não',"+jurosTexto+","+resta+",'"+pagamento+"')");
                        con.close();


        			}catch(Exception t){
        				System.out.println(t.getMessage());


        			}

        
    }

    public JTable listarTodos() {

         try{
            Vector cabecalho = new Vector();
            Vector linhas = new Vector();
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT cod_emp,nome,valor,pagou,juros,data_emp,data_pag,garantia,status,pagamento,valor_rec FROM Emprestimos WHERE (cod_emp)> 0 ORDER BY cod_emp desc");
            rs.next();
            ResultSetMetaData rsmd = rs.getMetaData();
                cabecalho.addElement("Código");
                cabecalho.addElement("Nome");
                cabecalho.addElement("Valor");
                cabecalho.addElement("Pagou");
                cabecalho.addElement("Juros");
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

   
    public JTable procurar(String cod, String onde) {
       try{
                    Vector cabecalho = new Vector();
                    Vector linhas = new Vector();
                    AccessDatabase a = new AccessDatabase();
                    Connection con = a.conectar();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT cod_emp, nome,data_emp, data_pag,garantia,status,valor,juros,pagou, valor_rec FROM "+onde+" WHERE cod_emp = ("+cod+")ORDER BY op desc");
                    rs.next();
                    ResultSetMetaData rsmd = rs.getMetaData();
                        cabecalho.addElement("Código");
                        cabecalho.addElement("Nome");
                        cabecalho.addElement("Data");
                        cabecalho.addElement("Bom P/");
                        cabecalho.addElement("Garantia");
                        cabecalho.addElement("Status");
                        cabecalho.addElement("Emprestei");
                        cabecalho.addElement("Juros");
                        cabecalho.addElement("Pagou");
                        cabecalho.addElement("Saldo");
                do {
                    linhas.addElement(proximaLinha(rs, rsmd));
                } while (rs.next());
                JTable tabela = new JTable(linhas, cabecalho);
                con.close();
                tabela.getColumnModel().getColumn(0).setPreferredWidth(70);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(350);
                tabela.getColumnModel().getColumn(2).setPreferredWidth(175);
                tabela.getColumnModel().getColumn(3).setPreferredWidth(175);
                tabela.getColumnModel().getColumn(4).setPreferredWidth(350);
                tabela.getColumnModel().getColumn(5).setPreferredWidth(200);
                tabela.getColumnModel().getColumn(6).setPreferredWidth(150);
                tabela.getColumnModel().getColumn(7).setPreferredWidth(150);
                tabela.getColumnModel().getColumn(8).setPreferredWidth(150);
                tabela.getColumnModel().getColumn(9).setPreferredWidth(150);
                return tabela;
               
                   }catch(Exception t){
                    System.out.println(t.getMessage());
                    return null;

                }
    }

    
    public JTable procurarAbertos() {
         try{
            Vector cabecalho = new Vector();
            Vector linhas = new Vector();
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT cod_emp, nome, data_pag,garantia, valor_rec  FROM Abertos ORDER BY data_pag,nome  ");
            rs.next();
            ResultSetMetaData rsmd = rs.getMetaData();
                 cabecalho.addElement("Código");
                 cabecalho.addElement("Nome");
                 cabecalho.addElement("Data do Pagamento");
                 cabecalho.addElement("Garantia");
                 cabecalho.addElement("Saldo");
                do {
                    linhas.addElement(proximaLinha(rs, rsmd));
                } while (rs.next());
                JTable tabela = new JTable(linhas, cabecalho);
                con.close();
                tabela.getColumnModel().getColumn(0).setPreferredWidth(70);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(450);
                tabela.getColumnModel().getColumn(2).setPreferredWidth(150);
                tabela.getColumnModel().getColumn(3).setPreferredWidth(350);
                tabela.getColumnModel().getColumn(4).setPreferredWidth(70);
                return tabela;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}
    }

    public Emprestimo paraResgatar(String cod) {
        try{
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Finalizados WHERE cod_emp = ("+cod+") ORDER BY data_pag  ");
            rs.next();
            Emprestimo emp = new Emprestimo();
            emp.setNome(rs.getString("nome"));
            emp.setCod(rs.getString("cod_emp"));
            

                con.close();
                return emp;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}
    }
     public Emprestimo paraAlterar(String cod) {
        try{
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Abertos WHERE cod_emp = ("+cod+")");
            rs.next();
            Emprestimo emp = new Emprestimo();
            emp.setCod(rs.getString("cod_emp"));
            emp.setNome(rs.getString("nome"));
            emp.setValor(rs.getString("valor"));
            emp.setJuros(rs.getString("juros"));
            emp.setPagou(rs.getString("pagou"));
            String dataEmpResult = rs.getString("data_emp");
            String diae = dataEmpResult.substring(8,10);
            String mese = dataEmpResult.substring(5,7);
            String anoe = dataEmpResult.substring(0,4);
            String dataEmp = diae+mese+anoe;
            emp.setData_emp(dataEmp);
            String dataPagResult = rs.getString("data_pag");
            String diap = dataPagResult.substring(8,10);
            String mesp = dataPagResult.substring(5,7);
            String anop = dataPagResult.substring(0,4);
            String dataPag = diap+mesp+anop;
            emp.setData_pag(dataPag);
            emp.setGarantia(rs.getString("garantia"));
            emp.setStatus(rs.getString("status"));
            emp.setRenov(rs.getString("renov"));
            emp.setValor_rec(rs.getString("valor_rec"));
            emp.setPagamento(rs.getString("pagamento"));
            
                con.close();
                return emp;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}
    }

    public JTable procurarFinalizados() {
       try{
            Vector cabecalho = new Vector();
            Vector linhas = new Vector();
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT cod_emp,nome,valor_pag,data_pag,garantia,pagamento  FROM Finalizados ORDER BY nome");
            rs.next();
            ResultSetMetaData rsmd = rs.getMetaData();

                 cabecalho.addElement("Código");
                 cabecalho.addElement("Nome");
                 cabecalho.addElement("Valor");
                 cabecalho.addElement("Data Pag");
                 cabecalho.addElement("Garantia");
                 cabecalho.addElement("Pagamento");
                 
                do {
                    linhas.addElement(proximaLinha(rs, rsmd));
                } while (rs.next());
                JTable tabela = new JTable(linhas, cabecalho);
                con.close();
                tabela.getColumnModel().getColumn(0).setPreferredWidth(70);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(350);
                tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
                tabela.getColumnModel().getColumn(3).setPreferredWidth(150);
                tabela.getColumnModel().getColumn(4).setPreferredWidth(200);
                tabela.getColumnModel().getColumn(5).setPreferredWidth(450);
               
                return tabela;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}
    }

     public JTable consultaPrincipal(String dataIni, String dataFim) {

            String diaIni = dataIni.substring(0,2);
            String mesIni = dataIni.substring(3,5);
            String anoIni = dataIni.substring(6,10);

            String dataIniForm = mesIni+"/"+diaIni+"/"+anoIni;

            String diaFim = dataFim.substring(0,2);
            String mesFim = dataFim.substring(3,5);
            String anoFim = dataFim.substring(6,10);

            String dataFimForm = mesFim+"/"+diaFim+"/"+anoFim;

            try{
            Vector cabecalho = new Vector();
            Vector linhas = new Vector();
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT cod_emp, nome, data_pag, valor_rec FROM Abertos WHERE data_pag >= #"+dataIniForm+"# AND data_pag <= #"+dataFimForm+"# ORDER BY data_pag");
            rs.next();
            ResultSetMetaData rsmd = rs.getMetaData();
                cabecalho.addElement("Código");
                 cabecalho.addElement("Nome");
                 cabecalho.addElement("Data do Pagamento");
                 cabecalho.addElement("Saldo");
                do {
                    linhas.addElement(proximaLinha(rs, rsmd));
                } while (rs.next());
                JTable tabela = new JTable(linhas, cabecalho);
                con.close();
                tabela.getColumnModel().getColumn(0).setPreferredWidth(70);
                tabela.getColumnModel().getColumn(1).setPreferredWidth(497);
                tabela.getColumnModel().getColumn(2).setPreferredWidth(150);
                tabela.getColumnModel().getColumn(3).setPreferredWidth(210);
                return tabela;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return null;

        			}
        
    }

     public double somarConsultaPrincipal(String dataIni, String dataFim) {

            String diaIni = dataIni.substring(0,2);
            String mesIni = dataIni.substring(3,5);
            String anoIni = dataIni.substring(6,10);

            String dataIniForm = mesIni+"/"+diaIni+"/"+anoIni;

            String diaFim = dataFim.substring(0,2);
            String mesFim = dataFim.substring(3,5);
            String anoFim = dataFim.substring(6,10);

            String dataFimForm = mesFim+"/"+diaFim+"/"+anoFim;

         try{
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT SUM(valor_rec) FROM Abertos WHERE data_pag >= #"+dataIniForm+"# AND data_pag <= #"+dataFimForm+"#");
          //ResultSet rs = st.executeQuery("SELECT SUM(valor_rec) FROM Abertos WHERE data_pag >= '"+dataIniForm+"' AND data_pag <= '"+dataFimForm+"'");
            rs.next();
            double soma = rs.getDouble(1);
            con.close();
                return soma;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return 0;

        			}  }

     public long gerarCodigo() {
         try{
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Emprestimos WHERE (cod_emp) =(SELECT Max(cod_emp) FROM Emprestimos)");
            rs.next();
            long cod_int = rs.getInt("cod_emp");
            long cod = (cod_int) + 1;
            con.close();

            return cod;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return 0;

        			}
    }


     public void Alterar(String cod, Emprestimo emp) {
        try{

                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                st.executeQuery("UPDATE Abertos SET nome='"+emp.getNome()+"',garantia='"+emp.getGarantia()+"' WHERE cod_emp = "+cod+"");
                con.close();


            }catch(Exception t){
                System.out.println(t.getMessage());


            }}
     public void AlterarTudo(String cod, Emprestimo emp) {
        try{

                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                st.executeQuery("UPDATE Emprestimos SET nome='"+emp.getNome()+"',garantia='"+emp.getGarantia()+"' WHERE op = (SELECT Max(op) FROM Emprestimos WHERE cod_emp = "+cod+" )");
                con.close();


            }catch(Exception t){
                System.out.println(t.getMessage());


            }}
    

    public double somarProcurarAbertos() {
         try{
            AccessDatabase a = new AccessDatabase();
            Connection con = a.conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT SUM(valor_rec) FROM Abertos");
            rs.next();
            double soma = rs.getDouble(1);
            con.close();
                return soma;

   			}catch(Exception t){
        				System.out.println(t.getMessage());
                        return 0;

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