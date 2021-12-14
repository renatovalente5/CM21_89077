package pt.ua.roomcontacts.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {

    // you don't need to explicit the SQL to use the defaults for
    // @Insert, @Update, @Delete

    @Insert ( onConflict = OnConflictStrategy.ABORT)
    void insert(Contact contact);

    @Query("DELETE FROM contacts")
    void deleteAll();

    // wrapping the results with live data makes the results changes observable
    @Query("SELECT * from contacts ORDER BY name ASC")
    LiveData<List<Contact>> getAlphabetizedContacts();


}