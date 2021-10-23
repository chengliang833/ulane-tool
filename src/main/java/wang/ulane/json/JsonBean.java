package wang.ulane.json;

@StdNameAnnotation
@FieldBaseAnnotation
public class JsonBean {
	private String SDFD;

	private String SeFd;
	
	private String Abc;
	
	private String SEF;
	
	private String sdfd;
	
	private String ab_Id;
	
	public String getSDFD() {
		return SDFD;
	}

	public void setSDFD(String sDFD) {
		SDFD = sDFD;
	}

	public String getSEF() {
		return SEF;
	}

	public void setSEF(String sEF) {
		SEF = sEF;
	}

	public String getSdfd() {
		return sdfd;
	}

	public void setSdfd(String sdfd) {
		this.sdfd = sdfd;
	}

	public String getAb_Id() {
		return ab_Id;
	}

	public void setAb_Id(String ab_Id) {
		this.ab_Id = ab_Id;
	}

	public String getSeFd() {
		return SeFd;
	}

	public void setSeFd(String seFd) {
		SeFd = seFd;
	}

	public String getAbc() {
		return Abc;
	}

	public void setAbc(String abc) {
		Abc = abc;
	}

	@Override
	public String toString() {
		return "JsonBean [SDFD=" + SDFD + ", SeFd=" + SeFd + ", Abc=" + Abc + ", SEF=" + SEF + ", sdfd=" + sdfd
				+ ", ab_Id=" + ab_Id + "]";
	}
	
}
