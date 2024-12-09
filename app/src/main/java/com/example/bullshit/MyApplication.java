package com.example.bullshit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class MyApplication extends Application {
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "app-database")
                .fallbackToDestructiveMigration()
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        new Thread(() -> {
                            AppDatabase appDatabase = getDatabase();

                            // Add predefined admin user
                            appDatabase.userDAO().insertUser(new User("admin2", "admin2", true));

                            // Add predefined non-admin users
                            appDatabase.userDAO().insertUser(new User("testuser1", "testuser1", false));
                            appDatabase.userDAO().insertUser(new User("testuser2", "testuser2", false));
                            appDatabase.userDAO().insertUser(new User("testuser3", "testuser3", false));
                            appDatabase.userDAO().insertUser(new User("testuser4", "testuser4", false));
                            appDatabase.userDAO().insertUser(new User("testuser5", "testuser5", false));
                        }).start();
                    }
                })
                .build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
