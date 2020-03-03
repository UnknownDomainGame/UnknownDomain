package engine.graphics.gl;

import engine.graphics.backend.ResourceFactory;
import engine.graphics.gl.mesh.GLMesh;
import engine.graphics.gl.mesh.GLSingleBufferMesh;
import engine.graphics.gl.texture.GLSampler;
import engine.graphics.gl.texture.GLTexture2D;
import engine.graphics.mesh.Mesh;
import engine.graphics.mesh.SingleBufferMesh;
import engine.graphics.texture.Sampler;
import engine.graphics.texture.Texture2D;

public final class GLResourceFactory implements ResourceFactory {
    private final Thread renderingThread;

    public GLResourceFactory(Thread renderingThread) {
        this.renderingThread = renderingThread;
    }

    @Override
    public Texture2D.Builder createTexture2DBuilder() {
        return GLTexture2D.builder();
    }

    @Override
    public Sampler createSampler() {
        return new GLSampler();
    }

    @Override
    public Mesh.Builder createMeshBuilder() {
        return GLMesh.builder();
    }

    @Override
    public SingleBufferMesh.Builder createSingleBufferMeshBuilder() {
        return GLSingleBufferMesh.builder();
    }
}
