package sac.lobosistemas.loboventas.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import sac.lobosistemas.loboventas.ListaEmitidas;
import sac.lobosistemas.loboventas.ListaPagadas;
import sac.lobosistemas.loboventas.ListaVencidas;

public class ListasAdapter extends FragmentStatePagerAdapter {

    public ListasAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0){
            ListaVencidas listaVencidas = new ListaVencidas();
            return listaVencidas;
        }else if(i==1){
            ListaEmitidas listaEmitidas = new ListaEmitidas();
            return listaEmitidas;
        } else if(i==2){
            ListaPagadas listaPagadas = new ListaPagadas();
            return listaPagadas;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
