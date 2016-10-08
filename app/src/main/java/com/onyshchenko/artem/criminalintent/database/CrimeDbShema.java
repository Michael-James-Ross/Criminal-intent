package com.onyshchenko.artem.criminalintent.database;

public class CrimeDbShema {
    public static final class CrimeTable {
        public static final String NAME = "Crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}
