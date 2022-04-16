/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Classes.Usuario;
import DAO.AccessDatabase;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author celio
 */
public class RepositorioUsuario implements RepositorioUsuarioInterface{

    public void adcionar(Usuario usu) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void alterar(Usuario usu) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String procurar(String nome,String senha) {

        try{

                AccessDatabase a = new AccessDatabase();
                Connection con = a.conectar();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM Usuarios WHERE usuario = '"+nome+"'");
                rs.next();
                Usuario usu = new Usuario();
                usu.setNome(rs.getString("usuario"));
                usu.setSenha(rs.getString("senha"));
                usu.setTipo(rs.getString("tipo"));
                if(usu.getSenha().equals(senha)){
                    con.close();
                    return "aceito";
                }else
                con.close();
                return "rejeitado" ;

            }catch(Exception t){
                System.out.println(t.getMessage());
                return null;

            }}

    public void remover(Usuario usu) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
