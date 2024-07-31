package com.ghostdetctor.ghost_detector.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ghostdetctor.ghost_detector.ui.ghost.model.Ghost;
import java.util.List;

@Dao
public interface GhostDAO {
   @Query("select * from ghost")
    List<Ghost> getAllGhost();

    @Query("SELECT * FROM ghost WHERE id == (:ghostId)")
    Ghost findByIds(int ghostId);

    @Query("SELECT * FROM ghost WHERE isHorror == :isHorror AND isCaptured == :isCaptured ")
    List<Ghost> findByType(boolean isHorror,boolean isCaptured);

    @Query("SELECT * FROM ghost WHERE isCaptured == :isCaptured ")
    List<Ghost> getCollection(boolean isCaptured);

    @Query("DELETE FROM ghost")
    void deleteAllGhost();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Ghost... ghosts);

    @Update
    void update(Ghost ghost);

    @Delete
    void delete(Ghost ghost);
}
