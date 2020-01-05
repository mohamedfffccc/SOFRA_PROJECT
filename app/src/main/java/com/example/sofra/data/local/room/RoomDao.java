package com.example.sofra.data.local.room;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomDao {
    @Insert
    void addItem(OrderItem... item);

    @Delete
    void removeItem(OrderItem... item);

    @Query("select * from OrderItem")
    List<OrderItem> getAll();

    @Query("delete  from OrderItem")
    void delAll();

    @Update
    void update(OrderItem... item);
}
