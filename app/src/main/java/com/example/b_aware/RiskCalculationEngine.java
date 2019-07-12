package com.example.b_aware;
import android.content.Context;
import android.util.Log;

import java.util.Hashtable;
import java.util.UUID;

public class RiskCalculationEngine {

    final private String TAG = "RiskCalculationEngine";
    private UUID sessionId;
    private Hashtable<String, RiskyWords> hashtable;
    private String lastPhrase;

    public RiskCalculationEngine(Context context){
        hashtable = new Hashtable<>();
//        AssetManager am = context.getAssets();
//        try {
//            String[] files = am.list("Files");
//            Log.e(TAG, files[0]);
//        }
//        catch (IOException ex){
//            Log.e(TAG, ex.getMessage());
//        }
        hashtable.put("CVV", new RiskyWords("CVV", 10));
        hashtable.put("CVV", new RiskyWords("Card", 8));
        hashtable.put("CVV", new RiskyWords("OTP", 10));
        hashtable.put("CVV", new RiskyWords("Mobile", 5));
        hashtable.put("CVV", new RiskyWords("Number", 3));
    }
    public UUID startSession(){
        sessionId = UUID.randomUUID();
        return sessionId;
    }
    public double calculateRisk(UUID sessionId, String phrase){


        Log.v("RiskCalculationEngine", phrase);

        double result = 0.0;
        String effectivePhrase = "";
        if(lastPhrase == null || lastPhrase.isEmpty()){
            effectivePhrase = phrase;
        }
        String[] words = effectivePhrase.split(" ");
        for (int i = 0; i < words.length; i++) {
            if(hashtable.get(words[i]) != null){
                result = result + hashtable.get(phrase).Rank;
            }
        }
        return 8.2;
    }
}