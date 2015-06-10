package br.edu.utfpr.cp.projofic1.nfcchamadas.database;

import java.io.Serializable;
import java.util.List;

public class Chamada implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -855168737472700300L;
	private long id_chamada;
	private long id_evento;
	private String descricao;
	private String qdtAula;
	private List<Presenca> presenca;
	private int quantidade;
	private int gravado;
	
	public long getId_evento() {
		return id_evento;
	}
	public void setId_evento(long id_evento) {
		this.id_evento = id_evento;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getQdtAula() {
		return qdtAula;
	}
	public void setQdtAula(String qdtAula) {
		this.qdtAula = qdtAula;
	}
	public long getId_chamada() {
		return id_chamada;
	}
	public void setId_chamada(long id_chamada) {
		this.id_chamada = id_chamada;
	}
	public List<Presenca> getPresenca() {
		return presenca;
	}
	public void setPresenca(List<Presenca> presenca) {
		this.presenca = presenca;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public int getGravado() {
		return gravado;
	}
	public void setGravado(int gravado) {
		this.gravado = gravado;
	}
	
	
	
	
	

}
