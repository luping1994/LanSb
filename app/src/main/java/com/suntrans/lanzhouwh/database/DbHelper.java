package com.suntrans.lanzhouwh.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "BOOKS.db";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "books_table";
	public final static String BOOK_ID = "book_id";
	public final static String BOOK_NAME = "book_name";
	public final static String BOOK_AUTHOR = "book_author";
	public DbHelper(Context context, String name, CursorFactory factory,
					int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub      INTEGER primary key autoincrement自增长
		//String sql = "drop table if exists switch_tb " ;


		String sqls = " create table  userinfo_tb ( NID INTEGER primary key autoincrement,"  //用户名ID 唯一标示通道 ，自增长
				+ "ruid TEXT,"   //第六感名称
				+ "rusername TEXT,"     //第六感地址
				+ "email TEXT,"   //报警内容
				+ "mobile TEXT,"   //报警内容
				+ "rusergid TEXT,"   //报警内容
				+ "nickname TEXT,"   //报警内容
				+ "verifyemail TEXT,"   //报警内容
				+ "verifymobile TEXT,"   //报警内容
				+ "issystem TEXT,"   //报警内容
				+ "isqiyong TEXT,"   //报警内容
				+ "avatar TEXT,"   //报警内容
				+ "deptidlist TEXT);";  //报警时间
		db.execSQL(sqls);    //创建报警记录表


	}

	@Override    //数据库版本更新的时候调用
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	//查询操作
	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("switch_tb", null, null, null, null, null, null);
		return cursor;
	}
	//增加操作
	public long insert(String bookname, String author)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(BOOK_NAME, bookname);
		cv.put(BOOK_AUTHOR, author);
		long row = db.insert(TABLE_NAME, null, cv);


		return row;
	}
	//删除操作
	public void delete(int id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = BOOK_ID + " = ?";
		String[] whereValue ={ Integer.toString(id) };
		db.delete(TABLE_NAME, where, whereValue);
	}
	//修改操作
	public void update(int id, String bookname, String author)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = BOOK_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put(BOOK_NAME, bookname);
		cv.put(BOOK_AUTHOR, author);
		db.update(TABLE_NAME, cv, where, whereValue);
	}
}
