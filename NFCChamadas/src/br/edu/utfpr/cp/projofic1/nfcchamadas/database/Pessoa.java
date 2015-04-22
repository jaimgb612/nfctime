package br.edu.utfpr.cp.projofic1.nfcchamadas.database;

import java.io.Serializable;

public class Pessoa implements Serializable{
	
	private long id, idTipoPessoa;
	private String nome, email, senha, rAcademico;
	
	
	public long getId() {
		return id;
	}
	
	
	public void setId(long id) {
		this.id = id;
	}
	
	
	public long getIdTipoPessoa() {
		return idTipoPessoa;
	}
	
	
	public void setIdTipoPessoa(long idTipoPessoa) {
		this.idTipoPessoa = idTipoPessoa;
	}
	
	
	public String getNome() {
		return nome;
	}
	
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	public String getEmail() {
		return email;
	}
	
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public String getSenha() {
		return senha;
	}
	
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	public String getrAcademico() {
		return rAcademico;
	}
	
	
	public void setrAcademico(String rAcademico) {
		this.rAcademico = rAcademico;
	}
}