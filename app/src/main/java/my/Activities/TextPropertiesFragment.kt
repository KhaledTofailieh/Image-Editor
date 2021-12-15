package my.Activities

import Extensions.FragmentListener
import Extensions.TextPropertiesButtons
import Extensions.open_color_picker
import Spinners.Data.sizeitem
import Spinners.Data.text_size_Array
import Spinners.Data.text_type_Array_names
import Tools.Text
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class TextPropertiesFragment : Fragment() {
    lateinit var view1:View
    lateinit var color_btn: ImageButton
    lateinit var size_spinner:Spinner
    lateinit var type_spinner:Spinner
    lateinit var mContext: Context
lateinit var  mListener:FragmentListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view1 =inflater.inflate(R.layout.text_properties_fragment, container, false)
        color_btn=view1.findViewById(R.id.text_color_button)
        size_spinner=view1.findViewById(R.id.text_size_spinner)
        type_spinner=view1.findViewById(R.id.text_type_spinner)



        color_btn.setOnClickListener {
          open_color_picker(mContext,Text.tPaint.color)

        mListener.onChangeTextProperties(TextPropertiesButtons.color,Text.tPaint.color)

        }

        size_spinner.adapter=ArrayAdapter(mContext,R.layout.support_simple_spinner_dropdown_item, text_size_Array)
        type_spinner.adapter=ArrayAdapter(mContext,R.layout.support_simple_spinner_dropdown_item, text_type_Array_names)

        size_spinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mListener.onChangeTextProperties(TextPropertiesButtons.size, (text_size_Array[p2]).toInt())
            }

        }
        type_spinner.onItemSelectedListener=object:AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                mListener.onChangeTextProperties(TextPropertiesButtons.type,p2)
            }
        }
        return view1
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext=context!!
        mListener=context as FragmentListener
    }
}
