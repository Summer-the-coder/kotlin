/*
 * Copyright 2010-2025 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.js.parser.antlr

import com.google.gwt.dev.js.rhino.ErrorReporter
import org.antlr.v4.runtime.ParserRuleContext
import org.jetbrains.kotlin.js.parser.antlr.generated.JavaScriptParser

class AntlrJsValidationVisitor(private val reporter: ErrorReporter) : AntlrJsBaseVisitor<ParserRuleContext?>() {
    override fun visitAssignmentExpression(ctx: JavaScriptParser.AssignmentExpressionContext): ParserRuleContext? {
        if (ctx.lhs is JavaScriptParser.ThisExpressionContext)
            reporter.error("Invalid assignment left-hand side.", ctx.lhs.startPosition, ctx.lhs.stopPosition)
        return super.visitAssignmentExpression(ctx)
    }

    override fun visitAssignmentOperatorExpression(ctx: JavaScriptParser.AssignmentOperatorExpressionContext): ParserRuleContext? {
        if (ctx.lhs is JavaScriptParser.ThisExpressionContext)
            reporter.error("Invalid assignment left-hand side.", ctx.lhs.startPosition, ctx.lhs.stopPosition)
        return super.visitAssignmentOperatorExpression(ctx)
    }
}

