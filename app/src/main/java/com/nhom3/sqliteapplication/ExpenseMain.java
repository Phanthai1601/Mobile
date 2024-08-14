package com.nhom3.sqliteapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nhom3.sqliteapplication.adapter.ExpenseAdapter;
import com.nhom3.sqliteapplication.adapter.PersonAdapter;
import com.nhom3.sqliteapplication.dao.ExpenseDAO;
import com.nhom3.sqliteapplication.dao.PersonDAO;
import com.nhom3.sqliteapplication.dto.ExpenseDTO;
import com.nhom3.sqliteapplication.dto.PersonDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ExpenseMain extends AppCompatActivity {

    private RecyclerView rcv;
    private FloatingActionButton btnAddPerson;
    private LinearLayoutManager linearLayoutManager;

    private ExpenseDAO dao ;
    private ExpenseAdapter adapter;
    private ArrayList<ExpenseDTO> list ;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actyivity_expense);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //vd4 set back button
        ActionBar actionBar = getSupportActionBar();
        String tab = "";
        for (int i = 0; i < 4; i++) {
            tab += "\t";
        }
        actionBar.setTitle(tab+"Quản Lý chi phí");
        actionBar.setDisplayHomeAsUpEnabled(true);
        rcv = findViewById(R.id.rcvPerson);
        btnAddPerson = findViewById(R.id.btnAddPerson);
        linearLayoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(linearLayoutManager);
        dao = new ExpenseDAO(this);
        list = dao.getAll();
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
        adapter = new ExpenseAdapter(this,list);
        rcv.setAdapter(adapter);
        Calendar calendar =Calendar.getInstance();
        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(ExpenseMain.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add_expense);
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
                EditText ed_name = dialog.findViewById(R.id.edName);
                EditText ed_money = dialog.findViewById(R.id.edMoney);
                EditText ed_content = dialog.findViewById(R.id.edContent);
                EditText ed_day = dialog.findViewById(R.id.edBirthday);
                ImageView img = dialog.findViewById(R.id.imgdate);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog dialog1 = new DatePickerDialog(ExpenseMain.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                int myear = year ;
                                int mmonth = month ;
                                int mdayOfMonth = dayOfMonth ;
                                GregorianCalendar c = new GregorianCalendar(myear,mmonth,mdayOfMonth);
                                ed_day.setText(sdf.format(c.getTime()));
                            }
                        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
                        dialog1.show();
                    }
                });
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ed_name.getText().toString().isEmpty()){
                            ed_name.setError("Tên không được để trống");
                        }else if (ed_money.getText().toString().isEmpty()) {
                            ed_money.setError("Số tiền không được để trống");
                        }else  if (ed_content.getText().toString().isEmpty()) {
                            ed_content.setError("Nội dung chi không được để trống");
                        }else if (ed_day.getText().toString().isEmpty()) {
                            ed_day.setError("Ngày Chi không được để trống");
                        }else if(!(isValidFormat("dd/MM/yyyy",ed_day.getText().toString()))){
                            ed_day.setError("Không đúng định dạng ngày");
                        }
                        else {
                            ExpenseDTO NHANVIEN = new ExpenseDTO();
                            NHANVIEN.setName(ed_name.getText().toString());
                            NHANVIEN.setMoney(Integer.parseInt(ed_money.getText().toString()));
                            NHANVIEN.setContent(ed_content.getText().toString());
                            NHANVIEN.setDay(ed_day.getText().toString());
                            long res = dao.insert(NHANVIEN);
                            if (res>0){
                                Toast.makeText(ExpenseMain.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
                                list.clear();
                                list.addAll(dao.getAll());
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(ExpenseMain.this,"Thêm thất bại",Toast.LENGTH_SHORT).show();
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
        ArrayList<ExpenseDTO> filteredList=new ArrayList<>();
        //list=dao.getAll();
        for (ExpenseDTO NHANVIEN : list){
            if (NHANVIEN.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(NHANVIEN);
            }

        }
        if (filteredList.isEmpty()){
            Toast.makeText(ExpenseMain.this, "no data", Toast.LENGTH_SHORT).show();
        }else {
            adapter.setFilteredList(filteredList);
        }
    }
    public boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
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