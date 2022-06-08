package marinic.patike.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import marinic.patike.viewmodel.PatikeViewModel;
import marinic.patike.R;

public class CUDFragment extends Fragment {

    static final int SLIKANJE =1;

    private String putanjaSlike;

    @BindView(R.id.naziv)
    EditText naziv;
    @BindView(R.id.tip_patike)
    Spinner tipPatike;
    @BindView(R.id.opis)
    EditText opis;
    @BindView(R.id.slika)
    ImageView slika;
    @BindView(R.id.novePatike)
    Button novePatike;
    @BindView(R.id.uslikaj)
    Button uslikaj;
    @BindView(R.id.promjenaPatike)
    Button promjenaPatike;
    @BindView(R.id.obrisiPatike)
    Button obrisiPatike;

    PatikeViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cud,
                container, false);
        ButterKnife.bind(this, view);

        model = ((MainActivity) getActivity()).getModel();

        if (model.getPatika().getId() == 0) {
            definirajNovePatike();
            return view;
        }
        definirajPromjenaBrisanjePatika();

        return view;
    }

    private void definirajPromjenaBrisanjePatika() {
        novePatike.setVisibility(View.GONE);
        tipPatike.setSelection(model.getPatika().getTip());
        naziv.setText(model.getPatika().getNaziv());
        opis.setText(model.getPatika().getOpis());

        Picasso.get().load(model.getPatika().getSlika()).error(R.drawable.no_img).into(slika);

        uslikaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uslikaj();
            }
        });

        promjenaPatike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promjenaPatika();
            }
        });

        obrisiPatike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obrisiPatike();
            }
        });


    }

    private void definirajNovePatike() {
        promjenaPatike.setVisibility(View.GONE);
        obrisiPatike.setVisibility(View.GONE);
        uslikaj.setVisibility(View.GONE);
        novePatike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novePatike();
            }
        });
    }

    private void novePatike() {
        model.getPatika().setNaziv(naziv.getText().toString());
        model.getPatika().setTip(tipPatike.getSelectedItemPosition());
        model.getPatika().setOpis(opis.getText().toString());
        model.dodajNovePatike();
        nazad();
    }
    private void promjenaPatika() {
        model.getPatika().setNaziv(naziv.getText().toString());
        model.getPatika().setTip(tipPatike.getSelectedItemPosition());
        model.getPatika().setOpis(opis.getText().toString());
        model.promjeniPatike();
        nazad();
    }

    private void obrisiPatike() {
        model.getPatika().setNaziv(naziv.getText().toString());
        model.getPatika().setTip(tipPatike.getSelectedItemPosition());
        model.getPatika().setOpis(opis.getText().toString());
        model.obrisiPatike();
        nazad();
    }

    @OnClick(R.id.nazad)
    public void nazad() {
        ((MainActivity) getActivity()).read();
    }

    private void uslikaj() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) == null) {
            Toast.makeText(getActivity(), "Problem kod kreiranja slike", Toast.LENGTH_LONG).show();
            return;

        }

            File slika = null;
            try {
                slika = kreirajDatotekuSlike();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "Problem kod kreiranja slike", Toast.LENGTH_LONG).show();
            return;
            }

            if (slika == null) {
                Toast.makeText(getActivity(), "Problem kod kreiranja slike", Toast.LENGTH_LONG).show();
                return;
            }

            Uri slikaURI = FileProvider.getUriForFile(getActivity(),"marinic.patike.provider", slika);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, slikaURI);
            startActivityForResult(takePictureIntent, SLIKANJE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SLIKANJE && resultCode == Activity.RESULT_OK) {

            model.getPatika().setSlika("file://" + putanjaSlike);
            model.promjeniPatike();
            Picasso.get().load(model.getPatika().getSlika()).into(slika);

        }
    }

    private File kreirajDatotekuSlike() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imeSlike = "patike_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imeSlike,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        putanjaSlike = image.getAbsolutePath();
        return image;
    }

}