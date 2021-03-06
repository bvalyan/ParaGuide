package com.optimalotaku.paraguide;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by bvaly on 1/29/2017.
 */
public class DetailDeckView extends AppCompatActivity {/*
    private DeckData deckInfo;
    private ListView cardlistView;
    private GridView gridview;
    FileManager cardmanager;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent decks = this.getIntent();
        cardmanager = new FileManager(this);
        deckInfo = (DeckData) decks.getSerializableExtra("deckObject");

        try {
            Map<String,List<CardData>> cDataMap = new HashMap<>();
            JSONArray cardCollection = new JSONArray(deckInfo.getDeckContents().toString());
            setContentView(R.layout.cardlist);
            gridview = (GridView) findViewById(R.id.gridview2);
            String [] pics = new String[cardCollection.length()];
            final String [] cardText = new String[cardCollection.length()];
            cDataMap = cardmanager.readCardsFromStorage();

            for (int i = 0; i < pics.length; i++){
                cardText[i] = cardCollection.getJSONObject(i).getString("name");
            }
            Arrays.sort(cardText);
            int k = 0;
            for (k = 0; k< cardText.length; k++){
                for (int j = 0; j < cardText.length; j++){
                    String compareCard = cardText[k];
                    if(cardCollection.getJSONObject(j).getString("name").equals(compareCard))
                        pics[k] = cardCollection.getJSONObject(j).getJSONObject("images").getString("medium_stats");
                }

            }

            MyDeckAdapter deckAdapter = new MyDeckAdapter(this, pics, cardText);
            gridview.setAdapter(deckAdapter);
            final Intent intent;
            intent = new Intent(this,CardDisplay.class);
            final Map<String, List<CardData>> finalCDataMap = cDataMap;

            final Button button = (Button) findViewById(R.id.deletebutton);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    deckDeletion(deckInfo.getDeckID());
                }
            });

            final Button button2 = (Button) findViewById(R.id.editbutton);
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();
                }
            });

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    gridview.playSoundEffect(SoundEffectConstants.CLICK); //send feedback on main drawer
                    gridview.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    Toast.makeText(getApplicationContext(), "You Clicked " +cardText[+ position], Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < finalCDataMap.get("All").size(); i++){
                        if(finalCDataMap.get("All").get(i).getName().equals(cardText[position])){
                            intent.putExtra("selectedCard", finalCDataMap.get("All").get(i));
                        }
                    }
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    protected void deckDeletion(String deckID){
        confirmDialog();

    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder
                .setMessage("This will delete \"" +deckInfo.getDeckName() + "\"... Are you sure?")
                .setPositiveButton("Yeah!",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        DeckDelete deleteInstance = new DeckDelete(DetailDeckView.this);
                        deleteInstance.execute(deckInfo.getDeckID(), deckInfo.getDeckName());
                    }
                })
                .setNegativeButton("Nope.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }
*/
}
