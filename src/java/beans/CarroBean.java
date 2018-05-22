package beans;

import conexao.SessionUtils;
import entidades.dao.CarroDAO;
import entidades.Carro;
import entidades.Usuario;
import exception.ErroSistema;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class CarroBean extends CrudBean<Carro, CarroDAO> implements Serializable {

    private CarroDAO carroDAO;
    private Usuario currentUser;
    
    @Override
    public CarroDAO getDao() {
        if(carroDAO == null){
            carroDAO = new CarroDAO();
        }
        return carroDAO;
    }
    
    @Override
    public void buscar(){
       if(!isBusca()){
           mudarParaBusca();
           return;
        }
        try {
            entidades = getDao().buscar();
            if(entidades == null || entidades.size() < 1){
                adicionarMensagem("Não temos nada cadastrado!", FacesMessage.SEVERITY_WARN);
            }
        } catch (ErroSistema ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
//            adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    
    @Override
    public Carro criarNovaEntidade() {
        HttpSession session = SessionUtils.getSession();
        currentUser = (Usuario) session.getAttribute("usuario");
        Carro c = new Carro();
        c.setIdDono(currentUser.getId());
        c.setNomeDono(currentUser.getNome());
        return c;
    }
    
    @Override
    public Carro criarNovaEntidadeTroca() {
        HttpSession session = SessionUtils.getSession();
        currentUser = (Usuario) session.getAttribute("usuario");
        Carro c = new Carro();
        c.setIdDono(currentUser.getId());
        c.setNomeDono(currentUser.getNome());
        return c;
    }
}
