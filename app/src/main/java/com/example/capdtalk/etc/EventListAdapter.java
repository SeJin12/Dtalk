package com.example.capdtalk.etc;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capdtalk.R;

import java.util.List;

public class EventListAdapter extends BaseAdapter {

    private Context context;
    private List<Event> eventList;

    public EventListAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int i) {
        return eventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.listview_event,null);
        TextView event_title = (TextView) v.findViewById(R.id.event_title);
        TextView event_content = (TextView) v.findViewById(R.id.event_content);
        TextView event_name = (TextView) v.findViewById(R.id.event_name);
        TextView event_date = (TextView) v.findViewById(R.id.event_date);

        event_title.setText(eventList.get(i).getTitle());
        event_content.setText(eventList.get(i).getContent());
        event_name.setText(eventList.get(i).getName());
        event_date.setText(eventList.get(i).getDate());

        v.setTag(eventList.get(i).getTitle());
        return v;
    }
}
