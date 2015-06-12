package br.edu.utfpr.cp.projofic1.nfcchamadas;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.DatabaseDAO;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Evento;

public class EventosListAdapter extends BaseAdapter {
	
	private Context context;
	private List<Evento> eventos;
	private long pessoaId;
	
	private boolean rerun = false, running = false;
	
	private Calendar startData, endData;
	
	private class QueryEventosTask extends AsyncTask<Long, Void, Object> {
		
		@Override
		protected Object doInBackground(Long... params) {
			running = true;
			rerun = false;
			try {
				DatabaseDAO dao = new DatabaseDAO();
				// Eventos de hoje
				if (endData == null)
					return dao.getEventosDaPessoa(params[0], startData);
				// Eventos invervalo de dias
				return dao.getEventosDaPessoa(params[0], startData, endData);
			} catch (SQLException e) {
				return e;
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			if (result instanceof SQLException) {
				// Quando houve um problema na recupera��o dos eventos no banco de dados
				SQLException e = (SQLException) result;
				Log.e("Query eventos", "Falha na query", e);
				// Nova tentativa
				rerun = true;
			} else if (result instanceof List<?>) {
				// Mandando atualizar a lista
				eventos = (List<Evento>) result;
				//Log.d("Hora", eventos.get(0).getHoraInicio().toString());
				EventosListAdapter.this.notifyDataSetChanged();
			}
			if (rerun)
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						new QueryEventosTask().execute(pessoaId);
					}
				});
			else
				running = false;
		};
	};
	
	
	public EventosListAdapter(Context context, long pessoaId) {
		this.context = context;
		this.pessoaId = pessoaId;
		setEventosDeHoje();
	}
	
	
	public void changeDate(Calendar startData, Calendar endData) {
		this.startData = startData;
		this.endData = endData;
		requestEventosUpdate();
	}
	
	
	public void setEventosDeHoje() {
		this.startData = Calendar.getInstance();
		this.endData = null;
		requestEventosUpdate();
	}
	
	
	public void requestEventosUpdate() {
		// Descantando a lista e remostrando a mensagem de carregando
		eventos = null;
		notifyDataSetChanged();
		
		// Requesitando a query
		if (running)
			rerun = true;
		else
			new QueryEventosTask().execute(pessoaId);
	}
	
	
	@Override
	public int getCount() {
		if (eventos == null)
			return 1;
		return eventos.size();
	}
	

	@Override
	public Evento getItem(int position) {
		if (eventos == null)
			return null;
		return eventos.get(position);
	}
	

	@Override
	public long getItemId(int position) {
		if (eventos == null)
			return 0;
		return eventos.get(position).getId();
	}
	

	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (eventos == null) {
			TextView tvCarregando = new TextView(context);
			tvCarregando.setTextAppearance(context, android.R.attr.textAppearanceMedium);
			tvCarregando.setTextColor(Color.RED);
			tvCarregando.setText(R.string.eventos_list_carregando_os_eventos);
			return tvCarregando;
		}
		
		Evento evento = getItem(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.evento_list_item, null);
		
		// Escrevendo o nome
		TextView tv = (TextView) view.findViewById(R.id.tvEventoListItemNome);
		tv.setText(evento.getNome());
		
		// Escrevendo a data
	
		
		tv = (TextView) view.findViewById(R.id.tvEventoListItemData);
		tv.setText(String.format(tv.getText().toString(), 
				new SimpleDateFormat("dd/MM/yyyy").format(evento.getData().getTime())  ));
		
		
		// Escrevendo a hora de in�cio e fim
		tv = (TextView) view.findViewById(R.id.tvEventoListItemHoraInicoFim);
		tv.setText(String.format(tv.getText().toString(),   new SimpleDateFormat("HH:mm").format(evento.getHoraInicio().getTime()), 
				new SimpleDateFormat("HH:mm").format(evento.getHoraFim().getTime())));
		return view;
	}

}
