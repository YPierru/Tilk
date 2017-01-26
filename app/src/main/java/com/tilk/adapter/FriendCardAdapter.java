package com.tilk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tilk.R;
import com.tilk.models.FriendTilkeur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YPierru on 25/01/2017.
 */

public class FriendCardAdapter extends ArrayAdapter<FriendTilkeur> {

    private List<FriendTilkeur> listFriends;
    private int idItemRes;
    private boolean editMode;


    static class CardViewHolder {
        TextView name;
        TextView conso;
        ImageButton btnAction;
    }

    public FriendCardAdapter(Context context, int idItemRes, ArrayList<FriendTilkeur> listFriends,boolean editMode) {
        super(context, idItemRes);
        this.idItemRes=idItemRes;
        this.listFriends=listFriends;
        this.editMode=editMode;
    }

    @Override
    public void add(FriendTilkeur object) {
        listFriends.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.listFriends.size();
    }

    @Override
    public FriendTilkeur getItem(int index) {
        return this.listFriends.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(idItemRes, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.name = (TextView) row.findViewById(R.id.tv_friend_name);
            viewHolder.conso = (TextView) row.findViewById(R.id.tv_conso_friend);
            viewHolder.btnAction = (ImageButton)row.findViewById(R.id.btn_action);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        final FriendTilkeur friend = getItem(position);
        viewHolder.name.setText(friend.getPseudo());
        viewHolder.conso.setText(String.valueOf(friend.getConsoYear())+"L");
        viewHolder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode) {
                    Toast.makeText(getContext(),"Supprimer "+friend.getPseudo(),Toast.LENGTH_SHORT).show();
                    listFriends.remove(friend);
                    notifyDataSetChanged();

                }
                else {
                    Toast.makeText(getContext(),"Dialoguer avec "+friend.getPseudo(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        return row;
    }

}
