package pollub.ism.lab_08;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.Calendar;

import pollub.ism.lab_08.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayAdapter<CharSequence> adapter;

    private String wybraneWarzywoNazwa = null;
    private Integer wybraneWarzywoIlosc = null;

    private int counter;
    private String result = "";

    public enum OperacjaMagazynowa {SKLADUJ, WYDAJ};

    private BazaMagazynowa bazaDanych;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter = ArrayAdapter.createFromResource(this, R.array.Asortyment, android.R.layout.simple_dropdown_item_1line);
        binding.spinner.setAdapter(adapter);
        binding.Changes.setEnabled(false);
        binding.Changes.setTextColor(Color.BLACK);
        binding.przyciskSkladuj.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                zmienStan(OperacjaMagazynowa.SKLADUJ);
            }
        });
        binding.przyciskWydaj.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                zmienStan(OperacjaMagazynowa.WYDAJ);
            }
        });
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                wybraneWarzywoNazwa = adapter.getItem(i).toString();
                aktualizuj();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Nie będziemy implementować, ale musi być
            }
        });
        bazaDanych = Room.databaseBuilder(getApplicationContext(), BazaMagazynowa.class, BazaMagazynowa.NAZWA_BAZY)
                .allowMainThreadQueries().build();

        if(bazaDanych.pozycjaMagazynowaDAO().size() == 0)
        {
            String[] asortyment = getResources().getStringArray(R.array.Asortyment);
            for(String nazwa : asortyment)
            {
                PozycjaMagazynowa pozycjaMagazynowa = new PozycjaMagazynowa();
                pozycjaMagazynowa.NAME = nazwa; pozycjaMagazynowa.QUANTITY = 0;
                bazaDanych.pozycjaMagazynowaDAO().insert(pozycjaMagazynowa);
            }
        }
    }
    private void aktualizuj()
    {
        wybraneWarzywoIlosc = bazaDanych.pozycjaMagazynowaDAO().findQuantityByName(wybraneWarzywoNazwa);
        binding.tekstStanMagazynu.setText("Stan magazynu dla " + wybraneWarzywoNazwa + " wynosi: " + wybraneWarzywoIlosc);

        result = "";
        counter = bazaDanych.pozycjaMagazynowaDAO().size(wybraneWarzywoNazwa);
        Tabela_2[] object = new Tabela_2[counter];
        object = bazaDanych.pozycjaMagazynowaDAO().show(wybraneWarzywoNazwa);

        for (int i=0; i<counter; i++)
        {
            result += (object[i].data + " " + object[i].stara + " -> " + object[i].nowa + "\n");
        }
        binding.Changes.setText(result);
        if (counter==0)
            binding.tekstJednostka.setText("");
        else
            binding.tekstJednostka.setText(object[counter-1].data + " " + object[counter-1].stara + " -> " + object[counter-1].nowa + "\n");
    }
    private void zmienStan(OperacjaMagazynowa operacja){
        Integer zmianaIlosci = null, nowaIlosc = null;
        try
        {
            zmianaIlosci = Integer.parseInt(binding.edycjaIlosc.getText().toString());
        }
        catch(NumberFormatException ex){
            return;
        }
        finally
        {
            binding.edycjaIlosc.setText("");
        }
        if (zmianaIlosci != 0)
        {
            switch (operacja)
            {
                case SKLADUJ:
                    nowaIlosc = wybraneWarzywoIlosc + zmianaIlosci;
                    break;
                case WYDAJ:
                    nowaIlosc = wybraneWarzywoIlosc - zmianaIlosci;
                    break;
            }
            Tabela_2 Tabela_2 = new Tabela_2();
            Tabela_2.data = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            Tabela_2.stara = wybraneWarzywoIlosc;
            Tabela_2.nowa = nowaIlosc;
            Tabela_2.name = wybraneWarzywoNazwa;
            bazaDanych.pozycjaMagazynowaDAO().insert(Tabela_2);
            bazaDanych.pozycjaMagazynowaDAO().updateQuantityByName(wybraneWarzywoNazwa, nowaIlosc);
        }
        aktualizuj();
    }
}