package com.neusoft.android.notepad;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class NotePad extends ListActivity {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    public static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private MyDataBaseAdapter dbHelper;
    private SQLiteDatabase db = null;
    private Cursor c;
    ///




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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, INSERT_ID, 0, "新建");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "option 菜单项被选中", Toast.LENGTH_SHORT).show();
        switch(item.getItemId())
        {
            case INSERT_ID:
                addNote();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub

        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,DELETE_ID,0,"删除");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "上下文菜单项被选中", Toast.LENGTH_SHORT).show();
        switch(item.getItemId())
        {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                dbHelper.delete(db, info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        Toast.makeText(getApplicationContext(), "发送时id=" + id, Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, EditActivity.class);
        i.putExtra(MyDataBaseAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        fillData();
    }


}
