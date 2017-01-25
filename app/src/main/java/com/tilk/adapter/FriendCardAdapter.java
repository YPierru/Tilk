package com.tilk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tilk.R;
import com.tilk.models.FriendTilkeur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YPierru on 25/01/2017.
 */

public class FriendCardAdapter extends ArrayAdapter<FriendTilkeur> {

    private static final String TAG = "FriendCardAdapter";
    private List<FriendTilkeur> listFriends;
    private int idItemRes;


    static class CardViewHolder {
        TextView name;
        TextView conso;
    }

    public FriendCardAdapter(Context context, int idItemRes, ArrayList<FriendTilkeur> listFriends) {
        super(context, idItemRes);
        this.idItemRes=idItemRes;
        this.listFriends=listFriends;
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
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(idItemRes, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.name = (TextView) row.findViewById(R.id.tv_friend_name);
            viewHolder.conso = (TextView) row.findViewById(R.id.tv_conso_friend);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        FriendTilkeur friend = getItem(position);
        viewHolder.name.setText(friend.getPseudo());
        viewHolder.conso.setText("Consommation en 2017 : "+String.valueOf(friend.getConsoYear()));
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
