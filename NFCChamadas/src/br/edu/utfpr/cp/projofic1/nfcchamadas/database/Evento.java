package br.edu.utfpr.cp.projofic1.nfcchamadas.database;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("SimpleDateFormat")
public class Evento {
	
	public static final String HORA_FORMAT = "HH:mm"; // Baseado em 24 horas
	public static final String DATA_FORMAT = "dd/MM/yyyy";
	
	private int id;
	private String nome;
	private Date data;
	private String horaInicio, horaFim, idCriadorEvento;
	private boolean gravado;
	
	public int getId() {
		return id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getNome() {
		return nome;
	}
	
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	public Date getData() {
		return data;
	}
	
	
	public void setData(Date data) throws IllegalArgumentException {
		// Primeiramente validando o formato da data
		
		this.data = data;
	}
	
	
	public String getHoraInicio() {
		return horaInicio;
	}
	
	
	public void setHoraInicio(String horaInicio) throws IllegalArgumentException {
	
		this.horaInicio = horaInicio;
	}
	
	
	public String getHoraFim() {
		return horaFim;
	}
	
	
	public void setHoraFim(String horaFim) throws IllegalArgumentException {
		
		this.horaFim = horaFim;
	}
	
	
	public String getIdCriadorEvento() {
		return idCriadorEvento;
	}
	
	
	public void setIdCriadorEvento(String idCriadorEvento) {
		this.idCriadorEvento = idCriadorEvento;
	}
	
	
	private void validateHora(String hora) throws IllegalArgumentException {
		
	}


	public boolean getGravado() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isGravado() {
		return gravado;
	}


	public void setGravado(boolean gravado) {
		this.gravado = gravado;
	}
}