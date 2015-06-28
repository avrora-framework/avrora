package avrora.avrora.arch.avr;

import avrora.avrora.arch.AbstractArchitecture;
import avrora.avrora.arch.AbstractInstr;

/**
 * The <code>AVRInstr</code> class is a container (almost a namespace) for all
 * of the instructions in this architecture. Each inner class represents an
 * instruction in the architecture and also extends the outer class.
 */
public abstract class AVRInstr implements AbstractInstr
{

    /**
     * The <code>accept()</code> method accepts an instruction visitor and calls
     * the appropriate <code>visit()</code> method for this instruction.
     *
     * @param v
     *            the instruction visitor to accept
     */
    public abstract void accept(AVRInstrVisitor v);


    /**
     * The <code>accept()</code> method accepts an addressing mode visitor and
     * calls the appropriate <code>visit_*()</code> method for this
     * instruction's addressing mode.
     *
     * @param v
     *            the addressing mode visitor to accept
     */
    public void accept(AVRAddrModeVisitor v)
    {
        // the default implementation of accept() is empty
    }


    /**
     * The <code>toString()</code> method converts this instruction to a string
     * representation. For instructions with operands, this method will render
     * the operands in the appropriate syntax as declared in the architecture
     * description.
     *
     * @return a string representation of this instruction
     */
    @Override
    public String toString()
    {
        // the default implementation of toString() simply returns the name
        return name;
    }

    /**
     * The <code>name</code> field stores a reference to the name of the
     * instruction as a string.
     */
    public final String name;

    /**
     * The <code>size</code> field stores the size of the instruction in bytes.
     */
    public final int size;


    /**
     * The <code>getSize()</code> method returns the size of this instruction in
     * bytes.
     */
    @Override
    public int getSize()
    {
        return size;
    }


    /**
     * The <code>getName()</code> method returns the name of this instruction.
     */
    @Override
    public String getName()
    {
        return name;
    }


    /**
     * The <code>getArchitecture()</code> method returns the architecture of
     * this instruction.
     */
    @Override
    public AbstractArchitecture getArchitecture()
    {
        return null;
    }


    /**
     * The default constructor for the <code>AVRInstr</code> class accepts a
     * string name and a size for each instruction.
     *
     * @param name
     *            the string name of the instruction
     * @param size
     *            the size of the instruction in bytes
     */
    protected AVRInstr(String name, int size)
    {
        this.name = name;
        this.size = size;
    }

    public abstract static class BRANCH_Instr extends AVRInstr
    {

        public final AVROperand.SREL target;


        protected BRANCH_Instr(String name, int size, AVRAddrMode.BRANCH am)
        {
            super(name, size);
            this.target = am.target;
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_BRANCH(this, target);
        }


        @Override
        public String toString()
        {
            return name + ' ' + target;
        }
    }

    public abstract static class GPRGPR_Instr extends AVRInstr
    {

        public final AVROperand.op_GPR rd;
        public final AVROperand.op_GPR rr;


        protected GPRGPR_Instr(String name, int size, AVRAddrMode.GPRGPR am)
        {
            super(name, size);
            this.rd = am.rd;
            this.rr = am.rr;
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_GPRGPR(this, rd, rr);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + rr;
        }
    }

    public abstract static class GPR_Instr extends AVRInstr
    {

        public final AVROperand.op_GPR rd;


        protected GPR_Instr(String name, int size, AVRAddrMode.GPR am)
        {
            super(name, size);
            this.rd = am.rd;
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_GPR(this, rd);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd;
        }
    }

    public abstract static class HGPRIMM8_Instr extends AVRInstr
    {

        public final AVROperand.op_HGPR rd;
        public final AVROperand.IMM8 imm;


        protected HGPRIMM8_Instr(String name, int size, AVRAddrMode.HGPRIMM8 am)
        {
            super(name, size);
            this.rd = am.rd;
            this.imm = am.imm;
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_HGPRIMM8(this, rd, imm);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + imm;
        }
    }

    public abstract static class LD_ST_Instr extends AVRInstr
    {

        public final AVRAddrMode.LD_ST am;
        public final AVROperand rd;
        public final AVROperand ar;


        protected LD_ST_Instr(String name, int size, AVRAddrMode.LD_ST am)
        {
            super(name, size);
            this.am = am;
            this.rd = am.get_rd();
            this.ar = am.get_ar();
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            am.accept(this, v);
        }


        @Override
        public String toString()
        {
            return name + am.toString();
        }
    }

    public abstract static class XLPM_Instr extends AVRInstr
    {

        public final AVRAddrMode.XLPM am;
        public final AVROperand source;
        public final AVROperand dest;


        protected XLPM_Instr(String name, int size, AVRAddrMode.XLPM am)
        {
            super(name, size);
            this.am = am;
            this.source = am.get_source();
            this.dest = am.get_dest();
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            am.accept(this, v);
        }


        @Override
        public String toString()
        {
            return name + am.toString();
        }
    }

    public static class ADC extends GPRGPR_Instr
    {

        ADC(int size, AVRAddrMode.GPRGPR am)
        {
            super("adc", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class ADD extends GPRGPR_Instr
    {

        ADD(int size, AVRAddrMode.GPRGPR am)
        {
            super("add", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class ADIW extends AVRInstr
    {

        public final AVROperand.op_RDL rd;
        public final AVROperand.IMM6 imm;


        ADIW(int size, AVRAddrMode.$adiw$ am)
        {
            super("adiw", size);
            this.rd = am.rd;
            this.imm = am.imm;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$adiw$(this, rd, imm);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + imm;
        }
    }

    public static class AND extends GPRGPR_Instr
    {

        AND(int size, AVRAddrMode.GPRGPR am)
        {
            super("and", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class ANDI extends HGPRIMM8_Instr
    {

        ANDI(int size, AVRAddrMode.HGPRIMM8 am)
        {
            super("andi", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class ASR extends GPR_Instr
    {

        ASR(int size, AVRAddrMode.GPR am)
        {
            super("asr", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BCLR extends AVRInstr
    {

        public final AVROperand.IMM3 bit;


        BCLR(int size, AVRAddrMode.$bclr$ am)
        {
            super("bclr", size);
            this.bit = am.bit;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$bclr$(this, bit);
        }


        @Override
        public String toString()
        {
            return name + ' ' + bit;
        }
    }

    public static class BLD extends AVRInstr
    {

        public final AVROperand.op_GPR rr;
        public final AVROperand.IMM3 bit;


        BLD(int size, AVRAddrMode.$bld$ am)
        {
            super("bld", size);
            this.rr = am.rr;
            this.bit = am.bit;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$bld$(this, rr, bit);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rr + ", " + bit;
        }
    }

    public static class BRBC extends AVRInstr
    {

        public final AVROperand.IMM3 bit;
        public final AVROperand.SREL target;


        BRBC(int size, AVRAddrMode.$brbc$ am)
        {
            super("brbc", size);
            this.bit = am.bit;
            this.target = am.target;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$brbc$(this, bit, target);
        }


        @Override
        public String toString()
        {
            return name + ' ' + bit + ", " + target;
        }
    }

    public static class BRBS extends AVRInstr
    {

        public final AVROperand.IMM3 bit;
        public final AVROperand.SREL target;


        BRBS(int size, AVRAddrMode.$brbs$ am)
        {
            super("brbs", size);
            this.bit = am.bit;
            this.target = am.target;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$brbs$(this, bit, target);
        }


        @Override
        public String toString()
        {
            return name + ' ' + bit + ", " + target;
        }
    }

    public static class BRCC extends BRANCH_Instr
    {

        BRCC(int size, AVRAddrMode.BRANCH am)
        {
            super("brcc", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRCS extends BRANCH_Instr
    {

        BRCS(int size, AVRAddrMode.BRANCH am)
        {
            super("brcs", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BREAK extends AVRInstr
    {

        BREAK(int size)
        {
            super("break", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class BREQ extends BRANCH_Instr
    {

        BREQ(int size, AVRAddrMode.BRANCH am)
        {
            super("breq", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRGE extends BRANCH_Instr
    {

        BRGE(int size, AVRAddrMode.BRANCH am)
        {
            super("brge", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRHC extends BRANCH_Instr
    {

        BRHC(int size, AVRAddrMode.BRANCH am)
        {
            super("brhc", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRHS extends BRANCH_Instr
    {

        BRHS(int size, AVRAddrMode.BRANCH am)
        {
            super("brhs", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRID extends BRANCH_Instr
    {

        BRID(int size, AVRAddrMode.BRANCH am)
        {
            super("brid", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRIE extends BRANCH_Instr
    {

        BRIE(int size, AVRAddrMode.BRANCH am)
        {
            super("brie", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRLO extends BRANCH_Instr
    {

        BRLO(int size, AVRAddrMode.BRANCH am)
        {
            super("brlo", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRLT extends BRANCH_Instr
    {

        BRLT(int size, AVRAddrMode.BRANCH am)
        {
            super("brlt", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRMI extends BRANCH_Instr
    {

        BRMI(int size, AVRAddrMode.BRANCH am)
        {
            super("brmi", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRNE extends BRANCH_Instr
    {

        BRNE(int size, AVRAddrMode.BRANCH am)
        {
            super("brne", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRPL extends BRANCH_Instr
    {

        BRPL(int size, AVRAddrMode.BRANCH am)
        {
            super("brpl", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRSH extends BRANCH_Instr
    {

        BRSH(int size, AVRAddrMode.BRANCH am)
        {
            super("brsh", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRTC extends BRANCH_Instr
    {

        BRTC(int size, AVRAddrMode.BRANCH am)
        {
            super("brtc", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRTS extends BRANCH_Instr
    {

        BRTS(int size, AVRAddrMode.BRANCH am)
        {
            super("brts", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRVC extends BRANCH_Instr
    {

        BRVC(int size, AVRAddrMode.BRANCH am)
        {
            super("brvc", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BRVS extends BRANCH_Instr
    {

        BRVS(int size, AVRAddrMode.BRANCH am)
        {
            super("brvs", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class BSET extends AVRInstr
    {

        public final AVROperand.IMM3 bit;


        BSET(int size, AVRAddrMode.$bset$ am)
        {
            super("bset", size);
            this.bit = am.bit;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$bset$(this, bit);
        }


        @Override
        public String toString()
        {
            return name + ' ' + bit;
        }
    }

    public static class BST extends AVRInstr
    {

        public final AVROperand.op_GPR rr;
        public final AVROperand.IMM3 bit;


        BST(int size, AVRAddrMode.$bst$ am)
        {
            super("bst", size);
            this.rr = am.rr;
            this.bit = am.bit;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$bst$(this, rr, bit);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rr + ", " + bit;
        }
    }

    public static class CALL extends AVRInstr
    {

        public final AVROperand.PADDR target;


        CALL(int size, AVRAddrMode.$call$ am)
        {
            super("call", size);
            this.target = am.target;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$call$(this, target);
        }


        @Override
        public String toString()
        {
            return name + ' ' + target;
        }
    }

    public static class CBI extends AVRInstr
    {

        public final AVROperand.IMM5 ior;
        public final AVROperand.IMM3 bit;


        CBI(int size, AVRAddrMode.$cbi$ am)
        {
            super("cbi", size);
            this.ior = am.ior;
            this.bit = am.bit;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$cbi$(this, ior, bit);
        }


        @Override
        public String toString()
        {
            return name + ' ' + ior + ", " + bit;
        }
    }

    public static class CBR extends HGPRIMM8_Instr
    {

        CBR(int size, AVRAddrMode.HGPRIMM8 am)
        {
            super("cbr", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class CLC extends AVRInstr
    {

        CLC(int size)
        {
            super("clc", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class CLH extends AVRInstr
    {

        CLH(int size)
        {
            super("clh", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class CLI extends AVRInstr
    {

        CLI(int size)
        {
            super("cli", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class CLN extends AVRInstr
    {

        CLN(int size)
        {
            super("cln", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class CLR extends AVRInstr
    {

        public final AVROperand.op_GPR rd;


        CLR(int size, AVRAddrMode.$clr$ am)
        {
            super("clr", size);
            this.rd = am.rd;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$clr$(this, rd);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd;
        }
    }

    public static class CLS extends AVRInstr
    {

        CLS(int size)
        {
            super("cls", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class CLT extends AVRInstr
    {

        CLT(int size)
        {
            super("clt", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class CLV extends AVRInstr
    {

        CLV(int size)
        {
            super("clv", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class CLZ extends AVRInstr
    {

        CLZ(int size)
        {
            super("clz", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class COM extends GPR_Instr
    {

        COM(int size, AVRAddrMode.GPR am)
        {
            super("com", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class CP extends GPRGPR_Instr
    {

        CP(int size, AVRAddrMode.GPRGPR am)
        {
            super("cp", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class CPC extends GPRGPR_Instr
    {

        CPC(int size, AVRAddrMode.GPRGPR am)
        {
            super("cpc", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class CPI extends HGPRIMM8_Instr
    {

        CPI(int size, AVRAddrMode.HGPRIMM8 am)
        {
            super("cpi", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class CPSE extends GPRGPR_Instr
    {

        CPSE(int size, AVRAddrMode.GPRGPR am)
        {
            super("cpse", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class DEC extends GPR_Instr
    {

        DEC(int size, AVRAddrMode.GPR am)
        {
            super("dec", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class EICALL extends AVRInstr
    {

        EICALL(int size)
        {
            super("eicall", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class EIJMP extends AVRInstr
    {

        EIJMP(int size)
        {
            super("eijmp", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class EOR extends GPRGPR_Instr
    {

        EOR(int size, AVRAddrMode.GPRGPR am)
        {
            super("eor", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class FMUL extends AVRInstr
    {

        public final AVROperand.op_MGPR rd;
        public final AVROperand.op_MGPR rr;


        FMUL(int size, AVRAddrMode.$fmul$ am)
        {
            super("fmul", size);
            this.rd = am.rd;
            this.rr = am.rr;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$fmul$(this, rd, rr);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + rr;
        }
    }

    public static class FMULS extends AVRInstr
    {

        public final AVROperand.op_MGPR rd;
        public final AVROperand.op_MGPR rr;


        FMULS(int size, AVRAddrMode.$fmuls$ am)
        {
            super("fmuls", size);
            this.rd = am.rd;
            this.rr = am.rr;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$fmuls$(this, rd, rr);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + rr;
        }
    }

    public static class FMULSU extends AVRInstr
    {

        public final AVROperand.op_MGPR rd;
        public final AVROperand.op_MGPR rr;


        FMULSU(int size, AVRAddrMode.$fmulsu$ am)
        {
            super("fmulsu", size);
            this.rd = am.rd;
            this.rr = am.rr;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$fmulsu$(this, rd, rr);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + rr;
        }
    }

    public static class ICALL extends AVRInstr
    {

        ICALL(int size)
        {
            super("icall", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class IJMP extends AVRInstr
    {

        IJMP(int size)
        {
            super("ijmp", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class IN extends AVRInstr
    {

        public final AVROperand.op_GPR rd;
        public final AVROperand.IMM6 imm;


        IN(int size, AVRAddrMode.$in$ am)
        {
            super("in", size);
            this.rd = am.rd;
            this.imm = am.imm;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$in$(this, rd, imm);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + imm;
        }
    }

    public static class INC extends GPR_Instr
    {

        INC(int size, AVRAddrMode.GPR am)
        {
            super("inc", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class JMP extends AVRInstr
    {

        public final AVROperand.PADDR target;


        JMP(int size, AVRAddrMode.$jmp$ am)
        {
            super("jmp", size);
            this.target = am.target;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$jmp$(this, target);
        }


        @Override
        public String toString()
        {
            return name + ' ' + target;
        }
    }

    public static class LDD extends AVRInstr
    {

        public final AVROperand.op_GPR rd;
        public final AVROperand.op_YZ ar;
        public final AVROperand.IMM6 imm;


        LDD(int size, AVRAddrMode.$ldd$ am)
        {
            super("ldd", size);
            this.rd = am.rd;
            this.ar = am.ar;
            this.imm = am.imm;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$ldd$(this, rd, ar, imm);
        }


        @Override
        public String toString()
        {
            return name + " " + rd + ", " + ar + "+" + imm;
        }
    }

    public static class LDI extends HGPRIMM8_Instr
    {

        LDI(int size, AVRAddrMode.HGPRIMM8 am)
        {
            super("ldi", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class LDS extends AVRInstr
    {

        public final AVROperand.op_GPR rd;
        public final AVROperand.DADDR addr;


        LDS(int size, AVRAddrMode.$lds$ am)
        {
            super("lds", size);
            this.rd = am.rd;
            this.addr = am.addr;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$lds$(this, rd, addr);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + addr;
        }
    }

    public static class LSL extends AVRInstr
    {

        public final AVROperand.op_GPR rd;


        LSL(int size, AVRAddrMode.$lsl$ am)
        {
            super("lsl", size);
            this.rd = am.rd;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$lsl$(this, rd);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd;
        }
    }

    public static class LSR extends GPR_Instr
    {

        LSR(int size, AVRAddrMode.GPR am)
        {
            super("lsr", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class MOV extends GPRGPR_Instr
    {

        MOV(int size, AVRAddrMode.GPRGPR am)
        {
            super("mov", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class MOVW extends AVRInstr
    {

        public final AVROperand.op_EGPR rd;
        public final AVROperand.op_EGPR rr;


        MOVW(int size, AVRAddrMode.$movw$ am)
        {
            super("movw", size);
            this.rd = am.rd;
            this.rr = am.rr;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$movw$(this, rd, rr);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + rr;
        }
    }

    public static class MUL extends GPRGPR_Instr
    {

        MUL(int size, AVRAddrMode.GPRGPR am)
        {
            super("mul", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class MULS extends AVRInstr
    {

        public final AVROperand.op_HGPR rd;
        public final AVROperand.op_HGPR rr;


        MULS(int size, AVRAddrMode.$muls$ am)
        {
            super("muls", size);
            this.rd = am.rd;
            this.rr = am.rr;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$muls$(this, rd, rr);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + rr;
        }
    }

    public static class MULSU extends AVRInstr
    {

        public final AVROperand.op_MGPR rd;
        public final AVROperand.op_MGPR rr;


        MULSU(int size, AVRAddrMode.$mulsu$ am)
        {
            super("mulsu", size);
            this.rd = am.rd;
            this.rr = am.rr;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$mulsu$(this, rd, rr);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + rr;
        }
    }

    public static class NEG extends GPR_Instr
    {

        NEG(int size, AVRAddrMode.GPR am)
        {
            super("neg", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class NOP extends AVRInstr
    {

        NOP(int size)
        {
            super("nop", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class OR extends GPRGPR_Instr
    {

        OR(int size, AVRAddrMode.GPRGPR am)
        {
            super("or", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class ORI extends HGPRIMM8_Instr
    {

        ORI(int size, AVRAddrMode.HGPRIMM8 am)
        {
            super("ori", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class OUT extends AVRInstr
    {

        public final AVROperand.IMM6 ior;
        public final AVROperand.op_GPR rr;


        OUT(int size, AVRAddrMode.$out$ am)
        {
            super("out", size);
            this.ior = am.ior;
            this.rr = am.rr;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$out$(this, ior, rr);
        }


        @Override
        public String toString()
        {
            return name + ' ' + ior + ", " + rr;
        }
    }

    public static class POP extends GPR_Instr
    {

        POP(int size, AVRAddrMode.GPR am)
        {
            super("pop", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class PUSH extends GPR_Instr
    {

        PUSH(int size, AVRAddrMode.GPR am)
        {
            super("push", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class RCALL extends AVRInstr
    {

        public final AVROperand.LREL target;


        RCALL(int size, AVRAddrMode.$rcall$ am)
        {
            super("rcall", size);
            this.target = am.target;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$rcall$(this, target);
        }


        @Override
        public String toString()
        {
            return name + ' ' + target;
        }
    }

    public static class RET extends AVRInstr
    {

        RET(int size)
        {
            super("ret", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class RETI extends AVRInstr
    {

        RETI(int size)
        {
            super("reti", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class RJMP extends AVRInstr
    {

        public final AVROperand.LREL target;


        RJMP(int size, AVRAddrMode.$rjmp$ am)
        {
            super("rjmp", size);
            this.target = am.target;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$rjmp$(this, target);
        }


        @Override
        public String toString()
        {
            return name + ' ' + target;
        }
    }

    public static class ROL extends AVRInstr
    {

        public final AVROperand.op_GPR rd;


        ROL(int size, AVRAddrMode.$rol$ am)
        {
            super("rol", size);
            this.rd = am.rd;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$rol$(this, rd);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd;
        }
    }

    public static class ROR extends GPR_Instr
    {

        ROR(int size, AVRAddrMode.GPR am)
        {
            super("ror", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class SBC extends GPRGPR_Instr
    {

        SBC(int size, AVRAddrMode.GPRGPR am)
        {
            super("sbc", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class SBCI extends HGPRIMM8_Instr
    {

        SBCI(int size, AVRAddrMode.HGPRIMM8 am)
        {
            super("sbci", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class SBI extends AVRInstr
    {

        public final AVROperand.IMM5 ior;
        public final AVROperand.IMM3 bit;


        SBI(int size, AVRAddrMode.$sbi$ am)
        {
            super("sbi", size);
            this.ior = am.ior;
            this.bit = am.bit;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$sbi$(this, ior, bit);
        }


        @Override
        public String toString()
        {
            return name + ' ' + ior + ", " + bit;
        }
    }

    public static class SBIC extends AVRInstr
    {

        public final AVROperand.IMM5 ior;
        public final AVROperand.IMM3 bit;


        SBIC(int size, AVRAddrMode.$sbic$ am)
        {
            super("sbic", size);
            this.ior = am.ior;
            this.bit = am.bit;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$sbic$(this, ior, bit);
        }


        @Override
        public String toString()
        {
            return name + ' ' + ior + ", " + bit;
        }
    }

    public static class SBIS extends AVRInstr
    {

        public final AVROperand.IMM5 ior;
        public final AVROperand.IMM3 bit;


        SBIS(int size, AVRAddrMode.$sbis$ am)
        {
            super("sbis", size);
            this.ior = am.ior;
            this.bit = am.bit;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$sbis$(this, ior, bit);
        }


        @Override
        public String toString()
        {
            return name + ' ' + ior + ", " + bit;
        }
    }

    public static class SBIW extends AVRInstr
    {

        public final AVROperand.op_RDL rd;
        public final AVROperand.IMM6 imm;


        SBIW(int size, AVRAddrMode.$sbiw$ am)
        {
            super("sbiw", size);
            this.rd = am.rd;
            this.imm = am.imm;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$sbiw$(this, rd, imm);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd + ", " + imm;
        }
    }

    public static class SBR extends HGPRIMM8_Instr
    {

        SBR(int size, AVRAddrMode.HGPRIMM8 am)
        {
            super("sbr", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class SBRC extends AVRInstr
    {

        public final AVROperand.op_GPR rr;
        public final AVROperand.IMM3 bit;


        SBRC(int size, AVRAddrMode.$sbrc$ am)
        {
            super("sbrc", size);
            this.rr = am.rr;
            this.bit = am.bit;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$sbrc$(this, rr, bit);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rr + ", " + bit;
        }
    }

    public static class SBRS extends AVRInstr
    {

        public final AVROperand.op_GPR rr;
        public final AVROperand.IMM3 bit;


        SBRS(int size, AVRAddrMode.$sbrs$ am)
        {
            super("sbrs", size);
            this.rr = am.rr;
            this.bit = am.bit;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$sbrs$(this, rr, bit);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rr + ", " + bit;
        }
    }

    public static class SEC extends AVRInstr
    {

        SEC(int size)
        {
            super("sec", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class SEH extends AVRInstr
    {

        SEH(int size)
        {
            super("seh", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class SEI extends AVRInstr
    {

        SEI(int size)
        {
            super("sei", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class SEN extends AVRInstr
    {

        SEN(int size)
        {
            super("sen", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class SER extends AVRInstr
    {

        public final AVROperand.op_HGPR rd;


        SER(int size, AVRAddrMode.$ser$ am)
        {
            super("ser", size);
            this.rd = am.rd;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$ser$(this, rd);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd;
        }
    }

    public static class SES extends AVRInstr
    {

        SES(int size)
        {
            super("ses", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class SET extends AVRInstr
    {

        SET(int size)
        {
            super("set", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class SEV extends AVRInstr
    {

        SEV(int size)
        {
            super("sev", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class SEZ extends AVRInstr
    {

        SEZ(int size)
        {
            super("sez", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class SLEEP extends AVRInstr
    {

        SLEEP(int size)
        {
            super("sleep", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class SPM extends AVRInstr
    {

        SPM(int size)
        {
            super("spm", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class STD extends AVRInstr
    {

        public final AVROperand.op_YZ ar;
        public final AVROperand.IMM6 imm;
        public final AVROperand.op_GPR rr;


        STD(int size, AVRAddrMode.$std$ am)
        {
            super("std", size);
            this.ar = am.ar;
            this.imm = am.imm;
            this.rr = am.rr;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$std$(this, ar, imm, rr);
        }


        @Override
        public String toString()
        {
            return name + " " + ar + "+" + imm + ", " + rr;
        }
    }

    public static class STS extends AVRInstr
    {

        public final AVROperand.DADDR addr;
        public final AVROperand.op_GPR rr;


        STS(int size, AVRAddrMode.$sts$ am)
        {
            super("sts", size);
            this.addr = am.addr;
            this.rr = am.rr;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$sts$(this, addr, rr);
        }


        @Override
        public String toString()
        {
            return name + ' ' + addr + ", " + rr;
        }
    }

    public static class SUB extends GPRGPR_Instr
    {

        SUB(int size, AVRAddrMode.GPRGPR am)
        {
            super("sub", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class SUBI extends HGPRIMM8_Instr
    {

        SUBI(int size, AVRAddrMode.HGPRIMM8 am)
        {
            super("subi", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class SWAP extends GPR_Instr
    {

        SWAP(int size, AVRAddrMode.GPR am)
        {
            super("swap", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class TST extends AVRInstr
    {

        public final AVROperand.op_GPR rd;


        TST(int size, AVRAddrMode.$tst$ am)
        {
            super("tst", size);
            this.rd = am.rd;
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public void accept(AVRAddrModeVisitor v)
        {
            v.visit_$tst$(this, rd);
        }


        @Override
        public String toString()
        {
            return name + ' ' + rd;
        }
    }

    public static class WDR extends AVRInstr
    {

        WDR(int size)
        {
            super("wdr", size);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }


        @Override
        public String toString()
        {
            return name;
        }
    }

    public static class ELPM extends XLPM_Instr
    {

        ELPM(int size, AVRAddrMode.XLPM am)
        {
            super("elpm", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class LPM extends XLPM_Instr
    {

        LPM(int size, AVRAddrMode.XLPM am)
        {
            super("lpm", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class LD extends LD_ST_Instr
    {

        LD(int size, AVRAddrMode.LD_ST am)
        {
            super("ld", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

    public static class ST extends LD_ST_Instr
    {

        ST(int size, AVRAddrMode.LD_ST am)
        {
            super("st", size, am);
        }


        @Override
        public void accept(AVRInstrVisitor v)
        {
            v.visit(this);
        }
    }

}
