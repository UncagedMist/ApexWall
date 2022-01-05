package tbc.uncagedmist.apexlegendswallpapers.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import tbc.uncagedmist.apexlegendswallpapers.Ads.GoogleAds;
import tbc.uncagedmist.apexlegendswallpapers.Common.Common;
import tbc.uncagedmist.apexlegendswallpapers.Common.MyApplicationClass;
import tbc.uncagedmist.apexlegendswallpapers.Model.Wallpapers;
import tbc.uncagedmist.apexlegendswallpapers.R;
import tbc.uncagedmist.apexlegendswallpapers.ViewHolder.ListWallpaperViewHolder;

public class PopularFragment extends Fragment {

    Context context;

    private static PopularFragment INSTANCE = null;

    RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference categoryBackground;

    FirebaseRecyclerOptions<Wallpapers> options;
    FirebaseRecyclerAdapter<Wallpapers, ListWallpaperViewHolder> adapter;

    @Override
    public void onAttach(@NonNull Activity activity) {
        context = activity;
        super.onAttach(activity);
    }

    public PopularFragment(Context context) {
        database = FirebaseDatabase.getInstance();
        categoryBackground = database.getReference(Common.FB_DB_NAME);

        Query query = categoryBackground.orderByChild("viewCount")
                .limitToLast(20);

        options = new FirebaseRecyclerOptions.Builder<Wallpapers>()
                .setQuery(query,Wallpapers.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Wallpapers, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder,
                                            int position, @NonNull final Wallpapers model) {

                holder.progressBar.setVisibility(View.VISIBLE);

                Picasso.get()
                        .load(model.getImageLink())
                        .into(holder.wallpaper, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                holder.setItemClickListener((view, position1) -> {

                    if (GoogleAds.mInterstitialAd != null) {
                        GoogleAds.mInterstitialAd.show(getActivity());
                    }
                    else {
                        ViewWallpaperFragment viewWallpaperFragment = new ViewWallpaperFragment();
                        FragmentTransaction transaction = getActivity()
                                .getSupportFragmentManager().beginTransaction();

                        Common.selected_background = model;
                        Common.selected_background_key = adapter.getRef(position1).getKey();

                        transaction.replace(R.id.main_frame,viewWallpaperFragment).commit();
                    }
                });

            }

            @NonNull
            @Override
            public ListWallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_wallpaper_item,parent,false);

                int height = parent.getMeasuredHeight() / 2;
                itemView.setMinimumHeight(height);

                if (MyApplicationClass.getInstance().isShowAds())   {
                    GoogleAds.loadGoogleFullscreen(context);
                }

                return new ListWallpaperViewHolder(itemView);
            }
        };
    }

    public static PopularFragment getInstance(Context context)    {

        if (INSTANCE == null)   {
            INSTANCE = new PopularFragment(context);
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        recyclerView = view.findViewById(R.id.recycler_trending);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (Common.isConnectedToInternet(context))
            loadTrendingList();
        else
            Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();

        return view;
    }

    private void loadTrendingList() {
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
        if (adapter != null)    {
            adapter.stopListening();
        }
    }
}