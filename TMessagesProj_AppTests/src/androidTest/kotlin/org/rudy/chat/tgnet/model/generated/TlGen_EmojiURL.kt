package org.rudy.chat.tgnet.model.generated

import kotlin.String
import kotlin.UInt
import org.rudy.chat.tgnet.OutputSerializedData
import org.rudy.chat.tgnet.model.TlGen_Object
import org.rudy.chat.tgnet.model.TlGen_Vector

public sealed class TlGen_EmojiURL : TlGen_Object {
  public data class TL_emojiURL(
    public val url: String,
  ) : TlGen_EmojiURL() {
    public override fun serializeToStream(stream: OutputSerializedData) {
      stream.writeInt32(MAGIC.toInt())
      stream.writeString(url)
    }

    public companion object {
      public const val MAGIC: UInt = 0xA575739DU
    }
  }
}
