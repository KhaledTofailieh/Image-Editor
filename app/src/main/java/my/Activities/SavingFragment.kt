package my.Activities

import Extensions.FragmentListener
import Extensions.TextPropertiesButtons
import Spinners.Adapters.ImageType_Adapter
import Spinners.Data.Image_type_list
import Spinners.Data.text_size_Array
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import java.util.*


class SavingFragment : Fragment() {
    lateinit var OkButton:ImageButton
    lateinit var ImageName:EditText
    lateinit var ImageType:Spinner
    lateinit var mListener:FragmentListener
    lateinit var TypeAdapter:ImageType_Adapter
    lateinit var mContext:Context
    lateinit var  view1:View
    lateinit var Imm:InputMethodManager
    val time= Calendar.getInstance().time
    var name=""
    var type= Image_type_list[0].name
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       view1 = inflater.inflate(R.layout.saving_fragment, container, false)

        OkButton=view1.findViewById(R.id.SavingSubmitt)
        ImageName=view1.findViewById(R.id.ImageName_txt)
        ImageType=view1.findViewById(R.id.ImageType_spinner)
        TypeAdapter=ImageType_Adapter(mContext, Image_type_list)
        ImageType.adapter=TypeAdapter

        ImageType.onItemSelectedListener=object : AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                type= Image_type_list[p2].name
            }

        }
        OkButton.setOnClickListener {
            name=ImageName.text.toString()
            Imm=mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            Imm.hideSoftInputFromWindow(ImageName.applicationWindowToken,0)
            if(name.isNullOrEmpty())
            {
                name="/newImge${time.time}"+".$type"
            }else
            {
                name=name+"."+type.toLowerCase()
            }
         mListener.onSaveSubmitted(name,type)
        }

        return view1
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as FragmentListener
        mContext=context
    }
}
