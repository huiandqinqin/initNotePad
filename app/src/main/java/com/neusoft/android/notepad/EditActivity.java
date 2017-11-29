package com.neusoft.android.notepad;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends Activity{

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;

    private MyDataBaseAdapter dbHelper;
    private SQLiteDatabase db;
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        dbHelper = new MyDataBaseAdapter(this);
        db = dbHelper.open();

        setContentView(R.layout.edit);

        mRowId = (savedInstanceState != null) ? (Long)savedInstanceState.getSerializable(MyDataBaseAdapter.KEY_ROWID):null;
        Toast.makeText(getApplicationContext(), "从savedInstanceState获取到id=" + mRowId, Toast.LENGTH_SHORT).show();

        if(mRowId == null)
        {
            Bundle extras = getIntent().getExtras();
            mRowId = (extras != null) ? extras.getLong(MyDataBaseAdapter.KEY_ROWID) : null;
            Toast.makeText(getApplicationContext(), "从extras中获取的id=" + mRowId, Toast.LENGTH_SHORT).show();


        }
        mTitleText = (EditText)findViewById(R.id.title);
        mBodyText = (EditText)findViewById(R.id.body);
        Button confirmButton = (Button)findViewById(R.id.confirm);


        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void populateFields()
    {
        if(mRowId != null)
        {
            Toast.makeText(getApplicationContext(), "从populateFields中获取的id="+ mRowId, Toast.LENGTH_SHORT).show();
            Cursor c = dbHelper.fetchData(db, mRowId);
            startManagingCursor(c);
            mTitleText.setText(c.getString(c.getColumnIndexOrThrow(MyDataBaseAdapter.KEY_TITLE)));
            mBodyText.setText(c.getString(c.getColumnIndexOrThrow(MyDataBaseAdapter.KEY_BODY)));
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        saveState();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        populateFields();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(MyDataBaseAdapter.KEY_ROWID, mRowId);
    }

    private void saveState()
    {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        if(title.equals(""))
        {
            Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mRowId == null)
        {
            long id = dbHelper.insertData(db, title, body);
            if(id > 0)
            {
                mRowId = id;
            }
        }
        else
        {
            dbHelper.updateData(db, mRowId, title, body);
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

}
