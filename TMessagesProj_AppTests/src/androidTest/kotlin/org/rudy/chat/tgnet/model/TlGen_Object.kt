package org.rudy.chat.tgnet.model

import org.rudy.chat.tgnet.OutputSerializedData

public interface TlGen_Object {
    fun serializeToStream(stream: OutputSerializedData)
}