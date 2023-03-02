/*******************************************************************************
 * Copyright (c) 2023, 2023 IBM Corp. and others
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] http://openjdk.java.net/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
 *******************************************************************************/

package org.openj9.test.arrays;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class TestJavaUtilArrays {
    static class ArraySet {
        int size;
        boolean[] booleans;
        byte[] bytes;
        char[] chars;
        short[] shorts;
        int[] ints;
        long[] longs;
        float[] floats;
        double[] doubles;

        private ArraySet() {}

        ArraySet(int size) {
            this.size = size;
            booleans = new boolean[size];
            bytes = new byte[size];
            chars = new char[size];
            shorts = new short[size];
            ints = new int[size];
            longs = new long[size];
            floats = new float[size];
            doubles = new double[size];
        }

        public void toggle(int i) {
            assert i < size;
            booleans[i] = !booleans[i];
            bytes[i] ^= (byte) -1;
            chars[i] ^= (char) -1;
            shorts[i] ^= (short) -1;
            ints[i] ^= -1;
            longs[i] ^= -1;
            floats[i] = Float.intBitsToFloat(~Float.floatToRawIntBits(floats[i]));
            doubles[i] = Double.longBitsToDouble(~Double.doubleToRawLongBits(doubles[i]));
        }

        public ArraySet clone() {
            ArraySet clone = new ArraySet();
            clone.size = size;
            clone.booleans = booleans.clone();
            clone.bytes = bytes.clone();
            clone.chars = chars.clone();
            clone.shorts = shorts.clone();
            clone.ints = ints.clone();
            clone.longs = longs.clone();
            clone.floats = floats.clone();
            clone.doubles = doubles.clone();
            return clone;
        }

        public ArraySet shiftedBy(int i) {
            assert i >= 0;
            ArraySet other = new ArraySet(i + size);

            System.arraycopy(booleans, 0, other.booleans, i, size);
            System.arraycopy(bytes, 0, other.bytes, i, size);
            System.arraycopy(chars, 0, other.chars, i, size);
            System.arraycopy(shorts, 0, other.shorts, i, size);
            System.arraycopy(ints, 0, other.ints, i, size);
            System.arraycopy(longs, 0, other.longs, i, size);
            System.arraycopy(floats, 0, other.floats, i, size);
            System.arraycopy(doubles, 0, other.doubles, i, size);

            return other;
        }
    }

    private static void testArraysMismatch(ArraySet a, ArraySet b, int expected) {
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d boolean array", a.size), Arrays.mismatch(a.booleans, b.booleans), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d byte array", a.size), Arrays.mismatch(a.bytes, b.bytes), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d char array", a.size), Arrays.mismatch(a.chars, b.chars), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d short array", a.size), Arrays.mismatch(a.shorts, b.shorts), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d int array", a.size), Arrays.mismatch(a.ints, b.ints), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d long array", a.size), Arrays.mismatch(a.longs, b.longs), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d float array", a.size), Arrays.mismatch(a.floats, b.floats), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d double array", a.size), Arrays.mismatch(a.doubles, b.doubles), expected);
    }

    private static void testArraysRangeMismatch(ArraySet a, int aFromIndex, int aToIndex, ArraySet b, int bFromIndex, int bToIndex, int expected) {
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d boolean array range", a.size), Arrays.mismatch(a.booleans, aFromIndex, aToIndex, b.booleans, bFromIndex, bToIndex), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d byte array range", a.size), Arrays.mismatch(a.bytes, aFromIndex, aToIndex, b.bytes, bFromIndex, bToIndex), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d char array range", a.size), Arrays.mismatch(a.chars, aFromIndex, aToIndex, b.chars, bFromIndex, bToIndex), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d short array range", a.size), Arrays.mismatch(a.shorts, aFromIndex, aToIndex, b.shorts, bFromIndex, bToIndex), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d int array range", a.size), Arrays.mismatch(a.ints, aFromIndex, aToIndex, b.ints, bFromIndex, bToIndex), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d long array range", a.size), Arrays.mismatch(a.longs, aFromIndex, aToIndex, b.longs, bFromIndex, bToIndex), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d float array range", a.size), Arrays.mismatch(a.floats, aFromIndex, aToIndex, b.floats, bFromIndex, bToIndex), expected);
        AssertJUnit.assertEquals(String.format("Incorrect result for length %d double array range", a.size), Arrays.mismatch(a.doubles, aFromIndex, aToIndex, b.doubles, bFromIndex, bToIndex), expected);
    }

    private static void testArraysMismatch(ArraySet uut) {
        ArraySet cmp = uut.clone();

        // no mismatch
        int mismatchIndex = -1;
        testArraysMismatch(uut, cmp, mismatchIndex);

        // early mismatch
        mismatchIndex = 0;
        cmp.toggle(mismatchIndex);
        testArraysMismatch(uut, cmp, mismatchIndex);
        cmp.toggle(mismatchIndex);

        // end mismatch
        mismatchIndex = uut.size - 1;
        cmp.toggle(mismatchIndex);
        testArraysMismatch(uut, cmp, mismatchIndex);
        cmp.toggle(mismatchIndex);


        int shift = 33;

        // no mismatch in range
        mismatchIndex = -1;
        cmp = uut.shiftedBy(shift);
        testArraysRangeMismatch(uut, 0, uut.size, cmp, shift, cmp.size, mismatchIndex);
        testArraysRangeMismatch(cmp, shift, cmp.size, uut, 0, uut.size, mismatchIndex);

        // early mismatch in range
        mismatchIndex = 1;
        cmp.toggle(shift + mismatchIndex);
        testArraysRangeMismatch(uut, 0, uut.size, cmp, shift, cmp.size, mismatchIndex);
        testArraysRangeMismatch(cmp, shift, cmp.size, uut, 0, uut.size, mismatchIndex);
        cmp.toggle(shift + mismatchIndex);

        // end mismatch in range
        mismatchIndex = uut.size - 1;
        cmp.toggle(shift + mismatchIndex);
        testArraysRangeMismatch(uut, 0, uut.size, cmp, shift, cmp.size, mismatchIndex);
        testArraysRangeMismatch(cmp, shift, cmp.size, uut, 0, uut.size, mismatchIndex);
        cmp.toggle(shift + mismatchIndex);
    }

    @Test(groups = {"level.sanity"}, invocationCount=4)
    public static void testArraysMismatch() {
        testArraysMismatch(new ArraySet(2));
        testArraysMismatch(new ArraySet(10));
        testArraysMismatch(new ArraySet(1000));
        testArraysMismatch(new ArraySet(1000000));
    }
}
