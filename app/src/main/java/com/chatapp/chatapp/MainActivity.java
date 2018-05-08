package com.chatapp.chatapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.chatapp.chatapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.chatapp.chatapp.util.Util.TimeIsNow;


public class MainActivity extends FragmentActivity {


    private ListView listView;
    private EditText messageEditText;
    private ArrayList<Message> listMessages = new ArrayList<>();
    private static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent origemIntent = getIntent();
        id = origemIntent.getStringExtra(LoadActivity.CHAVE);

        messageEditText = findViewById(R.id.messageEditText);
        listView = findViewById(R.id.listView);
    }



    public void CaptureMessageSend(View view) {
        Message msg = new Message("Usuario", messageEditText.getText().toString(), Util.TimeIsNow());
        setMessageBox(msg);
        messageEditText.setText("");
        new ConsomeWS().execute("http://10.0.2.2:8080/FAQBot/api/sendmessage/"+ id + "&" + Util.RemoverAcentos(msg.getMessage()));
    }

    public void setMessageBox(Message msg){
        listMessages.add(msg);

        MessageListAdapter<Message> adapter =
                 new MessageListAdapter<>(this, listMessages);

        listView.setAdapter(adapter);
    }

    private class ConsomeWS extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .build();
            try{
                Response response = client.newCall(request).execute();
                return response.body().string();
            }
            catch(IOException e){
                return "";
            }
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                int count = 0;
                JSONArray mensagens = new JSONArray(json);

                while(count >= 0){
                    try {
                         JSONObject mensagem =
                                mensagens.getJSONObject(count);

                        Message msg = new Message("Bot", mensagem.getString("resposta_mensagem"), TimeIsNow());
                        count += 1;
                        setMessageBox(msg);
                    }catch(Exception ex){
                        count = -1;
                    }
                }
            } catch (JSONException e) {
                Message msg = new Message("Bot", "O serviço está fora do ar no momento. Tente novamente mais tarde.", TimeIsNow());
                setMessageBox(msg);
                e.printStackTrace();
            } catch(Exception f){
                f.printStackTrace();
            }
        }
    }
}
