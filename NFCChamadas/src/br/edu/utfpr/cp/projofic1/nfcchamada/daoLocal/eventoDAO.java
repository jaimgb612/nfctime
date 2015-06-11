package br.edu.utfpr.cp.projofic1.nfcchamada.daoLocal;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Evento;
import br.edu.utfpr.cp.projofic1.nfcchamadas.database.Pessoa;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



public class eventoDAO {
	
	private DBHelper dbHelper;
	private SQLiteDatabase mDatabase;
	
	
	public eventoDAO(Context context) {
		dbHelper = new DBHelper(context);
		try {
			open(); 
		}catch(Exception e) {
			Log.e("eventoDAO", "Exception while connecting the DB.");
			e.printStackTrace();
		}
	}
	public void open() throws Exception {
		mDatabase = dbHelper.getWritableDatabase();
	}
	public void close() {
		dbHelper.close();
	}

	public void save(Evento i) {
		ContentValues values = new ContentValues();
		
		values.put("name", i.getNome());
		values.put("data", i.getData().toString());
		values.put("hora_inicio", i.getHoraInicio().toString());
		values.put("hora_fim", i.getHoraFim().toString());
		values.put("gravado", i.getGravado() ? 1 : 0);
		
		mDatabase.insert(DBHelper.TABLE_EVENTO, null, values);
	}
	
	public List<Evento> getAll(Pessoa c) {
		ArrayList<Evento> ret = new ArrayList<Evento>();
		
		Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + DBHelper.TABLE_EVENTO + " WHERE id_criador_evento= ?", 
																	new String [] { String.valueOf(c.getId()) });
		cursor.moveToFirst();
		while(!cursor.moveToNext()) {
			Evento i = cursorToIssue(cursor, c);
			ret.add(i);
		}
		cursor.close();		
		
		return ret;
	}
	
	private Evento cursorToIssue(Cursor cursor, Pessoa c) {
		Evento i = new Evento();
		
		i.setId( cursor.getInt(0) );
		i.setNome(cursor.getString(1));
	//	i.setData( (Date) cursor.getString(2) );
		//i.setHoraInicio(cursor.getString(3) );
		//i.setHoraFim( cursor.getString(4) );
		i.setGravado(cursor.getLong(5) == 1 ? true : false );
		return i;
	}
}
