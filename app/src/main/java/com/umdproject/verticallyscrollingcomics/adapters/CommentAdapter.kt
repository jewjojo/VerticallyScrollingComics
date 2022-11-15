package com.umdproject.verticallyscrollingcomics.adapters


import com.umdproject.verticallyscrollingcomics.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.ArrayList
import androidx.recyclerview.widget.RecyclerView


class CommentAdapter(private val context: Context, private val list: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var tvLabel: TextView

        init {
            tvLabel = itemView.findViewById(R.id.commentBox) // Initialize your All views prensent in list items
        }

        internal fun bind(position: Int) {
            // This method will be called anytime a list item is created or update its data
            //Do your stuff here
            tvLabel.text = list[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.read_comments, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}