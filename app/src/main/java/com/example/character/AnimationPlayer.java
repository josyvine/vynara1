package com.example.character;

import java.util.HashMap;
import java.util.Map;

public class AnimationPlayer {
    private Skeleton skeleton;
    private final Map<String, AnimationClip> clipLibrary = new HashMap<>();
    private AnimationClip activeClip;
    private float currentTimeSeconds = 0f;
    private boolean isPlaying = false;

    public AnimationPlayer(Skeleton skeleton) {
        this.skeleton = skeleton;
        loadDefaultClips();
    }

    private void loadDefaultClips() {
        // Walk animation clip
        AnimationClip walk = new AnimationClip("walk", 1.2f, true);
        AnimationTrack lLegTrack = new AnimationTrack("LEFT_LEG")
                .addKeyframe(new Keyframe(0.0f, 0, 0, 0, 25f, 0, 0))
                .addKeyframe(new Keyframe(0.6f, 0, 0, 0, -25f, 0, 0))
                .addKeyframe(new Keyframe(1.2f, 0, 0, 0, 25f, 0, 0));
        AnimationTrack rLegTrack = new AnimationTrack("RIGHT_LEG")
                .addKeyframe(new Keyframe(0.0f, 0, 0, 0, -25f, 0, 0))
                .addKeyframe(new Keyframe(0.6f, 0, 0, 0, 25f, 0, 0))
                .addKeyframe(new Keyframe(1.2f, 0, 0, 0, -25f, 0, 0));
        walk.addTrack(lLegTrack).addTrack(rLegTrack);
        clipLibrary.put("walk", walk);

        // Run animation clip
        AnimationClip run = new AnimationClip("run", 0.8f, true);
        run.addTrack(lLegTrack).addTrack(rLegTrack);
        clipLibrary.put("run", run);

        // Idle animation clip
        AnimationClip idle = new AnimationClip("idle", 2.0f, true);
        clipLibrary.put("idle", idle);
    }

    public void playClip(String clipName) {
        if (clipName != null && clipLibrary.containsKey(clipName.toLowerCase())) {
            this.activeClip = clipLibrary.get(clipName.toLowerCase());
            this.currentTimeSeconds = 0f;
            this.isPlaying = true;
        }
    }

    public void pause() { isPlaying = false; }
    public void resume() { isPlaying = true; }

    public void update(float deltaTimeSeconds) {
        if (!isPlaying || activeClip == null || skeleton == null) return;

        currentTimeSeconds += deltaTimeSeconds;
        if (currentTimeSeconds > activeClip.getDurationSeconds()) {
            if (activeClip.isLooping()) {
                currentTimeSeconds %= activeClip.getDurationSeconds();
            } else {
                currentTimeSeconds = activeClip.getDurationSeconds();
                isPlaying = false;
            }
        }

        // Apply track rotations to skeleton
        for (AnimationTrack track : activeClip.getTracks()) {
            Bone bone = skeleton.getBoneBySemanticName(track.getBoneSemanticName());
            if (bone != null && !track.getKeyframes().isEmpty()) {
                Keyframe kf = track.getKeyframes().get(0);
                float[] r = kf.getRotationDegrees();
                float phase = (float) Math.sin(currentTimeSeconds * Math.PI * 2 / activeClip.getDurationSeconds());
                bone.getLocalTransform().setRotation(r[0] * phase, r[1], r[2]);
            }
        }
    }

    public AnimationClip getActiveClip() { return activeClip; }
    public float getCurrentTimeSeconds() { return currentTimeSeconds; }
    public boolean isPlaying() { return isPlaying; }
}
