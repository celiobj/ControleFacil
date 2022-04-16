/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Classes.TipoObj;
import javax.swing.JTable;

/**
 *
 * @author celio
 */
public interface RepositorioTipoObjInterface {
    
    public void adicionar(TipoObj tipo);
    public void alterar(TipoObj novo, TipoObj antigo);
    public JTable consultaTodos();
    public TipoObj procurar(String cod);
}
