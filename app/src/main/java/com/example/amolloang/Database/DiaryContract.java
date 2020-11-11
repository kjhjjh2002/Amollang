package com.example.amolloang.Database;

import android.provider.BaseColumns;

public final class DiaryContract {

    private DiaryContract(){

    }

    // 베이스 컬럼명 설정
    public static class DiaryEntry implements BaseColumns{
        public static final String TABLE_NAME = "diary";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENTS = "contents";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_MOOD = "mood";
    }
}
