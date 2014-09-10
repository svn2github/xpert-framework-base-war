package com.base.bo.controleacesso;

import com.base.dao.controleacesso.PermissaoDAO;
import com.base.modelo.controleacesso.Perfil;
import com.base.modelo.controleacesso.Permissao;
import com.base.modelo.controleacesso.Usuario;
import com.xpert.core.conversion.Conversion;
import com.xpert.core.crud.AbstractBusinessObject;
import com.xpert.persistence.dao.BaseDAO;
import com.xpert.core.validation.UniqueField;
import com.xpert.core.exception.BusinessException;
import com.xpert.core.validation.UniqueFields;
import com.xpert.utils.CollectionsUtils;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Ayslan
 */
@Stateless
public class PermissaoBO extends AbstractBusinessObject<Permissao> {

    @EJB
    private PermissaoDAO permissaoDAO;

    @Override
    public BaseDAO getDAO() {
        return permissaoDAO;
    }

    public TreeNode getTreeNodeMenu(Perfil perfil) {
        List<Permissao> permissoesPerfil = permissaoDAO.getPermissoesComFilhos(perfil);
        List<Permissao> permissoesMenu = null;
        if (perfil != null && perfil.getId() != null) {
            permissoesMenu = permissaoDAO.getPermissoesMenuComFilhos(perfil);
        }
        return getTreeNode(permissoesPerfil, permissoesMenu);
    }

    public TreeNode getTreeNode(Perfil perfil) {
        List<Permissao> todasPermissoes = permissaoDAO.listAll("descricao");
        List<Permissao> permissoesPerfil = null;
        if (perfil != null && perfil.getId() != null) {
            permissoesPerfil = permissaoDAO.getPermissoesComFilhos(perfil);
        }
        return getTreeNode(todasPermissoes, permissoesPerfil);
    }

   public void adicionarPermissaoAoMap(TreeNode root, Permissao permissao, Map<Permissao, TreeNode> nodeMap,
            List<Permissao> permissoes, List<Permissao> permissoesParaSelecionar, boolean selectable) {
        TreeNode node = new DefaultTreeNode(permissao, root);
        node.setExpanded(true);
        node.setSelectable(selectable);
        if (permissoesParaSelecionar != null && permissoesParaSelecionar.contains(permissao)) {
            node.setSelected(true);
        }
        nodeMap.put(permissao, node);
        if (permissao.getPermissaoPai() != null) {
            Permissao permissaoPai = permissaoDAO.getInitialized(permissao.getPermissaoPai());
            if (!permissoes.contains(permissaoPai) &&  nodeMap.get(permissaoPai) == null) {
                adicionarPermissaoAoMap(root, permissaoPai, nodeMap, permissoes, permissoesParaSelecionar, false);
            }
        }
    }

    /**
     * Método que carrega a arvore para uma lista de permissoes
     *
     * @param permissoes - as permissoes a serem exibidas
     * @param permissoesParaSelecionar - as permissoes que ficarao selecionadas
     * @return
     */
    public TreeNode getTreeNode(List<Permissao> permissoes, List<Permissao> permissoesParaSelecionar) {
        TreeNode root = new DefaultTreeNode();
        root.setExpanded(true);

        Map<Permissao, TreeNode> nodeMap = new LinkedHashMap<Permissao, TreeNode>();

        //criar nó para cada permissao
        for (Permissao permissao : permissoes) {
           adicionarPermissaoAoMap(root, permissao, nodeMap, permissoes, permissoesParaSelecionar, true);
        }
        for (Map.Entry<Permissao, TreeNode> entry : nodeMap.entrySet()) {

            Permissao permissao = entry.getKey();
            TreeNode node = entry.getValue();
            if (permissao.getPermissaoPai() != null) {
                TreeNode parent = nodeMap.get(permissao.getPermissaoPai());
                //selecionar apenas ate o segundo nivel
                if (parent != null && parent.isSelected()) {
                    node.setSelected(true);
                }
                node.setExpanded(false);
                if (parent != null) {
                    node.setParent(parent);
                } else {
                    node.setParent(root);
                }
            }
        }
        return root;
    }

    /**
     *
     * Retorna uma lista de permissoes a partir de um usuario. Caso seja um
     * superusuario retornar todas as permissoes. Casa seja nao seja
     * superusuario, retornar as permissoes dos perfis dele.
     *
     * @param usuario
     * @return
     */
    public List getPermissoes(Usuario usuario) {
        List<Permissao> permissoes = new ArrayList<Permissao>();
        if (usuario != null) {
            if (usuario.isSuperUsuario()) {
                permissoes = permissaoDAO.getTodasPermissoesComFilhos();
            } else {
                permissoes = permissaoDAO.getPermissoesComFilhos(usuario);
            }
        }
        permissoes = getChildren(permissoes);
        return permissoes;
    }

    private List<Permissao> getChildren(List<Permissao> permissoes) {
         List<Permissao> permissoesAdd = new ArrayList<Permissao>();
        if (permissoes != null) {
            for (Permissao permissao : permissoes) {
                if (!permissoesAdd.contains(permissao)) {
                    permissoesAdd.add(permissao);
                }
                permissao.getPermissoesFilhas().size();
                List<Permissao> children = getChildren(permissao.getPermissoesFilhas());
                if (children != null && !children.isEmpty()) {
                    for (Permissao child : children) {
                        if (!permissoesAdd.contains(child)) {
                            permissoesAdd.add(child);
                        }
                    }
                }
            }
        }
        return permissoesAdd;
    }

    @Override
    public void save(Permissao permissao) throws BusinessException {
        super.save(permissao);
    }

    @Override
    public List<UniqueField> getUniqueFields() {
        return new UniqueFields().add("key");
    }

    @Override
    public void validate(Permissao permissao) throws BusinessException {
    }

    public Integer getNivel(Permissao permissao) {
        Integer nivel = 0;
        if (permissao.getPermissaoPai() != null) {
            nivel = getNivel(permissao.getPermissaoPai()) + 1;
        }
        return nivel;
    }

    @Override
    public boolean isAudit() {
        return true;
    }

    /**
     * Seta o campo CaminhoPermissao da Permissao. O formato eh permissao 1 >
     * permissao 2 > permissao 3
     *
     * @param listaPermissoes
     * @param adicionarPropriaPermissao Indica se o caminho deve ir ateh o final
     * indicando a propria permissao
     */
    public void criarCaminhoPermissao(List<Permissao> listaPermissoes, boolean adicionarPropriaPermissao) {
        if (listaPermissoes != null) {
            for (Permissao permissao : listaPermissoes) {
                StringBuilder builder = new StringBuilder();

                List<Permissao> permissoes = new ArrayList<Permissao>();
                Permissao permissaoAtual = permissao;
                while (permissaoAtual != null) {
                    permissoes.add(permissaoAtual);
                    permissaoAtual = permissaoDAO.getInitialized(permissaoAtual.getPermissaoPai());
                }

                Collections.reverse(permissoes);
                for (int i = 0; i < permissoes.size(); i++) {
                    if (adicionarPropriaPermissao == true || !permissoes.get(i).equals(permissao)) {
                        if (i > 0) {
                            builder.append(" > ");
                        }
                        if (permissoes.get(i).equals(permissao)) {
                            builder.append("<b style='font-size: 13px;'>").append(permissoes.get(i).getNomeMenuVerificado()).append("</b>");
                        } else {
                            builder.append(permissoes.get(i).getNomeMenuVerificado());
                        }
                    }
                }
                permissao.setCaminhoPermissao(builder.toString());
            }
        }
    }

    public List<Permissao> pesquisarPermissao(String query, List<Permissao> listaPermissoes) {
        List<Permissao> permissoes = new ArrayList<Permissao>();

        if (listaPermissoes != null) {
            for (Permissao permissao : listaPermissoes) {
                if (permissao.isPossuiMenu() && permissao.getUrl() != null && !permissao.getUrl().isEmpty()) {
                    if (Conversion.removeAccent(permissao.getCaminhoPermissao()).toLowerCase().contains(Conversion.removeAccent(query.toLowerCase()))
                            || (permissao.getNomeMenu() != null && permissao.getNomeMenu().toLowerCase().contains(query.toLowerCase()))) {
                        permissoes.add(permissao);
                    }
                }
            }
        }

        CollectionsUtils.orderAsc(permissoes, "caminhoPermissao");

        return permissoes;
    }
}
