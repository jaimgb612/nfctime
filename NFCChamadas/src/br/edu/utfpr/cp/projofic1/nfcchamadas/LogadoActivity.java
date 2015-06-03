package br.edu.utfpr.cp.projofic1.nfcchamadas;

import java.io.IOException;

import com.sun.mail.util.QDecoderStream;

import android.app.Activity;
import android.app.AlertDialog.Builder;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.utfpr.cp.projofic1.nfcchamada.daoLocal.chamadaDAO;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Chamada;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Pessoa;
import br.edu.utfpr.cp.projofic1.nfcchamadas.util.ObjectSerializer;


public class LogadoActivity extends Activity {
	
	public static final String EXTRA_PESSOA_ID = "pessoa_id";
	private static final int ID_ACTIVITY_LOGADO = 22;
	SharedPreferences preferences;
	long id_evento;
	final Context context = this;
	private Chamada chamada =null;
	chamadaDAO chDAO;

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
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("Serializar POJO Pessoa", "Falha ao tentar serializar", e);
		}


	}
	
	OnItemClickListener listenerEvento = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long id) {
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
				builder.setPositiveButton("Sim",  new DialogInterface.OnClickListener(){
	
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
						Intent i = new Intent(LogadoActivity.this, NFCActivity.class);
						chamada = new Chamada();
						chamada.setDescricao(descricao.getText().toString());
						chamada.setId_evento(id_evento);
						chamada.setQdtAula(qtdAula.getText().toString());
						i.putExtra("chamada", chamada);
						
						chDAO.save(chamada);
						startActivityForResult(i, ID_ACTIVITY_LOGADO);
						
					}
					
					
				});
				
	
				builder.create().show();
				
			}else{
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
		// TODO Auto-generated method stub
		
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
	
	
}
