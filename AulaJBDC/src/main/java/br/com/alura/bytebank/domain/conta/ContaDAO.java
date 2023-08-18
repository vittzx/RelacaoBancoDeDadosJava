package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.management.RuntimeErrorException;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

public class ContaDAO {
    private Connection connection;  
    ContaDAO(Connection conn){
        this.connection = conn;
    }

    public void salvarConta(DadosAberturaConta dadosDaConta){
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), BigDecimal.ZERO,cliente, true );
        String sql = "INSERT INTO CONTA (numero, saldo, cliente_nome, cliente_cpf, cliente_email) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        
        try{
            ps = connection.prepareStatement(sql);
            ps.setInt(1, conta.getNumero());
            ps.setBigDecimal(2, BigDecimal.ZERO);
            ps.setString(3,dadosDaConta.dadosCliente().nome());
            ps.setString(4,dadosDaConta.dadosCliente().cpf());
            ps.setString(5,dadosDaConta.dadosCliente().email());
            ps.setBoolean(6, true);
            ps.execute();
            ps.close();
            connection.close();
        }catch(SQLException e){
            System.out.println("deu ruim");
        }
    }

    public Set<Conta> listar(){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Set<Conta> contas = new HashSet<>();
        String sql = "SELECT * FROM conta WHERE esta_ativa = true";
        try{
            ps = connection.prepareStatement(sql);   // sempre com try e catch pois lanca excecao SQLException
            resultSet  =ps.executeQuery(); // pega todas as linhas do resultado

            while(resultSet.next()){
                Integer numero = resultSet.getInt(1); // coluna que esta selecionada | se o numero da conta esta na primaira coluna, entao passao o indice 1
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean estaAtiva  = resultSet.getBoolean(6);

                DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome,cpf,email);
                Cliente cliente = new Cliente(dadosCadastroCliente);
                contas.add(new Conta(numero,saldo,cliente, estaAtiva));
            }
            resultSet.close();
            ps.close();
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }


        return contas;
    }

    public Conta listarPorNumero(Integer numero) {
        String sql = "SELECT * FROM conta WHERE numero = ? AND esta_ativa = true ";

        PreparedStatement ps;
        ResultSet resultSet;
        Conta conta = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, numero);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Integer numeroRecuperado = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean estaAtiva = resultSet.getBoolean(6);
                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                conta = new Conta(numeroRecuperado, saldo,cliente, estaAtiva);
            }
            resultSet.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conta;
    }

    public void alterar(Integer numeroConta, BigDecimal valor){
        String sql = "UPDATE CONTA SET saldo = ? WHERE numero = ?";
        PreparedStatement ps;
        try{
            ps = connection.prepareStatement(sql);
            ps.setBigDecimal(1, valor);
            ps.setInt(2, numeroConta);
            ps.execute();
            ps.close();
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void deletar(Integer numeroDaConta){
        String sql = "DELETE FROM conta WHERE numero = ?";
        PreparedStatement ps = null;
        try{
            ps = connection.prepareStatement(sql);
            ps.setInt(1, numeroDaConta);
            ps.execute();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void alterarLogico(Integer numeroDaConta){
        String sql = "UPDATE conta SET esta_ativa = false WHERE numero = ?";
        PreparedStatement ps = null;
        try{
            ps = connection.prepareStatement(sql);
            ps.setInt(1, numeroDaConta);
            ps.execute();
            ps.close();
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
