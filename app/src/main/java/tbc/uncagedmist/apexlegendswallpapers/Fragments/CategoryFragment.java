package tbc.uncagedmist.apexlegendswallpapers.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import tbc.uncagedmist.apexlegendswallpapers.Ads.GoogleAds;
import tbc.uncagedmist.apexlegendswallpapers.Common.Common;
import tbc.uncagedmist.apexlegendswallpapers.Common.MyApplicationClass;
import tbc.uncagedmist.apexlegendswallpapers.Model.Wallpapers;
import tbc.uncagedmist.apexlegendswallpapers.R;
import tbc.uncagedmist.apexlegendswallpapers.ViewHolder.CategoryViewHolder;

public class CategoryFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference categoryBackground;

    FirebaseRecyclerOptions<Wallpapers> options;
    FirebaseRecyclerAdapter<Wallpapers, CategoryViewHolder> adapter;

    @SuppressLint("StaticFieldLeak")
    private static CategoryFragment INSTANCE = null;

    RecyclerView recyclerView;

    private InterstitialAd mInterstitialAd;

    Context context;

    @Override
    public void onAttach(@NonNull Activity activity) {
        context = activity;
        super.onAttach(activity);
    }

    public CategoryFragment() {
        database = FirebaseDatabase.getInstance();
        categoryBackground = database.getReference(Common.FB_DB_NAME);

        options = new FirebaseRecyclerOptions.Builder<Wallpapers>()
                .setQuery(categoryBackground,Wallpapers.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Wallpapers, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Wallpapers model) {

                holder.progressBar.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(model.getImageLink())
                        .into(holder.wallpaperImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                holder.setItemClickListener((view, position1) -> {
                    if (GoogleAds.mInterstitialAd != null) {
                        GoogleAds.mInterstitialAd.show((Activity) context);
                    }
                    else {
                        ViewWallpaperFragment viewWallpaperFragment = new ViewWallpaperFragment();
                        FragmentTransaction transaction = ((AppCompatActivity)context)
                                .getSupportFragmentManager().beginTransaction();

                        Common.selected_background = model;
                        Common.selected_background_key = adapter.getRef(position).getKey();

                        transaction.replace(R.id.main_frame,viewWallpaperFragment).commit();
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_wallpaper_item,parent,false);

                if (MyApplicationClass.getInstance().isShowAds())   {
                    GoogleAds.loadGoogleFullscreen(context);
                }

                return new CategoryViewHolder(itemView);
            }
        };
    }

    public static CategoryFragment getInstance()    {

        if (INSTANCE == null)   {
            INSTANCE = new CategoryFragment();
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = view.findViewById(R.id.recycler_category);

        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (Common.isConnectedToInternet(context)) {
            setCategory();
        }
        else
            Toast.makeText(getContext(), "Please Connect to Internet...", Toast.LENGTH_SHORT).show();


        return view;
    }

    private void setCategory() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter != null)    {
            adapter.startListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null)    {
            adapter.startListening();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null)
            adapter.stopListening();
    }
}