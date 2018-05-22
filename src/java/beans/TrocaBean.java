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
public class TrocaBean extends CarroBean implements Serializable {

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
    
     public void listaCarros(){
        this.estadoTela = "trocar";
        if(!isTroca()) {
            mudarParaTroca();
            return;
        }
        try {
            setListaCarrosTroca(getDao().trocar());
            if(getListaCarrosTroca() == null || getListaCarrosTroca().size() < 1){
                adicionarMensagem("Não temos carro para troca!", FacesMessage.SEVERITY_WARN);
            }
        } catch (ErroSistema ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
//            adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    
    public void buscar(Carro carroPraTrocar){
        setCarroParaTroca(carroPraTrocar);
        if(!isTroca()) {
            mudarParaTroca();
            return;
        }
        try {
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
        if(!isTroca()) {
            mudarParaTroca();
            return;
        }
        int idDonoCarro;
        try {
            setCarroSelecionado(carroSelecionado);
            getDao().efetuaTroca(getCarroSelecionado(), carroParaTroca);
            idDonoCarro = carroParaTroca.getIdDono();
            carroParaTroca.setIdDono(carroSelecionado.getIdDono());
            carroSelecionado.setIdDono(idDonoCarro);
            adicionarMensagem("Troca efetuada com sucesso!", FacesMessage.SEVERITY_INFO);
            setCarroSelecionado(null);
            setCarroParaTroca(null);
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
    
    //Metodos para controle da tela
    @Override
    public boolean isInseri(){
        return "inserir".equals(estadoTela);
    }
    @Override
    public boolean isEdita(){
        return "editar".equals(estadoTela);
    }
    @Override
    public boolean isBusca(){
        return "buscar".equals(estadoTela);
    }
    
    @Override
    public void mudarParaInseri(){
        estadoTela = "inserir";
    }
    @Override
    public void mudarParaEdita(){
        estadoTela = "editar";
    }
    @Override
    public void mudarParaBusca(){
        estadoTela = "buscar";
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
