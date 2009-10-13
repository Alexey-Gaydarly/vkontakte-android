package org.googlecode.vkontakte_android;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import org.googlecode.vkontakte_android.database.MessageDao;
import org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper;
import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.KEY_MESSAGE_DATE;
import org.googlecode.vkontakte_android.provider.UserapiProvider;


public class MessagesListTabActivity extends ListActivity implements AbsListView.OnScrollListener {
    private MessagesListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list);
//        Cursor incomingMessagesCursor = managedQuery(UserapiProvider.MESSAGES_URI, null, KEY_MESSAGE_RECEIVERID + "=" + CSettings.myId, null, KEY_MESSAGE_DATE + " DESC");
        Cursor allMessagesCursor = managedQuery(UserapiProvider.MESSAGES_URI, null, null, null, KEY_MESSAGE_DATE + " DESC");
        adapter = new MessagesListAdapter(this, R.layout.message_row, allMessagesCursor);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageDao messageDao = new MessageDao(adapter.getCursor());
                Intent intent = new Intent(getApplicationContext(), ComposeMessageActivity.class);
                intent.putExtra(UserapiDatabaseHelper.KEY_MESSAGE_SENDERID, messageDao.getSenderId());
                startActivity(intent);
            }
        });
        getListView().setOnScrollListener(this);
    }


    public void onScroll(AbsListView v, int i, int j, int k) {
    }

    public void onScrollStateChanged(AbsListView v, int state) {
        if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && getListView().getLastVisiblePosition() == adapter.getCount() - 1) {
            //todo: download more messages?
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long rowId = info.id;
        switch (item.getItemId()) {
            case R.id.message_view:
                return true;

            case R.id.message_reply:
                return true;

            case R.id.message_mark_as_spam:
                return true;

            case R.id.message_delete:
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.message_context_menu, menu);
    }
}