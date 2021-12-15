package Spinners.Adapters
import Spinners.Data.Image_type_item
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import my.Activities.R

class ImageType_Adapter(context: Context , TypesList:List<Image_type_item>):ArrayAdapter<Image_type_item>(context,0,TypesList)
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
            mConvertview = LayoutInflater.from(context).inflate(R.layout.colorslice_spinner_style,parent,false)
        }else{mConvertview=convertView}

        val textView= mConvertview.findViewById(R.id.Color_Slice_Name) as TextView

        val currentItem=getItem(position)

        textView.text=currentItem?.name

        return  mConvertview
    }
}