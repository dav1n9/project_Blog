package com.example.sgdevcamp_blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRv_post;
    private FloatingActionButton mBtn_write;
    private ArrayList<PostItem> mPostItems;
    private DBHelper mDBHelper;
    private CustomAdapter mAdapter;
    public static Context mContext;
    public int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        setInit();
    }

    private void setInit() {
        mDBHelper = new DBHelper(this);
        mRv_post = findViewById(R.id.rv_post);
        mBtn_write = findViewById(R.id.btn_write);
        mPostItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        //load recent DB
        lostRecentDB();

        mBtn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] strChoiceItems = {"글쓰기", "관리자모드"};
                builder.setTitle("원하는 작업을 선택해 주세요.");
                builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        if (position == 0) {
                            // 팝업창
                            Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog);
                            dialog.setContentView(R.layout.dialog_edit);
                            EditText et_title = dialog.findViewById(R.id.et_title);
                            EditText et_content = dialog.findViewById(R.id.et_content);
                            Button btn_ok = dialog.findViewById(R.id.btn_ok);
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    //Insert DB
                                    String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());    // 현재 날짜, 시간 받아오기
                                    mDBHelper.InsertPost(et_title.getText().toString(), et_content.getText().toString(), currentTime);

                                    //Insert UI
                                    PostItem item = new PostItem();
                                    item.setTitle(et_title.getText().toString());
                                    item.setContent(et_content.getText().toString());
                                    item.setWriteDate(currentTime);

                                    mAdapter.addItem(item);

                                    // 0번째로 스크롤 이동
                                    mRv_post.smoothScrollToPosition(0);
                                    dialog.dismiss();  // 팝업창 끄기
                                    Toast.makeText(MainActivity.this, "할 일 목록에 추가 되었습니다.", Toast.LENGTH_SHORT).show();

                                }
                            });

                            dialog.show();

                        }else if(position == 1) {
                            // 관리자모드 변경 부분


                            //if(check == 0) {
                            //    mAdapter.changeMode(check);
                            //    check = 1;
                            //    Toast.makeText(MainActivity.this, "check == 1", Toast.LENGTH_SHORT).show();

                            //} else {
                            //    mAdapter.changeMode(check);
                            //    check = 0;
                            //    Toast.makeText(MainActivity.this, "check == 0", Toast.LENGTH_SHORT).show();
                            //}

                        }
                    }
                });
                builder.show();

            }
        });
    }

    private void lostRecentDB() {
        // 저장되어 있언 DB를 가져온다.
        mPostItems = mDBHelper.getPostList();

        // null이면 새로 만들기
        if(mAdapter == null) {
            mAdapter = new CustomAdapter(mPostItems, this);
            // 리사이클러뷰 성능강화? why?
            mRv_post.setHasFixedSize(true);
            mRv_post.setAdapter(mAdapter);
        }
    }
}