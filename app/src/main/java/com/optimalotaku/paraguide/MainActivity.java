package com.optimalotaku.paraguide;

        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.lang.*;
        import org.json.*;


public class MainActivity extends AppCompatActivity {

    String heroID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);


    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;


        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);


        }

        protected String doInBackground(Void... urls) {


            try {
                EditText hEdit = (EditText)findViewById(R.id.heroText);
                switch (heroID.toLowerCase()){
                    case "rampage"        : heroID = "0fd6f97096f0356e479dc8ef23dcd819";
                        break;
                    case "countess"       : heroID = "051b478dd9b58f6f31c5e580996724df";
                        break;
                    case "murdock"        : heroID = "0df5b4104955a71326ad15a36fd6ed62";
                        break;
                    case "gadget"         : heroID = "20a755dd3f6ef6fda5ac9bc1dfba365f";
                        break;
                    case "narbash"        : heroID = "214573d44df234eb620002c891d9e826";
                        break;
                    case "khaimera"       : heroID = "2baa49db7d0f9fd4e9d5d9ef435626e7";
                        break;
                    case "steel"          : heroID = "350982a548a16ce00215b04dbe62a0b1";
                        break;
                    case "lt. belica"     : heroID = "3a76b90019a3df4bd43387791edd865a";
                        break;
                    case "iggy & scorch"  : heroID = "4f1658f6bb26fb1426aa2cbe103c42b3";
                        break;
                    case "riktor"         : heroID = "564380fe55ccc1014330d8123fa9810d";
                        break;
                    case "crunch"         : heroID = "605b482ced792e4366dfb0d1b901bc2c";
                        break;
                    case "gideon"         : heroID = "621951eb7e50d848c32e5fcf110d6776";
                        break;
                    case "kwang"          : heroID = "6aff7bbee67452fde268dadc938093db";
                        break;
                    case "sevarog"        : heroID = "7399ecfbab365cdb8543da3dfbe830e6";
                        break;
                    case "howitzer"       : heroID = "786d477b6bf412e5a1ab0a2539e8b78c";
                        break;
                    case "twinblast"      : heroID = "966e34d87f81ec512e0bb5be93b87bbf";
                        break;
                    case "grim.exe"       : heroID = "a9c3c59927ff7313d52751a54b331ad2";
                        break;
                    case "dekker"         : heroID = "ce9955a7ce823ffc0ecc71902f039201";
                        break;
                    case "Kallari"        : heroID = "cf19dc5ac814474f6d317b2c28dff999";
                        break;
                    case "the fey"        : heroID = "d1bc0d62ee2c08a7a493f666bd514932";
                        break;
                    case "greystone"      : heroID = "d62ed5512ced144d4c5fe8bf3505c6a6";
                        break;
                    case "feng mao"       : heroID = "dc8dd4e2ff11d6706866be8fb18e9208";
                        break;
                    case "muriel"         : heroID = "f8fc4d76627c1121251760c28729bbff";
                        break;
                    case "sparrow"        : heroID = "fb582fe82f0956269d7d656189d8b845";
                        break;
                    case "grux"           : heroID = "fc010be1a0bf295a347944e1e97ba583";
                        break;
                    default               : System.exit(2);
                }

                String apiKey = "b6d974bd42ec4a2f9b2322034bd0d0e0";
                URL url = new URL( "https://developer-paragon.epicgames.com/v1/hero/" + heroID);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.addRequestProperty("X-Epic-ApiKey", apiKey);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            TextView responseView = (TextView) findViewById(R.id.responseView);
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            JSONArray arr = new JSONArray();
            try {
                JSONObject obj = new JSONObject(response);
                String picURL = obj.getJSONObject("images").getString("icon");
                int mobility = Integer.parseInt(obj.getJSONObject("stats").getString("Mobility"));
                int basicAttack = Integer.parseInt(obj.getJSONObject("stats").getString("BasicAttack"));
                int durability = Integer.parseInt(obj.getJSONObject("stats").getString("Durability"));
                int abilityAttack = Integer.parseInt(obj.getJSONObject("stats").getString("AbilityAttack"));



                String mobilityStatement = new String();
                String basicAttackStatement = new String();
                String durabilityStatement = new String();
                String abilityAttackStatement = new String();

                switch(mobility){
                    case 0 : mobilityStatement = "This hero is dangerously immobile. Be prepared with a backup plan and/or cards with blink if the situation gets out of hand.\n";
                        break;
                    case 1 : mobilityStatement = "This hero is dangerously immobile. Be prepared with a backup plan and/or cards with blink if the situation gets out of hand.\n";
                        break;
                    case 2 : mobilityStatement = "This hero is dangerously immobile. Be prepared with a backup plan and/or cards with blink if the situation gets out of hand.\n";
                        break;
                    case 3 : mobilityStatement = "This hero is very immobile. While you may have an escape move, do your best to ensure your blind spots are clear at all times and be wary of enemy team-ups.\n";
                        break;
                    case 4 : mobilityStatement = "This hero is very immobile. While you may have an escape move, do your best to ensure your blind spots are clear at all times and be wary of enemy team-ups.\n";
                        break;
                    case 5 : mobilityStatement = "This hero is somewhat immobile. While you have ways to get around, you can still be killed do to lack of escape options. Make sure to prioritize having your escape options up, especially if the fight goes south.\n";
                        break;
                    case 6 : mobilityStatement = "This hero is somewhat immobile. While you have ways to get around, you can still be killed do to lack of escape options. Make sure to prioritize having your escape options up, especially if the fight goes south.\n";
                        break;
                    case 7 : mobilityStatement = "This hero is very mobile. You can roam the map with decent options for both engage and disengage. Utilize this mobility to maximize the potential of this hero.\n";
                        break;
                    case 8 : mobilityStatement = "This hero is very mobile. You can roam the map with decent options for both engage and disengage. Utilize this mobility to maximize the potential of this hero.\n";
                        break;
                    case 9 : mobilityStatement = "This hero is very mobile. You can roam the map with decent options for both engage and disengage. Utilize this mobility to maximize the potential of this hero.\n";
                        break;
                    case 10 : mobilityStatement = "This hero is very mobile. You can roam the map with decent options for both engage and disengage. Utilize this mobility to maximize the potential of this hero.\n";
                };

                switch(basicAttack){
                    case 1 : basicAttackStatement = "This hero does not specialize in basic attacks at all. Don't spend CP on anything enhancing basics. It's probably a better idea to focus on their abilities.\n";
                        break;
                    case 2 : basicAttackStatement = "This hero does not specialize in basic attacks at all. Don't spend CP on anything enhancing basics. It's probably a better idea to focus on their abilities.\n";
                        break;
                    case 3 : basicAttackStatement = "This hero has a very weak basic attack. While you may very well end up using it, it should never be the focus of your assault.\n";
                        break;
                    case 4 : basicAttackStatement = "This hero has a slightly below average basic attack. More than likely, you want to focus on another trait that is higher, especially in builds, but don't be afraid to use your basics.\n";
                        break;
                    case 5 : basicAttackStatement = "This hero has an average basic attack. More than likely, you want to focus on another trait that is higher, especially in builds, but don't be afraid to use your basics.\n";
                        break;
                    case 6 : basicAttackStatement = "This hero has a slightly above average basic attack. More than likely, they are a mixed attacker building basic damage in conjunction with another trait. Match the rating of this trait with the others to determine how you want to utilize this.\n";
                        break;
                    case 7 : basicAttackStatement = "This hero has an above average basic attack. More than likely, they are a mixed attacker building basic damage in conjunction with another trait. Match the rating of this trait with the others to determine how you want to utilize this.\n";
                        break;
                    case 8 : basicAttackStatement = "This hero has a strong basic attack. You should make a point to focus on it in your builds, as it will be key in your success.\n";
                        break;
                    case 9 : basicAttackStatement = "This hero has a strong basic attack. You should make a point to focus on it in your builds, as it will be key in your success.\n";
                        break;
                    case 10 : basicAttackStatement = "This hero has a strong basic attack. You should make a point to focus on it in your builds, as it will be key in your success.\n";
                };

                responseView.setText(mobilityStatement);
                responseView.append("\n");
                responseView.append(basicAttackStatement);
                responseView.append("\n");
                responseView.append(durabilityStatement);
                responseView.append("\n");
                responseView.append(abilityAttackStatement);



            } catch (JSONException e) {
                e.printStackTrace();
            }
            //responseView.setText(response);
        }
    }

    public void onClick(View view){
        EditText hEdit = (EditText) findViewById(R.id.heroText);
        heroID = hEdit.getText().toString();
        RetrieveFeedTask feed = new RetrieveFeedTask();
        feed.execute();
    }

}

