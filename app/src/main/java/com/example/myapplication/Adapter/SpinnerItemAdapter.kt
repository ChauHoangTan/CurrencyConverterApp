package com.example.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.R

class SpinnerItemAdapter(private val context: Context, private val flagMap: Map<String, String>): BaseAdapter() {

    private val keys = flagMap.keys.toList()

    override fun getCount(): Int {
        return flagMap.size
    }

    override fun getItem(position: Int): Any {
        return keys[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getPositionByKey(key: String): Int {
        return keys.indexOf(key)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);

        val imageView: ImageView = view.findViewById(R.id.imgView)
        val textView: TextView = view.findViewById(R.id.textView)

        val key = getItem(position)
        textView.text = key.toString();

        Glide.with(context)
            .load(flagMap[key])
            .into(imageView)
        return view
    }
}