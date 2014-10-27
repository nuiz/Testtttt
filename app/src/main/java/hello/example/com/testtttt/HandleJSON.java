package hello.example.com.testtttt;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class HandleJSON {

    private Boolean success = false;
    private String urlString = "http://order.papangping.com/order.php";
    private Context ctx;

    public volatile boolean parsingComplete = false;
    public HandleJSON(Context ctx){ this.ctx = ctx; }
    public Boolean getSuccess(){ return success; }

    @SuppressLint("NewApi")
    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);

            // get success variable
            success = reader.getBoolean("success");

            parsingComplete = true;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void fetchJSON(String name, Integer number){
        RunLoad rl = new RunLoad();
        rl.setName(name);
        rl.setNumber(number);
        rl.setH(this);
        rl.execute();
    }
    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private class RunLoad extends AsyncTask<Void, Void, Void> {
        private String name;
        private Integer number;
        private HandleJSON h;

        public void setName(String name) {
            this.name = name;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public void setH(HandleJSON h) {
            this.h = h;
        }

        ProgressDialog pg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pg = new ProgressDialog(ctx);
            pg.setMessage("Loading...");
            pg.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://api.papangping.com/order.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                List<NameValuePair> nvParams = new ArrayList<NameValuePair>();
                nvParams.add(new BasicNameValuePair("name", name));
                nvParams.add(new BasicNameValuePair("number", (String.valueOf(number))));

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(nvParams));
                writer.flush();
                writer.close();
                os.close();

                Log.d("write", getQuery(nvParams));

                conn.connect();

                InputStream stream = conn.getInputStream();
                String data = convertStreamToString(stream);

                // parse json
                h.readAndParseJSON(data);

                stream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // จัดการกับ view
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pg.dismiss();

            AlertDialog al = new AlertDialog.Builder(ctx).setTitle("Response")
                    .setMessage("")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            if(h.getSuccess()){
                al.setMessage("Success");
            }
            else {
                al.setMessage("Failed");
            }
        }
    }
}
