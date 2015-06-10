package br.edu.utfpr.cp.projofic1.nfcchamadas.database;

import java.util.Calendar;

import android.annotation.SuppressLint;
<<<<<<< HEAD
=======

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
>>>>>>> branch 'master' of https://github.com/joaobracaioli/nfctime


@SuppressLint("SimpleDateFormat")
public class Evento {
	
	public static final String HORA_FORMAT = "HH:mm"; // Baseado em 24 horas
	public static final String DATA_FORMAT = "dd/MM/yyyy";
	
<<<<<<< HEAD
	private long id, idCriadorEvento;
	private String nome;
	private Calendar horaInicio, horaFim, data;
=======
	private int id;
	private String nome;
	private Date data;
	private String horaInicio, horaFim, idCriadorEvento;
	private boolean gravado;
>>>>>>> branch 'master' of https://github.com/joaobracaioli/nfctime
	
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
	
	
<<<<<<< HEAD
	public Calendar getData() {
=======
	public Date getData() {
>>>>>>> branch 'master' of https://github.com/joaobracaioli/nfctime
		return data;
	}
	
	
<<<<<<< HEAD
	public void setData(Calendar data) throws IllegalArgumentException {
//		// Primeiramente validando o formato da data
//		try {
//			new SimpleDateFormat(DATA_FORMAT).parse(data);
//		} catch (ParseException e) {
//			throw new IllegalArgumentException("Formato de data errado", e);
//		}
=======
	public void setData(Date data) throws IllegalArgumentException {
		// Primeiramente validando o formato da data
>>>>>>> branch 'master' of https://github.com/joaobracaioli/nfctime
		
		this.data = data;
	}
	
	
	public Calendar getHoraInicio() {
		return horaInicio;
	}
	
	
<<<<<<< HEAD
	public void setHoraInicio(Calendar horaInicio) throws IllegalArgumentException {
//		validateHora(horaInicio);
=======
	public void setHoraInicio(String horaInicio) throws IllegalArgumentException {
	
>>>>>>> branch 'master' of https://github.com/joaobracaioli/nfctime
		this.horaInicio = horaInicio;
	}
	
	
	public Calendar getHoraFim() {
		return horaFim;
	}
	
	
<<<<<<< HEAD
	public void setHoraFim(Calendar horaFim) throws IllegalArgumentException {
//		validateHora(horaFim);
=======
	public void setHoraFim(String horaFim) throws IllegalArgumentException {
		
>>>>>>> branch 'master' of https://github.com/joaobracaioli/nfctime
		this.horaFim = horaFim;
	}
	
	
	public long getIdCriadorEvento() {
		return idCriadorEvento;
	}
	
	
	public void setIdCriadorEvento(long idCriadorEvento) {
		this.idCriadorEvento = idCriadorEvento;
	}
	
	
<<<<<<< HEAD
//	private void validateHora(String hora) throws IllegalArgumentException {
//		try {
//			new SimpleDateFormat(HORA_FORMAT).parse(hora);
//		} catch (ParseException e) {
//			throw new IllegalArgumentException("Formato de hora errado", e);
//		}
//	}
=======
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
>>>>>>> branch 'master' of https://github.com/joaobracaioli/nfctime
}
