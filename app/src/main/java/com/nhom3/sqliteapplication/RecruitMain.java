package com.nhom3.sqliteapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nhom3.sqliteapplication.adapter.RecruitAdapter;
import com.nhom3.sqliteapplication.dao.RecruitDAO;
import com.nhom3.sqliteapplication.dto.RecruitDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecruitMain extends AppCompatActivity {
    private RecyclerView rcv;
    private FloatingActionButton btnAddPerson;
    private LinearLayoutManager linearLayoutManager;
    private RecruitDAO dao;
    private RecruitAdapter adapter;
    private ArrayList<RecruitDTO> list;
    private SearchView searchView;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recruit_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //vd4 set back button
        ActionBar actionBar = getSupportActionBar();
       /* String tab = "";
        for (int i = 0; i < 4; i++) {
            tab += "\t";
        }*/
        actionBar.setTitle(/*tab+*/"Quản Lý Chính Sách Tuyển Dụng");
        actionBar.setDisplayHomeAsUpEnabled(true);
        rcv = findViewById(R.id.rcvPerson);
        btnAddPerson = findViewById(R.id.btnAddPerson);
        linearLayoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(linearLayoutManager);
        dao=new RecruitDAO(this);
        list=dao.getAll();
        searchView=findViewById(R.id.searchPerson);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FinterList(newText);
                return true;
            }
        });
        adapter = new RecruitAdapter(this,list);
        rcv.setAdapter(adapter);
        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(RecruitMain.this);
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

                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnAdd = dialog.findViewById(R.id.btnAdd_KH);

                EditText ed_id = dialog.findViewById(R.id.edReId);
                EditText ed_title = dialog.findViewById(R.id.edReTitle);
                EditText ed_content = dialog.findViewById(R.id.edReContent);


                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ed_title.getText().toString().isEmpty()){
                            ed_title.setError("không được để trống");
                        }else if(ed_id.getText().toString().isEmpty()){
                            ed_id.setError("không được để trống");
                        }else if (ed_content.getText().toString().isEmpty()) {
                            ed_content.setError("không được để trống");
                        }
                        else {
                            RecruitDTO person = new RecruitDTO();

                            person.setRecruitid(Integer.parseInt(ed_id.getText().toString()));
                            person.setTitle(ed_title.getText().toString());
                            person.setContent(ed_content.getText().toString());

                            long res = dao.insert(person);
                            if (res>0){
                                Toast.makeText(RecruitMain.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
                                list.clear();
                                list.addAll(dao.getAll());
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(RecruitMain.this,"Thêm thất bại",Toast.LENGTH_SHORT).show();
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
    }
    private void FinterList(String text) {
        ArrayList<RecruitDTO> filteredList=new ArrayList<>();
//        list=dao.getAll();

        for (RecruitDTO person: list){
            if (String.valueOf(person.getRecruitid()).toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(person);
            }

        }
        if (filteredList.isEmpty()){
            Toast.makeText(RecruitMain.this, "no data", Toast.LENGTH_SHORT).show();
        }else {
            adapter.setFilteredList(filteredList);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_report) {
            Toast.makeText(getApplicationContext(), "Chức năng đang trong quá trình phát triển",Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id==R.id.action_close) {
            finish();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

}
