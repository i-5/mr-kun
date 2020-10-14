package jp.ne.sonet.medipro.mr.server.controller;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class EnqueteController {
    private EnqueteInfo enqinfo;
    private EnqueteManager enqmanager;
    private EnqueteQuestionManager questionmanager;
    private DBConnect dbConnect;
    private Connection connection;

    public EnqueteController(EnqueteInfo info) {

	this.dbConnect = new DBConnect();
	Connection conn = dbConnect.getDBConnect();
	this.initEnqueteController( info, conn );
    }

    public EnqueteController(EnqueteInfo info, Connection conn) {
	this.initEnqueteController( info, conn );
    }

    private void initEnqueteController(EnqueteInfo info, Connection conn){
	this.enqinfo = info;
	this.connection = conn;

	this.enqmanager = new EnqueteManager( connection );
	this.questionmanager = new EnqueteQuestionManager( connection );
    }

    /**
     * DB��connection��Close����B�i����Class�ŃI�[�v�������Ƃ��̂ݎg�p���邱�ƁB�j
     **/
    public void closeDB(){
	dbConnect.closeDB(connection);
    }

    /**
     * �g�p���Ă���DB��connection�𓾂�B
     **/
    public Connection getConnection(){
	return connection;
    }

    /**
     * �A���P�[�g��URL����ё��M�ς݃X�e�[�^�X�𓾂�
     **/
    public void getEnqueteId(HttpServletRequest req,
				HttpServletResponse res){
	try {
	    enqmanager.getEnqueteIdTable( enqinfo );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	}
    }

    /**
     * �ŐV�A���P�[�g��ID,URL����ё��M�ς݃X�e�[�^�X�𓾂�
     **/
    public void getMaxEnqueteId(HttpServletRequest req,
				HttpServletResponse res){
	try {
	    enqmanager.getEnqueteIdMaxTable( enqinfo );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	}
    }

    /**
     * Enquete Message ���M�L�^
     **/
    public void setEnqueteSendLog(HttpServletRequest req,
					HttpServletResponse res){
	try {
	    enqmanager.setEnqueteSendLogTable( enqinfo );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	}
    }

    /**
     * Enquete ���񓚂�����Ԃɂ���
     **/
    public void updateEnqueteSendLog(HttpServletRequest req,
					HttpServletResponse res){
	try {
	    enqmanager.updateEnqueteSendLogTable( enqinfo );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	}
    }

    /**
     * Enquete ���񓚂��Ă��邩�m�F����
     **/
    public boolean isEnqueteReplyStatus(HttpServletRequest req,
					HttpServletResponse res){
	try {
	    enqmanager.getEnqueteReplyStatus( enqinfo );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	}
	return enqinfo.getReplyStatus();
    }

    /**
     * Enquete �̉񓚂��i�[����
     **/
    public void setEnqueteAnswer(HttpServletRequest req,
				HttpServletResponse res){
	try {
	    enqmanager.setEnqueteAnswer( enqinfo );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	}
    }


    /**
     * Enquete �̖��𓾂�
     **/
    public Vector getEnqueteQuestion(EnqueteQuestionInfo qinfo,
					HttpServletRequest req,
					HttpServletResponse res){
	try {
	    questionmanager.getQuestion(qinfo);
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	}
	return qinfo.getQuestion();
    }

    /**
     * �f�ÉȃO���[�v�𓾂�
     **/
    public String getShinryokaGroup(EnqueteQuestionInfo qinfo,
					HttpServletRequest req,
					HttpServletResponse res){
	ShinryokaGroupManager manager = new ShinryokaGroupManager(connection);

	try {
	    qinfo.setGroupId( manager.getShinryokaGroup( qinfo.getShinryokaCd() ) );
	    qinfo.setGroupName( manager.getGroupName() );
	} catch (MrException e) {
	    DispatManager dispat= new DispatManager();
	    dispat.distribute(req, res);
	}
	return qinfo.getGroupId();
    }
}
