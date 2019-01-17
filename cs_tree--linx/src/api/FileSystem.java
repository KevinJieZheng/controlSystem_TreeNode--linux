package api;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * �ļ�ϵͳ
 * 
 * @author 14405
 *
 */
public class FileSystem {

	/**
	 * �ļ��ṹ
	 * 
	 * @author 14405
	 *
	 */
	public static class FCB {

		public boolean isDir; // �Ƿ�Ŀ¼

		public long id; // Ψһ��־

		public String name; // �ļ�����Ŀ¼��

		public byte data[] = new byte[2048]; // �ļ����ݿ�

		public Dir parent; // ��Ŀ¼
	}

	/**
	 * �ļ�
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
	 * Ŀ¼
	 * 
	 * @author 14405
	 *
	 */
	public static class Dir extends FCB {

		public List<Dir> childs; // ��Ŀ¼

		public Dir() {
			isDir = true;
			childs = new ArrayList<>();
		}
	}

	protected long count; // id������
	protected Dir cwd/* ����Ŀ¼ */, root /* ��Ŀ¼ */;

	public FileSystem() {

	}

	/**
	 * ��ʽ��
	 */
	public void reformat() {
		count = 0;
		root = new Dir();
		root.name = "/";
		root.id = count++;
		cwd = root;
	}

	/**
	 * ��·����ȡ�ļ���
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
						System.out.println("�Ҳ���Ŀ¼Ŀ¼");
				}

				sb.setLength(0);
			}
		}

		return parent;
	}

	/**
	 * �����ļ���
	 */
	public void mkdir(String path) {
		
		Dir parent = cwd;
		
		boolean flag = true;
		
		for (FCB child : parent.childs) {
			if (!child.isDir)
				continue;
			if (child.name.equals(path)) {
				System.out.println("�ļ��Ѵ���");
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
			System.out.println("�޷��޸�");
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
				//�ҵ��ӽڵ���ͬ�����ļ�
				parent.childs.remove(index);
				flag = false;
				break;
			}
		}
		if (flag)
			System.out.println("�޷�ɾ��");
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
				case "exit":System.out.println("���˳�");return;
				case "pwd":System.out.println(fs.pwd());break;
				case "mkdir":fs.mkdir(inputArgs);break;
				case "list":fs.list();break;
				case "ls":fs.list();break;
				case "rmdir":fs.rmdir(inputArgs);break;
				case "rename":String[] inputArgsArray = inputArgs.split("\\s");
					fs.rename(inputArgsArray[0], inputArgsArray[1]);break;			
				case "cd":fs.cd(inputArgs);break;
				default:System.out.println("ָ����Ч");
				
				}
				
				System.out.print("["+fs.pwd()+"]");
			}
		}
	
	}
}
