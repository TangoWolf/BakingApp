package com.example.shaol.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shaol.bakingapp.Models.Details;
import com.example.shaol.bakingapp.Models.Steps;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shaol on 5/18/2018.
 */


public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.MasterListViewHolder> {

    private final ListItemClickListener mOnClickListener;
    Details aDetailsInfo = null;
    Steps[] aStepsInfo = null;

    public interface ListItemClickListener {
        void onListItemClick(Steps stepsInfo, int position);
    }

    public MasterListAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    class MasterListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.detail_text_li)
        TextView listItem;

        public MasterListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Steps stepInfo = aStepsInfo[adapterPosition];
            mOnClickListener.onListItemClick(stepInfo, adapterPosition);
        }
    }

    @Override
    public MasterListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.detail_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MasterListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MasterListViewHolder viewHolder, int position) {
        Steps steps = aStepsInfo[position];
            String stepName = steps.getShortDescription();
            viewHolder.listItem.setText(stepName);
    }

    @Override
    public int getItemCount() {
        if (aStepsInfo == null) return 0;
        return aStepsInfo.length;
    }

    public void setData(Details detailInfo){
        aDetailsInfo = detailInfo;
        aStepsInfo = detailInfo.getSteps();
        notifyDataSetChanged();
    }
}

