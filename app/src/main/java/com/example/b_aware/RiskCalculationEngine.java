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
        hashtable.put("CVV", new RiskyWords("CVV", 10));
        hashtable.put("CARD", new RiskyWords("CARD", 8));
        hashtable.put("OTP", new RiskyWords("OTP", 10));
        hashtable.put("MOBILE", new RiskyWords("MOBILE", 5));
        hashtable.put("NUMBER", new RiskyWords("NUMBER", 3));
    }
    public UUID startSession(){
        sessionId = UUID.randomUUID();
        return sessionId;
    }
    public double calculateRisk(UUID sessionId, String phrase){

        Log.v("RiskCalculationEngine", phrase);
        if(phrase == null || phrase.isEmpty())
        {
            return 0.0;
        }

        double result = 0.0;
        String effectivePhrase = "";
        if(lastPhrase == null || lastPhrase.isEmpty()){
            effectivePhrase = phrase.toUpperCase();
        }
        else
        {
            if(effectivePhrase.toUpperCase().contains(lastPhrase.toUpperCase()))
            {
                effectivePhrase = phrase.toUpperCase().substring(phrase.indexOf(lastPhrase.toUpperCase())+ lastPhrase.length()-1);
            }
            else
            {
                effectivePhrase = phrase.toUpperCase();
            }
        }
        String[] words = effectivePhrase.toUpperCase().split(" ");
        for (int i = 0; i < words.length; i++) {
            if(hashtable.get(words[i]) != null){
                result = result + hashtable.get(words[i]).Rank;
            }
        }
        lastPhrase = effectivePhrase.toUpperCase();
        return result;
    }
}