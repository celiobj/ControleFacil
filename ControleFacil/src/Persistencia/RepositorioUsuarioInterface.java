/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Classes.Usuario;

/**
 *
 * @author celio
 */
public interface RepositorioUsuarioInterface {

    public void adcionar(Usuario usu);
    public void alterar(Usuario usu);
    public String procurar(String nome, String senha);
    public void remover(Usuario usu);

}
