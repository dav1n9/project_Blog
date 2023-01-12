package com.example.sgdevcamp_blog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper_cm extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Post_db";

    public DBHelper_cm(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Comment_table(id INTEGER PRIMARY KEY AUTOINCREMENT, postId INTEGER NOT NULL, comment TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //select
    public ArrayList<CommentItem> getCommentList(int _id) {

        ArrayList<CommentItem> commentItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, comment FROM Comment_table WHERE postId = '"+_id+"' ORDER BY id DESC", null);

        if(cursor.getCount() != 0) {
            // != 0 이면 조회된 데이터가 있음을 의미. -> 내부 수정
            while(cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String comment = cursor.getString(cursor.getColumnIndex("comment"));

                CommentItem commentItem = new CommentItem();
                commentItem.setId(id);
                commentItem.setComment(comment);

                commentItems.add(commentItem);
            }
        }
        cursor.close();

        return commentItems;
    }

    //insert
    public void InsertComment(int _postId, String _comment) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Comment_table(postId, comment) VALUES ('"+_postId+"','"+_comment+"')");
    }

    //delete
    public void DeleteComment(int _id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Comment_table WHERE id = '"+_id+"'");
    }

    public void DeleteCommentAll(int _postId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Comment_table WHERE postId = '"+_postId+"'");
    }

    //update문 생략
}
