package tbc.uncagedmist.apexlegendswallpapers.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;

import tbc.uncagedmist.apexlegendswallpapers.Interface.ItemClickListener;
import tbc.uncagedmist.apexlegendswallpapers.R;

public class ListWallpaperViewHolder
        extends RecyclerView.ViewHolder implements View.OnClickListener {

    public KenBurnsView wallpaper;
    public ProgressBar progressBar;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ListWallpaperViewHolder(View itemView) {
        super(itemView);

        wallpaper = itemView.findViewById(R.id.imageView);
        progressBar = itemView.findViewById(R.id.progress_bar);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }
}