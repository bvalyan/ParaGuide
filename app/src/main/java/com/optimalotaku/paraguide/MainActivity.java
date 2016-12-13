package com.optimalotaku.paraguide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class MainActivity extends AppCompatActivity {

    String heroID;
    String picURL;
    String apiKey = "b6d974bd42ec4a2f9b2322034bd0d0e0";

    Drawable d = new Drawable() {
        @Override
        public void draw(Canvas canvas) {

        }

        @Override
        public void setAlpha(int i) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }


    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);


    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        private int mProgressStatus = 0;


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
                    default               : heroID = null;
                }

                apiKey = "b6d974bd42ec4a2f9b2322034bd0d0e0";
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
                response = "HERO NOT FOUND";
                responseView.setText(response);
            }

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            JSONArray arr = new JSONArray();
            try {
                JSONObject obj = new JSONObject(response);
                picURL = "https:" + obj.getJSONObject("images").getString("icon");
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
                    case 5 : mobilityStatement = "This hero is somewhat immobile. While you have ways to get around, you can still be killed due to lack of escape options. Make sure to prioritize having your escape options up, especially if the fight goes south.\n";
                        break;
                    case 6 : mobilityStatement = "This hero is somewhat immobile. While you have ways to get around, you can still be killed due to lack of escape options. Make sure to prioritize having your escape options up, especially if the fight goes south.\n";
                        break;
                    case 7 : mobilityStatement = "This hero is very mobile. You can roam the map with decent options for both engage and disengage. Utilize this mobility to maximize the potential of this hero.\n";
                        break;
                    case 8 : mobilityStatement = "This hero is very mobile. You can roam the map with decent options for both engage and disengage. Utilize this mobility to maximize the potential of this hero.\n";
                        break;
                    case 9 : mobilityStatement = "This hero is very mobile. You can roam the map with decent options for both engage and disengage. Utilize this mobility to maximize the potential of this hero.\n";
                        break;
                    case 10 : mobilityStatement = "This hero is very mobile. You can roam the map with decent options for both engage and disengage. Utilize this mobility to maximize the potential of this hero.\n";
                }

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
                }

                switch(durability){
                    case 1 : durabilityStatement = "This hero suffers from low durability. Be very careful to avoid unnecessary damage during fights and take advatage of other attributes.\n";
                        break;
                    case 2 : durabilityStatement = "This hero suffers from low durability. Be very careful to avoid unnecessary damage during fights and take advatage of other attributes.\n";
                        break;
                    case 3 : durabilityStatement = "This hero is not very durable. Prioritize movement or damage when confronted with conflict.\n";
                        break;
                    case 4 : durabilityStatement = "This hero has below average durability. You can take hits, but don't rely on your durability to get you out of any situation.\n";
                        break;
                    case 5 : durabilityStatement = "This hero has average durability. You can take hits, but you should focus just as much on basic attacking or firing your abilities.\n";
                        break;
                    case 6 : durabilityStatement = "This hero has slightly above average durability. You can take hits, but you should focus just as much on basic attacking or firing your abilities.\n";
                        break;
                    case 7 : durabilityStatement = "This hero is durable. You might do well to build HP/armor and take advantage of these natural defense stats and shouldn't hesitate to jump in team fights.\n";
                        break;
                    case 8 : durabilityStatement = "This hero is very durable. You should be the enagager in virtually every team fight. Build some HP or armor to fulfill the role of tanking for your team.\n";
                        break;
                    case 9 : durabilityStatement = "This hero is very durable. You should be the enagager in virtually every team fight. Build some HP or armor to fulfill the role of tanking for your team.\n";
                        break;
                    case 10 : durabilityStatement = "This hero is very durable. You should be the enagager in virtually every team fight. Build some HP or armor to fulfill the role of tanking for your team.\n";
                }

                switch(abilityAttack){
                    case 1 : abilityAttackStatement = "This hero has very low ability power and should absolutely focus on other attributes. It would probably be better not to build cards solely for the purpose of boosting ability power.\n";
                        break;
                    case 2 : abilityAttackStatement = "This hero has very low ability power and should absolutely focus on other attributes. It would probably be better not to build cards solely for the purpose of boosting ability power.\n";
                        break;
                    case 3 : abilityAttackStatement = "This hero has low ability power. It would be best not to focus on it over other attributes.\n";
                        break;
                    case 4 : abilityAttackStatement = "This hero has slightly below average ability power. Utilize it in conjuction with other beneficial attributes to be effective.\n";
                        break;
                    case 5 : abilityAttackStatement = "This hero has average ability power. Utilize it in conjuction with other beneficial attributes to be effective.\n";
                        break;
                    case 6 : abilityAttackStatement = "This hero has slightly above average ability power. Take full advatage of your abilities in battle, but don't expect too much from them.\n";
                        break;
                    case 7 : abilityAttackStatement = "This hero has above average ability power. Boost it with cards that increase power and make sure to build mana or mana regen to supplement your casting.\n";
                        break;
                    case 8 : abilityAttackStatement = "This hero has high ability power. Boost it with cards that increase power and make sure to build mana or mana regen to supplement your casting.\n";
                        break;
                    case 9 : abilityAttackStatement = "This hero has high ability power. Boost it with cards that increase power and make sure to build mana or mana regen to supplement your casting.\n";
                        break;
                    case 10 : abilityAttackStatement = "This hero has high ability power. Boost it with cards that increase power and make sure to build mana or mana regen to supplement your casting.\n";
                }

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

    class RetrieveImages extends AsyncTask<Void, Void, String>{

        private Exception exception;

        @Override
        protected String doInBackground(Void... urls) {
            URL url = null;
            try {
                url = new URL( "https://developer-paragon.epicgames.com/v1/hero/" + heroID);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                System.out.println("hereURL");
                e.printStackTrace();
            }
            urlConnection.addRequestProperty("X-Epic-ApiKey", apiKey);

            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                if(bufferedReader != null){
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                    }
                }
            } catch (IOException e) {
                System.out.println("here");
                e.printStackTrace();
            }
            try {
                if(bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                System.out.println("here2");
                e.printStackTrace();
            }

            String picJSONDATA = stringBuilder.toString();

            JSONObject obj = null;
            try {
                obj = new JSONObject(picJSONDATA);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if(obj != null)
                    picURL = "https:" + obj.getJSONObject("images").getString("icon");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                InputStream is = (InputStream) new URL(picURL).getContent();
                d = Drawable.createFromStream(is, "src name");

            } catch (Exception e) {
                System.out.println(Log.e("ERROR", e.getMessage(), e));
                return null;
            }
            return "found!";
        }

        protected void onPostExecute(String response) {
            ImageView picDisplay = (ImageView) findViewById(R.id.HeroImages);
            picDisplay.setVisibility(View.GONE);
            String questionURL = "http://images.clipartpanda.com/question-mark-icon-8235.png";

            //picDisplay.setImageDrawable(d);
            if(response == null){

                System.out.println("ITS NULL");
                //Glide.with(MainActivity.this).load(questionURL).into(picDisplay);
                picDisplay.setVisibility(View.GONE);
            }
            else{
                picDisplay.setVisibility(View.VISIBLE);
                Glide.with(MainActivity.this).load(picURL).into(picDisplay);
                picURL = null;

            }

        }
    }


    public void onClick(View view) throws InterruptedException {

        EditText hEdit = (EditText) findViewById(R.id.heroText);
        heroID = hEdit.getText().toString();
        RetrieveFeedTask feed = new RetrieveFeedTask();
        RetrieveImages images = new RetrieveImages();
        feed.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Thread.sleep(1000);
        images.execute();
        View view2 = this.getCurrentFocus();
        if (view2 != null) {
            //hide keyboard upon return
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }


    }

}

