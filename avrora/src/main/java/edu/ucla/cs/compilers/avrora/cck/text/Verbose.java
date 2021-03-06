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

package edu.ucla.cs.compilers.avrora.cck.text;

import java.util.HashMap;

/**
 * The <code>Verbose</code> class is used to get instances of
 * <code>Verbose.Printer</code> for reporting the internal operations of various
 * subsystems. This class centralizes the management for verbose flags.
 *
 * @author Ben L. Titzer
 */
public class Verbose
{

    static boolean ALL;

    static final HashMap<String, Printer> printerMap = new HashMap<String, Printer>();
    static final Printer verbosePrinter = getVerbosePrinter("verbose");


    public static Printer getVerbosePrinter(String category)
    {
        Printer p = getPrinter(category);
        if (verbosePrinter != null && verbosePrinter.enabled)
        {
            verbosePrinter.println("verbose: requested printer for "
                    + StringUtil.quote(category));
        }
        return p;
    }


    public static boolean isVerbose(String category)
    {
        Printer printer = printerMap.get(category);
        return printer != null && printer.enabled;
    }


    public static void setVerbose(String category, boolean on)
    {
        if ("all".equals(category))
        {
            ALL = on;
            for (Printer p : printerMap.values())
            {
                p.enabled = on;
            }
            return;
        }

        Printer p = getPrinter(category);
        if (verbosePrinter != null && verbosePrinter.enabled)
        {
            verbosePrinter.println("verbose: set printer "
                    + StringUtil.quote(category) + " to " + on);
        }
        p.enabled = on;
    }


    private static Printer getPrinter(String category)
    {
        Printer p = printerMap.get(category);
        if (p == null)
        {
            p = new Printer();
            printerMap.put(category, p);
        }
        return p;
    }

    public static class Printer extends edu.ucla.cs.compilers.avrora.cck.text.Printer
    {

        public boolean enabled;


        Printer()
        {
            super(System.out);
            enabled = ALL;
        }


        @Override
        public void println(String s)
        {
            if (enabled)
                super.println(s);
        }


        @Override
        public void print(String s)
        {
            if (enabled)
                super.print(s);
        }


        @Override
        public void nextln()
        {
            if (enabled)
                super.nextln();
        }


        @Override
        public void indent()
        {
            if (enabled)
                super.indent();
        }


        @Override
        public void spaces()
        {
            if (enabled)
                super.spaces();
        }


        @Override
        public void unindent()
        {
            if (enabled)
                super.unindent();
        }


        @Override
        public void startblock()
        {
            if (enabled)
                super.startblock();
        }


        @Override
        public void startblock(String name)
        {
            if (enabled)
                super.startblock(name);
        }


        @Override
        public void endblock()
        {
            if (enabled)
                super.endblock();
        }
    }

}
