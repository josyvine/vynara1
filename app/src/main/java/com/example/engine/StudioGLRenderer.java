package com.example.engine;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class StudioGLRenderer implements GLSurfaceView.Renderer {
    private final SceneManager sceneManager;
    private final CameraManager cameraManager;
    private final LightManager lightManager;

    private int programHandle;
    private int uMVPMatrixHandle;
    private int uModelMatrixHandle;
    private int uColorHandle;
    private int uLightPosHandle;
    private int uLightColorHandle;

    private int aPositionHandle;
    private int aNormalHandle;

    // Grid Buffer
    private FloatBuffer gridBuffer;
    private int gridVertexCount = 0;

    private final float[] mvpMatrix = new float[16];

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;\n" +
            "uniform mat4 uModelMatrix;\n" +
            "attribute vec4 aPosition;\n" +
            "attribute vec3 aNormal;\n" +
            "varying vec3 vNormal;\n" +
            "varying vec3 vFragPos;\n" +
            "void main() {\n" +
            "    vFragPos = vec3(uModelMatrix * aPosition);\n" +
            "    vNormal = vec3(uModelMatrix * vec4(aNormal, 0.0));\n" +
            "    gl_Position = uMVPMatrix * aPosition;\n" +
            "}\n";

    private final String fragmentShaderCode =
            "precision mediump float;\n" +
            "varying vec3 vNormal;\n" +
            "varying vec3 vFragPos;\n" +
            "uniform vec4 uColor;\n" +
            "uniform vec3 uLightPos;\n" +
            "uniform vec3 uLightColor;\n" +
            "void main() {\n" +
            "    vec3 norm = normalize(vNormal);\n" +
            "    vec3 lightDir = normalize(uLightPos - vFragPos);\n" +
            "    float diff = max(dot(norm, lightDir), 0.3);\n" +
            "    vec3 diffuse = diff * uLightColor;\n" +
            "    vec3 finalColor = uColor.rgb * (diffuse + vec3(0.2));\n" +
            "    gl_FragColor = vec4(finalColor, uColor.a);\n" +
            "}\n";

    public StudioGLRenderer(SceneManager sceneManager, CameraManager cameraManager, LightManager lightManager) {
        this.sceneManager = sceneManager;
        this.cameraManager = cameraManager;
        this.lightManager = lightManager;
        initGridBuffer();
    }

    private void initGridBuffer() {
        int gridSize = 10;
        float[] gridVertices = new float[(gridSize * 2 + 1) * 4 * 3];
        int idx = 0;
        for (int i = -gridSize; i <= gridSize; i++) {
            // X lines
            gridVertices[idx++] = -gridSize; gridVertices[idx++] = 0f; gridVertices[idx++] = i;
            gridVertices[idx++] = gridSize;  gridVertices[idx++] = 0f; gridVertices[idx++] = i;
            // Z lines
            gridVertices[idx++] = i; gridVertices[idx++] = 0f; gridVertices[idx++] = -gridSize;
            gridVertices[idx++] = i; gridVertices[idx++] = 0f; gridVertices[idx++] = gridSize;
        }
        gridVertexCount = gridVertices.length / 3;

        ByteBuffer bb = ByteBuffer.allocateDirect(gridVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        gridBuffer = bb.asFloatBuffer();
        gridBuffer.put(gridVertices);
        gridBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.07f, 0.08f, 0.11f, 1.0f); // Dark studio canvas #12131C
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        programHandle = GLES20.glCreateProgram();
        GLES20.glAttachShader(programHandle, vertexShader);
        GLES20.glAttachShader(programHandle, fragmentShader);
        GLES20.glLinkProgram(programHandle);

        uMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "uMVPMatrix");
        uModelMatrixHandle = GLES20.glGetUniformLocation(programHandle, "uModelMatrix");
        uColorHandle = GLES20.glGetUniformLocation(programHandle, "uColor");
        uLightPosHandle = GLES20.glGetUniformLocation(programHandle, "uLightPos");
        uLightColorHandle = GLES20.glGetUniformLocation(programHandle, "uLightColor");

        aPositionHandle = GLES20.glGetAttribLocation(programHandle, "aPosition");
        aNormalHandle = GLES20.glGetAttribLocation(programHandle, "aNormal");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        cameraManager.getActiveCamera().updateProjectionMatrix(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(programHandle);

        Camera camera = cameraManager.getActiveCamera();
        float[] viewMatrix = camera.getViewMatrix();
        float[] projMatrix = camera.getProjectionMatrix();

        // Setup Light
        if (!lightManager.getLights().isEmpty()) {
            Light mainLight = lightManager.getLights().get(0);
            GLES20.glUniform3fv(uLightPosHandle, 1, mainLight.getPosition(), 0);
            GLES20.glUniform3fv(uLightColorHandle, 1, mainLight.getColorRGB(), 0);
        } else {
            GLES20.glUniform3f(uLightPosHandle, 5f, 10f, 5f);
            GLES20.glUniform3f(uLightColorHandle, 1f, 1f, 1f);
        }

        // Draw Grid Ground
        drawGrid(viewMatrix, projMatrix);

        // Draw Scene Objects
        Scene scene = sceneManager.getActiveScene();
        if (scene != null) {
            for (SceneObject obj : scene.getObjects()) {
                drawSceneObject(obj, viewMatrix, projMatrix);
            }
        }
    }

    private void drawGrid(float[] viewMatrix, float[] projMatrix) {
        float[] identity = new float[16];
        Matrix.setIdentityM(identity, 0);

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, identity, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projMatrix, 0, mvpMatrix, 0);

        GLES20.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(uModelMatrixHandle, 1, false, identity, 0);
        GLES20.glUniform4f(uColorHandle, 0.2f, 0.25f, 0.35f, 0.5f); // Subtle grid color

        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false, 0, gridBuffer);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, gridVertexCount);
        GLES20.glDisableVertexAttribArray(aPositionHandle);
    }

    private void drawSceneObject(SceneObject obj, float[] viewMatrix, float[] projMatrix) {
        if (!obj.isVisible() || obj.getMesh() == null) return;

        Mesh mesh = obj.getMesh();
        Transform transform = obj.getTransform();
        float[] modelMatrix = transform.getModelMatrix();

        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projMatrix, 0, mvpMatrix, 0);

        GLES20.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(uModelMatrixHandle, 1, false, modelMatrix, 0);

        Material mat = obj.getMaterial();
        if (mat != null) {
            float[] color = mat.getBaseColorRGBA();
            if (obj.isSelected()) {
                GLES20.glUniform4f(uColorHandle, 0f, 0.9f, 1f, 1f); // Cyan highlight when selected
            } else {
                GLES20.glUniform4f(uColorHandle, color[0], color[1], color[2], color[3]);
            }
        } else {
            GLES20.glUniform4f(uColorHandle, 0.8f, 0.8f, 0.8f, 1f);
        }

        // Draw Mesh
        if (mesh.getVertexBuffer() != null) {
            GLES20.glEnableVertexAttribArray(aPositionHandle);
            GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mesh.getVertexBuffer());
        }

        if (mesh.getNormalBuffer() != null) {
            GLES20.glEnableVertexAttribArray(aNormalHandle);
            GLES20.glVertexAttribPointer(aNormalHandle, 3, GLES20.GL_FLOAT, false, 0, mesh.getNormalBuffer());
        }

        if (mesh.getIndexBuffer() != null) {
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.getIndices().length, GLES20.GL_UNSIGNED_SHORT, mesh.getIndexBuffer());
        } else if (mesh.getVertexBuffer() != null) {
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mesh.getVertexCount());
        }

        GLES20.glDisableVertexAttribArray(aPositionHandle);
        GLES20.glDisableVertexAttribArray(aNormalHandle);

        // Render Children
        for (SceneObject child : obj.getChildren()) {
            drawSceneObject(child, viewMatrix, projMatrix);
        }
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
