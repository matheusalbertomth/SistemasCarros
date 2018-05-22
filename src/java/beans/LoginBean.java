/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 * Managed Bean for Login
 *
 */
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import conexao.SessionUtils;
import entidades.Usuario;

import entidades.dao.LoginDAO;
import exception.ErroSistema;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1094801825228386363L;

    private String pwd;
    private String msg;
    private String user;
    public int id;

    public String getPassword() {
        return pwd;
    }

    public void setPassword(String pwd) {
        this.pwd = pwd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLogin() {
        return user;
    }

    public void setLogin(String login) {
        this.user = login;
    }

    //validate login
    public String validateUsernamePassword() throws ErroSistema {
        LoginDAO ldao = new LoginDAO();
        boolean valid = ldao.autenticar(user, pwd);
        if (valid) {
            Usuario usuarioLogado = ldao.getUsuarioLogado(user);
            this.setId(usuarioLogado.getId());
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("username", user);
            session.setAttribute("usuario", usuarioLogado);
            return "admin";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Usuário Incorreto!",
                            "Verifique as informações"));
            return "index";
        }
    }

    //logout event, invalidate session
    public String logout() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        return "index";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
