package pollub.ism.lab_08;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Tabela_2")
public class Tabela_2
{
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String data;
    public int stara;
    public String name;
    public int nowa;
}

