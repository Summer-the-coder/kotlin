/*
 * Copyright 2010-2025 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.js.parser.antlr

import com.google.gwt.dev.js.rhino.CodePosition
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.tree.TerminalNode
import org.jetbrains.kotlin.js.backend.ast.JsDoubleLiteral
import org.jetbrains.kotlin.js.backend.ast.JsExpressionStatement
import org.jetbrains.kotlin.js.backend.ast.JsFunction
import org.jetbrains.kotlin.js.backend.ast.JsIntLiteral
import org.jetbrains.kotlin.js.backend.ast.JsLocation
import org.jetbrains.kotlin.js.backend.ast.JsNode
import org.jetbrains.kotlin.js.backend.ast.JsNumberLiteral
import org.jetbrains.kotlin.js.backend.ast.JsParameter
import org.jetbrains.kotlin.js.backend.ast.JsStringLiteral
import org.jetbrains.kotlin.js.backend.ast.JsVars
import org.jetbrains.kotlin.js.backend.ast.SourceInfoAwareJsNode
import org.jetbrains.kotlin.js.parser.antlr.generated.JavaScriptParser

internal val ParserRuleContext.startPosition: CodePosition
    get() = start.codePosition

internal val TerminalNode.startPosition: CodePosition
    get() = symbol.codePosition

internal val ParserRuleContext.stopPosition: CodePosition
    get() = stop.codePosition

internal val Token.codePosition: CodePosition
    get() = CodePosition(line, charPositionInLine)

internal fun unwrapStringLiteral(literalValue: String): String {
    literalValue.run {
        if (startsWith("'") && endsWith("'"))
            return removeSurrounding("'")

        if (startsWith("\"") && endsWith("\""))
            return removeSurrounding("\"")

        return this
    }
}

internal fun String.toStringLiteral(): JsStringLiteral {
    return JsStringLiteral(unwrapStringLiteral(this))
}

internal fun String.toDecimalLiteral(): JsNumberLiteral {
    val intValue = toIntOrNull()
    if (intValue != null)
        return JsIntLiteral(intValue)

    return JsDoubleLiteral(toDouble())
}

internal fun String.toHexLiteral(): JsNumberLiteral {
    val cleanHex = removePrefix("0x").removePrefix("0X")
    val longValue = cleanHex.toLong(16)

    return if (longValue in Int.MIN_VALUE..Int.MAX_VALUE)
        JsIntLiteral(longValue.toInt())
    else
        JsDoubleLiteral(longValue.toDouble())
}

internal fun String.toOctalLiteral(): JsNumberLiteral {
    val longValue = removePrefix("0").toLong(8)

    return if (longValue in Int.MIN_VALUE..Int.MAX_VALUE)
        JsIntLiteral(longValue.toInt())
    else
        JsDoubleLiteral(longValue.toDouble())
}