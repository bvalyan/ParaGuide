package com.optimalotaku.paraguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.grid_home);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new MyAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                switch(position){
                    case 0 : intent = new Intent(MainActivity.this, PlayerView.class);
                                startActivity(intent);
                        break;
                    case 1 : intent = new Intent(MainActivity.this, DeckView.class);
                                startActivity(intent);
                        break;
                    case 2 :Toast.makeText(MainActivity.this, "Coming Soon!",
                            Toast.LENGTH_SHORT).show();
                        break;
                    case 3 : intent = new Intent(MainActivity.this, HeroView.class);
                              startActivity(intent);
                        break;
                    case 4 :Toast.makeText(MainActivity.this, "Coming Soon!",
                            Toast.LENGTH_SHORT).show();
                        break;
                    case 5 :Toast.makeText(MainActivity.this, "Coming Soon!",
                            Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    /*public void getCardData(){
        ParagonAPICardInfo cardInfo = new ParagonAPICardInfo();
        cardInfo.delegate = this;
        cardInfo.execute();
    }

    //@Override
    public void processCardInfoFinish(List<CardData> cDataList) {

        ParagonAPIAttrReplace attrTranslator = new ParagonAPIAttrReplace();

        /*
            Add up the Year month and day to get a number to get a number to mod with the
            number of cards to select the card of the day
         */
       /* SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Calendar today = Calendar.getInstance();
        String todayStr = formatter.format(today.getTime());
        Log.i("INFO","Today's Date: "+ todayStr);
        String[] todayParts = todayStr.split("-");
        Integer dateSum = Integer.parseInt(todayParts[0]) + Integer.parseInt(todayParts[1]) + Integer.parseInt(todayParts[2]);
        Log.i("INFO","Today's Date Sum: "+dateSum.toString());
        Integer chosenCard = dateSum % cDataList.size();
        Log.i("INFO","Today's Chosen Card Index: "+chosenCard.toString());

        //Grab the chosen card
        CardData cardOfTheDay = cDataList.get(chosenCard);

        ImageView cotdImage = (ImageView) findViewById(R.id.cardOfTheDay);
        TextView cotdText   = (TextView) findViewById(R.id.cardOfTheDayText);

        //Set Picture Image with Glide
        Glide.with(this).load("https:" + cardOfTheDay.getImageUrl()).into(cotdImage);
        cotdImage.setVisibility(View.VISIBLE);

        cotdText.setText("Name: "+cardOfTheDay.getName()+"\n\n");

        cotdText.append("Card Effects:\n\n");
        List<CardEffect> effectList = cardOfTheDay.getEffectList();
        for(CardEffect eff: effectList) {
            if(eff.getStat() != null && eff.getStatValue() != null) {
                Log.i("INFO","MainActivity - processCardInfoFinish - Stat: "+eff.getStat()+" Human Readable: "+ cardOfTheDay.statToHumanReadable(eff.getStat()));
                cotdText.append("" + cardOfTheDay.statToHumanReadable(eff.getStat()) +": "+eff.getStatValue()+"\n");
            }
            if(eff.getDescription() != null){
               cotdText.append("" + attrTranslator.replaceSymbolsWithText(eff.getDescription()) + "\n");
            }
            if(eff.getCooldown()!= null && Integer.parseInt(eff.getCooldown()) > 1){
                cotdText.append(" Cooldown: " + eff.getCooldown() + "s\n" );
            }
        }
        cotdText.append("\n");

        cotdText.append("Fully Upgraded Card Effects:\n");
        List<CardEffect> maxEffectList = cardOfTheDay.getMaxEffectList();
        for(CardEffect eff: maxEffectList) {
            if(eff.getStat() != null && eff.getStatValue() != null) {
                cotdText.append("• " + cardOfTheDay.statToHumanReadable(eff.getStat()) +": "+eff.getStatValue()+"\n");
            }
            if(eff.getDescription() != null){
                cotdText.append("• " + attrTranslator.replaceSymbolsWithText(eff.getDescription()) + "\n");
            }
            if(eff.getCooldown()!= null && Integer.parseInt(eff.getCooldown()) > 1){
                cotdText.append("• Cooldown: " + eff.getCooldown() + "s\n" );
            }
        }

    }*/
}

