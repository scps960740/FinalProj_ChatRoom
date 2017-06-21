package tw.edu.stust.finalproj_chatroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 2017/6/20.
 */

public class CustomerAdapter extends BaseAdapter {

    String name;

    LayoutInflater layoutInflater;
    ArrayList<HashMap<String,Object>> arrayList ;

    CustomerAdapter(Context context , ArrayList<HashMap<String,Object>> arrayList , String name){

        this.layoutInflater = LayoutInflater.from(context);
        this.arrayList = arrayList;
        this.name = name;

    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewTag viewTag;
        if(convertView == null){

            convertView = layoutInflater.inflate(R.layout.chat_room_list,null);
            viewTag = new ViewTag();
            viewTag.tvOtherUserName = (TextView)convertView.findViewById(R.id.tvOtherUserName);
            viewTag.tvOtherUserMg = (TextView)convertView.findViewById(R.id.tvOtherUserMsg);
            viewTag.tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewTag.tvUserMsg = (TextView)convertView.findViewById(R.id.tvUserMsg);
            convertView.setTag(viewTag);
        }else {

            viewTag = (ViewTag)convertView.getTag();
        }


             viewTag.tvOtherUserName.setText(arrayList.get(position).get("name").toString());
             viewTag.tvOtherUserMg.setText(arrayList.get(position).get("msg").toString());



        return convertView;
    }

    class ViewTag{

        TextView tvOtherUserName , tvOtherUserMg, tvUserName , tvUserMsg;

    }

}


