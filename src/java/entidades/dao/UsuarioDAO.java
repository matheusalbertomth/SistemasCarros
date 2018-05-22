package entidades.dao;

import entidades.Carro;
import entidades.Usuario;
import conexao.FabricaConexao;
import exception.ErroSistema;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class UsuarioDAO implements CrudDAO<Usuario>{

    private Usuario user;

    @Override
    public void salvar(Usuario entidade) throws ErroSistema {
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps;
            if(entidade.getId() == null){
                ps = conexao.prepareStatement("INSERT INTO Usuarios (nome, login, senha) VALUES (?, ?,?)");
            } else {
                ps = conexao.prepareStatement("UPDATE Usuarios set nome=?, login=?, senha=? where id=?");
                ps.setInt(3, entidade.getId());
            }
            ps.setString(1, entidade.getNome());
            ps.setString(2, entidade.getLogin());
            ps.setString(3, entidade.getSenha());
            ps.execute();
            FabricaConexao.fecharConexao();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao tentar salvar usuario!", ex);
        }
    }

    @Override
    public void deletar(Usuario entidade) throws ErroSistema {
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps  = conexao.prepareStatement("DELETE from Usuarios where id = ?");
            ps.setInt(1, entidade.getId());
            ps.execute();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao deletar o usuario!", ex);
        }
    }

    @Override
    public List<Usuario> buscar() throws ErroSistema {
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps = conexao.prepareStatement("select * from Usuarios");
            ResultSet resultSet = ps.executeQuery();
            List<Usuario> usuarios = new ArrayList<>();
            while(resultSet.next()){
                Usuario usuario = new Usuario();
                usuario.setId(resultSet.getInt("id"));
                usuario.setNome(resultSet.getString("Nome"));
                usuario.setLogin(resultSet.getString("login"));
                usuario.setSenha(resultSet.getString("senha"));
                usuarios.add(usuario);
            }
            FabricaConexao.fecharConexao();
            return usuarios;
            
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao buscar os usuarios!",ex);
        }
    }
}
