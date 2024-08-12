package com.nhom3.sqliteapplication.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhom3.sqliteapplication.dto.LoginDTO;
import com.nhom3.sqliteapplication.database.DbHelper;

public class LoginDAO {

    private DbHelper dbHelper;

    SQLiteDatabase db;

    public LoginDAO(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(LoginDTO item){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id",item.getId());
        contentValues.put("Username",item.getUsername());
        contentValues.put("Password",item.getPassword());
        contentValues.put("Fullname",item.getFullname());
        long res = db.insert("LOGINS",null,contentValues);
        return res;
    }

    public long update(int id, LoginDTO item){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id",item.getId());
        contentValues.put("Username",item.getUsername());
        contentValues.put("Password",item.getPassword());
        contentValues.put("Fullname",item.getFullname());
        long res = db.update("LOGINS", contentValues,"IDTK=?",new String[]{String.valueOf(id)});
        return res;
    }

    public long delete(int id){
        long res = db.delete("LOGINS","IDTK=?",new String[]{String.valueOf(id)});
        if(res == -1 ) return 0;
        return 1;
    }
    @SuppressLint("Range")
    public LoginDTO Login(String username, String password){

        // Câu truy vấn kiểm tra xem username có tồn tại không
        String query = "SELECT * FROM LOGINS WHERE Username = ? AND Password = ?";
        String[] selectionArgs = {username, password};

        // Thực hiện truy vấn
        Cursor cursor = db.rawQuery(query,selectionArgs);
//        while(cursor.moveToNext()){
//            add.
//        }
//        return list

        if (cursor.moveToFirst()) {
            // Truy cập dữ liệu từ Cursor
            int Id = cursor.getInt(cursor.getColumnIndex("Id"));
            String Username = cursor.getString(cursor.getColumnIndex("Username"));
            String Password = cursor.getString(cursor.getColumnIndex("Password"));
            String Fullname = cursor.getString(cursor.getColumnIndex("Fullname"));
            LoginDTO user = new LoginDTO(Id,Username,Password,Fullname);
            cursor.close();
            return user;
        }
        // Đóng Cursor
        cursor.close();
        return null;
    }
}
