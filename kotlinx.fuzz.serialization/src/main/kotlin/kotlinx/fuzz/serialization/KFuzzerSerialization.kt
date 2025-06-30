package kotlinx.fuzz.serialization

import java.nio.charset.Charset
import kotlin.text.Charsets
import kotlinx.fuzz.KFuzzer
import kotlinx.fuzz.serialization.encoding.KFuzzerDecoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.serializer

val KFuzzer.serialization: KFuzzerSerialization
    get() = KFuzzerSerialization(fuzzer = this)

inline fun KFuzzer.serialization(
    block: KFuzzerSerialization.Builder.() -> Unit,
): KFuzzerSerialization {
    val serialization = KFuzzerSerialization(fuzzer = this)
    val builder = KFuzzerSerialization.Builder(serialization)
    builder.apply(block)
    return builder.build()
}

class KFuzzerSerialization(
    val fuzzer: KFuzzer,
    val serializersModule: SerializersModule = EmptySerializersModule(),
    val boolean: ((KFuzzer) -> Boolean)? = { fuzzer -> fuzzer.boolean() },
    val byte: ((KFuzzer) -> Byte)? = { fuzzer -> fuzzer.byte() },
    val short: ((KFuzzer) -> Short)? = { fuzzer -> fuzzer.short() },
    val char: ((KFuzzer) -> Char)? = { fuzzer -> fuzzer.char() },
    val int: ((KFuzzer) -> Int)? = { fuzzer -> fuzzer.int() },
    val long: ((KFuzzer) -> Long)? = { fuzzer -> fuzzer.long() },
    val float: ((KFuzzer) -> Float)? = { fuzzer -> fuzzer.float() },
    val double: ((KFuzzer) -> Double)? = { fuzzer -> fuzzer.double() },
    val string: ((KFuzzer) -> String)? = null,
    val collectionSize: ((KFuzzer) -> Int)? = null,
    val isNull: ((KFuzzer) -> Boolean)? = { fuzzer -> fuzzer.boolean() },
) {
    class Builder(serialization: KFuzzerSerialization) {
        var fuzzer: KFuzzer = serialization.fuzzer
        var serializersModule = serialization.serializersModule

        private var boolean: ((KFuzzer) -> Boolean)? = serialization.boolean
        fun boolean(block: ((KFuzzer) -> Boolean)?) {
            boolean = block
        }

        private var byte: ((KFuzzer) -> Byte)? = serialization.byte
        fun byte(block: ((KFuzzer) -> Byte)?) {
            byte = block
        }

        private var short: ((KFuzzer) -> Short)? = serialization.short
        fun short(block: ((KFuzzer) -> Short)?) {
            short = block
        }

        private var char: ((KFuzzer) -> Char)? = serialization.char
        fun char(block: ((KFuzzer) -> Char)?) {
            char = block
        }

        private var int: ((KFuzzer) -> Int)? = serialization.int
        fun int(block: ((KFuzzer) -> Int)?) {
            int = block
        }

        private var long: ((KFuzzer) -> Long)? = serialization.long
        fun long(block: ((KFuzzer) -> Long)?) {
            long = block
        }

        private var float: ((KFuzzer) -> Float)? = serialization.float
        fun float(block: ((KFuzzer) -> Float)?) {
            float = block
        }

        private var double: ((KFuzzer) -> Double)? = serialization.double
        fun double(block: ((KFuzzer) -> Double)?) {
            double = block
        }

        private var string: ((KFuzzer) -> String)? = serialization.string
        fun string(block: ((KFuzzer) -> String)?) {
            string = block
        }

        private var collectionSize: ((KFuzzer) -> Int)? = serialization.collectionSize
        fun collectionSize(block: ((KFuzzer) -> Int)?) {
            collectionSize = block
        }

        private var isNull: ((KFuzzer) -> Boolean)? = serialization.isNull
        fun isNull(block: ((KFuzzer) -> Boolean)?) {
            isNull = block
        }

        fun build(): KFuzzerSerialization {
            return KFuzzerSerialization(
                fuzzer = fuzzer,
                serializersModule = serializersModule,
                boolean = boolean,
                byte = byte,
                short = short,
                char = char,
                int = int,
                long = long,
                float = float,
                double = double,
                string = string,
                collectionSize = collectionSize,
                isNull = isNull,
            )
        }
    }
}

fun <T> KFuzzerSerialization.fuzz(serializer: DeserializationStrategy<T>): T {
    val decoder = KFuzzerDecoder(
        serialization = this,
        descriptor = serializer.descriptor,
    )
    return decoder.decodeSerializableValue(serializer)
}

inline fun <reified T> KFuzzerSerialization.fuzz(): T {
    return fuzz(serializersModule.serializer<T>())
}
