package my.Activities
import Extensions.FragmentListener
import Extensions.Screen_Size
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
class AddingTextFragment : Fragment() {
    lateinit var view1:View
    lateinit var Done_btn: ImageButton
    lateinit var Text_txt:EditText
    lateinit var mListener:FragmentListener
    lateinit var mContext: Context
    lateinit var Imm:InputMethodManager
     var mText:String?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        view1=inflater.inflate(R.layout.adding_text_fragment, container, false)
        view1.minimumHeight=Screen_Size.Height
        view1.minimumWidth=Screen_Size.Width

        Done_btn=view1.findViewById(R.id.Submit_text)
        Text_txt=view1.findViewById(R.id.text_txt)

        Done_btn.setOnClickListener {
            Imm=mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            Imm.hideSoftInputFromWindow(Text_txt.applicationWindowToken,0)
            mText = Text_txt.text.toString()
            mListener.onTextSubmittingClicked(mText)
        }
        return view1
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener=context as FragmentListener
        mContext=context
    }
}
