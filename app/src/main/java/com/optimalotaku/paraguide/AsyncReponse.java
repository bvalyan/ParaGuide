package com.optimalotaku.paraguide;

import android.graphics.Bitmap;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Jerek on 12/15/2016.
 */

interface HeroInfoResponse {

    void processHeroInfoFinish(Map<String,HeroData> hdata);

}

interface DeckInfoResponse{

    void processDeckInfoFinish(List<DeckData> dDataList) throws IOException;

}

interface CardInfoResponse{
    void processCardInfoFinish(Map<String,List<CardData>> cDataMap);
}

interface PlayerInfoResponse{
    void processPlayerInfoFinish(PlayerData pData);
}

interface ImageLoaderResponse{
    void processImageLoaderFinish(Bitmap imgBitmap);
}