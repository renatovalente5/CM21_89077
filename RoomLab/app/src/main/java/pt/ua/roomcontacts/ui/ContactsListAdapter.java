package pt.ua.roomcontacts.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ua.roomcontacts.R;
import pt.ua.roomcontacts.data.Contact;


public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactsViewHolder> {

    private final LayoutInflater mInflater;
    private List<Contact> mContacts; // Cached copy of words

    ContactsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ContactsViewHolder( mInflater.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsListAdapter.ContactsViewHolder holder, int position) {

        if (mContacts != null) {
            Contact current = mContacts.get(position);
            holder.tvName.setText(current.getName());
            holder.tvEmail.setText(current.getEmail());
            holder.btMessage.setSelected(current.isOnline());

        } else {
            // Covers the case of data not being ready yet.
            holder.tvName.setText(R.string.no_data);
            holder.tvEmail.setText("");
            holder.btMessage.setSelected(false);
        }
    }

    void setContacts(List<Contact> contacts) {
        mContacts = contacts;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mContacts != null)
            return mContacts.size();
        else return 0;
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        final TextView tvName;
        final TextView tvEmail;
        final Button btMessage;

        // activity context
        private final Context context;


        private ContactsViewHolder(Context context, View itemView) {
            super(itemView);
            tvName = itemView.findViewById( R.id.tvName);
            tvEmail = itemView.findViewById( R.id.tvEmail);
            btMessage = itemView.findViewById( R.id.btSendEmail);

            // Store the context
            this.context = context;
            // Attach a click listener to the entire row view
            btMessage.setOnClickListener(this);
        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Contact contact = mContacts.get(position);

                sendEmail( context, contact);
            }
        }
    }

    private void sendEmail(Context context, Contact contact) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ contact.getEmail() });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hello!");

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

}

