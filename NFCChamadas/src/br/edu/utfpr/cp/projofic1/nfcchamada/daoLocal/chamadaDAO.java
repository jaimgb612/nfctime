package br.edu.utfpr.cp.projofic1.nfcchamada.daoLocal;


import java.util.ArrayList;
import java.util.List;


import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Chamada;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Presenca;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class chamadaDAO {
	
	
	private DBHelper dbHelper;
	private SQLiteDatabase mDatabase;
	
	
	public chamadaDAO(Context context) {
		dbHelper = new DBHelper(context);
		try {
			open(); 
		}catch(Exception e) {
			Log.e("chamadaDAO", "Exception while connecting the DB.");
			e.printStackTrace();
		}
	}

	public void open() throws Exception {
		mDatabase = dbHelper.getWritableDatabase();
	}
	public void close() {
		dbHelper.close();
	}
	
	public void save(Chamada i) {
		ContentValues values = new ContentValues();
		
		values.put("id_evento", i.getId_evento());
		values.put("descricao", i.getDescricao().toString());
		values.put("qtd_aula", i.getQdtAula());
		values.put("gravado", i.getGravado());
		values.put("total_integrante", i.getQuantidade());
		
		System.out.println(values);
		System.out.println("Chamdas"+i);
		long idPresencaAluno = mDatabase.insert(DBHelper.TABLE_CHAMADA, null, values);
		i.setId_chamada(idPresencaAluno);
		
	}
	public void upgrade(Chamada i){
		ContentValues values = new ContentValues();
		
		values.put("gravado", i.getGravado());
		mDatabase.update(DBHelper.TABLE_CHAMADA, values, "id_evento = " + i.getId_evento(), null);
	
		
	}
	public Chamada getChamada(long id_evento){
		
		
		System.err.println("Evento id aqui :"+id_evento);
		Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + DBHelper.TABLE_CHAMADA + " WHERE id_evento= ?", 
				new String [] { String.valueOf(id_evento) });
				
		
				while(cursor.moveToNext()) {
					Chamada chamada = new Chamada();
					chamada.setId_evento(cursor.getLong(1));
					chamada.setDescricao(cursor.getString(2));
					chamada.setQdtAula(cursor.getString(3));
					chamada.setGravado(cursor.getInt(4));
					chamada.setQuantidade(cursor.getInt(5));
					System.out.println(chamada);
					return chamada;
				}
				cursor.close();		
				
				return null;
		
	}
	
	
	
}
