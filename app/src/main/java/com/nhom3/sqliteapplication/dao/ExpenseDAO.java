package com.nhom3.sqliteapplication.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhom3.sqliteapplication.dto.ExpenseDTO;
import com.nhom3.sqliteapplication.dto.PersonDTO;
import com.nhom3.sqliteapplication.database.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    DbHelper dbHelper;
    SQLiteDatabase db ;
    public ExpenseDAO(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(ExpenseDTO NHANVIEN){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", NHANVIEN.getName());
        contentValues.put("Money", NHANVIEN.getMoney());
        contentValues.put("Content", NHANVIEN.getContent());
        contentValues.put("Day",String.valueOf(NHANVIEN.getDay()));
        long res = db.insert("EXPENSE",null,contentValues);
        return res ;
    }

    public long update(ExpenseDTO NHANVIEN){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", NHANVIEN.getName());
        contentValues.put("Money", NHANVIEN.getMoney());
        contentValues.put("Content", NHANVIEN.getContent());
        contentValues.put("Day",String.valueOf(NHANVIEN.getDay()));
        long res = db.update("EXPENSE",contentValues,"Id=?",new String[]{NHANVIEN.getId()+""});
        return res ;
    }

    public int delete(int id){
        long  check = db.delete("EXPENSE","Id=?",new String[]{String.valueOf(id)});
        if(check==-1){
            return  0 ;
        }
        return 1 ;
    }

    public ArrayList<ExpenseDTO> getAll(){
        String sql="SELECT * FROM EXPENSE";
        return (ArrayList<ExpenseDTO>) getData(sql);
    }

    public ExpenseDTO getID(String id){
        String sql = "SELECT * FROM EXPENSE WHERE Id=?";
        List<ExpenseDTO> list = getData(sql,id);
        return list.get(0);
    }

    @SuppressLint("Range")
    private List<ExpenseDTO> getData(String sql, String...selectionArgs) {

        List<ExpenseDTO> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            ExpenseDTO obj = new ExpenseDTO();
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("Id"))));
            obj.setName(c.getString(c.getColumnIndex("Name")));
            obj.setMoney(Integer.parseInt(c.getString(c.getColumnIndex("Money"))));
            obj.setContent(c.getString(c.getColumnIndex("Content")));
            obj.setDay(c.getString(c.getColumnIndex("Day")));

            list.add(obj);
        }
        return list;
    }


}

