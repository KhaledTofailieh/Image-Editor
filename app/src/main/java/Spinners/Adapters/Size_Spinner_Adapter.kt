package Spinners.Adapters

import Spinners.Data.Size_item_info
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import my.Activities.R

class Size_Spinner_Adapter(context: Context, Sizes_List: Array<Size_item_info>)
    : ArrayAdapter<Size_item_info>(context, 0, Sizes_List)
{
    lateinit var mConvertview: View

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position,convertView,parent)

    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position,convertView,parent)

    }

    fun initView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        if(convertView == null)
        {
            mConvertview = LayoutInflater.from(context).inflate(R.layout.size_spinner_style,parent,false)
        }else{mConvertview=convertView}

        val imageView=mConvertview?.findViewById(R.id.Size_Spinner_icon) as ImageView
        val textView= mConvertview.findViewById(R.id.Size_Spinner_Value) as TextView

        val currentItem:Size_item_info?=getItem(position)

        imageView.setImageResource(currentItem?.image!!)
        textView.text=currentItem?.number.toString()

        return  mConvertview
    }

}