package com.nhom3.sqliteapplication.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom3.sqliteapplication.dao.PersonDAO;
import com.nhom3.sqliteapplication.dao.RecruitDAO;
import com.nhom3.sqliteapplication.dto.PersonDTO;
import com.nhom3.sqliteapplication.R;
import com.nhom3.sqliteapplication.dto.RecruitDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RecruitAdapter extends RecyclerView.Adapter<RecruitAdapter.PersonViewHolder> {

    private Context context;
    private ArrayList<RecruitDTO> list;

    public RecruitAdapter(Context context, ArrayList<RecruitDTO> list){
        this.context= context;
        this.list = list;
    }
    public void setFilteredList(ArrayList<RecruitDTO> filteredList){
        this.list=filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View studentView =
                inflater.inflate(R.layout.item_recruit, parent, false);
        PersonViewHolder viewHolder = new PersonViewHolder(studentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        RecruitDTO NHANVIEN = list.get(position);

        holder.txtReID.setText("ID:"+list.get(position).getRecruitid());
        holder.txtReMaTD.setText("Mã chính sách:"+list.get(position).getRecruitid());
        holder.txtReTitle.setText("Tiêu đề:"+list.get(position).getTitle());
        holder.txtReContent.setText("Nội dung:"+list.get(position).getContent());


        RecruitDAO dao = new RecruitDAO(context);
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add_recruit);
                Window window = dialog.getWindow();
                if(window==null){
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams windowacc = window.getAttributes();
                windowacc.gravity = Gravity.NO_GRAVITY ;
                window.setAttributes(windowacc);
                Button btnCancel = dialog.findViewById(com.nhom3.sqliteapplication.R.id.btnCancel);

                Button btnAdd = dialog.findViewById(R.id.btnAdd_KH);
                btnAdd.setText("CẬP NHẬT");
                EditText ed_title = dialog.findViewById(R.id.edReTitle);
                EditText ed_content = dialog.findViewById(R.id.edReContent);

                TextView txt = dialog.findViewById(R.id.txtTitel);
                txt.setText("SỬA KHÁCH HÀNG");
                ed_title.setText(NHANVIEN.getTitle());
                ed_content.setText(NHANVIEN.getContent()+"");


                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ed_title.getText().length()==0||
                                ed_content.getText().length()==0){
                            Toast.makeText(context,"Không được để trống",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            NHANVIEN.setTitle(ed_title.getText().toString());
                            NHANVIEN.setContent(ed_content.getText().toString());
                            long res = dao.update(NHANVIEN);
                            if (res>0){
                                Toast.makeText(context,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                                list.clear();
                                list.addAll(dao.getAll());
                                notifyDataSetChanged();
                            }else {
                                Toast.makeText(context,"Cập nhật thất bại ",Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("Bạn có chắc muốn xóa ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int check = dao.delete(list.get(holder.getAdapterPosition()).getRecruitid());
                        switch (check){
                            case  1 :
                                list.clear();
                                list.addAll(dao.getAll());
                                notifyDataSetChanged();
                                Toast.makeText(context,"Xóa thành công",Toast.LENGTH_SHORT).show();
                                break;
                            case -1:
                                Toast.makeText(context,"Không thể xóa ",Toast.LENGTH_SHORT).show();
                                break;
                            case 0 :
                                Toast.makeText(context,"Xóa không thành công",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }

                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class PersonViewHolder extends RecyclerView.ViewHolder{
        TextView txtReID,txtReMaTD , txtReTitle , txtReContent;
        ImageView imgDel,imgEdit;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);

            txtReID = itemView.findViewById(R.id.txtReID);
            txtReMaTD=itemView.findViewById(R.id.txtReMaTD);
            txtReTitle = itemView.findViewById(R.id.txtReTitle);
            txtReContent = itemView.findViewById(R.id.txtReContent);
            imgDel = itemView.findViewById(R.id.imgRedel);
            imgEdit = itemView.findViewById(R.id.imgReedit);
        }
    }

    public boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = (Date) sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }
}
