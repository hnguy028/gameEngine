package textures;

import java.nio.ByteBuffer;

public class TextureData {
	private int width;
	private int height;
	private ByteBuffer buffer;
	 
	public TextureData(ByteBuffer _buffer, int _width, int _height) {
		this.buffer = _buffer;
		this.width = _width;
		this.height = _height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}
	
}
