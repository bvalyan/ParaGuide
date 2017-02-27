package com.optimalotaku.paraguide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Brandon on 1/18/17.
 */
public class SkillDisplay extends AppCompatActivity{
    private String skillname;
    private String skillPic;
    private String skillDesc;
    private SpannableString ssSkillDesc;
    TextView skillTextName;
    ImageView skillImage;
    TextView skillText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle gifts2 = getIntent().getExtras();
        skillname = gifts2.getString("skillname");
        skillPic  = gifts2.getString("skillpic");
        skillDesc = gifts2.getString("skillDesc");
        ParagonAPIAttrReplace replacer = new ParagonAPIAttrReplace();
        ssSkillDesc = replacer.replaceSymbolsWithImages(this,skillDesc);
        setContentView(R.layout.skillview);
        skillTextName = (TextView) findViewById(R.id.skillname);
        skillImage = (ImageView) findViewById(R.id.skillimage);
        skillText = (TextView) findViewById(R.id.skillText);
        skillText.setTextSize(17);
        Glide.with(this).load(skillPic).into(skillImage);
        skillTextName.setText(skillname);
        skillText.setText(ssSkillDesc);

    }
}
