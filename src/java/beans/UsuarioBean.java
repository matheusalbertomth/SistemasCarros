package beans;

import entidades.dao.UsuarioDAO;
import entidades.Usuario;
import exception.ErroSistema;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class UsuarioBean extends CrudBean<Usuario, UsuarioDAO> implements Serializable {

    private UsuarioDAO usuarioDAO;
    
    @Override
    public UsuarioDAO getDao() {
        if(usuarioDAO == null){
            usuarioDAO = new UsuarioDAO() {
                @Override
                public List<Usuario> trocar() throws ErroSistema {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
        }
        return usuarioDAO;
    }

    @Override
    public Usuario criarNovaEntidade() {
        return new Usuario();
    }
    
//    public Usuario usuarioLogado() {
//        LoginBean = new LoginBean();
//    }

    @Override
    public Usuario criarNovaEntidadeTroca() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
