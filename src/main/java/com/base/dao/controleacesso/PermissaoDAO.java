package com.base.dao.controleacesso;

import com.base.modelo.controleacesso.Perfil;
import com.base.modelo.controleacesso.Permissao;
import com.base.modelo.controleacesso.Usuario;
import com.xpert.persistence.dao.BaseDAO;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.Query;

/**
 *
 * @author #Author
 */
@Local
public interface PermissaoDAO extends BaseDAO<Permissao> {

    public List<Permissao> getTodasPermissoesComFilhos();

    public List<Permissao> getPermissoesComFilhos(Perfil perfil);

    public List<Permissao> getPermissoesComFilhos(Usuario usuario);

    public List<Permissao> getPermissoesMenuComFilhos(Perfil perfil);

    public List<Permissao> getPermissoesAtalhos(Usuario usuario);
}
