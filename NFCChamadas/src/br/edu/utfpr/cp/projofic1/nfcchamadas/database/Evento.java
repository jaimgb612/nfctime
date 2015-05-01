package br.edu.utfpr.cp.projofic1.nfcchamadas.database;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@SuppressLint("SimpleDateFormat")
public class Evento {
	
	public static final String HORA_FORMAT = "HH:mm"; // Baseado em 24 horas
	public static final String DATA_FORMAT = "dd/MM/yyyy";
	
	private long id;
	private String nome, data;
	private String horaInicio, horaFim, idCriadorEvento;
	
	
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
	
	
	public String getData() {
		return data;
	}
	
	
	public void setData(String data) throws IllegalArgumentException {
		// Primeiramente validando o formato da data
		try {
			new SimpleDateFormat(DATA_FORMAT).parse(data);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Formato de data errado", e);
		}
		
		this.data = data;
	}
	
	
	public String getHoraInicio() {
		return horaInicio;
	}
	
	
	public void setHoraInicio(String horaInicio) throws IllegalArgumentException {
		validateHora(horaInicio);
		this.horaInicio = horaInicio;
	}
	
	
	public String getHoraFim() {
		return horaFim;
	}
	
	
	public void setHoraFim(String horaFim) throws IllegalArgumentException {
		validateHora(horaFim);
		this.horaFim = horaFim;
	}
	
	
	public String getIdCriadorEvento() {
		return idCriadorEvento;
	}
	
	
	public void setIdCriadorEvento(String idCriadorEvento) {
		this.idCriadorEvento = idCriadorEvento;
	}
	
	
	private void validateHora(String hora) throws IllegalArgumentException {
		try {
			new SimpleDateFormat(HORA_FORMAT).parse(hora);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Formato de hora errado", e);
		}
	}
}