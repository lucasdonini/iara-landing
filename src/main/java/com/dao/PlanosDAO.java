package com.dao;

import com.dto.AtualizarListarPlanosDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlanosDAO extends DAO{
    public PlanosDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    //Cadastrar novo Plano
    public void cadastrar(AtualizarListarPlanosDTO plano) throws SQLException {
        //Comando SQL
        String sql = "INSERT INTO planos(nome, valor, descricao) VALUES (?,?,?)";

        try ( PreparedStatement pstmt = this.conn.prepareStatement(sql)){ //Preparando comando SQL
            //Definindo variáveis no código SQL
            pstmt.setString(1,plano.getNome());
            pstmt.setDouble(2,plano.getValor());
            pstmt.setString(3,plano.getDescricao());
            //Salvando alterações no banco
            pstmt.execute();
            //Confirmando transações
            this.conn.commit();
        } catch(SQLException e){
            System.err.println(e.getMessage());
            //Cancelando transações
            this.conn.rollback();
            //Enviando exceção
            throw e;
        }

    }

    //Remover plano
    public void remover(AtualizarListarPlanosDTO plano) throws SQLException{
        // Comando SQL
        String sql = "DELETE FROM planos WHERE nome = ?";

        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)){
            //Definindo variáveis no código SQL
            pstmt.setString(1,plano.getNome());
            //Salvando alterações no banco
            if (pstmt.executeUpdate()!=1){
                throw new SQLException();
            }
            //Confirmando transações
            this.conn.commit();
        } catch(SQLException e){
            System.err.println(e.getMessage());
            this.conn.rollback();
            throw e;
        }
    }

    //Atualizar nome
    private void alterarNome(AtualizarListarPlanosDTO plano, String novoNome) throws SQLException{
        //Comando SQL
        String sql = "UPDATE planos WHERE nome = ? SET nome = ?";

        try(PreparedStatement pstmt = this.conn.prepareStatement(sql)){ //Preparando comando SQL
            //Definindo variáveis no código SQL
            pstmt.setString(1, plano.getNome());
            pstmt.setString(2, novoNome);
            //Salvando alterações no banco de dados
            pstmt.executeUpdate();
        } catch(SQLException e){
            System.err.println(e.getMessage());
            //Lançando exceção
            throw e;
        }
    }

    //Atualizar valor
    private void alterarValor(AtualizarListarPlanosDTO plano, double novoValor) throws SQLException{
        //Comando SQL
        String sql = "UPDATE planos WHERE valor = ? SET valor = ?";

        try(PreparedStatement pstmt = this.conn.prepareStatement(sql)){ //Preparando comando SQL
            //Definindo variáveis no código SQL
            pstmt.setDouble(1, plano.getValor());
            pstmt.setDouble(2, novoValor);
            //Salvando alterações no banco de dados
            pstmt.executeUpdate();
        } catch(SQLException e){
            System.err.println(e.getMessage());
            //Lançando exceção
            throw e;
        }
    }

    //Atualizar descrição
    private void alterarDescricao(AtualizarListarPlanosDTO plano, String novaDescricao) throws SQLException{
        //Comando SQL
        String sql = "UPDATE planos WHERE descricao = ? SET descricao = ?";

        try(PreparedStatement pstmt = this.conn.prepareStatement(sql)){ //Preparando comando SQL
            //Definindo variáveis no código SQL
            pstmt.setString(1, plano.getDescricao());
            pstmt.setString(2, novaDescricao);
            //Salvando alterações no banco de dados
            pstmt.executeUpdate();
        } catch(SQLException e){
            System.err.println(e.getMessage());
            //Lançando exceção
            throw e;
        }
    }

    //Verificar requisição de atualização e confirmar alteração no banco
    public void alterar(AtualizarListarPlanosDTO plano, String nome, Double valor, String descricao) throws SQLException{
        try{
                //realizando verificações
            if (plano.getNome()!=null){
                this.alterarNome(plano, nome);
            } else if(plano.getValor()!=null){
                this.alterarValor(plano, valor);
            } else if(plano.getDescricao()!=null){
                this.alterarDescricao(plano, descricao);
            }
           //Confirmando transação
           this.conn.commit(); 
        } catch(SQLException sql){
            System.err.println(sql.getMessage());
            //Cancelando transação
            this.conn.rollback();
            //Lançando excessão
            throw sql;
        }
        
    }
}
