package tbc.uncagedmist.apexlegendswallpapers.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;

import tbc.uncagedmist.apexlegendswallpapers.Interface.ItemClickListener;
import tbc.uncagedmist.apexlegendswallpapers.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public ProgressBar progressBar;
    public KenBurnsView wallpaperImage;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoryViewHolder(View itemView) {
        super(itemView);

        progressBar = itemView.findViewById(R.id.progress_bar);
        wallpaperImage = itemView.findViewById(R.id.imageView);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }
}