package wikipackage;

class DataStructure {
	String id = "";
	int textCount = 0, titleCount=0, infoboxCount=0, refCount= 0, catCount=0, extCount=0;
	
	public DataStructure(int titleCount, int textCount, int infoboxCount,int refCount, int catCount, int extCount){
		this.titleCount += titleCount;
		this.textCount += textCount;
		this.refCount += refCount;
		this.catCount += catCount;
		this.extCount += extCount;
		this.infoboxCount += infoboxCount;
	}
}
