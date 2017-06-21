package tw.edu.stust.finalproj_chatroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoomActivity extends AppCompatActivity {

    private Button btnSend;
    private EditText edtvMsg;
    private ListView listViewChatRoom;

    private String userName, roomName, chat_msg, chat_user_name;

    private Intent intent;

    private DatabaseReference root;

    private ArrayList<HashMap<String, Object>> chatList = null;
    private HashMap<String, Object> map;

    private InputMethodManager imm;

    private CustomerAdapter customerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chatList = new ArrayList<>();

        intent = getIntent();

        btnSend = (Button) findViewById(R.id.btnSend);
        edtvMsg = (EditText) findViewById(R.id.edtvMsg);
        listViewChatRoom = (ListView) findViewById(R.id.listviewChatRoom);

        //自動滑到最底部
        //TODO


        userName = intent.getStringExtra("userName");
        roomName = intent.getStringExtra("chatRoomName");

        root = FirebaseDatabase.getInstance().getReference().child(roomName);

        //隱藏虛擬鍵盤
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        setTitle("聊天室:" + roomName);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatClass c = new ChatClass();
                c.userName = userName;
                c.userMsg = edtvMsg.getText().toString();
                root.push().setValue(c);
                edtvMsg.setText("");
                if(customerAdapter.getCount() > 3) {
                    listViewChatRoom.setStackFromBottom(true);
                }


            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    chatList.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        ChatClass chatClass = ds.getValue(ChatClass.class);
                        chat_user_name = chatClass.userName;
                        chat_msg = chatClass.userMsg;

                        map = new HashMap<String, Object>();
                        map.put("name",chat_user_name);
                        map.put("msg",chat_msg);

                        chatList.add(map);

                    }


                Log.i("123", chatList.toString());
                customerAdapter = new CustomerAdapter(ChatRoomActivity.this,chatList,userName);

                listViewChatRoom.setAdapter(customerAdapter);

                if(customerAdapter.getCount() > 8) {
                    listViewChatRoom.setStackFromBottom(true);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }



}

