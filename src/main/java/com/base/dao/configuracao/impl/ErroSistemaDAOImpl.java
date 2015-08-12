package com.base.dao.configuracao.impl;

import com.base.application.BaseDAOImpl;
import com.base.dao.configuracao.ErroSistemaDAO;
import com.base.modelo.configuracao.ErroSistema;
import javax.ejb.Stateless;

/**
 *
 * @author Ayslan
 */
@Stateless
public class ErroSistemaDAOImpl extends BaseDAOImpl<ErroSistema> implements ErroSistemaDAO {

    @Override
    public Class getEntityClass() {
        return ErroSistema.class;
    }
    
    
    
}
