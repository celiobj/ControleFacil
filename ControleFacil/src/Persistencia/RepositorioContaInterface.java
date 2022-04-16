/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Classes.Conta;
import javax.swing.JTable;

/**
 *
 * @author celio
 */
public interface RepositorioContaInterface {

    void adicionar(Conta co);
    double sacar(String op,String valor,String onde, String tipo);
    double emprestar(String op,String valor,String onde, String tipo,String data);
    double receber (String op,String valor,String onde,String tipo, String data);
    double depositar(String op,String valor,String onde,String tipo);
    double transferir(String de,String saldoDe, String para, String saldoPara, String valor, String destino, String fonte);
    JTable extrato (String conta);
    double saldo (String onde);
    double consultarEmprestado (String onde, String data_ini, String data_fim);
    double consultarRecebido (String onde, String data_ini, String data_fim);
    Conta procurar(String qual);

}
