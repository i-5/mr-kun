package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.exception.*; 

/**
 * <h3>�Y�t�t�@�C�����Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/07/02 �ߑO 03:08:52)
 * @author: 
 */
public class AttachFileInfoManager {
    protected Connection conn;
    
    protected static final String ATTACH_FILE_SEQ_STRING
	= "SELECT (TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') || "
	+ "TRIM(TO_CHAR(attach_file_seq.nextval,'00'))) filename, "
	+ "TRIM(TO_CHAR(attach_file_seq.nextval,'00')) counter FROM dual";

    /**
     * AttachFileInfoManager �R���X�g���N�^�[�E�R�����g�B
     */
    public AttachFileInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>�Y�t�t�@�C�����̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/02 �ߑO 03:12:58)
     * @return jp.ne.sonet.medipro.mr.server.entity.AttachFileInfo
     * @param mrID java.lang.String
     * @param fileName java.lang.String
     */
    public AttachFileInfo getAttachFileInfo(String mrID, String attachFileName) {
	ResultSet rs;
	Statement stmt;
	String filename = "";
	String attachpath = "";
	String inputpath = "";
	String ans = "";
	String coprefix;
	
	AttachFileInfo attachfileinfo = new AttachFileInfo();
	ConstantMasterTableManager manager = new ConstantMasterTableManager(conn);
	ConstantMasterTable constantmaster;
	
	try {
	    //�t�@�C���� �� �r�p�k�̎擾
	    stmt = conn.createStatement();
	    try {
		rs = stmt.executeQuery(ATTACH_FILE_SEQ_STRING);
		while ( rs.next() ) {
		    attachfileinfo.setSeq(rs.getString("counter"));
		    filename = rs.getString("filename");
		}
	    } finally {
		stmt.close();
	    }

	    //�Y�t�t�@�C���l�`�w�T�C�Y�̎擾
	    constantmaster = manager.getConstantMasterTable("ATTACHMAX");
	    Long long_object;
	    long long_primitive = 10L;
	    long_object = new Long(long_primitive);
	    long maxfilesize = long_object.valueOf(constantmaster.getNaiyo1().trim()).longValue();
	    attachfileinfo.setMaxFileSize(maxfilesize);
		
	    //�Y�t�t�@�C���p�X �� �i�[�t�@�C���p�X�̎擾
	    constantmaster = manager.getConstantMasterTable("ATTACHHOME");
	    attachpath = constantmaster.getNaiyo1();
	    inputpath = constantmaster.getNaiyo2();

	    //�g���q�̎擾
	    int point = attachFileName.indexOf(".");
	    if (point >= 0) {
		ans = attachFileName.substring(point);
	    }

	    //��Ѓv���t�B�b�N�X�̎擾
	    CompanyTableManager companytablemanager = new CompanyTableManager(conn);
	    CompanyTable companytable = companytablemanager.getCompanyTable(mrID);
	    coprefix = companytable.getCdPrefix();

	    attachpath = attachpath + "/" + coprefix + "/" +  filename.substring(15) + "/";
	    inputpath = inputpath + "/" + coprefix + "/" +  filename.substring(15) + "/";
	    filename = filename + ans;
		
	    attachfileinfo.setAttachFullPath(attachpath + filename);
	    attachfileinfo.setInputFile(filename);
	    attachfileinfo.setInputFullPath(inputpath);
	} catch (NumberFormatException e) {
	    throw new MrException(e);
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return attachfileinfo;
    }
}
