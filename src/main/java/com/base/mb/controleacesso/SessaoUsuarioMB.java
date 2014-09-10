package com.base.mb.controleacesso;

import com.base.bo.controleacesso.PermissaoBO;
import com.base.dao.controleacesso.PermissaoDAO;
import com.base.modelo.controleacesso.Permissao;
import com.base.modelo.controleacesso.SolicitacaoRecuperacaoSenha;
import com.base.modelo.controleacesso.Usuario;
import com.xpert.i18n.I18N;
import com.xpert.security.model.User;
import com.xpert.security.session.AbstractUserSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.el.MethodExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

/**
 * @ManagedBean que guarda o usuario logado e suas permissoess
 * 
 * @author ayslan
 */
@ManagedBean
@SessionScoped
public class SessaoUsuarioMB extends AbstractUserSession implements Serializable {

    @EJB
    private PermissaoDAO permissaoDAO;
    @EJB
    private PermissaoBO permissaoBO;
    private Usuario user;
    private List<Permissao> roles;
    private List<Permissao> atalhos;
    private MenuModel menuModel;
    private SolicitacaoRecuperacaoSenha solicitacaoRecuperacaoSenha;

    @Override
    public void createSession() {
        roles = permissaoBO.getPermissoes(user);
        atalhos = permissaoDAO.getPermissoesAtalhos(user);
        criarMenu();
        criarCaminhoPermissao();
        super.createSession();
        //iniciar lazy
        user.setPerfis(permissaoDAO.getInitialized(user.getPerfis()));
    }

    public void criarMenu() {
        menuModel = new DefaultMenuModel();
        //home
        MenuItem item = new MenuItem();
        item.setValue(I18N.get("menu.home"));
        item.setIcon("ui-icon-home");
        item.setUrl("/view/home.jsf");
        menuModel.addMenuItem(item);


        Map<Permissao, Submenu> subMenuMap = new HashMap<Permissao, Submenu>();
        //urls dinamicas
        List<Permissao> permissoes = getRoles();
        if (permissoes != null && !permissoes.isEmpty()) {
            //primeiro "for" para adicionar os submenus
            for (Permissao permissao : permissoes) {
                putSubmenu(permissao, subMenuMap, menuModel);
            }

            //montar itens
            for (Permissao permissao : permissoes) {
                if (permissao.isPossuiMenu()) {
                    String url = permissao.getUrlMenuVerificado();
                    if (url != null && !url.trim().isEmpty()) {
                        Submenu submenu = null;
                        if (permissao.getPermissaoPai() != null) {
                            submenu = subMenuMap.get(permissao.getPermissaoPai());
                            //cadicionar o menu pai quando não encontrado
                            if (submenu == null) {
                                putSubmenu(permissao.getPermissaoPai(), subMenuMap, menuModel);
                                submenu = subMenuMap.get(permissao.getPermissaoPai());
                            }
                        }
                        item = new MenuItem();
                        item.setValue(permissao.getNomeMenuVerificado());
                        item.setUrl(permissao.getUrlMenuVerificado());
                        //adicionar ao submenu quando encontrado, senao adicionar ao root
                        if (submenu != null) {
                            submenu.getChildren().add(item);
                        } else {
                            menuModel.addMenuItem(item);
                        }
                    }
                }
            }
        }

        //sair
        item = new MenuItem();
        item.setValue(I18N.get("menu.sair"));
        item.setIcon("ui-icon-close");
        item.setActionExpression(getMethodExpressionSair());
        menuModel.addMenuItem(item);
    }

    public void putSubmenu(Permissao permissao, Map<Permissao, Submenu> subMenuMap, MenuModel menuModel) {
        if (permissao != null) {
            if (permissao.isPossuiMenu()) {
                String url = permissao.getUrlMenuVerificado();
                if (url == null || url.trim().isEmpty()) {
                    Submenu submenu = subMenuMap.get(permissao);
                    //caso a permissao tenha pai deve ser adicionado um submenu desse pai quando nao encontrado
                    if (submenu == null) {
                        submenu = new Submenu();
                        submenu.setLabel(permissao.getNomeMenuVerificado());
                        subMenuMap.put(permissao, submenu);
                        Submenu pai = null;
                        Permissao permissaoPai = permissaoDAO.getInitialized(permissao.getPermissaoPai());
                        if (permissaoPai != null) {
                            putSubmenu(permissaoPai, subMenuMap, menuModel);
                            pai = subMenuMap.get(permissaoPai);
                        }
                        //setar submenupai
                        if (pai != null) {
                            pai.getChildren().add(submenu);
                        } else {
                            //adicionar ao root
                            menuModel.addSubmenu(submenu);
                        }
                    }
                }
            }
        }
    }

    public void criarCaminhoPermissao() {
        permissaoBO.criarCaminhoPermissao(atalhos, false);
        permissaoBO.criarCaminhoPermissao(roles, true);
    }

    public List<Permissao> pesquisarPermissao(String query) {
        return permissaoBO.pesquisarPermissao(query, roles);
    }

    private MethodExpression getMethodExpressionSair() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createMethodExpression(facesContext.getELContext(), "#{loginMB.logout}", null, new Class[0]);
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    @Override
    public void setUser(User user) {
        this.user = (Usuario) user;
    }

    @Override
    public Usuario getUser() {
        return user;
    }

    @Override
    public List getRoles() {
        return roles;
    }

    public List<Permissao> getAtalhos() {
        return atalhos;
    }

    public void setAtalhos(List<Permissao> atalhos) {
        this.atalhos = atalhos;
    }

    public SolicitacaoRecuperacaoSenha getSolicitacaoRecuperacaoSenha() {
        return solicitacaoRecuperacaoSenha;
    }

    public void setSolicitacaoRecuperacaoSenha(SolicitacaoRecuperacaoSenha solicitacaoRecuperacaoSenha) {
        this.solicitacaoRecuperacaoSenha = solicitacaoRecuperacaoSenha;
    }
}
