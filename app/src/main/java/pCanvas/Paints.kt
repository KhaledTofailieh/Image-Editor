package pCanvas

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

   class mtextPaint:Paint{
      constructor():super()
    {
        textSize=30f
        color=Color.RED
        typeface= Typeface.DEFAULT_BOLD
        Typeface.create(Typeface.DEFAULT,Typeface.BOLD)

    }

}