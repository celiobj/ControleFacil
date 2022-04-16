/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author celio
 */
public class Produto {
    
    private String codProduto;
    private String descProduto;
    private String valor;
    private String qtd;
    private String fornecedor;

    /**
     * @return the codProduto
     */
    public String getCodProduto() {
        return codProduto;
    }

    /**
     * @param codProduto the codProduto to set
     */
    public void setCodProduto(String codProduto) {
        this.codProduto = codProduto;
    }

    /**
     * @return the descProduto
     */
    public String getDescProduto() {
        return descProduto;
    }

    /**
     * @param descProduto the descProduto to set
     */
    public void setDescProduto(String descProduto) {
        this.descProduto = descProduto;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * @return the qtd
     */
    public String getQtd() {
        return qtd;
    }

    /**
     * @param qtd the qtd to set
     */
    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    /**
     * @return the fornecedor
     */
    public String getFornecedor() {
        return fornecedor;
    }

    /**
     * @param fornecedor the fornecedor to set
     */
    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }
    
}
