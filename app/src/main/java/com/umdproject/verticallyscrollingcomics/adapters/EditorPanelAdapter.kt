package com.umdproject.verticallyscrollingcomics.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umdproject.verticallyscrollingcomics.R

internal class EditorPanelAdapter(context: Context, panels: MutableList<Bitmap>) :
    RecyclerView.Adapter<EditorPanelAdapter.ViewHolder?>() {

    var mPanels: MutableList<Bitmap>
    private val mContext: Context

    init {
        mPanels = panels
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext)
                .inflate(R.layout.editor_panel, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val currentPanel: Bitmap = mPanels[position]

        holder.bindTo(currentPanel)
    }


    override fun getItemCount(): Int {
        return mPanels.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val mPanel: ImageView
        init {


            mPanel = itemView.findViewById(R.id.panel_image)


            itemView.setOnClickListener(this)
        }

        fun bindTo(currentPanel: Bitmap) {
            //mPanel.setImageBitmap(Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565))
            mPanel.setImageBitmap(mPanels[adapterPosition])
        }

        override fun onClick(view: View) {

        }
    }




}