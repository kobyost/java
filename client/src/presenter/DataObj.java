package presenter;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataObj  implements Serializable{
	Object data;
	int serialNumber;

	public DataObj(Object data, int serialNumber) {
		this.data = data;
		this.serialNumber = serialNumber;

	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
}
