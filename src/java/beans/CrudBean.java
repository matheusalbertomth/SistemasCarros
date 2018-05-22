package beans;

import entidades.dao.CrudDAO;
import exception.ErroSistema;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public abstract class CrudBean<E, D extends CrudDAO> {

    public String estadoTela = "buscar"; //Inserir, Editar, Buscar, Trocar
    
    public E entidade;
    public E entidadeParaTroca;
    public List<E> entidades;
    public List<E> entidadesTroca;
    
    public void novo(){
        if(!isInseri()){
            mudarParaInseri();
            entidade =  criarNovaEntidade();
        }
    }

    public void salvar(){
        try {
            getDao().salvar(entidade);
            entidade = criarNovaEntidade();
            adicionarMensagem("Salvo com sucesso!", FacesMessage.SEVERITY_INFO);
            mudarParaBusca();
        } catch (ErroSistema ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
            adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
        mudarParaBusca();
    }
    
    public void editar(E entidade){
        if(!isEdita()){
            this.entidade = entidade;
            mudarParaEdita();
        }
    }
    
    public void deletar(E entidade){
        try {
            getDao().deletar(entidade);
            entidades.remove(entidade);
            adicionarMensagem("Deletado com sucesso!", FacesMessage.SEVERITY_INFO);
        } catch (ErroSistema ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
            adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    
    public void buscar(){
        mudarParaBusca();
        try {
            entidades = getDao().buscar();
            if(entidades == null || entidades.size() < 1){
                adicionarMensagem("Não temos nada cadastrado!", FacesMessage.SEVERITY_WARN);
            }
        } catch (ErroSistema ex) {
            Logger.getLogger(CrudBean.class.getName()).log(Level.SEVERE, null, ex);
            adicionarMensagem(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    
    public void adicionarMensagem(String mensagem, FacesMessage.Severity tipoErro){
        FacesMessage fm = new FacesMessage(tipoErro, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }
    
    //getters e setters
    public E getEntidade() {
        return entidade;
    }   

    public void setEntidade(E entidade) {
        this.entidade = entidade;
    }

    public List<E> getEntidades() {
        return entidades;
    }

    public void setEntidades(List<E> entidades) {
        this.entidades = entidades;
    }

    public List<E> getEntidadesTroca() {
        return entidadesTroca;
    }

    public void setEntidadesTroca(List<E> entidadesTroca) {
        this.entidadesTroca = entidadesTroca;
    }
   
    //Responsvel por criar os métodos nas classes bean
    public abstract D getDao();
    public abstract E criarNovaEntidade();
    public abstract E criarNovaEntidadeTroca();
    
    //Metodos para controle da tela
    public boolean isInseri(){
        return "inserir".equals(estadoTela);
    }
    public boolean isEdita(){
        return "editar".equals(estadoTela);
    }
    public boolean isBusca(){
        return "buscar".equals(estadoTela);
    }
    
    private boolean isHome() {
        return "home".equals(estadoTela);
    }

    private void mudarParaHome() {
        estadoTela = "home";
    }
    
    public void mudarParaInseri(){
        estadoTela = "inserir";
    }
    
    public void mudarParaEdita(){
        estadoTela = "editar";
    }
    
    public void mudarParaBusca(){
        estadoTela = "buscar";
    }

    public E getEntidadeParaTroca() {
        return entidadeParaTroca;
    }

    public void setEntidadeParaTroca(E entidadeParaTroca) {
        this.entidadeParaTroca = entidadeParaTroca;
    }

    
}
