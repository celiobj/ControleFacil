/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Classes.Emprestimo;
import javax.swing.JTable;



/**
 *
 * @author celio
 */
public interface RepositorioEmprestimoInterface {

    public void adicionar(Emprestimo emp);
    public Emprestimo paraAlterar(String cod);
    public void Alterar(String cod, Emprestimo emp);
    public void AlterarTudo(String cod, Emprestimo emp);
    public long gerarCodigo ();
    public double renovar(String cod, String nome, String saldo, String valorEmprestado, String valorPago, String data_emp , String data_pag, String juros, String tipo, String pagamento, String garantia);
    public void finalizar(String cod,String nome, String valor,String data_fim, String pagamento, String garantia);
    public void resgatar(String cod, String nome, String valor,String juros,String data_emp, String dataPag, String garantia, String pagamento);
    public double somarProcurarAbertos();
    public JTable consultaPrincipal(String dataIni, String dataFim);
    public double somarConsultaPrincipal(String dataIni, String dataFim);
    public JTable procurar(String cod,String onde);
    public Emprestimo paraResgatar(String cod);
    public JTable procurarAbertos();
    public JTable procurarFinalizados();
    public JTable listarTodos();

}


