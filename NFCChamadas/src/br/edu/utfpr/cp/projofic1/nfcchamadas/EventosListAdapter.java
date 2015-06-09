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
	private Calendar startDia, endDia;
	
	private boolean updating = false, updateAgain = false, exited = false;
	
	
	private class QueryEventosTask extends AsyncTask<Long, Void, Object> {
		
		@Override
		protected synchronized Object doInBackground(Long... params) {
			updating = true;
			try {
				DatabaseDAO dao = new DatabaseDAO();
				if (endDia != null)
					return dao.getEventosDaPessoa(params[0], startDia, endDia);
				else
					return dao.getEventosDaPessoa(params[0], startDia);
			} catch (SQLException e) {
				return e;
			} finally {
				updating = false;
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected synchronized void onPostExecute(Object result) {
			if (result instanceof SQLException) {
				// Quando houve um problema na recuperação dos eventos no banco de dados
				SQLException e = (SQLException) result;
				Log.e("Query eventos", "Falha na query", e);
				// Nova tentativa
				reexecute();
			} else if (result instanceof List<?>) {
				// Mandando atualizar a lista
				eventos = (List<Evento>) result;
				EventosListAdapter.this.notifyDataSetChanged();
			}
			if (updateAgain && !exited) {
				updateAgain = false;
				reexecute();
			}
		}
		
		private void reexecute() {
			new Handler().post(new Runnable() {
				
				@Override
				public void run() {
					new QueryEventosTask().execute(pessoaId);
				}
			});
		}
	}
	
	
	public EventosListAdapter(Context context, long pessoaId, Calendar startDia, Calendar endDia) {
		if (startDia == null || endDia == null)
			throw new IllegalArgumentException("Os dias não podem ser null");
		this.context = context;
		this.pessoaId = pessoaId;
		this.startDia = startDia;
		this.endDia = endDia;
		new QueryEventosTask().execute(pessoaId);
	}
	
	
	public EventosListAdapter(Context context, long pessoaId) {
		this.context = context;
		this.pessoaId = pessoaId;
		this.startDia = Calendar.getInstance();
		this.endDia = null;
		new QueryEventosTask().execute(pessoaId);
	}
	
	
	public void setExited() {
		exited = true;
	}
	
	
	public void requestEventosUpdate() {
		// Descantando a lista e remostrando a mensagem de carregando
		eventos = null;
		notifyDataSetChanged();
		
		// Requesitando a query
		if  (!updating)
			new QueryEventosTask().execute(pessoaId);
		else
			updateAgain = true;
	}
	
	
	public void setIntervaloDiaAndUpdate(Calendar startDia, Calendar endDia) {
		if (startDia == null || endDia == null)
			throw new IllegalArgumentException("Os dias não podem ser null");
		this.startDia = startDia;
		this.endDia = endDia;
		requestEventosUpdate();
	}
	
	
	public void updateEventosComDiaAtual() {
		this.startDia = Calendar.getInstance();
		this.endDia = null;
		requestEventosUpdate();
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
	

	@SuppressLint({ "ViewHolder", "InflateParams", "SimpleDateFormat" }) @Override
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
		SimpleDateFormat dataHoraFormat = new SimpleDateFormat(Evento.DATA_FORMAT);
		tv = (TextView) view.findViewById(R.id.tvEventoListItemData);
		tv.setText(String.format(tv.getText().toString(), dataHoraFormat.format(evento.getData().getTime())));
		
		// Escrevendo a hora de início e fim
		dataHoraFormat = new SimpleDateFormat(Evento.HORA_FORMAT);
		tv = (TextView) view.findViewById(R.id.tvEventoListItemHoraInicoFim);
		tv.setText(String.format(tv.getText().toString(),
				dataHoraFormat.format(evento.getHoraInicio().getTime()),
				dataHoraFormat.format(evento.getHoraFim().getTime())));
		return view;
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		setExited();
	}

}
