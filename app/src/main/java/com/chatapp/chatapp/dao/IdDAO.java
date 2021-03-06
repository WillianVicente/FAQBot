package com.chatapp.chatapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chatapp.chatapp.dao.DBHelper;

public class IdDAO {

    private Context context;

    public IdDAO (Context context){
        this.context = context;
    }

    public void InserirId(String id){
        ContentValues cv = new ContentValues ();
        cv.put("id", id);
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert("login", null, cv);
        db.close();
    }


    public String RecuperarId(){
        String id = "";
        try {
            DBHelper helper = new DBHelper(context);
            SQLiteDatabase db = helper.getReadableDatabase();

            Cursor cursor = db.query("login", null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            }
            cursor.close();
            db.close();
            helper.close();
            return id;
        }catch(Exception e){
            return id;
        }
    }
}
