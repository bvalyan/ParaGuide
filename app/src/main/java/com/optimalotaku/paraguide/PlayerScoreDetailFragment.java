package com.optimalotaku.paraguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by bvalyan on 8/7/17.
 */

public class PlayerScoreDetailFragment extends Fragment {
    PlayerData pData;

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_back, container, false);
        return view;
    }*/

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            pData = (PlayerData) args.getSerializable("pData");
        }
    }

}
