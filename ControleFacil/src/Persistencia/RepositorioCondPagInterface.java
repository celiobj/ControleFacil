/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Classes.CondicoesPagamento;
import java.util.Vector;
import javax.swing.JTable;

/**
 *
 * @author celio
 */
public interface RepositorioCondPagInterface {
    
    public void adicionar(CondicoesPagamento cond);
    public void alterar (CondicoesPagamento produto, CondicoesPagamento alterado);
    public JTable consultaTodos();
    public CondicoesPagamento procurar(String cod);
    public Vector<CondicoesPagamento> todas();
}
