package br.edu.utfpr.cp.projofic1.nfcchamadas;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.DatabaseDAO;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Pessoa;

public class LogadoActivity extends Activity {
	
	public static final String EXTRA_PESSOA_ID = "pessoa_id";
	
	private Pessoa pessoaLogada;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logado);
		
		new AsyncTask<Void, Void, SQLException>() {
			@Override
			protected SQLException doInBackground(Void... params) {
				// Recuperando a pessoa no banco, que foi logada
				try {
					DatabaseDAO dbDAO = new DatabaseDAO();
					pessoaLogada = dbDAO.getPessoaById(getIntent().getLongExtra(EXTRA_PESSOA_ID, -1));
				} catch (SQLException e) {
					return e;
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(SQLException result) {
				if (result != null) {
					// Se houve um problema na comunica��o com o Banco de dados
					Log.e("Conex�o com o servidor de Banco de Dados", "Falha na opera��o ou conex�o", result);
					Toast.makeText(LogadoActivity.this, R.string.falha_na_conexao_com_servidor, Toast.LENGTH_SHORT).show();
					finish();
				} else {
					// Mostrando os dados da pessoa logada
					

					Intent i = new Intent(LogadoActivity.this, NFCActivity.class);
				//	i.putExtra("pessoa", pessoaLogada);
					startActivity(i); 
			//		TextView tvDadosPessoa = (TextView) findViewById(R.id.tvDadosUsuario);
					/*
					tvDadosPessoa.setText(
							"Nome: " + pessoaLogada.getNome() + "\n" +
							"E-mail: " + pessoaLogada.getEmail() + "\n" +
							"Reg. Acad�mico: " + pessoaLogada.getrAcademico());
					*/
				}
			}
		}.execute();
	}
}
