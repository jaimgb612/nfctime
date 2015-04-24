package br.edu.utfpr.cp.projofic1.nfcchamadas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.DatabaseDAO;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Pessoa;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.TipoPessoa;

public class CadastrarUsuarioActivity extends Activity {
	
	public static final String RESULT_DATA_RACADEMICO = "RAcademico";
	public static final String RESULT_DATA_SENHA = "senha";
	
	private EditText etNome, etEmail, etRAcademico, etSenha, etConfirmaSenha;
	private Spinner spnTipoPessoa;
	private Button bCancel, bOk;
	
	private DatabaseDAO dbDAO;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastrar_usuario);
		setResult(RESULT_CANCELED);
		
		// Pegando as views
		etNome = (EditText) findViewById(R.id.etNome);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etRAcademico = (EditText) findViewById(R.id.etRAcademico);
		etSenha = (EditText) findViewById(R.id.etSenha);
		etConfirmaSenha = (EditText) findViewById(R.id.etConfirmaSenha);
		spnTipoPessoa = (Spinner) findViewById(R.id.spnTipoPessoa);
		bCancel = (Button) findViewById(R.id.bCancel);
		bOk = (Button) findViewById(R.id.bOk);
		
		// Preenchendo o Spinner de tipos de pessoa
		List<TipoPessoa> tiposPessoa = new ArrayList<TipoPessoa>();
		TipoPessoa tipoPessoa = new TipoPessoa();
		tipoPessoa.setId(1);
		tipoPessoa.setNome("Professor");
		tiposPessoa.add(tipoPessoa);
		tipoPessoa = new TipoPessoa();
		tipoPessoa.setId(2);
		tipoPessoa.setNome("Aluno");
		tiposPessoa.add(tipoPessoa);
		spnTipoPessoa.setAdapter(new TiposPessoaSpinnerAdapter(this, tiposPessoa));
		
		// Criando os eventos dos botões
		bCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		bOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				submit();
			}
		});
	}
	
	
	private void submit() {
		final ProgressDialog progDial = ProgressDialog.show(CadastrarUsuarioActivity.this, "", "");
		progDial.setCancelable(false);
		new AsyncTask<Void, Void, String>() {
			
			private Pessoa pessoa;
			
			@Override
			protected String doInBackground(Void... params) {
				String validar = validarCampos();
				
				// Se todos os campos são válidos
				if (validar == null) {
					pessoa = new Pessoa();
					pessoa.setNome(etNome.getText().toString().trim());
					pessoa.setEmail(etEmail.getText().toString().trim());
					pessoa.setrAcademico(etRAcademico.getText().toString().trim());
					pessoa.setSenha(etSenha.getText().toString());
					pessoa.setIdTipoPessoa(spnTipoPessoa.getSelectedItemId());
					try {
						dbDAO.cadastrarPessoa(pessoa);
					} catch (SQLException e) {
						// Caso houve um problema ao cadastrar a pessoa na Base de Dados no servidor
						Log.e("Conexão com o Banco de dados no servidor", "Falha ao cadastrar pessoa no Banco", e);
						return getResources().getString(R.string.falha_ao_cadastrar_usuario);
					}
					
				} else {
					// Caso haja algum campo inválido
					return validar;
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				progDial.cancel();
				
				if (result != null) {
					// Se houve algum problema
					Toast.makeText(CadastrarUsuarioActivity.this, result, Toast.LENGTH_SHORT).show();
					
				} else {
					// Se foi um sucesso, termina a activity retornando os dados do novo usuário para fazer login
					Intent resultData = new Intent();
					resultData.putExtra(RESULT_DATA_RACADEMICO, pessoa.getrAcademico());
					resultData.putExtra(RESULT_DATA_SENHA, pessoa.getSenha());
					setResult(RESULT_OK, resultData);
					finish();
				}
			}
		}.execute();
	}
	
	
	private String validarCampos() {
		// Validando se inseriu um nome
		if (etNome.getText().toString().trim().equals(""))
			return getResources().getString(R.string.cadastrar_usuario_validar_insira_nome);
		
		// Validando o e-mail
		if (etEmail.getText().toString().trim().equals(""))
			return getResources().getString(R.string.cadastrar_usuario_validar_insira_email_valido);
		try {
			InternetAddress netAddr = new InternetAddress(etEmail.getText().toString().trim());
			netAddr.validate();
		} catch (AddressException e) {
			return getResources().getString(R.string.cadastrar_usuario_validar_insira_email_valido);
		}
		
		// Validando se inseriu um registro acadêmico
		if (etRAcademico.getText().toString().trim().equals(""))
			return getResources().getString(R.string.cadastrar_usuario_validar_insira_registro_academico);
		
		// Validando a senha
		if (etSenha.getText().toString().equals(""))
			return getResources().getString(R.string.cadastrar_usuario_validar_insira_senha);
		
		// Validando confirma senha
		if (!etSenha.getText().toString().equals(etConfirmaSenha.getText().toString()))
			return getResources().getString(R.string.cadastrar_usuario_validar_confirma_senha_nao_coincide);
		
		// Validando se foi selecionado um tipo de pessoa
		if (spnTipoPessoa.getSelectedItemId() == Spinner.INVALID_ROW_ID)
			return getResources().getString(R.string.cadastrar_usuario_validar_selecione_tipo_pessoa);
		
		// Se os campos estiverem válidos
		return null;
	}
	
	
	@Override
	protected void onDestroy() {
		try {
			if (dbDAO != null)
				dbDAO.close();
		} catch (SQLException e) {
			Log.e("Conexão com o Banco de dados no servidor", "Falha ao fechar a conexão", e);
		}
		super.onDestroy();
	}
}
