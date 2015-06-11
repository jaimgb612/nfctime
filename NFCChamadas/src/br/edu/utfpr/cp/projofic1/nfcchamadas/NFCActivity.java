package br.edu.utfpr.cp.projofic1.nfcchamadas;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.cp.projofic1.nfccahamdas.factory.NDEFRecordFactory;
import br.edu.utfpr.cp.projofic1.nfcchamada.daoLocal.chamadaDAO;
import br.edu.utfpr.cp.projofic1.nfcchamada.daoLocal.presencaDAO;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Chamada;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.DatabaseDAO;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Presenca;
import br.edu.utfpr.cp.projofic1.nfcchamadas.model.BaseRecord;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NFCActivity extends Activity {
	
	

	public static final String MIME_TEXT_PLAIN = "text/plain";
	private NfcAdapter nfc;
	private TextView ultimaLida;
	
	PendingIntent nfcPendingIntent;
    IntentFilter[] intentFiltersArray;
	private TextView recNumberTxt;
    private ListView lPresentes;
    private List<Presenca> presentes;
    ArrayAdapter<Presenca> adapter ;
    List<BaseRecord> dataList;
    int positionCorrent;
	private Button salvar;
	public Chamada chamada;
	public DatabaseDAO dbDAO;
	public presencaDAO presenca;
	public chamadaDAO chamadaDAO;
	private Context context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
		
		nfc = NfcAdapter.getDefaultAdapter(this);
		
		
		recNumberTxt = (TextView) findViewById(R.id.txtTag);
		lPresentes = (ListView) findViewById(R.id.listPresentes);
		ultimaLida = (TextView) findViewById(R.id.txtTag);
		TextView txtEvento = (TextView) findViewById(R.id.txtEvento);
		TextView txtQtd = (TextView) findViewById(R.id.txtQtd);
		TextView txtDesc = (TextView) findViewById(R.id.txtDescricao);
		salvar =(Button) findViewById(R.id.btnSalvar);
		
		context = this;

		
		if (nfc == null || !(nfc.isEnabled())) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "NFC não ativo ou não suportado, favor ativar!!!", Toast.LENGTH_LONG).show();
            finish();
            return;
 
        }

		Intent nfcIntent = new Intent(this, getClass());
		nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		chamada = (Chamada) getIntent().getSerializableExtra("chamada");;
		
		
		
		txtQtd.setText("Qtd de aulas:"+chamada.getQdtAula());
		txtDesc.setText("Descrição: "+chamada.getDescricao());
		txtEvento.setText("Id do evento: "+chamada.getId_evento());
		
		
		nfcPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
		
		//Intercpta leitura de TAG
		
		IntentFilter tagIntentFilter =  new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		
		try {
            //tagIntentFilter.addDataScheme("http");
            // tagIntentFilter.addDataScheme("vnd.android.nfc");
           // tagIntentFilter.addDataScheme("tel");
			//tagIntentFilter.addDataScheme(scheme);
            tagIntentFilter.addDataType(MIME_TEXT_PLAIN);
            intentFiltersArray = new IntentFilter[]{tagIntentFilter};
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
		
		presenca = new presencaDAO(this);
		
		//monta lista de presentes 
		lPresentes.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lPresentes.setTextFilterEnabled(true);
		updateUI(); //Atualiza lista
		
		//Botão salvar
		//if(presenca.getByPresentesEvento(chamada).size()>2){
		salvar.setOnClickListener(listenerSalvar);
		//}
	
	}

	public void updateUI() {
		presentes = presenca.getByPresentesEvento(chamada);
		
		adapter = new  ArrayAdapter<Presenca>(this,android.R.layout.simple_list_item_1, android.R.id.text1, presentes);
		lPresentes.setAdapter(adapter);
		lPresentes.setOnItemLongClickListener(listenerAluno);
		
	}
	
	OnItemLongClickListener listenerAluno = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int idSelect, long arg3) {
			positionCorrent= idSelect;
			
			Builder builder = new Builder(NFCActivity.this);
			builder.setTitle("Atenção");
			builder.setNeutralButton("Cancelar", null);
			builder.setMessage("Deseja retirar a presença do aluno "+presentes.get(positionCorrent).getNome()+" ?");
			
			builder.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					presenca.delete(presentes.get(positionCorrent));
					 updateUI();
				}
			});
			 
		     builder.create().show();
		    // adapter.notifyDataSetChanged();
		    
			return false;
			
	
		}

	}; 
	
	OnClickListener listenerSalvar = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			submit();
			
		}
	};
	
	private void submit() {
		final ProgressDialog progDial = ProgressDialog.show(NFCActivity.this, "", "");
		progDial.setCancelable(false);
		
		new AsyncTask<Void, Void, String>() {
			
			
			
			@Override
			protected String doInBackground(Void... params) {
			
				
				try {
					
					
					dbDAO = new DatabaseDAO();
					chamadaDAO = new chamadaDAO(context);
					chamada = dbDAO.salvarPresenca(chamada, presenca);
					chamadaDAO.save(chamada);
					
					
				} catch (SQLException e) {
					// Caso houve um problema ao cadastrar a pessoa na Base de Dados no servidor
					Log.e("Conex�o com o Banco de dados no servidor", "Falha ao cadastrar pessoa no Banco", e);
					return getResources().getString(R.string.ERRO_SALVAR_PRESENCA);
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				
				super.onPostExecute(result);
				
				progDial.cancel();
				
				if (result != null) {
					// Se houve algum problema
					Toast.makeText(NFCActivity.this, result, Toast.LENGTH_SHORT).show();
					
				} else {
					// Se foi um sucesso, termina a activity retornando os dados do novo usu�rio para fazer login
					Intent resultData = new Intent();
					
					setResult(RESULT_OK, resultData);
					finish();
				}
			}
			
			
			
		}.execute();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			
			presenca.destruirTabela();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 @Override
	    protected void onResume() {
	        super.onResume();
	   
	        nfc.enableForegroundDispatch(this,
	        		//Seta como desejo manipular a utlização da tag
	        		//no caso uma arry de intercptação
	        		nfcPendingIntent,
	        		intentFiltersArray,null);
	        handleIntent(getIntent());
	       
	    }

	 @Override
	    protected void onPause() {
	       
	        super.onPause();
	        nfc.disableForegroundNdefPush(this);
	 }
	 
	  private void handleIntent(Intent i) {

	        Log.d("NFC", "Intent [" + i + "]");

	        getTag(i);
	 }
	@Override
	protected void onNewIntent(Intent intent) {
		 Log.d("Nfc", "New intent");
		 getTag(intent);
		 

	}
	
	 private void getTag(Intent i) {
	        if (i == null)
	            return ;

	        String type = i.getType();
	        String action = i.getAction();
	        dataList = new ArrayList<BaseRecord>();
	        Presenca aluno = new Presenca();
	        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
	            Log.d("Nfc", "Action NDEF Found");
	            Parcelable[] parcs = i.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

	            // List record


	            for (Parcelable p : parcs) {
	                NdefMessage msg = (NdefMessage) p;
	                final int numRec = msg.getRecords().length;
	                recNumberTxt.setText(String.valueOf(numRec));

	                NdefRecord[] records = msg.getRecords();
	                for (NdefRecord record: records) {
	                    BaseRecord result = NDEFRecordFactory.createRecord(record);
	                  //  if (result instanceof RDTSpRecord)
	                    //    dataList.addAll( ((RDTSpRecord) result).records);
	                    //else
	                    ultimaLida.setText(result.toString());
	   	             try{
	   	             String[] teste = result.payload.toString().split(":");
	   	             
	   	             
	   	            
	   	             
		   	            if(presenca.getPresente(teste[0])){
		   	            	Toast.makeText(NFCActivity.this, "Tag já lida !", Toast.LENGTH_LONG).show();
		   	             }else{
		   	            	 aluno.setNome(teste[1]);
		   	            	 aluno.setRa(Integer.parseInt(teste[0]));
		   	            	 
		   	             presentes.add(aluno);
		   	             presenca.save(chamada.getId_evento(), aluno);
		   	             }
	   	             }catch(Exception e){
	   	            	 
	   	            	 try{
	   	            	 String[] teste1 = result.payload.toString().split(" ");
	   	            	 if(presenca.getPresente(teste1[0])){
			   	            	Toast.makeText(NFCActivity.this, "Tag já lida !", Toast.LENGTH_LONG).show();
			   	             }else{
			   	            	 aluno.setNome(teste1[1]);
			   	            	 aluno.setRa(Integer.parseInt(teste1[0]));
			   	             presentes.add(aluno);
			   	             presenca.save(chamada.getId_evento(), aluno);
			   	            
			   	             }
	   	            	 }catch(Exception ex){
	   	            	 
	   	            	 Toast.makeText(NFCActivity.this, "Tag fora do formato padrão !", Toast.LENGTH_LONG).show();
	   	            
	   	            	 }
	   	             }
	   	             
	   	             adapter.notifyDataSetChanged();
	                }
	            }
	          //  System.out.println(presenca.getPresente());
//	           NdefAdapter adpt = new NdefAdapter(dataList);
	//           lView.setAdapter(adpt);
	 //          adpt.notifyDataSetChanged();
	        }

	    }
	
	 // ListView adapter]
	 /*
	    class NdefAdapter extends ArrayAdapter<BaseRecord> {
	    	
	    	 List<BaseRecord> recordList;
	    	 
	    	 public NdefAdapter(List<BaseRecord> recordList) {
	             super(NFCActivity.this, R.layout.record_layout, recordList);
	             this.recordList = recordList;
	         }
	    	 
	    	 @Override
	         public int getCount() {
	             return recordList.size();
	         }

	         @Override
	         public BaseRecord getItem(int position) {
	             return recordList.get(position);
	         }
	         
	         @Override
	         public View getView(int position, View convertView, ViewGroup parent) {
	             View v = convertView;
	             Log.d("Nfc","Get VIew");
	             if (v == null) {
	                 LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                 v = inf.inflate(R.layout.record_layout, null);
	             }

	             TextView tnfTxt = (TextView) v.findViewById(R.id.tnfText);
	             TextView recContentTxT = (TextView) v.findViewById(R.id.recCont);
	             TextView typeTxt = (TextView) v.findViewById(R.id.typeTxt);
	             
	             BaseRecord record = recordList.get(position);
	             tnfTxt.setText("" + record.tnf);
	          
	             recContentTxT.setText(record.toString());
	             
	             try{
	             String[] teste = record.payload.toString().split(":");
	             
	             
	             presentes.add(teste[0] +":"+teste[1]);
	             
	             
	             presenca.setPresente(Integer.parseInt(teste[0]));
	             }catch(Exception e){
	            	 
	            	 try{
	            	 String[] teste1 = record.payload.toString().split(" ");
	            	 presentes.add(teste1[0] +": "+teste1[1]);
	            	 presenca.setPresente(Integer.parseInt(teste1[0]));
	            	 }catch(Exception ex){
	            	 
	            	 Toast.makeText(NFCActivity.this, "Tag fora do formato padrão !", Toast.LENGTH_LONG).show();
	            
	            	 }
	             }
	             
	             adapter.notifyDataSetChanged();
	             
	             return v;

	         }
	         
	         @Override
	         public long getItemId(int position) {
	             return recordList.get(position).hashCode();
	         }


	    }
  
	 */
}
	     

	     
	  

