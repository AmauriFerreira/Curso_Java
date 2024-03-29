package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import conexao.ConnectionFactory;
import modelo.Contato;

public class ContatoDao {
	// a conexao com o banco de dados
	private Connection connection;

	public ContatoDao() {
		try{
			this.connection = ConnectionFactory.getConnection();
		}
		catch(Exception e)
		{
			System.out.println("Erro na abertura do banco de dados: " + e.getMessage());
		}
	}

	public List<Contato> getListar(){
		try{
			PreparedStatement stmt = this.connection.prepareStatement("select * from contatos");
			ResultSet rs = stmt.executeQuery();

			List<Contato> contatos = new ArrayList<Contato>();

			while (rs.next()) {
				// criando o objeto Contato
				Contato contato = new Contato();
				contato.setId(rs.getLong("id"));
				contato.setNome(rs.getString("nome"));
				contato.setEmail(rs.getString("email"));
				contato.setEndereco(rs.getString("endereco"));
				contato.setDataNascimento(rs.getString("dataNascimento"));

				// adicionando o objeto a lista
				contatos.add(contato);
			}

			rs.close();
			stmt.close();
			return contatos;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void adiciona(Contato contato) {
		String sql = "insert into contatos (nome,email,endereco,dataNascimento) values (?,?,?,?)";
		try {
			// prepared statement para insercao
			PreparedStatement stmt = connection.prepareStatement(sql);

			stmt.setString(1,contato.getNome());
			stmt.setString(2,contato.getEmail());
			stmt.setString(3,contato.getEndereco());
			stmt.setString(4, contato.getDataNascimento());

			// executa
			stmt.execute();
			stmt.close();

		} 	catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	public Contato getPesquisar(int id){
		Contato contato = new Contato();
		try{
			PreparedStatement stmt = this.connection.prepareStatement("select * from contatos");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				if(id == rs.getInt("id")){
					contato.setId(rs.getLong("id"));
					contato.setNome(rs.getString("nome"));
					contato.setEmail(rs.getString("email"));
					contato.setEndereco(rs.getString("endereco"));
					contato.setDataNascimento(rs.getString("dataNascimento"));
				}
			}

			rs.close();
			stmt.close();
			return contato;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void altera(Contato contato) {
		String sql = "update contatos set nome=?, email=?, endereco=?," +
				"dataNascimento=? where id=?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, contato.getNome());
			stmt.setString(2, contato.getEmail());
			stmt.setString(3, contato.getEndereco());
			stmt.setString(4, contato.getDataNascimento());
			stmt.setLong(5, contato.getId());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void remove(int id) {
		try {
			PreparedStatement stmt = connection.prepareStatement("delete from contatos where id=?");
			stmt.setLong(1, id);
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
