
package br.com.infox.dal;

import java.sql.*;

/**
 *
 * @author LeoUser
 */
public class ModuloConexao {

    /*Método responsavél por estabelece conexão
    com o banco de dados MySql
     */
    public static Connection conector() {
        java.sql.Connection conexao = null;
        //Linha abaixo "Chama" o driver
        String driver = "com.mysql.jdbc.Driver";
        //Armazenando informações referentes ao Banco
        String url = "jdbc:mysql://192.168.0.11:3306/dbinfox";
        String user = "leo";
        String password = "Leo@1053";
        //Estabelecendo a conexão com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url,user,password);
            return conexao;
        } catch (Exception e) {
            //Linha abaixo serve de apoio para erros
            //System.out.println(e);
            return null;
        }
    }
}
