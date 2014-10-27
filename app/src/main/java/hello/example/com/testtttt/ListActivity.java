package hello.example.com.testtttt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ListActivity extends Activity {
    ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
    ListView lv;
    ListAdapter adt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RunLoad rl = new RunLoad();
        rl.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class RunLoad extends AsyncTask<Void, Void, Void> {
        ProgressDialog pg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pg = new ProgressDialog(ListActivity.this);
            pg.setMessage("Loading...");
            pg.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://api.papangping.com/list.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.connect();

                InputStream stream = conn.getInputStream();
                String data = HandleJSON.convertStreamToString(stream);

                JSONObject reader = new JSONObject(data);

                // get success variable
                JSONArray arr = reader.getJSONArray("data");
                Integer i;
                OrderEntity order;
                for (i=0;i<arr.length();i++){
                    JSONObject jData = arr.getJSONObject(i);
                    order = new OrderEntity();
                    order.setName(jData.getString("name"));
                    order.setNumber(jData.getInt("number"));

                    orders.add(order);
                }

                stream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // จัดการกับ view
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            lv = (ListView)findViewById(R.id.orderList);
            adt = new ListAdapter(getApplicationContext(), R.layout.order_layout, orders);
            lv.setAdapter(adt);
            adt.setNotifyOnChange(true);

            pg.dismiss();
        }
    }
}
