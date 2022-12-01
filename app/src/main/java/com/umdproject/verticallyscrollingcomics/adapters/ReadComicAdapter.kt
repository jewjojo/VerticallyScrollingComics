package com.umdproject.verticallyscrollingcomics.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.dataClasses.ComicPanel
import com.umdproject.verticallyscrollingcomics.viewModels.CurrentComicViewModel

internal class ReadComicAdapter(context: Context, viewModelIn: CurrentComicViewModel) :
    RecyclerView.Adapter<ReadComicAdapter.ViewHolder?>() {

    var viewModel: CurrentComicViewModel
    private val mContext: Context

    init {
        viewModel = viewModelIn
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext)
                .inflate(R.layout.reader_panel, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val currentPanel: ComicPanel = viewModel.panels.value!![position]
        holder.bindTo(currentPanel)
    }


    override fun getItemCount(): Int {
        return viewModel.panels.value!!.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val mPanel: ImageView
        init {
            mPanel = itemView.findViewById(R.id.reader_panel_image)
            // Set spacing from viewmodel
            mPanel.setPadding(0,0,0, viewModel.panelSpacing.value!! * 2)
        }

        fun bindTo(currentPanel: ComicPanel) {
            mPanel.setImageBitmap(currentPanel.image)
        }

        override fun onClick(view: View) {

        }
    }




}