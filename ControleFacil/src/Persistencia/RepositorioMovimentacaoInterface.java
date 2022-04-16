/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;


import Classes.Movimentacao;
import java.util.Vector;

/**
 *
 * @author note
 */
public interface RepositorioMovimentacaoInterface {
    
    public Vector<Movimentacao> listarMovimentacao(String data);
    public void adicionar (Movimentacao mov);
    public void remover (String cod);
    public String pegarUltimoCodigo();
    public String verificarMovimentacao(String data);
    
    
    
}
