//All the database related operations are found here
package com.example.quicklogs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Database extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "Expenses.db";
    private static final String TABLE_EXPENSES = "Expenses";
    private static final String TABLE_INCOMES = "Incomes";
    private static final String TABLE_LOGIN = "Login";
    private static final int DATABASE_VERSION = 1;
    public static final String COL_1 ="userid";
    public static final String COL_2 ="name";
    public static final String COL_3 ="password";
    public static int uid;
    public static float tsavings;

    Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //creating the required tables
        // details related to the expenses made by the user
        String CREATE_EXPENSES = "CREATE TABLE " + TABLE_EXPENSES +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type VARCHAR(10) NOT NULL, " +
                "amount FLOAT(6) NOT NULL, " +
                "description VARCHAR(250) NOT NULL, " +
                "userid INTEGER NOT NULL," +
                "date DATE NOT NULL," +
                "FOREIGN KEY(userid) REFERENCES "+TABLE_LOGIN+"(userid));";
       // details related to the income and desired savings of the user are stored
        String CREATE_INCOMES = "CREATE TABLE " + TABLE_INCOMES +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount FLOAT(6) NOT NULL, " +
                "date DATE NOT NULL," +
                "maxdailyexpense FLOAT(10) NOT NULL,"+
                "desiredasavings FLOAT(10) NOT NULL,"+
                "requiredasavings FLOAT(10) NOT NULL,"+
                "userid INTEGER NOT NULL," +
                "FOREIGN KEY(userid) REFERENCES "+TABLE_LOGIN+"(userid));";
       //login details are stored
        String CREATE_LOGIN = "CREATE TABLE " + TABLE_LOGIN +
                "(userid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(20) NOT NULL, " +
                "password VARCHAR(20) NOT NULL, " +
                "email VARCHAR(20) NOT NULL, " +
                "phone INT(10) NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_EXPENSES);
        sqLiteDatabase.execSQL(CREATE_INCOMES);
        sqLiteDatabase.execSQL(CREATE_LOGIN);

    }

    //when the database is updated
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_TABLES = "DROP TABLE " + TABLE_EXPENSES + ";";
        String DROP_2= "DROP TABLE " + TABLE_INCOMES + ";";
        String DROP_3= "DROP TABLE " +  TABLE_LOGIN + ";";
        sqLiteDatabase.execSQL(DROP_TABLES);
        sqLiteDatabase.execSQL(DROP_2);
        sqLiteDatabase.execSQL(DROP_3);
        onCreate(sqLiteDatabase);
    }
    //when a new expense is added
    void addExpense(Expense expense) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", expense.getType());
        contentValues.put("amount", expense.getAmount());
        contentValues.put("description", expense.getDescription());
        contentValues.put("date", expense.getDate());
        contentValues.put("userid",uid);
        db.insert(TABLE_EXPENSES, null, contentValues);
    }

    //adding or updating the income and desired annual savings of the user
    void addIncome(float amount, String date, float dasavings) {
        String users = String.valueOf(uid);
        //if it is the first time user is providing the income and savings details
        if(!checkuserid(users)) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("amount", amount);
            contentValues.put("date", date);
            contentValues.put("desiredasavings", dasavings);
            //maximum daily expense
            float mdexpense = (amount - dasavings) / 365;
            contentValues.put("maxdailyexpense", mdexpense);
            //required annual savings = desired annual savings
            float rasavings =dasavings;
            contentValues.put("requiredasavings", rasavings);
            contentValues.put("userid", uid);
            db.insert(TABLE_INCOMES, null, contentValues);
        }
        // if the user wants to update the income and savings
        else{
            SQLiteDatabase db = getWritableDatabase();
            float tsavings = 0;
            ContentValues contentValues = new ContentValues();
            contentValues.put("amount", amount);
            contentValues.put("date", date);
            contentValues.put("desiredasavings", dasavings);
            float mdexpense = (amount - dasavings) / 365;
            contentValues.put("maxdailyexpense", mdexpense);
            //required annual savings = desired annual savings - today's savings
            float rasavings =dasavings-tsavings;
            contentValues.put("requiredasavings", rasavings);
            contentValues.put("userid", uid);
            db.update(TABLE_INCOMES, contentValues,"userid="+uid,null);
        }
    }
    //calculate the new required savings
    float getNewRequiredSavings(float savings){
        tsavings = savings;
        float dasavings,rasavings;
        float amount;
        SQLiteDatabase db = getReadableDatabase();
        //get the desired annual savings
        String SELECT_ASAVINGS = "SELECT desiredasavings FROM " + TABLE_INCOMES + " WHERE userid = " + uid +";";
        Cursor cur = db.rawQuery(SELECT_ASAVINGS, null);
        cur.moveToFirst();
        dasavings = cur.getFloat(0);
        rasavings = dasavings - savings;
        cur.close();
        // get the annual income
        String SELECT_AMOUNT = "SELECT amount FROM " + TABLE_INCOMES + " WHERE userid = " + uid +";";
        Cursor cur2 = db.rawQuery(SELECT_AMOUNT, null);
        cur2.moveToFirst();
        amount = cur2.getFloat(0);
        float mdexpense = (amount - dasavings) / 365;
        cur2.close();
        //update the table with new values
        SQLiteDatabase db2 = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("maxdailyexpense", mdexpense);
        contentValues.put("requiredasavings", rasavings);
        db.update(TABLE_INCOMES, contentValues,"userid="+uid,null);
        return rasavings;

    }
    //getting the maximum daily expense
    float getMaxExpense(){
        float mdexpense=0;
        SQLiteDatabase db = getReadableDatabase();

        String SELECT_MDEXPENSE = "SELECT maxdailyexpense FROM " + TABLE_INCOMES + " WHERE userid = " + uid +";";
        Cursor cur = db.rawQuery(SELECT_MDEXPENSE, null);
        cur.moveToFirst();
        mdexpense = cur.getFloat(0);
        cur.close();
        return mdexpense;
    }
    //getting the expenses of a user for that month and year
    List<Expense> getExpensesForMonth(String month, String year) {
        SQLiteDatabase db = getReadableDatabase();
        List<Expense> expenses = new ArrayList<>();

        String SELECT_ALL = "SELECT * FROM " + TABLE_EXPENSES + " WHERE userid ="+uid+" and date LIKE '%-" + month + "-" + year + "' ORDER BY id DESC;";
        Cursor selectCursor = db.rawQuery(SELECT_ALL, null);

        if (selectCursor.moveToFirst()) {
            do {
                expenses.add(new Expense(selectCursor.getString(selectCursor.getColumnIndex("description")), selectCursor.getString(selectCursor.getColumnIndex("type")), selectCursor.getString(selectCursor.getColumnIndex("date")), selectCursor.getFloat(selectCursor.getColumnIndex("amount"))));
            } while (selectCursor.moveToNext());
        }

        selectCursor.close();
        return expenses;
    }
   //getting the graph data for that month and yeear
    JSONObject getGraphData(String month, String year) {
        SQLiteDatabase db = getReadableDatabase();
        JSONObject result = new JSONObject();
        String SELECT_MAX_DATE = "SELECT MAX(date) FROM " + TABLE_EXPENSES + " WHERE userid = "+uid+" and date LIKE '%-" + month + "-" + year + "';";
        String SELECT_MIN_DATE = "SELECT MIN(date) FROM " + TABLE_EXPENSES + " WHERE userid = "+uid+" and date LIKE '%-" + month + "-" + year + "';";
        String SELECT_MAX_EXPENSE = "SELECT MAX(amount) FROM " + TABLE_EXPENSES + " WHERE userid = "+uid+" and date LIKE '%-" + month + "-" + year + "';";
        String SELECT_TOTAL_INCOME = "SELECT SUM(amount) FROM " + TABLE_INCOMES + " WHERE userid = "+uid+" and date LIKE '%-" + month + "-" + year + "';";

        Cursor values = null;
        try {
            values = db.rawQuery(SELECT_MAX_DATE, null);
            values.moveToFirst();
            result.put("lastDate", values.getString(0));

            values = db.rawQuery(SELECT_MIN_DATE, null);
            values.moveToFirst();
            result.put("firstDate", values.getString(0));

            values = db.rawQuery(SELECT_MAX_EXPENSE, null);
            values.moveToFirst();
            result.put("maxExpense", values.getString(0));

            values = db.rawQuery(SELECT_TOTAL_INCOME, null);
            values.moveToFirst();
            result.put("totalIncome", values.getString(0));
        } catch (JSONException e) {
            Log.e("JSON ERROR", e.toString());
        }

        Log.e("Result", result.toString());
        values.close();
        return result;
    }
    // get the expenses for a day for that user
    float getExpensesForDate(int date, String month, String year) {
        String day;
        if(date < 10) {
            day = "0" + date + "-" + month + "-" + year;
        }
        else {
            day = date + "-" + month + "-" + year;
        }
        SQLiteDatabase db = getReadableDatabase();
        String SELECT_EXPENSE = "SELECT SUM(amount) FROM " + TABLE_EXPENSES + " WHERE userid = "+uid+" and date = '" + day + "';";
        Cursor expense = db.rawQuery(SELECT_EXPENSE, null);
        expense.moveToFirst();
        float result = expense.getFloat(0);
        expense.close();
        return result;
    }

    //getting the expense for a category
    float getExpenseForCategory(String value) {
        float result;
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
        String monthYear = dateFormat.format(calendar.getTime());

        SQLiteDatabase db = getReadableDatabase();
        String SELECT_QUERY = "SELECT SUM(amount) FROM " + TABLE_EXPENSES + " WHERE userid = "+uid+" and type = '" + value + "' AND date LIKE '%-" + monthYear + "';";
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        cursor.moveToFirst();
        result = cursor.getFloat(0);
        cursor.close();
        return result;
    }
    //adding a new user
    public long addUser(String name, String password, String email, String phone) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(calendar.getTime());

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);

        contentValues.put("email", email);
        contentValues.put("phone", phone);

      long res =  db.insert(TABLE_LOGIN, null, contentValues);
        uid = getUserid(name,password);
        addIncome(0,date,0);
     return res;
    }

    // changing the password
    public long changePassword(String oldpassword,String password){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        long res = db.update(TABLE_LOGIN, contentValues,"userid="+uid+" and password=?",new String[] {oldpassword});
        return res;
    }

    //updating the user details
    public long updateUser(String name,String email,String mobile){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("phone", mobile);
        long res = db.update(TABLE_LOGIN, contentValues,"userid="+uid,null);
        return res;
    };

    //checking if the user exists in login table
    public boolean checkUser(String username, String password){
        String[] columns = { COL_1 };
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_2 + "=?" + " and " + COL_3 + "=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE_LOGIN,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count>0) {
            uid = getUserid(username, password);
            return true;

        }
        else
            return  false;
    }

    //checking if the user id is there in the incomes tables
    public boolean checkuserid(String uid){
        String[] columns = { COL_1 };
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_1 + "=?" ;
        String[] selectionArgs = { uid };
        Cursor cursor = db.query(TABLE_INCOMES,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count>0) {
            return true;

        }
        else
            return  false;
    }
    //getting the userid of the user when they log in
    int getUserid(String name,String password){
        SQLiteDatabase db = getReadableDatabase();
        String SELECT_USERID = "SELECT userid FROM " + TABLE_LOGIN + " WHERE name = '" + name + "' and password = '"+password+ "';";
        Cursor userid = db.rawQuery(SELECT_USERID, null);
        userid.moveToFirst();
        int result = userid.getInt(0);
        userid.close();
        return result;
    }
  // get the savings made today
    float getTodaySavings(String date){
        float totalamount = 0;
        float maxdailyexpense = 0;
        float savings = 0;
        SQLiteDatabase db = getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String newdate = dateFormat.format(calendar.getTime());
        String SELECT_TOTALAMOUNT = "SELECT SUM(amount) FROM " + TABLE_EXPENSES + " WHERE userid = "+uid+" AND date LIKE '%" + newdate + "';";

        Cursor cursor2 = db.rawQuery(SELECT_TOTALAMOUNT, null);
        if( cursor2 != null && cursor2.moveToFirst() ) {
            //cursor2.moveToFirst();
            totalamount = cursor2.getFloat(0);
            cursor2.close();
        }
        String SELECT_MAXEXPENSE = "SELECT MAXDAILYEXPENSE FROM " + TABLE_INCOMES + " WHERE userid = "+uid+" AND date LIKE '%" + newdate + "';";
        Cursor cursor3 = db.rawQuery(SELECT_MAXEXPENSE, null);
        if( cursor3 != null && cursor3.moveToFirst() ) {
         //   cursor3.moveToFirst();
            maxdailyexpense = cursor3.getFloat(0);
            cursor3.close();
        }
       savings = maxdailyexpense - totalamount ;
        return savings;
    }

    //deleting the expense
    void deleteExpense(Expense expense) {
        SQLiteDatabase db = getWritableDatabase();
        String DELETE_QUE = "DELETE FROM " + TABLE_EXPENSES + " WHERE userid =" +uid+ " AND round(amount,1) = " + expense.getAmount() + " AND description = '" + expense.getDescription() + "' AND date = '" + expense.getDate() + "' AND type = '" + expense.getType() + "';";

        db.execSQL(DELETE_QUE);
    }
}
