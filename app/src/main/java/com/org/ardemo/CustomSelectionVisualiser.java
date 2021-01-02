/*
 * Copyright 2018 Google LLC All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.org.ardemo;


import androidx.annotation.Nullable;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.BaseTransformableNode;
import com.google.ar.sceneform.ux.SelectionVisualizer;

/**
 * Visualizes that a {@link BaseTransformableNode} is selected by rendering a footprint for the
 * selected node.
 */
public class CustomSelectionVisualiser implements SelectionVisualizer {
    private final Node footprintNode;
    @Nullable
    private ViewRenderable footprintRenderable;

    public CustomSelectionVisualiser() {
        footprintNode = new Node();
    }

    public void setFootprintRenderable(ViewRenderable renderable) {
        ViewRenderable copyRenderable = renderable.makeCopy();
        footprintNode.setRenderable(copyRenderable);
        Quaternion q1 = Quaternion.axisAngle(new Vector3(1f, 0, 0), 90f);
        Quaternion q2 = Quaternion.axisAngle(new Vector3(0, 0, 1f), 40f);

        footprintNode.setLocalRotation(Quaternion.multiply(q1,q2));


        copyRenderable.setCollisionShape(null);
        footprintRenderable = copyRenderable;
    }

    @Nullable
    public ViewRenderable getFootprintRenderable() {
        return footprintRenderable;
    }

    @Override
    public void applySelectionVisual(BaseTransformableNode node) {
        footprintNode.setParent(node);
    }

    @Override
    public void removeSelectionVisual(BaseTransformableNode node) {
        footprintNode.setParent(null);
    }
}
