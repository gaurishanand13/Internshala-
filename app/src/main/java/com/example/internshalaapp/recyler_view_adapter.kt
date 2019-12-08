package com.example.internshalaapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view.view.*
import java.lang.StringBuilder

class viewHolder(itemViiew : View) : RecyclerView.ViewHolder(itemViiew)

interface adapterInterface
{
    fun register_workshop(position: Int,itemView : View)
    fun unregister_workshop(position: Int,itemView : View)
    fun update_color(itemView : View)
    fun update_color2(itemView: View)
}

class myAdapter(var context: Context,var workshopRegistered : Array<Boolean>) : RecyclerView.Adapter<viewHolder>()
{

    var myInterface : adapterInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = layoutInflater.inflate(R.layout.item_view,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount() = 9

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.title.text = workShopDetails.ar1[position]
        holder.itemView.details.text = workShopDetails.ar2[position]

        if(workshopRegistered[position] == true)
        {
            myInterface?.update_color(holder.itemView)
        }
        else
        {
            myInterface?.update_color2(holder.itemView)
        }

        holder.itemView.registerWorkshopButton.registerWorkshopButtonTextView.setOnClickListener {
            if(holder.itemView.registerWorkshopButton.registerWorkshopButtonTextView.text.toString().equals("REGISTER"))
            {
                myInterface?.register_workshop(position,holder.itemView)
            }
            else
            {
                //it means we need to deregister this workshop with the user
                myInterface?.unregister_workshop(position,holder.itemView)
            }
        }
    }


}



class myAdapter2(var workshopRegistered : String,val count : Int) : RecyclerView.Adapter<viewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = layoutInflater.inflate(R.layout.item_view2,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount() = count

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.title.text = workShopDetails.ar1[workshopRegistered[position]-'0']
        holder.itemView.details.text = workShopDetails.ar2[workshopRegistered[position]-'0']
    }


}