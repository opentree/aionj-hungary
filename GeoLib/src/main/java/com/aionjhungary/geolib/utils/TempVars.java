/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.aionjhungary.geolib.utils;

import java.util.ArrayList;

import com.aionjhungary.geolib.collision.bih.BIHNode.BIHStackData;
import com.aionjhungary.geolib.math.Matrix3f;
import com.aionjhungary.geolib.math.Matrix4f;
import com.aionjhungary.geolib.math.Quaternion;
import com.aionjhungary.geolib.math.Triangle;
import com.aionjhungary.geolib.math.Vector2f;
import com.aionjhungary.geolib.math.Vector3f;

/**
 * Temporary variables assigned to each thread. Engine classes may access
 * these temp variables with TempVars.get(). A method using temp vars with this
 * class is not allowed to make calls to other methods using the class otherwise
 * memory corruption will occur. A locking mechanism may be implemented
 * in the future to prevent the occurance of such situation.
 */
public class TempVars {

    private static final ThreadLocal<TempVars> varsLocal
            = new ThreadLocal<TempVars>(){
        @Override
        public TempVars initialValue(){
            return new TempVars();
        }
    };

    public static TempVars get(){
        return varsLocal.get();
    }

    private TempVars(){
    }

    private boolean locked = false;
    private StackTraceElement[] lockerStack;

    public final boolean lock(){
        if (locked){
           System.err.println("INTERNAL ERROR");
           System.err.println("Offending trace: ");

           StackTraceElement[] stack = new Throwable().getStackTrace();
           for (int i = 1; i < stack.length; i++){
               System.err.println("\tat "+stack[i].toString());
           }

           System.err.println("Attempted to aquire TempVars lock owned by");
           for (int i = 1; i < lockerStack.length; i++){
               System.err.println("\tat "+lockerStack[i].toString());
           }
           System.exit(1);
           return false;
        }

        lockerStack = new Throwable().getStackTrace();
        locked = true;
        return true;
    }

    public final boolean unlock(){
        if (!locked){
            System.err.println("INTERNAL ERROR");
            System.err.println("Attempted to release non-existent lock: ");

            StackTraceElement[] stack = new Throwable().getStackTrace();
            for (int i = 1; i < stack.length; i++){
                System.err.println("\tat "+stack[i].toString());
            }

            System.exit(1);
            return false;
        }

        lockerStack = null;
        locked = false;
        return true;
    }
    
    /**
     * Fetching triangle from mesh
     */
    public final Triangle triangle = new Triangle();

    /**
     * General vectors.
     */
    public final Vector3f vect1 = new Vector3f();
    public final Vector3f vect2 = new Vector3f();
    public final Vector3f vect3 = new Vector3f();
    public final Vector3f vect4 = new Vector3f();
    public final Vector3f vect5 = new Vector3f();

    /**
     * 2D vector
     */
    public final Vector2f vect2d  = new Vector2f();

    /**
     * General matrices.
     */
    public final Matrix3f tempMat3 = new Matrix3f();
    public final Matrix4f tempMat4 = new Matrix4f();

    /**
     * General quaternions.
     */
    public final Quaternion quat1 = new Quaternion();

    /**
     * BoundingBox ray collision
     */
    public final float[] fWdU = new float[3];
    public final float[] fAWdU = new float[3];
    public final float[] fDdU = new float[3];
    public final float[] fADdU = new float[3];
    public final float[] fAWxDdU = new float[3];

    /**
     * BIHTree
     */
    public final ArrayList<BIHStackData> bihStack = new ArrayList<BIHStackData>();

}
