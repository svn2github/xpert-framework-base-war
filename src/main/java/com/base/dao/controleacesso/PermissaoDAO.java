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

    public List<Permissao> getTodasPermissoes();

    public List<Permissao> getPermissoes(Perfil perfil);

    public List<Permissao> getPermissoes(Usuario usuario);

    public List<Permissao> getPermissoesMenu(Perfil perfil);

    public List<Permissao> getPermissoesAtalhos(Usuario usuario);
}
