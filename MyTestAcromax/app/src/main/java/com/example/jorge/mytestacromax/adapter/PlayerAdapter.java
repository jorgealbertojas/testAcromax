package com.example.jorge.mytestacromax.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.mytestacromax.R;
import com.example.jorge.mytestacromax.model.Player;

import java.util.List;

/**
 * @link PlayerAdapter} exposes a list of weather forecasts to a
 * {@link android.support.v7.widget.RecyclerView}
 */

/**
 * Created by jorge on 01/11/2017.
 */

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerAdapterViewHolder> {

    private List<Player> data;

    private Context mContext;

    private TextView mTxName;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private static PlayerAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface PlayerAdapterOnClickHandler {
        void onClick(Player player);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public PlayerAdapter(PlayerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public PlayerAdapter(List<Player> data) {
        this.data = data;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public class PlayerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mNameTextView;

        public PlayerAdapterViewHolder(View view) {
            super(view);
            mNameTextView = (TextView) view.findViewById(R.id.tx_name);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Player player = data.get(adapterPosition);
            mClickHandler.onClick(player);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public PlayerAdapter.PlayerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.information_player, viewGroup, false);
        mContext = viewGroup.getContext();
        return new PlayerAdapterViewHolder(v);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param PlayerAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(PlayerAdapterViewHolder PlayerAdapterViewHolder, int position) {

        Player player = ((Player) data.get(position));

        PlayerAdapterViewHolder.mNameTextView.setText(player.getName());

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == data) return 0;
        return data.size();
    }


}
