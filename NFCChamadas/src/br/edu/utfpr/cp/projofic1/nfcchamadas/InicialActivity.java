package br.edu.utfpr.cp.projofic1.nfcchamadas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class InicialActivity extends Activity implements Runnable {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicial);
		
		
        
        
        Handler handler = new Handler();
        handler.postDelayed(this, 3000);
       
	}

	@Override
	public void run() {
		
		   //Criando preference para verificar se já está logado 
        SharedPreferences preferences= getSharedPreferences("sessaoPessoa", Context.MODE_PRIVATE);
		// TODO Auto-generated method stub
        
        
      if(preferences.getBoolean("status", true)){
        	
        	Intent i = new Intent(InicialActivity.this, LoginActivity.class);
        	startActivity(i);
        	this.finish();
   
	}else{
        	
        	Intent i = new Intent(InicialActivity.this, LogadoActivity.class);
        	
        	startActivity(i);
        	this.finish();
        }
		
		
	}
}
