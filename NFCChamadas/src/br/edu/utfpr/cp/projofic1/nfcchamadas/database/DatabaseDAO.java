package br.edu.utfpr.cp.projofic1.nfcchamadas.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import android.annotation.SuppressLint;
import br.edu.utfpr.cp.projofic1.nfcchamada.daoLocal.presencaDAO;

public class DatabaseDAO {
	
	private Connection dbConnection;
	
	
	public DatabaseDAO() throws SQLException {
		dbConnection = new MySQLConnectionFactory().getConnection();
	}
	
	
	public void close() throws SQLException {
		dbConnection.close();
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.close();
	}
	
	
	public List<TipoPessoa> getTiposPessoa() throws SQLException {
		// Fazendo a query
		PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM tipo_pessoa");
		ResultSet result = stmt.executeQuery();
		
		// Preenchendo a lista
		List<TipoPessoa> tiposPessoa = new ArrayList<TipoPessoa>();
		while (result.next()) {
			TipoPessoa tipoPessoa = new TipoPessoa();
			tipoPessoa.setId(result.getLong("id_tipo_pessoa"));
			tipoPessoa.setNome(result.getString("nome"));
			tiposPessoa.add(tipoPessoa);
		}
		
		// Fechando a query do banco de dados e retornando a lista
		result.close();
		stmt.close();
		return tiposPessoa;
	}
	
	
	
	public Pessoa loginUsuario(String emailRegAcademico, String senha) throws SQLException {
		// Verificando por registro acad�mico
		// Pesquisando se existe um registro de matr�cula com este usu�rio e senha cadastrado
		PreparedStatement stmt = dbConnection.prepareStatement(
				"SELECT * FROM pessoa WHERE r_matricula = ? AND senha = ?");
		stmt.setString(1, emailRegAcademico);
		stmt.setString(2, senha);
		ResultSet result = stmt.executeQuery();
		Pessoa pessoa = null;
		
		if (result.next()) {
			// Se existir, fecha a query do banco e retuorna o ID da pessoa para ser feito o login
					
			pessoa = new Pessoa();
			pessoa.setId(result.getLong("id_pessoa"));
			pessoa.setNome(result.getString("nome"));
			pessoa.setEmail(result.getString("email"));
			pessoa.setSenha(result.getString("senha"));
			pessoa.setrAcademico(result.getString("r_matricula"));
			pessoa.setIdTipoPessoa(result.getLong("id_tipo_pessoa"));
			
			
			result.close();
			stmt.close();
			
			return pessoa;
		}
		// Se n�o exisir, s� fecha a query do banco de dados e vai para a verifica��o por e-mail
		result.close();
		stmt.close();
		
		// Verificando por e-mail
		try {
			// Validando o e-mail
			InternetAddress netAddr = new InternetAddress(emailRegAcademico);
			netAddr.validate();
			
			// Pesquisando se existe um e-mail com este usu�rio e senha cadastrado
			stmt = dbConnection.prepareStatement(
					"SELECT * FROM pessoa WHERE email = ? AND senha = ?");
			stmt.setString(1, emailRegAcademico);
			stmt.setString(2, senha);
			result = stmt.executeQuery();
		
			if (result.next()) {
				// Se existir, retorna o ID da pessoa para ser feito o login
				
				pessoa = new Pessoa();
				pessoa.setId(result.getLong("id_pessoa"));
				pessoa.setNome(result.getString("nome"));
				pessoa.setEmail(result.getString("email"));
				pessoa.setSenha(result.getString("senha"));
				pessoa.setrAcademico(result.getString("r_matricula"));
				pessoa.setIdTipoPessoa(result.getLong("id_tipo_pessoa"));
				result.close();
				stmt.close();
				
			} else {
				// Se n�o existir, retorna null
				return null;
			}
			return pessoa;
			// Fechando a query do banco de dados e retornando o resultado
		
			
		} catch(AddressException e) {
			return null;
		}
	}
	
	public void cadastrarPessoa(Pessoa pessoa) throws SQLException {
		String sql = "INSERT INTO pessoa (nome, email, senha, r_matricula, id_tipo_pessoa) VALUES(?, ?, ?, ?, ?)";
		PreparedStatement stmt = dbConnection.prepareStatement(sql);
		stmt.setString(1, pessoa.getNome());
		stmt.setString(2, pessoa.getEmail());
	
		stmt.setString(3,pessoa.getSenha());
		stmt.setString(4, pessoa.getrAcademico());
		stmt.setLong(5, pessoa.getIdTipoPessoa());
		stmt.execute();
		stmt.close();

	}
	public Chamada salvarPresenca(Chamada chamada, presencaDAO presenca) throws SQLException {
		
		
		String sqlChamda = "INSERT INTO chamada (id_evento, nome_turma, qtd_aula, quantidade_aluno, realizada) VALUES(?, ?, ?, ?,?)";
		
		String sqlPresenca = "INSERT INTO presenca (chamada_id_chamada,chamada_id_evento, pessoa_id_pessoa) VALUES(?, ?, ?)";
		List<Presenca> presentes = presenca.getByPresentesEvento(chamada);
		
		PreparedStatement stmt = dbConnection.prepareStatement(sqlChamda,Statement.RETURN_GENERATED_KEYS);
		stmt.setLong(1,chamada.getId_evento());
		stmt.setString(2, chamada.getDescricao());
		stmt.setString(3, chamada.getQdtAula());
		stmt.setInt(4, presentes.size());
		stmt.setInt(5, 1);
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();  
		 
		 int id = 0;  
		  if(rs.next()){  
		        id = rs.getInt(1);  
		 }         
		
		PreparedStatement stmtP = dbConnection.prepareStatement(sqlPresenca);
		
		for(Presenca p : presentes){
			
			stmtP.setLong(1,id);
			stmtP.setLong(2, chamada.getId_evento());
			stmtP.setLong(3, p.getRa());
			stmtP.executeUpdate();
			
			System.out.println("Salvando"+p.getNome()+"RA: "+p.getRa());
		}
		
		stmtP.close();
		
		chamada.setId_chamada(id);
		chamada.setQuantidade(presentes.size());
		
		
		return chamada;

	}

	
	/**
	 * @param id
	 * @return Se n�o tiver uma pessoa com esse ID, retornar� <code>null</code>
	 * @throws SQLException
	 */
	public Pessoa getPessoaById(long id) throws SQLException {
		String sql = "SELECT nome, email, senha, r_matricula, id_tipo_pessoa FROM pessoa WHERE id_pessoa = ?";
		PreparedStatement stmt = dbConnection.prepareStatement(sql);
		stmt.setLong(1, id);
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			Pessoa pessoa = new Pessoa();
			pessoa.setId(id);
			pessoa.setNome(result.getString("nome"));
			pessoa.setEmail(result.getString("email"));
			pessoa.setSenha(result.getString("senha"));
			pessoa.setrAcademico(result.getString("r_matricula"));
			pessoa.setIdTipoPessoa(result.getLong("id_tipo_pessoa"));
			return pessoa;
		}
		return null;
	}
	
		public List<Evento> getEventosDaPessoa(long pessoaId) throws SQLException {
		ArrayList<Evento> eventos = new ArrayList<Evento>();
		
		// Fazendo a consulta no banco de dados
		String sql = "SELECT * FROM evento WHERE id_criador_evento = ?";
		PreparedStatement stmt = dbConnection.prepareStatement(sql);
		stmt.setString(1, String.valueOf(pessoaId));
		ResultSet result = stmt.executeQuery();
		
		// Preenchendo a lista com os resultados da consulta
		fillEventosList(eventos, result);
		
		// Retornando a lista
		return eventos;
	}
	
	@SuppressLint("SimpleDateFormat")
	public List<Evento> getEventosDaPessoa(long pessoaId, Calendar startDia, Calendar endDia) throws SQLException {
		List<Evento> eventos = new ArrayList<Evento>();
		
		// Fazendo a consulta no banco de dados
		String sql = "SELECT * FROM evento WHERE id_criador_evento = ? AND data BETWEEN ? AND ?";
		PreparedStatement stmt = dbConnection.prepareStatement(sql);
		stmt.setString(1, String.valueOf(pessoaId));
		stmt.setDate(2, new java.sql.Date(startDia.getTimeInMillis()));
		stmt.setDate(3, new java.sql.Date(endDia.getTimeInMillis()));
		ResultSet result = stmt.executeQuery();
		
		// Preenchendo a lista com os resultados da consulta
		fillEventosList(eventos, result);
		
		// Retornando a lista
		return eventos;
	}


	@SuppressLint("SimpleDateFormat")
	public List<Evento> getEventosDaPessoa(long pessoaId, Calendar dia) throws SQLException {
		List<Evento> eventos = new ArrayList<Evento>();

		// Fazendo a consulta no banco de dados
		String sql = "SELECT * FROM evento WHERE id_criador_evento = ? AND data = ?";
		PreparedStatement stmt = dbConnection.prepareStatement(sql);
		stmt.setLong(1, pessoaId);
		stmt.setDate(2, new java.sql.Date(dia.getTimeInMillis()));
		ResultSet result = stmt.executeQuery();

		fillEventosList(eventos, result);

		result.close();
		stmt.close();
		// Retornando a lista
		return eventos;
	}
	

	private void fillEventosList(List<Evento> eventos, ResultSet result) throws SQLException {
		while (result.next()) {
			Evento evento = new Evento();
			evento.setId(result.getLong("id_evento"));
			evento.setNome(result.getString("nome"));
			evento.setData(toCalendar(result.getDate("data")));
			evento.setHoraInicio(toCalendar(result.getDate("hora_inicio")));
			evento.setHoraFim(toCalendar(result.getDate("hora_fim")));
			evento.setIdCriadorEvento(result.getLong("id_criador_evento"));
			eventos.add(evento);
		}
	}

	private Calendar toCalendar(java.sql.Date date) {
		Calendar cal = Calendar.getInstance();
		long time = date.getTime();
		cal.setTimeInMillis(time);
		return cal;
	}
}
