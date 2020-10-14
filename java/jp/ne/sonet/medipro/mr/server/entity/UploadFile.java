package jp.ne.sonet.medipro.mr.server.entity;

import com.jspsmart.upload.*;
import jp.ne.sonet.medipro.mr.common.exception.*; 
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/07/01 午後 03:30:41)
 * @author: 
 */
public class UploadFile {
//
protected com.jspsmart.upload.File file;
//
protected String serverPath;
protected String serverFileName;
public UploadFile(com.jspsmart.upload.File file) {
	this.file = file;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 03:35:20)
 * @return java.lang.String
 */
public java.lang.String getFieldName() {
	return file.getFieldName();
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 03:35:20)
 * @return java.lang.String
 */
public java.lang.String getFileName() {
	return file.getFileName();
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 03:35:20)
 * @return java.lang.String
 */
public java.lang.String getFilePathName() {
	return file.getFilePathName();
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 03:35:20)
 * @return java.lang.String
 */
public java.lang.String getServerFileName() {
	return serverFileName;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 03:35:20)
 * @return java.lang.String
 */
public java.lang.String getServerPath() {
	return serverPath;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 03:35:20)
 * @return int
 */
public int getSize() {
	return file.getSize();
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:22:29)
 */
public void save() {
	saveAs(getServerPath(), getServerFileName());	
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 04:23:02)
 * @param path java.lang.String
 * @param name java.lang.String
 */
public void saveAs(String path, String name) {
	try {
		file.saveAs(path + "/" + name);
	}
	catch (com.jspsmart.upload.SmartUploadException e) {
		throw new MrException(e);
	}
	catch (java.io.IOException e) {
		throw new MrException(e);
	}
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 03:35:20)
 * @param newServerFileName java.lang.String
 */
public void setServerFileName(java.lang.String newServerFileName) {
	serverFileName = newServerFileName;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 03:35:20)
 * @param newServerPath java.lang.String
 */
public void setServerPath(java.lang.String newServerPath) {
	serverPath = newServerPath;
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/07/01 午後 03:42:22)
 * @return java.lang.String
 */
public String toString() {
	StringBuffer me = new StringBuffer(1024);
	me.append("UploadFile\n");
	me.append("  field        = " + getFieldName() + "\n");
	me.append("  pathname     = " + getFilePathName() + "\n");
	me.append("  filename     = " + getFileName() + "\n");
	me.append("  size         = " + getSize() + "\n");
	me.append("  path(server) = " + getServerPath() + "\n");
	me.append("  file(server) = " + getServerFileName() + "\n");
	return me.toString();
}
}
