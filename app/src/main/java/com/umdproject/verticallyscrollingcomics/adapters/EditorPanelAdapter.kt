package com.umdproject.verticallyscrollingcomics.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.umdproject.verticallyscrollingcomics.R
import com.umdproject.verticallyscrollingcomics.dataClasses.ComicPanel
import com.umdproject.verticallyscrollingcomics.viewModels.CurrentComicViewModel

internal class EditorPanelAdapter(context: Context, viewModelIn: CurrentComicViewModel) :
    RecyclerView.Adapter<EditorPanelAdapter.ViewHolder?>() {

    var viewModel: CurrentComicViewModel
    private val mContext: Context

    init {
        viewModel = viewModelIn
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
            mPanel = itemView.findViewById(R.id.panel_image)
            itemView.setOnClickListener(this)
        }

        fun bindTo(currentPanel: ComicPanel) {
            //mPanel.setImageBitmap(Bitmap.createBitmap(500,500, Bitmap.Config.RGB_565))
            mPanel.setImageBitmap(viewModel.panels.value!![adapterPosition].image)
        }

        override fun onClick(view: View) {

            val dialogBuilder = AlertDialog.Builder(mContext)
            dialogBuilder.setPositiveButton("Exit", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

            var hasHapticsListDebug = mutableListOf<Boolean>()

            for (panel in viewModel.panels.value!!) {
                hasHapticsListDebug.add(panel.hasHaptics)
            }

            //Log.d("EPA", "Before: " + hasHapticsListDebug.toString())

            var one = arrayOf("Haptics")
            var two = booleanArrayOf(viewModel.panels.value!![adapterPosition].hasHaptics)

            dialogBuilder.setMultiChoiceItems(one, two) { dialog, which, isChecked ->
                viewModel.setHapticsPrefs(adapterPosition, isChecked)
                var hasHapticsListDebug2 = mutableListOf<Boolean>()

                for (panel in viewModel.panels.value!!) {
                    hasHapticsListDebug2.add(panel.hasHaptics)
                }
                //Log.d("EPA","After: " + hasHapticsListDebug2.toString())
            }

            val alert = dialogBuilder.create()

            alert.setTitle("Panel Settings")

            alert.show()
        }
    }




}




