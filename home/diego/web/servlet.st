servlet(package,table) ::= <<
package $package$.servlet;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import et.log.Log;
import $package$.logic.$table.manager$;
import $package$.model.$table.bean$;
import et.web.BaseServlet;
$table.parentTables: {parentTable|
import $package$.logic.$parentTable.manager$;}$
import et.web.IAction;

public class $table.servlet$ extends BaseServlet {
    private static final String FORMURL = "/jsp/$table.javaName$/$table.javaName$Form.jsp";
    private static final String BROWSERURL = "/jsp/$table.javaName$/$table.javaName$Browser.jsp";
    private static final String BEAN_NAME = "$table.bean$";
    private static final String BEAN_LIST_NAME = "$table.bean$List";
    static final long serialVersionUID = 1L;
    private static Log mLog = Log.getInstance($table.servlet$.class);

    //Command pattern implementato con Enum.
    private enum Action implements IAction {
    	start(BROWSERURL,BROWSERURL) {
			public String doAction(HttpServletRequest pRequest, HttpServletResponse pResponse) {
				return okURL;
			}
		},
    	load(BROWSERURL,BROWSERURL) {
			public String doAction(HttpServletRequest pRequest, HttpServletResponse pResponse) {
				$table.bean$ lInput$table.bean$ = ($table.bean$) pRequest.getAttribute(BEAN_NAME);
		        List<$table.bean$> l$table.bean$List;
		        //Carico i dati
				if (lInput$table.bean$ != null) {
					l$table.bean$List = $table.manager$.getInstance().loadAllUsingTemplate(lInput$table.bean$);
				} else {
					l$table.bean$List = $table.manager$.getInstance().loadAll();
				}
		        pRequest.setAttribute(BEAN_LIST_NAME, l$table.bean$List);
				return okURL;
			}
		},
    	loadPage(BROWSERURL,BROWSERURL) {
			public String doAction(HttpServletRequest pRequest, HttpServletResponse pResponse) {
		        $table.bean$ lInput$table.bean$ = ($table.bean$) pRequest.getAttribute(BEAN_NAME);
		        //Sistemo il numero dei record
			    int lIntNumberOfRecords = $table.manager$.getInstance().countRecordsUsingTemplate(lInput$table.bean$);
			    lInput$table.bean$.setRecordCount(lIntNumberOfRecords);
		        //Carico i dati
				mLog.debug("Requesting page ",lInput$table.bean$.getPageNumber()," of size ",lInput$table.bean$.getPageSize());
		        List<$table.bean$> l$table.bean$List = $table.manager$.getInstance().loadPageUsingTemplate(lInput$table.bean$, lInput$table.bean$);
		        pRequest.setAttribute(BEAN_LIST_NAME, l$table.bean$List);
				return okURL;
			}
		},
    	startInsert(FORMURL,FORMURL) {
			public String doAction(HttpServletRequest pRequest, HttpServletResponse pResponse){
				return okURL;
			}
		},
    	insert(FORMURL,FORMURL) {
			public String doAction(HttpServletRequest pRequest, HttpServletResponse pResponse){
		        $table.bean$ lInput$table.bean$ = ($table.bean$) pRequest.getAttribute(BEAN_NAME);
		        $table.manager$.getInstance().insert(lInput$table.bean$);
				return okURL;
			}
		},
    	startUpdate(FORMURL,FORMURL) {
			public String doAction(HttpServletRequest pRequest, HttpServletResponse pResponse){
		        $table.bean$ lInput$table.bean$ = ($table.bean$) pRequest.getAttribute(BEAN_NAME);
		        $table.bean$ lOutput$table.bean$ = $table.manager$.getInstance().loadByPrimaryKey(lInput$table.bean$);
		        pRequest.setAttribute(BEAN_NAME, lOutput$table.bean$);
		        return okURL;
			}
		},
    	update(FORMURL,FORMURL) {
			public String doAction(HttpServletRequest pRequest, HttpServletResponse pResponse){
		        $table.bean$ lInput$table.bean$ = ($table.bean$) pRequest.getAttribute(BEAN_NAME);
		        $table.manager$.getInstance().updateByPrimaryKey(lInput$table.bean$);
				return okURL;
			}
		},
    	delete(FORMURL,FORMURL) {
			public String doAction(HttpServletRequest pRequest, HttpServletResponse pResponse){
		        $table.bean$ lInput$table.bean$ = ($table.bean$) pRequest.getAttribute(BEAN_NAME);
		        $table.manager$.getInstance().deleteByPrimaryKey(lInput$table.bean$);
				return okURL;
			}
		},
    	duplicate(FORMURL,FORMURL) {
			public String doAction(HttpServletRequest pRequest, HttpServletResponse pResponse){
		        $table.bean$ lInput$table.bean$ = ($table.bean$) pRequest.getAttribute(BEAN_NAME);
		        $table.bean$ lOutput$table.bean$ = $table.manager$.getInstance().loadByPrimaryKey(lInput$table.bean$);
		        pRequest.setAttribute(BEAN_NAME, lOutput$table.bean$);
				return okURL;
			}
		};
		
		//Proprietà di ogni action
    	public String okURL, koURL;
    	
    	//Costruttore
    	Action(String okURL, String koURL){
    		this.okURL = okURL;
    		this.koURL = koURL;
    	}

    	public String getOkURL(){ return okURL; }
    	public String getKoURL(){ return koURL; }
    	
		//Hashmap che data una stringa mi da una Action
		private static final Map<String, Action> stringToEnum = new HashMap<String, Action>();
		static { // Initialize map from constant name to enum constant
			for (Action lAction : values())
			stringToEnum.put(lAction.toString(), lAction);
		}
		//Ritorna una action data una stringa e null per non trovato
		public static Action fromString(String pStrAction) {
			return stringToEnum.get(pStrAction);
		}
		
    }

    protected final void forward(HttpServletRequest pRequest, HttpServletResponse pResponse, String pURL) throws ServletException, IOException {
    	mLog.debug("forwarding to ", pURL);
$table.parentTables: {parentTable|
    	pRequest.setAttribute("$parentTable.javaName$",$parentTable.manager$.getInstance().loadAll());}$
        pRequest.getRequestDispatcher(pURL).forward(pRequest, pResponse);
    }

	@Override
	protected IAction getAction(String pStrAction) {
		return Action.fromString(pStrAction);
	}
}
>>