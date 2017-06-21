package tw.edu.stust.finalproj_chatroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private EditText room_name_edittext;
    private Button btn_add_room,btnSignOut;
    private ListView listView;

    private ArrayList<String> list_chant_room = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    private String name;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    private InputMethodManager inputMethodManager;

    private FirebaseUser firebaseUser ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){

            //Toast.makeText(this,firebaseUser.getDisplayName().toString(),Toast.LENGTH_SHORT).show();
            name = firebaseUser.getDisplayName().toString();


        }

        btnSignOut = (Button)findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();

            }
        });


        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        room_name_edittext = (EditText)findViewById(R.id.room_name_edittext);
        inputMethodManager.hideSoftInputFromWindow(room_name_edittext.getWindowToken(),0);

        btn_add_room = (Button)findViewById(R.id.btn_add_room);
        btn_add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> map = new HashMap<>();
                map.put(room_name_edittext.getText().toString(),"");
                root.updateChildren(map);
                room_name_edittext.setText("");

            }
        });

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this,ChatRoomActivity.class);
                intent.putExtra("chatRoomName",list_chant_room.get(position).toString());
                intent.putExtra("userName",name);
                startActivity(intent);

            }
        });


        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("123","test1");

                Set<String> set = new HashSet<>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){

                    set.add(((DataSnapshot)i.next()).getKey());

                }

                list_chant_room.clear();
                list_chant_room.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_chant_room);
        listView.setAdapter(arrayAdapter);
        requstName();



    }

    private void requstName() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("請輸入你的聊天室暱稱");
        final EditText Dialog_editName = new EditText(this);
        builder.setView(Dialog_editName);
        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                name = Dialog_editName.getText().toString();

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                requstName();

            }
        });

        builder.setCancelable(false);
        builder.show();

    }
}
