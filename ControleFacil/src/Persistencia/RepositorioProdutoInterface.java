/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Classes.Produto;
import java.util.Vector;
import javax.swing.JTable;

/**
 *
 * @author celio
 */
public interface RepositorioProdutoInterface {
    
    public void adicionar(Produto produto);
    public void comprar(Produto produto, int qtd);
    public void vender(Produto produto, int qtd);
    public void alterar (Produto produto, Produto alterado);
    public JTable consultaTodos();
    public Produto procurar(String codProduto);
    public Vector<Produto> todos();
    
}
