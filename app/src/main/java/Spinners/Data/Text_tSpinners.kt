package Spinners.Data

import android.graphics.Typeface

data class typeitem(val type:Typeface)

val textType_Array= arrayOf("Ink Free","Blackadder ITC","Broadway","Jokerman","Forte","Arial (Body CS)")


data class sizeitem(val value:String)
val text_size_Array= arrayOf(
    "40", "35","30","25"
             ,"20","15","10")

val text_type_Array_Data= arrayOf(typeitem(Typeface.DEFAULT),typeitem(Typeface.DEFAULT_BOLD)
           , typeitem(Typeface.MONOSPACE), typeitem(Typeface.SANS_SERIF)
            , typeitem(Typeface.SERIF)
    )

  val text_type_Array_names= arrayOf("DEFAULT","DEFAULT_BOLD","MONOSPACE"
                                                       ,"SANS_SERIF" ,"SERIF")