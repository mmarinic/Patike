package marinic.patike.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import marinic.patike.model.Patika;


@Dao
public interface DAO {

    @Query("select * from patike order by id")
    LiveData<List<Patika>> dohvatiPatike();

    @Insert
    void dodajNovePatike(Patika patika);

    @Update
    void promjeniPatike(Patika patika);

    @Delete
    void obrisiPatike(Patika patika);


}
