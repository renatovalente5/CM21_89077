package pt.ua.roomcontacts.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Objects;

public class ContactsRepository {

    private final ContactDao mContactDao;
    private final LiveData<List<Contact>> mAllContacts;

    public ContactsRepository(Application application) {
        // get the database instance
        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);

        // get the contract to interact with the db
        mContactDao = db.wordDao();

        // get the current content of contacts  table
        mAllContacts = mContactDao.getAlphabetizedContacts();
    }

    // Room executes queries on a separate thread; don't need to specify the Executor support
    //for querying
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Contact>> getAllWords() {
        return mAllContacts;
    }


    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> {
            mContactDao.insert(contact);
        });
    }


    public void deleteAll() {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> {
            mContactDao.deleteAll();
        });
    }


    public boolean contains(@NonNull Contact contact) {

        // using the data that is already in live data, instead of quering the database
        return Objects.requireNonNull(mAllContacts.getValue()).contains(contact);

    }

}
