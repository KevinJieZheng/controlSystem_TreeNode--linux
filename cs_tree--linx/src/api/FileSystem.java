package api;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 文件系统
 * 
 * @author 14405
 *
 */
public class FileSystem {

	/**
	 * 文件结构
	 * 
	 * @author 14405
	 *
	 */
	public static class FCB {

		public boolean isDir; // 是否目录

		public long id; // 唯一标志

		public String name; // 文件名或目录名

		public byte data[] = new byte[2048]; // 文件数据块

		public Dir parent; // 父目录
	}

	/**
	 * 文件
	 * 
	 * @author 14405
	 *
	 */
	public static class File extends FCB {

		public File() {
			isDir = false;
		}
	}

	/**
	 * 目录
	 * 
	 * @author 14405
	 *
	 */
	public static class Dir extends FCB {

		public List<Dir> childs; // 子目录

		public Dir() {
			isDir = true;
			childs = new ArrayList<>();
		}
	}

	protected long count; // id计数器
	protected Dir cwd/* 工作目录 */, root /* 根目录 */;

	public FileSystem() {

	}

	/**
	 * 格式化
	 */
	public void reformat() {
		count = 0;
		root = new Dir();
		root.name = "/";
		root.id = count++;
		cwd = root;
	}

	/**
	 * 从路径获取文件夹
	 * 
	 * @param path
	 * @return
	 */
	public Dir getDirfromPath(String path) {
		
		char c[] = path.toCharArray();
		
		StringBuffer sb = new StringBuffer();
		
		Dir parent = cwd;
		
		for (int i = 0; i < c.length; i++) {
			sb.append(c[i]);
			if (c[i] == '/' || i == c.length - 1) {
				String p = sb.toString();
				if (p.equals("/")) {
					parent = root;
				} else if (p.equals("..")||p.equals("../")) {
					if (parent.parent != null) {
						
						parent = parent.parent;
					}
				} else if (p.equals(".")||p.equals("./")) {
					// DO NOTHING
				} else {
					if (c[i] == '/') {
						p = p.substring(0, p.length() - 2);
					}
					boolean flag = true;
					for (FCB child : parent.childs) {
						if (!child.isDir)
							continue;
						if (child.name.equals(p)) {
							parent = (Dir) child;
							flag = false;
							break;
						}
					}
					if (flag)
						System.out.println("找不到目录目录");
				}

				sb.setLength(0);
			}
		}

		return parent;
	}

	/**
	 * 创建文件夹
	 */
	public void mkdir(String path) {
		
		Dir parent = cwd;
		
		boolean flag = true;
		
		for (FCB child : parent.childs) {
			if (!child.isDir)
				continue;
			if (child.name.equals(path)) {
				System.out.println("文件已存在");
				flag = false;
				break;
			}
		}
		if (flag) {
			int pos = path.lastIndexOf('/');
			String part = path.substring(0, pos + 1);
			Dir par = getDirfromPath(part);
			Dir newdir = new Dir();
			
			newdir.name = path.substring(pos + 1, path.length());
			newdir.id = count++;
			newdir.parent=par;
			par.childs.add(newdir);
		}
		
	}

	/**
	 * 
	 */
	public void list() {
		for (int i = 0; i < cwd.childs.size(); i++) {
			System.out.println(cwd.childs.get(i).name);
		}
	}

	/**
	 * 
	 */
	public void cd(String path) {
		cwd = getDirfromPath(path);
	}
	
	public String pwd() {
		
		StringBuffer path = new StringBuffer("/");
		Dir par = cwd;
		while(par.parent!=null) {

			path.insert(0, "/"+par.name);
			par=par.parent;
		}

		return path.toString();
		
	}
	
	
	
	public void rename(String oldName,String newName) {
		Dir parent = cwd;
		boolean flag = true;
		
		for (FCB child : parent.childs) {		
			if (!child.isDir)
				continue;
			if (child.name.equals(oldName)) {
					flag = false;
					child.name = newName;
			}		
		}
		if (flag)
			System.out.println("无法修改");
	}
	public void rmdir(String name) {
		Dir parent = cwd;
		boolean flag = true;
		int index=0;
		for (FCB child : parent.childs) {
			if (!child.isDir) {
				index++;
				continue;}
			
			if (child.name.equals(name)) {
				//找到子节点下同名的文件
				parent.childs.remove(index);
				flag = false;
				break;
			}
		}
		if (flag)
			System.out.println("无法删除");
	}
	
	
	public static void main(String args[]) {
		FileSystem fs = new FileSystem();
		fs.reformat();
		
		String inputCode = null, inputArgs = null;
		
		System.out.print("["+fs.pwd()+"]");
		try (Scanner input = new Scanner(System.in)) {
			while (input.hasNext()) {
				//System.out.print("["+fs.pwd()+"]");
				inputCode = input.next();
				inputArgs = input.nextLine().trim();
				//System.out.println(inputCode+"+"+inputArgs);
				
				switch(inputCode) {
				case "exit":System.out.println("已退出");return;
				case "pwd":System.out.println(fs.pwd());break;
				case "mkdir":fs.mkdir(inputArgs);break;
				case "list":fs.list();break;
				case "ls":fs.list();break;
				case "rmdir":fs.rmdir(inputArgs);break;
				case "rename":String[] inputArgsArray = inputArgs.split("\\s");
					fs.rename(inputArgsArray[0], inputArgsArray[1]);break;			
				case "cd":fs.cd(inputArgs);break;
				default:System.out.println("指令无效");
				
				}
				
				System.out.print("["+fs.pwd()+"]");
			}
		}
	
	}
}
