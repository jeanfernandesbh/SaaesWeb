package br.com.saaes.jsf.md;

import br.com.saaes.app.util.JsfUtil;
import br.com.saaes.autent.Autenticacao;
import br.com.saaes.facade.FacUtil;
import static br.com.saaes.jsf.md.Login.USUARIO_KEY;
import br.com.saaes.modelo.T900Usuario;
import br.com.saaes.dao.DAO;
import br.com.saaes.util.JPAUtil;
import java.io.Serializable;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

/**
 *
 * @author f4679646
 */
@ManagedBean
public class CadastroUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    private T900Usuario novoUsuario;
    private String confirmaSenha;
    private EntityManager em;
    
    private final Calendar calendar = Calendar.getInstance();

    @PostConstruct
    public void init() {
        this.em = JPAUtil.getEm();
        this.novoUsuario = new T900Usuario();

    }

    public void cadastraUsuario() {
        try {
            em.getTransaction().begin();
            novoUsuario.setSenha(Autenticacao.criptografa(novoUsuario.getSenha(), "MD5"));
            novoUsuario.setAtivo(Boolean.TRUE);
            novoUsuario.setDtInc(calendar.getTime());

            DAO.save(novoUsuario, novoUsuario.getId(), em, Boolean.TRUE);

            em.getTransaction().commit();

            JsfUtil.addSuccessMessage("Usuário cadastrado com Sucesso!");

            FacUtil.setSession(Boolean.TRUE);
            FacUtil.setAtributoSessao(USUARIO_KEY, novoUsuario);
            FacUtil.redirectPag("/index.xhtml");

        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Erro ao cadastrar usuário!");
        }

    }
   
    
    
    public void isSenha() {
        if (confirmaSenha.equals(novoUsuario.getSenha())) {
            JsfUtil.addAlertMessage("Senhas OK");
        } else {
            JsfUtil.addAlertMessage("Senhas não são iguais");
        }
    }

    public T900Usuario getNovoUsuario() {
        return novoUsuario;
    }

    public void setNovoUsuario(T900Usuario novoUsuario) {
        this.novoUsuario = novoUsuario;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

}
