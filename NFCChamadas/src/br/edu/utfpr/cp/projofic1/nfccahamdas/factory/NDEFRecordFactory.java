package br.edu.utfpr.cp.projofic1.nfccahamdas.factory;

import java.util.Arrays;

import br.edu.utfpr.cp.projofic1.nfcchamadas.model.BaseRecord;
import br.edu.utfpr.cp.projofic1.nfcchamadas.model.NDEFExternalType;
import br.edu.utfpr.cp.projofic1.nfcchamadas.model.RDTSpRecord;
import br.edu.utfpr.cp.projofic1.nfcchamadas.model.RDTTextRecord;
import android.nfc.NdefRecord;
import android.util.Log;

public class NDEFRecordFactory {
	
	/* NDEF (NFC Data Exchange Format) é um formato binário leve, usado para encapsular os dados digitados. 
	 * Ele é especificado pelo Fórum NFC, para transmissão e armazenamento com NFC, no entanto, é o transporte agnóstico.
	 * NDEF define mensagens e registros. Um registro NDEF contém dados digitados, como MIME tipo de mídia,
	 * a URI, ou uma carga útil de aplicativo personalizado. Uma mensagem NDEF é um recipiente para um ou
	 *  mais registros NDEF.
	 *  */
	
	
	public static BaseRecord createRecord(NdefRecord record) {
        short tnf = record.getTnf();
        byte[] cont = record.getPayload();

        Log.d("Nfc", "Dump record ["+dumpPayload(record.getPayload())+"]");

        //indica o tipo de campo contém um conhecido nome do tipo RTD.
        if (tnf == NdefRecord.TNF_WELL_KNOWN) {
            Log.d("Nfc", "tipo de campo ");
            // Check Tipo texto
            if (Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)) {
                RDTTextRecord result = RDTTextRecord.createRecord(record.getPayload());
                return result;
            }
            else if (Arrays.equals(record.getType(), NdefRecord.RTD_URI)) {
                Log.d("Nfc", "RTD_URI");
                //Log.d("Nfc", "Content [" + new String(data.content)+ "]");
            }
            else if (Arrays.equals(record.getType(), NdefRecord.RTD_SMART_POSTER)) {
                Log.d("Nfc", "Poster smart");


                //Log.d("Nfc", "Smart poster ["+new String(data.content)+"]");
                RDTSpRecord result = RDTSpRecord.createRecord(record.getPayload());
                return result;
            }
            // Maybe handle more
        }
        else if (tnf == NdefRecord.TNF_EXTERNAL_TYPE) {

            NDEFExternalType extType = NDEFExternalType.createRecord(record.getPayload());
        }

        return null;
    }



    private static String dumpPayload(byte[] payload) {
        StringBuffer pCont = new StringBuffer();
        for (int rn=0; rn < payload.length;rn++) {
            pCont.append(" " + ( Integer.toHexString(payload[rn])));
        }

        return pCont.toString();
    }

    private static String dumpPayload2String(byte[] payload) {
        StringBuffer pCont = new StringBuffer();
        for (int rn=0; rn < payload.length;rn++) {
            pCont.append(( char) payload[rn]);
        }

        return pCont.toString();
    }

}
