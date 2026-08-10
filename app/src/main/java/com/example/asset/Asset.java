package com.example.asset;

public class Asset {
    private String id;
    private String name;
    private String category; // MESH, MATERIAL, SKELETON, ANIMATION, TEXTURE
    private String format; // GLTF, OBJ, PBR
    private String fileSizeStr;

    public Asset(String id, String name, String category, String format, String fileSizeStr) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.format = format;
        this.fileSizeStr = fileSizeStr;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getFormat() { return format; }
    public String getFileSizeStr() { return fileSizeStr; }
}
