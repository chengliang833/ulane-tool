package wang.ulane.limitalgorithm.slide;

public class SlideObj {
	
	private int size;
	private long timestamp;
	public SlideObj(int size, long timestamp) {
		super();
		this.size = size;
		this.timestamp = timestamp;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "SlideObj [size=" + size + ", timestamp=" + timestamp + "]";
	}
}
