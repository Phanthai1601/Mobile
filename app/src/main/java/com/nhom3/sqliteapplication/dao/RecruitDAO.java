package com.nhom3.sqliteapplication.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhom3.sqliteapplication.dto.RecruitDTO;
import com.nhom3.sqliteapplication.database.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class RecruitDAO {
    DbHelper dbHelper;
    SQLiteDatabase db ;
    public RecruitDAO(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(RecruitDTO person){
        ContentValues contentValues = new ContentValues();
        contentValues.put("RecruitID",person.getRecruitid());
        contentValues.put("Id",person.getId());
        contentValues.put("Title",person.getTitle());
        contentValues.put("Content",person.getContent());
        long res = db.insert("RECRUITS",null,contentValues);
        return res ;
    }

    public long update(RecruitDTO person){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Title",person.getTitle());
        contentValues.put("Content",person.getContent());
        long res = db.update("RECRUITS",contentValues,"Recruitid=?",new String[]{person.getRecruitid()+""});
        return res ;
    }

    public int delete(int recruitid){
        long  check = db.delete("RECRUITS","Recruitid=?",new String[]{String.valueOf(recruitid)});
        if(check==-1){
            return  0 ;
        }
        return 1 ;
    }

    public ArrayList<RecruitDTO> getAll(){
        String sql="SELECT * FROM RECRUITS";
        return (ArrayList<RecruitDTO>) getData(sql);
    }

    public RecruitDTO getRecruitid(String recruit){
        String sql = "SELECT * FROM RECRUITS WHERE Recruitid=?";
        List<RecruitDTO> list = getData(sql,recruit);
        return list.get(0);
    }


    @SuppressLint("Range")
    private List<RecruitDTO> getData(String sql, String...selectionArgs) {

        List<RecruitDTO> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            RecruitDTO obj = new RecruitDTO();
            obj.setRecruitid(Integer.parseInt(c.getString(c.getColumnIndex("Recruitid"))));
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("Id"))));
            obj.setTitle(c.getString(c.getColumnIndex("Title")));
            obj.setContent(c.getString(c.getColumnIndex("Content")));

            list.add(obj);
        }
        return list;
    }


}
