package com.namobiletech.imamiajantri.Utils.dbUtility;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.namobiletech.imamiajantri.Model.AyyatList;
import com.namobiletech.imamiajantri.Model.ListAyyat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.namobiletech.imamiajantri.Utils.dbUtility.DbContract.MenuEntry.AYAH_DESCRIPTION_TABLE_ARABIC;
import static com.namobiletech.imamiajantri.Utils.dbUtility.DbContract.MenuEntry.AYAH_DESCRIPTION_TABLE_ENGLISH;
import static com.namobiletech.imamiajantri.Utils.dbUtility.DbContract.MenuEntry.DUA;
import static com.namobiletech.imamiajantri.Utils.dbUtility.DbContract.MenuEntry.FAVOURITE;
import static com.namobiletech.imamiajantri.Utils.dbUtility.DbContract.MenuEntry.QURAN_NAME_ARBI;
import static com.namobiletech.imamiajantri.Utils.dbUtility.DbContract.MenuEntry.TABLE_NAME;

public class DbHelper extends SQLiteOpenHelper {

    public static final String TAG = DbHelper.class.getSimpleName();

    private Resources mResources;

    private static final String DATABASE_NAME = "menu.db";
    private static final int DATABASE_VERSION = 8;
    ArrayList<ListAyyat> list = new ArrayList<>();
    ArrayList<ListAyyat> listenglish = new ArrayList<>();

    final String SQL_CREATE_NAMAZ_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            DbContract.MenuEntry.DATE + " TEXT NOT NULL," +
            DbContract.MenuEntry.COLUMN_FAJR + " TEXT NOT NULL, " +
            DbContract.MenuEntry.COLUMN_DHUHR + " TEXT NOT NULL, " +
            DbContract.MenuEntry.COLUMN_ASR + " TEXT NOT NULL, " +
            DbContract.MenuEntry.COLUMN_MAGHRIB + " TEXT NOT NULL, " +
            DbContract.MenuEntry.COLUMN_ISHA + " TEXT NOT NULL, " +
            DbContract.MenuEntry.COLUMN_SUNRISE + " TEXT NOT NULL, " +
            DbContract.MenuEntry.COLUMN_IMSAK + " INTEGER NOT NULL " + " );";

    final String SQL_CREATE_SURAH_TABLE = "CREATE TABLE IF NOT EXISTS " + QURAN_NAME_ARBI + " (" +
            DbContract.MenuEntry.SURAH_ID + " INTEGER PRIMARY KEY   AUTOINCREMENT, " +
            DbContract.MenuEntry.SURAH_NUMBER + " TEXT NOT NULL, " +
            DbContract.MenuEntry.NAME + " TEXT NOT NULL, " +
            DbContract.MenuEntry.ENGLISH_NAME + " TEXT NOT NULL, " +
            DbContract.MenuEntry.NAME_TRANSLATION + " TEXT NOT NULL, " +
            DbContract.MenuEntry.TYPE + " TEXT NOT NULL );";

    final String AYAH_QUERY = "CREATE TABLE IF NOT EXISTS " + AYAH_DESCRIPTION_TABLE_ARABIC + " (" +
            DbContract.MenuEntry.AYYAH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DbContract.MenuEntry.AYYAH_NUMBER + " TEXT NOT NULL, " +
            DbContract.MenuEntry.TEXT + " TEXT NOT NULL, " +
            DbContract.MenuEntry.NUMBER_IN_SURAH + " TEXT NOT NULL, " +
            DbContract.MenuEntry.JUZZ + " TEXT NOT NULL, " +
            DbContract.MenuEntry.MANZIL + " TEXT NOT NULL, " +
            DbContract.MenuEntry.PAGE + " TEXT NOT NULL, " +
            DbContract.MenuEntry.RUKKU + " TEXT NOT NULL, " +
            DbContract.MenuEntry.HIZB_QUARTER + " TEXT NOT NULL, " +
            DbContract.MenuEntry.SAJJDAH + " TEXT NOT NULL, " +
            DbContract.MenuEntry.SURAH_NUMBER + " TEXT NOT NULL, " +
            DbContract.MenuEntry.ARABIC_TEXT_TYPE + " TEXT NOT NULL );";

    final String AYAH_QUERY_ENGLISH = "CREATE TABLE IF NOT EXISTS " + AYAH_DESCRIPTION_TABLE_ENGLISH + " (" +
            DbContract.MenuEntry.AYYAH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DbContract.MenuEntry.AYYAH_NUMBER + " TEXT NOT NULL, " +
            DbContract.MenuEntry.TEXT + " TEXT NOT NULL, " +
            DbContract.MenuEntry.NUMBER_IN_SURAH + " TEXT NOT NULL, " +
            DbContract.MenuEntry.JUZZ + " TEXT NOT NULL, " +
            DbContract.MenuEntry.MANZIL + " TEXT NOT NULL, " +
            DbContract.MenuEntry.PAGE + " TEXT NOT NULL, " +
            DbContract.MenuEntry.RUKKU + " TEXT NOT NULL, " +
            DbContract.MenuEntry.HIZB_QUARTER + " TEXT NOT NULL, " +
            DbContract.MenuEntry.SAJJDAH + " TEXT NOT NULL, " +
            DbContract.MenuEntry.SURAH_NUMBER + " TEXT NOT NULL, " +
            DbContract.MenuEntry.ARABIC_TEXT_TYPE + " TEXT NOT NULL );";

    final String DUA_STRING = "CREATE TABLE IF NOT EXISTS " + DUA + " (" +
            DbContract.MenuEntry.DUA_CAT_ID + " INTEGER, " +
            DbContract.MenuEntry.DUA_SUBCAT_ID + " TEXT NOT NULL, " +
            DbContract.MenuEntry.DUA_ID + " TEXT NOT NULL, " +
            DbContract.MenuEntry.DUA_CAT_NAME + " TEXT NOT NULL, " +
            DbContract.MenuEntry.DUA_SUBCAT_NAME + " TEXT NOT NULL, " +
            DbContract.MenuEntry.DUA_TEXT + " TEXT NOT NULL, " +
            DbContract.MenuEntry.POST_TITLE + " TEXT NOT NULL );";

    final String FAVOURITE_STRING = "CREATE TABLE IF NOT EXISTS " + FAVOURITE + " (" +
            DbContract.MenuEntry.FAVOURIE_ID + " TEXT, " +
            DbContract.MenuEntry.FAVOURIE_TEXT + " TEXT NOT NULL, " +
            DbContract.MenuEntry.FAVOURITE_TITLE + " TEXT NOT NULL );";


    public DbHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//
//        this.context = context;
//        mResources = context.getResources();
        super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(AYAH_QUERY);
        sqLiteDatabase.execSQL(AYAH_QUERY_ENGLISH);
        sqLiteDatabase.execSQL(SQL_CREATE_NAMAZ_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SURAH_TABLE);
        sqLiteDatabase.execSQL(DUA_STRING);
        sqLiteDatabase.execSQL(FAVOURITE_STRING);

        Log.d(TAG, "Database Created Successfully");

//        Toast.makeText(context, "Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.MenuEntry.TABLE_NAME);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.MenuEntry.QURAN_NAME_ARBI);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.MenuEntry.AYAH_DESCRIPTION_TABLE_ARABIC);

        if (i1 > i) {
            sqLiteDatabase.execSQL("ALTER TABLE DUA ADD COLUMN duatitle ");
            sqLiteDatabase.execSQL(FAVOURITE_STRING);

        }

        onCreate(sqLiteDatabase);

    }

    public Cursor returnnumRows(DbHelper dbHelper, String number) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                DbContract.MenuEntry.AYYAH_ID,
                DbContract.MenuEntry.AYYAH_NUMBER,
                DbContract.MenuEntry.TEXT,
                DbContract.MenuEntry.NUMBER_IN_SURAH,
                DbContract.MenuEntry.JUZZ,
                DbContract.MenuEntry.MANZIL,
                DbContract.MenuEntry.PAGE,
                DbContract.MenuEntry.RUKKU,
                DbContract.MenuEntry.HIZB_QUARTER,
                DbContract.MenuEntry.SAJJDAH,
                DbContract.MenuEntry.SURAH_NUMBER,
                DbContract.MenuEntry.ARABIC_TEXT_TYPE,

        };
        return db.query
                (
                        AYAH_DESCRIPTION_TABLE_ARABIC,
                        columns,
                        DbContract.MenuEntry.SURAH_NUMBER + "=" + number,
                        null, null, null, null, null
                );

    }

    public Cursor returnnumRowsEnglish(DbHelper dbHelper, String number) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                DbContract.MenuEntry.AYYAH_ID,
                DbContract.MenuEntry.AYYAH_NUMBER,
                DbContract.MenuEntry.TEXT,
                DbContract.MenuEntry.NUMBER_IN_SURAH,
                DbContract.MenuEntry.JUZZ,
                DbContract.MenuEntry.MANZIL,
                DbContract.MenuEntry.PAGE,
                DbContract.MenuEntry.RUKKU,
                DbContract.MenuEntry.HIZB_QUARTER,
                DbContract.MenuEntry.SAJJDAH,
                DbContract.MenuEntry.SURAH_NUMBER,
                DbContract.MenuEntry.ARABIC_TEXT_TYPE,

        };
        return db.query
                (
                        AYAH_DESCRIPTION_TABLE_ENGLISH,
                        columns,
                        DbContract.MenuEntry.SURAH_NUMBER + "=" + number,
                        null, null, null, null, null
                );

    }

    public boolean PutNamazTimeInformation(DbHelper dbHelper, JSONObject jsonObject, Context context) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            writePrayertimeToDb(db, jsonObject, context);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

        return true;
    }

    public boolean PutQuranInformation(DbHelper dbHelper, JSONObject jsonObject, Context context) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            writeQuranToDb(db, jsonObject, context);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

        return true;
    }

    public boolean PutAyyahInformation(DbHelper dbHelper, JSONObject jsonObject, Context context) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            writeAyyahsToDb(db, jsonObject, context);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return true;
    }

    public boolean PutAyyahInformationEnglish(DbHelper dbHelper, JSONObject jsonObject, Context context) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            writeAyyahsToDbEnglish(db, jsonObject, context);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

        return true;
    }

    private void writeAyyahsToDbEnglish(SQLiteDatabase db, JSONObject jsonObject, Context context) throws IOException, JSONException {
        final String NUMBER = "number";
        final String TEXT = "text";
        final String NUMBER_IN_SURAH = "numberInSurah";
        final String JUZZ = "juz";
        final String MANZIL = "manzil";
        final String PAGE = "page";
        final String RUKKU = "ruku";
        final String HIZABQUARTER = "hizbQuarter";
        final String SAJDA = "sajda";
        final String ARABIC_TEXT_TYPE = "QuranType";

        String surrahNumber;

        try {
            //hitting the api .. this time get object basic
            JSONObject dataobject = jsonObject.getJSONObject("data");

            //this time get arrays inside main object
            JSONArray surraharray = dataobject.getJSONArray("surahs");

            for (int i = 0; i < surraharray.length(); i++) {

                //turn by turn all objects inside array
                JSONObject surahobject = surraharray.getJSONObject(i);

                surrahNumber = surahobject.getString(NUMBER);

                //inside that object get array ...
                JSONArray ayyaharay = surahobject.getJSONArray("ayahs");

                for (int j = 0; j < ayyaharay.length(); j++) {

                    JSONObject ayatobject = ayyaharay.getJSONObject(j);

                    String number;
                    String text;
                    String number_in_surah;
                    String juzz;
                    String manzil;
                    String page;
                    String ruku;
                    String hizabquarter;
                    String sajda;
                    String arabic_text_type;

                    number = ayatobject.getString(NUMBER);
                    text = ayatobject.getString(TEXT);
                    number_in_surah = ayatobject.getString(NUMBER_IN_SURAH);
                    juzz = ayatobject.getString(JUZZ);
                    manzil = ayatobject.getString(MANZIL);
                    page = ayatobject.getString(PAGE);
                    ruku = ayatobject.getString(RUKKU);
                    hizabquarter = ayatobject.getString(HIZABQUARTER);
                    sajda = ayatobject.getString(SAJDA);
//                arabic_text_type = ayah_arabic_object.getString(ARABIC_TEXT_TYPE);

                    /*db.beginTransaction();

                    try {

                        ContentValues menuValues = new ContentValues();
                        menuValues.put(DbContract.MenuEntry.AYYAH_NUMBER, number);
                        menuValues.put(DbContract.MenuEntry.TEXT, text);
                        menuValues.put(DbContract.MenuEntry.NUMBER_IN_SURAH, number_in_surah);
                        menuValues.put(DbContract.MenuEntry.JUZZ, juzz);
                        menuValues.put(DbContract.MenuEntry.MANZIL, manzil);
                        menuValues.put(DbContract.MenuEntry.PAGE, page);
                        menuValues.put(DbContract.MenuEntry.RUKKU, ruku);
                        menuValues.put(DbContract.MenuEntry.HIZB_QUARTER, hizabquarter);
                        menuValues.put(DbContract.MenuEntry.SAJJDAH, sajda);
                        menuValues.put(DbContract.MenuEntry.SURAH_NUMBER, surrahNumber);
                        menuValues.put(DbContract.MenuEntry.ARABIC_TEXT_TYPE, "Uthmani");

                        db.insert(AYAH_DESCRIPTION_TABLE_ENGLISH, null, menuValues);
                        db.setTransactionSuccessful();

                    } finally {
                        db.endTransaction();
                    }*/

                    ListAyyat ayyatList = new ListAyyat(number, text, number_in_surah, juzz ,manzil, page, ruku, hizabquarter, sajda, surrahNumber);

                    listenglish.add(ayyatList);

                    if(i == 113 && j == 5) {

                        AddtoDataBaseEnglish(listenglish);

                    }


                }
            }
//            JSONObject firstsurah = surraharray.getJSONObject(0);
//            JSONArray ayyaharay = firstsurah.getJSONArray("ayahs");
//            JSONObject ayyaobj = ayyaharay.getJSONObject(0);
//            String ayat = ayyaobj.getString("text");
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void writeAyyahsToDb(SQLiteDatabase db, JSONObject jsonObject, Context context) throws IOException, JSONException {

        final String NUMBER = "number";
        final String TEXT = "text";
        final String NUMBER_IN_SURAH = "numberInSurah";
        final String JUZZ = "juz";
        final String MANZIL = "manzil";
        final String PAGE = "page";
        final String RUKKU = "ruku";
        final String HIZABQUARTER = "hizbQuarter";
        final String SAJDA = "sajda";
        final String ARABIC_TEXT_TYPE = "QuranType";

        String surrahNumber;

        try {
            //hitting the api .. this time get object basic
            JSONObject dataobject = jsonObject.getJSONObject("data");

            //this time get arrays inside main object
            JSONArray surraharray = dataobject.getJSONArray("surahs");

            for (int i = 0; i < surraharray.length(); i++) {

                //turn by turn all objects inside array
                JSONObject surahobject = surraharray.getJSONObject(i);

                surrahNumber = surahobject.getString(NUMBER);

                //inside that object get array ...
                JSONArray ayyaharay = surahobject.getJSONArray("ayahs");

                for (int j = 0; j < ayyaharay.length(); j++) {

                    JSONObject ayatobject = ayyaharay.getJSONObject(j);

                    String number;
                    String text;
                    String number_in_surah;
                    String juzz;
                    String manzil;
                    String page;
                    String ruku;
                    String hizabquarter;
                    String sajda;
                    String arabic_text_type;


                    number = ayatobject.getString(NUMBER);
                    text = ayatobject.getString(TEXT);
                    number_in_surah = ayatobject.getString(NUMBER_IN_SURAH);
                    juzz = ayatobject.getString(JUZZ);
                    manzil = ayatobject.getString(MANZIL);
                    page = ayatobject.getString(PAGE);
                    ruku = ayatobject.getString(RUKKU);
                    hizabquarter = ayatobject.getString(HIZABQUARTER);
                    sajda = ayatobject.getString(SAJDA);
//                arabic_text_type = ayah_arabic_object.getString(ARABIC_TEXT_TYPE);

                    ListAyyat ayyatList = new ListAyyat(number, text, number_in_surah, juzz ,manzil, page, ruku, hizabquarter, sajda, surrahNumber);

                    list.add(ayyatList);

                    if(i == 113 && j == 5) {

                        AddtoDataBase(list);

                    }
                }
            }
//            JSONObject firstsurah = surraharray.getJSONObject(0);
//            JSONArray ayyaharay = firstsurah.getJSONArray("ayahs");
//            JSONObject ayyaobj = ayyaharay.getJSONObject(0);
//            String ayat = ayyaobj.getString("text");
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void AddtoDataBase(ArrayList<ListAyyat> list) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues menuValues = new ContentValues();
            for (ListAyyat listAyyat : list) {

                Log.e("ayyattest", listAyyat.getAyah_text());

                menuValues.put(DbContract.MenuEntry.AYYAH_NUMBER, listAyyat.getAyah_number());
                menuValues.put(DbContract.MenuEntry.TEXT, listAyyat.getAyah_text());
                menuValues.put(DbContract.MenuEntry.NUMBER_IN_SURAH, listAyyat.getAyah_no());
                menuValues.put(DbContract.MenuEntry.JUZZ, listAyyat.getAyah_juzz());
                menuValues.put(DbContract.MenuEntry.MANZIL, listAyyat.getAyah_manzil());
                menuValues.put(DbContract.MenuEntry.PAGE, listAyyat.getQuran_page());
                menuValues.put(DbContract.MenuEntry.RUKKU, listAyyat.getRuku_number());
                menuValues.put(DbContract.MenuEntry.HIZB_QUARTER, listAyyat.getHizzb());
                menuValues.put(DbContract.MenuEntry.SAJJDAH, listAyyat.getSajdah());
                menuValues.put(DbContract.MenuEntry.SURAH_NUMBER, listAyyat.getSurrahNumber());
                menuValues.put(DbContract.MenuEntry.ARABIC_TEXT_TYPE, "Uthmani");

                db.insert(AYAH_DESCRIPTION_TABLE_ARABIC, null, menuValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void AddtoDataBaseEnglish(ArrayList<ListAyyat> list) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues menuValues = new ContentValues();
            for (ListAyyat listAyyat : list) {

                Log.e("ayyattest", listAyyat.getAyah_text());

                menuValues.put(DbContract.MenuEntry.AYYAH_NUMBER, listAyyat.getAyah_number());
                menuValues.put(DbContract.MenuEntry.TEXT, listAyyat.getAyah_text());
                menuValues.put(DbContract.MenuEntry.NUMBER_IN_SURAH, listAyyat.getAyah_no());
                menuValues.put(DbContract.MenuEntry.JUZZ, listAyyat.getAyah_juzz());
                menuValues.put(DbContract.MenuEntry.MANZIL, listAyyat.getAyah_manzil());
                menuValues.put(DbContract.MenuEntry.PAGE, listAyyat.getQuran_page());
                menuValues.put(DbContract.MenuEntry.RUKKU, listAyyat.getRuku_number());
                menuValues.put(DbContract.MenuEntry.HIZB_QUARTER, listAyyat.getHizzb());
                menuValues.put(DbContract.MenuEntry.SAJJDAH, listAyyat.getSajdah());
                menuValues.put(DbContract.MenuEntry.SURAH_NUMBER, listAyyat.getSurrahNumber());
                menuValues.put(DbContract.MenuEntry.ARABIC_TEXT_TYPE, "Uthmani");

                db.insert(AYAH_DESCRIPTION_TABLE_ENGLISH, null, menuValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void writeQuranToDb(SQLiteDatabase db, JSONObject jsonObject, Context context) throws IOException, JSONException {

        final String NUMBER = "number";
        final String NAME = "name";
        final String ENGLISH_NAME = "englishName";
        final String TRANSLATION_ENGLISH = "englishNameTranslation";
        final String REV_TYPE = "revelationType";

        try {

            JSONObject dataobject = jsonObject.getJSONObject("data");
            JSONArray surraharray = dataobject.getJSONArray("surahs");

            for (int i = 0; i < surraharray.length(); i++) {
                JSONObject surahOBject = surraharray.getJSONObject(i);

                String number;
                String name;
                String english_name;
                String translation_english;
                String revelation_type;

                number = surahOBject.getString(NUMBER);
                name = surahOBject.getString(NAME);
                english_name = surahOBject.getString(ENGLISH_NAME);
                translation_english = surahOBject.getString(TRANSLATION_ENGLISH);
                revelation_type = surahOBject.getString(REV_TYPE);

                ContentValues menuValues = new ContentValues();
                menuValues.put(DbContract.MenuEntry.SURAH_NUMBER, number);
                menuValues.put(DbContract.MenuEntry.NAME, name);
                menuValues.put(DbContract.MenuEntry.ENGLISH_NAME, english_name);
                menuValues.put(DbContract.MenuEntry.NAME_TRANSLATION, translation_english);
                menuValues.put(DbContract.MenuEntry.TYPE, revelation_type);

                db.insert(QURAN_NAME_ARBI, null, menuValues);

                /*JSONArray ayyaharay = surahOBject.getJSONArray("ayahs");

                for (int j = 0; j < ayyaharay.length();j++){

                    String number;
                    String name;
                    String english_name;
                    String translation_english;
                    String revelation_type;


                    JSONObject ayyaobj = ayyaharay.getJSONObject(j);
                    number = ayyaobj.getString(NUMBER);
                    name = ayyaobj.getString(NAME);
                    english_name = ayyaobj.getString(ENGLISH_NAME);
                    translation_english = ayyaobj.getString(TRANSLATION_ENGLISH);
                    revelation_type = ayyaobj.getString(REV_TYPE);

                    ContentValues menuValues = new ContentValues();
                    menuValues.put(DbQuranArbiContract.MenuEntry.NUMBER,number);
                    menuValues.put(DbQuranArbiContract.MenuEntry.NAME,name);
                    menuValues.put(DbQuranArbiContract.MenuEntry.ENGLISH_NAME,english_name);
                    menuValues.put(DbQuranArbiContract.MenuEntry.NAME_TRANSLATION,translation_english);
                    menuValues.put(DbQuranArbiContract.MenuEntry.TYPE,revelation_type);

                    db.insert(TABLE_NAME, null, menuValues);
                }*/
            }
//            JSONObject firstsurah = surraharray.getJSONObject(0);
//            JSONArray ayyaharay = firstsurah.getJSONArray("ayahs");
//            JSONObject ayyaobj = ayyaharay.getJSONObject(0);
//            String ayat = ayyaobj.getString("text");
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void writePrayertimeToDb(SQLiteDatabase db, JSONObject jsonObject, Context context) throws IOException, JSONException {
        final String MNU_FAJR = "Fajr";
        final String MNU_DHUHR = "Dhuhr";
        final String MNU_ASR = "Asr";
        final String MNU_MAGHRIB = "Maghrib";
        final String MNU_ISHA = "Isha";
        final String MNU_SUNRISE = "Sunrise";
        final String MNU_IMSAK = "Imsak";

        try {
            JSONArray jArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jArray.length(); i++) {
                String fajr;
                String dhuhr;
                String asr;
                String maghrib;
                String isha;
                String sunrise;
                String imsak;

                String date;

                JSONObject json_data = jArray.getJSONObject(i);

                JSONObject timings = json_data.getJSONObject("timings");

                JSONObject dateobject = json_data.getJSONObject("date");

                JSONObject formattedDate = dateobject.getJSONObject("gregorian");

                date = formattedDate.getString("date");

                fajr = timings.getString(MNU_FAJR);
                dhuhr = timings.getString(MNU_DHUHR);
                asr = timings.getString(MNU_ASR);
                maghrib = timings.getString(MNU_MAGHRIB);
                isha = timings.getString(MNU_ISHA);
                sunrise = timings.getString(MNU_SUNRISE);
                imsak = timings.getString(MNU_IMSAK);

                fajr = fajr.substring(0, 5);
                dhuhr = dhuhr.substring(0, 5);
                asr = asr.substring(0, 5);
                maghrib = maghrib.substring(0, 5);
                isha = isha.substring(0, 5);
                sunrise = sunrise.substring(0, 5);
                imsak = imsak.substring(0, 5);

//                fajr = fajr.replace(" (PST)","");
//                dhuhr = dhuhr.replace(" (PST)","");
//                asr = asr.replace(" (PST)","");
//                maghrib = maghrib.replace(" (PST)","");
//                isha = isha.replace(" (PST)","");
//                sunrise = sunrise.replace(" (PST)","");
//                imsak = imsak.replace(" (PST)","");

                ContentValues menuValues = new ContentValues();

                menuValues.put(DbContract.MenuEntry.DATE, date);
                menuValues.put(DbContract.MenuEntry.COLUMN_FAJR, fajr);
                menuValues.put(DbContract.MenuEntry.COLUMN_DHUHR, dhuhr);
                menuValues.put(DbContract.MenuEntry.COLUMN_ASR, asr);
                menuValues.put(DbContract.MenuEntry.COLUMN_MAGHRIB, maghrib);
                menuValues.put(DbContract.MenuEntry.COLUMN_ISHA, isha);
                menuValues.put(DbContract.MenuEntry.COLUMN_SUNRISE, sunrise);
                menuValues.put(DbContract.MenuEntry.COLUMN_IMSAK, imsak);

                db.insert(TABLE_NAME, null, menuValues);

//                Toast.makeText(context, "Inserted", Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteDataFromTable(DbHelper dbHelper, String tablename) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + tablename);

    }

    public boolean PutDuaInformation(DbHelper dbHelper, JSONArray jsonArray, Context context) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            writeDuasToDb(db, jsonArray, context);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

        return true;
    }

    private void writeDuasToDb(SQLiteDatabase db, JSONArray jsonArray, Context context) throws IOException, JSONException {

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject categoryObj = jsonArray.getJSONObject(i);

            String category_id = categoryObj.getString("category_id");
            String category_name = categoryObj.getString("category_name");

            JSONArray subCat_array = categoryObj.getJSONArray("subcategories");

            for (int j = 0; j < subCat_array.length(); j++) {

                JSONObject subCatObject = subCat_array.getJSONObject(j);

                String subcat_id = subCatObject.getString("subcategory_id");
                String subcat_name = subCatObject.getString("subcategory_name");

                JSONArray allPostsArray = subCatObject.getJSONArray("all_posts");

                for (int k = 0; k < allPostsArray.length(); k++) {

                    JSONObject allpostsObject = allPostsArray.getJSONObject(k);

                    String postcontent = allpostsObject.getString("post_content");
                    String Dua_id = allpostsObject.getString("ID");
                    String dua_title = allpostsObject.getString("post_title");

                    ContentValues menuValues = new ContentValues();

                    menuValues.put(DbContract.MenuEntry.DUA_CAT_ID, category_id);
                    menuValues.put(DbContract.MenuEntry.DUA_CAT_NAME, category_name);
                    menuValues.put(DbContract.MenuEntry.DUA_SUBCAT_ID, subcat_id);
                    menuValues.put(DbContract.MenuEntry.DUA_SUBCAT_NAME, subcat_name);
                    menuValues.put(DbContract.MenuEntry.DUA_TEXT, (postcontent));
                    menuValues.put(DbContract.MenuEntry.DUA_ID, Dua_id);
                    menuValues.put(DbContract.MenuEntry.POST_TITLE, dua_title);

                    Log.d("duatitle", dua_title);
                    Log.e("duatitle", dua_title);
                    Log.v("duatitle", dua_title);
                    Log.i("duatitle", dua_title);

                    db.insert(DUA, null, menuValues);

                }

            }

        }

    }

    public Cursor getDuasInformation(DbHelper duaDbhelper) {
        SQLiteDatabase db2 = duaDbhelper.getReadableDatabase();

        String[] columns = {
                DbContract.MenuEntry.DUA_CAT_ID,
                DbContract.MenuEntry.DUA_SUBCAT_ID,
                DbContract.MenuEntry.DUA_ID,
                DbContract.MenuEntry.DUA_CAT_NAME,
                DbContract.MenuEntry.DUA_SUBCAT_NAME,
                DbContract.MenuEntry.DUA_TEXT,
                DbContract.MenuEntry.POST_TITLE,
        };
        /*SQLiteDatabase database = dbHelper.getReadableDatabase();*/

        Cursor cursor = db2.query(DUA, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor getAyyahsInformation(DbHelper ayahDbHelperArbi) {
        SQLiteDatabase db2 = ayahDbHelperArbi.getReadableDatabase();

        String[] columns = {
                DbContract.MenuEntry.AYYAH_ID,
                DbContract.MenuEntry.AYYAH_NUMBER,
                DbContract.MenuEntry.TEXT,
                DbContract.MenuEntry.NUMBER_IN_SURAH,
                DbContract.MenuEntry.JUZZ,
                DbContract.MenuEntry.MANZIL,
                DbContract.MenuEntry.PAGE,
                DbContract.MenuEntry.RUKKU,
                DbContract.MenuEntry.HIZB_QUARTER,
                DbContract.MenuEntry.SAJJDAH,
                DbContract.MenuEntry.SURAH_NUMBER,
                DbContract.MenuEntry.ARABIC_TEXT_TYPE,

        };
        /*SQLiteDatabase database = dbHelper.getReadableDatabase();*/


        Cursor cursor = db2.query(AYAH_DESCRIPTION_TABLE_ARABIC, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor getAyyahsInformationEnglish(DbHelper ayahDbHelperArbi) {
        SQLiteDatabase db2 = ayahDbHelperArbi.getReadableDatabase();

        String[] columns = {
                DbContract.MenuEntry.AYYAH_ID,
                DbContract.MenuEntry.AYYAH_NUMBER,
                DbContract.MenuEntry.TEXT,
                DbContract.MenuEntry.NUMBER_IN_SURAH,
                DbContract.MenuEntry.JUZZ,
                DbContract.MenuEntry.MANZIL,
                DbContract.MenuEntry.PAGE,
                DbContract.MenuEntry.RUKKU,
                DbContract.MenuEntry.HIZB_QUARTER,
                DbContract.MenuEntry.SAJJDAH,
                DbContract.MenuEntry.SURAH_NUMBER,
                DbContract.MenuEntry.ARABIC_TEXT_TYPE,

        };
        /*SQLiteDatabase database = dbHelper.getReadableDatabase();*/


        Cursor cursor = db2.query(AYAH_DESCRIPTION_TABLE_ENGLISH, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor getQuranInformation(DbHelper quranDBHelperArbi) {

        SQLiteDatabase database = quranDBHelperArbi.getReadableDatabase();


        String[] columns = {
                DbContract.MenuEntry.SURAH_ID,
                DbContract.MenuEntry.SURAH_NUMBER,
                DbContract.MenuEntry.NAME,
                DbContract.MenuEntry.ENGLISH_NAME,
                DbContract.MenuEntry.NAME_TRANSLATION,
                DbContract.MenuEntry.TYPE,

        };
        /*SQLiteDatabase database = dbHelper.getReadableDatabase();*/


        Cursor cursor = database.query(QURAN_NAME_ARBI, columns, null, null, null, null, null);

        return cursor;
    }

    public Cursor getNamazTimeInformation(DbHelper dbHelper) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] columns = {
                DbContract.MenuEntry.DATE,
                DbContract.MenuEntry.COLUMN_FAJR,
                DbContract.MenuEntry.COLUMN_DHUHR,
                DbContract.MenuEntry.COLUMN_ASR,
                DbContract.MenuEntry.COLUMN_MAGHRIB,
                DbContract.MenuEntry.COLUMN_ISHA,
                DbContract.MenuEntry.COLUMN_SUNRISE,
                DbContract.MenuEntry.COLUMN_IMSAK,
        };

        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);

        return cursor;
    }

    public Cursor getDuaInformation(DbHelper dbHelper) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] columns = {
                DbContract.MenuEntry.DUA_CAT_ID,
                DbContract.MenuEntry.DUA_CAT_NAME,
                DbContract.MenuEntry.DUA_SUBCAT_ID,
                DbContract.MenuEntry.DUA_SUBCAT_NAME,
                DbContract.MenuEntry.DUA_ID,
                DbContract.MenuEntry.DUA_TEXT,
                DbContract.MenuEntry.POST_TITLE,
        };

        Cursor cursor = database.query(DUA, columns, null, null, DbContract.MenuEntry.DUA_CAT_ID, null, null);

        return cursor;
    }

    public Cursor getCatInformation(DbHelper dbHelper, String number) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] columns = {
                DbContract.MenuEntry.DUA_CAT_ID,
                DbContract.MenuEntry.DUA_CAT_NAME,
                DbContract.MenuEntry.DUA_SUBCAT_ID,
                DbContract.MenuEntry.DUA_SUBCAT_NAME,
                DbContract.MenuEntry.DUA_ID,
                DbContract.MenuEntry.DUA_TEXT,
                DbContract.MenuEntry.POST_TITLE,
        };

        Cursor cursor = database.query(DUA, columns, DbContract.MenuEntry.DUA_CAT_ID + "=" + number, null, DbContract.MenuEntry.DUA_SUBCAT_ID, null, null);

        return cursor;
    }

    public Cursor getsubCatInformation(DbHelper dbHelper, String number) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] columns = {
                DbContract.MenuEntry.DUA_CAT_ID,
                DbContract.MenuEntry.DUA_CAT_NAME,
                DbContract.MenuEntry.DUA_SUBCAT_ID,
                DbContract.MenuEntry.DUA_SUBCAT_NAME,
                DbContract.MenuEntry.DUA_ID,
                DbContract.MenuEntry.DUA_TEXT,
                DbContract.MenuEntry.POST_TITLE,
        };

        Cursor cursor = database.query(DUA, columns, DbContract.MenuEntry.DUA_SUBCAT_ID + "=" + number, null, null, null, null);

        return cursor;
    }

    public Cursor getpostInformation(DbHelper dbHelper, String number) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] columns = {
                DbContract.MenuEntry.DUA_CAT_ID,
                DbContract.MenuEntry.DUA_CAT_NAME,
                DbContract.MenuEntry.DUA_SUBCAT_ID,
                DbContract.MenuEntry.DUA_SUBCAT_NAME,
                DbContract.MenuEntry.DUA_ID,
                DbContract.MenuEntry.DUA_TEXT,
                DbContract.MenuEntry.POST_TITLE,
        };

        Cursor cursor = database.query(DUA, columns, DbContract.MenuEntry.DUA_ID + "=" + number, null, null, null, null);

        return cursor;
    }

    public Cursor getfvrtList(DbHelper dbHelper) {
//        SQLiteDatabase database = dbHelper.getReadableDatabase();
//
//        String[] columns = {
//                DbContract.MenuEntry.DUA_CAT_ID,
//                DbContract.MenuEntry.DUA_CAT_NAME,
//                DbContract.MenuEntry.DUA_SUBCAT_ID,
//                DbContract.MenuEntry.DUA_SUBCAT_NAME,
//                DbContract.MenuEntry.DUA_ID,
//                DbContract.MenuEntry.DUA_TEXT,
//                DbContract.MenuEntry.POST_TITLE,
//                DbContract.MenuEntry.FAVOURITE_STATUS
//        };
//
//        Cursor cursor = database.query(DUA, columns, DbContract.MenuEntry.FAVOURITE_STATUS + "=" + check, null, null, null, null);

        String query = "select * from " + FAVOURITE ;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void deleteDuaFromTable(DbHelper dbHelper, String tablename) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + tablename);

    }

    public void savedua( String duaid, String fvrtTitle, String fvrtText, DbHelper dbHelper)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.MenuEntry.FAVOURIE_ID, duaid);
        contentValues.put(DbContract.MenuEntry.FAVOURIE_TEXT, fvrtText);
        contentValues.put(DbContract.MenuEntry.FAVOURITE_TITLE, fvrtTitle);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.insert(FAVOURITE, null, contentValues);

       /* String sql = "UPDATE "+DUA +" SET " + DbContract.MenuEntry.FAVOURITE_STATUS+ " = " + newValue + " WHERE "+DbContract.MenuEntry.DUA_ID+ " = "+duaid;
        db.execSQL(sql);*/
    }

    public void Unsavedua( String duaid, DbHelper dbHelper)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "delete from " + FAVOURITE + " where " + DbContract.MenuEntry.FAVOURIE_ID +" = " + duaid;
        db.execSQL(query);
    }

    public Boolean getduastatus(String suCatID, DbHelper dbHelper)
    {

        String query = "select * from " + FAVOURITE + " where "+ DbContract.MenuEntry.FAVOURIE_ID + " = " + suCatID;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToNext();

        if(cursor.getCount() > 0)
        {
            return true;
        }
        else {
            return false;
        }

    }

}
