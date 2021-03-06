package veesto.com.android.veesto.Record;

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
 * Created by erez on 13/11/2016.
 */
public class AudioSourcesAdapter extends ArrayAdapter<AudioSourceE>{

        private final ArrayList<AudioSourceE> sources;
        private final Activity context;

        public AudioSourcesAdapter(Activity context, int resource,ArrayList<AudioSourceE> sources) {
            super(context, resource);
            this.context = context;
            this.sources = sources;
        }

        public AudioSourceE getMyItem(int position)
        {
            return sources.get(position);
        }

        @Override
        public AudioSourceE getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return sources.size();
        }

        public static final class ViewHolderSource
        {
            TextView name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = context.getLayoutInflater();
            ViewHolderSource holder ;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.audio_source_card, parent, false);
                holder  = new ViewHolderSource();
                holder.name = (TextView)convertView.findViewById(R.id.audio_source_text);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolderSource)convertView.getTag();
            }
            Log.d("getView", "position = " + position);

            holder.name.setText(sources.get(position).toString());

            return convertView;
        }

    }

