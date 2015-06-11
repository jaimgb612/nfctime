package br.edu.utfpr.cp.projofic1.nfcchamadas.database;

public class Presenca {
	
	private long id;
	private long ra;
	private String nome;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getRa() {
		return ra;
	}
	public void setRa(long ra) {
		this.ra = ra;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ""+ra+": "+nome;
	}
	
	
	
	

}
