package com.zf.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.common.TestPagerForm;

public class ScanFileUtils {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private List<File> listFiles = new ArrayList<File>();
	
	private TestPagerForm pagerForm;	
	
	private String rootPath;
	
	private String suffix;
	
	private String excludeFile;

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setExcludeFile(String excludeFile) {
		this.excludeFile = excludeFile;
	}

	public TestPagerForm getPagerForm() {
		return pagerForm;
	}

	public void setPagerForm(TestPagerForm pagerForm) {
		this.pagerForm = pagerForm;
	}

	public List<File> getListFiles() {
		this.getAllFile(rootPath);
		logger.info("Get all file correct!");
		return listFiles;
	}	
	
	private void getAllFile(String path){
		File file = new File(path);
		if(file.isFile()){
			if(file.getName().endsWith(suffix) && ((excludeFile!=null && !file.getName().equals(excludeFile)) || excludeFile==null)){
				this.filterFile(file);
			}	
		}else{
			File[] files = file.listFiles();
			this.orderByDate(files);
			for (File f : files) {
				String fileName = f.getName();
				if(!fileName.equals(".") && !fileName.equals("..")){
					this.getAllFile(f.getAbsolutePath());
				}
			}
			
		}
	}
	
	private void orderByDate(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				long diff = f1.lastModified() - f2.lastModified();
				if (diff > 0)
					return -1;
				else if (diff == 0)
					return 0;
				else
					return 1;
			}
		});
	}
		 
	
	
	private void getFileCondition(File file){
		pagerForm.setTotalCount(pagerForm.getTotalCount()+1);
		if(pagerForm.getTotalCount()>(pagerForm.getPageNum()-1)*pagerForm.getNumPerPage() && listFiles.size()<pagerForm.getNumPerPage()){
			listFiles.add(file);
		}
	}
	
	private void filterFile(File file){
		if(pagerForm.getName()!=null && !pagerForm.getName().trim().equals("")){
			if(file.getName().contains(pagerForm.getName())){
				this.getFileCondition(file);
			}
		}else{
			this.getFileCondition(file);
		}
	}
	
	
	
	public static void main(String[] args) {
//		PatchUtils pu = new PatchUtils();
//		pu.getAllFile("D:\\apache-tomcat-7.0.54\\apache-tomcat-7.0.54\\conf");
//		List<String> list = pu.getListFiles();
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i));
//		}
	}
	
}
