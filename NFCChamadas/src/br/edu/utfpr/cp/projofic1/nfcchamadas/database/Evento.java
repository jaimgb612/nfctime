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
//		// Primeiramente validando o formato da data
//		try {
//			new SimpleDateFormat(DATA_FORMAT).parse(data);
//		} catch (ParseException e) {
//			throw new IllegalArgumentException("Formato de data errado", e);
//		}
		
		this.data = data;
	}
	
	
	public Calendar getHoraInicio() {
		return horaInicio;
	}
	
	
	public void setHoraInicio(Calendar horaInicio) throws IllegalArgumentException {
//		validateHora(horaInicio);
		this.horaInicio = horaInicio;
	}
	
	
	public Calendar getHoraFim() {
		return horaFim;
	}
	
	
	public void setHoraFim(Calendar horaFim) throws IllegalArgumentException {
//		validateHora(horaFim);
		this.horaFim = horaFim;
	}
	
	
	public long getIdCriadorEvento() {
		return idCriadorEvento;
	}
	
	
	public void setIdCriadorEvento(long idCriadorEvento) {
		this.idCriadorEvento = idCriadorEvento;
	}
	
	
//	private void validateHora(String hora) throws IllegalArgumentException {
//		try {
//			new SimpleDateFormat(HORA_FORMAT).parse(hora);
//		} catch (ParseException e) {
//			throw new IllegalArgumentException("Formato de hora errado", e);
//		}
//	}
}