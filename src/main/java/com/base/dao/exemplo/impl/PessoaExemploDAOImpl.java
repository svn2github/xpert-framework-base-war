package com.base.dao.exemplo.impl;

import com.base.application.BaseDAOImpl;
import com.base.dao.exemplo.PessoaExemploDAO;
import com.base.modelo.exemplo.PessoaExemplo;
import javax.ejb.Stateless;

/**
 *
 * @author ayslan
 */
@Stateless
public class PessoaExemploDAOImpl extends BaseDAOImpl<PessoaExemplo> implements PessoaExemploDAO {

    @Override
    public Class getEntityClass() {
        return PessoaExemplo.class;
    }

}
