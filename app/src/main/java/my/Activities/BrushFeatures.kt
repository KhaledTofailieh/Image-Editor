package my.Activities

import Extensions.*
import Spinners.Adapters.Size_Spinner_Adapter
import Spinners.Adapters.Type_Spinner_Adapter
import Spinners.Data.Sizes_information_List
import Spinners.Data.types_info_list
import Tools.Pen
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Spinner

@SuppressLint("ValidFragment")
class BrushFeatures(context:Context) : Fragment() {
    lateinit var SelectColor_button:ImageButton
    lateinit var SelectSize_spinner:Spinner
    lateinit var SelectType_spinner:Spinner
    lateinit var mListener:FragmentListener
    lateinit var type:DrawingButtons
    val mContext:Context
    init{
        mContext=context
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view= inflater.inflate(R.layout.brush_features_fragment, container, false)

        val mAdapter=Size_Spinner_Adapter(mContext,Sizes_information_List)
        val tAdapter=Type_Spinner_Adapter(mContext, types_info_list)

        SelectColor_button=view.findViewById(R.id.brush_color_button)
        SelectSize_spinner=view.findViewById(R.id.brush_size_spinner)
        SelectType_spinner=view.findViewById(R.id.brush_type_spinner)
        SelectType_spinner.adapter=tAdapter
        SelectSize_spinner.adapter=mAdapter
        SelectSize_spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
             //   Toast.makeText(context,"Item Selected : ${Sizes_information_List[p2]}", Toast.LENGTH_SHORT).show()
              Brush_Features.size= p2
                Pen.setpro()
            }

        }

        SelectColor_button.setOnClickListener {
            mListener.onChangeBrush(BrushFeatureButtons.color)
        }

        SelectType_spinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               Brush_Features.type=DrawingButtons.valueOf(types_info_list[p2].name)
                Pen.setpro()
            }
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener=context as FragmentListener
    }
}
