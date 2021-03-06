package sac.lobosistemas.loboventas.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.ArrayList;
import sac.lobosistemas.loboventas.R;
import sac.lobosistemas.loboventas.model.Reportes;

public class ReportesAdapter extends RecyclerView.Adapter<ReportesAdapter.ViewHolder> implements View.OnClickListener{

    private ArrayList<Reportes> mDataSet;
    private View.OnClickListener listener;

    public ReportesAdapter(ArrayList<Reportes> listado_reportes) {
        mDataSet = listado_reportes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descripcion, cantidad, valor_unitario;
        public ViewHolder(View v) {
            super(v);

            descripcion = (TextView)itemView.findViewById(R.id.txtDescripcion);
            cantidad = (TextView)itemView.findViewById(R.id.txtCantidad);
            valor_unitario = (TextView)itemView.findViewById(R.id.txtValorUnitario);
        }
    }

    public ReportesAdapter() {
        mDataSet = new ArrayList<>();
    }

    public void setmDataSet(ArrayList<Reportes> dataSet){
        mDataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_reporte,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {

        Float valor = Float.parseFloat(mDataSet.get(i).getValor_unitario());

        holder.descripcion.setText(mDataSet.get(i).getDescripcion().trim()+"                                                        ");
        holder.cantidad.setText(""+mDataSet.get(i).getCantidad());
        holder.valor_unitario.setText(mDataSet.get(i).getMoneda()+" "+darFormato(String.valueOf(round(valor,2))));
    }

    public float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
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