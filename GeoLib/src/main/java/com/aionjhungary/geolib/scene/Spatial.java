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

import java.util.logging.Logger;

import com.aionjhungary.geolib.bounding.BoundingVolume;
import com.aionjhungary.geolib.collision.Collidable;
import com.aionjhungary.geolib.math.Matrix3f;
import com.aionjhungary.geolib.math.Matrix4f;
import com.aionjhungary.geolib.math.Quaternion;
import com.aionjhungary.geolib.math.Transform;
import com.aionjhungary.geolib.math.Vector3f;
import com.aionjhungary.geolib.utils.TempVars;


/**
 * <code>Spatial</code> defines the base class for scene graph nodes. It
 * maintains a link to a parent, it's local transforms and the world's
 * transforms. All other nodes, such as <code>Node</code> and
 * <code>Geometry</code> are subclasses of <code>Spatial</code>.
 *
 * @author Mark Powell
 * @author Joshua Slack
 * @version $Revision: 4075 $, $Data$
 */
public abstract class Spatial implements Collidable {

    private static final Logger logger = Logger.getLogger(Spatial.class.getName());

    public enum CullHint {
        /** 
         * Do whatever our parent does. If no parent, we'll default to dynamic.
         */
        Inherit,

        /**
         * Do not draw if we are not at least partially within the view frustum
         * of the renderer's camera.
         */
        Dynamic,

        /** 
         * Always cull this from view.
         */
        Always,

        /**
         * Never cull this from view. Note that we will still get culled if our
         * parent is culled.
         */
        Never;
    }

    /**
     * Refresh flag types
     */
    protected static final int RF_TRANSFORM = 0x01, // need light resort + combine transforms
                               RF_BOUND = 0x02;

    protected CullHint cullHint = CullHint.Inherit;

    /** 
     * Spatial's bounding volume relative to the world.
     */
    protected BoundingVolume worldBound;

    /** 
     * This spatial's name.
     */
    protected String name;

    public transient float queueDistance = Float.NEGATIVE_INFINITY;

    protected Transform localTransform;

    protected Transform worldTransform;



    /**
     * Refresh flags. Indicate what data of the spatial need to be
     * updated to reflect the correct state.
     */
    protected transient int refreshFlags = 0;

    /**
     * Default Constructor.
     */
    public Spatial() {
        localTransform = new Transform();
        worldTransform = new Transform();

        refreshFlags |= RF_BOUND;
    }

    /**
     * Constructor instantiates a new <code>Spatial</code> object setting the
     * rotation, translation and scale value to defaults.
     *
     * @param name
     *            the name of the scene element. This is required for
     *            identification and comparision purposes.
     */
    public Spatial(String name) {
        this();
        this.name = name;
    }

    /**
     * Indicate that the transform of this spatial has changed and that
     * a refresh is required.
     */
    protected void setTransformRefresh(){
        refreshFlags |= RF_TRANSFORM;
        setBoundRefresh();
    }

    /**
     * Indicate that the bounding of this spatial has changed and that
     * a refresh is required.
     */
    protected void setBoundRefresh(){
        refreshFlags |= RF_BOUND;
    }

    /**
     * Sets the name of this spatial.
     *
     * @param name
     *            The spatial's new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this spatial.
     *
     * @return This spatial's name.
     */
    public String getName() {
        return name;
    }

    /**
     * <code>getWorldRotation</code> retrieves the absolute rotation of the
     * Spatial.
     *
     * @return the Spatial's world rotation matrix.
     */
    public Quaternion getWorldRotation() {
        checkDoTransformUpdate();
        return worldTransform.getRotation();
    }

    /**
     * <code>getWorldTranslation</code> retrieves the absolute translation of
     * the spatial.
     *
     * @return the world's tranlsation vector.
     */
    public Vector3f getWorldTranslation() {
        checkDoTransformUpdate();
        return worldTransform.getTranslation();
    }

    /**
     * <code>getWorldScale</code> retrieves the absolute scale factor of the
     * spatial.
     *
     * @return the world's scale factor.
     */
    public Vector3f getWorldScale() {
        checkDoTransformUpdate();
        return worldTransform.getScale();
    }

    /**
     * <code>getWorldTransform</code> retrieves the world transformation
     * of the spatial.
     *
     * @return the world transform.
     */
    public Transform getWorldTransform(){
        checkDoTransformUpdate();
        return worldTransform;
    }

    /**
     * <code>rotateUpTo</code> is a util function that alters the
     * localrotation to point the Y axis in the direction given by newUp.
     *
     * @param newUp
     *            the up vector to use - assumed to be a unit vector.
     */
    public void rotateUpTo(Vector3f newUp) {
        TempVars vars = TempVars.get();
        assert vars.lock();

        Vector3f compVecA = vars.vect1;
        Quaternion q = vars.quat1;

        // First figure out the current up vector.
        Vector3f upY = compVecA.set(Vector3f.UNIT_Y);
        Quaternion rot = localTransform.getRotation();
        rot.multLocal(upY);

        // get angle between vectors
        float angle = upY.angleBetween(newUp);

        // figure out rotation axis by taking cross product
        Vector3f rotAxis = upY.crossLocal(newUp).normalizeLocal();

        // Build a rotation quat and apply current local rotation.
        q.fromAngleNormalAxis(angle, rotAxis);
        q.mult(rot, rot);

        assert vars.unlock();

        setTransformRefresh();
    }

    /**
     * <code>lookAt</code> is a convienence method for auto-setting the local
     * rotation based on a position and an up vector. It computes the rotation
     * to transform the z-axis to point onto 'position' and the y-axis to 'up'.
     * Unlike {@link Quaternion#lookAt} this method takes a world position to
     * look at not a relative direction.
     *
     * @param position
     *            where to look at in terms of world coordinates
     * @param upVector
     *            a vector indicating the (local) up direction. (typically {0,
     *            1, 0} in jME.)
     */
    public void lookAt(Vector3f position, Vector3f upVector) {
        Vector3f worldTranslation = getWorldTranslation();

        TempVars vars = TempVars.get();
        assert vars.lock();
        Vector3f compVecA = vars.vect4;
        assert vars.unlock();
        
        compVecA.set(position).subtractLocal(worldTranslation);
        getLocalRotation().lookAt(compVecA, upVector);

        setTransformRefresh();
    }

    /**
     * Should be overriden by Node and Geometry.
     */
    protected void updateWorldBound(){
        // the world bound of a leaf is the same as it's model bound
        // for a node, the world bound is a combination of all it's children
        // bounds
        // -> handled by subclass
        refreshFlags &= ~RF_BOUND;
    }

    /**
     * Should only be called from updateGeometricState().
     * In most cases should not be subclassed.
     */
    protected void updateWorldTransforms(){
    	worldTransform.set(localTransform);
    	refreshFlags &= ~RF_TRANSFORM;
    }

    void checkDoTransformUpdate(){
        if ( (refreshFlags & RF_TRANSFORM) == 0 )
            return;
        worldTransform.set(localTransform);
        refreshFlags &= ~RF_TRANSFORM;
    }

    void checkDoBoundUpdate(){
        if ( (refreshFlags & RF_BOUND) == 0 )
            return;

        checkDoTransformUpdate();

        // All children's bounds have been updated. Update my own now.
        updateWorldBound();
    }

    /**
     * <code>updateGeometricState</code> updates the lightlist,
     * computes the world transforms, and computes the world bounds
     * for this Spatial.
     * Calling this when the Spatial is attached to a node
     * will cause undefined results. User code should only call this
     * method on Spatials having no parent.
     * 
     * @see Spatial#getWorldLightList()
     * @see Spatial#getWorldTransform()
     * @see Spatial#getWorldBound()
     */
    public void updateGeometricState(){
        // assume that this Spatial is a leaf, a proper implementation
        // for this method should be provided by Node.

        // NOTE: Update world transforms first because
        // bound transform depends on them.
        if ((refreshFlags & RF_TRANSFORM) != 0){
            updateWorldTransforms();
        }
        if ((refreshFlags & RF_BOUND) != 0){
            updateWorldBound();
        }

        assert refreshFlags == 0;
    }

    /**
     * Convert a vector (in) from this spatials' local coordinate space to world
     * coordinate space.
     *
     * @param in
     *            vector to read from
     * @param store
     *            where to write the result (null to create a new vector, may be
     *            same as in)
     * @return the result (store)
     */
    public Vector3f localToWorld(final Vector3f in, Vector3f store) {
        return worldTransform.transformVector(in, store);
    }

    /**
     * Convert a vector (in) from world coordinate space to this spatials' local
     * coordinate space.
     *
     * @param in
     *            vector to read from
     * @param store
     *            where to write the result
     * @return the result (store)
     */
    public Vector3f worldToLocal(final Vector3f in, final Vector3f store) {
        return worldTransform.transformInverseVector(in, store);
    }

    /**
     * <code>getLocalRotation</code> retrieves the local rotation of this
     * node.
     *
     * @return the local rotation of this node.
     */
    public Quaternion getLocalRotation() {
        return localTransform.getRotation();
    }

    /**
     * <code>setLocalRotation</code> sets the local rotation of this node.
     *
     * @param rotation
     *            the new local rotation.
     */
    public void setLocalRotation(Matrix3f rotation) {
        localTransform.getRotation().fromRotationMatrix(rotation);
        this.worldTransform.setRotation(this.localTransform.getRotation());
        setTransformRefresh();
    }

    /**
     * <code>setLocalRotation</code> sets the local rotation of this node,
     * using a quaterion to build the matrix.
     *
     * @param quaternion
     *            the quaternion that defines the matrix.
     */
    public void setLocalRotation(Quaternion quaternion) {
        localTransform.setRotation(quaternion);
        this.worldTransform.setRotation(this.localTransform.getRotation());
        setTransformRefresh();
    }

    /**
     * <code>getLocalScale</code> retrieves the local scale of this node.
     *
     * @return the local scale of this node.
     */
    public Vector3f getLocalScale() {
        return localTransform.getScale();
    }

    /**
     * <code>setLocalScale</code> sets the local scale of this node.
     *
     * @param localScale
     *            the new local scale, applied to x, y and z
     */
    public void setLocalScale(float localScale) {
        localTransform.setScale(localScale);
        worldTransform.setScale(localTransform.getScale());
        setTransformRefresh();
    }

    /**
     * <code>setLocalScale</code> sets the local scale of this node.
     *
     * @param localScale
     *            the new local scale
     */
    public void setLocalScale(float x, float y, float z) {
        localTransform.setScale(x, y, z);
        worldTransform.setScale(localTransform.getScale());
        setTransformRefresh();
    }

    /**
     * <code>setLocalScale</code> sets the local scale of this node.
     *
     * @param localScale
     *            the new local scale.
     */
    public void setLocalScale(Vector3f localScale) {
        localTransform.setScale(localScale);
        worldTransform.setScale(localTransform.getScale());
        setTransformRefresh();
    }

    /**
     * <code>getLocalTranslation</code> retrieves the local translation of
     * this node.
     *
     * @return the local translation of this node.
     */
    public Vector3f getLocalTranslation() {
        return localTransform.getTranslation();
    }

    /**
     * <code>setLocalTranslation</code> sets the local translation of this
     * spatial.
     *
     * @param localTranslation
     *            the local translation of this spatial.
     */
    public void setLocalTranslation(Vector3f localTranslation) {
        this.localTransform.setTranslation(localTranslation);
        this.worldTransform.setTranslation(this.localTransform.getTranslation());
        setTransformRefresh();
    }

    /**
     * <code>setLocalTranslation</code> sets the local translation of this
     * spatial.
     */
    public void setLocalTranslation(float x, float y, float z) {
        this.localTransform.setTranslation(x,y,z);
        this.worldTransform.setTranslation(this.localTransform.getTranslation());
        setTransformRefresh();
    }

    /**
     * <code>setLocalTransform</code> sets the local transform of this
     * spatial.
     */
    public void setLocalTransform(Transform t) {
        this.localTransform.set(t);
        setTransformRefresh();
    }

    /**
     * <code>getLocalTransform</code> retrieves the local transform of
     * this spatial.
     *
     * @return the local transform of this spatial.
     */
    public Transform getLocalTransform(){
        return localTransform;
    }

    /**
     * Translates the spatial by the given translation vector.
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial move(float x, float y, float z){
        this.localTransform.getTranslation().addLocal(x, y, z);
        this.worldTransform.setTranslation(this.localTransform.getTranslation());
        setTransformRefresh();

        return this;
    }

    /**
     * Translates the spatial by the given translation vector.
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial move(Vector3f offset){
        this.localTransform.getTranslation().addLocal(offset);
        this.worldTransform.setTranslation(this.localTransform.getTranslation());
        setTransformRefresh();

        return this;
    }

    /**
     * Scales the spatial by the given value
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial scale(float s){
        return scale(s,s,s);
    }

    /**
     * Scales the spatial by the given scale vector.
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial scale(float x, float y, float z){
        this.localTransform.getScale().multLocal(x,y,z);
        this.worldTransform.setScale(this.localTransform.getScale());
        setTransformRefresh();

        return this;
    }

    /**
     * Rotates the spatial by the given rotation.
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial rotate(Quaternion rot){
        this.localTransform.getRotation().multLocal(rot);
        this.worldTransform.setRotation(this.localTransform.getRotation());
        setTransformRefresh();

        return this;
    }

    /**
     * Rotates the spatial by the yaw, roll and pitch angles (in radians),
     * in the local coordinate space.
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial rotate(float yaw, float roll, float pitch){
        assert TempVars.get().lock();
        Quaternion q = TempVars.get().quat1;
        q.fromAngles(yaw, roll, pitch);
        rotate(q);
        assert TempVars.get().unlock();

        return this;
    }

    /**
     * Centers the spatial in the origin, the spatial should have no
     * parent when this method is called.
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial center(){
        if ((refreshFlags & RF_BOUND) != 0){
            updateGeometricState();
        }

        Vector3f worldTrans = getWorldTranslation();
        Vector3f worldCenter = getWorldBound().getCenter();

        Vector3f absTrans = worldTrans.subtract(worldCenter);
        setLocalTranslation(absTrans);

        return this;
    }

    /**
     * @see #setCullHint(CullHint)
     * @return the cull mode of this spatial, or if set to CullHint.Inherit,
     * the cullmode of it's parent.
     */
    public CullHint getCullHint() {
        if (cullHint != CullHint.Inherit)
            return cullHint;
        else
            return CullHint.Dynamic;
    }

    /**
     * Sets the level of detail to use when rendering this Spatial,
     * this call propagates to all geometries under this Spatial.
     *
     * @param lod The lod level to set.
     */
    public void setLodLevel(int lod){
    }

    /**
     * <code>updateModelBound</code> recalculates the bounding object for this
     * Spatial.
     */
    public abstract void updateModelBound();

    /**
     * <code>setModelBound</code> sets the bounding object for this Spatial.
     *
     * @param modelBound
     *            the bounding object for this spatial.
     */
    public abstract void setModelBound(BoundingVolume modelBound);

    /**
     * @return The sum of all verticies under this Spatial.
     */
    public abstract int getVertexCount();

    /**
     * @return The sum of all triangles under this Spatial.
     */
    public abstract int getTriangleCount();

    /**
     * @return A clone of this Spatial, the scene graph in its entirety
     * is cloned and can be altered independently of the original scene graph.
     *
     * Note that meshes of geometries are not cloned explicetly, they
     * are shared if static, or specially cloned if animated.
     *
     * All controls will be cloned using the Control.cloneForSpatial method
     * on the clone.
     *
     * @see Mesh#cloneForAnim() 
     */
    @Override
    public Spatial clone(){
        try{
            Spatial clone = (Spatial) super.clone();
            if (worldBound != null)
                clone.worldBound = worldBound.clone();


            clone.worldTransform = worldTransform.clone();
            clone.localTransform = localTransform.clone();

            clone.setBoundRefresh();
            clone.setTransformRefresh();

            return clone;
        }catch (CloneNotSupportedException ex){
            throw new AssertionError();
        }
    }

    /**
     * @return Similar to Spatial.clone() except will create a deep clone
     * of all geometry's meshes, normally this method shouldn't be used
     * instead use Spatial.clone()
     *
     * @see Spatial#clone()
     */
    public abstract Spatial deepClone();

    /**
     * Note that we are <i>matching</i> the pattern, therefore the pattern
     * must match the entire pattern (i.e. it behaves as if it is sandwiched
     * between "^" and "$").
     * You can set regex modes, like case insensitivity, by using the (?X)
     * or (?X:Y) constructs.
     *
     * @param spatialSubclass Subclass which this must implement.
     *                        Null causes all Spatials to qualify.
     * @param nameRegex  Regular expression to match this name against.
     *                        Null causes all Names to qualify.
     * @return true if this implements the specified class and this's name
     *         matches the specified pattern.
     *
     * @see java.util.regex.Pattern
     */
    public boolean matches(Class<? extends Spatial> spatialSubclass,
                                String nameRegex) {
        if (spatialSubclass != null && !spatialSubclass.isInstance(this))
            return false;
      
        if (nameRegex != null && (name == null || !name.matches(nameRegex)))
            return false;

        return true;
    }

    /**
     * <code>getWorldBound</code> retrieves the world bound at this node
     * level.
     *
     * @return the world bound at this level.
     */
    public BoundingVolume getWorldBound() {
        checkDoBoundUpdate();
        return worldBound;
    }

    /**
     * <code>setCullHint</code> sets how scene culling should work on this
     * spatial during drawing. CullHint.Dynamic: Determine via the defined
     * Camera planes whether or not this Spatial should be culled.
     * CullHint.Always: Always throw away this object and any children during
     * draw commands. CullHint.Never: Never throw away this object (always draw
     * it) CullHint.Inherit: Look for a non-inherit parent and use its cull
     * mode. NOTE: You must set this AFTER attaching to a parent or it will be
     * reset with the parent's cullMode value.
     *
     * @param hint
     *            one of CullHint.Dynamic, CullHint.Always, CullHint.Inherit or
     *            CullHint.Never
     */
    public void setCullHint(CullHint hint) {
        cullHint = hint;
    }

    /**
     * @return the cullmode set on this Spatial
     */
    public CullHint getLocalCullHint() {
        return cullHint;
    }

    /**
     * Returns the Spatial's name followed by the class of the spatial <br>
     * Example: "MyNode (com.jme3.scene.Spatial)
     *
     * @return Spatial's name followed by the class of the Spatial
     */
    @Override
    public String toString() {
        return name + " (" + this.getClass().getSimpleName() + ')';
    }

    /**
     * Creates a transform matrix that will convert from this spatials'
     * local coordinate space to the world coordinate space
     * based on the world transform.
     *
     * @param store Matrix where to store the result, if null, a new one
     * will be created and returned.
     *
     * @return store if not null, otherwise, a new matrix containing the result.
     *
     * @see Spatial#getWorldTransform() 
     */
    public Matrix4f getLocalToWorldMatrix(Matrix4f store) {
        if (store == null) {
            store = new Matrix4f();
        } else {
            store.loadIdentity();
        }
        // multiply with scale first, then rotate, finally translate (cf.
        // Eberly)
        store.scale(getWorldScale());
        store.multLocal(getWorldRotation());
        store.setTranslation(getWorldTranslation());
        return store;
    }
    
}

