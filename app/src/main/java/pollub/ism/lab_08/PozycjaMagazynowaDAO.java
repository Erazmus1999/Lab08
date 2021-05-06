package pollub.ism.lab_08;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PozycjaMagazynowaDAO
{
    @Insert
    public void insert(PozycjaMagazynowa pozycja);
    @Insert
    public void insert(Tabela_2 pozycja);
    @Update
    void update(PozycjaMagazynowa pozycja);
    @Query("SELECT QUANTITY FROM Warzywniak WHERE NAME= :wybraneWarzywoNazwa")
    int findQuantityByName(String wybraneWarzywoNazwa);
    @Query("UPDATE Warzywniak SET QUANTITY = :wybraneWarzywoNowaIlosc WHERE NAME= :wybraneWarzywoNazwa")
    void updateQuantityByName(String wybraneWarzywoNazwa, int wybraneWarzywoNowaIlosc);
    @Query("SELECT COUNT(*) FROM Warzywniak")
    int size();
    @Query("SELECT * FROM Tabela_2 WHERE name= :wybraneWarzywoNazwa")
    Tabela_2[] show(String wybraneWarzywoNazwa);
    @Query("SELECT COUNT(*) FROM Tabela_2 WHERE name= :wybraneWarzywoNazwa")
    int size(String wybraneWarzywoNazwa);
}