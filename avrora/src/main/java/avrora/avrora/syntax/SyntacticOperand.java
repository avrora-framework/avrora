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

package avrora.avrora.syntax;

import avrora.avrora.arch.legacy.LegacyInstrProto;
import avrora.avrora.arch.legacy.LegacyOperand;
import avrora.avrora.arch.legacy.LegacyRegister;
import avrora.cck.parser.AbstractToken;
import avrora.cck.util.Util;

/**
 * The <code>SyntacticOperand</code> class is an implementation of the
 * <code>avrora.core.LegacyOperand</code> interface that corresponds to source
 * assembly programs. Therefore instances of this class contain Tokens that tie
 * them back to their original locations in the source assembly. This is useful
 * for reporting assembly errors when the prototype fails to build an
 * instruction because the operands do not meet the constraints specified in the
 * Atmel instruction set reference.
 *
 * @author Ben L. Titzer
 * @see LegacyOperand
 * @see LegacyInstrProto
 */
public abstract class SyntacticOperand extends ASTNode implements LegacyOperand
{

    public abstract void simplify(int nextByteAddress, Context c);

    protected final AbstractToken left;
    protected final AbstractToken right;


    protected SyntacticOperand(AbstractToken l, AbstractToken r)
    {
        left = l;
        right = r;
    }


    @Override
    public AbstractToken getLeftMostToken()
    {
        return left;
    }


    @Override
    public AbstractToken getRightMostToken()
    {
        return right;
    }


    @Override
    public LegacyOperand.Register asRegister()
    {
        return null;
    }


    @Override
    public LegacyOperand.Constant asConstant()
    {
        return null;
    }

    /**
     * The <code>SyntacticOperand.Register</code> class represents a register
     * operand at the source level. This may be an actual register name (e.g.
     * "r21") or it could be a symbolic name for a register that has been
     * renamed by an assembler directive.
     */
    public static class Register extends SyntacticOperand
            implements LegacyOperand.Register
    {

        public final AbstractToken name;
        private boolean simplified;
        private LegacyRegister register;


        public Register(AbstractToken n)
        {
            super(n, n);
            name = n;
        }


        @Override
        public LegacyOperand.Register asRegister()
        {
            return this;
        }


        @Override
        public LegacyRegister getRegister()
        {
            // sanity check to avoid possibly hard to find bugs in the future
            if (!simplified)
                throw Util.failure(
                        "register operand not yet simplified: " + name);
            return register;
        }


        @Override
        public void simplify(int currentByteAddress, Context c)
        {
            register = c.getRegister(name);
            simplified = true;
        }


        @Override
        public String toString()
        {
            return "reg:" + name.image;
        }

    }

    /**
     * The <code>SyntacticOperand.Expr</code> class represents a constant
     * expression that was specified in the source assembly as an expression.
     * This expression might be compound and need to be evaluated before its
     * actual value is known.
     */
    public static class Expr extends SyntacticOperand
            implements LegacyOperand.Constant
    {

        public final avrora.avrora.syntax.Expr expr;
        private boolean simplified;
        private boolean useByteAddress;
        private int value;


        public Expr(avrora.avrora.syntax.Expr e, boolean b)
        {
            super(e.getLeftMostToken(), e.getRightMostToken());
            expr = e;
            useByteAddress = b;
        }


        @Override
        public LegacyOperand.Constant asConstant()
        {
            return this;
        }


        @Override
        public int getValue()
        {
            // sanity check to avoid possibly hard to find bugs in the future
            if (!simplified)
                throw Util.failure(
                        "expression operand not yet simplified: " + expr);
            return value;
        }


        @Override
        public int getValueAsWord()
        {
            if (!simplified)
                throw Util.failure(
                        "expression operand not yet simplified: " + expr);
            if (useByteAddress)
            {
                return (value >> 1);
            } else
                return value;
        }


        @Override
        public void simplify(int nextByteAddress, Context c)
        {
            value = expr.evaluate(nextByteAddress, c);
            simplified = true;
        }


        @Override
        public String toString()
        {
            return "expr:" + expr;
        }
    }
}
