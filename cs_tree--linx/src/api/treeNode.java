package api;

public class treeNode {
	String data;
	treeNode firstChild;
    treeNode nextSibling;
    treeNode parentNode;
    int deep;
    
    public treeNode(String data, treeNode  firstChild,
            treeNode nextSibling, treeNode parentNode,int deep) {
        super();
        this.data = data;
        this.firstChild = firstChild;
        this.nextSibling = nextSibling;
        this.parentNode = parentNode;
        this.deep= deep;
    }
    
    public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
