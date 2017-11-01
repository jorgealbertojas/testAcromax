package com.example.jorge.mytestacromax.interfaceJson;

import com.example.jorge.mytestacromax.model.Player;
import com.example.jorge.mytestacromax.utilite.ListWrapperPlayer;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.example.jorge.mytestacromax.utilite.Information.INFORMATION;

/**
 * Created by jorge on 01/11/2017.
 */

/** *
 * Interface for get JSON with Retrofit  */

public interface Interface {
    /** Get order Popular API Retrofit */
    @GET(INFORMATION)
    Call<ListWrapperPlayer<Player>> getPlayer();
}
