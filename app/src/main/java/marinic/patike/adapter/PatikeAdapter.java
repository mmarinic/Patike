package marinic.patike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.squareup.picasso.Picasso;
import java.util.List;
import marinic.patike.model.Patika;
import marinic.patike.R;


public class PatikeAdapter extends ArrayAdapter<Patika> {

    private List<Patika> podaci;
    private PatikeClickListener patikeClickListener;
    private int resource;
    private Context context;

    public PatikeAdapter(@NonNull Context context, int resource, PatikeClickListener patikeClickListener) {
        super(context, resource);

        this.resource = resource;
        this.context = context;
        this.patikeClickListener = patikeClickListener;
    }


    private static class ViewHolder {

        private TextView naziv;
        private ImageView slika;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        Patika patika;
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                view = inflater.inflate(this.resource, null);

                viewHolder.naziv = view.findViewById(R.id.naziv_tip);
                viewHolder.slika = view.findViewById(R.id.slika);
            } else {
                viewHolder = (ViewHolder) view.getTag();

            }

            patika = getItem(position);

            if (patika != null) {

                viewHolder.naziv.setText(patika.getNaziv() + " - " + context.getResources().getStringArray(R.array.tip_patike)[patika.getTip()]);

                if (patika.getSlika() == null) {
                    Picasso.get().load(R.drawable.no_img).fit().centerCrop().into(viewHolder.slika);
                } else {
                    Picasso.get().load(patika.getSlika()).fit().centerCrop().into(viewHolder.slika);
                }
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    patikeClickListener.onItemClick(patika);
                }
            });
        }
        return view;
    }

    @Override
    public int getCount() {
        return podaci == null ? 0 : podaci.size();
    }

    @Nullable
    @Override
    public Patika getItem(int position) {
        return podaci.get(position);
    }

    public void setPodaci(List<Patika> patike) {
        this.podaci = patike;
    }

    public void osvjeziPodatke() {
        notifyDataSetChanged();
    }

}
