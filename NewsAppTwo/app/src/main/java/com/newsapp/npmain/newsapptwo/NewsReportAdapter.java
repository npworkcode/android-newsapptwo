package com.newsapp.npmain.newsapptwo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsReportAdapter extends ArrayAdapter<NewsReport>
{
    private final Context context;

    public NewsReportAdapter(Activity context, List<NewsReport> newsReportsList)
    {
        super(context, 0, newsReportsList);
        this.context = context;
    }

    static class ViewHolder
    {
        private TextView storeNewsReportTitleTextView;
        private TextView storeNewsReportSectionNameTextView;
        private TextView storeNewsReportWebURLTextView;
        private TextView storeNewsReportAuthorNameTextView;
        private TextView storeNewsReportDatePublishedTextView;
    }

    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView,  @NonNull ViewGroup parent)
    {
        ViewHolder holder;
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        NewsReport currentNewsReportItem = getItem(position);
        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.storeNewsReportTitleTextView = convertView.findViewById(R.id.txt_newsreport_title);
            holder.storeNewsReportSectionNameTextView = convertView.findViewById(R.id.txt_newsreport_section_name);
            holder.storeNewsReportWebURLTextView = convertView.findViewById(R.id.txt_newsreport_webURL);
            holder.storeNewsReportAuthorNameTextView = convertView.findViewById(R.id.txt_newsreport_author_name);
            holder.storeNewsReportDatePublishedTextView =  convertView.findViewById(R.id.txt_newsreport_date_published);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.storeNewsReportTitleTextView.setText(currentNewsReportItem.getTitle());
        holder.storeNewsReportSectionNameTextView.setText(currentNewsReportItem.getSectionName());
        holder.storeNewsReportWebURLTextView.setText(currentNewsReportItem.getWebUri());
        holder.storeNewsReportAuthorNameTextView.setText(currentNewsReportItem.getAuthorName());
        holder.storeNewsReportDatePublishedTextView.setText(currentNewsReportItem.getDatePublished());
        return convertView;
    }
}
