/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.dao;

import conexao.FabricaConexao;
import entidades.Carro;
import entidades.Usuario;
import exception.ErroSistema;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vapstor
 */
public class LoginDAO {
    protected Usuario usuario;
    protected Carro carro;
    /**
     *
     * @param login
     * @param senha
     * @return
     * @throws ErroSistema
     */
    public boolean autenticar(String login, String senha) throws ErroSistema {
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps  = conexao.prepareStatement("SELECT * from Usuarios WHERE login = ? and senha = ?");
            ps.setString(1, login);
            ps.setString(2, senha);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                return true;
            }
            ps.execute();
            return false;
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao autenticar (BD)!", ex);
        } catch (ErroSistema ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Usuario getUsuarioLogado(String login) throws ErroSistema {
        Usuario userLogado = new Usuario();
        try {
            Connection conexao = FabricaConexao.getConexao();
            PreparedStatement ps  = conexao.prepareStatement("SELECT * from Usuarios WHERE login = ?");
            ps.setString(1, login);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                userLogado.setNome(resultSet.getString("Nome"));
                userLogado.setId(resultSet.getInt("id"));
//                carro.setIdDono(userLogado.getId());
                userLogado.setLogin(resultSet.getString("login"));
                userLogado.setSenha(resultSet.getString("senha"));
            }
            ps.execute();
            return userLogado;
        } catch (SQLException ex) {
            throw new ErroSistema("Erro ao autenticar (BD)!", ex);
        } catch (ErroSistema ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userLogado;
    }
    
}
