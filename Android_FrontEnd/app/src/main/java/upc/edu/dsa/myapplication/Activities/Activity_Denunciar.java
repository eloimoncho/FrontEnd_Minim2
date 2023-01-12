package upc.edu.dsa.myapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import upc.edu.dsa.myapplication.Entities.Denuncia;
import upc.edu.dsa.myapplication.PouServices;
import upc.edu.dsa.myapplication.R;

public class Activity_Denunciar extends AppCompatActivity {

    TextView txt_nombre, txt_descripcion, txt_fecha;
    TextInputEditText inputNombre, inputDescripcion, inputFecha;
    Button btn_enviar;

    PouServices pouServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.denuncia_minim2_screen);

        inputFecha = findViewById(R.id.inputFecha);
        inputNombre = findViewById(R.id.inputNombre);
        inputDescripcion = findViewById(R.id.inputDescripcion);
        btn_enviar = (Button) findViewById(R.id.btn_enviar);

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateString = inputFecha.getText().toString();
                String informerString = inputNombre.getText().toString();
                String messageString = inputDescripcion.getText().toString();

                //per guardar nom per futures denuncies
                SharedPreferences sharedPreferences = getSharedPreferences("pouName", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Pou", informerString);
                editor.commit();

                Denuncia ref = new Denuncia (dateString, informerString, messageString);
                Call<Denuncia> call = pouServices.denuncia(ref);

                call.enqueue(new Callback<Denuncia>() {
                    @Override
                    public void onResponse(Call<Denuncia> call, Response<Denuncia> response) {
                        Log.i("Denuncia de abuso: " ,""+ ref.toString());
                        if (response.isSuccessful()) {
                            Log.d("Denuncia", "Denuncia completada");
                            Toast.makeText(getApplicationContext(), "Denuncia creada amb exit", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Activity_Pou_Salon.class);
                        }
                    }

                    @Override
                    public void onFailure(Call<Denuncia> call, Throwable t) {
                        Log.d("Denuncia", "Denuncia sense exit");
                        Toast.makeText(getApplicationContext(), "Denuncia sense exit", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Activity_Pou_Salon.class);
                    }
                });

            }
        });

    }

}
