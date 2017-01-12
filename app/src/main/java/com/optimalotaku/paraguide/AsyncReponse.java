package com.optimalotaku.paraguide;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Jerek on 12/15/2016.
 */

interface HeroInfoResponse {

    void processHeroInfoFinish(HeroData hdata);

}

interface DeckInfoResponse{

    void processDeckInfoFinish(List<DeckData> dDataList);

}

interface CardInfoResponse{
    void processCardInfoFinish(List<CardData> cDataList);
}

interface PlayerInfoResponse{
    void processPlayerInfoFinish(PlayerData pData);
}

interface ImageLoaderResponse{
    void processImageLoaderFinish(Bitmap imgBitmap);
}