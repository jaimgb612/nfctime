package br.edu.utfpr.cp.projofic1.nfcchamadas;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import br.edu.utfpr.cp.projofic1.nfcchamada.daoLocal.chamadaDAO;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Chamada;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Pessoa;
import br.edu.utfpr.cp.projofic1.nfcchamadas.util.ObjectSerializer;


public class LogadoActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	
	public static final String EXTRA_PESSOA_ID = "pessoa_id";
	
	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private Calendar startDia, endDia;
	SharedPreferences preferences;
	private Button bStartDia, bEndDia;
	long id_evento;
	final Context context = this;
	private Chamada chamada =null;
	chamadaDAO chDAO;
	private static final int ID_ACTIVITY_LOGADO = 22;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logado);


		//altera preference para logado
		preferences= getSharedPreferences("sessaoPessoa", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("status", false);
		editor.commit();

		Pessoa pessoaLoagada;
		try {
			//Recuperando objeto pessoa serializada ! no SharedPreferences
			pessoaLoagada = (Pessoa) ObjectSerializer.deserialize(preferences.getString("pessoaLogada", 
					ObjectSerializer.serialize(new Pessoa())));

			// Mostrando os dados da pessoa logada


			//	Intent i = new Intent(LogadoActivity.this, NFCActivity.class);
			//	i.putExtra("pessoa", pessoaLogada);
			//startActivity(i); 
			TextView tvDadosPessoa = (TextView) findViewById(R.id.tvDadosUsuario);

			tvDadosPessoa.setText(
					"Nome: " + pessoaLoagada.getNome() + "\n" +
							"E-mail: " + pessoaLoagada.getEmail() + "\n" +
							"Reg. Acad�mico: " + pessoaLoagada.getrAcademico());
			
			// Preenchendo a lista
			ListView eventosList = (ListView) findViewById(android.R.id.list);
			eventosList.setAdapter(new EventosListAdapter(this, pessoaLoagada.getId()));
			eventosList.setOnItemClickListener(listenerEvento);
			
			bStartDia = (Button) findViewById(R.id.bStartDia);
			bEndDia = (Button) findViewById(R.id.bEndDia);
			
			bStartDia.setOnClickListener(this);
			bEndDia.setOnClickListener(this);
			
			CheckBox chkboxHoje = (CheckBox) findViewById(R.id.chboxHoje);
			chkboxHoje.setOnCheckedChangeListener(this);
			chkboxHoje.setChecked(true);
			
			startDia = Calendar.getInstance();
			String currentDate = dateFormat.format((endDia = Calendar.getInstance()).getTime());
			bStartDia.setText(currentDate);
			bEndDia.setText(currentDate);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("Serializar POJO Pessoa", "Falha ao tentar serializar", e);
		}
	}
	
	OnItemClickListener listenerEvento = new OnItemClickListener() {


		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long id) {
				id_evento=id;
			
			chDAO = new chamadaDAO(context);
			
			
			
			if((chamada = chDAO.getChamada(id_evento))== null){
			
				Builder builder = new Builder(context);
				
				LayoutInflater li = LayoutInflater.from(context);
				View promptsView = li.inflate(R.layout.dialog_chamada, null);
	
				final EditText qtdAula = (EditText) promptsView.findViewById(R.id.edtQtd);
				final EditText descricao = (EditText) promptsView.findViewById(R.id.edtDescricao);
				
				
				builder.setView(promptsView);
				builder.setNegativeButton("Não", null);
				builder.setTitle("Chamada");
				builder.setPositiveButton("Abrir",  new DialogInterface.OnClickListener(){
	
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
						Intent i = new Intent(LogadoActivity.this, NFCActivity.class);
						chamada = new Chamada();
						chamada.setDescricao(descricao.getText().toString());
						chamada.setId_evento(id_evento);
						chamada.setQdtAula(qtdAula.getText().toString());
						chamada.setGravado(1);
						chDAO.save(chamada);
						i.putExtra("chamada", chamada);
						
					
						startActivityForResult(i, ID_ACTIVITY_LOGADO);
						
					}
					
					
				});
				
	
				builder.create().show();
				
			}else if(chamada.getGravado()==2){
				Builder builder = new Builder(LogadoActivity.this);
				builder.setTitle("Chamada já realizada");
				builder.setMessage("Chamada realizada para esse evento !");
				builder.setNegativeButton("Ok! ",null);
				builder.create().show();
			}
			else{
				Intent i = new Intent(LogadoActivity.this, NFCActivity.class);
				i.putExtra("chamada", chamada);
				startActivityForResult(i, ID_ACTIVITY_LOGADO);
				
			}
		}
			
	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.principal, menu);
	    return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_settings:
			openCaptura();
			return true;
		case R.id.item1:
			logout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
  
	
	//Só para teste !!!!!
	private void openCaptura() {
		Intent i = new Intent(LogadoActivity.this, NFCActivity.class);
		startActivity(i);
		
	}

	
	private void logout() {

		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		this.finish();
		
	}

	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bStartDia || v.getId() == R.id.bEndDia) {
			final Calendar dia = v.getId() == R.id.bStartDia ? startDia : endDia;
			DatePickerDialog dialData = new DatePickerDialog(this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// Alterando a data
					dia.set(Calendar.YEAR, year);
					dia.set(Calendar.MONTH, monthOfYear);
					dia.set(Calendar.DAY_OF_MONTH, dayOfMonth);

					// Atualizando a lista
					ListView eventosList = (ListView) LogadoActivity.this.findViewById(android.R.id.list);
					EventosListAdapter listAdapter = (EventosListAdapter) eventosList.getAdapter();
					listAdapter.changeDate(startDia, endDia);

					// Atualizando os bot�es com as novas datas
					bStartDia.setText(dateFormat.format(startDia.getTime()));
					bEndDia.setText(dateFormat.format(endDia.getTime()));
				}
			}, dia.get(Calendar.YEAR), dia.get(Calendar.MONTH), dia.get(Calendar.DAY_OF_MONTH));
			dialData.show();
		}
	}

	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		ListView list = (ListView) findViewById(android.R.id.list);
		EventosListAdapter adapter = (EventosListAdapter) list.getAdapter();
		if (isChecked) {
			bStartDia.setEnabled(false);
			bEndDia.setEnabled(false);
			adapter.setEventosDeHoje();
		} else {
			bStartDia.setEnabled(true);
			bEndDia.setEnabled(true);
			adapter.changeDate(startDia, endDia);
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == RESULT_OK) {
			if(data != null)
			chamada =(Chamada) data.getSerializableExtra("chamada");
			chamada.setGravado(2);
			chDAO.upgrade(chamada);
			ListView list = (ListView) findViewById(android.R.id.list);
			EventosListAdapter adapter = (EventosListAdapter) list.getAdapter();
			adapter.notifyDataSetChanged();
			System.err.println("Aeee volteou e SALVOUUUUUU");
			
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		ListView list = (ListView) findViewById(android.R.id.list);
		EventosListAdapter adapter = (EventosListAdapter) list.getAdapter();
		//adapter.setExited();
	}
}
