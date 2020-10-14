package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
//import com.jspsmart.upload.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/01 �ߌ� 03:47:50)
 * @author: 
 */
public class UploadFileManager {

    protected com.jspsmart.upload.Files files;

    protected Hashtable uploadFiles;

    public UploadFileManager(com.jspsmart.upload.Files files){
	//
	this.files = files;
	uploadFiles = new Hashtable();
	Enumeration enum = files.getEnumeration();
	while (enum.hasMoreElements()) {
	    com.jspsmart.upload.File file = (com.jspsmart.upload.File) enum.nextElement();

	    if (file.getFileName().equals("") == false ) {
		UploadFile uploadfile = new  UploadFile(file);		
		uploadFiles.put(file.getFieldName(), uploadfile);
	    }
	}
    }

    /**
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/01 �ߌ� 04:04:23)
     * @return jp.ne.sonet.medipro.mr.server.entity.UploadFile
     * @param fieldName java.lang.String
     */
    public UploadFile findUploadFile(String fieldName) {
	
	Object obj = uploadFiles.get(fieldName);
	if (obj == null) {
	    return null;
	}
	return (UploadFile) obj;
	
    }

    /**
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/01 �ߌ� 04:06:36)
     * @return int
     */
    public int getCount() {
	//
	return files.getCount();
    }

    /**
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/01 �ߌ� 04:08:38)
     * @return java.util.Enumeration
     */
    public Enumeration getUploadFiles() {
	//
	return files.getEnumeration();
    }
}
