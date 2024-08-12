package com.nhom3.sqliteapplication.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhom3.sqliteapplication.dto.RelaxDTO;
import com.nhom3.sqliteapplication.database.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class RelaxDAO {
    DbHelper dbHelper;
    SQLiteDatabase db ;
    public RelaxDAO(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(RelaxDTO person){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Relaxid",person.getRelaxid());
        contentValues.put("Id",person.getId());
        contentValues.put("Reason",person.getReason());
        contentValues.put("Day",person.getDay());
        contentValues.put("Date",String.valueOf(person.getDate()));
        long res = db.insert("RELAXS",null,contentValues);
        return res ;
    }

    public long update(RelaxDTO person){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Reason",person.getReason());
        contentValues.put("Day",person.getDay());
        contentValues.put("Date",String.valueOf(person.getDate()));
        long res = db.update("RELAXS",contentValues,"Relaxid=?",new String[]{person.getRelaxid()+""});
        return res ;
    }

    public int delete(int relaxid){
        long  check = db.delete("RELAXS","Relaxid=?",new String[]{String.valueOf(relaxid)});
        if(check==-1){
            return  0 ;
        }
        return 1 ;
    }

    public ArrayList<RelaxDTO> getAll(){
        String sql="SELECT * FROM RELAXS";
        return (ArrayList<RelaxDTO>) getData(sql);
    }

    public RelaxDTO getRelaxid(String relaxid){
        String sql = "SELECT * FROM RELAXS WHERE Relaxid=?";
        List<RelaxDTO> list = getData(sql,relaxid);
        return list.get(0);
    }


    @SuppressLint("Range")
    private List<RelaxDTO> getData(String sql, String...selectionArgs) {

        List<RelaxDTO> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            RelaxDTO obj = new RelaxDTO();
            obj.setRelaxid(Integer.parseInt(c.getString(c.getColumnIndex("Relaxid"))));
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("Id"))));
            obj.setDay(c.getString(c.getColumnIndex("Day")));
            obj.setReason(c.getString(c.getColumnIndex("Reason")));
            obj.setDate(c.getString(c.getColumnIndex("Date")));
            list.add(obj);
        }
        return list;
    }


}
