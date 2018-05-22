package beans;

import conexao.SessionUtils;
import entidades.Carro;
import entidades.Usuario;
import entidades.dao.CarroDAO;
import exception.ErroSistema;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.bean.SessionScoped;

import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class TrocaBean extends CrudBean<Carro, CarroDAO> implements Serializable {

    private CarroDAO carroDAO;
    private Carro carroSelecionado, carroParaTroca;
    private List<Carro> listaCarrosTroca;

    @Override
    public CarroDAO getDao() {
        if(carroDAO == null){
            carroDAO = new CarroDAO();
        }
        return carroDAO;
    }
    
    @Override
    public Carro criarNovaEntidade() {
        HttpSession session = SessionUtils.getSession();
        Usuario currentUser = (Usuario) session.getAttribute("usuario");
        Carro c = new Carro();
        c.setIdDono(currentUser.getId());
        return c;
    }
    
    public void buscar(Carro carroPraTrocar){
        if(!isTroca()) {
            mudarParaTroca();
            return;
        }
        try {
            setCarroParaTroca(carroPraTrocar);
            setListaCarrosTroca(getDao().trocar());
            if(getListaCarrosTroca() == null || getListaCarrosTroca().size() < 1){
                adicionarMensagem("Não temos carro para troca!", FacesMessage.SEVERITY_WARN);
            }
        } catch (ErroSistema ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
//            adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    
    public void trocar(Carro carroSelecionado) {
        int idDonoCarro;
        try {
            getDao().efetuaTroca(carroSelecionado, carroParaTroca);
            setCarroSelecionado(carroSelecionado);
            idDonoCarro = carroParaTroca.getIdDono();
            carroParaTroca.setIdDono(carroSelecionado.getIdDono());
            carroSelecionado.setIdDono(idDonoCarro);
            adicionarMensagem("Troca efetuada com sucesso!", FacesMessage.SEVERITY_INFO);
            mudarParaBusca();
        } catch (ErroSistema ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
//            adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    @Override
    public Carro criarNovaEntidadeTroca() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isTroca(){
        return "trocar".equals(estadoTela);
    }
    
    public void mudarParaTroca(){
        estadoTela = "trocar";
    }

    public Carro getCarroSelecionado() {
        return carroSelecionado;
    }

    public void setCarroSelecionado(Carro carroSelecionado) {
        this.carroSelecionado = carroSelecionado;
    }

    public Carro getCarroParaTroca() {
        return carroParaTroca;
    }

    public void setCarroParaTroca(Carro carroParaTroca) {
        this.carroParaTroca = carroParaTroca;
    }

    public List<Carro> getListaCarrosTroca() {
        return listaCarrosTroca;
    }

    public void setListaCarrosTroca(List<Carro> listaCarrosTroca) {
        this.listaCarrosTroca = listaCarrosTroca;
    }

    

    

}
