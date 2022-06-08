package marinic.patike.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import marinic.patike.dao.Baza;
import marinic.patike.dao.DAO;
import marinic.patike.model.Patika;


public class PatikeViewModel extends AndroidViewModel {

    DAO dao;

    private Patika patika;


    private LiveData<List<Patika>> patike;

    public PatikeViewModel(Application application) {
        super(application);
        dao = Baza.getBaza(application.getApplicationContext()).DAO();

    }

    public LiveData<List<Patika>> dohvatiPatike() {
        patike = dao.dohvatiPatike();
        return patike;
    }

    public void dodajNovePatike() {

        dao.dodajNovePatike(patika);
    }

    public void promjeniPatike() {

        dao.promjeniPatike(patika);
    }

    public void obrisiPatike() {

        dao.obrisiPatike(patika);
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }
}
