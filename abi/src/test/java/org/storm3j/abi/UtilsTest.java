/*
 * Copyright 2019 Web3 Labs LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.storm3j.abi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import org.storm3j.abi.datatypes.Bool;
import org.storm3j.abi.datatypes.DynamicArray;
import org.storm3j.abi.datatypes.DynamicBytes;
import org.storm3j.abi.datatypes.Fixed;
import org.storm3j.abi.datatypes.Int;
import org.storm3j.abi.datatypes.StaticArray;
import org.storm3j.abi.datatypes.Ufixed;
import org.storm3j.abi.datatypes.Uint;
import org.storm3j.abi.datatypes.Utf8String;
import org.storm3j.abi.datatypes.generated.Int64;
import org.storm3j.abi.datatypes.generated.StaticArray2;
import org.storm3j.abi.datatypes.generated.Uint256;
import org.storm3j.abi.datatypes.generated.Uint64;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.storm3j.abi.Utils.typeMap;

public class UtilsTest {

    @Test
    public void testGetTypeName() throws ClassNotFoundException {
        assertThat(Utils.getTypeName(new TypeReference<Uint>() {}), is("uint256"));
        assertThat(Utils.getTypeName(new TypeReference<Int>() {}), is("int256"));
        assertThat(Utils.getTypeName(new TypeReference<Ufixed>() {}), is("ufixed256"));
        assertThat(Utils.getTypeName(new TypeReference<Fixed>() {}), is("fixed256"));

        assertThat(Utils.getTypeName(new TypeReference<Uint64>() {}), is("uint64"));
        assertThat(Utils.getTypeName(new TypeReference<Int64>() {}), is("int64"));
        assertThat(Utils.getTypeName(new TypeReference<Bool>() {}), is("bool"));
        assertThat(Utils.getTypeName(new TypeReference<Utf8String>() {}), is("string"));
        assertThat(Utils.getTypeName(new TypeReference<DynamicBytes>() {}), is("bytes"));

        assertThat(
                Utils.getTypeName(
                        new TypeReference.StaticArrayTypeReference<StaticArray<Uint>>(5) {}),
                is("uint256[5]"));
        assertThat(Utils.getTypeName(new TypeReference<DynamicArray<Uint>>() {}), is("uint256[]"));
    }

    @Test
    public void testTypeMap() {
        final List<BigInteger> input =
                Arrays.asList(BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN);

        assertThat(
                typeMap(input, Uint256.class),
                equalTo(
                        Arrays.asList(
                                new Uint256(BigInteger.ZERO),
                                new Uint256(BigInteger.ONE),
                                new Uint256(BigInteger.TEN))));
    }

    @Test
    public void testTypeMapNested() {
        List<BigInteger> innerList1 = Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2));
        List<BigInteger> innerList2 = Arrays.asList(BigInteger.valueOf(3), BigInteger.valueOf(4));

        final List<List<BigInteger>> input = Arrays.asList(innerList1, innerList2);

        StaticArray2<Uint256> staticArray1 =
                new StaticArray2<>(Uint256.class, new Uint256(1), new Uint256(2));

        StaticArray2<Uint256> staticArray2 =
                new StaticArray2<>(Uint256.class, new Uint256(3), new Uint256(4));

        List<StaticArray2> actual = typeMap(input, StaticArray2.class, Uint256.class);

        assertThat(actual.get(0), equalTo(staticArray1));
        assertThat(actual.get(1), equalTo(staticArray2));
    }

    @Test
    public void testTypeMapEmpty() {
        assertThat(typeMap(new ArrayList<>(), Uint256.class), equalTo(new ArrayList<Uint256>()));
    }
}
