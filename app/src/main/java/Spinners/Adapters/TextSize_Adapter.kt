package Spinners.Adapters

import Spinners.Data.sizeitem
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class TextSize_Adapter(context: Context,sizes:Array<sizeitem>):ArrayAdapter<sizeitem>(context,0,sizes){

    override fun getDropDownViewTheme(): Resources.Theme? {
        return super.getDropDownViewTheme()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent)
    }

    fun initView()
    {

    }


}