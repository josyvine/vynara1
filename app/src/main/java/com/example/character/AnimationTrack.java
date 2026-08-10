package com.example.character;

import java.util.ArrayList;
import java.util.List;

public class AnimationTrack {
    private String boneSemanticName;
    private final List<Keyframe> keyframes = new ArrayList<>();

    public AnimationTrack(String boneSemanticName) {
        this.boneSemanticName = boneSemanticName;
    }

    public String getBoneSemanticName() { return boneSemanticName; }
    public List<Keyframe> getKeyframes() { return keyframes; }

    public AnimationTrack addKeyframe(Keyframe kf) {
        keyframes.add(kf);
        return this;
    }
}
