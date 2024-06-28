package ru.bgitu.core.datastore.util

import com.google.protobuf.BoolValue
import com.google.protobuf.Int32Value
import com.google.protobuf.Int64Value
import com.google.protobuf.StringValue

fun String.toProtobuf(): StringValue {
    return StringValue.of(this)
}

fun Int.toProtobuf(): Int32Value {
    return Int32Value.of(this)
}

inline fun <reified T> com.google.protobuf.GeneratedMessageLite<*,*>.toKotlinType(): T {
    return when (this) {
        is StringValue -> value as T
        is Int32Value -> value as T
        is Int64Value -> value as T
        is BoolValue -> value as T
        else -> error("Not primitive type")
    }
}