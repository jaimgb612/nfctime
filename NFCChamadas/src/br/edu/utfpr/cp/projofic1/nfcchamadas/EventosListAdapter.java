package br.edu.utfpr.cp.projofic1.nfcchamadas;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
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
	
	private AsyncTask<Long, Void, Object> queryEventosTask = new AsyncTask<Long, Void, Object>() {
		
		@Override
		protected Object doInBackground(Long... params) {
			try {
				DatabaseDAO dao = new DatabaseDAO();
				return dao.getEventosDaPessoa(params[0]);
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
				this.execute(pessoaId);
			} else if (result instanceof List<?>) {
				// Mandando atualizar a lista
				eventos = (List<Evento>) result;
				EventosListAdapter.this.notifyDataSetChanged();
			}
		};
	};
	
	
	public EventosListAdapter(Context context, long pessoaId) {
		this.context = context;
		this.pessoaId = pessoaId;
		queryEventosTask.execute(pessoaId);
	}
	
	
	public void requestEventosUpdate() {
		// Descantando a lista e remostrando a mensagem de carregando
		eventos = null;
		notifyDataSetChanged();
		
		// Requesitando a query
		queryEventosTask.execute(pessoaId);
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
