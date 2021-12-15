package Extensions

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast

class GestureListener(val mContext: Context) :GestureDetector.OnDoubleTapListener,GestureDetector.OnGestureListener
{
    val mListener:FragmentListener
    init {
        mListener = mContext as FragmentListener
    }
    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {


        return true
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return true
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return true
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        //mListener.onZoomButtonClicked(ZoomButtons.Zoom_In)
        //Toast.makeText(mContext,"Double",Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {

        return true
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {

        return true
    }

}