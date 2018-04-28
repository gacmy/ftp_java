import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTP {
	 //ftp��������ַ
    public String hostname = "127.0.0.1";
    //ftp�������˿ں�Ĭ��Ϊ21
    public Integer port = 21 ;
    //ftp��¼�˺�
    public String username = "gac";
    //ftp��¼����
    public String password = "123456";
    
    public FTPClient ftpClient = null;
    
    /**
     * ��ʼ��ftp������
     */
    public void initFtpClient() {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            System.out.println("connecting...ftp������:"+this.hostname+":"+this.port); 
            ftpClient.connect(hostname, port); //����ftp������
            ftpClient.login(username, password); //��¼ftp������
            int replyCode = ftpClient.getReplyCode(); //�Ƿ�ɹ���¼������
            if(!FTPReply.isPositiveCompletion(replyCode)){
                log("connect failed...ftp������:"+this.hostname+":"+this.port); 
            }
            System.out.println("connect successfu...ftp������:"+this.hostname+":"+this.port); 
        }catch (MalformedURLException e) { 
           e.printStackTrace(); 
        }catch (IOException e) { 
           e.printStackTrace(); 
        } 
    }

    /**
    * �ϴ��ļ�
    * @param pathname ftp���񱣴��ַ
    * @param fileName �ϴ���ftp���ļ���
    *  @param originfilename ���ϴ��ļ������ƣ����Ե�ַ�� * 
    * @return
    */
    public boolean uploadFile( String pathname, String fileName,String originfilename){
        boolean flag = false;
        InputStream inputStream = null;
        try{
            log("��ʼ�ϴ��ļ�");
            inputStream = new FileInputStream(new File(originfilename));
            initFtpClient();
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            CreateDirecroty(pathname);
            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            ftpClient.logout();
            flag = true;
            System.out.println("�ϴ��ļ��ɹ�");
        }catch (Exception e) {
            System.out.println("�ϴ��ļ�ʧ��");
            e.printStackTrace();
        }finally{
            if(ftpClient.isConnected()){ 
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            } 
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            } 
        }
        return true;
    }
    /**
     * �ϴ��ļ�
     * @param pathname ftp���񱣴��ַ
     * @param fileName �ϴ���ftp���ļ���
     * @param inputStream �����ļ��� 
     * @return
     */
    public boolean uploadFile( String pathname, String fileName,InputStream inputStream){
        boolean flag = false;
        try{
            System.out.println("��ʼ�ϴ��ļ�");
            initFtpClient();
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            CreateDirecroty(pathname);
            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            ftpClient.logout();
            flag = true;
            System.out.println("�ϴ��ļ��ɹ�");
        }catch (Exception e) {
            System.out.println("�ϴ��ļ�ʧ��");
            e.printStackTrace();
        }finally{
            if(ftpClient.isConnected()){ 
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            } 
            if(null != inputStream){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            } 
        }
        return true;
    }
    //�ı�Ŀ¼·��
     public boolean changeWorkingDirectory(String directory) {
            boolean flag = true;
            try {
                flag = ftpClient.changeWorkingDirectory(directory);
                if (flag) {
                  System.out.println("�����ļ���" + directory + " �ɹ���");

                } else {
                    System.out.println("�����ļ���" + directory + " ʧ�ܣ���ʼ�����ļ���");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return flag;
        }

    //�������Ŀ¼�ļ��������ftp�������Ѵ��ڸ��ļ����򲻴���������ޣ��򴴽�
    public boolean CreateDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote + "/";
        // ���Զ��Ŀ¼�����ڣ���ݹ鴴��Զ�̷�����Ŀ¼
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(path)) {
                    if (makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        System.out.println("����Ŀ¼[" + subDirectory + "]ʧ��");
                        changeWorkingDirectory(subDirectory);
                    }
                } else {
                    changeWorkingDirectory(subDirectory);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // �������Ŀ¼�Ƿ񴴽����
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

  //�ж�ftp�������ļ��Ƿ����    
    public boolean existFile(String path) throws IOException {
            boolean flag = false;
            FTPFile[] ftpFileArr = ftpClient.listFiles(path);
            if (ftpFileArr.length > 0) {
                flag = true;
            }
            return flag;
        }
    //����Ŀ¼
    public boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
            if (flag) {
                log("�����ļ���" + dir + " �ɹ���");

            } else {
               log("�����ļ���" + dir + " ʧ�ܣ�");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /** * �����ļ� * 
    * @param pathname FTP�������ļ�Ŀ¼ * 
    * @param filename �ļ����� * 
    * @param localpath ���غ���ļ�·�� * 
    * @return */
    public  boolean downloadFile(String pathname, String filename, String localpath){ 
        boolean flag = false; 
        OutputStream os=null;
        try { 
            log("��ʼ�����ļ�");
            initFtpClient();
            //�л�FTPĿ¼ 
            ftpClient.changeWorkingDirectory(pathname); 
            FTPFile[] ftpFiles = ftpClient.listFiles(); 
            for(FTPFile file : ftpFiles){ 
                if(filename.equalsIgnoreCase(file.getName())){ 
                    File localFile = new File(localpath + "/" + file.getName()); 
                    os = new FileOutputStream(localFile); 
                    ftpClient.retrieveFile(file.getName(), os); 
                    os.close(); 
                } 
            } 
            ftpClient.logout(); 
            flag = true; 
            System.out.println("�����ļ��ɹ�");
        } catch (Exception e) { 
            System.out.println("�����ļ�ʧ��");
            e.printStackTrace(); 
        } finally{ 
            if(ftpClient.isConnected()){ 
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            } 
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            } 
        } 
        return flag; 
    }
    
    /** * ɾ���ļ� * 
    * @param pathname FTP����������Ŀ¼ * 
    * @param filename Ҫɾ�����ļ����� * 
    * @return */ 
    public boolean deleteFile(String pathname, String filename){ 
        boolean flag = false; 
        try { 
            System.out.println("��ʼɾ���ļ�");
            initFtpClient();
            //�л�FTPĿ¼ 
            ftpClient.changeWorkingDirectory(pathname); 
            ftpClient.dele(filename); 
            ftpClient.logout();
            flag = true; 
            System.out.println("ɾ���ļ��ɹ�");
        } catch (Exception e) { 
            System.out.println("ɾ���ļ�ʧ��");
            e.printStackTrace(); 
        } finally {
            if(ftpClient.isConnected()){ 
                try{
                    ftpClient.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }
            } 
        }
        return flag; 
    }
    
    public static void main(String[] args) {
    	run(args);
    }
    
    public static void run(String[] args) {
    	Scanner scanner = new Scanner(System.in);
    	FTP ftp = new FTP();
    	log("please input oreateType (d->download,u->upload)");
    	String serverFilePath;
    	String serverFileName;
    	String localPath;
    	String text = scanner.nextLine();
    	if(text.equals("u")) {
    		log("please input serverFilePath:");
    		serverFilePath = scanner.nextLine();
    		log("please input serverFileName:");
    		serverFileName = scanner.nextLine();
    		log("please input local file path:");
    		localPath = scanner.nextLine();
    		ftp.uploadFile(serverFilePath, serverFileName,localPath);
    	}
    	
//    	if(args != null && args.length == 3) {
//    		if(args[0].equals("del")) {
//    			 ftp.deleteFile(args[1],args[2]);
//    		}
//    		return;
//    	}
//    	if(args == null || args.length != 4) {
//    		log("args:operate type(d->download,u->upload),server->filePath,server->filename,client->file absolute path");
//    		return;
//    	}
//    	
//    	if(args[0].equals("u")) {
//    		ftp.uploadFile(args[1], args[2], args[3]);
//    	}else if(args[0].equals("d")) {
//    		ftp.downloadFile(args[1], args[2], args[3]);
//    	}else if(args[0].equals("del")) {
//    		ftp.deleteFile("ftpFile/data", "123.docx");
//    	}
    	
    	
//        ftp.uploadFile("ftpFile/data", "123.docx", "E://123.docx");
//        //ftp.downloadFile("ftpFile/data", "123.docx", "F://");
//        ftp.deleteFile("ftpFile/data", "123.docx");
      log("done");
    }
    private static void log(String info) {
    	System.out.println("******************************");
    	System.out.println("info:"+info);
    	System.out.println("******************************");
    	
    }
}
