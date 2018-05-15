package entidades.dao;

import entidades.Carro;
import entidades.Usuario;
import conexao.FabricaConexao;
import exception.ErroSistema;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO implements CrudDAO<Usuario>{

    private Usuario user;

    @Override
    public void salvar(Usuario entidade) throws ErroSistema {
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps;
            if(entidade.getId() == null){
                ps = conexao.prepareStatement("INSERT INTO Usuarios (nome, login, senha) VALUES (?, ?,?)");
            } else {
                ps = conexao.prepareStatement("update Usuarios set nome=?, login=?, senha=? where id=?");
                ps.setInt(3, entidade.getId());
            }
            ps.setString(1, entidade.getNome());
            ps.setString(2, entidade.getLogin());
            ps.setString(3, entidade.getSenha());
            ps.execute();
            FabricaConexao.fecharConexao();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao tentar salvar!", ex);
        }
    }

    @Override
    public void deletar(Usuario entidade) throws ErroSistema {
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps  = conexao.prepareStatement("delete from usuario where id = ?");
            ps.setInt(1, entidade.getId());
            ps.execute();
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao deletar o carro!", ex);
        }
    }

    @Override
    public List<Usuario> buscar() throws ErroSistema {
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps = conexao.prepareStatement("select * from usuario");
            ResultSet resultSet = ps.executeQuery();
            List<Usuario> usuarios = new ArrayList<>();
            while(resultSet.next()){
                Usuario usuario = new Usuario();
                usuario.setId(resultSet.getInt("id"));
                usuario.setLogin(resultSet.getString("login"));
                usuario.setSenha(resultSet.getString("senha"));
                usuarios.add(usuario);
            }
            FabricaConexao.fecharConexao();
            return usuarios;
            
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao buscar os carros!",ex);
        }
    }

    @Override
    public boolean autenticar(Usuario entidade) throws ErroSistema {
        Usuario usuario = null;
        String login = entidade.getLogin();
        String senha = entidade.getSenha();
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps  = conexao.prepareStatement("SELECT login, senha from Usuarios WHERE id = ?");
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                usuario = new Usuario();
                usuario.setLogin(resultSet.getString("login"));
                usuario.setSenha(resultSet.getString("senha"));
            }
            ps.execute();
            if (usuario != null) {
                if (login.equals(usuario.getLogin())) {
                    if (senha.equals(usuario.getSenha())) {
                        this.user = usuario;
                        return true;
                        //aqui tem que enviar o user, mas para onde?
                    } else {
                        throw new Exception("Senha Incorreta");
                    }
                } else {
                    throw new Exception("Login Incorreto");
                }
            }
            return false;
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao autenticar (BD)!", ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
