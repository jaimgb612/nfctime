package br.edu.utfpr.cp.projofic1.nfcchamada.daoLocal;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Chamada;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Evento;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Pessoa;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Presenca;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class presencaDAO {
	
	private DBHelper dbHelper;
	private SQLiteDatabase mDatabase;
	
	public presencaDAO(Context context) {
		dbHelper = new DBHelper(context);
		try {
			open(); 
		}catch(Exception e) {
			Log.e("presencaDAO", "Exception while connecting the DB.");
			e.printStackTrace();
		}
	}
	
	public void open() throws Exception {
		mDatabase = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void save(long id_evento, Presenca aluno) {
		ContentValues values = new ContentValues();
		
		
		
			values.put("id_chamada_evento",id_evento);
			values.put("pessoa_id_pessoa", aluno.getRa());
			values.put("nome", aluno.getNome());
			
			long idPresencaAluno = mDatabase.insert(DBHelper.TABLE_PRESENCA, null, values);
			aluno.setId(idPresencaAluno);
		
		
	}
	public void delete(Presenca p){
		mDatabase.delete(DBHelper.TABLE_PRESENCA, "_id= " + p.getId(), null);
	}
	
	public boolean getPresente(String ra){
		Cursor cursor = mDatabase.rawQuery("SELECT COUNT(*) FROM " + DBHelper.TABLE_PRESENCA + " WHERE pessoa_id_pessoa= ?", 
				new String [] { String.valueOf(ra) });
		
		if(cursor.getCount() >0){
						
			//return true;
		}
		
		return false;
		
	}
	public void destruirTabela(){
		
		mDatabase.delete(DBHelper.TABLE_PRESENCA, null, null);
		mDatabase.close();
	}
	
	public List<Presenca> getByPresentesEvento(Chamada c){
			
		
		ArrayList<Presenca> ret = new ArrayList<Presenca>();
		
		Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + DBHelper.TABLE_PRESENCA + " WHERE id_chamada_evento= ?", 
																	new String [] { String.valueOf(c.getId_evento()) });
	
		while(cursor.moveToNext()) {
			Presenca i = cursorToAluno(cursor, c);
			ret.add(i);
			
		}
		cursor.close();		
		
		return ret;
		
	}
	private Presenca cursorToAluno(Cursor cursor, Chamada c){
		Presenca p  = new Presenca();
		
		p.setId(cursor.getLong(0));
		p.setNome(cursor.getString(3));
		p.setRa(cursor.getLong(2));
		
		return p;
		
	}
	

}
