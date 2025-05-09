/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 *
 * Additional modifications by Guy Steele in 2019 to refactor the code
 * and to implement the {@link RandomGenerator} interface.
 */
// Android-note: RandomGenerator is not used as it is not available in Android.

package com.example.javatest.concurrent;

import java.io.ObjectStreamField;
import java.security.AccessControlContext;
import java.util.Random;
import java.util.Spliterator;
import com.example.javatest.concurrent.ForkJoinTask;
import com.example.javatest.concurrent.atomic.AtomicInteger;
import com.example.javatest.concurrent.atomic.AtomicLong;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import sun.misc.Unsafe;


/**
 * A random number generator (with period 2<sup>64</sup>) isolated
 * to the current thread.  Like the global {@link Random}
 * generator used by the {@link Math} class,
 * a {@code ThreadLocalRandom} is initialized
 * with an internally generated seed that may not otherwise be
 * modified. When applicable, use of {@code ThreadLocalRandom} rather
 * than shared {@code Random} objects in concurrent programs will
 * typically encounter much less overhead and contention.  Use of
 * {@code ThreadLocalRandom} is particularly appropriate when multiple
 * tasks (for example, each a {@link ForkJoinTask}) use random numbers
 * in parallel in thread pools.
 *
 * <p>Usages of this class should typically be of the form:
 * {@code ThreadLocalRandom.current().nextX(...)} (where
 * {@code X} is {@code Int}, {@code Long}, etc).
 * When all usages are of this form, it is never possible to
 * accidentally share a {@code ThreadLocalRandom} across multiple threads.
 *
 * <p>This class also provides additional commonly used bounded random
 * generation methods.
 *
 * <p>Instances of {@code ThreadLocalRandom} are not cryptographically
 * secure.  Consider instead using {@link java.security.SecureRandom}
 * in security-sensitive applications. Additionally,
 * default-constructed instances do not use a cryptographically random
 * seed unless the {@linkplain System#getProperty system property}
 * {@code java.util.secureRandomSeed} is set to {@code true}.
 *
 * @since 1.7
 * @author Doug Lea
 */

// Android-removed: RandomGenerator not available in Android.
// @RandomGeneratorProperties(
//         name = "ThreadLocalRandom",
//         i = 64, j = 0, k = 0,
//         equidistribution = 1
// )
public class ThreadLocalRandom extends Random {
    // Android-changed: Removed reference to RandomGenerator.
    /*
     * This class implements the java.util.Random API (and subclasses
     * Random) using a single static instance that accesses 64 bits of
     * random number state held in class java.lang.Thread (field
     * threadLocalRandomSeed). In doing so, it also provides a home
     * for managing package-private utilities that rely on exactly the
     * same state as needed to maintain the ThreadLocalRandom
     * instances. We leverage the need for an initialization flag
     * field to also use it as a "probe" -- a self-adjusting thread
     * hash used for contention avoidance, as well as a secondary
     * simpler (xorShift) random seed that is conservatively used to
     * avoid otherwise surprising users by hijacking the
     * ThreadLocalRandom sequence.  The dual use is a marriage of
     * convenience, but is a simple and efficient way of reducing
     * application-level overhead and footprint of most concurrent
     * programs. Even more opportunistically, we also define here
     * other package-private utilities that access Thread class
     * fields.
     *
     * Even though this class subclasses java.util.Random, it uses the
     * same basic algorithm as java.util.SplittableRandom.  (See its
     * internal documentation for explanations, which are not repeated
     * here.)  Note that ThreadLocalRandom is not a "splittable" generator
     * (it does not support the split method), but it behaves as if
     * one instance of the SplittableRandom algorithm had been
     * created for each thread, each with a distinct gamma parameter
     * (calculated from the thread id).
     *
     * Because this class is in a different package than class Thread,
     * field access methods use Unsafe to bypass access control rules.
     * To conform to the requirements of the Random superclass
     * constructor, the common static ThreadLocalRandom maintains an
     * "initialized" field for the sake of rejecting user calls to
     * setSeed while still allowing a call from constructor.  Note
     * that serialization is completely unnecessary because there is
     * only a static singleton.  But we generate a serial form
     * containing "rnd" and "initialized" fields to ensure
     * compatibility across versions.
     *
     * Implementations of non-core methods are mostly the same as in
     * SplittableRandom, that were in part derived from a previous
     * version of this class.
     *
     * The nextLocalGaussian ThreadLocal supports the very rarely used
     * nextGaussian method by providing a holder for the second of a
     * pair of them. As is true for the base class version of this
     * method, this time/space tradeoff is probably never worthwhile,
     * but we provide identical statistical properties.
     */

    // Android-added: mix64 implementation from version 11 of the class.
    private static long mix64(long z) {
        z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL;
        z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L;
        return z ^ (z >>> 33);
    }

    private static int mix32(long z) {
        z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL;
        return (int)(((z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L) >>> 32);
    }

    /**
     * Field used only during singleton initialization.
     * True when constructor completes.
     */
    boolean initialized;

    /** Constructor used only for static singleton */
    private ThreadLocalRandom() {
        initialized = true; // false during super() call
    }

    /**
     * Initialize Thread fields for the current thread.  Called only
     * when Thread.threadLocalRandomProbe is zero, indicating that a
     * thread local seed value needs to be generated. Note that even
     * though the initialization is purely thread-local, we need to
     * rely on (static) atomic generators to initialize the values.
     */
    static final void localInit() {
        int p = probeGenerator.addAndGet(PROBE_INCREMENT);
        int probe = (p == 0) ? 1 : p; // skip 0
        // Android-changed: RandomSupport not available in Android.
        // long seed = RandomSupport.mixMurmur64(seeder.getAndAdd(SEEDER_INCREMENT));
        long seed = mix64(seeder.getAndAdd(SEEDER_INCREMENT));
        Thread t = Thread.currentThread();
        U.putLong(t, SEED, seed);
        U.putInt(t, PROBE, probe);
    }

    /**
     * Returns the current thread's {@code ThreadLocalRandom} object.
     * Methods of this object should be called only by the current thread,
     * not by other threads.
     *
     * @return the current thread's {@code ThreadLocalRandom}
     */
    public static ThreadLocalRandom current() {
        if (U.getInt(Thread.currentThread(), PROBE) == 0)
            localInit();
        return instance;
    }

    /**
     * Throws {@code UnsupportedOperationException}.  Setting seeds in
     * this generator is not supported.
     *
     * @throws UnsupportedOperationException always
     */
    public void setSeed(long seed) {
        // only allow call from super() constructor
        if (initialized)
            throw new UnsupportedOperationException();
    }

    /**
     * Update the thread local seed value by adding to it the sum
     * of {@code GOLDEN_GAMMA} (an odd value) and twice the thread id.
     * This sum is always odd (to guarantee that the generator
     * has maximum period) and is different for different threads.
     * Because thread id values are allocated consecutively starting
     * from 0, the high 32 bits of this sum will be the same as the
     * high 32 bits of {@code GOLDEN_GAMMA} unless an extremely large
     * number of threads have been created, and so the overall
     * value added to the thread local seed value will have at least
     * fourteen 01 and 10 transitions (see the documentation for the
     * method {@code mixGamma} in class {@code SplittableRandom}),
     * which should provide adequate statistical quality for
     * applications likely to use {@code ThreadLocalRandom}.
     */
    final long nextSeed() {
        Thread t; long r; // read and update per-thread seed
        U.putLong(t = Thread.currentThread(), SEED,
                  r = U.getLong(t, SEED) + (t.getId() << 1) + GOLDEN_GAMMA);
        return r;
    }

    /**
     * Generates a pseudorandom number with the indicated number of
     * low-order bits.  Because this class has no subclasses, this
     * method cannot be invoked or overridden.
     *
     * @param  bits random bits
     * @return the next pseudorandom value from this random number
     *         generator's sequence
     */
    protected int next(int bits) {
        return nextInt() >>> (32 - bits);
    }

    // Within-package utilities

    /*
     * Descriptions of the usages of the methods below can be found in
     * the classes that use them. Briefly, a thread's "probe" value is
     * a non-zero hash code that (probably) does not collide with
     * other existing threads with respect to any power of two
     * collision space. When it does collide, it is pseudo-randomly
     * adjusted (using a Marsaglia XorShift). The nextSecondarySeed
     * method is used in the same contexts as ThreadLocalRandom, but
     * only for transient usages such as random adaptive spin/block
     * sequences for which a cheap RNG suffices and for which it could
     * in principle disrupt user-visible statistical properties of the
     * main ThreadLocalRandom if we were to use it.
     *
     * Note: Because of package-protection issues, versions of some
     * these methods also appear in some subpackage classes.
     */

    /**
     * Returns the probe value for the current thread without forcing
     * initialization. Note that invoking ThreadLocalRandom.current()
     * can be used to force initialization on zero return.
     */
    static final int getProbe() {
        return U.getInt(Thread.currentThread(), PROBE);
    }

    /**
     * Pseudo-randomly advances and records the given probe value for the
     * given thread.
     */
    static final int advanceProbe(int probe) {
        probe ^= probe << 13;   // xorshift
        probe ^= probe >>> 17;
        probe ^= probe << 5;
        U.putInt(Thread.currentThread(), PROBE, probe);
        return probe;
    }

    /**
     * Returns the pseudo-randomly initialized or updated secondary seed.
     */
    static final int nextSecondarySeed() {
        int r;
        Thread t = Thread.currentThread();
        if ((r = U.getInt(t, SECONDARY)) != 0) {
            r ^= r << 13;   // xorshift
            r ^= r >>> 17;
            r ^= r << 5;
        }
        else if ((r = mix32(seeder.getAndAdd(SEEDER_INCREMENT))) == 0)
            r = 1; // avoid zero
        U.putInt(t, SECONDARY, r);
        return r;
    }

    // Support for other package-private ThreadLocal access

    /**
     * Erases ThreadLocals by nulling out Thread maps.
     */
    static final void eraseThreadLocals(Thread thread) {
//        U.putReference(thread, THREADLOCALS, null);
//        U.putReference(thread, INHERITABLETHREADLOCALS, null);
    }

    static final void setInheritedAccessControlContext(Thread thread,
                                                       @SuppressWarnings("removal") AccessControlContext acc) {
//        U.putReferenceRelease(thread, INHERITEDACCESSCONTROLCONTEXT, acc);
    }

    // Serialization support

    private static final long serialVersionUID = -5851777807851030925L;

    /**
     * @serialField rnd long
     *              seed for random computations
     * @serialField initialized boolean
     *              always true
     */
    private static final ObjectStreamField[] serialPersistentFields = {
        new ObjectStreamField("rnd", long.class),
        new ObjectStreamField("initialized", boolean.class),
    };

    /**
     * Saves the {@code ThreadLocalRandom} to a stream (that is, serializes it).
     * @param s the stream
     * @throws java.io.IOException if an I/O error occurs
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {

        java.io.ObjectOutputStream.PutField fields = s.putFields();
        fields.put("rnd", U.getLong(Thread.currentThread(), SEED));
        fields.put("initialized", true);
        s.writeFields();
    }

    /**
     * Returns the {@link #current() current} thread's {@code ThreadLocalRandom}.
     * @return the {@link #current() current} thread's {@code ThreadLocalRandom}
     */
    private Object readResolve() {
        return current();
    }

    // Static initialization

    /**
     * The seed increment.  This must be an odd value for the generator to
     * have the maximum period (2 to the 64th power).
     *
     * The value 0x9e3779b97f4a7c15L is odd, and moreover consists of the
     * first 64 bits of the fractional part of the golden ratio,
     * which is known to generate good Weyl sequences.
     */
    private static final long GOLDEN_GAMMA = 0x9e3779b97f4a7c15L;

    /**
     * The increment for generating probe values.
     */
    private static final int PROBE_INCREMENT = 0x9e3779b9;

    /**
     * The increment of seeder per new instance.
     */
    private static final long SEEDER_INCREMENT = 0xbb67ae8584caa73bL;

    // BEGIN Android-added: Constants from version 11 of the class.
    /**
     * The least non-zero value returned by nextDouble(). This value
     * is scaled by a random value of 53 bits to produce a result.
     */
    private static final double DOUBLE_UNIT = 0x1.0p-53;  // 1.0  / (1L << 53)
    private static final float  FLOAT_UNIT  = 0x1.0p-24f; // 1.0f / (1 << 24)
    // END Android-added: Constants from version 11 of the class.

    // IllegalArgumentException messages
    static final String BAD_BOUND = "bound must be positive";
    static final String BAD_RANGE = "bound must be greater than origin";
    static final String BAD_SIZE  = "size must be non-negative";

    // Unsafe mechanics
    private static final Unsafe U = Unsafe.getUnsafe();
    private static final long SEED
        = 0;
    private static final long PROBE
        = 1;
    private static final long SECONDARY
        = 2;
//    private static final long THREADLOCALS
//        = U.objectFieldOffset(Thread.class, "threadLocals");
//    private static final long INHERITABLETHREADLOCALS
//        = U.objectFieldOffset(Thread.class, "inheritableThreadLocals");
//    private static final long INHERITEDACCESSCONTROLCONTEXT
//        = U.objectFieldOffset(Thread.class, "inheritedAccessControlContext");

    // Android-added: nextLocalGaussian from version 11 of the class.
    /** Rarely-used holder for the second of a pair of Gaussians */
    private static final ThreadLocal<Double> nextLocalGaussian =
        new ThreadLocal<>();

    /** Generates per-thread initialization/probe field */
    private static final AtomicInteger probeGenerator = new AtomicInteger();

    /** The common ThreadLocalRandom */
    private static final ThreadLocalRandom instance = new ThreadLocalRandom();

    /**
     * The next seed for default constructors.
     */
    private static final AtomicLong seeder
        // Android-changed: RandomSupport not available in Android.
        // = new AtomicLong(RandomSupport.mixMurmur64(System.currentTimeMillis()) ^
        //                  RandomSupport.mixMurmur64(System.nanoTime()));
        = new AtomicLong(mix64(System.currentTimeMillis()) ^
                         mix64(System.nanoTime()));


    // at end of <clinit> to survive static initialization circularity
    static {
//        String sec = VM.getSavedProperty("java.util.secureRandomSeed");
//        if (Boolean.parseBoolean(sec)) {
//            byte[] seedBytes = java.security.SecureRandom.getSeed(8);
//            long s = (long)seedBytes[0] & 0xffL;
//            for (int i = 1; i < 8; ++i)
//                s = (s << 8) | ((long)seedBytes[i] & 0xffL);
//            seeder.set(s);
        }
    }

    // BEGIN Android-changed: Keep version 11 implementation.
    /**
    @SuppressWarnings("serial")
    private static final class ThreadLocalRandomProxy extends Random {
        static final Random PROXY = new ThreadLocalRandomProxy();

        public int nextInt() {
            return ThreadLocalRandom.current().nextInt();
        }

        public long nextLong() {
            return ThreadLocalRandom.current().nextLong();
        }
    }

    /**
     * {@inheritDoc}
     * /
    @Override
    public boolean nextBoolean() {
        return super.nextBoolean();
    }

    /**
     * {@inheritDoc}
     * /
    @Override
    public int nextInt() {
        return mix32(nextSeed());
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * /
    @Override
    public int nextInt(int bound) {
        return super.nextInt(bound);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * /
    @Override
    public int nextInt(int origin, int bound) {
        return super.nextInt(origin, bound);
    }

    /**
     * {@inheritDoc}
     * /
    @Override
    public long nextLong() {
        return RandomSupport.mixMurmur64(nextSeed());
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * /
    @Override
    public long nextLong(long bound) {
        return super.nextLong(bound);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * /
    @Override
    public long nextLong(long origin, long bound) {
        return super.nextLong(origin, bound);
    }

    /**
     * {@inheritDoc}
     * /
    @Override
    public float nextFloat() {
        return super.nextFloat();
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @implNote {@inheritDoc}
     * /
    @Override
    public float nextFloat(float bound) {
         return super.nextFloat(bound);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @implNote {@inheritDoc}
     * /
    @Override
    public float nextFloat(float origin, float bound) {
        return super.nextFloat(origin, bound);
    }

    /**
     * {@inheritDoc}
     * /
    @Override
    public double nextDouble() {
        return super.nextDouble();
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @implNote {@inheritDoc}
     * /
    @Override
    public double nextDouble(double bound) {
        return super.nextDouble(bound);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @implNote {@inheritDoc}
     * /
    @Override
    public double nextDouble(double origin, double bound) {
        return super.nextDouble(origin, bound);
    }
    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @since 1.8
     * /
    @Override
    public IntStream ints(long streamSize) {
        return AbstractSpliteratorGenerator.ints(ThreadLocalRandomProxy.PROXY, streamSize);
    }

    /**
     * {@inheritDoc}
     * @implNote This method is implemented to be equivalent to
     *           {@code ints(Long.MAX_VALUE)}.
     * @since 1.8
     * /
    @Override
    public IntStream ints() {
        return AbstractSpliteratorGenerator.ints(ThreadLocalRandomProxy.PROXY);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @since 1.8
     * /
    @Override
    public IntStream ints(long streamSize, int randomNumberOrigin, int randomNumberBound) {
        return AbstractSpliteratorGenerator.ints(ThreadLocalRandomProxy.PROXY, streamSize, randomNumberOrigin, randomNumberBound);
    }

    /**
     * {@inheritDoc}
     * @implNote This method is implemented to be equivalent to
     *           {@code ints(Long.MAX_VALUE, randomNumberOrigin, randomNumberBound)}.
     * @throws IllegalArgumentException {@inheritDoc}
     * @since 1.8
     * /
    @Override
    public IntStream ints(int randomNumberOrigin, int randomNumberBound) {
        return AbstractSpliteratorGenerator.ints(ThreadLocalRandomProxy.PROXY, randomNumberOrigin, randomNumberBound);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @since 1.8
     * /
    @Override
    public LongStream longs(long streamSize) {
        return AbstractSpliteratorGenerator.longs(ThreadLocalRandomProxy.PROXY, streamSize);
    }

    /**
     * {@inheritDoc}
     * @implNote This method is implemented to be equivalent to
     *           {@code longs(Long.MAX_VALUE)}.
     * @since 1.8
     * /
    @Override
    public LongStream longs() {
        return AbstractSpliteratorGenerator.longs(ThreadLocalRandomProxy.PROXY);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @since 1.8
     * /
    @Override
    public LongStream longs(long streamSize, long randomNumberOrigin, long randomNumberBound) {
        return AbstractSpliteratorGenerator.longs(ThreadLocalRandomProxy.PROXY, streamSize, randomNumberOrigin, randomNumberBound);
    }

    /**
     * {@inheritDoc}
     * @implNote This method is implemented to be equivalent to
     *           {@code longs(Long.MAX_VALUE, randomNumberOrigin, randomNumberBound)}.
     * @throws IllegalArgumentException {@inheritDoc}
     * @since 1.8
     * /
    @Override
    public LongStream longs(long randomNumberOrigin, long randomNumberBound) {
        return AbstractSpliteratorGenerator.longs(ThreadLocalRandomProxy.PROXY, randomNumberOrigin, randomNumberBound);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @since 1.8
     * /
    @Override
    public DoubleStream doubles(long streamSize) {
        return AbstractSpliteratorGenerator.doubles(ThreadLocalRandomProxy.PROXY, streamSize);
    }

    /**
     * {@inheritDoc}
     * @implNote This method is implemented to be equivalent to
     *           {@code doubles(Long.MAX_VALUE)}.
     * @since 1.8
     * /
    @Override
    public DoubleStream doubles() {
        return AbstractSpliteratorGenerator.doubles(ThreadLocalRandomProxy.PROXY);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @since 1.8
     * /
    @Override
    public DoubleStream doubles(long streamSize, double randomNumberOrigin, double randomNumberBound) {
        return AbstractSpliteratorGenerator.doubles(ThreadLocalRandomProxy.PROXY, streamSize, randomNumberOrigin, randomNumberBound);
    }

    /**
     * {@inheritDoc}
     * @implNote This method is implemented to be equivalent to
     *           {@code doubles(Long.MAX_VALUE, randomNumberOrigin, randomNumberBound)}.
     * @throws IllegalArgumentException {@inheritDoc}
     * @since 1.8
     * /
    @Override
    public DoubleStream doubles(double randomNumberOrigin, double randomNumberBound) {
        return AbstractSpliteratorGenerator.doubles(ThreadLocalRandomProxy.PROXY, randomNumberOrigin, randomNumberBound);
    }
    */

    /**
     * The form of nextLong used by LongStream Spliterators.  If
     * origin is greater than bound, acts as unbounded form of
     * nextLong, else as bounded form.
     *
     * @param origin the least value, unless greater than bound
     * @param bound the upper bound (exclusive), must not equal origin
     * @return a pseudorandom value
     */
//    final long internalNextLong(long origin, long bound) {
////        long r = mix64(nextSeed());
////        if (origin < bound) {
////            long n = bound - origin, m = n - 1;
////            if ((n & m) == 0L)  // power of two
////                r = (r & m) + origin;
////            else if (n > 0L) {  // reject over-represented candidates
////                for (long u = r >>> 1;            // ensure nonnegative
////                     u + m - (r = u % n) < 0L;    // rejection check
////                     u = mix64(nextSeed()) >>> 1) // retry
////                    ;
////                r += origin;
////            }
////            else {              // range not representable as long
////                while (r < origin || r >= bound)
////                    r = mix64(nextSeed());
////            }
////        }
////        return r;
//        return ;
//    }

    /**
     * The form of nextInt used by IntStream Spliterators.
     * Exactly the same as long version, except for types.
     *
     * @param origin the least value, unless greater than bound
     * @param bound the upper bound (exclusive), must not equal origin
     * @return a pseudorandom value
     */
//    final int internalNextInt(int origin, int bound) {
//        int r = mix32(nextSeed());
//        if (origin < bound) {
//            int n = bound - origin, m = n - 1;
//            if ((n & m) == 0)
//                r = (r & m) + origin;
//            else if (n > 0) {
//                for (int u = r >>> 1;
//                     u + m - (r = u % n) < 0;
//                     u = mix32(nextSeed()) >>> 1)
//                    ;
//                r += origin;
//            }
//            else {
//                while (r < origin || r >= bound)
//                    r = mix32(nextSeed());
//            }
//        }
//        return r;
//    }

    /**
     * The form of nextDouble used by DoubleStream Spliterators.
     *
     * @param origin the least value, unless greater than bound
     * @param bound the upper bound (exclusive), must not equal origin
     * @return a pseudorandom value
     */
//    final double internalNextDouble(double origin, double bound) {
//        double r = (nextLong() >>> 11) * DOUBLE_UNIT;
//        if (origin < bound) {
//            r = r * (bound - origin) + origin;
//            if (r >= bound) // correct for rounding
//                r = Double.longBitsToDouble(Double.doubleToLongBits(bound) - 1);
//        }
//        return r;
//    }

    /**
     * Returns a pseudorandom {@code int} value.
     *
     * @return a pseudorandom {@code int} value
     */
//    public int nextInt() {
//        return mix32(nextSeed());
//    }

    /**
     * Returns a pseudorandom {@code int} value between zero (inclusive)
     * and the specified bound (exclusive).
     *
     * @param bound the upper bound (exclusive).  Must be positive.
     * @return a pseudorandom {@code int} value between zero
     *         (inclusive) and the bound (exclusive)
     * @throws IllegalArgumentException if {@code bound} is not positive
     */
//    public int nextInt(int bound) {
//        if (bound <= 0)
//            throw new IllegalArgumentException(BAD_BOUND);
//        int r = mix32(nextSeed());
//        int m = bound - 1;
//        if ((bound & m) == 0) // power of two
//            r &= m;
//        else { // reject over-represented candidates
//            for (int u = r >>> 1;
//                 u + m - (r = u % bound) < 0;
//                 u = mix32(nextSeed()) >>> 1)
//                ;
//        }
//        return r;
//    }

    /**
     * Returns a pseudorandom {@code int} value between the specified
     * origin (inclusive) and the specified bound (exclusive).
     *
     * @param origin the least value returned
     * @param bound the upper bound (exclusive)
     * @return a pseudorandom {@code int} value between the origin
     *         (inclusive) and the bound (exclusive)
     * @throws IllegalArgumentException if {@code origin} is greater than
     *         or equal to {@code bound}
     */
//    public int nextInt(int origin, int bound) {
//        if (origin >= bound)
//            throw new IllegalArgumentException(BAD_RANGE);
//        return internalNextInt(origin, bound);
//    }

    /**
     * Returns a pseudorandom {@code long} value.
     *
     * @return a pseudorandom {@code long} value
     */
//    public long nextLong() {
//        return mix64(nextSeed());
//    }

    /**
     * Returns a pseudorandom {@code long} value between zero (inclusive)
     * and the specified bound (exclusive).
     *
     * @param bound the upper bound (exclusive).  Must be positive.
     * @return a pseudorandom {@code long} value between zero
     *         (inclusive) and the bound (exclusive)
     * @throws IllegalArgumentException if {@code bound} is not positive
     */
//    public long nextLong(long bound) {
//        if (bound <= 0)
//            throw new IllegalArgumentException(BAD_BOUND);
//        long r = mix64(nextSeed());
//        long m = bound - 1;
//        if ((bound & m) == 0L) // power of two
//            r &= m;
//        else { // reject over-represented candidates
//            for (long u = r >>> 1;
//                 u + m - (r = u % bound) < 0L;
//                 u = mix64(nextSeed()) >>> 1)
//                ;
//        }
//        return r;
//    }

    /**
     * Returns a pseudorandom {@code long} value between the specified
     * origin (inclusive) and the specified bound (exclusive).
     *
     * @param origin the least value returned
     * @param bound the upper bound (exclusive)
     * @return a pseudorandom {@code long} value between the origin
     *         (inclusive) and the bound (exclusive)
     * @throws IllegalArgumentException if {@code origin} is greater than
     *         or equal to {@code bound}
     */
//    public long nextLong(long origin, long bound) {
//        if (origin >= bound)
//            throw new IllegalArgumentException(BAD_RANGE);
//        return internalNextLong(origin, bound);
//    }

    /**
     * Returns a pseudorandom {@code double} value between zero
     * (inclusive) and one (exclusive).
     *
     * @return a pseudorandom {@code double} value between zero
     *         (inclusive) and one (exclusive)
     */
//    public double nextDouble() {
//        return (mix64(nextSeed()) >>> 11) * DOUBLE_UNIT;
//    }

    /**
     * Returns a pseudorandom {@code double} value between 0.0
     * (inclusive) and the specified bound (exclusive).
     *
     * @param bound the upper bound (exclusive).  Must be positive.
     * @return a pseudorandom {@code double} value between zero
     *         (inclusive) and the bound (exclusive)
     * @throws IllegalArgumentException if {@code bound} is not positive
//     */
//    public double nextDouble(double bound) {
//        if (!(bound > 0.0))
//            throw new IllegalArgumentException(BAD_BOUND);
//        double result = (mix64(nextSeed()) >>> 11) * DOUBLE_UNIT * bound;
//        return (result < bound) ? result : // correct for rounding
//            Double.longBitsToDouble(Double.doubleToLongBits(bound) - 1);
//    }

    /**
     * Returns a pseudorandom {@code double} value between the specified
     * origin (inclusive) and bound (exclusive).
     *
     * @param origin the least value returned
     * @param bound the upper bound (exclusive)
     * @return a pseudorandom {@code double} value between the origin
     *         (inclusive) and the bound (exclusive)
     * @throws IllegalArgumentException if {@code origin} is greater than
     *         or equal to {@code bound}
     */
//    public double nextDouble(double origin, double bound) {
//        if (!(origin < bound))
//            throw new IllegalArgumentException(BAD_RANGE);
//        return internalNextDouble(origin, bound);
//    }

    /**
     * Returns a pseudorandom {@code boolean} value.
     *
     * @return a pseudorandom {@code boolean} value
     */
//    public boolean nextBoolean() {
//        return mix32(nextSeed()) < 0;
//    }

    /**
     * Returns a pseudorandom {@code float} value between zero
     * (inclusive) and one (exclusive).
     *
     * @return a pseudorandom {@code float} value between zero
     *         (inclusive) and one (exclusive)
     */
//    public float nextFloat() {
//        return (mix32(nextSeed()) >>> 8) * FLOAT_UNIT;
//    }

//    public double nextGaussian() {
//        // Use nextLocalGaussian instead of nextGaussian field
//        Double d = nextLocalGaussian.get();
//        if (d != null) {
//            nextLocalGaussian.set(null);
//            return d.doubleValue();
//        }
//        double v1, v2, s;
//        do {
//            v1 = 2 * nextDouble() - 1; // between -1 and 1
//            v2 = 2 * nextDouble() - 1; // between -1 and 1
//            s = v1 * v1 + v2 * v2;
//        } while (s >= 1 || s == 0);
//        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
//        nextLocalGaussian.set(Double.valueOf(v2 * multiplier));
//        return v1 * multiplier;
//    }

    // stream methods, coded in a way intended to better isolate for
    // maintenance purposes the small differences across forms.

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandom {@code int} values.
     *
     * @param streamSize the number of values to generate
     * @return a stream of pseudorandom {@code int} values
     * @throws IllegalArgumentException if {@code streamSize} is
     *         less than zero
     * @since 1.8
     */
//    public IntStream ints(long streamSize) {
//        if (streamSize < 0L)
//            throw new IllegalArgumentException(BAD_SIZE);
//        return StreamSupport.intStream
//            (new RandomIntsSpliterator
//             (0L, streamSize, Integer.MAX_VALUE, 0),
//             false);
//    }

    /**
     * Returns an effectively unlimited stream of pseudorandom {@code int}
     * values.
     *
     * @implNote This method is implemented to be equivalent to {@code
     * ints(Long.MAX_VALUE)}.
     *
     * @return a stream of pseudorandom {@code int} values
     * @since 1.8
     */
//    public IntStream ints() {
//        return StreamSupport.intStream
//            (new RandomIntsSpliterator
//             (0L, Long.MAX_VALUE, Integer.MAX_VALUE, 0),
//             false);
//    }

    /**
     * Returns a stream producing the given {@code streamSize} number
     * of pseudorandom {@code int} values, each conforming to the given
     * origin (inclusive) and bound (exclusive).
     *
     * @param streamSize the number of values to generate
     * @param randomNumberOrigin the origin (inclusive) of each random value
     * @param randomNumberBound the bound (exclusive) of each random value
     * @return a stream of pseudorandom {@code int} values,
     *         each with the given origin (inclusive) and bound (exclusive)
     * @throws IllegalArgumentException if {@code streamSize} is
     *         less than zero, or {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     * @since 1.8
     */
//    public IntStream ints(long streamSize, int randomNumberOrigin,
//                          int randomNumberBound) {
//        if (streamSize < 0L)
//            throw new IllegalArgumentException(BAD_SIZE);
//        if (randomNumberOrigin >= randomNumberBound)
//            throw new IllegalArgumentException(BAD_RANGE);
//        return StreamSupport.intStream
//            (new RandomIntsSpliterator
//             (0L, streamSize, randomNumberOrigin, randomNumberBound),
//             false);
//    }

    /**
     * Returns an effectively unlimited stream of pseudorandom {@code
     * int} values, each conforming to the given origin (inclusive) and bound
     * (exclusive).
     *
     * @implNote This method is implemented to be equivalent to {@code
     * ints(Long.MAX_VALUE, randomNumberOrigin, randomNumberBound)}.
     *
     * @param randomNumberOrigin the origin (inclusive) of each random value
     * @param randomNumberBound the bound (exclusive) of each random value
     * @return a stream of pseudorandom {@code int} values,
     *         each with the given origin (inclusive) and bound (exclusive)
     * @throws IllegalArgumentException if {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     * @since 1.8
     */
//    public IntStream ints(int randomNumberOrigin, int randomNumberBound) {
//        if (randomNumberOrigin >= randomNumberBound)
//            throw new IllegalArgumentException(BAD_RANGE);
//        return StreamSupport.intStream
//            (new RandomIntsSpliterator
//             (0L, Long.MAX_VALUE, randomNumberOrigin, randomNumberBound),
//             false);
//    }

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandom {@code long} values.
     *
     * @param streamSize the number of values to generate
     * @return a stream of pseudorandom {@code long} values
     * @throws IllegalArgumentException if {@code streamSize} is
     *         less than zero
     * @since 1.8
     */
//    public LongStream longs(long streamSize) {
//        if (streamSize < 0L)
//            throw new IllegalArgumentException(BAD_SIZE);
//        return StreamSupport.longStream
//            (new RandomLongsSpliterator
//             (0L, streamSize, Long.MAX_VALUE, 0L),
//             false);
//    }

    /**
     * Returns an effectively unlimited stream of pseudorandom {@code long}
     * values.
     *
     * @implNote This method is implemented to be equivalent to {@code
     * longs(Long.MAX_VALUE)}.
     *
     * @return a stream of pseudorandom {@code long} values
     * @since 1.8
     */
//    public LongStream longs() {
//        return StreamSupport.longStream
//            (new RandomLongsSpliterator
//             (0L, Long.MAX_VALUE, Long.MAX_VALUE, 0L),
//             false);
//    }

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandom {@code long}, each conforming to the given origin
     * (inclusive) and bound (exclusive).
     *
     * @param streamSize the number of values to generate
     * @param randomNumberOrigin the origin (inclusive) of each random value
     * @param randomNumberBound the bound (exclusive) of each random value
     * @return a stream of pseudorandom {@code long} values,
     *         each with the given origin (inclusive) and bound (exclusive)
     * @throws IllegalArgumentException if {@code streamSize} is
     *         less than zero, or {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     * @since 1.8
     */
//    public LongStream longs(long streamSize, long randomNumberOrigin,
//                            long randomNumberBound) {
//        if (streamSize < 0L)
//            throw new IllegalArgumentException(BAD_SIZE);
//        if (randomNumberOrigin >= randomNumberBound)
//            throw new IllegalArgumentException(BAD_RANGE);
//        return StreamSupport.longStream
//            (new RandomLongsSpliterator
//             (0L, streamSize, randomNumberOrigin, randomNumberBound),
//             false);
//    }

    /**
     * Returns an effectively unlimited stream of pseudorandom {@code
     * long} values, each conforming to the given origin (inclusive) and bound
     * (exclusive).
     *
     * @implNote This method is implemented to be equivalent to {@code
     * longs(Long.MAX_VALUE, randomNumberOrigin, randomNumberBound)}.
     *
     * @param randomNumberOrigin the origin (inclusive) of each random value
     * @param randomNumberBound the bound (exclusive) of each random value
     * @return a stream of pseudorandom {@code long} values,
     *         each with the given origin (inclusive) and bound (exclusive)
     * @throws IllegalArgumentException if {@code randomNumberOrigin}
     *         is greater than or equal to {@code randomNumberBound}
     * @since 1.8
     */
//    public LongStream longs(long randomNumberOrigin, long randomNumberBound) {
//        if (randomNumberOrigin >= randomNumberBound)
//            throw new IllegalArgumentException(BAD_RANGE);
//        return StreamSupport.longStream
//            (new RandomLongsSpliterator
//             (0L, Long.MAX_VALUE, randomNumberOrigin, randomNumberBound),
//             false);
//    }
//
//    /**
//     * Returns a stream producing the given {@code streamSize} number of
//     * pseudorandom {@code double} values, each between zero
//     * (inclusive) and one (exclusive).
//     *
//     * @param streamSize the number of values to generate
//     * @return a stream of {@code double} values
//     * @throws IllegalArgumentException if {@code streamSize} is
//     *         less than zero
//     * @since 1.8
//     */
//    public DoubleStream doubles(long streamSize) {
//        if (streamSize < 0L)
//            throw new IllegalArgumentException(BAD_SIZE);
//        return StreamSupport.doubleStream
//            (new RandomDoublesSpliterator
//             (0L, streamSize, Double.MAX_VALUE, 0.0),
//             false);
//    }
//
//    /**
//     * Returns an effectively unlimited stream of pseudorandom {@code
//     * double} values, each between zero (inclusive) and one
//     * (exclusive).
//     *
//     * @implNote This method is implemented to be equivalent to {@code
//     * doubles(Long.MAX_VALUE)}.
//     *
//     * @return a stream of pseudorandom {@code double} values
//     * @since 1.8
//     */
//    public DoubleStream doubles() {
//        return StreamSupport.doubleStream
//            (new RandomDoublesSpliterator
//             (0L, Long.MAX_VALUE, Double.MAX_VALUE, 0.0),
//             false);
//    }
//
//    /**
//     * Returns a stream producing the given {@code streamSize} number of
//     * pseudorandom {@code double} values, each conforming to the given origin
//     * (inclusive) and bound (exclusive).
//     *
//     * @param streamSize the number of values to generate
//     * @param randomNumberOrigin the origin (inclusive) of each random value
//     * @param randomNumberBound the bound (exclusive) of each random value
//     * @return a stream of pseudorandom {@code double} values,
//     *         each with the given origin (inclusive) and bound (exclusive)
//     * @throws IllegalArgumentException if {@code streamSize} is
//     *         less than zero, or {@code randomNumberOrigin}
//     *         is greater than or equal to {@code randomNumberBound}
//     * @since 1.8
//     */
//    public DoubleStream doubles(long streamSize, double randomNumberOrigin,
//                                double randomNumberBound) {
//        if (streamSize < 0L)
//            throw new IllegalArgumentException(BAD_SIZE);
//        if (!(randomNumberOrigin < randomNumberBound))
//            throw new IllegalArgumentException(BAD_RANGE);
//        return StreamSupport.doubleStream
//            (new RandomDoublesSpliterator
//             (0L, streamSize, randomNumberOrigin, randomNumberBound),
//             false);
//    }
//
//    /**
//     * Returns an effectively unlimited stream of pseudorandom {@code
//     * double} values, each conforming to the given origin (inclusive) and bound
//     * (exclusive).
//     *
//     * @implNote This method is implemented to be equivalent to {@code
//     * doubles(Long.MAX_VALUE, randomNumberOrigin, randomNumberBound)}.
//     *
//     * @param randomNumberOrigin the origin (inclusive) of each random value
//     * @param randomNumberBound the bound (exclusive) of each random value
//     * @return a stream of pseudorandom {@code double} values,
//     *         each with the given origin (inclusive) and bound (exclusive)
//     * @throws IllegalArgumentException if {@code randomNumberOrigin}
//     *         is greater than or equal to {@code randomNumberBound}
//     * @since 1.8
//     */
//    public DoubleStream doubles(double randomNumberOrigin, double randomNumberBound) {
//        if (!(randomNumberOrigin < randomNumberBound))
//            throw new IllegalArgumentException(BAD_RANGE);
//        return StreamSupport.doubleStream
//            (new RandomDoublesSpliterator
//             (0L, Long.MAX_VALUE, randomNumberOrigin, randomNumberBound),
//             false);
//    }
//
//    /**
//     * Spliterator for int streams.  We multiplex the four int
//     * versions into one class by treating a bound less than origin as
//     * unbounded, and also by treating "infinite" as equivalent to
//     * Long.MAX_VALUE. For splits, it uses the standard divide-by-two
//     * approach. The long and double versions of this class are
//     * identical except for types.
//     */
//    private static final class RandomIntsSpliterator
//            implements Spliterator.OfInt {
//        long index;
//        final long fence;
//        final int origin;
//        final int bound;
//        RandomIntsSpliterator(long index, long fence,
//                              int origin, int bound) {
//            this.index = index; this.fence = fence;
//            this.origin = origin; this.bound = bound;
//        }
//
//        public RandomIntsSpliterator trySplit() {
//            long i = index, m = (i + fence) >>> 1;
//            return (m <= i) ? null :
//                new RandomIntsSpliterator(i, index = m, origin, bound);
//        }
//
//        public long estimateSize() {
//            return fence - index;
//        }
//
//        public int characteristics() {
//            return (Spliterator.SIZED | Spliterator.SUBSIZED |
//                    Spliterator.NONNULL | Spliterator.IMMUTABLE);
//        }
//
//        public boolean tryAdvance(IntConsumer consumer) {
//            if (consumer == null) throw new NullPointerException();
//            long i = index, f = fence;
//            if (i < f) {
//                consumer.accept(ThreadLocalRandom.current().internalNextInt(origin, bound));
//                index = i + 1;
//                return true;
//            }
//            return false;
//        }
//
//        public void forEachRemaining(IntConsumer consumer) {
//            if (consumer == null) throw new NullPointerException();
//            long i = index, f = fence;
//            if (i < f) {
//                index = f;
//                int o = origin, b = bound;
//                ThreadLocalRandom rng = ThreadLocalRandom.current();
//                do {
//                    consumer.accept(rng.internalNextInt(o, b));
//                } while (++i < f);
//            }
//        }
//    }
//
//    /**
//     * Spliterator for long streams.
//     */
//    private static final class RandomLongsSpliterator
//            implements Spliterator.OfLong {
//        long index;
//        final long fence;
//        final long origin;
//        final long bound;
//        RandomLongsSpliterator(long index, long fence,
//                               long origin, long bound) {
//            this.index = index; this.fence = fence;
//            this.origin = origin; this.bound = bound;
//        }
//
//        public RandomLongsSpliterator trySplit() {
//            long i = index, m = (i + fence) >>> 1;
//            return (m <= i) ? null :
//                new RandomLongsSpliterator(i, index = m, origin, bound);
//        }
//
//        public long estimateSize() {
//            return fence - index;
//        }
//
//        public int characteristics() {
//            return (Spliterator.SIZED | Spliterator.SUBSIZED |
//                    Spliterator.NONNULL | Spliterator.IMMUTABLE);
//        }
//
//        public boolean tryAdvance(LongConsumer consumer) {
//            if (consumer == null) throw new NullPointerException();
//            long i = index, f = fence;
//            if (i < f) {
//                consumer.accept(ThreadLocalRandom.current().internalNextLong(origin, bound));
//                index = i + 1;
//                return true;
//            }
//            return false;
//        }
//
//        public void forEachRemaining(LongConsumer consumer) {
//            if (consumer == null) throw new NullPointerException();
//            long i = index, f = fence;
//            if (i < f) {
//                index = f;
//                long o = origin, b = bound;
//                ThreadLocalRandom rng = ThreadLocalRandom.current();
//                do {
//                    consumer.accept(rng.internalNextLong(o, b));
//                } while (++i < f);
//            }
//        }
//
//    }
//
//    /**
//     * Spliterator for double streams.
//     */
//    private static final class RandomDoublesSpliterator
//            implements Spliterator.OfDouble {
//        long index;
//        final long fence;
//        final double origin;
//        final double bound;
//        RandomDoublesSpliterator(long index, long fence,
//                                 double origin, double bound) {
//            this.index = index; this.fence = fence;
//            this.origin = origin; this.bound = bound;
//        }
//
//        public RandomDoublesSpliterator trySplit() {
//            long i = index, m = (i + fence) >>> 1;
//            return (m <= i) ? null :
//                new RandomDoublesSpliterator(i, index = m, origin, bound);
//        }
//
//        public long estimateSize() {
//            return fence - index;
//        }
//
//        public int characteristics() {
//            return (Spliterator.SIZED | Spliterator.SUBSIZED |
//                    Spliterator.NONNULL | Spliterator.IMMUTABLE);
//        }
//
//        public boolean tryAdvance(DoubleConsumer consumer) {
//            if (consumer == null) throw new NullPointerException();
//            long i = index, f = fence;
//            if (i < f) {
//                consumer.accept(ThreadLocalRandom.current().internalNextDouble(origin, bound));
//                index = i + 1;
//                return true;
//            }
//            return false;
//        }
//
//        public void forEachRemaining(DoubleConsumer consumer) {
//            if (consumer == null) throw new NullPointerException();
//            long i = index, f = fence;
//            if (i < f) {
//                index = f;
//                double o = origin, b = bound;
//                ThreadLocalRandom rng = ThreadLocalRandom.current();
//                do {
//                    consumer.accept(rng.internalNextDouble(o, b));
//                } while (++i < f);
//            }
//        }
//    }
    // END Android-changed: Keep version 11 implementation.


