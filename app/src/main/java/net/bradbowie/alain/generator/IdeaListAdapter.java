package net.bradbowie.alain.generator;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bradbowie.alain.R;
import net.bradbowie.alain.databinding.IdeaListHeaderBinding;
import net.bradbowie.alain.databinding.IdeaListRowBinding;
import net.bradbowie.alain.model.Idea;
import net.bradbowie.alain.util.CollectionUtils;
import net.bradbowie.alain.util.LOG;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bradbowie on 4/11/16.
 */
public class IdeaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = LOG.tag(IdeaListAdapter.class);

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_IDEA = 10;

    private ArrayList<Idea> greatestIdeasEver = new ArrayList<>();
    private Random r = new Random();
    private boolean generating = false;
    private String headerText;

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public void setGenerating(boolean generating) {
        if(generating && !this.generating) {
            notifyItemInserted(0);
        } else if(!generating && this.generating) {
            notifyItemRemoved(0);
        }

        this.generating = generating;
    }

    public void addIdea(Idea greatestIdeaEver) {
        if(greatestIdeaEver != null) {
            LOG.d(TAG, "Inserting greatest idea ever: " + greatestIdeaEver);
            greatestIdeasEver.add(0, greatestIdeaEver);
            notifyItemInserted(generating ? 1 : 0);
        }
    }

    public void addAllIdeas(List<Idea> ideas) {
        if(!CollectionUtils.isValid(ideas)) return;

        LOG.w(TAG, "Adding all ideas: " + ideas.size());
        for(int i = ideas.size() - 1; i >= 0; i--) {
            addIdea(ideas.get(i));
        }
    }

    public ArrayList<Idea> getGreatestIdeasEver() {
        return greatestIdeasEver;
    }

    public void clearIdeas() {
        if (CollectionUtils.isValid(greatestIdeasEver)) {
            int start = generating ? 1 : 0;
            int size = greatestIdeasEver.size();
            greatestIdeasEver = new ArrayList<>();
            notifyItemRangeRemoved(start, size);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(generating && position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_IDEA;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER: return inflateHeader(parent);
            case VIEW_TYPE_IDEA: return inflateIdea(parent);
        }

        return null;
    }

    private RecyclerView.ViewHolder inflateHeader(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.idea_list_header, parent, false);
        return new HeaderViewHolder(v);
    }

    private RecyclerView.ViewHolder inflateIdea(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.idea_list_row, parent, false);
        return new IdeaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(generating && position == 0) {
            bindHeader((HeaderViewHolder) holder);
        } else {
            bindIdea((IdeaViewHolder) holder, generating ? position - 1 : position);
        }
    }

    private void bindHeader(HeaderViewHolder holder) {
        holder.getBinding().ideaListHeader.setText(headerText);
    }

    private void bindIdea(IdeaViewHolder holder, int position) {
        Idea idea = greatestIdeasEver.get(position);
        LOG.v(TAG, "Binding row: " + idea + " at position " + position);
        holder.getBinding().setIdea(idea);
    }

    @Override
    public int getItemCount() {
        int count = CollectionUtils.size(greatestIdeasEver);
        if(generating) count += 1;
        return count;
    }

    protected static class IdeaViewHolder extends RecyclerView.ViewHolder {
        IdeaListRowBinding binding;

        public IdeaViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public IdeaListRowBinding getBinding() {
            return binding;
        }
    }

    protected static class HeaderViewHolder extends RecyclerView.ViewHolder {
        IdeaListHeaderBinding binding;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public IdeaListHeaderBinding getBinding() {
            return binding;
        }
    }
}
