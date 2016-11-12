package veesto.com.android.veesto.FriendList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import veesto.com.android.veesto.R;

/**
 * Created by erez on 12/11/2016.
 */
public class FriendsListAdapter extends ArrayAdapter<String> {
    private final ArrayList<String> friends;
    private final Activity context;

    public FriendsListAdapter(Activity context, int resource,ArrayList<String> friends) {
        super(context, resource);
        this.context = context;
        this.friends = friends;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return friends.size();
    }

    public class ViewHolderFriend
    {
        TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        ViewHolderFriend holder ;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.friend_card, parent, false);
            holder  = new ViewHolderFriend();
            holder.name = (TextView)convertView.findViewById(R.id.friend_name);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolderFriend)convertView.getTag();
        }

        holder.name.setText(friends.get(position));
        return convertView;
    }

}
