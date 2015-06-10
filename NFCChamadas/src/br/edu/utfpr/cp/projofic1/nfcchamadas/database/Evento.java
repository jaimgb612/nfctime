package br.edu.utfpr.cp.projofic1.nfcchamadas.database;

import java.util.Calendar;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class Evento {
	
	public static final String HORA_FORMAT = "HH:mm"; // Baseado em 24 horas
	public static final String DATA_FORMAT = "dd/MM/yyyy";
	
	private long id, idCriadorEvento;
	private String nome;
	private Calendar horaInicio, horaFim, data;
	private boolean gravado;
	
	
	public long getId() {
		return id;
	}
	
	
	public void setId(long id) {
		this.id = id;
	}
	
	
	public String getNome() {
		return nome;
	}
	
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	

	public Calendar getData() {
		return data;
	}
	
	

	public void setData(Calendar data) throws IllegalArgumentException {
		// Primeiramente validando o formato da data
		this.data = data;
	}
	
	
	public Calendar getHoraInicio() {
		return horaInicio;
	}
	

	public void setHoraInicio(Calendar horaInicio) throws IllegalArgumentException {
		this.horaInicio = horaInicio;
	}
	
	
	public Calendar getHoraFim() {
		return horaFim;
	}
	
	

	public void setHoraFim(Calendar horaFim) throws IllegalArgumentException {
		this.horaFim = horaFim;
	}
	
	
	public long getIdCriadorEvento() {
		return idCriadorEvento;
	}
	
	
	public void setIdCriadorEvento(long idCriadorEvento) {
		this.idCriadorEvento = idCriadorEvento;
	}
	
	
	public boolean getGravado() {
		return false;
	}


	public boolean isGravado() {
		return gravado;
	}


	public void setGravado(boolean gravado) {
		this.gravado = gravado;
	}
	
}
