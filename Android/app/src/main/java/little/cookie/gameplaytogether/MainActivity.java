package little.cookie.gameplaytogether;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    private ArrayList<String> items;
    private ArrayList<String> uris;

    private Spinner spinner;
    private EditText et_link;
    private TextView tw_name;
    private ActivityResultLauncher<Intent>  launcher;

    private boolean isStarted = false;

    private Class<?> gamepadClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSharedPreferences();
        setOnClick();
        initStuff();
    }

    private void initStuff() {

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();

                            int takeFlags = result.getData().getFlags();
                            takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            getContentResolver().takePersistableUriPermission(uri, takeFlags);

                            String name = getDataFromUri(uri);
                            updateAdapter(name, uri);


                        }
                    }
                });

    }

    private void loadSharedPreferences() {
        prefs = getSharedPreferences("gamepads", MODE_PRIVATE);
        items = new ArrayList<>();
        uris = new ArrayList<>();
        int amount = prefs.getInt("amount", 0);
        for (int i = 0; i < amount; i++) {
            items.add(prefs.getString("item" + i, "error"));
            uris.add(prefs.getString("uri" + i, "error"));
        }

        et_link = findViewById(R.id.server);
        et_link.setText(prefs.getString("link", ""));
    }

    private void setOnClick() {

        findViewById(R.id.button).setOnClickListener(view ->
        {
            prefs.edit().putString("link", et_link.getText().toString()).apply();
            loadClass();
            startConnection();
            startIntent();
        });


        //String[] arr_items = (String[]) ims.toArray();

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(uris.size()>0) {
                    Log.d("dataforme",uris.get(i));
                    getDataFromUri(Uri.parse(uris.get(i)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.add).setOnClickListener(view -> selectZip());


    }

    private void updateAdapter(String name, Uri uri) {

        String path = uri.toString();



        if(!items.contains(path)) {
            items.add(name);
            uris.add(path);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(items.indexOf(uri.getPath()));

            prefs = getSharedPreferences("gamepads", MODE_PRIVATE);
            int size = items.size();
            prefs.edit().putString("item" + (size - 1), name).apply();
            prefs.edit().putString("uri" + (size - 1), path).apply();
            prefs.edit().putInt("amount", size).apply();

        }
    }

    private void selectZip() {


        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/zip");
        launcher.launch(intent);


    }

    private void loadClass(ZipEntry entry, ZipInputStream zipInputStream)
    {
        try {
            File tempFile = File.createTempFile("temp_class", ".dex", getCacheDir());
            tempFile.deleteOnExit();

            // Сохраните содержимое файла класса во временном файле
            try (OutputStream os = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = zipInputStream.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            }

            String dexOutputDir = getApplicationInfo().dataDir;
            DexClassLoader classLoader = new DexClassLoader(tempFile.getAbsolutePath(), dexOutputDir, null, getClassLoader());
         gamepadClass = classLoader.loadClass("com.example.MyClass");

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    private String getDataFromUri(Uri uri) {
        String name="";
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                Log.d("dataforme",zipEntry.getName());
                if (zipEntry.getName().endsWith(".png")) {
                    Bitmap bitmap = BitmapFactory.decodeStream(zipInputStream);
                    ImageView imageView = findViewById(R.id.gameImage);
                    imageView.setImageBitmap(bitmap);
                    Log.d("dataforme","there is image!");
                }else {
                    Log.d("dataforme","there is no image!");
                }
                if(zipEntry.getName().endsWith(".txt"))
                {

                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipInputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                        stringBuilder.append('\n');
                    }
                    name = stringBuilder.toString();



                }
            }
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    private void startIntent() {
    }


    private void startConnection() {

    }

    private void loadClass() {

    }

}