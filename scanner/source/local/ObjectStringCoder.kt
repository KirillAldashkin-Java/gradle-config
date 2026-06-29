package local

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.io.Serializable
import java.util.zip.Deflater
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.io.encoding.Base64

fun serializeString(value: Serializable): String {
    ByteArrayOutputStream().use { bytes ->
        GZIPOutputStream(bytes).use { gzip ->
            ObjectOutputStream(gzip).use { obj ->
                obj.writeObject(value)
            }
        }
        return Base64.encode(bytes.toByteArray())
    }
}

fun deserializeString(str: String): Any {
    ByteArrayInputStream(Base64.decode(str)).use { bytes ->
        GZIPInputStream(bytes).use { gzip ->
            ObjectInputStream(gzip).use { obj ->
                return obj.readObject()
            }
        }
    }
}

private class GZipEncoder(out: OutputStream) : GZIPOutputStream(out) {
    init {
        def.setLevel(Deflater.BEST_COMPRESSION)
    }
}
