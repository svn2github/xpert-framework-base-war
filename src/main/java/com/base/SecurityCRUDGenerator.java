package com.base;

import com.base.modelo.exemplo.PessoaExemplo;
import com.xpert.utils.HumaniseCamelCase;
import com.xpert.utils.StringUtils;
import java.util.logging.Logger;

/**
 * Classe para facilitar a geracao das permissoes da classe GeracaoPermissao
 *  
 * @see GeracaoPermissao
 * @author Ayslan
 */
public class SecurityCRUDGenerator {

    private static final Logger logger = Logger.getLogger(SecurityCRUDGenerator.class.getName());
    private static String VIEW_PREFFIX;

    public static void main(String[] args) {
        /**
         * informar padráo da view
         */
        VIEW_PREFFIX = "/view/controleAcesso/";
        /**
         * adicionar aqui as classes que serão geradas
         */
        generate(PessoaExemplo.class);
    }

    public static void generate(Class clazz) {

        String className = clazz.getSimpleName();
        String classLower = StringUtils.getLowerFirstLetter(clazz.getSimpleName());
        String classHuman = new HumaniseCamelCase().humanise(className);

        StringBuilder builder = new StringBuilder();
        builder.append("//").append(classHuman).append("\n");
        builder.append("create(new Permissao(\"").append(classLower).append("\", \"").append(classHuman).append("\", true), null);").append("\n");
        builder.append("create(new Permissao(\"").append(classLower).append(".create\", \"Cadastro de ").append(classHuman).append("\", \"").append(VIEW_PREFFIX).append(classLower).append("/create").append(className).append(".jsf\", true), \"").append(classLower).append("\");").append("\n");
        builder.append("create(new Permissao(\"").append(classLower).append(".list\", \"Consulta de ").append(classHuman).append("\", \"").append(VIEW_PREFFIX).append(classLower).append("/list").append(className).append(".jsf\", true), \"").append(classLower).append("\");").append("\n");
        builder.append("create(new Permissao(\"").append(classLower).append(".audit\", \"Auditoria de ").append(classHuman).append("\"").append("), \"").append(classLower).append("\");").append("\n");
        builder.append("create(new Permissao(\"").append(classLower).append(".delete\", \"Exclusão de ").append(classHuman).append("\"").append("), \"").append(classLower).append("\");").append("\n");
        builder.append("\n");
        System.out.println(builder.toString());

    }
}
