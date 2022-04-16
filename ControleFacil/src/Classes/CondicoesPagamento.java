/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author celio
 */
public class CondicoesPagamento {
    
    private String cod_CondPag;
    private String descricao;
    private String parcelas;

    /**
     * @return the cod_CondPag
     */
    public String getCod_CondPag() {
        return cod_CondPag;
    }

    /**
     * @param cod_CondPag the cod_CondPag to set
     */
    public void setCod_CondPag(String cod_CondPag) {
        this.cod_CondPag = cod_CondPag;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the parcelas
     */
    public String getParcelas() {
        return parcelas;
    }

    /**
     * @param parcelas the parcelas to set
     */
    public void setParcelas(String parcelas) {
        this.parcelas = parcelas;
    }
    
}
