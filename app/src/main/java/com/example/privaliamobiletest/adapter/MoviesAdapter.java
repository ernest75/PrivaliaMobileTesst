package com.example.privaliamobiletest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.privaliamobiletest.R;
import com.example.privaliamobiletest.networking.apimodels.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {


    private List<Movie> mMovieList;
    private Context mContext;
    private ViewHolder mViewHolder;

    private static String baseImageUrl = "https://image.tmdb.org/t/p/w1280";

    public MoviesAdapter(List<Movie> mMovieList, Context mContext) {
        this.mMovieList = mMovieList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Movie movie = mMovieList.get(position);
        String year;
        if (movie.getReleaseDate().toString().length()>0) {
            year = movie.getReleaseDate().substring(0, 4);
        }else{
            year = mContext.getString(R.string.year_unknown);
        }
        viewHolder.mTvMovieTittle.setText(movie.getTitle() + " ( " + year + " )");
        viewHolder.mTvOverview.setText(movie.getOverview());

        final RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.noimage)
                .placeholder(R.drawable.ic_file_download_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(mContext)
                .load(baseImageUrl + movie.getPosterPath())
                .apply(options)
                .into(viewHolder.mIvMovie);

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivMovie)
        ImageView mIvMovie;
        @BindView(R.id.tvMovieTittle)
        TextView mTvMovieTittle;
        @BindView(R.id.tvOverview)
        TextView mTvOverview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
