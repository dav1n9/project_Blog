package com.example.sgdevcamp_blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView mRv_comment;
    private ImageButton mBtn_send;
    private ArrayList<CommentItem> mCommentItems;
    private DBHelper_cm mDBHelper_cm;
    private CustomAdapter_cm mAdapter_cm;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String postId_str = intent.getStringExtra("postId");   // 클릭한 post의 ID값 받아오기
        postId = Integer.parseInt(postId_str);
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        TextView tv_title = findViewById(R.id.tv_title_detail);
        TextView tv_content = findViewById(R.id.tv_content_detail);
        tv_title.setText(title);
        tv_content.setText(content);

        Toast.makeText(DetailActivity.this, postId_str + "입니다.", Toast.LENGTH_SHORT).show();

        setInit();
    }

    private void setInit() {
        mDBHelper_cm = new DBHelper_cm(this);
        mRv_comment = findViewById(R.id.rv_comment);
        mBtn_send = findViewById(R.id.btn_send);

        //load recent DB
        lostRecentDB();

        // 댓글 쓰면 저장하기..insert 부분
        EditText et_comment = findViewById(R.id.et_comment);
        mBtn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Insert DB
                mDBHelper_cm.InsertComment(postId, et_comment.getText().toString());
                //Insert UI
                CommentItem item = new CommentItem();
                item.setComment(et_comment.getText().toString());

                mAdapter_cm.addItem(item);
                mRv_comment.smoothScrollToPosition(0);

                Toast.makeText(DetailActivity.this, "댓글이 추가되었습니다.: "+postId, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void lostRecentDB() {
        // 저장되어 있언 DB를 가져온다.
        mCommentItems = mDBHelper_cm.getCommentList(postId);

        // null이면 새로 만들기
        if(mAdapter_cm == null) {
            mAdapter_cm = new CustomAdapter_cm(mCommentItems, this);
            // 리사이클러뷰 성능강화? why?
            mRv_comment.setHasFixedSize(true);
            mRv_comment.setAdapter(mAdapter_cm);
        }
    }

}
