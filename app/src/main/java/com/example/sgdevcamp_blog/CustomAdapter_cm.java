package com.example.sgdevcamp_blog;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter_cm extends RecyclerView.Adapter<CustomAdapter_cm.ViewHolder_cm> {

    private ArrayList<CommentItem> mCommentItems;
    private Context mContext;
    private DBHelper_cm mDBHelper_cm;

    public CustomAdapter_cm(ArrayList<CommentItem> mCommentItems, Context mContext) {
        this.mCommentItems = mCommentItems;
        this.mContext = mContext;
        mDBHelper_cm = new DBHelper_cm(mContext);
    }

    @NonNull
    @Override
    public ViewHolder_cm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list, parent, false);
        return new ViewHolder_cm(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_cm.ViewHolder_cm holder, int position) {
        holder.tv_comment.setText(mCommentItems.get(position).getComment());
    }

    @Override
    public int getItemCount() {return mCommentItems.size();}

    public void addItem(CommentItem _item) {
        mCommentItems.add(0, _item);
        notifyItemInserted(0);
    }

    public class ViewHolder_cm extends RecyclerView.ViewHolder {

        private TextView tv_comment;
        private ImageButton btn_delete_detail;

        public ViewHolder_cm(@NonNull View itemView) {
            super(itemView);

            tv_comment = itemView.findViewById(R.id.tv_comment);
            btn_delete_detail = itemView.findViewById(R.id.btn_delete_detail);

            // 댓글 삭제
            btn_delete_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curPos = getAdapterPosition();
                    CommentItem commentItem = mCommentItems.get(curPos);

                    // delete table
                    mDBHelper_cm.DeleteComment(commentItem.getId());
                   // mDBHelper_cm.DeleteCommentAll(0);

                    // delete UI
                    mCommentItems.remove(curPos);
                    notifyItemRemoved(curPos);
                    Toast.makeText(mContext, "목록이 제거되었습니다.", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}
