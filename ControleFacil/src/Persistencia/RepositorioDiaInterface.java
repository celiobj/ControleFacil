/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Classes.Dia;



/**
 *
 * @author celio
 */
public interface RepositorioDiaInterface {

    
    public void adicionar(Dia dia);
    public Dia pegarUltimo();
   
    

}
