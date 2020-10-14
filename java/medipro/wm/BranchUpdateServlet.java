package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.common.util.*;

/**
 * <strong>支店・営業所追加・変更対応Servletクラス</strong>
 * <br>支店・営業所の追加・変更を行う.
 * @author
 * @version
 */
public class BranchUpdateServlet extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**処理しない*/
    private static final int RTN_NONE = -1;
    /**支店未選択*/
    private static final int RTN_NO_SELECT = -2;
    /**支店追加*/
    private static final int RTN_ADD_BRANCH = 1;
    /**支店更新*/
    private static final int RTN_UPDATE_BRANCH = 2;
    /**営業所追加*/
    private static final int RTN_ADD_OFFICE = 3;
    /**営業所更新*/
    private static final int RTN_UPDATE_OFFICE = 4;
    /**支店・営業所更新*/
    private static final int RTN_UPDATE_BOTH = 5;
    /**支店・営業所追加*/
    private static final int RTN_ADD_BOTH = 6;
    /**支店更新・営業所追加*/
    private static final int RTN_ADD_OFFICE_UPDATE_BRANCH = 7;

    /**支店・営業所一覧htmlファイル相対パス*/
    private static final String BRANCH_LIST_PATH = "wm/WmBranchList/index.html";
    /**支店・営業所追加・変更htmlファイル相対パス*/
    private static final String BRANCH_UPDATE_PATH = "wm/WmBranchUpdate/index.html";

    ////////////////////////////////////////////////////
    //class variable
    //
//      private BranchUpdateSession buses;
//      private String mode;
//      private String select;
//      private String select2;
//      private String textfield;
//      private String textfield2;
//      private String path;

    ////////////////////////////////////////////////////
    //class method
    //

    /**
     * doGet.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)	{
    	doPost(request, response);
    }

    /**
     * doPost.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String mode;
		String select;
		String select2;
		String textfield;
		String textfield2;
		String path = null;

		try {
			if (new SessionManager().check(request) != true) {
				new DispatManager().distSession(request, response);
			} else {
				// セッションの取得
				HttpSession session = request.getSession(true);
				String brUpdKey = SysCnst.KEY_BRANCHUPDATE_SESSION;
				BranchUpdateSession buses
					= (BranchUpdateSession) session.getValue(brUpdKey);
				if (buses == null) {
					buses = new BranchUpdateSession();
					session.putValue(brUpdKey, buses);
				}

				// パラメータ取得
				mode = request.getParameter("mode");
				select = request.getParameter("select");
				select2 = request.getParameter("select2");
				textfield = Converter.getParameter(request, "textfield");
				textfield2 = Converter.getParameter(request, "textfield2");
				if (SysCnst.DEBUG) {
					log("mode:"+mode);
					log("select:"+select);
					log("select2:"+select2);
					log("textfield:"+textfield);
					log("textfield2:"+textfield2);
				}
				if (mode.equals("change")) {
					// 支店リストボックス選択変更時
					buses.setBranchCD(select);
					buses.setBranchName(null);
					if (select.equals("shinki-touroku")) {
						buses.setNewBranch(true);
					}
					else {
						buses.setNewBranch(false);
					}
					buses.setOfficeCD(select2);
					buses.setOfficeName(null);
					if (select2.equals("shinki-touroku")) {
						buses.setNewOffice(true);
					} else {
						buses.setNewOffice(false);
					}
					
					if (buses.getMessageState() == SysCnst.BRANCH_UPDATE_MSG_NOSELECT) {
						// ｢支店を選択して...｣メッセージ表示されていた場合
						// メッセージIDセット
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NONE);
					}
					path = BRANCH_UPDATE_PATH;
				}
				else if (mode.equals("list")) {
					// ｢元に戻る｣押下時
					buses.setBranchCD(null);
					buses.setBranchName(null);
					buses.setOfficeCD(null);
					buses.setOfficeName(null);
					path = BRANCH_LIST_PATH;
				}
				else if (mode.equals("updatemessage")) {
					// ｢保存｣押下時
					buses.setBranchCD(select);
					buses.setBranchName(textfield);
					buses.setOfficeCD(select2);
					buses.setOfficeName(textfield2);
					buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_CONFIRM);
					if (select.equals("shinki-touroku")) {
						buses.setNewBranch(true);
					} else {
						buses.setNewBranch(false);
					}
					if (select2.equals("shinki-touroku")) {
						buses.setNewOffice(true);
					} else {
						buses.setNewOffice(false);
					}
					path = BRANCH_UPDATE_PATH;
				}
				else if (mode.equals("update")) {
					// ｢保存しますか｣で｢はい｣押下時
					buses.setBranchCD(select);
					buses.setBranchName(textfield);
					buses.setOfficeCD(select2);
					buses.setOfficeName(textfield2);
					if (select.equals("shinki-touroku")) {
						buses.setNewBranch(true);
					} else {
						buses.setNewBranch(false);
					}
					if (select2.equals("shinki-touroku")) {
						buses.setNewOffice(true);
					} else {
						buses.setNewOffice(false);
					}
					BranchListController bctrl = 
						new BranchListController(request, response);
					int rtn = checkParameter(request, textfield, textfield2, buses);
					if (SysCnst.DEBUG) {
						log("####rtn:"+rtn);
					}
					switch (rtn) {
					case RTN_NONE:	// 何もしない
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NONE);
						break;
					case RTN_NO_SELECT:	// エラーメッセージ
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NOSELECT);
						break;
					case RTN_ADD_BRANCH:	// 支店追加
						bctrl.addBranch(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_UPDATE_BRANCH:	// 支店更新
						bctrl.updateBranch(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_ADD_OFFICE:	// 営業所追加
						bctrl.addOffice(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_UPDATE_OFFICE:	// 営業所更新
						bctrl.updateOffice(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_UPDATE_BOTH:	// 支店・営業所更新
						bctrl.updateBranch(session);
						BranchListController bctrl2 = 
							new BranchListController(request, response);
						bctrl2.updateOffice(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_ADD_BOTH:	// 支店・営業所追加
						bctrl.addBranch(session);
						if (buses.getBranchCD() != null) {
							BranchListController bctrl3 = 
								new BranchListController(request, response);
							bctrl3.addOffice(session);
						}
						//					buses.setBranchCD(select);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					case RTN_ADD_OFFICE_UPDATE_BRANCH:	// 支店更新・営業所追加
						bctrl.updateBranch(session);
						BranchListController bctrl4 = 
							new BranchListController(request, response);
						bctrl4.addOffice(session);
						buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_DONE);
						break;
					}
					path = BRANCH_UPDATE_PATH;
				}
				else if (mode.equals("done")) {
					// ｢保存しました｣で｢OK｣押下時
					buses.setBranchCD(select);
					buses.setBranchName(textfield);
					buses.setOfficeCD(select2);
					buses.setOfficeName(textfield2);
					buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NONE);
					path = BRANCH_UPDATE_PATH;
				} else if (mode.equals("noupdate")) {
					// ｢保存しますか｣で｢いいえ｣押下時
					buses.setBranchCD(select);
					buses.setBranchName(textfield);
					buses.setOfficeCD(select2);
					buses.setOfficeName(textfield2);
					buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NONE);
					path = BRANCH_UPDATE_PATH;
				} else {
					buses.setMessageState(SysCnst.BRANCH_UPDATE_MSG_NONE);
					path = BRANCH_UPDATE_PATH;
				}
			}
			// Go to the next page
			response.sendRedirect(SysCnst.HTML_ENTRY_POINT + path);
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }
	
    /**
     * checkParameter.
     * @return int
     * @param request HttpServletRequest
     */
    public int checkParameter(HttpServletRequest request,
							  String textfield,
							  String textfield2,
							  BranchUpdateSession buses) {

		if (buses.isNewBranch() == true) {
			// 支店新規追加時
			if (!textfield.equals("")) {
				// 支店テキスト入力あり
				if (!textfield2.equals("")) {
					// 営業所テキスト入力あり
					// 支店・営業所追加
					return RTN_ADD_BOTH;
				} else {
					// 営業所テキスト未入力
					// 支店追加
					return RTN_ADD_BRANCH;
				}
			} else {
				// 支店テキスト未入力の場合
				if (!textfield2.equals("")) {
					// 営業所テキスト入力あり
					// エラー
					return RTN_NO_SELECT;
				} else {
					// 営業所テキスト未入力
					// 何もしない
					return RTN_NONE;
				}
			}
		} else {
			if (buses.isNewOffice() == true) {
				// 支店新規追加以外、営業所新規追加時
				if (!textfield.equals("")) {
					// 支店テキスト入力あり
					if (!textfield2.equals("")) {
						// 営業所テキスト入力ありの場合
						if (buses.getOriginalBranch().equals(textfield)) {
							// 支店変更されていない場合
							// 営業所追加
							return RTN_ADD_OFFICE;
						}
						else {
							// 支店変更されていた場合
							// 支店更新・営業所追加
							return RTN_ADD_OFFICE_UPDATE_BRANCH;
						}
					} else {
						// 営業所テキスト未入力の場合
						if (buses.getOriginalBranch().equals(textfield)) {
							// 支店変更されていない場合
							// 何もしない
							return RTN_NONE;
						} else {
							// 支店変更されていた場合
							// 支店更新
							return RTN_UPDATE_BRANCH;
						}
					}
				} else {
					// 支店テキスト未入力
					// エラーメッセージ
					return RTN_NO_SELECT;
				}
			} else {
				// 支店新規追加以外、営業所新規追加以外
				if (textfield != null) {
					// 支店テキストnull以外(ウェブ、支店サブ時)
					if (!textfield.equals("")) {
						// 支店テキスト入力あり
						if (!textfield2.equals("")) {
							// 営業所テキスト入力あり
							if (buses.getOriginalBranch().equals(textfield)) {
								// 支店変更されていない場合
								if (buses.getOriginalOffice().equals(textfield2)) {
									// 営業所変更されていない場合
									// 何もしない
									return RTN_NONE;
								} else {
									// 営業所変更されていた場合
									// 営業所更新
									return RTN_UPDATE_OFFICE;
								}
							} else {
								// 支店変更されていた場合
								if (buses.getOriginalOffice().equals(textfield2)) {
									// 営業所変更されていない場合
									// 支店更新
									return RTN_UPDATE_BRANCH;
								} else {
									// 営業所変更されていた場合
									// 支店・営業所更新
									return RTN_UPDATE_BOTH;
								}
							}
						} else {
							// 営業所テキスト入力なし
							if (buses.getOriginalBranch().equals(textfield)) {
								// 支店変更されていない場合
								// 何もしない
								return RTN_NONE;
							} else {
								// 支店変更されていた場合
								// 支店更新
								return RTN_UPDATE_BRANCH;
							}
						}
					} else {
						// 支店テキスト入力なし
						if (!textfield2.equals("")) {
							// 営業所テキスト入力あり
							if (buses.getOriginalOffice().equals(textfield2)) {
								// 営業所変更されていない場合
								// 何もしない
								return RTN_NONE;
							} else {
								// 営業所変更されていた場合
								// 営業所更新
								return RTN_UPDATE_OFFICE;
							}
						} else {
							// 営業所テキスト入力なし
							// 何もしない
							return RTN_NONE;
						}
					}
				} else {
					// 支店テキストnull(営業所サブ時)
					if (!textfield2.equals("")) {
						// 営業所テキスト入力あり
						if (buses.getOriginalOffice().equals(textfield2)) {
							// 営業所変更されていない場合
							// 何もしない
							return RTN_NONE;
						} else {
							// 営業所変更されていた場合
							// 営業所更新
							return RTN_UPDATE_OFFICE;
						}
					} else {
						// 営業所テキスト入力なし
						// 何もしない
						return RTN_NONE;
					}
				}
			}
		}
    }
}
