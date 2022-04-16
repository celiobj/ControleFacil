/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Classes.Objeto;
import java.util.Vector;
import javax.swing.JTable;

/**
 *
 * @author celio
 */
public interface RepositorioObjetoInterface {
    
    public void adicionar(Objeto obj);
    public void alterar(Objeto novo, Objeto antigo);
    public JTable consultaTodos();
    public Vector<Objeto> todos();
    public Objeto procurar(String cod);
    public Vector listarTipos();
    public JTable listarPorTipo(String cod);
   
   
}
