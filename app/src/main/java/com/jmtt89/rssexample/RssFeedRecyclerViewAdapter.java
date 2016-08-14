package com.jmtt89.rssexample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.prof.rssparser.Article;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RssFeedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Article> items;
    private final Context context;

    public RssFeedRecyclerViewAdapter(Context context, List<Article> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_item_image, parent, false);
                return new ImageViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_item, parent, false);
                return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Article item = items.get(position);
        if(item.getImage() != null && !item.getImage().isEmpty()){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Article item = items.get(position);

        if(item.getImage() != null && !item.getImage().isEmpty()){
            //ImageViewHolder
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            imageViewHolder.mItem = item;
            imageViewHolder.mTitleView.setText(item.getTitle());
            try {
                imageViewHolder.mSubTitleView.setText(DateUtils.getRelativeTimeSpanString(item.getPubDate().getTime()));
            }catch (NullPointerException _e){
                _e.printStackTrace();
            }
            imageViewHolder.mContentView.setText(item.getDescription());
            imageViewHolder.mActionShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, item.getLink());
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                }
            });
            imageViewHolder.mActionBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                    context.startActivity(intent);
                }
            });

            Picasso.with(context).load(item.getImage()).into(imageViewHolder.mImageView);

        }else{
            //ViewHolder
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.mItem = item;
            viewHolder.mTitleView.setText(item.getTitle());
            try {
                viewHolder.mSubTitleView.setText(DateUtils.getRelativeTimeSpanString(item.getPubDate().getTime()));
            }catch (NullPointerException _e){
                _e.printStackTrace();
            }
            viewHolder.mContentView.setText(item.getDescription());
            viewHolder.mActionShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, item.getLink());
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                }
            });
            viewHolder.mActionBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                    context.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView    mTitleView;
        public final TextView    mSubTitleView;
        public final TextView    mContentView;
        public final ImageButton mActionShare;
        public final ImageButton mActionBrowser;
        public Article mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView    = (TextView)    view.findViewById(R.id.txtTitle);
            mSubTitleView = (TextView)    view.findViewById(R.id.txtSubTitle);
            mContentView  = (TextView)    view.findViewById(R.id.txtContent);
            mActionShare  = (ImageButton) view.findViewById(R.id.btnShare);
            mActionBrowser= (ImageButton) view.findViewById(R.id.btnBrowser);
        }

    }

    public class ImageViewHolder extends ViewHolder {
        public final ImageView mImageView;
        private final ImageButton toggle;

        public ImageViewHolder(View view) {
            super(view);
            mImageView = (ImageView)   view.findViewById(R.id.image);
            toggle     = (ImageButton) view.findViewById(R.id.btnToggleContent);

            toggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (mContentView.getVisibility()){
                        case View.GONE:
                            mContentView.setVisibility(View.VISIBLE);
                            break;
                        case View.VISIBLE:
                            mContentView.setVisibility(View.GONE);
                            break;
                    }
                }
            });

        }
    }
}
