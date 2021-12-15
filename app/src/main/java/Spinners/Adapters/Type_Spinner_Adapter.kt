package Spinners.Adapters

import Spinners.Data.Size_item_info
import Spinners.Data.typeData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import my.Activities.R

class Type_Spinner_Adapter(context: Context,types:Array<typeData>) :ArrayAdapter<typeData>(context,0,types){
   lateinit var mConvertview:View
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
            mConvertview = LayoutInflater.from(context).inflate(R.layout.type_spinner_style,parent,false)
        }else{mConvertview=convertView}

        val imageView=mConvertview?.findViewById(R.id.type_spinner_icon) as ImageView
        val textView= mConvertview.findViewById(R.id.type_item_name) as TextView

        val currentItem=getItem(position)

        imageView.setImageResource(currentItem?.image!!)
        textView.text=currentItem.name

        return  mConvertview
    }
}