package com.namobiletech.imamiajantri.Utils.dbUtility;

import android.provider.BaseColumns;

public class DbContract {

    public static final class MenuEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "NamazTimings";
        public static final String DATE = "date";
        public static final String COLUMN_FAJR = "fajr";
        public static final String COLUMN_DHUHR = "dhuhr";
        public static final String COLUMN_ASR = "asr";
        public static final String COLUMN_MAGHRIB = "maghrib";
        public static final String COLUMN_ISHA = "isha";
        public static final String COLUMN_SUNRISE = "sunrise";
        public static final String COLUMN_IMSAK = "imsak";

        public static final String QURAN_NAME_ARBI = "QURAN_ARBI";
        public static final String SURAH_ID = "id";
        public static final String SURAH_NUMBER = "surah_number";
        public static final String NAME = "surah_name";
        public static final String ENGLISH_NAME = "english_name";
        public static final String NAME_TRANSLATION= "eng_translation";
        public static final String TYPE = "revel_type";

        public static final String AYAH_DESCRIPTION_TABLE_ARABIC = "AYYAH_Arabic";
        public static final String AYAH_DESCRIPTION_TABLE_ENGLISH = "AYYAH_english";
        public static final String AYYAH_ID = "id";
        public static final String AYYAH_NUMBER = "ayah_number";
        public static final String TEXT = "ayah_text";
        public static final String NUMBER_IN_SURAH = "ayah_no";
        public static final String JUZZ= "ayah_juzz";
        public static final String MANZIL = "ayah_manzil";
        public static final String PAGE = "quran_page";
        public static final String RUKKU = "ruku_number";
        public static final String HIZB_QUARTER = "hizzb";
        public static final String SAJJDAH = "sajdah";
        public static final String ARABIC_TEXT_TYPE = "ayah_type";

        public static final String DUA = "DUA";
        public static final String DUA_CAT_ID = "dua_car_id";
        public static final String DUA_SUBCAT_ID = "dua_subcat_id";
        public static final String DUA_ID = "dua_id";
        public static final String POST_TITLE = "duatitle";
        public static final String DUA_CAT_NAME = "dua_catname";
        public static final String DUA_SUBCAT_NAME = "dua_subcat_name";
        public static final String DUA_TEXT = "dua_text";


        public static final String FAVOURITE = "Favourite";
        public static final String FAVOURIE_TEXT = "favourite_text";
        public static final String FAVOURIE_ID = "favourite_id";
        public static final String FAVOURITE_TITLE = "favourite_title";
//        public static final String FAVOURITE_STATUS = "favouriteStatus";

    }
}
