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

package com.aionjhungary.geolib.scene;

import com.aionjhungary.geolib.bounding.BoundingVolume;
import com.aionjhungary.geolib.collision.Collidable;
import com.aionjhungary.geolib.collision.CollisionResults;
import com.aionjhungary.geolib.math.Matrix4f;
import com.aionjhungary.geolib.scene.VertexBuffer.Type;
import com.aionjhungary.geolib.utils.TempVars;

public class Geometry extends Spatial {

    /**
     * The mesh contained herein
     */
    protected Mesh mesh;

    /**
     * When true, the geometry's transform will not be applied.
     */
    protected boolean ignoreTransform = false;

    protected transient Matrix4f cachedWorldMat = new Matrix4f();

    /**
     * Do not use this constructor. Serialization purposes only.
     */
    public Geometry(){
    }

    /**
     * Create a geometry node without any mesh data.
     * @param name The name of this geometry
     */
    public Geometry(String name){
        super(name);
    }

    /**
     * Create a geometry node with mesh data.
     *
     * @param name The name of this geometry
     * @param mesh The mesh data for this geometry
     */
    public Geometry(String name, Mesh mesh){
        this(name);
        if (mesh == null)
            throw new NullPointerException();

        this.mesh = mesh;
    }

    /**
     * @return If ignoreTransform mode is set.
     * @see Geometry#setIgnoreTransform(boolean) 
     */
    public boolean isIgnoreTransform() {
        return ignoreTransform;
    }

    /**
     * @param ignoreTransform If true, the geometry's transform will not be applied.
     */
    public void setIgnoreTransform(boolean ignoreTransform) {
        this.ignoreTransform = ignoreTransform;
    }

    public int getVertexCount(){
        return mesh.getVertexCount();
    }

    public int getTriangleCount(){
        return mesh.getTriangleCount();
    }

    public void setMesh(Mesh mesh){
        //if (mesh == null)
           // throw new NullPointerException();

        this.mesh = mesh;
        setBoundRefresh();
    }

    public Mesh getMesh(){
        return mesh;
    }

    /**
     * @return The bounding volume of the mesh, in model space.
     */
    public BoundingVolume getModelBound(){
        return mesh.getBound();
    }

    /**
     * Updates the bounding volume of the mesh. Should be called when the
     * mesh has been modified.
     */
    public void updateModelBound() {
        mesh.updateBound();
        setBoundRefresh();
    }

    /**
     * <code>updateWorldBound</code> updates the bounding volume that contains
     * this geometry. The location of the geometry is based on the location of
     * all this node's parents.
     *
     * @see com.jme.scene.Spatial#updateWorldBound()
     */
    @Override
    protected void updateWorldBound() {
        super.updateWorldBound();
        if (mesh == null)
            throw new NullPointerException("Geometry: "+getName()+" has null mesh");

        if (mesh.getBound() != null) {
            if (ignoreTransform){
                // we do not transform the model bound by the world transform,
                // just use the model bound as-is
                worldBound = mesh.getBound().clone(worldBound);
            }else{
                worldBound = mesh.getBound().transform(worldTransform, worldBound);
            }
        }
    }

    @Override
    protected void updateWorldTransforms(){
        super.updateWorldTransforms();

        cachedWorldMat.loadIdentity();
        cachedWorldMat.setRotationQuaternion(worldTransform.getRotation());
        cachedWorldMat.setTranslation(worldTransform.getTranslation());

        assert TempVars.get().lock();
        Matrix4f scaleMat = TempVars.get().tempMat4;
        scaleMat.loadIdentity();
        scaleMat.scale(worldTransform.getScale());
        cachedWorldMat.multLocal(scaleMat);
        assert TempVars.get().unlock();

    }

    public Matrix4f getWorldMatrix(){
        return cachedWorldMat;
    }

    @Override
    public void setModelBound(BoundingVolume modelBound) {
        this.worldBound = null;
        mesh.setBound(modelBound);
        updateModelBound();
    }

    public int collideWith(Collidable other, CollisionResults results){
        if (refreshFlags != 0)
            throw new IllegalStateException("Scene graph must be updated" +
                                            " before checking collision");

        if (mesh != null){
            // NOTE: BIHTree in mesh already checks collision with the
            // mesh's bound
            int added = mesh.collideWith(other, cachedWorldMat, worldBound, results);
            return added;
        }
        return 0;
    }

    /**
     * This version of clone is a shallow clone, in other words, the
     * same mesh is referenced as the original geometry.
     * Exception: if the mesh is marked as being a software
     * animated mesh, (bind pose is set) then the positions
     * and normals are deep copied.
     * @return
     */
    @Override
    public Geometry clone(){
        Geometry geomClone = (Geometry) super.clone();
        geomClone.cachedWorldMat = cachedWorldMat.clone();
        
        if (mesh.getBuffer(Type.BindPosePosition) != null){
            geomClone.mesh = mesh.cloneForAnim();
        }
        return geomClone;
    }

    /**
     * Creates a deep clone of the geometry,
     * this creates an identical copy of the mesh
     * with the vertexbuffer data duplicated.
     * @return
     */
    @Override
    public Spatial deepClone(){
        Geometry geomClone = clone();
        geomClone.mesh = mesh.deepClone();
        return geomClone;
    }
}
