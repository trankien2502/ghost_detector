package com.ghostdetctor.ghost_detector.ui.ghost.collection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ghostdetctor.ghost_detector.ui.ghost.model.Ghost;
import com.ghostdetector.ghost_detector.databinding.ItemGhostBinding;

import java.util.List;

public class GhostAdapter extends RecyclerView.Adapter<GhostAdapter.GhostViewHolder> {

    List<Ghost> ghostList;
    Context context;
    IOnClickGhostItemListener iOnClickGhostItemListener;

    public GhostAdapter(Context context,List<Ghost> ghostList,  IOnClickGhostItemListener iOnClickGhostItemListener) {
        this.ghostList = ghostList;
        this.context = context;
        this.iOnClickGhostItemListener = iOnClickGhostItemListener;
    }

    public GhostAdapter(Context context,List<Ghost> ghostList ) {
        this.ghostList = ghostList;
        this.context = context;
    }

    public interface IOnClickGhostItemListener {
        void onClickGhostItemListener(Ghost ghost);
    }
    @NonNull
    @Override
    public GhostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGhostBinding itemGhostBinding = ItemGhostBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new GhostViewHolder(itemGhostBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GhostViewHolder holder, int position) {
        Ghost ghost = ghostList.get(position);
        if (ghost==null) return;
        holder.itemGhostBinding.tvGhostName.setText(ghost.getName());
        Bitmap bitmap = BitmapFactory.decodeFile(ghost.getImagePath());
        holder.itemGhostBinding.ivGhost.setImageBitmap(bitmap);
        holder.itemGhostBinding.clGhost.setOnClickListener(view -> iOnClickGhostItemListener.onClickGhostItemListener(ghost));
    }

    @Override
    public int getItemCount() {
        if (ghostList==null)
            return 0;
        return ghostList.size();
    }

    public static class GhostViewHolder extends RecyclerView.ViewHolder{
        ItemGhostBinding itemGhostBinding;
        public GhostViewHolder(ItemGhostBinding itemGhostBinding) {
            super(itemGhostBinding.getRoot());
            this.itemGhostBinding = itemGhostBinding;
        }
    }
}
