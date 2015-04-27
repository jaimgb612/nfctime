package br.edu.utfpr.cp.projofic1.nfcchamadas;

import java.io.IOException;
import java.sql.SQLException;





import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.DatabaseDAO;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Pessoa;
import br.edu.utfpr.cp.projofic1.nfcchamadas.util.ObjectSerializer;


public class LogadoActivity extends Activity {
	
	public static final String EXTRA_PESSOA_ID = "pessoa_id";
	
	SharedPreferences preferences;
	

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
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
		}
	
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
           // openSearch();
            return true;
        case R.id.item1:
            logout();
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	}

	private void logout() {

		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		this.finish();
		
	}
	
	
}
