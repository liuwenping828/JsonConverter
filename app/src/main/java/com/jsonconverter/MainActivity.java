package com.jsonconverter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        downLoadDefaultRes("http://192.168.42.114:8080/0000004.mp4");

    }


    private void downLoadDefaultRes(final String url) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://www.baidu.com/")
                .client(new OkHttpClient());
        final ApiService service = builder.build().create(ApiService.class);

        new AsyncTask<Void, Long, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Call<ResponseBody> call = service.downloadFile(url);
                try {
                    Response<ResponseBody> response = call.execute();
                    if (response.isSuccessful()) {
                        writeResponseBodyToDisk(response.body());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(MainActivity.this,"vedio download success", Toast.LENGTH_SHORT).show();
            }

        }.execute();

    }


    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }


    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getSDCardPath() + "0001.mp4");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("fileDown", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                Log.d("fileDown","file down success");

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    public void requestNetWork() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(JsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<JSONObject> call = service.getBeauties(1, 1);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Log.d("success", response.isSuccessful() + "");
                Log.d("body", response.body().toString());
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
    }


}
