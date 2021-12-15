package Tools

import java.nio.IntBuffer
class Zooming {

    companion object {
       var newWidth:Int=0
        var newHeight:Int=0

        fun Zoom(mBuffer:IntBuffer, Width:Int,Height:Int): IntBuffer
        {
            val newWidth = Width * 2
            val newHeight = Height * 2

            Companion.newWidth =newWidth
            Companion.newHeight =newHeight
            //int capacity = newHeight * newWidth * 4;
            //ByteBuffer newbuffer= ByteBuffer.allocate(capacity);

            var newbuffer=IntBuffer.allocate(newHeight*newWidth)

            var n = 0
            var i = 0
            var j = 0
            var k = 0
            var rgb: Int


            var f = 0
            var r = 1
            while (i<Height)
            {

                while (j<Width)
                {
                    rgb=mBuffer.get(n++);

                    newbuffer.put(k++,rgb);
                    newbuffer.put(k++,rgb);


                    newbuffer.put(((r)* newWidth)+f ,rgb);
                    newbuffer.put(((r)* newWidth)+f +1,rgb);


                  f+=2

                    j++
                }
                 r+=2

                j=0
                f=0
                i++

                k+=newWidth;
            }


            return newbuffer
        }

        fun Zoomout(mBuffer: IntBuffer, Width: Int, Height: Int): IntBuffer {


            val newWidth = Width / 2
            val newHeight = Height / 2
            Companion.newWidth =newWidth
            Companion.newHeight =newHeight

            val newbuffer = IntBuffer.allocate(newHeight*newWidth)

            var n = 0
            var i = 0
            var j = 0

            var rgb: Int

            var index = 0

            while (i < Height) {

                while (j < Width) {
                    rgb = mBuffer.get(n)

                    n += 2
                    newbuffer.put(index++, rgb)
                    j += 2
                }
                j = 0

                i += 2
                n = i * Width
            }
            return newbuffer
        }

    }}
