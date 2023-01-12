package com.example.sgdevcamp_blog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<PostItem> mPostItems;
    private Context mContext;
    private DBHelper mDBHelper;
    private int getCheck;

    public ImageButton btn_delete;
    public ImageButton btn_update;


    public CustomAdapter(ArrayList<PostItem> mPostItems, Context mContext) {
        this.mPostItems = mPostItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {

        holder.tv_title.setText(mPostItems.get(position).getTitle());
        holder.tv_content.setText(mPostItems.get(position).getContent());
        holder.tv_writeDate.setText(mPostItems.get(position).getWriteDate());

        // MainActivity에서 선언한 변수 불러오기
        getCheck = ((MainActivity)MainActivity.mContext).check;

    }

    @Override
    public int getItemCount() {
        return mPostItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_writeDate;

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_writeDate = itemView.findViewById(R.id.tv_date);


            // 삭제
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int curPos = getAdapterPosition();
                    PostItem postItem = mPostItems.get(curPos);

                    String[] strChoiceItems = {"삭제", "취소"};
                    builder.setTitle("삭제하시겠습니까?.");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if (position == 0) {
                                // delete table
                                String beforeTime = postItem.getWriteDate();
                                mDBHelper.DeletePost(beforeTime);

                                // 해당 포스터의 댓글들도 같이 삭제
                                DBHelper_cm mDBHelper_cm = new DBHelper_cm(mContext);
                                mDBHelper_cm.DeleteCommentAll(postItem.getId());

                                // delete UI
                                mPostItems.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext, "목록이 제거되었습니다.", Toast.LENGTH_SHORT).show();

                            } else if (position == 1) {
                                //
                            }
                        }
                    });
                    builder.show();
                }
            });




            // 수정하기
            btn_update = itemView.findViewById(R.id.btn_update);
            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int curPos = getAdapterPosition();
                    PostItem postItem = mPostItems.get(curPos);

                    Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                    dialog.setContentView(R.layout.dialog_edit);
                    EditText et_title = dialog.findViewById(R.id.et_title);
                    EditText et_content = dialog.findViewById(R.id.et_content);
                    Button btn_ok = dialog.findViewById(R.id.btn_ok);

                    et_title.setText(postItem.getTitle());          // 기존의 글 가져와서 set
                    et_content.setText(postItem.getContent());
                    
                    et_title.setSelection(et_title.getText().length());   // 마우스 커서를 제목 끝으로 설정

                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String title = et_title.getText().toString();
                            String content = et_content.getText().toString();
                            //update table
                            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());    // 현재 날짜, 시간 받아오기
                            String beforeTime = postItem.getWriteDate();

                            mDBHelper.UpdatePost(title, content, currentTime, beforeTime);


                            //update UI
                            postItem.setTitle(title);
                            postItem.setContent(content);
                            postItem.setWriteDate(currentTime);
                            notifyItemChanged(curPos, postItem);
                            dialog.dismiss();
                            Toast.makeText(mContext, "목록 수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();

                        }
                    });

                    dialog.show();
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 현재 리스트 아이템 위치
                    int curPos = getAdapterPosition();
                    PostItem postItem = mPostItems.get(curPos);

                    Intent gotoDetail = new Intent(mContext,DetailActivity.class);
                    gotoDetail.putExtra("postId", postItem.getId()+"");

                    Toast.makeText(mContext, postItem.getId() + "입니다.", Toast.LENGTH_SHORT).show();

                    gotoDetail.putExtra("title", tv_title.getText().toString());
                    gotoDetail.putExtra("content", tv_content.getText().toString());
                    ((MainActivity)mContext).startActivity(gotoDetail);


                }
            });


        }
    }

    // 액티비티에서 호출되는함수.
    // 현재 어댑터에 새로운 게이글 전달받아 추가하는 목적
    public void addItem(PostItem _item) {
        // 항상 0번째로 넣는다(역순으로) => 최신의 데이터가 위에 오도록
        mPostItems.add(0, _item);
        notifyItemInserted(0);
    }

    
    // 관리자모드(블라인드 모드)
    public void changeMode(int check) {
        // 메인엑티비티에서 관리자 모드 클릭시 getCheck == 1이 되고
        // 버튼을 숨긴다.
        if(check == 0) {
            btn_update.setVisibility(View.INVISIBLE);
            btn_delete.setVisibility(View.INVISIBLE);


        } else if(check == 1) {
            btn_update.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);

        }
    }

}
