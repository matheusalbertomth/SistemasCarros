package entidades.dao;

import entidades.Carro;
import conexao.FabricaConexao;
import conexao.SessionUtils;
import entidades.Usuario;
import exception.ErroSistema;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Danilo Souza Almeida
 */
public class CarroDAO implements CrudDAO<Carro>{
    UsuarioDAO userDAO;
    
    @Override
    public void salvar(Carro carro) throws ErroSistema{
        HttpSession session = SessionUtils.getSession();
        Usuario currentUser = (Usuario) session.getAttribute("usuario");
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps;
//            System.out.println("dono " +ldao.getId());
            if(carro.getId() == null){
                ps = conexao.prepareStatement("INSERT INTO `Carros` (IdDono, `modelo`,`fabricante`,`cor`,`ano`, `NomeDono`) VALUES (?, ?, ?, ?, ?, ?)");
            } else {
                ps = conexao.prepareStatement("UPDATE Carros SET IdDono=?, modelo=?, fabricante=?, cor=?, ano=?, NomeDono=? where id=?");
                ps.setInt(6, carro.getId());
            }
            ps.setInt(1, currentUser.getId());
            ps.setString(2, carro.getModelo());
            ps.setString(3, carro.getFabricante());
            ps.setString(4, carro.getCor());
            ps.setDate(5, new Date(carro.getAno().getTime()));
            ps.setString(6, carro.getNomeDono());
            ps.execute();
            FabricaConexao.fecharConexao();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao tentar salvar!", ex);
        }
    }
    
    @Override
    public void deletar(Carro carro) throws ErroSistema{
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps  = conexao.prepareStatement("DELETE FROM Carros WHERE id = ?");
            ps.setInt(1, carro.getId());
            ps.execute();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao deletar o carro!", ex);
        }
    }
    
    @Override
    public List<Carro> buscar() throws ErroSistema{
        HttpSession session = SessionUtils.getSession();
        Usuario currentUser = (Usuario) session.getAttribute("usuario");
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps = conexao.prepareStatement("SELECT * FROM Carros WHERE IdDono = ?");
            ps.setInt(1, currentUser.getId());
            ResultSet resultSet = ps.executeQuery();
            List<Carro> carros = new ArrayList<>();
            while(resultSet.next()){
                Carro carro = new Carro();
                carro.setIdDono(resultSet.getInt("IdDono"));
                carro.setId(resultSet.getInt("id"));
                carro.setModelo(resultSet.getString("modelo"));
                carro.setFabricante(resultSet.getString("fabricante"));
                carro.setCor(resultSet.getString("cor"));
                carro.setAno(resultSet.getDate("ano"));
                carro.setNomeDono(resultSet.getString("NomeDono"));
                carros.add(carro);
            }
            FabricaConexao.fecharConexao();
            return carros;
            
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao buscar os carros!",ex);
        }
    }
    
    @Override
    public List<Carro> trocar() throws ErroSistema {
         HttpSession session = SessionUtils.getSession();
        Usuario currentUser = (Usuario) session.getAttribute("usuario");
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps = conexao.prepareStatement("SELECT * FROM Carros WHERE IdDono <> ?");
            ps.setInt(1, currentUser.getId());
            ResultSet resultSet = ps.executeQuery();
            List<Carro> allCarros = new ArrayList<>();
            while(resultSet.next()){
                Carro carro = new Carro();
                carro.setIdDono(resultSet.getInt("IdDono"));
                carro.setId(resultSet.getInt("id"));
                carro.setModelo(resultSet.getString("modelo"));
                carro.setFabricante(resultSet.getString("fabricante"));
                carro.setCor(resultSet.getString("cor"));
                carro.setAno(resultSet.getDate("ano"));
                carro.setNomeDono(resultSet.getString("NomeDono"));
                allCarros.add(carro);
            }
            FabricaConexao.fecharConexao();
            return allCarros;
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao buscar os carros!",ex);
        }
    }
    
    public void efetuaTroca(Carro carro1, Carro carro2) throws ErroSistema {
        try {
            Connection conexao = FabricaConexao.getConexao();
            int idDonoCarro1 = carro1.getIdDono();

            PreparedStatement ps = conexao.prepareStatement("UPDATE Carros SET IdDono = ?, NomeDono=? WHERE id = ?");
            PreparedStatement ps2 = conexao.prepareStatement("UPDATE Carros SET IdDono = ?, NomeDono=? WHERE id = ?");

            ps.setInt(1, carro2.getIdDono());
            ps.setString(2, carro2.getNomeDono());
            ps.setInt(3, carro1.getId());

            ps2.setInt(1, idDonoCarro1);
            ps2.setString(2, carro1.getNomeDono());
            ps2.setInt(3, carro2.getId());
            
            ps.execute();
            ps2.execute();
            
            FabricaConexao.fecharConexao();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao efetuar a troca!",ex);
        }
    }
}
