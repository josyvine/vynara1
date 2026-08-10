package com.example.character;

import java.util.ArrayList;
import java.util.List;

public class AnimationClip {
    private String name;
    private float durationSeconds;
    private boolean isLooping;
    private final List<AnimationTrack> tracks = new ArrayList<>();

    public AnimationClip(String name, float durationSeconds, boolean isLooping) {
        this.name = name;
        this.durationSeconds = durationSeconds;
        this.isLooping = isLooping;
    }

    public String getName() { return name; }
    public float getDurationSeconds() { return durationSeconds; }
    public boolean isLooping() { return isLooping; }
    public List<AnimationTrack> getTracks() { return tracks; }

    public AnimationClip addTrack(AnimationTrack track) {
        tracks.add(track);
        return this;
    }
}
