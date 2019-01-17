package api;

import java.io.File;

public class treeFun {

	private treeNode root=new treeNode("/", null, null,null,0);//树根（相当于链表的头指针）
    public treeNode getRoot()//获取树根
    {
        return root;
    }
    
    
    //输入地址，获得地址下的所有文件，构造出文件树
	public void getFile(String path,treeNode myNode,int deep)
    {
        File file=new File(path);
        File[] array=file.listFiles();
        treeNode newNode=null;
        for(int i=0;i<array.length;i++)
        {
            newNode=new treeNode(array[i].getName(),null, null,myNode,deep);
            //判断当前节点有没有firstChild，没有的话为其添加一个
            if (myNode.firstChild==null) 
            {
                if (array[i].isFile())
                    myNode.firstChild=newNode;
                if (array[i].isDirectory()) 
                {
                    myNode.firstChild=newNode;
                    getFile(array[i].getPath(), newNode,deep+1);
                }
            }
            //当前节点已经存在firstChild，所以后面的都是firstChild节点的兄弟
            else
            {
                treeNode p=myNode.firstChild;
                while(p.nextSibling!=null)
                    p=p.nextSibling;
                if (array[i].isFile())
                    p.nextSibling=newNode;
                if (array[i].isDirectory()) 
                {
                    p.nextSibling=newNode;
                    getFile(array[i].getPath(), newNode,deep+1);
                }
            }   
        }
    }
	public treeNode changeDir(String keyword,treeNode myNode) {
		if(myNode.firstChild.getData().equals(keyword)) {
			return myNode.firstChild;
		}
		else {
			treeNode p=myNode.firstChild;
            while(true) {
            	if(!p.nextSibling.getData().equals(keyword)&&p.nextSibling!=null)
            		p=p.nextSibling;
            	else if(p.nextSibling.getData().equals(keyword)){
            		return p.nextSibling;
            	}
            	else return null;
            }
		}		
	}
	public void listDir(treeNode myNode) {
		if(myNode.firstChild!=null) {
			treeNode p=myNode.firstChild;
			if(p.firstChild!=null) {
				System.out.println(p.getData());
			}
            while(p.nextSibling!=null)
            	if(p.firstChild!=null) {
            		System.out.println(p.nextSibling.getData());
            	}
                p=p.nextSibling;
		}
		else {
			System.out.println("Empty directory");
		}
	}
	@SuppressWarnings("null")
	public void printWorkDir(String path,treeNode myNode) {
		String []mypath = null;
		int i=0;
		while(myNode.parentNode!=null) {
			mypath[i]=myNode.parentNode.getData();
			myNode=myNode.parentNode;
		}
		for(;i<0;i--) {
			path=path+"/"+mypath[i];
		}
		System.out.println(path);
	}
	public void resumeDirName(treeNode myNode,String keyword,String newName) {
		if(myNode.firstChild.getData().equals(keyword)) {
			myNode.firstChild.setData(newName);
			System.out.println("rmdir success");
		}
		else {
			treeNode p=myNode.firstChild;
            while(true) {
            	if(!p.nextSibling.getData().equals(keyword)&&p.nextSibling!=null)
            		p=p.nextSibling;
            	else if(p.nextSibling.getData().equals(keyword)){
            		p.nextSibling.setData(newName);
        			System.out.println("rmdir success");
            	}
            	else {
            		System.out.println("can not find"+keyword);
            	}
            }
		}
	}
	public static void main(String[] args) {
		String rootPath = "D:\\gitData";
		treeFun tf = new treeFun();
		tf.getFile(rootPath, tf.getRoot(), 1);
		tf.printWorkDir(rootPath, tf.getRoot());
	}
	
	
}
