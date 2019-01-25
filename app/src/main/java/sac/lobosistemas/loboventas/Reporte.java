package sac.lobosistemas.loboventas;

import android.graphics.Color;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.ArrayList;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import retrofit2.Call;
import retrofit2.Callback;
import sac.lobosistemas.loboventas.io.LoboVentasApiAdapter;
import sac.lobosistemas.loboventas.io.LoboVentasApiService;
import sac.lobosistemas.loboventas.model.Reportes;
import sac.lobosistemas.loboventas.ui.adapter.ReportesAdapter;

public class Reporte extends AppCompatActivity {

    ArrayList<Reportes> reportes = new ArrayList<>(); //Para consultar los reportes

    private ReportesAdapter mAdapter; //Adaptador para los reportes
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView txtRazonSocial, txtRUC, txtSerie, txtFechaEmision, txtFechaVencimiento, txtTotal, txtPagado, txtRestante, lblPagado, lblRestante, lblTotal;
    FabSpeedDial fabSpeedDial;
    ProgressBar progressReporte;
    String factura_num, RUC;
    Float restante, total, pagado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtRazonSocial = findViewById(R.id.txtRazonSocial);
        txtRUC = findViewById(R.id.txtRUC);
        txtSerie = findViewById(R.id.txtSerie);
        txtFechaEmision = findViewById(R.id.txtFechaEmision);
        txtFechaVencimiento = findViewById(R.id.txtFechaVencimiento);
        txtTotal = findViewById(R.id.txtTotal);
        txtPagado = findViewById(R.id.txtPagado);
        txtRestante = findViewById(R.id.txtRestante);

        lblPagado = findViewById(R.id.lblPagado);
        lblRestante = findViewById(R.id.lblRestante);
        lblTotal = findViewById(R.id.lblTotal);

        progressReporte = findViewById(R.id.progressReporte);
        fabSpeedDial = findViewById(R.id.fabSpeedDial);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                if(id == R.id.enviarFactura){
                    new DialogoReporte(Reporte.this, ""+RUC, ""+factura_num,"factura");
                } else if(id == R.id.enviarEstado){
                    new DialogoReporte(Reporte.this, ""+RUC, ""+factura_num,"estado");
                }
                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });

        //-------------------------RecyclerView-------------------------//
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_reportes);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //---------------------------Adaptador--------------------------//
        mAdapter= new ReportesAdapter();
        mRecyclerView.setAdapter(mAdapter);

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            String razon_social = getIntent().getExtras().getString("empresa_razonsocial");
            factura_num = getIntent().getExtras().getString("factura_num");
            txtRazonSocial.setText(razon_social.trim());
        }

        Log.d("reportes",""+factura_num);

        //------------------------------------Conexión con la API------------------------------------//
        LoboVentasApiService ApiService = LoboVentasApiAdapter.getApiService();

        //------------------------------------RetroFit Reportes--------------------------------------//
        Call<ArrayList<Reportes>> call = ApiService.getReportes(""+factura_num);
        call.enqueue(new Callback<ArrayList<Reportes>>() {
            @Override
            public void onResponse(Call<ArrayList<Reportes>> call, retrofit2.Response<ArrayList<Reportes>> response) {
                if(response.isSuccessful()) {
                    reportes = response.body();
                    mAdapter.setmDataSet(reportes); //Agregamos el adaptador al Recycler View//

                    RUC = ""+reportes.get(0).getRuc();

                    txtRUC.setText("RUC: "+reportes.get(0).getRuc());
                    txtSerie.setText(reportes.get(0).getSerie()+" - "+factura_num);
                    txtFechaEmision.setText(reportes.get(0).getFemision());
                    txtFechaVencimiento.setText(reportes.get(0).getFvencimiento());
                    txtTotal.setText(reportes.get(0).getMoneda()+" "+darFormato(reportes.get(0).getTotal()));

                    if(reportes.get(0).getA_cuenta() == null){
                        ViewGroup.LayoutParams params = lblPagado.getLayoutParams();
                        params.height = 0;
                        lblPagado.setLayoutParams(params);
                        lblRestante.setLayoutParams(params);
                        txtPagado.setLayoutParams(params);
                        txtRestante.setLayoutParams(params);
                        lblTotal.setText("Total:");
                        txtTotal.setTextColor(Color.parseColor("#d02e26"));
                        lblTotal.setTextColor(Color.parseColor("#d02e26"));
                    } else {
                        total = Float.parseFloat(reportes.get(0).getTotal());
                        pagado = Float.parseFloat(reportes.get(0).getA_cuenta());

                        lblTotal.setText("Total:");
                        lblPagado.setText("Pagado:");
                        lblRestante.setText("Restante:");

                        restante = total - pagado;

                        txtPagado.setText(reportes.get(0).getMoneda()+" "+darFormato(reportes.get(0).getA_cuenta()));
                        txtRestante.setText(reportes.get(0).getMoneda()+" "+darFormato(String.valueOf(round (restante,2))));

                        lblRestante.setTextColor(Color.parseColor("#d02e26"));
                        txtRestante.setTextColor(Color.parseColor("#d02e26"));
                    }

                    progressReporte.setVisibility(View.INVISIBLE);
                    Log.d("onResponse reportes","Se cargaron "+reportes.size()+" reportes.");

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Reportes>> call, Throwable t) {
                Log.d("onResponse reportes","Algo salió mal: "+t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu_elegido){
        finish();
        return true;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public String darFormato(String num){
        int cont=-1;

        for(int i=0; i<num.length(); i++){
            if(num.charAt(i) == '.'){
                cont++;
            }else if(cont>=0){
                cont++;
            }
        }

        if(cont==-1){num+=".00";}
        else if(cont==1){num+="0";}

        return num;
    }
}