package com.neusoft.android.notepad;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class NotePad extends ListActivity {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    public static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private MyDataBaseAdapter dbHelper;
    private SQLiteDatabase db = null;
    private Cursor c;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dbHelper = new MyDataBaseAdapter(this);
        db = (SQLiteDatabase) dbHelper.open();
        fillData();
        registerForContextMenu(getListView());

    }

    private void addNote()
    {
        Intent i = new Intent(this, EditActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    private void fillData(){
        c = dbHelper.fetchAllData(db);
        startManagingCursor(c);
        c.getCount();
        if(c != null && c.getCount() >= 0)
        {
            ListAdapter adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1, c,
                    new String[] {MyDataBaseAdapter.KEY_TITLE},
                    new int[] {android.R.id.text1}
            );
            setListAdapter(adapter);

        }

    }

}
