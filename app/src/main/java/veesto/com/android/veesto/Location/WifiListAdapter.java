package veesto.com.android.veesto.Location;

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
 * Created by erez on 10/11/2016.
 */
public class WifiListAdapter extends ArrayAdapter<String>
{
    private final ArrayList<String> networks;
    private final Activity context;

    public WifiListAdapter(Activity context, int resource,ArrayList<String> networks) {
        super(context, resource);
        this.context = context;
        this.networks = networks;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return networks.size();
    }

    public class ViewHolderNetwork
    {
        TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        ViewHolderNetwork holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.netword_card, parent, false);
            holder  = new ViewHolderNetwork();
            holder.name = (TextView)convertView.findViewById(R.id.network_card_name);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolderNetwork)convertView.getTag();
        }
        holder.name.setText(networks.get(position));

        return convertView;
    }

}
