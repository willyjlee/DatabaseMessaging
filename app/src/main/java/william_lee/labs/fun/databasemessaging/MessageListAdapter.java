package william_lee.labs.fun.databasemessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by william_lee on 7/9/16.
 */
public class MessageListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<entry> entries;
    private LayoutInflater layoutInflater;

    public MessageListAdapter(Context context, ArrayList<entry>entries) {
        this.context = context;
        this.entries = entries;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return entries.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        entry entry = entries.get(position);
        View view = layoutInflater.inflate(R.layout.entry_view, null);

        TextView content = (TextView) view.findViewById(R.id.contentTextView);
        TextView time = (TextView) view.findViewById(R.id.timeTextView);

        content.setText(entry.getContent());
        time.setText(entry.getTime());

        return view;
    }

}
