package com.example.sgdevcamp_blog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Post_db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터베이스 생성될 때 호출
        // 데이터베이스 테이블... 각 컬럼들 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS Post_table(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    // SELECT 문 (목록 조회)
    public ArrayList<PostItem> getPostList() {
        ArrayList<PostItem> postItems = new ArrayList<>();
        // 데이터를 읽는 행위임.
        SQLiteDatabase db = getReadableDatabase();
        // 내림차순 정렬해서..
        Cursor cursor = db.rawQuery("SELECT * FROM Post_table ORDER BY writeDate DESC", null);

        if(cursor.getCount() != 0) {
            // != 0 이면 조회된 데이터가 있음을 의미. -> 내부 수정
            while(cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                PostItem postItem = new PostItem();
                postItem.setId(id);
                postItem.setTitle(title);
                postItem.setContent(content);
                postItem.setWriteDate(writeDate);

                postItems.add(postItem);
            }
        }
        cursor.close();

        return postItems;
    }

    // INSERT 문
    // id값은 AUTOINCREMENT 해주었기 때문에 자동증가 할 것이므로 생략.
    public void InsertPost(String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Post_table(title, content, writeDate) VALUES('" + _title + "', '"+ _content +"', '"+ _writeDate +"');");
    }

    // UPDATE 문
    public void UpdatePost(String _title, String _content, String _writeDate, String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE Post_table SET title='"+ _title +"', content='"+ _content +"', writeDate='"+ _writeDate + "' WHERE writeDate ='" + _beforeDate + "'");
    }

    // DELETE 문
    public void DeletePost(String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Post_table WHERE writeDate = '"+_beforeDate+"'");
    }
}
