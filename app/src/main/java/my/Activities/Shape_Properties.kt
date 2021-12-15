package my.Activities

import Extensions.FragmentListener
import Extensions.ShapePropertiesButtons
import Extensions.TextPropertiesButtons
import Spinners.Data.shape_size_array
import Spinners.Data.text_size_Array
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import yuku.ambilwarna.AmbilWarnaDialog
import java.util.*


class Shape_Properties : Fragment() {
    lateinit var  Color_btn:ImageButton
    lateinit var Size_spn:Spinner
    lateinit var view1:View
    lateinit var mListener:FragmentListener
    lateinit var mContext: Context
    var prevcolor =0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       view1= inflater.inflate(R.layout.shape_properties_fragment, container, false)
        Color_btn=view1.findViewById(R.id.Shape_color_button)
        Size_spn=view1.findViewById(R.id.Shape_size_spinner)
        Size_spn.adapter=ArrayAdapter(mContext,R.layout.support_simple_spinner_dropdown_item, shape_size_array)

        Color_btn.setOnClickListener {
            val colorPicker = AmbilWarnaDialog(
                mContext,
               prevcolor,
                object : AmbilWarnaDialog.OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog?) {

                    }

                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        prevcolor = color
                        mListener.onChangeShapeProperties(ShapePropertiesButtons.sColor,color)
                    }

                })
            colorPicker.show()
        }

        Size_spn.onItemSelectedListener=object : AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mListener.onChangeShapeProperties(ShapePropertiesButtons.Size, (shape_size_array[p2]))
            }

        }
        return view1
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener=context as FragmentListener
        mContext=context
    }
}
