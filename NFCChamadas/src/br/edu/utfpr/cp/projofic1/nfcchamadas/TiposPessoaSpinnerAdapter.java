package br.edu.utfpr.cp.projofic1.nfcchamadas;

import java.util.List;

import br.edu.utfpr.cp.projofic1.nfcchamadas.database.TipoPessoa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TiposPessoaSpinnerAdapter extends BaseAdapter {
	
	private List<TipoPessoa> tiposPessoa;
	
	private Context context;
	
	
	public TiposPessoaSpinnerAdapter(Context context, List<TipoPessoa> tiposPessoa) {
		this.context = context;
		this.tiposPessoa = tiposPessoa;
	}
	

	@Override
	public int getCount() {
		return tiposPessoa.size();
	}
	

	@Override
	public TipoPessoa getItem(int position) {
		return tiposPessoa.get(position);
	}
	

	@Override
	public long getItemId(int position) {
		return tiposPessoa.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView v = (TextView) inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
		v.setText(getItem(position).getNome());
		return v;
	}

}
