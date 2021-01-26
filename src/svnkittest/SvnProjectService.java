package svnkittest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;


/**
* @author: HSG 
* @����ʱ��: 2017��4��27�� ����11:39:25 
* @version: 1.0  
*/
//@Service("svnProjectService")  
public class SvnProjectService {  
      

    // ��Ŀ�Ĵ��λ��  
    private String workspace = null;  
      
    private ResourceBundle rb = ResourceBundle.getBundle("application");  
      
    // SVN���û���������  
    private String username = null;  
    private String password = null;  
      
    private String templete = null;  
      
    @Resource(name="xcodeService")  
    //private XcodeService xcodeService;  
      
      
    private void init(){  
        String webapp = System.getProperty("webapp.root");  
        if(null!=webapp&&!webapp.endsWith("/") && !webapp.endsWith("\\")){  
            webapp = webapp + "/";  
        }  
        // ������web�������Ժ��п���WebContentû��  
        if(new File(webapp + "WebContent").exists()){  
            webapp = webapp + "WebContent";  
        }  
        username = rb.getString("svn.username");  
        password = rb.getString("svn.password");  
        workspace = rb.getString("project.svn.path");  
        templete = webapp + "templete";  
    }  
      
    public SvnProjectService(){  
        super();  
        init();  
    }  
      
    /**  
     * ������Ŀ���  
     * @param project  
     *          Project  
     * @return  
     */  
	// public boolean createProjectFrame(Project project,List<String>
	// tableNames) {
	// if(project == null){
	// return false;
	// }
	// File src = new File(templete); // ģ����Ŀ��λ��
	// File ws = new File(workspace); // work copy
	// if(!ws.exists()){
	// ws.mkdirs();
	// }
	// File dest = new File(workspace + "/" + project.getName());
	// if(!dest.exists()){
	// dest.mkdirs();
	// }
	//
	// checkWorkCopy(project); // ȷ�������ռ�
	//
	// // ����ģ����Ŀ�������ռ�
	// try {
	// // FileUtils.copyDirectory(src, dest);
	// } catch (IOException e) {
	// // logger.error(e.getMessage(), e);
	// return false;
	// }
	//
	// //�޸�.project�ļ��е�����
	// editProjectFile(project);
	//
	// // ���ɿ�ܴ���
	// //xcodeService.createBaseFrameWithDatasource(project,tableNames);
	//
	// // �ύ��SVN
	// commitProjectToSvn(project);
	//
	// return true;
	// }
	//
    /**  
     * �޸���Ŀ�ļ�  
     * @param
     * @throws
     * @throws IOException  
     */  
    @SuppressWarnings("unchecked")  
	// private void editProjectFile(Project project) {
	//
	// String pro = workspace + "/" + project.getName() + "/";
	//
	// String settings = pro + ".settings/";
	//
	// // 1. �޸�.settings/org.eclipse.wst.common.component
	// Document document = null;
	// try {
	// document = XmlReaderUtil.getDocument(new File(settings
	// + "org.eclipse.wst.common.component"));
	// } catch (DocumentException e) {
	// e.printStackTrace();
	// }
	//
	// Element root = document.getRootElement();
	//
	// root.element("wb-module").attribute("deploy-name")
	// .setValue(project.getName());
	//
	// if (root.element("wb-module").element("property").attribute("name")
	// .getValue().equals("java-output-path")) {
	//
	// root.element("wb-module").element("property").attribute("value")
	// .setValue("/" + project.getName() + "/build/classes");
	// }
	//
	// Iterator<Element> itr = (Iterator<Element>) XmlReaderUtil.getElements(
	// document, "//wb-module//property").iterator();
	//
	// while (itr.hasNext()) {
	// Element element = itr.next();
	// if ("context-root".equals(element.attribute("name").getValue())) {
	// element.attribute("value").setValue("/" + project.getName());
	// }
	// }
	//
	// // ���޸ĺ��ֵд��
	// XmlReaderUtil.writerXml(document, settings
	// + "org.eclipse.wst.common.component");
	//
	// // 2. �޸�.project
	// try {
	// document = XmlReaderUtil.getDocument(new File(pro + ".project"));
	// XmlReaderUtil.setElementText(document, "//projectDescription",
	// "name", project.getName());
	// XmlReaderUtil.writerXml(document, pro + ".project");
	// } catch (DocumentException e) {
	// e.printStackTrace();
	// }
	// }
  
    /**  
     * ��SVN������Ŀ��work copy  
     * @param project  
     *          Project  
     * @return  
     */  
	// public boolean updateProjectFromSvn(Project project) {
	// if(null == project || null == rb.getString("svn.url")){
	// return false;
	// }
	// project.setSvnUrl(rb.getString("svn.url"));
	//
	// SVNClientManager clientManager = SVNUtil.authSvn(project.getSvnUrl(),
	// username, password);
	// if (null == clientManager) {
	// logger.error("SVN login error! >>> url:" + project.getSvnUrl()
	// + " username:" + username + " password:" + password);
	// return false;
	// }
	// // ע��һ�������¼�������
	// clientManager.getCommitClient().setEventHandler(new
	// UpdateEventHandler());
	//
	// SVNURL repositoryURL = null;
	// try {
	// // eg: http://svn.ambow.com/wlpt/bsp
	// repositoryURL =
	// SVNURL.parseURIEncoded(project.getSvnUrl()).appendPath("trunk/"+project.getName(),
	// false);
	// } catch (SVNException e) {
	// //logger.error(e.getMessage(),e);
	// return false;
	// }
	//
	// File ws = new File(new File(workspace), project.getName());
	// if(!SVNWCUtil.isVersionedDirectory(ws)){
	// SVNUtil.checkout(clientManager, repositoryURL, SVNRevision.HEAD, new
	// File(workspace), SVNDepth.INFINITY);
	// }else{
	// SVNUtil.update(clientManager, ws, SVNRevision.HEAD, SVNDepth.INFINITY);
	// }
	// return true;
	// }
  
    /**  
     * �ύ��Ŀ��SVN  
     * @param project  
     *          Project  
     * @return  
     */  
	// public boolean commitProjectToSvn(Project project) {
	// SVNClientManager clientManager = SVNUtil.authSvn(project.getSvnUrl(),
	// username, password);
	//
	// clientManager.getCommitClient().setEventHandler(new
	// CommitEventHandler());
	//
	// File wc_project = new File( workspace + "/" + project.getName());
	//
	// checkVersiondDirectory(clientManager,wc_project);
	//
	// SVNUtil.commit(clientManager, wc_project, false, "svnkit");
	//
	// return true;
	// }
      
    /**  
     * �ݹ��鲻�ڰ汾���Ƶ��ļ�����add��svn  
     * @param clientManager  
     * @param wc  
     */  
    private void checkVersiondDirectory(SVNClientManager clientManager,File wc){  
        if(!SVNWCUtil.isVersionedDirectory(wc)){  
            SVNUtil.addEntry(clientManager, wc);  
        }  
        if(wc.isDirectory()){  
            for(File sub:wc.listFiles()){  
                if(sub.isDirectory() || sub.getName().equals(".svn")){  
                    continue;  
                }  
                checkVersiondDirectory(clientManager,sub);  
            }  
        }  
    }  
      
	// private void checkWorkCopy(Project project){
	// project.setSvnUrl(rb.getString("svn.url"));
	// SVNClientManager clientManager = SVNUtil.authSvn(project.getSvnUrl(),
	// username, password);
	//
	// SVNURL repositoryURL = null; // trunk
	// try {
	// // eg: http://svn.ambow.com/wlpt/bsp
	// repositoryURL = SVNURL.parseURIEncoded(project.getSvnUrl())
	// .appendPath("trunk", false);
	// } catch (SVNException e) {
	// //logger.error(e.getMessage(),e);
	// }
	//
	// File wc = new File(workspace);
	// File wc_project = new File( workspace + "/" + project.getName());
	//
	// SVNURL projectURL = null; // projectName
	// try {
	// projectURL = repositoryURL.appendPath(project.getName(), false);
	// } catch (SVNException e) {
	// //logger.error(e.getMessage(),e);
	// }
	//
	// if(!SVNUtil.isWorkingCopy(wc)){
	// if(!SVNUtil.isURLExist(projectURL,username,password)){
	// SVNUtil.checkout(clientManager, repositoryURL, SVNRevision.HEAD, wc,
	// SVNDepth.EMPTY);
	// }else{
	// SVNUtil.checkout(clientManager, projectURL, SVNRevision.HEAD, wc_project,
	// SVNDepth.INFINITY);
	// }
	// }else{
	// SVNUtil.update(clientManager, wc, SVNRevision.HEAD, SVNDepth.INFINITY);
	// }
	// }
  
}  
