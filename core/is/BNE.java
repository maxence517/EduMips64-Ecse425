/*
 * BNE.java
 *
 * may 2006
 * Instruction BNE of the MIPS64 Instruction Set
 * (c) 2006 EduMips64 project - Trubia Massimo, Russo Daniele
 *
 * This file is part of the EduMIPS64 project, and is released under the GNU
 * General Public License.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package edumips64.core.is;
import edumips64.core.*;
import edumips64.utils.*;
/** <pre>
 *         Syntax: BNE rt, rs, immediate
 *    Description: if rs != rt then branch
 *                 To compare GPRs then do a PC-relative conditional branch
 *  </pre>
  * @author Trubia Massimo, Russo Daniele
 */

public class BNE extends FlowControl_IType {
    final String OPCODE_VALUE="000101";


    public BNE() {
        super.OPCODE_VALUE = OPCODE_VALUE;
        syntax="%R,%R,%B";
	name="BNE";
    }

    // IF stage is performing the prediction - jumping to the predicted location
    // IF stage performs n-bit local predictor - jumps (predict taken) if CPU counter is >= ^(n-1)

    /**
     * IF stage performs n-bit local predictor - jumps (predict taken) if CPU counter is >= 2^(n-1) otherwise, will perform predict not taken
     *
     * @throws RAWException
     * @throws IrregularWriteOperationException
     * @throws IrregularStringOfBitsException
     * @throws JumpException
     * @throws TwosComplementSumException
     */
    @Override
    public void IF() throws RAWException, IrregularWriteOperationException, IrregularStringOfBitsException, JumpException, TwosComplementSumException {

        //Gets CPU
        int currentCount = cpu.getnBitCount();
        double boundaryCount = Math.pow(2,CPU.N_FORNBITPREDICTOR-1);

        BitSet64 bs = new BitSet64();
        bs.writeHalf(params.get(OFFSET_FIELD));
        String offset = bs.getBinString();

        String pc_new = "";
        Register pc = cpu.getPC();

        String pc_old = cpu.getPC().getBinString();

        if(currentCount >= boundaryCount) {
            //updating program counter
            pc_new = InstructionsUtils.twosComplementSum(pc_old, offset);
            pc.setBits(pc_new, 0);

            throw new JumpException();
        }
    }

    // ID stage resolves the branch - determines
    public void ID() throws RAWException, IrregularWriteOperationException, IrregularStringOfBitsException,TwosComplementSumException, JumpException {

        //for n-bit local predictor
        int currentCount = cpu.getnBitCount();
        double boundaryCount = Math.pow(2,CPU.N_FORNBITPREDICTOR-1);


        if(cpu.getRegister(params.get(RS_FIELD)).getWriteSemaphore()>0 || cpu.getRegister(params.get(RT_FIELD)).getWriteSemaphore()>0)
            throw new RAWException();

        //getting registers rs and rt
        String rs=cpu.getRegister(params.get(RS_FIELD)).getBinString();
        String rt=cpu.getRegister(params.get(RT_FIELD)).getBinString();
         //converting offset into a signed binary value of 64 bits in length
        BitSet64 bs=new BitSet64();
        bs.writeHalf(params.get(OFFSET_FIELD));
        String offset=bs.getBinString();
        boolean condition=!rs.equals(rt); //condition is if you have to branch or not. Condition = branch (taken), !condition = do not branch (not taken)

        String pc_new="";
        Register pc=cpu.getPC();
        String pc_old=cpu.getPC().getBinString();

        BitSet64 bs_temp = new BitSet64();
        bs_temp.writeDoubleWord(-4);
        pc_old = InstructionsUtils.twosComplementSum(pc_old, bs_temp.getBinString());

        //Incrementing or decrementing the counter
        //If currentCount > 0 and the true outcome is not taken then you decrement
        //decrementing means increasing certaincy for predict not taken and decreasing certaincy for predict taken
        if (currentCount > 0 && !condition) {
            cpu.setnBitCount(currentCount-1);
        }

        //If currentCount < max boundary and the true outcome is taken then you increment
        //incrementing means decreasing certaincy for predict not taken and increasing certaincy for predict taken
        if (currentCount < Math.pow(2,CPU.N_FORNBITPREDICTOR)-1 && condition) {
            cpu.setnBitCount(currentCount+1);
        }

        //if you've predicted taken but you're actually not taken, then jump out of the branch (applying a fix)
        if(currentCount >= boundaryCount && !condition){
            pc_new=InstructionsUtils.twosComplementSubstraction(pc_old,offset);
            pc.setBits(pc_new,0);
            throw new JumpException();
        }

        //if you've predict not taken but you're actually taken, then jump to the branch
        else if (currentCount < boundaryCount && condition) {
            pc_new=InstructionsUtils.twosComplementSum(pc_old,offset);
            pc.setBits(pc_new,0);
            throw new JumpException();
        }
    }
}
