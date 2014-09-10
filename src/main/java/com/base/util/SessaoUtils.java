package com.base.util;

import com.base.modelo.controleacesso.Usuario;
import com.xpert.faces.utils.FacesUtils;
import javax.faces.context.FacesContext;

/**
 * Classe utilitaria para acesso de dados da sessao
 * 
 * @author Ayslan
 */
public class SessaoUtils {

    /**
     * 
     * @return o Usuario logado na aplicacao, ou seja o usuario setado no #{sessaoUsuarioMB.user}
     */
    public static Usuario getUser() {
        if (FacesContext.getCurrentInstance() != null) {
            return (Usuario) FacesUtils.getBeanByEl("#{sessaoUsuarioMB.user}");
        }
        return null;
    }

}
