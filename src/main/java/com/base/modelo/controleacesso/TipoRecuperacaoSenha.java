package com.base.modelo.controleacesso;

/**
 *
 * @author ayslan
 */
public enum TipoRecuperacaoSenha {

    NOVO_USUARIO("Novo Usuário"), ESQUECI_SENHA("Esqueci Senha");
    private String descricao;

    private TipoRecuperacaoSenha(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
