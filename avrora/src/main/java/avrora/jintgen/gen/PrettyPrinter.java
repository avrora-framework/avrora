/**
 * Copyright (c) 2004-2005, Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the University of California, Los Angeles nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package avrora.jintgen.gen;

import java.util.List;

import avrora.cck.text.Printer;
import avrora.jintgen.isdl.parser.Token;
import avrora.jintgen.jigir.AssignStmt;
import avrora.jintgen.jigir.BinOpExpr;
import avrora.jintgen.jigir.CallExpr;
import avrora.jintgen.jigir.CallStmt;
import avrora.jintgen.jigir.CodeVisitor;
import avrora.jintgen.jigir.CommentStmt;
import avrora.jintgen.jigir.ConversionExpr;
import avrora.jintgen.jigir.DeclStmt;
import avrora.jintgen.jigir.DotExpr;
import avrora.jintgen.jigir.Expr;
import avrora.jintgen.jigir.FixedRangeExpr;
import avrora.jintgen.jigir.IfStmt;
import avrora.jintgen.jigir.IndexExpr;
import avrora.jintgen.jigir.Literal;
import avrora.jintgen.jigir.ReadExpr;
import avrora.jintgen.jigir.ReturnStmt;
import avrora.jintgen.jigir.Stmt;
import avrora.jintgen.jigir.StmtVisitor;
import avrora.jintgen.jigir.UnOpExpr;
import avrora.jintgen.jigir.VarExpr;
import avrora.jintgen.jigir.WriteStmt;
import avrora.jintgen.types.TypeRef;

/**
 * @author Ben L. Titzer
 */
public class PrettyPrinter implements StmtVisitor, CodeVisitor
{
    protected final Printer printer;


    @Override
    public void visit(CallExpr e)
    {
        printer.print(getMethod(e.method.image) + '(');
        visitExprList(e.args);
        printer.print(")");
    }


    @Override
    public void visit(ReadExpr e)
    {
        printer.print("read");
        if (e.typeRef != null)
            printer.print(" : " + renderType(e.typeRef));
        printer.print("(");
        printer.print(getVariable(e.operand));
        printer.print(")");
    }


    @Override
    public void visit(ConversionExpr e)
    {
        inner(e.expr, Expr.PREC_TERM);
        printer.print(':' + renderType(e.typeRef));
    }


    @Override
    public void visit(VarExpr e)
    {
        printer.print(getVariable(e.variable));
    }


    @Override
    public void visit(DotExpr e)
    {
        e.expr.accept(this);
        printer.print(".");
        printer.print(e.field.image);
    }


    @Override
    public void visit(AssignStmt s)
    {
        s.dest.accept(this);
        printer.print(" = ");
        s.expr.accept(this);
        printer.println(";");
    }


    @Override
    public void visit(AssignStmt.Var s)
    {
        s.dest.accept(this);
        printer.print(" = ");
        s.expr.accept(this);
        printer.println(";");
    }


    @Override
    public void visit(AssignStmt.Map s)
    {
        s.map.accept(this);
        printer.print("[");
        s.index.accept(this);
        printer.print("] = ");
        s.expr.accept(this);
        printer.println(";");
    }


    @Override
    public void visit(AssignStmt.Bit s)
    {
        s.dest.accept(this);
        printer.print("[");
        s.bit.accept(this);
        printer.print("] = ");
        s.expr.accept(this);
        printer.println(";");
    }


    @Override
    public void visit(AssignStmt.FixedRange s)
    {
        s.dest.accept(this);
        printer.print("[" + s.high_bit + ':' + s.low_bit + "] = ");
        s.expr.accept(this);
        printer.println(";");
    }


    protected String getMethod(String s)
    {
        return s;
    }


    public PrettyPrinter(Printer p)
    {
        printer = p;
    }


    @Override
    public void visit(CallStmt s)
    {
        printer.print(getMethod(s.method.image) + '(');
        visitExprList(s.args);
        printer.println(");");
    }


    @Override
    public void visit(WriteStmt s)
    {
        printer.print("write");
        if (s.typeRef != null)
            printer.print(" : " + renderType(s.typeRef));
        printer.print("(");
        printer.print(getVariable(s.operand));
        printer.print(", ");
        s.expr.accept(this);
        printer.println(");");
    }


    @Override
    public void visit(CommentStmt s)
    {
        printer.println(s.toString());
    }


    @Override
    public void visit(IfStmt s)
    {
        printer.print("if ( ");
        s.cond.accept(this);
        printer.print(" ) ");
        printer.startblock();
        visitStmtList(s.trueBranch);
        printer.endblock();
        if (!s.falseBranch.isEmpty())
        {
            printer.startblock("else");
            visitStmtList(s.falseBranch);
            printer.endblock();
        }
    }


    @Override
    public void visit(ReturnStmt s)
    {
        printer.print("return ");
        s.expr.accept(this);
        printer.println(";");
    }


    protected String getVariable(Token v)
    {
        return v.toString();
    }


    @Override
    public void visit(DeclStmt s)
    {
        printer.print(
                renderType(s.typeRef) + ' ' + getVariable(s.name) + " = ");
        s.init.accept(this);
        printer.println(";");
    }


    protected void inner(Expr e, int outerPrecedence)
    {
        int inprec = e.getPrecedence();
        if (inprec == -1 || outerPrecedence == -1 || inprec < outerPrecedence)
        {
            printer.print("(");
            e.accept(this);
            printer.print(")");
        } else
        {
            e.accept(this);
        }
    }


    protected void binop(String op, Expr left, Expr right, int p)
    {
        inner(left, p);
        printer.print(' ' + op + ' ');
        inner(right, p);
    }


    @Override
    public void visit(BinOpExpr e)
    {
        binop(e.operation.image, e.left, e.right, e.getPrecedence());
    }


    @Override
    public void visit(Literal.BoolExpr e)
    {
        printer.print(e.toString());
    }


    @Override
    public void visit(Literal.IntExpr e)
    {
        printer.print(e.toString());
    }


    @Override
    public void visit(Literal.EnumVal e)
    {
        printer.print(e.toString());
    }


    @Override
    public void visit(UnOpExpr e)
    {
        printer.print(e.operation.image);
        inner(e.expr, e.getPrecedence());
    }


    public void visitStmtList(List<Stmt> l)
    {
        for (Stmt t : l)
        {
            t.accept(this);
        }
    }


    @Override
    public void visit(IndexExpr e)
    {
        e.expr.accept(this);
        printer.print("[");
        e.index.accept(this);
        printer.print("]");
    }


    @Override
    public void visit(FixedRangeExpr e)
    {
        inner(e.expr, e.getPrecedence());
        printer.print("[" + e.high_bit + ':' + e.low_bit + ']');
    }


    protected void visitExprList(List<Expr> l)
    {
        int cntr = 0;
        for (Expr a : l)
        {
            if (cntr++ != 0)
                printer.print(", ");
            a.accept(this);
        }
    }


    protected String renderType(TypeRef tr)
    {
        return tr.toString();
    }

}
