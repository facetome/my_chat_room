package com.basic.chat_room.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.chat_room.Entry.Contact;
import com.basic.chat_room.Entry.GroupEntry;
import com.basic.chat_room.R;
import com.basic.chat_room.utils.XmppUtil;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by basic on 2015/6/15.
 */
public class SingleFragment extends Fragment {
    public static final int LABLE_NAME = R.string.label_single_point;
    public static final int LABEL_ICON = R.drawable.single_selector;
    private static final String TAG = "SingleFragment";

    private ExpandableListView mListView;
    private ImageView mEmptyIcon;
    private BaseExpandAdapter mAdapter;
    private List<GroupEntry> mGroup;
    private SparseArray<List<Contact>> mRosterEntrySparse;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_fragment, null);
        mEmptyIcon = (ImageView) view.findViewById(R.id.empty_icon);
        mListView = (ExpandableListView) view.findViewById(R.id.single_expandlist);
        mListView.setCacheColorHint(Color.TRANSPARENT);
        mAdapter = new BaseExpandAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(new OnChildClickListener() {
           @Override
           public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
               String communicationId = mRosterEntrySparse.get(groupPosition)
                         .get(childPosition)
                         .getName();
               Log.d(TAG, "key:contact_name:" + communicationId);
               Intent intent = new Intent(getActivity(), BaseComunicationActivity.class);
               intent.putExtra(BaseComunicationActivity.KEY_COMUNICATION_ID, communicationId);
               getActivity().startActivity(intent);
               return true;
           }
       });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //开始获取联系人.
        syncRoster();
    }

    //同步联系人
    private void syncRoster() {
        Roster roster = XmppUtil.getConnection(getActivity()).getRoster();
        mRosterEntrySparse = new SparseArray<>();
        mGroup = new ArrayList<>();
        if (roster != null) {
            Collection<RosterGroup> groups = roster.getGroups();
            if (groups != null) {
                int key = 0;
                mRosterEntrySparse = new SparseArray<>();
                mGroup = new ArrayList<>();
                for (RosterGroup rosterGroup : groups) {
                    if (rosterGroup != null) {
                        List<Contact> entryList = new ArrayList<>();
                        Collection<RosterEntry> collection = rosterGroup.getEntries();
                        Iterator<RosterEntry> iterator = collection.iterator();
                        int availableNum = 0;
                        while (iterator.hasNext()) {
                            RosterEntry entry = iterator.next();
                            Contact contact = new Contact();
                            contact.setName(entry.getName());
                            contact.setUserId(entry.getUser());
                            boolean isAvailable = roster.getPresence(entry.getName()).isAvailable();
                            availableNum = isAvailable == true ? ++availableNum : availableNum;
                            contact.setIsAvaliable(isAvailable);
                            entryList.add(contact);
                        }
                        GroupEntry groupEntry = new GroupEntry();
                        groupEntry.setGroupName(rosterGroup.getName());
                        groupEntry.setTotalNum(rosterGroup.getEntryCount());
                        groupEntry.setOnlineNum(availableNum);
                        mGroup.add(groupEntry);
                        mRosterEntrySparse.put(key, entryList);
                        key++;
                    }
                }
            }
        }

        if(mGroup.size() != 0){
         mEmptyIcon.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();


    }


    private class BaseExpandAdapter extends BaseExpandableListAdapter {


        @Override
        public int getGroupCount() {
            return mGroup == null ? 0 : mGroup.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mRosterEntrySparse == null ? 0 : mRosterEntrySparse.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroup.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mRosterEntrySparse.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.expand_group_item,
                        null);
            }
            ((TextView) convertView.findViewById(R.id.group_title)).setText(mGroup
                    .get(groupPosition).getGroupName());
            GroupEntry entry = mGroup.get(groupPosition);
            ((TextView) convertView.findViewById(R.id.presence_num)).setText(getString(R.string
                    .lable_separator_slash, entry.getOnlineNum(), entry.getTotalNum()));

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.roaster_item,
                        null);
            }
            ImageView icon = (ImageView) convertView.findViewById(R.id.roaster_icon);
            TextView title = (TextView) convertView.findViewById(R.id.roaster_name);
            TextView status = (TextView) convertView.findViewById(R.id.roaster_status);
            Contact contact = mRosterEntrySparse.get(groupPosition).get(childPosition);
            title.setText(contact.getName());
            String available = contact.isAvaliable() == true ?
                    getString(R.string.lable_prefrence_online) : getString(R.string.lable_prefrence_offline);
            status.setText(available);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
