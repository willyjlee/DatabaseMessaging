package william_lee.labs.fun.databasemessaging;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {

    //private Button addBtn;
    //private Button reloadBtn;
    private ListView listView;

    private MessageListAdapter listAdapter;
    private ArrayList<entry> list;

    private JSonParse parser = new JSonParse();

    private View footerView;

    private static final String get_messages_url = "http://ip/android_messaging/load_messages.php";
    private static final String add_message_url = "http://ip/android_messaging/insertnew.php";

    private static final String success_tag = "success";
    private static final String entries_tag = "entries";
    private static final String entry_id_tag = "message_id";
    private static final String entry_content_tag = "message_content";
    private static final String entry_time_tag = "message_time";

    // ip address = 192.168.1.73


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        //addBtn = (Button) findViewById(R.id.new_message_btn);
        //reloadBtn = (Button)findViewById(R.id.reload_btn);
        listView = (ListView) findViewById(R.id.listView);

        footerView = getLayoutInflater().inflate(R.layout.add_message_view, null);

        Log.i("tag", "up to here ok");

//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String str = ((EditText) footerView.findViewById(R.id.addMsgEditText)).getText().toString();
//                if(str.length()>0){
//                    addMsg(str);
//                    setup();
//                }
//            }
//        });

//        reloadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setup();
//            }
//        });

        setup();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        final int id = item.getItemId();
        if( id == R.id.new_message_btn){
            String str = ((EditText) footerView.findViewById(R.id.addMsgEditText)).getText().toString();
            if(str.length()>0){
                addMsg(str);
                //setup();
            }
        }
        else if(id == R.id.reload_btn){
            setup();
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        final int id = item.getItemId();
//        if (id == R.id.action_selfie) {
//            final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, REQUEST_PHOTO);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onResume(){
        super.onResume();
        setup();
    }

    public void setup(){
        reload();
        footerView = getLayoutInflater().inflate(R.layout.add_message_view, null);
//        EditText editText = (EditText) footerView.findViewById(R.id.addMsgEditText);
//        editText.setFocusableInTouchMode(true);
//        editText.requestFocus();
        listView.addFooterView(footerView);
    }

    public void reload(){
        Log.i("tag", ""+listView.removeFooterView(footerView));
        load();
        //update();
    }

    public void update(){
        listAdapter = new MessageListAdapter(MessagingActivity.this, list);
        listView.setAdapter(listAdapter);
    }

    public void load(){
        list=new ArrayList<entry>();
        new Loader().execute();

    }

    public void addMsg(String s){
        new Adder().execute(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    class Adder extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String...arg){

            String msg = arg[0];
            Log.i("tag",msg);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String timestr = SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            Log.i("tag",timestr);

            params.add(new BasicNameValuePair(entry_content_tag, msg));
            params.add(new BasicNameValuePair(entry_time_tag, timestr));

            JSONObject jobj = parser.makeHttpRequest(add_message_url, "push", params);

            try {
                if (jobj.getInt(success_tag) == 1){
                    Log.i("tag", "added new msg");
                }
                else{
                    Log.i("tag", "failed to add new msg");
                    Log.i("tag", jobj.getString("message"));
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setup();
                }
            });
        }

    }

    class Loader extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String...arg){
            List<NameValuePair> params=new ArrayList<NameValuePair>();


            JSONObject jobj = parser.makeHttpRequest(get_messages_url, "pull", params);

            try{
                if(jobj.getInt(success_tag)==1){
                    JSONArray jsonArray = jobj.getJSONArray(entries_tag);
                    JSONObject getobj;
                    entry add;
                    for(int i=0;i<jsonArray.length();i++){
                        getobj=jsonArray.getJSONObject(i);
                        add = new entry(getobj.getInt(entry_id_tag),
                                getobj.getString(entry_content_tag), getobj.getString(entry_time_tag));
                        list.add(add);
                    }
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return "";

        }

        @Override
        protected void onPostExecute(String s){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }
    }

}
