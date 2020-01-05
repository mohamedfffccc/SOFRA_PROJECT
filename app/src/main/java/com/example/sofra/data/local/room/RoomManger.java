package com.example.sofra.data.local.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {OrderItem.class}, version = 4, exportSchema = false)


public abstract class RoomManger extends RoomDatabase {
    public abstract RoomDao roomDao();

    public static RoomManger roomManger;

    public static synchronized RoomManger getInistance(Context context) {
        if (roomManger == null) {
            roomManger = Room.databaseBuilder(context.getApplicationContext(), RoomManger.class,
                    "sofradatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return roomManger;
    }
}
