package pt.ua.roomcontacts.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pt.ua.roomcontacts.data.Contact;
import pt.ua.roomcontacts.data.ContactsRepository;

/**
 * The ViewModel class is designed to store and manage UI-related data
 *  ViewModel objects are automatically retained during configuration changes
 *  so that data they hold is immediately available to the next activity or fragment instance
 *
 */
public class ContactsViewModel extends AndroidViewModel {

    // reference to the Repository whre to get data
    private final ContactsRepository mRepository;

    // the current data to display in the activity
    // since this is a live data, updated will be pushed automatically from db to UI
    private final LiveData<List<Contact>> mAllContacts;

    public ContactsViewModel(Application application) {
        super(application);

        // get and observe the list of contacts provided by the Repository
        mRepository = new ContactsRepository(application);
        mAllContacts = mRepository.getAllWords();
    }

    public LiveData<List<Contact>> getAllContacts() { return mAllContacts; }

    public void insert(Contact contact) {
        // send the new Contact into the database using the Repository abstraction
        mRepository.insert(contact);
    }

    /**
     * remove all entries
     */
    public void clearAll() {
        mRepository.deleteAll();
    }

    public boolean alreadyExists(Contact contact) {
        return mRepository.contains(contact);
    }
}
