package org.rudy.chat.tgnet.model.generated

import kotlin.String
import kotlin.UInt
import org.rudy.chat.tgnet.OutputSerializedData
import org.rudy.chat.tgnet.model.TlGen_Object
import org.rudy.chat.tgnet.model.TlGen_Vector

public sealed class TlGen_DataJSON : TlGen_Object {
  public data class TL_dataJSON(
    public val `data`: String,
  ) : TlGen_DataJSON() {
    public override fun serializeToStream(stream: OutputSerializedData) {
      stream.writeInt32(MAGIC.toInt())
      stream.writeString(data)
    }

    public companion object {
      public const val MAGIC: UInt = 0x7D748D04U
    }
  }
}
