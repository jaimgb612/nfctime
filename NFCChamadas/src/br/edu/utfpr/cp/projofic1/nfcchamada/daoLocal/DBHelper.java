package br.edu.utfpr.cp.projofic1.nfcchamada.daoLocal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	


	private static final String DATABASE_NAME = "nfcchamada.db";
	private static final int DATABASE_VERSION = 2;
	
	public static final String TABLE_PESSOA = "pessoa";
	public static final String TABLE_EVENTO = "evento";
	public static final String TABLE_CHAMADA = "chamada";
	public static final String TABLE_PRESENCA = "presenca";
	
	
	
	public static final String SQL_CREATE_TABLE_PESSOA = "CREATE TABLE " + TABLE_PESSOA + "("
			+ "name TEXT NOT NULL, "
			+ "r_matricula INTEGER PRIMARY KEY );";

public static final String SQL_CREATE_TABLE_EVENTO = "CREATE TABLE " + TABLE_EVENTO + "("
			+ "id_evento INTEGER PRIMARY KEY, "
			+ "name TEXT NOT NULL, "
			+ "data DATE, "
			+ "hora_inicio TIMESTAMP, "
			+ "hora_fim TIMESTAMP "
			+ " );";

public static final String SQL_CREATE_TABLE_CHAMADA = "CREATE TABLE " + TABLE_CHAMADA + "("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "id_evento INTEGER NOT NULL,"
			+ "descricao TEXT, "
			+ "qtd_aula TEXT,"
			+ "gravado INTEGER,"
			+ "total_integrante INTEGER);";
	
public static final String SQL_CREATE_TABLE_PRESENCA = "CREATE TABLE " + TABLE_PRESENCA + "("
		+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ "id_chamada_evento INTEGER NOT NULL, "
		+ "pessoa_id_pessoa INTEGER NOT NULL,"
		+ "nome TEXT);";
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_PESSOA);
		db.execSQL(SQL_CREATE_TABLE_EVENTO);
		db.execSQL(SQL_CREATE_TABLE_CHAMADA);
		db.execSQL(SQL_CREATE_TABLE_PRESENCA);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,  int oldVersion, int newVersion) {
		Log.w("DBHelper", "atualizando o bd da versao " + oldVersion + " para a versao " + newVersion);
		//realizar aqui os passos para migracao dos dados
		
		//neste caso, apaga as tabelas existentes
		db.execSQL("DROP IF EXISTS " + TABLE_PESSOA);
		db.execSQL("DROP IF EXISTS " + TABLE_EVENTO);
		db.execSQL("DROP IF EXISTS " + TABLE_CHAMADA);
		db.execSQL("DROP IF EXISTS " + TABLE_PRESENCA);
		
		//e manda criar novamente
		onCreate(db);
	}

}
