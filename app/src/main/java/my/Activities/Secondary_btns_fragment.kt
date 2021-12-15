package my.Activities

import Extensions.FragmentListener
import Extensions.Secondary_buttons
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class Secondary_btns_fragment : Fragment() {

    lateinit var close_button:ImageButton
    lateinit var ok_button:ImageButton
    lateinit var mLstener:FragmentListener

lateinit var view1:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view1=inflater.inflate(R.layout.secondary_btns_fragment, container, false)

        close_button=view1.findViewById(R.id.close_button)
        ok_button=view1.findViewById(R.id.check_ok_button)


        ok_button.setOnClickListener {


            mLstener.onSecondaryButtonClicked(Secondary_buttons.ok_button)
        }

        close_button.setOnClickListener {
            mLstener.onSecondaryButtonClicked(Secondary_buttons.close_button)
        }
        return view1
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mLstener=context as FragmentListener
    }

    fun getview():View{
    return this.view1
}

}
