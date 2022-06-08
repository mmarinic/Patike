package marinic.patike.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import marinic.patike.model.Patika;
import marinic.patike.R;
import marinic.patike.adapter.PatikeAdapter;
import marinic.patike.adapter.PatikeClickListener;
import marinic.patike.viewmodel.PatikeViewModel;


public class ReadFragment extends Fragment {

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.lista)
    ListView listView;

    private PatikeViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read,
                container, false);
        ButterKnife.bind(this,view);

        model = ((MainActivity)getActivity()).getModel();

        definirajListu();
        definirajSwipe();
        osvjeziPodatke();


        return view;
    }

    private void osvjeziPodatke(){
        model.dohvatiPatike().observe(getViewLifecycleOwner(), new Observer<List<Patika>>() {
            @Override
            public void onChanged(@Nullable List<Patika> patike) {
                 swipeRefreshLayout.setRefreshing(false);
                ((PatikeAdapter)listView.getAdapter()).setPodaci(patike);
                ((PatikeAdapter) listView.getAdapter()).osvjeziPodatke();

            }
        });
    }
    private void definirajSwipe() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                osvjeziPodatke();
            }
        });

    }

    private void definirajListu() {

        listView.setAdapter( new PatikeAdapter(getActivity(), R.layout.red_liste, new PatikeClickListener() {
            @Override
            public void onItemClick(Patika patika) {
                model.setPatika(patika);
                ((MainActivity)getActivity()).cud();
            }
        }));
    }

    @OnClick(R.id.fab)
    public void novePatike(){
        model.setPatika(new Patika());
        ((MainActivity)getActivity()).cud();
    }


}